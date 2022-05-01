package edu.cmu.cs.cs214.rec06.loggingsystem;

public interface LoggingListener {

    void wrireDebug(String message);
    void writeError(String error);
    void close();

}
