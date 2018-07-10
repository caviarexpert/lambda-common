package com.itranga.aws.lambda.tests;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DigitPadding {
	
	@Test
	public void paddingDigits(){
		String value = "23.45";
		BigDecimal number = new BigDecimal("23.45");
		//number = number.setScale(2, RoundingMode.CEILING);
		// String.format("%06f", number)) -> 23,450000
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("000000.00", otherSymbols);
		String textPrice = df.format(number);
		assertThat(textPrice).as("Checks %s is 0000%s", value, value)
			.isEqualTo(String.format("0000%s", value));
	}
}
