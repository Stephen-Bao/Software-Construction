package edu.cmu.cs.cs214.hw5.core;

/**
 * The plugin interface that data plugins use to implement and register
 * with the {@link VaccineAnalysisFramework}.
 */
public interface DataPlugin {
    /**
     * Gets the name of the data plugin.
     * @return plugin name
     */
    String name();

    /**
     * Gets the source instruction of the data plugin. It will be displayed in
     * the label of source input dialogue.
     * @return instruction string
     */
    String sourceInstruction();

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * (if necessary).
     * @param framework The {@link VaccineAnalysisFramework} instance with which
     * the plugin was registered.
     */
    void onRegister(VaccineAnalysisFramework framework);

    /**
     * Specify the source for timeline vaccine data.
     * @param src timeline data source
     */
    void setTimelineVaccineDataSource(String src);

    /**
     * Specify the source for last hour vaccine data.
     * @param src last hour data source
     */
    void setLastHourVaccineDataSource(String src);

    /**
     * Specify the source for population data.
     * @param src population data source
     */
    void setPopulationDataSource(String src);

    /**
     * Based on the specified data sources, parse each source to
     * create and return a new Dataset instance.
     * @return a new Dataset instance
     * @throws Exception error when accessing data sources, will be shown in
     *                   the error dialogue.
     */
    Dataset getData() throws Exception;
}
