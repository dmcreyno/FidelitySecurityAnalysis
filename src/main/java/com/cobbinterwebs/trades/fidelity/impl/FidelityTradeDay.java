package com.cobbinterwebs.trades.fidelity.impl;

////////////////////////////////////////////////////////////////////////////////
// Copyright 2021 Cobb Interwebs, LLC
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////////////////////////////////////////////////////////////////////////////////

import com.cobbinterwebs.trades.ITradeRecord;
import com.cobbinterwebs.trades.config.Configuration;
import com.cobbinterwebs.locale.DisplayKeys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Reads the data for one day of trading and stores the stats. Keeps the trades
 * in a list. Also, puts the dollar volume into <i>buckets</i> which are
 * defined in the configuration properties files.
 * TODO do not keep the trades in memory.
 */
public class FidelityTradeDay extends com.cobbinterwebs.trades.TradeDay {
    private static final Logger log = LogManager.getLogger(FidelityTradeDay.class);
    
    /**
     * The data comes as a CSV of trades for one day.
     */
    public FidelityTradeDay(File pFile, Configuration pConfig) {
    	super(pFile,pConfig);
     }

    /**
     * Reads the File for the day. Puts the trade dollar-volume in the
     * appropriate bucket.
     */
    @Override
    public void process() {
        FidelityCSVInputReader csvInputReader = new FidelityCSVInputReader(aFile);
        try {
            csvInputReader.initFile();
            dateStr = csvInputReader.getDate();
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
                        || currentLine.startsWith("\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",")) {
                    done = true;
                } else {

                    if (log.isTraceEnabled()) log.trace("LINE: {}", currentLine);

                    try {
                        ITradeRecord tr = ITradeRecord.create(currentLine);
                        if (log.isDebugEnabled()) log.debug("adding a trade . . . {}", tr);
                        if(null == tr) {
                            Exception e = new IllegalStateException();
                            log.fatal("BOOM!!!", e);
                            System.exit(-1);
                        }
                        this.tradeList.add(tr);
//                        distributeToBucket(tr);
                    } catch (Exception e) {
                        log.error("error processing line {} in file {}", lineCounter,aFile.getName());
                        log.error("error processing data, \"{}\"", currentLine, e);
                    }
                } // end if check for end of file
                lineCounter++;
            } // end while not done
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
        private final int LINE_NO_DATE = config.getDateLineNumber();
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
            // throw away the first few lines (as set by getHeaderSkipLineCount)
            for (int i = 0; i < config.getHeaderSkipLineCount(); i++) {
                String line = reader.readLine();
                if(i == LINE_NO_DATE) { // the date line number. Date is read from file.
                    log.debug(DisplayKeys.get(DisplayKeys.PROCESSING_FILE_DATE), line);
                    dateStr = line;
                }
            }
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

    public String getDebugString() {
        String delimiter = "|";
    
        return this.getDayOrdinal() + delimiter +
                this.getDateStr() + delimiter +
                this.getVolume() + delimiter +
                this.getBuyVolume() + delimiter +
                this.getSellVolume() + delimiter +
                this.getUnknownVolume() + delimiter +
                this.getDollarVolume() + delimiter +
                this.getBuyDollarVolume() + delimiter +
                this.getSellDollarVolume() + delimiter +
                this.getUnknownDollarVolume() + "FidelityTradeDay[Date=" + getDateStr() + ", " +
                "Volume=" + getVolume() + ", " +
                "BuyVolume=" + getBuyVolume() + ", " +
                "SellVolume=" + getSellVolume() + ", " +
                "UnknownVolume=" + getUnknownVolume() + ", " +
                "DollarVolume=" + getDollarVolume() + ", " +
                "BuyDollarVolume=" + getBuyDollarVolume() + ", " +
                "SellDollarVolume=" + getSellDollarVolume() + ", " +
                "UnknownDollarVolume=" + getUnknownDollarVolume() + ", " +
                "TeeTrade=" + getTeeTradeCount() + "]";
    }

}
