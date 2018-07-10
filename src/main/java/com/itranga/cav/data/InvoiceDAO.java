package com.itranga.cav.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.itranga.cav.model.invoice.Invoice;

public class InvoiceDAO {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(InvoiceDAO.class);
	public static final String invoiceTable = "invoices";
	private static final MathContext mc = new MathContext(10);
	
	private static void put(Invoice invoice, String itemName) throws JsonProcessingException{
		if(invoice == null) throw new IllegalArgumentException("invocice is NULL");
		
		String json = SimpleDBManager.getMapper().writeValueAsString(invoice);
		
		PutAttributesRequest put = new PutAttributesRequest();
		put.setDomainName(invoiceTable);
		boolean isUpdate = itemName != null;
		String invoiceId = isUpdate ? itemName : UUID.randomUUID().toString();
		put.setItemName(invoiceId);
		
		List<ReplaceableAttribute> attributes = new ArrayList<>();
		
		//add sku mulit-value attribute
		List<ReplaceableAttribute> skus = invoice.getCart().stream().map( cartLine -> {
			return new ReplaceableAttribute("sku", cartLine.getSku(), isUpdate);
		}).collect( Collectors.toList() );
		
		
		BigDecimal itemsTotalAmount = invoice.getCart().stream()
				.map( cartLine -> cartLine.getPrice().multiply(cartLine.getQuantity(), mc))
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.setScale(2, BigDecimal.ROUND_CEILING);
		attributes.add(new ReplaceableAttribute("itemsTotalAmount", SimpleDBManager.formatMoney(itemsTotalAmount), isUpdate));
		attributes.add(new ReplaceableAttribute("content", json, isUpdate));
		
		attributes.addAll(skus);
		
		
		put.withAttributes(attributes);
		SimpleDBManager.getSimpleDb().putAttributes(put);
	}
	
	public static Invoice save(Invoice invoice) throws Exception{
		put(invoice, null);
		return invoice;
	}
	public static Optional<Invoice> toInvoice(Item item){
		Optional<Attribute> jsonAttr = item.getAttributes().stream()
			.filter( attr -> attr.getName().equals("content"))
			.findFirst();
		if(jsonAttr.isPresent()){
			try {
				Invoice invoice = SimpleDBManager.getMapper().readValue(jsonAttr.get().getValue(), Invoice.class);
				fillInvoice(invoice, item);
				return Optional.of(invoice);
			} catch (IOException e) {
				LOG.error("Failed to deserialize invoice "+ item.getName());
			}
		}
		return Optional.empty();
	}
	private static Invoice fillInvoice(Invoice invoice, Item item){
		invoice.setId(item.getName());
		item.getAttributes().forEach( attr -> {
			if(attr.getName().equals("itemsTotalAmount")){
				invoice.setItemsTotalAmount(new BigDecimal(attr.getValue()));
			}
		});
		return invoice;
	}
	
	public static Optional<Invoice> getInvoice(String id){
		if(id==null) return Optional.empty();
		String query = "select * from %s where itemName() = '%s'";
		SelectRequest selectRequest = new SelectRequest(String.format(query, invoiceTable, id));
		Optional<Item> result = SimpleDBManager.getSimpleDb()
			.select(selectRequest).getItems().stream().findFirst();
		if(result.isPresent()){
			return toInvoice(result.get());
		}
		return Optional.empty();
	}
}
