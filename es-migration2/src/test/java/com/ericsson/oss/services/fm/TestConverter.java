package com.ericsson.oss.services.fm;


import com.ericsson.oss.services.fm.alarm.migration.concurrent.Converter;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;

public class TestConverter {
    private static final String TEST_MIGRATION_TEMP_DIR = "/tmp/edelpao";

    public static void main(String[] args) throws Exception {
        String exportTempDir = TEST_MIGRATION_TEMP_DIR + "/exported";
        String convertedTempDir = TEST_MIGRATION_TEMP_DIR + "/converted";

        Monitor monitor = new Monitor(1, 1,
                                      1, exportTempDir, convertedTempDir);

        monitor.exporterExiting();

        Converter converter = new Converter(0, exportTempDir, convertedTempDir,
                                            monitor, 1);

        converter.run();
    }
}
