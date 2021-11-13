package com.cobbinterwebs.trades.fidelity.impl;
////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.TradeRecord;

/**
 * Class representing a trade as defined in the CSV file from Fidelity Investments.
 * 
 * <h2>Positions and Names</h2>
 * <ol>
 * <li>Time</li>
 * <li>Last Price</li>
 * <li>Last Size</li>
 * <li>Bid Price</li>
 * <li>Ask Price</li>
 * <li>Bid Size</li>
 * <li>Ask Size</li>
 * <li>Bid Exchange</li>
 * <li>Ask Exchange</li>
 * <li>Last Exchange</li>
 * <li>Trade Condition</li>
 * <ol>
 * @author Cobb Interwebs, LLC
 * @see com.cobbinterwebs.trades.TradeRecord
 */
public class FidelityTradeRecord extends TradeRecord {

	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.fidelity.FidelityTradeRecord");
	
	private Calendar cal = new GregorianCalendar();
	/*
	 * DELEGATES for the calendar. Might be overkill. At this time (06NOV2021) I
	 * think there are a few that I can absolutely justify. I am deviating from
	 * agile methodology only because it is so easy to generate these with the
	 * click of a button. When I finish implementing the wavelet  feature,
	 * I will revisit and delete/deprecate the unused.
	 */
	
	/**
	 * @see java.util.Calendar
	 */
	public final Date getTime() {
		return cal.getTime();
	}

	/**
	 * @see java.util.Calendar
	 */
	public final void setTime(Date date) {
		cal.setTime(date);
	}

	/**
	 * @see java.util.Calendar
	 */
	public long getTimeInMillis() {
		return cal.getTimeInMillis();
	}

	/**
	 * @see java.util.Calendar
	 */
	public void setTimeInMillis(long millis) {
		cal.setTimeInMillis(millis);
	}

	/**
	 * @see java.util.Calendar
	 */
	public int get(int field) {
		return cal.get(field);
	}

	/**
	 * @see java.util.Calendar
	 */
	public void set(int field, int value) {
		cal.set(field, value);
	}

	/**
	 * @see java.util.Calendar
	 */
	public final void set(int year, int month, int date) {
		cal.set(year, month, date);
	}

	/**
	 * @see java.util.Calendar
	 */
	public final void set(int year, int month, int date, int hourOfDay, int minute) {
		cal.set(year, month, date, hourOfDay, minute);
	}

	/**
	 * @see java.util.Calendar
	 */
	public final void set(int year, int month, int date, int hourOfDay, int minute, int second) {
		cal.set(year, month, date, hourOfDay, minute, second);
	} /////////////////////////////////////		
	///////////////////////////////////////
	/////// END OF CALENDAR DELEGATES /////
	///////////////////////////////////////	

	public static int HOUR = 0;
	public static int SECOND = 1;
	public static int MINUTE = 2;
	
	/**
	 * The indexes used when parsing the csv data record.
	 * @author Cobb Interwebs, LLC
	 *
	 */
	public final static class Indexes {
		static final int TIME_STAMP = 0;
		static final int LAST_PRICE = 1;
		static final int LAST_SIZE = 2;
		static final int BID_PRICE = 3;
		static final int ASK_PRICE = 4;
		static final int CONDITION = 10;
	}
	
	/**
	 * CTOR
	 * @param a String containing a line from the file of trade records.
	 */
	public FidelityTradeRecord(String pData) {
		super(pData, ',');
		
		log.trace(DisplayKeys.get(DisplayKeys.LOG_PARSING), pData);
		
		timeStr = super.rawTokens.get(Indexes.TIME_STAMP);
		int[] timeArray = convertDateString(timeStr);
		cal.set(Calendar.HOUR_OF_DAY, timeArray[FidelityTradeRecord.HOUR]);
		cal.set(Calendar.MINUTE, timeArray[FidelityTradeRecord.MINUTE]);
		cal.set(Calendar.SECOND, timeArray[FidelityTradeRecord.SECOND]);
		
		price = new BigDecimal(super.rawTokens.get(Indexes.LAST_PRICE),mathCtx);
		size = new BigDecimal(super.rawTokens.get(Indexes.LAST_SIZE),mathCtx);
		try {
			bid = new BigDecimal(super.rawTokens.get(Indexes.BID_PRICE), mathCtx);
		} catch(NumberFormatException nfex) {
			log.debug("Trade record has no bid info. Data: {}", pData);
		}
		
		try {
			ask = new BigDecimal(super.rawTokens.get(Indexes.ASK_PRICE), mathCtx);
		} catch(NumberFormatException nfex) {
			log.debug("Trade trade has no ask info. Data: {}", pData);
		}
		
		super.tTrade = "T".equals(super.rawTokens.get(Indexes.CONDITION));
	}

	/**
	 * It will return the trade time as an
	 * array of ints. This is useful for the time aspects of the jfreechart
	 * library which has it's own representation of time.
	 * @param pDateStr The time string as read from the trade csv file.
	 * @return array of integers (int) hours[0], min[1], sec[2]
	 */
	protected static int[] convertDateString(String pDateStr) {
		String dateString = pDateStr.substring(0,8);
		StringTokenizer strtok = new StringTokenizer(dateString, ":");
		int hr = Integer.parseInt(strtok.nextToken());
		int min = Integer.parseInt(strtok.nextToken());
		int sec = Integer.parseInt(strtok.nextToken());
				
		int[] rVal = new int[3];
		rVal[FidelityTradeRecord.HOUR] = hr;
		rVal[FidelityTradeRecord.MINUTE] = min;
		rVal[FidelityTradeRecord.SECOND] = sec;
		
		return rVal;
	}
	
	
}
