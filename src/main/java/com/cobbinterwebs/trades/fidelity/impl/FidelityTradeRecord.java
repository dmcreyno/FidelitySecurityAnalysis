package com.cobbinterwebs.trades.fidelity.impl;
////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.TradeRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class FidelityTradeRecord extends TradeRecord {

	private static final Logger log = LogManager.getLogger("com.cobbinterwebs.fidelity.FidelityTradeRecord");
	
	public final static class Indexes {
		static final int TIME_STAMP = 0;
		static final int LAST_PRICE = 1;
		static final int LAST_SIZE = 2;
		static final int BID_PRICE = 3;
		static final int ASK_PRICE = 4;
//		static final int BID_SIZE = 5;
//		static final int ASK_SIZE = 6;
//		static final int BID_XCHANGE = 7;
//		static final int ASK_XCHANGE = 8;
//		static final int LAST_XCHANGE = 9;
		static final int CONDITION = 10;
	}
	
	/**
	 * "Time","Last Price","Last Size","Bid Price","Ask Price","Bid Size","Ask Size","Bid Exchange","Ask Exchange","Last Exchange","Trade Condition"
	 */
	public FidelityTradeRecord(String pData) {
		super(pData, ',');
		
		log.trace(DisplayKeys.get(DisplayKeys.LOG_PARSING), pData);
		
		timeStr = super.rawTokens.get(Indexes.TIME_STAMP);
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
}
