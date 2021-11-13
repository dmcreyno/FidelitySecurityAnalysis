package com.cobbinterwebs.charts.wavelet.fidelity.impl;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.cobbinterwebs.charts.wavlet.fidelity.impl.FidelityChartProcessor;
import com.cobbinterwebs.test.FidelityAbstractBaseSecurityAnalysisTestCase;

public class FidelityChartProcessorTestCase extends FidelityAbstractBaseSecurityAnalysisTestCase{
	@Before
	public void beforeClass() {
		super.setHome("test-data");
	}

	@Test
	public void testProcess() {
		//C:\Users\Trader\Workspaces\CobbInterwebs\FidelitySecurityAnalysis
		File testDataFile = new File("test-data/DWAC/charInput/20211108-1D-5MIN.csv");
		FidelityChartProcessor p = new FidelityChartProcessor(testDataFile);
		
		try {
			p.process();
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}

}
