package com.cobbinterwebs.trades.fidelity.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.cobbinterwebs.trades.ITradeRecord;

import static org.junit.Assert.assertEquals;

public class FidelityTradeRecordTestsCase {
	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.trades.fidelity.impl.FidelityTradeRecordTestsCase");
	
	String csvRawRecord = "\"09:30:00 ET\",\"125.03\",\"100\",\"125.00\",\"125.06\",\"1\",\"2\",\"Z\",\"N\",\"IX\",\"--\"";
	String csvRawRecordTeeTrade = "\"09:30:00 ET\",\"125.03\",\"100\",\"125.00\",\"125.06\",\"1\",\"2\",\"Z\",\"N\",\"IX\",\"T\"";
	
	FidelityTradeRecord testRecord;
	Calendar cal;
	
	@Before public void parseRecord() {
		testRecord = new FidelityTradeRecord(csvRawRecord);
		cal = new GregorianCalendar();
		cal.setTime(testRecord.getTime());
	}
	

	@Test
	public void testGetTime() {
		assertEquals(9, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(30, cal.get(Calendar.MINUTE));
		assertEquals(0, cal.get(Calendar.SECOND));
	}


	@Test
	public void testSentiment() {
		assertEquals(ITradeRecord.BuySell.UNKNOWN, testRecord.sentiment());
	}

	@Test
	public void testGetDollarVolume() {
		assertEquals(new BigDecimal("12503.00"), testRecord.getDollarVolume());
	}

	@Test
	public void testGetPrice() {
		assertEquals(new BigDecimal("125.03"), testRecord.getPrice());
	}

	@Test
	public void testGetSize() {
		assertEquals(new BigDecimal("100"), testRecord.getSize());
	}

	@Test
	public void testIsTeeTrade() {
		String isNotTeeTrade = "\"09:30:00 ET\",\"125.03\",\"100\",\"125.00\",\"125.06\",\"1\",\"2\",\"Z\",\"N\",\"IX\",\"--\"";
		String isTeeTrade = "\"09:30:00 ET\",\"125.03\",\"100\",\"125.00\",\"125.06\",\"1\",\"2\",\"Z\",\"N\",\"IX\",\"T\"";
		FidelityTradeRecord tradeRecord = new FidelityTradeRecord(isNotTeeTrade);
		assertEquals(Boolean.FALSE, tradeRecord.isTeeTrade());
		tradeRecord = new FidelityTradeRecord(isTeeTrade);
		assertEquals(Boolean.TRUE, tradeRecord.isTeeTrade());
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
