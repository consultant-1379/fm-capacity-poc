package com.ericsson.oss.services.fm.alarm.migration.concurrent;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;

public class Importer implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Importer.class);

    private final int id;
    private final String esHost;
    //	private static String convertedTempDir;
    private final Monitor monitor;

    public Importer(int id, String esHost, Monitor monitor) {
        this.id = id;
        this.esHost = esHost;
        //		Importer.convertedTempDir = convertedTempDir;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            do {
                long t = System.currentTimeMillis();
                LOGGER.debug("Getting ES input file to import");

                String esDataFile = monitor.getEsFileForImporting(id);
                //String esDataFile = getEsFile(id);

                if (esDataFile == null) {
                    if (monitor.isConverterAlive()) {
                        LOGGER.debug("Waiting for " + Util.RETRY_SLEEP_SECONDS + " seconds for ES input files...");
                        Util.sleep(Util.RETRY_SLEEP_SECONDS);
                        continue;
                    }

                    LOGGER.info("ES input file is not found to import and Converter threads are not alive."
                                        + " Exiting Importer thread [" + id + "]");
                    return;
                }

                checkAndCreateIndex(esDataFile);
                //			esDataFile = Importer.convertedTempDir + File.separator + esDataFile;
                LOGGER.debug("Picked " + esDataFile + " ...took " + ((System.currentTimeMillis() - t) + " milliseconds"));

                String[] cmd = { "/usr/bin/curl",
                        "-H",
                        "Content-Type: application/json",
                        "-s",
                        "-XPOST",
                        esHost + "/_bulk",
                        "--data-binary",
                        "@" + esDataFile,
                        "-o",
                        "/tmp/esLoading1_" + id + ".log" };
                LOGGER.info(" with cmd: " + String.join(" ", cmd) + " : ");
                t = System.currentTimeMillis();
                int exitVal = Util.executeAndExport(cmd, "/tmp/esLoading2_" + id + ".log", false, true);
                if (exitVal == 0) {
                    //Delete the SOLR format file after converting into ES format
                    new File(esDataFile).delete();
                } else {
                    LOGGER.error("Import failed for file " + esDataFile);
                }

                LOGGER.info("Import took " + ((System.currentTimeMillis() - t) / 1000 + " seconds"));

                //TODO If import take >= 60 seconds, check if the index is in RED state.
                //If index is in RED state, add the day to 'skip list' and skip imports into index of that day.

            } while (true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	/*private static synchronized String getEsFile(int id) {
		
		File dir = new File(Importer.convertedTempDir);
		String exportedFiles[] = dir.list(new ExportedFileNameFilter());
		String fileName;
		if (exportedFiles.length > 0) {
			fileName = (id % 2 == 0) ? exportedFiles[0] : exportedFiles[exportedFiles.length-1];
			String absoluteFileName = Importer.convertedTempDir + File.separator + fileName;
			File file = new File(absoluteFileName);
			file.renameTo(new File(absoluteFileName + IMPORTING_EXT));
			return absoluteFileName + IMPORTING_EXT;
		}

		return null;
	}*/

    private void checkAndCreateIndex(String file) {
        // e.g esDataFile file name: "esData_2020-01-19T07:00:00:000Z_1_fm_history_2020wk25.json"
        //Extract index name from file name
        String esDataFile = file.substring(file.lastIndexOf(File.separator) + 1);
        int i = esDataFile.indexOf("_");
        i = esDataFile.indexOf("_", i + 1);
        i = esDataFile.indexOf("_", i + 1);
        String esIndexName = esDataFile.substring(i + 1, esDataFile.indexOf(".json"));
        //Check if the index is already existing and create if not existing
        synchronized (monitor) {
            if (!monitor.isIndexPresent(esIndexName)) {
                String[] cmd = { "/usr/bin/curl",
                        "-H", "Content-Type:application/json",
                        "-XPUT", esHost + "/" + esIndexName + "?pretty",
                        "-d", "{\"settings\" : {\"index\" : {\"number_of_shards\" : 1,\"number_of_replicas\" : 0}}}" };

                LOGGER.info("Creating ES index with cmd: " + String.join(" ", cmd) + " : ");
                int exitVal = Util.executeAndExport(cmd, "/tmp/indexCreation.log", false, true);
                if (exitVal == 0) {
                    monitor.addEsIndex(esIndexName);
                } else {
                    LOGGER.error("Index " + esIndexName + "creation failed");
                }
            }
        }
    }

	/*private static class ExportedFileNameFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".json");
		}

	}*/
	
/*	private void log(String msg){
		System.out.println(new Date().toString() + " Importer thread ["+id+"]: " + msg);
	}*/
}
