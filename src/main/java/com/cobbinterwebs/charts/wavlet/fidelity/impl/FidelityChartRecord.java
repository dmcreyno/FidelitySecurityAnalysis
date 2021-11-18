/**
 * 
 */
package com.cobbinterwebs.charts.wavlet.fidelity.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.text.StringTokenizer;

import com.cobbinterwebs.chart.wavelet.AbstractChartRecord;
import com.cobbinterwebs.locale.DisplayKeys;

/**
 * This class understands how to parse a single line from the CSV file; the data is a chart record.
 * @author Cobb Interwebs, LLC
 * 
 * Date,Time,Open,High,Low,Close,Volume
 *
 */
public class FidelityChartRecord extends AbstractChartRecord {
	public final static class Indexes {
		static final int DATE = 0;
		static final int TIME = 1;
		static final int OPEN = 2;
		static final int HIGH = 3;
		static final int LOW = 4;
		static final int CLOSE = 5;
		static final int VOLUME = 6;
	}

    public FidelityChartRecord(String pData) {
    	super(pData,',');
    	
    	super.openPrice = new BigDecimal(rawTokens.get(Indexes.OPEN));
    	super.highPrice = new BigDecimal(rawTokens.get(Indexes.HIGH));
    	super.lowPrice = new BigDecimal(rawTokens.get(Indexes.LOW));
    	super.closePrice = new BigDecimal(rawTokens.get(Indexes.CLOSE));
    	super.volume = new BigInteger(rawTokens.get(Indexes.VOLUME));
    
    }


}
