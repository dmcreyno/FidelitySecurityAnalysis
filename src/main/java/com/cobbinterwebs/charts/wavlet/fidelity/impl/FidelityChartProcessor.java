package com.cobbinterwebs.charts.wavlet.fidelity.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cobbinterwebs.base.ConfigurationAware;
import com.cobbinterwebs.chart.wavelet.IChartFileReader;
import com.cobbinterwebs.chart.wavelet.IChartRecord;
import com.cobbinterwebs.locale.DisplayKeys;
import com.cobbinterwebs.trades.config.Configuration;

/**
 * 
 * @author Cobb Interwebs, LLC
 *
 */
public class FidelityChartProcessor implements IChartFileReader, ConfigurationAware {
	
	static final Logger log = LogManager.getLogger("om.cobbinterwebs.chart.wavelet.fidelity.impl.FidelityChartProcessor");
	List<IChartRecord> chartRecordList = new ArrayList<>();
	
	
	protected File aFile;
    /**
     * The properties file used to control aspects of the ticker being analyzed. Multiple tickers are
     * processed and each can be configured to have different properties, rounding, precision, etc. The TradeDay
     * needs this information to control maths.
     */
    protected Configuration config;

    public FidelityChartProcessor(File pFile, Configuration pConfig) {
    	aFile = pFile;
    	config = pConfig;
	}

    public FidelityChartProcessor(File pFile) {
    	aFile = pFile;
	}
    
	@Override
    public void process() {
        FidelityCSVInputReader csvInputReader = new FidelityCSVInputReader(aFile);
        try {
            csvInputReader.initFile();
        } catch (IOException e) {
            log.error("reader initiation failed.",e);
            System.exit(-1);
        }
        try {
            String currentLine = csvInputReader.readLine();
            log.trace("throwing away header [{}]",currentLine);
            long lineCounter = 1L;
            boolean done = false;
            while(!done) {
                if ((currentLine = csvInputReader.readLine()) == null
                		|| currentLine.startsWith("\"The data and information")) {
                    done = true;
                } else {

                    if (log.isTraceEnabled()) log.trace("LINE: {}", currentLine);

                    try {
                        IChartRecord tr = IChartRecord.create(currentLine);
                        if (log.isDebugEnabled()) log.debug("adding a trade . . . {}", tr);
                        if(null == tr) {
                            Exception e = new IllegalStateException();
                            log.fatal("BOOM!!!", e);
                            System.exit(-1);
                        }
                        chartRecordList.add(tr);
                    } catch (Exception e) {
                        log.error("error processing line {} in file {}", lineCounter,aFile.getName());
                        log.error("error processing data, \"{}\"", currentLine, e);
                        return;
                    }
                } // end if check for end of file
                lineCounter++;
            } // end while not done
            log.info("loaded {} records for chart processing.",lineCounter);
        } catch (IOException e) {
            log.error("reading file failed: {}", aFile.getAbsolutePath(), e);
        } finally {
            csvInputReader.close();
        }
    }
	
    /**
     * Wrapper around a buffered reader. While there is not much value in wrapping that class
     * this class will skip the summary header info Fidelity puts in their exports.
     */
    public class FidelityCSVInputReader {
        private final Logger log = LogManager.getLogger(FidelityCSVInputReader.class);
        private BufferedReader reader;
        private String dateStr;
        private final File file;

        /**
         * CTOR accepting an instance of a File .
         * @param pFile the file to read from.
         */
        public FidelityCSVInputReader(File pFile) {
            file=pFile;
        }

        void initFile() throws IOException {
            log.info("Reading file {}", file.getCanonicalFile());
            reader = new BufferedReader(new FileReader(file));
        }

        String getDate() {
            return this.dateStr;
        }

        String readLine() throws IOException {
            return reader.readLine();
        }

        void close() {
            try {
                reader.close();
            } catch (Exception e) {
                log.error(DisplayKeys.get(DisplayKeys.ERROR_FILE_CLOSE),file.getAbsolutePath(), e);
            }
        }
    }
	

}
