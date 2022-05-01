package edu.cmu.cs.cs214.hw5.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Object that represents a collection of processed data in the specific region in the {@link ProcessedFeature},
 * contains a region String and a map of {@link ProcessedFeature}
 */
public class RegionalProcessedData {
    private final String region;
    private final Map<String, ProcessedFeature> features = new HashMap<>();

    /**
     * Returns an initialized RegionalPrecessedData object
     *
     * @param region the region of this regionalProcessedData object
     */
    public RegionalProcessedData(String region){
        this.region = region;
    }

    /**
     * Adds a feature to the regional processed data
     *
     * @param feature the {@link ProcessedFeature} object to be added
     */
    public void addFeature(ProcessedFeature feature) {
        features.put(feature.getFeatureName(), feature);
    }

    /**
     * Returns the {@link ProcessedFeature} from a given feature name in the RegionalProcessedData object
     *
     * @param featureName the name of the feature
     * @return the {@link ProcessedFeature} object of the given feature name
     */
    public ProcessedFeature getFeature(String featureName) {
        if (features.containsKey(featureName)) {
            return features.get(featureName);
        }
        throw new UnsupportedOperationException("feature not found in the RegionalProcessedData: "+featureName);
    }

    /**
     * Getter of the region
     *
     * @return the region of the RegionalProcessedData Object
     */
    public String getRegion() {
        return region;
    }


    @Override
    public String toString() {
        return features.toString();
    }

}
