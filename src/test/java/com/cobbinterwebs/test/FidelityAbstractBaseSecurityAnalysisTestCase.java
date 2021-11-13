/**
 * 
 */
package com.cobbinterwebs.test;

import com.cobbinterwebs.trades.config.Configuration;

/**
 * @author Cobb Interwebs, LLC
 *
 */
public abstract class FidelityAbstractBaseSecurityAnalysisTestCase {

	/**
	 * @param baseDir corresponds the application's value for com.cobbinterwebs.trades.home.
	 */
	public FidelityAbstractBaseSecurityAnalysisTestCase() {
		
	}

	/**
	 * @param baseDir corresponds the application's value for com.cobbinterwebs.trades.home.
	 */
	protected void setHome(String baseDir) {
		System.setProperty(Configuration.PropertyConstants.HOME_KEY, baseDir);
	}
}
