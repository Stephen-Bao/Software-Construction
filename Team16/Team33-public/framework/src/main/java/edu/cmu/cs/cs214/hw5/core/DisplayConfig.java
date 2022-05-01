package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A configuration class maintaining data source, all its applicable display plugins
 * and corresponding selectable features for each dimension in the term of a
 * {@link FeatureSelection}.
 */
public final class DisplayConfig {
    private final String dataSourceName;
    private final DisplayPlugin displayPlugin;
    /** A list of features selected to be visualized. Each {@link FeatureSelection}
     * represents a list of features that are applicable for this dimension */
    private final List<FeatureSelection> featureSelections;
    
    /**
     * Constructs a display configuration instance for a given data source, one applicable
     * display plugin and a list of {@code FeatureSelection}.
     *
     * @param dataSourceName    the name of the data source
     * @param displayPlugin     the display plugin instance to be configured to visualize the data source
     * @param featureSelections the feature selection instance
     */
    public DisplayConfig(String dataSourceName, DisplayPlugin displayPlugin, List<FeatureSelection> featureSelections) {
        this.dataSourceName = dataSourceName;
        this.displayPlugin = displayPlugin;
        this.featureSelections = featureSelections;
    }
    
    /**
     * Getter method of dataSourceName field
     *
     * @return      the name of the data source
     */
    public String getDataSourceName() {
        return dataSourceName;
    }
    
    /**
     * Getter method of displayPlugin field
     *
     * @return      the display plugin instance
     */
    public DisplayPlugin getDisplayPlugin() {
        return displayPlugin;
    }
    
    /**
     * Getter method of featureSelections field
     *
     * @return      the list of FeatureSelection
     */
    public List<FeatureSelection> getFeatureSelections() {
        return new ArrayList<>(featureSelections);
    }
}

