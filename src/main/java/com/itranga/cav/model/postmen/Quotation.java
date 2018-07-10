package com.itranga.cav.model.postmen;

public class Quotation {
	
	private String deliveryDate;
	private String serviceName;
	private String priceCurrency;
	private float price;
	private String courierSlug;

	public String getDeliveryDate() {
		return deliveryDate;
	}


	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public String getPriceCurrency() {
		return priceCurrency;
	}


	public void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}

	public String getCourierSlug() {
		return courierSlug;
	}

	public void setCourierSlug(String courierSlug) {
		this.courierSlug = courierSlug;
	}
	
	

}
