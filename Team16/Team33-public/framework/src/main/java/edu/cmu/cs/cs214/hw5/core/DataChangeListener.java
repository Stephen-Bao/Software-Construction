package edu.cmu.cs.cs214.hw5.core;

import edu.cmu.cs.cs214.hw5.gui.FrameworkGUI;

import java.util.List;

/**
 * An observer interface that listens for changes in a game's state. The
 * {@link FrameworkImpl} calls these methods to notify all listeners, such as
 * {@link FrameworkGUI}, for its state changes.
 */

public interface DataChangeListener {
    /**
     * Called when a data plugin is registered to the framework.
     * Registers the {@link DataPlugin} loaded by {@link PluginLoader}
     *
     * @param dataPlugin    the data plugin
     */
    void onDataPluginRegistered(DataPlugin dataPlugin);
    
    /**
     * Called when a display plugin is registered to the framework.
     * Registers the {@link DisplayPlugin} loaded by {@link PluginLoader}
     *
     * @param displayPlugin  the display plugin
     */
    void onDisplayPluginRegistered(DisplayPlugin displayPlugin);
    
    /**
     * Called when a data source is uploaded to the framework successfully.
     * The GUI will be notified to notice user that data has been loaded,
     * and display corresponding selections of the loaded data.
     *
     * @param processedData the data that has been loaded
     */
    void onInputDataLoaded(ProcessedData processedData);
    
    /**
     * Called when an existing data source is deleted.
     * The GUI will be notified to notice user that data has been deleted,
     * and update corresponding selections of the loaded data options.
     *
     * @param dataSourceName      the data source name
     */
    void onInputDataDeleted(String dataSourceName);

    /**
     * Called when display plugin failed to visualize data.
     *
     * @param dataSourceName the name of the data source
     * @param displayPluginName the name of the display plugin
     */
    void onDisplayFailed(String dataSourceName, String displayPluginName);

    /**
     * Called when a list of selected features has been predicted.
     *
     * @param predictedFeature the predicted features
     */
    void onFeaturePredicted(String dataSourceName,List<ProcessedFeature> predictedFeature);
}
