/**
 * 
 */
package com.cobbinterwebs.charts.wavlet.fidelity.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.cobbinterwebs.chart.wavelet.AbstractChartRecord;

/**
 * This clss understands how to parse a single line from the CSV file; the data is a chart record.
 * @author davidmcreynolds
 *
 */
public class FidelityChartRecord extends AbstractChartRecord {

	/**
	 * @param csvRecord
	 */
	public FidelityChartRecord(String csvRecord) {
		super(csvRecord);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param highPrice
	 * @param lowPrice
	 * @param closePrice
	 * @param volume
	 * @param dateTime
	 */
	public FidelityChartRecord(BigDecimal highPrice, BigDecimal lowPrice, BigDecimal closePrice, BigInteger volume,
			Date dateTime) {
		super(highPrice, lowPrice, closePrice, volume, dateTime);
		// TODO Auto-generated constructor stub
	}

}
