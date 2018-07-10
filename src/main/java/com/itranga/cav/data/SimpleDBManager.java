package com.itranga.cav.data;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClientBuilder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class SimpleDBManager {
	private static AmazonSimpleDB simpleDb;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static DecimalFormat decimalFormat = new DecimalFormat();
	private static final String decimalPattern = "000000.00";
	static {
		String CONFIG_ATTR = "SIMPLEDB_REGION";
		String AWS_REGION = System.getProperty(CONFIG_ATTR);
		if( AWS_REGION == null ){
			AWS_REGION = System.getenv(CONFIG_ATTR);
			if( AWS_REGION == null ){
				throw new Error(CONFIG_ATTR + " property or environment is NULL");
			}
		}
		AmazonSimpleDBClientBuilder builder = AmazonSimpleDBClientBuilder.standard();
		builder.setRegion(AWS_REGION);
		simpleDb = builder.build();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);
		decimalFormat.applyPattern(decimalPattern);
		SimpleModule module = new SimpleModule();
		module.addSerializer(BigDecimal.class, new MoneySerializer());
		mapper.registerModule(module);
	}
	/**
	 * @throws IllegalStateException if {@link #init(String)} is not called before
	 * @return simpleDb instance;
	 */
	public static AmazonSimpleDB getSimpleDb(){
		if( simpleDb == null ) throw new IllegalStateException("SimpleDB is not initialized. Call com.itranga.cav.data.SimpleDBManager.init(region)");
		return simpleDb;
	}
	/**
	 * @throws IllegalArgumentException if the Format cannot format the given object
	 * @param value decimal value to be padded
	 * @return left padded decimal as 000000.00
	 */
	public static String formatMoney(Object value){
		return decimalFormat.format(value);
	}
	public static ObjectMapper getMapper(){
		return mapper;
	}
	
	public static String getDecimalPattern(){
		return decimalPattern;
	}
	
	private static class MoneySerializer extends JsonSerializer<BigDecimal> {
		@Override
		public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			// put your desired money style here
			jgen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		}
	}
}
