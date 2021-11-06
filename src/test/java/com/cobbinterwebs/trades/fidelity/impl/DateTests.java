package com.cobbinterwebs.trades.fidelity.impl;

import static org.junit.Assert.assertEquals;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.junit.Test;

import com.cobbinterwebs.trades.fidelity.impl.FidelityTradeRecord;

public class DateTests {

	public DateTests() {
		// TODO Auto-generated constructor stub
	}

	@Test
	public void dateStringConversionTest() {
		String testString = "15:59:43 ET";
		int[] tradeTimeArray = FidelityTradeRecord.convertDateString(testString);
		
		assertEquals(15, tradeTimeArray[FidelityTradeRecord.HOUR]);
		assertEquals(59, tradeTimeArray[FidelityTradeRecord.MINUTE]);
		assertEquals(43, tradeTimeArray[FidelityTradeRecord.SECOND]);
	}
	
}
