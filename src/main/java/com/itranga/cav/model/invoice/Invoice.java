package com.itranga.cav.model.invoice;

import java.math.BigDecimal;
import java.util.List;

import com.itranga.cav.model.postmen.Quotation;
import com.itranga.cav.model.postmen.UpuAddress;

public class Invoice {
	
	private String id;
	private UpuAddress shippingAddress;
	private Quotation shippingQuotation;
	private List<CartLine> cart;
	private String paymentId;
	private BigDecimal itemsTotalAmount;
	private BigDecimal itemsTaxAmount;
	private BigDecimal totalAmount;
	private BigDecimal vat;

	public UpuAddress getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(UpuAddress shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public Quotation getShippingQuotation() {
		return shippingQuotation;
	}

	public void setShippingQuotation(Quotation shippingQuotation) {
		this.shippingQuotation = shippingQuotation;
	}

	public List<CartLine> getCart() {
		return cart;
	}

	public void setCart(List<CartLine> cart) {
		this.cart = cart;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getItemsTotalAmount() {
		return itemsTotalAmount;
	}

	public void setItemsTotalAmount(BigDecimal itemsTotalAmount) {
		this.itemsTotalAmount = itemsTotalAmount;
	}

	public BigDecimal getVat() {
		return vat;
	}

	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}

	public BigDecimal getItemsTaxAmount() {
		return itemsTaxAmount;
	}

	public void setItemsTaxAmount(BigDecimal itemsTaxAmount) {
		this.itemsTaxAmount = itemsTaxAmount;
	}

	public static class CartLine {
		private String sku;
		private String itemName;
		private String itemDescription;
		private java.math.BigDecimal quantity;
		private java.math.BigDecimal price;
		private String currency;
		public String getSku() {
			return sku;
		}
		public void setSku(String sku) {
			this.sku = sku;
		}
		public java.math.BigDecimal getQuantity() {
			return quantity;
		}
		public void setQuantity(java.math.BigDecimal quantity) {
			this.quantity = quantity;
		}
		public java.math.BigDecimal getPrice() {
			return price;
		}
		public void setPrice(java.math.BigDecimal price) {
			this.price = price;
		}
		public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public String getItemDescription() {
			return itemDescription;
		}
		public void setItemDescription(String itemDescription) {
			this.itemDescription = itemDescription;
		}
		
	}
}
