package edu.cmu.cs.cs214.hw5.core;

/**
 * A display plugin interface. This interface lists methods a {@link DisplayPlugin}
 * concrete class should provide to {@link Framework}
 */
public interface DisplayPlugin {

    /**
     * Returns the name of the display plugin.
     *
     * @return      the name of plugin
     */
    String getName();
    
    /**
     * Returns the number of dimension the display plugin supports.
     * The framework will notify GUI to show different configuration panel for user to configure visualization.
     *
     * @return      the number of dimensions
     */
    Dimension getDimension();

    /**
     * Plots charts out of the given data with associated configurations.
     * The {@link Framework} will call {@link DisplayPlugin} and pass the processed data of the selected
     * data source along with display configurations indicating the features and regions to be displayed.
     *
     * @param processedData     the processed data to be visualized
     * @param displayConfig     the visualization configuration associated
     *                          with the processed data
     * @return true if success, false otherwise
     * @throws IllegalArgumentException if the processed data does not satisfy the display plugin's requirement,
     * such as the date of the processed data is missing or the region of the data is not supported.
     * @throws UnsupportedOperationException if the dimension of the processed data does not fit the dimension of visualizer,
     * or the processed data causes uneven length of each dimensions in charts,
     */
    boolean display(ProcessedData processedData, DisplayConfig displayConfig);
}
