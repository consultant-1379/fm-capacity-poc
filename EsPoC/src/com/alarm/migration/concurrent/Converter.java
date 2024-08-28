package com.alarm.migration.concurrent;

import static com.alarm.migration.Util.CONVERTING_EXT;
import static com.alarm.migration.Util.RETRY_SLEEP_SECONDS;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import com.alarm.migration.Util;

public class Converter implements Runnable {

	private int id;
	private static String exportTempDir;
	private String convertedTempDir;
	private Monitor monitor;
	private int indexType;
	private int sleepTime;
	
	public Converter(int id, String exportTempDir, String convertedTempDir,
			Monitor monitor, int indexType, int sleepTime) {
		this.id = id;
		Converter.exportTempDir = exportTempDir;
		this.convertedTempDir = convertedTempDir;
		this.monitor = monitor;
		this.indexType = indexType;
		this.sleepTime = sleepTime;
	}
	
	@Override
	public void run() {
		try{
		do{
			long t = System.currentTimeMillis();
			log("Getting SOLR exported file to convert");
			String solrFile = getSOLRFile();
			
			if(solrFile == null) {
				if(monitor.isExporterAlive()) {
					log("Waiting for " + RETRY_SLEEP_SECONDS + " seconds for SOLR exported files...");
					Util.sleep(RETRY_SLEEP_SECONDS);
					continue;
				}
				
				log("SOLR exportd file is not found to convert and Exporter threads are not alive."
						+ " Exiting Converter thread [" + id + "]");
				monitor.converterExiting();
				break;
			}
			log("Picked " + solrFile + " ...took " + ((System.currentTimeMillis() - t) + " milliseconds"));
			
			String [] parts = solrFile.split("_");
			String dateTimeStr = parts[1];
			String pageNo = parts[2].substring(0, parts[2].indexOf(".json"));
			String esIndexName = Util.getEsIndexName(indexType, dateTimeStr);
			String esDataFile = convertedTempDir + File.separator + "esData_" + 
							dateTimeStr + "_" + pageNo + "_" + esIndexName + ".json";

			//Migrate into ES format
			//cat solrData.json | jq -c '.response["docs"] | .[] | {"index": {"_index": "fm_history", "_type": "json"}}, .' | curl -H "Content-Type: application/json" -XPOST localhost:9200/_bulk --data-binary @-
			log("Converting into ES format...");
			
			String[] cmd = {"/usr/bin/jq", "-c", 
					".response[\"docs\"] | .[] | {\"index\": {\"_index\": \"" + esIndexName + "\", \"_type\": \"doc\"}}, .",
					solrFile};
			log(" with cmd: " + String.join(" ", cmd) + " > " + esDataFile + " : ");
			
			//Trying to avoid below error during conversion by waiting for 2 seconds
			//ERROR: parse error: Unfinished string at EOF at line 649432, column 25
			if (!monitor.isRetryPhase)
				Util.sleep(sleepTime);
			else
				Util.sleep(sleepTime + 5);
			
			int exitVal = Util.executeAndExport(cmd, esDataFile, false, true);
			
			if(exitVal == 0) {
				System.out.println("Conversion success: " + esDataFile);
				monitor.enqueueEsFileForImporting(esIndexName, esDataFile);
				new File(solrFile).delete();
			}else{
				log("Conversion failed for file " + solrFile);
//						+ "Export the file manually for the time range and see if the file is proper.");
				//Extract timeRange from file name and add to monitor.addFailedTimeRange() to retry export from Exporter threads.
				addTimeRangeToFailedQueue(solrFile);
				//Delete the 0 size output file on failure
				
				new File(esDataFile).delete();
					String target = "/ericsson/postgres/solrFailedFiles/"
							+ solrFile.substring(solrFile.lastIndexOf("/") + 1);
				Files.move(Paths.get(solrFile), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
			}
			//Delete the SOLR format file irrespective of conversion success state to save space.
			//By running the export command for the time range in file name, same file can be generated again.
			
//			new File(solrFile).delete();
			log("Conversion took " + ((System.currentTimeMillis() - t)/1000 + " seconds"));
		
		} while(true);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void addTimeRangeToFailedQueue(String fileName){
		// e.g fileName:  /tmp/migration/exported/solrData_2020-01-19T06:00:00.000Z_5.json.converting
		// e.g timeRange to be prepared: 2020-01-19T06:00:00.000Z TO 2020-01-19T06:59:59.999Z_5
		
		fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
		String[] parts = fileName.split("_");
		String fromTime = parts[1];
		monitor.addFailedTimeRange(fromTime + " TO " + fromTime.replaceAll(":00:00.000Z", ":59:59.999Z") 
				+ "_" + parts[2].substring(0, parts[2].indexOf(".")));
	}
	
	private static synchronized String getSOLRFile() {
		
		File dir = new File(Converter.exportTempDir);
		String exportedFiles[] = dir.list(new ExportedFileNameFilter());
		File file;
		String absoluteFileName;
		if (exportedFiles.length > 0) {
			absoluteFileName = Converter.exportTempDir + File.separator + exportedFiles[0];
			file = new File(absoluteFileName);
			file.renameTo(new File(absoluteFileName + CONVERTING_EXT));
			return absoluteFileName + CONVERTING_EXT;
		}
		
		return null;
	}
	
	private static class ExportedFileNameFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".json");
		}
		
	}
	private void log(String msg){
		System.out.println(new Date().toString() + " Converter thread ["+id+"]: " + msg);
	}
	
}
