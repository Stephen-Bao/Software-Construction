package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The object that represents user configurations on {@link edu.cmu.cs.cs214.hw5.gui.FrameworkGUI}
 * to load the input data from {@link DataPlugin}
 */
public class InputDataConfig {
    private final DataPlugin dataPlugin;
    private final String dataSourceName;
    private final List<String> args;

    /**
     * Returns a InputDataConfig object with user configurations including the dataPlugin chosen,
     * the data source name, and other arguments for the dataPlugin to load the data.
     *
     * @param dataPlugin the data plugin chosen
     * @param dataSourceName the user-defined name of the data source
     * @param args arguments to be passed to the data plugin to load data
     */
    public InputDataConfig(DataPlugin dataPlugin, String dataSourceName, List<String> args) {
        this.dataPlugin = dataPlugin;
        this.dataSourceName = dataSourceName;
        this.args = new ArrayList<>(args);
    }

    /**
     * Getter of the dataPlugin.
     *
     * @return the dataPlugin
     */
    public DataPlugin getDataPlugin() {
        return dataPlugin;
    }

    /**
     * Getter of the dataSourceName.
     *
     * @return data source name
     */
    public String getInputDataName() {
        return dataSourceName;
    }

    /**
     * Getter of the args.
     *
     * @return arguments
     */
    public List<String> getInputDataArgs() {
        return args;
    }
}
