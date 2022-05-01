package edu.cmu.cs.cs214.rec06.loggingsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LogfileListener implements LoggingListener{

    private static final String LOGFILE_NAME = "log.txt";

    private static final String DEBUG_PREFIX = "[Debug]";
    private static final String ERROR_PREFIX = "[Error]";
    private static final String LOGGER_STARTED_UP = "Logger started up.";
    private static final String LOGGER_SHUTTING_DOWN = "Logger shutting down.";

    private PrintWriter fileOut = null;

    public LogfileListener() {
        try {
            fileOut = new PrintWriter(new File(LOGFILE_NAME));
            fileOut.println(LOGGER_STARTED_UP);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void wrireDebug(String message) {
        String logLine = DEBUG_PREFIX + " " + message;
        fileOut.println(logLine);
    }

    @Override
    public void writeError(String error) {
        String logLine = ERROR_PREFIX + " " + error;
        fileOut.println(logLine);
    }

    @Override
    public void close() {
        fileOut.println(LOGGER_SHUTTING_DOWN);
        fileOut.close();
    }
}
