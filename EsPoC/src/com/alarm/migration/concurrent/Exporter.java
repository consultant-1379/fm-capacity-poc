package com.alarm.migration.concurrent;

import java.util.Date;

import com.alarm.migration.Util;

public class Exporter implements Runnable{

	private int id;
	private String exportTempDir;
	private int numOfRecsToExportPerIteration;
	private String solrHost;
	private String solrCore;
	private Monitor monitor;
	
	public Exporter(int id, String solrHost, String solrCore, String exportTempDir, 
			int numOfRecsToExportPerIteration, Monitor monitor) {
		this.id = id;
		this.solrHost = solrHost;
		this.solrCore = solrCore;
		this.exportTempDir = exportTempDir;
		this.numOfRecsToExportPerIteration = numOfRecsToExportPerIteration;
		this.monitor = monitor;
		
	}
	
	@Override
	public void run() {
		
		String timeRange;
		long numOfRecordsInTimeRange;
		int pageNo;
		long iterationStartTime;
		int startIndex;
		String solrDataFileNamePrefix;
		do {
			timeRange = monitor.getTimeRangeForExporting(id);
			if(timeRange == null){
				log("No more time range elements for this thread.");
				
				if(monitor.isRetryPhase){
					log("Exporter thread [" + id + "] is exiting.");
					monitor.exporterExiting();
					break;
				}
				
				/*Wait for export map to become empty and also Wait for remaining exported 
				files (if any) to get converted and then continue with retry*/
				monitor.waitForExportCompletion(exportTempDir, id);
				Util.sleep(5);
				monitor.prepareFailedTimeRangesForRetry(id);
				continue;
			}
			
			if(!monitor.isRetryPhase){
				pageNo = 1;
				startIndex = 0;
				
				numOfRecordsInTimeRange = Util.getNumOfRecordsInTimeRange(id, solrHost, solrCore, timeRange);
				if (numOfRecordsInTimeRange == 0) {
					log("No records found for time range " + timeRange);
					continue;
				} else {
					log("Going to migrate " + numOfRecordsInTimeRange + " records of time range " + timeRange);
				}
			}
			else{
				//e.g timeRange from failedTimeRanges: "2020-01-19T06:00:00.000Z TO 2020-01-19T06:59:59.999Z_5"
				String[] parts = timeRange.split("_");
				timeRange = parts[0];
				pageNo = Integer.parseInt(parts[1]);
				startIndex = (pageNo - 1) * numOfRecsToExportPerIteration;
				//Set numOfRecordsInTimeRange to greater than startIndex to get into the loop
				numOfRecordsInTimeRange = startIndex + 1; 
			}
			
			//e.g timeRange: "2020-01-19T06:00:00.000Z TO 2020-01-19T06:59:59.999Z"
			solrDataFileNamePrefix = exportTempDir + "/solrData_" + timeRange.substring(0, timeRange.indexOf(" "));
			//With in hour time range, pages loop
			while (startIndex < numOfRecordsInTimeRange) {
				checkAndWait();
				iterationStartTime = System.currentTimeMillis();
				log("Iteration: " + pageNo + ". Exporting "
						+ numOfRecsToExportPerIteration + " records from solr...");
				//curl http://localhost:8983/solr/collection1/query -d 'q=*:*&start=0&rows=20000&fq=insertTime:[2020-01-19T06:00:00.000Z TO 2020-01-19T06:59:59.999Z]&sort=insertTime+asc'
				String cmd[] = {"/usr/bin/curl", 
						"http://" + solrHost + "/solr/" + solrCore + "/query",
						"-d", "q=*:*&start=" + startIndex + "&rows="+ numOfRecsToExportPerIteration 
						+ "&fq=insertTime:[" + timeRange + "]&sort=insertTime+asc",
						"-o", solrDataFileNamePrefix + "_" + pageNo + ".json"};
				
				log(" with cmd: " + String.join(" ", cmd) + " : ");
				int exitVal = Util.executeAndExport(cmd, "/tmp/solrout.log", false, true);
				/*If the export is failed due to an exception from SOLR, then SOLR returns a valid json with exception at the end of the file.
				 * In this case, executeAndExport will return 0 but the conversion will fail. So the Converter will add the failed timeRanges
				 * to the Monitor.failedTimeRanges which will be retried at the end.
				 */
				if(exitVal != 0) {
					log("Export failed for time range " + timeRange);
				}
				
				log("Export took " + ((System.currentTimeMillis() - iterationStartTime)/1000 + " seconds"));
				
				startIndex = startIndex + numOfRecsToExportPerIteration;
				pageNo++;
			}		
		} while (true);
		
	}
	
	private void checkAndWait(){
		
		while(!monitor.canExport()){
			log("Waiting for already exported file to get consumed.");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void log(String msg){
		System.out.println(new Date().toString() + " Exporter thread ["+id+"]: " + msg);
	}
	
}
