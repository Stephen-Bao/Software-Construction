package edu.cmu.cs.cs214.hw5.core;

/**
 * VaccineAnalysisFramework is the class to manage the flow of reading data
 * via DataPlugin and drawing the chart with DisplayPlugin. Also handling the
 * events and errors for frameworkChangeListeners.
 */
public class VaccineAnalysisFramework {
    /**
     * Currently only assign one FrameworkChangeListener, and it's the GUI.
     */
    private FrameworkChangeListener frameworkChangeListener;
    /**
     * Local dataset for DataPlugin writing and DisplayPlugin reading.
     */
    private Dataset dataset = new Dataset();

    /**
     * Runs the input data plugin and set the dataset. If there is an error
     * running the plugin, notify the GUI with the error message.
     * @param plugin input data plugin
     */
    public void runDataPlugin(final DataPlugin plugin) {
        try {
            dataset = plugin.getData();
            notifyDatasetLoaded();
        } catch (Exception err) {
            notifyDataPluginError(err.toString());
            err.printStackTrace();
        }
    }

    /**
     * Get the current dataset copy.
     * @return the defensive copy of current dataset
     */
    public Dataset getDataSet() {
        return new Dataset(dataset);
    }

    /**
     * Setter of current change listener.
     * @param listener input listener
     */
    public void setFrameworkChangeListener(
            final FrameworkChangeListener listener
    ) {
        frameworkChangeListener = listener;
    }

    /**
     * Register display plugin and show in the GUI menu.
     * @param plugin input display plugin
     */
    public void registerDisplayPlugin(final DisplayPlugin plugin) {
        plugin.onRegister(this);
        frameworkChangeListener.onDisplayPluginRegistered(plugin);
    }

    /**
     * Register data plugin and show in the GUI menu.
     * @param plugin input data plugin
     */
    public void registerDataPlugin(final DataPlugin plugin) {
        plugin.onRegister(this);
        frameworkChangeListener.onDataPluginRegistered(plugin);
    }

    /**
     * Start run the input display plugin.
     * @param plugin input display plugin
     */
    public void startNewDisplay(final DisplayPlugin plugin) {
        frameworkChangeListener.onNewDisplay(plugin);
    }

    /**
     * Notify dataset loaded.
     */
    public void notifyDatasetLoaded() {
        frameworkChangeListener.onDatasetLoaded();
    }

    /**
     * Notify data plugin error.
     * @param errMessage error message
     */
    public void notifyDataPluginError(final String errMessage) {
        frameworkChangeListener.onDataPluginError(errMessage);
    }

    /**
     * Notify display plugin error.
     * @param errMessage error message
     */
    public void notifyDisplayPluginError(final String errMessage) {
        frameworkChangeListener.onDisplayPluginError(errMessage);
    }
}
