package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The object that represents the processed feature in the {@link RegionalProcessedData}
 */
public class ProcessedFeature {
    private final String featureName;
    private final List<Long> value;

    /**
     * Returns a ProcessedFeature object from a feature name and value represented in List<Long>
     *
     * @param featureName the name of the feature
     * @param val the list of values in time serious with padding
     */
    public ProcessedFeature(String featureName, List<Long> val) {
        this.featureName = featureName;
        this.value = val;
    }

    /**
     * Getter of the processed feature value
     *
     * @return a defensive copy of the value of the processed feature
     */
    public List<Long> getValue() {
        return new ArrayList<>(value);
    }

    /**
     * Getter of the feature name
     *
     * @return the name of the feature
     */
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public String toString() {
        return "{" + featureName + " : " + value.toString() + '}';
    }

}
