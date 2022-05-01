package edu.cmu.cs.cs214.hw5.core;

import java.util.List;

/**
 * A data plugin interface as the API between {@link DataPlugin} and {@link Framework}.
 */
public interface DataPlugin {

    /**
     * Gets the name of the data plugin.
     *
     * @return      name of plugin
     */
    String getName();

    /**
     * Gets the input data from input arguments.
     * When framework calls the selected DataPlugin, the framework calls this method to pass
     * user input of data source name and arguments for data plugin to load the data into {@link InputData}.
     *
     * @param name  the input data source name
     * @param args  input arguments, such as file directory or api address
     * @return      parsed input data source
     * @throws IllegalArgumentException             if the user input data is invalid, such as a local file path is not found
     * @throws javax.xml.bind.DataBindingException  if access to an XML document failed
     * @throws UnsupportedOperationException        if error found in parsing file into the {@link InputDataPoint} object,
     *                                              such as missing required data field (region or time)
     */
    InputData getInputData(String name, List<String> args);

}
