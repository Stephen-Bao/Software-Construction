package edu.cmu.cs.cs214.hw2;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Main program that runs the assembly language interpreter.
 * @author Xinyu Bao
 */
public class Interpret {
    /**
     * The main method to run the assembly language interpreter
     * @param args Program arguments.
     * @throws IOException On input error.
     */
    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        Processor myProcessor = new Processor();
        myProcessor.loadFile(filePath);
        myProcessor.execute();
    }
}











