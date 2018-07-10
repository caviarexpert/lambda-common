package com.itranga.cav.data;

import java.math.BigDecimal;
import java.util.Optional;

import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;

public class TaxesDAO {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(TaxesDAO.class);
	public static final String taxesTable = "taxes";
	//TODO get VAT from configuration
	private static BigDecimal defaultVAT = new BigDecimal(0.2);
	public static BigDecimal getVAT(String countryCode){
		if(countryCode == null){
			LOG.warn("Attempts to get VAT for NULL country code");
			return defaultVAT;
		}
		String query = "select * from %s where CountryCode = '%s'";
		SelectRequest selectRequest = new SelectRequest(String.format(query, taxesTable, countryCode));
		Optional<Item> result = SimpleDBManager.getSimpleDb()
			.select(selectRequest).getItems().stream().findFirst();
		if(result.isPresent()){
			Optional<BigDecimal> optVAT = result.get().getAttributes().stream()
				.filter( attr -> attr.getName().equals("VAT"))
				.map( attr -> attr.getValue())
				.map( vatString -> new BigDecimal(vatString))
				.findFirst();
			if(optVAT.isPresent()){
				return optVAT.get();
			}
		}
		return defaultVAT;
	}

}
