package com.ericsson.oss.services.fm.alarm.migration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

public class Util {

    public static final String MIGRATION_TEMP_DIR = "/ericsson/postgres/migration";
    public static final int RETRY_SLEEP_SECONDS = 3;
    public static final int WEEKLY_INDICES = 0;
    public static final int DAILY_INDICES = 1;
    public static final int[] rangeSize = { 4096 * 1000, 2048 * 1000, 1024 * 1000, 512 * 1000, 256 * 1000, 128 * 1000, 64 * 1000, 32 * 1000,
            16 * 1000, 4 * 1000, 2 * 1000, 1000, 500, 100, 50, 10, 5, 1 };
    private static final String INDEX_POSTFIX = "-m";
    public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static DateFormat getDateFormat() {
        final DateFormat dateFormat = new SimpleDateFormat(Util.DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat;
    }

    public static String getEsIndexName(int indexType, String dateTime) {
        DateFormat dateFormat = getDateFormat();
        Calendar cal = Calendar.getInstance();
        String indexName = null;
        try {
            cal.setTime(dateFormat.parse(dateTime));
            if (indexType == Util.WEEKLY_INDICES) {
                indexName = "fm_history_" + cal.get(Calendar.YEAR) + "wk" + String.format("%02d",
                                                                                          cal.get(Calendar.WEEK_OF_YEAR)) + Util.INDEX_POSTFIX;
            } else {
                indexName = "fm_history_" + cal.get(Calendar.YEAR) + String.format("%02d", cal.get(Calendar.MONTH) + 1)
                        + String.format("%02d", cal.get(Calendar.DATE)) + Util.INDEX_POSTFIX;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return indexName;
    }

    public static void sleep(int seconds) {
        if (seconds > 0) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String fromFileNameToRange(final String filename) {
        return StringUtils.substringBetween(filename, "_", "_");
    }

    public static boolean createFailureFile(final String esDataFileFullPath, final String esDataFile) {
        String target = Util.MIGRATION_TEMP_DIR + "/failure/" + esDataFile;
        try {
            Files.copy(Paths.get(esDataFileFullPath), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
