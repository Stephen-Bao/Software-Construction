package edu.cmu.cs.cs214.hw5.core;

/**
 * Framework change listener defines the framework's change events and methods
 * handling the events.
 */
public interface FrameworkChangeListener {
    /**
     * Handling data plugin registered.
     * @param plugin registered data plugin
     */
    void onDataPluginRegistered(DataPlugin plugin);

    /**
     * Handling display plugin registered.
     * @param plugin registered display plugin
     */
    void onDisplayPluginRegistered(DisplayPlugin plugin);

    /**
     * Handling display plugin is triggered.
     * @param plugin display plugin to be run
     */
    void onNewDisplay(DisplayPlugin plugin);

    /**
     * Handling data set is loaded from a data plugin.
     */
    void onDatasetLoaded();

    /**
     * Handling data plugin running error.
     * @param errMessage error message
     */
    void onDataPluginError(String errMessage);

    /**
     * Handling display plugin running error.
     * @param errMessage error message
     */
    void onDisplayPluginError(String errMessage);
}
