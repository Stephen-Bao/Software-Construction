package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The object that represents the processed data of a data source from the {@link Framework}
 */
public class ProcessedData {
    private final String name;
    private final GeoScope geoScope;
    private final List<Date> dates;
    private final List<String>  featureNames;
    private final Map<String, Boolean> predictFeatures = new HashMap<>();
    private final Map<String, RegionalProcessedData> regionalProcessedDataMap;

    /**
     * Returns a ProcessedData Object with a name (same as the data source name), a time interval of the data,
     * a feature contents of the data, a geometric scope of the data, and {@link RegionalProcessedData} in a map.
     *
     * @param name name of the data source
     * @param dates the overall time interval in the processed data
     * @param featureNames the overall list of features contained in the processed data
     * @param regionalProcessedDataMap the map contains regional processed data with the region name as the key.
     * @param geoScope the geometric scope of the processed data
     */
    public ProcessedData(String name, List<Date> dates, List<String> featureNames,
                         Map<String, RegionalProcessedData> regionalProcessedDataMap, GeoScope geoScope){
        this.name = name;
        this.dates = dates;
        this.featureNames = featureNames;
        this.regionalProcessedDataMap = regionalProcessedDataMap;
        this.geoScope = geoScope;
    }

    /**
     * Adds a {@link ProcessedFeature} to the processed data
     *
     * @param region the region that this processed feature belongs to
     * @param feature the processed feature to be added
     */
    public void addFeature(String region, ProcessedFeature feature) {
        regionalProcessedDataMap.get(region).addFeature(feature);
    }

    /**
     * Getter of the dates
     *
     * @return a defensive copy of the time interval
     */
    public List<Date> getDates() {
        return new ArrayList<>(dates);
    }

    /**
     * Getter of the featureNames
     *
     * @return a defensive copy fo the feature names
     */
    public List<String> getFeatureNames() {
        return new ArrayList<>(featureNames);
    }

    /**
     * Getter of the name
     *
     * @return the name of the processed data
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of the GeoScope
     *
     * @return the geoScope of the processed data
     */
    public GeoScope getGeoScope() {
        return geoScope;
    }

    /**
     * Getter of the regionalProcessedDataMap
     *
     * @return the regional processed data map
     */
    public Map<String, RegionalProcessedData> getRegionalProcessedDataMap() {
        return regionalProcessedDataMap;
    }

    /**
     * Adds a feature into the predict features
     *
     * @param featureName the name of the feature that has been predicted
     */
    public void addPredictFeature(String featureName) {
        if(!predictFeatures.containsKey(featureName)) {
            predictFeatures.put(featureName, true);
        }
    }

    /**
     * Returns if the feature has been predicted.
     * If a feature is predicted, user can access the predicted feature by adding the PREDICT_PREFIX {@link Constant}
     * to access the prediction value of the feature.
     *
     * @param featureName the name of feature to check if it is predicted
     * @return boolean
     */
    public boolean isPredictFeature(String featureName) {
        return predictFeatures.containsKey(featureName);
    }

    @Override
    public String toString() {
        return "ProcessedData {" +
                "name='" + name + '\'' +
                ", \n dates=" + dates.toString() +
                ", \n featureNames=" + featureNames.toString() +
                ", \n data=" + regionalProcessedDataMap.toString() +
                '}';
    }
}
