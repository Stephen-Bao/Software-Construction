package edu.cmu.cs.cs214.hw5.core;

import java.util.List;

/**
 * An interface of the COVID-19 data analysis and visualization framework.
 */
public interface Framework {
    /**
     * Adds a given GUI listener to the framework's listener list.
     *
     * @param listener  the listener to be added
     */
    void addDataChangeListener(DataChangeListener listener);
    
    /**
     * Registers a list of data plugins to the framework can be used.
     * Users can choose to upload data source with one or more of these data plugins.
     *
     * @param dataPluginList    the list of data plugins
     */
    void registerDataPlugins(List<DataPlugin> dataPluginList);
    
    /**
     * Registers a list of display plugins to the framework can be used.
     * Users can choose to visualize uploaded data sources with one or more of these
     * display plugins.
     *
     * @param displayPluginList the list of display plugins
     */
    void registerDisplayPlugins(List<DisplayPlugin> displayPluginList);

    /**
     * Reads User configuration of the input data, load data from the user
     * input data configurations and process it.
     * Called by GUI when user clicks on load data button with user's input data configuration.
     *
     * @param inputDataConfig user provided input data configurations, including dataPlugin selected,
     * name of this input data, and some other arguments for dataPlugin to process
     */
    void loadInputData(InputDataConfig inputDataConfig);
    
    /**
     * Deletes a data source from the framework with the given data source name.
     * Called by GUI when user clicks on delete data with a data source choosen.
     *
     * @param dataSourceName    the data source name
     */
    void deleteInputData(String dataSourceName);

    /**
     * Returns available initial DisplayConfigs for the given input data, indicating which displayPlugin(s),
     * feature(s) can be configured for visualization in each dimension.
     * Called by GUI when user selects a data source for visualization config.
     *
     * @param dataSourceName    the data source name
     * @return                  the list of display plugins and their initial
     *                          configurations
     */
    List<DisplayConfig> getAvailableDisplayPluginConfig(String dataSourceName);
    
    /**
     * Returns a list of features for a given data source, which are
     * applicable for data prediction.
     * Called by GUI when user selects a data source and wants to configure features for prediction.
     *
     * @param dataSourceName    the data source name
     * @return                  the list of feature names
     */
    FeatureSelection getPredictableFeatureSelection(String dataSourceName);
    
    /**
     * Adds a data analysis configuration for a given data sources.
     * Called by GUI when user selects features for prediction and click on add prediction button.
     *
     * @param dataSourceName    the data source name
     * @param featureSelection  the list of feature names
     * @return true if prediction success, false if the feature has already been predicted
     * @throws UnsupportedOperationException if training model failed, or prediction failed
     */
    boolean predictFeature(String dataSourceName, FeatureSelection featureSelection);
    
    /**
     * Adds a visualization configuration from a given data sources with specified
     * display plugin and selected features.
     * Called by GUI when user adds a display configuration.
     *
     * @param displayConfig     the visualization configuration
     */
    void addDisplayConfig(DisplayConfig displayConfig);
    
    /**
     * Plots all previously configured visualizations that have not been displayed yet.
     * Called by GUI when user clicks on plot.
     *
     * @throws IllegalArgumentException if a display throws an IllegalArgumentException exception
     * @throws UnsupportedOperationException if a display throws an IllegalArgumentException exception
     */
    void plot();
}
