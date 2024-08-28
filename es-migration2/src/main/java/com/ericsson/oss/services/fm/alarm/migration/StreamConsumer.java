package com.ericsson.oss.services.fm.alarm.migration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class StreamConsumer extends Thread {
    InputStream is;
    boolean type;
    OutputStream os;
    boolean consoleOut = false;

    public StreamConsumer(InputStream is, boolean type, boolean consoleOut) {
        this(is, type, null, consoleOut);
    }

    public StreamConsumer(InputStream is, boolean type, OutputStream redirect, boolean consoleOut) {
        this.is = is;
        this.type = type;
        this.os = redirect;
        this.consoleOut = consoleOut;
    }

    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null) {
                pw = new PrintWriter(os);
            }

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            char[] animationChars = new char[] { '|', '/', '-', '\\' };
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (pw != null) {
                    pw.println(line);
                }
                //				if(type == false && consoleOut)
                //					System.out.print("ERROR: ");
                if (consoleOut && !line.isEmpty()) {
                    if (first) {
                        System.out.println( "\n" + line);
                    }
                    else {
                        System.out.print(line + "\r");
                    }
                    first = false;
                }
            }
            if (!first) {
                System.out.println();
            }
            if (pw != null) {
                pw.flush();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
