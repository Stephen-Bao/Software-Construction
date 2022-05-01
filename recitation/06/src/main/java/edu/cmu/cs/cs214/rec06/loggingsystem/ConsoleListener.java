package edu.cmu.cs.cs214.rec06.loggingsystem;

public class ConsoleListener implements LoggingListener{

    private static final String DEBUG_PREFIX = "[Debug]";
    private static final String ERROR_PREFIX = "[Error]";
    private static final String LOGGER_STARTED_UP = "Logger started up.";
    private static final String LOGGER_SHUTTING_DOWN = "Logger shutting down.";

    public ConsoleListener() {
        System.out.println(LOGGER_STARTED_UP);
    }

    @Override
    public void wrireDebug(String message) {
        String logLine = DEBUG_PREFIX + " " + message;
        System.out.println(logLine);
    }

    @Override
    public void writeError(String error) {
        String logLine = ERROR_PREFIX + " " + error;
        System.out.println(logLine);
    }

    @Override
    public void close() {
        System.out.println(LOGGER_SHUTTING_DOWN);
    }
}
