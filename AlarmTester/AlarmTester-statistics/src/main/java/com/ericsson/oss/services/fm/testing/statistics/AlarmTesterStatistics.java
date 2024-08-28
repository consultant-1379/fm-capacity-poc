/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.fm.testing.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlarmTesterStatistics {

    static String insertingPatternStr = "\\s*(\\d+\\:\\d+\\:\\d+\\,\\d+)\\s*INFO[\\s,\\S]*\\(EJB\\s*default\\s*\\-\\s*(\\d+)\\)[\\s,\\S]*>>>>>>>>>>>>>>> INSERTING into DPS\\:[\\s,\\S]*objectOfReference=([\\s,\\S]*), alarmState[\\s,\\S]*probableCause=(\\S+),[\\s,\\S]*alarmId=(\\d+),[\\s,\\S]*alarmNumber=(\\d+),[\\s,\\S]*";
    static Pattern insertingPattern = Pattern.compile(insertingPatternStr);
    static String insertedPatternStr = "\\s*(\\d+\\:\\d+\\:\\d+\\,\\d+) INFO[\\s,\\S]*\\(EJB\\s*default\\s*\\-\\s*(\\d+)\\)[\\s,\\S]*elapsedTime=(\\d+\\.\\d+)[\\s,\\S]*>>>>>>>>>>>>>>> INSERTED into DPS\\:[\\s,\\S]*poId=(\\d+)[\\s,\\S]*objectOfReference=([\\s,\\S]*), alarmState[\\s,\\S]*probableCause=(\\S+),[\\s,\\S]*alarmId=(\\d+),[\\s,\\S]*alarmNumber=(\\d+)[\\s,\\S]*";
    static Pattern insertedPattern = Pattern.compile(insertedPatternStr);
    static String separatorChar = ";";
    static Path rootPath;
    static Map<AlarmInfo, AlarmInsertingData> insertingMap = new ConcurrentHashMap<>();
    static Map<AlarmInfo, AlarmInsertedData> insertedMap = new ConcurrentHashMap<>();
    static volatile boolean finishedReading = false;
    static volatile int statisticsProcessed = 0;
    static Thread statisticsThread;

    public static void main(final String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Input server.log file path is needed !");
            return;
        }
        final String inputFilePath = args[0];
        final Path path = Paths.get(inputFilePath);
        rootPath = path.getParent();
        System.out.println("Input Path: " + path);
        doStatistics(inputFilePath);
        statisticsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String outputPath = rootPath.toString().concat(File.separator).concat("output.csv");
                    System.out.println("Output Path: " + outputPath);
                    final BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
                    bw.write("alarmThreadId");
                    bw.write(separatorChar);
                    bw.write("alarmObject");
                    bw.write(separatorChar);
                    bw.write("alarmNumber");
                    bw.write(separatorChar);
                    bw.write("alarmType");
                    bw.write(separatorChar);
                    bw.write("alarmTime");
                    bw.write(separatorChar);
                    bw.write("alarmInsertingTime");
                    bw.write(separatorChar);
                    bw.write("alarmInsertedTime");
                    bw.write(separatorChar);
                    bw.write("elapsedTime");
                    bw.write(separatorChar);
                    bw.write("alarmPoId");
                    bw.newLine();
                    writeStatistics(bw);
                    bw.close();
                    System.out.println("Statitstics processed data is " + statisticsProcessed);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        statisticsThread.start();
    }

    public static void doStatistics(final String inputFilePath) throws IOException {
        System.out.println("Reading log file...");
        final BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
        String line = null;
        while ((line = br.readLine()) != null) {
            //System.out.println("Line of server log: " + line);
            makeStatistics(line);
        }
        br.close();
        System.out.println("Found " + insertingMap.size() + " entries for \"INSERTING\" debug log");
        System.out.println("Found " + insertedMap.size() + " entries for \"INSERTED\" debug log");
        finishedReading = true;
    }

    private static void makeStatistics(final String line) {
        Matcher statMatcher = insertingPattern.matcher(line);
        if (statMatcher.matches() && statMatcher.groupCount() == 6) {
            //            System.out.println(line);
            insertingMap.put(
                    new AlarmInfo(statMatcher.group(2), statMatcher.group(3), statMatcher.group(6), statMatcher.group(4), statMatcher.group(5)),
                    new AlarmInsertingData(statMatcher.group(1)));
        } else {
            statMatcher = insertedPattern.matcher(line);
            if (statMatcher.matches() && statMatcher.groupCount() == 8) {
                //                System.out.println(line);
                insertedMap.put(
                        new AlarmInfo(statMatcher.group(2), statMatcher.group(5), statMatcher.group(8), statMatcher.group(6), statMatcher.group(7)),
                        new AlarmInsertedData(statMatcher.group(1), statMatcher.group(3), statMatcher.group(4)));
            }
        }
    }

    public static void writeStatisticsData(final BufferedWriter bw) throws IOException {
        final Iterator<Map.Entry<AlarmInfo, AlarmInsertingData>> it = insertingMap.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry<AlarmInfo, AlarmInsertingData> insertingEntry = it.next();
            final AlarmInfo alarmInfo = insertingEntry.getKey();
            final AlarmInsertedData alarmInsertedData = insertedMap.get(alarmInfo);
            if (alarmInsertedData != null) {
                bw.write(alarmInfo.getAlarmThreadId());
                bw.write(separatorChar);
                bw.write(alarmInfo.getAlarmObject());
                bw.write(separatorChar);
                bw.write(alarmInfo.getAlarmNumber());
                bw.write(separatorChar);
                bw.write(alarmInfo.getAlarmType());
                bw.write(separatorChar);
                bw.write(alarmInfo.getAlarmTime());
                bw.write(separatorChar);
                bw.write(insertingEntry.getValue().getAlarmTime());
                bw.write(separatorChar);
                bw.write(alarmInsertedData.getAlarmTime());
                bw.write(separatorChar);
                bw.write(alarmInsertedData.getElapsedTime());
                bw.write(separatorChar);
                bw.write(alarmInsertedData.getAlarmPoId());
                bw.newLine();
                it.remove();
                insertedMap.remove(alarmInfo);
                statisticsProcessed++;
            }
        }
    }

    public static void writeStatistics(final BufferedWriter bw) throws IOException {
        do {
            writeStatisticsData(bw);
            if (insertingMap.size() <= 0 && insertedMap.size() <= 0) {
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        } while (!finishedReading);
        writeStatisticsData(bw);
        System.out.println("Remaining " + insertingMap.size() + " entries for \"INSERTING\" debug log");
        System.out.println("Remaining " + insertedMap.size() + " entries for \"INSERTED\" debug log");
    }

}
