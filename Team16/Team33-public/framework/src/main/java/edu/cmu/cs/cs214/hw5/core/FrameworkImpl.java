package edu.cmu.cs.cs214.hw5.core;

import java.util.*;
import java.util.stream.Collectors;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.functions.LinearRegression;

import static edu.cmu.cs.cs214.hw5.core.Constant.PREDICT_PREFIX;
import static edu.cmu.cs.cs214.hw5.core.Constant.TIME_FEATURE_NAME;

/**
 * The Object that implements the Framework API, works to read data from the DataPlugins, process it,
 * predict selected features, and output to the DisplayPlugins with user defined display configurations.
 */
public class FrameworkImpl implements Framework {
    private final List<DataChangeListener> dataChangeListenerList = new ArrayList<>();
    private final Map<String, DataPlugin> dataPluginMap = new HashMap<>();
    private final Map<String, DisplayPlugin> displayPluginMap = new HashMap<>();

    private final Map<String, InputData> inputDataMap = new HashMap<>();
    private final Map<String, ProcessedData> processedDataMap = new HashMap<>();
    private final List<DisplayConfig> configList = new ArrayList<>();

    /**
     * Returns a FrameWorkImpl object
     */
    public FrameworkImpl() {}

    @Override
    public void addDataChangeListener(DataChangeListener listener) {
        dataChangeListenerList.add(listener);
    }

    @Override
    public void registerDataPlugins(List<DataPlugin> dataPluginList) {
        for(DataPlugin dataPlugin : dataPluginList) {
            dataPluginMap.put(dataPlugin.getName(), dataPlugin);
            for (DataChangeListener dataChangeListener : dataChangeListenerList) {
                dataChangeListener.onDataPluginRegistered(dataPlugin);
            }
        }
    }

    @Override
    public void registerDisplayPlugins(List<DisplayPlugin> displayPluginList) {
        for (DisplayPlugin displayPlugin: displayPluginList) {
            displayPluginMap.put(displayPlugin.getName(), displayPlugin);
            for (DataChangeListener dataChangeListener : dataChangeListenerList) {
                dataChangeListener.onDisplayPluginRegistered(displayPlugin);
            }
        }
    }

    @Override
    public void loadInputData(InputDataConfig inputDataConfig) {
        DataPlugin dataPlugin = inputDataConfig.getDataPlugin();
        String dataSourceName = inputDataConfig.getInputDataName();
        InputData inputData = dataPlugin.getInputData(dataSourceName,inputDataConfig.getInputDataArgs());
        if(inputData == null){
            throw new UnsupportedOperationException("Load Input data from DataPlugin failed");
        }

        if(inputDataMap.containsKey(dataSourceName)){
            throw new IllegalArgumentException("Invalid data source name");
        }
        inputDataMap.put(dataSourceName, inputData);

        ProcessedData processedData = processData(inputData);
        processedDataMap.put(dataSourceName, processedData);

        notifyLoadInputData(processedData);
    }

    /**
     * Returns the processedData of a data source
     * @param dataSourceName the name of the data source
     */
    protected ProcessedData getProcessedData(String dataSourceName) {
        return processedDataMap.get(dataSourceName);
    }

    @Override
    public void deleteInputData(String dataSourceName) {
        if(!inputDataMap.containsKey(dataSourceName)) {
            throw new IllegalArgumentException("Invalid data name");
        }
        inputDataMap.remove(dataSourceName);
        processedDataMap.remove(dataSourceName);
        
        notifyDeleteInputData(dataSourceName);
    }

    @Override
    public FeatureSelection getPredictableFeatureSelection(String dataSourceName) {
        if (!processedDataMap.containsKey(dataSourceName)) {
            throw new IllegalArgumentException("invalid dataSourceName");
        }
        ProcessedData processedData = processedDataMap.get(dataSourceName);
        return new FeatureSelection(Selection.MULTIPLE, processedData.getFeatureNames());
    }

    @Override
    public boolean predictFeature(String dataSourceName, FeatureSelection featureSelection) {
        if (!processedDataMap.containsKey(dataSourceName)) {
            throw new IllegalArgumentException("invalid dataSourceName");
        }
        ProcessedData processedData = processedDataMap.get(dataSourceName);
        return predictData(processedData, featureSelection);
    }

    @Override
    public List<DisplayConfig> getAvailableDisplayPluginConfig(String dataSourceName) {
        if (!processedDataMap.containsKey(dataSourceName)) {
            throw new IllegalArgumentException("invalid dataSourceName");
        }
        ProcessedData processedData = processedDataMap.get(dataSourceName);
        List<String> processedFeatures = processedData.getFeatureNames();
        processedFeatures.add(0,TIME_FEATURE_NAME);

        // create feature & region selections, will be distributed among all available display plugins
        FeatureSelection singleChoiceSelection = new FeatureSelection(Selection.SINGLE, processedFeatures);
        FeatureSelection multipleChoiceSelection = new FeatureSelection(Selection.MULTIPLE, processedFeatures);
        List<String> regionSelections = new ArrayList<>();
        for(Map.Entry<String, RegionalProcessedData> regionalProcessedDataEntry: processedData.getRegionalProcessedDataMap().entrySet()) {
            regionSelections.add(regionalProcessedDataEntry.getKey());
        }
        FeatureSelection regionSelection = new FeatureSelection(Selection.MULTIPLE, regionSelections);

        List<DisplayConfig> result = new ArrayList<>();
        for(Map.Entry<String, DisplayPlugin> entry : displayPluginMap.entrySet()){
            DisplayPlugin displayPlugin = entry.getValue();
            List<FeatureSelection> featureSelections = new ArrayList<>();
            // for the first dimension, only allows single choice
            featureSelections.add(singleChoiceSelection);
            // for other dimensions (2nd / 3rd order), allow multiple choice
            for (int i = 0; i < displayPlugin.getDimension().ordinal(); i++) {
                featureSelections.add(multipleChoiceSelection);
            }
            // add region selections
            featureSelections.add(regionSelection);
            DisplayConfig validDisplayConfig = new DisplayConfig(dataSourceName, displayPlugin,featureSelections);
            result.add(validDisplayConfig);
        }
        return result;
    }

    @Override
    public void addDisplayConfig(DisplayConfig displayConfig) {
        configList.add(displayConfig);
    }

    @Override
    public void plot() {
        Iterator<DisplayConfig> iterator = configList.iterator();
        while (iterator.hasNext()) {
            DisplayConfig config = iterator.next();
            if (!processedDataMap.containsKey(config.getDataSourceName())) {
                // skip the data sources that has been deleted
                continue;
            }
            ProcessedData processedData = processedDataMap.get(config.getDataSourceName());
            DisplayPlugin plugin = config.getDisplayPlugin();
            iterator.remove();
            if(!plugin.display(processedData, config)) {
                notifyDisplayFailed(config);
            }
        }
    }

    /**
     * Process input data into a ProcessedData with padding.
     *
     * @param data Inputdata provided by the dataPlugin
     * @return ProcessedData from the given inputData
     */
    private ProcessedData processData(InputData data) {
        List<Date> dates = getOverallDates(data);
        List<String> features = getOverallFeatures(data);
        Map<String, RegionalProcessedData> regionalProcessedDataMap = getRegionalProcessedDataMap(data, dates, features);
        return new ProcessedData(data.getDataSourceName(), dates, features, regionalProcessedDataMap, data.getGeoScope());
    }

    /**
     * Returns the dates covered by the Input data to indicate the overall time interval of the data,
     * also will be used to pad data.
     *
     * @param data input data
     * @return overall time interval
     */
    private List<Date> getOverallDates(InputData data) {
        List<Date> dates = new ArrayList<>();
        Map<Date, Boolean> datesOccurrenceMap = new HashMap<>();
        for(InputDataPoint dataPoint: data.getInputDataList()) {
            datesOccurrenceMap.put(dataPoint.getDate(), true);
        }
        for(Map.Entry<Date, Boolean> entry: datesOccurrenceMap.entrySet()) {
            dates.add(entry.getKey());
        }
        Collections.sort(dates);
        return dates;
    }

    /**
     * Returns the features covered by the input data to indicate the overall evaluated features,
     * also will be used to pad data.
     *
     * @param data input data
     * @return overall features covered in the input data
     */
    private List<String> getOverallFeatures(InputData data) {
        List<String> features = new ArrayList<>();
        Map<String, Boolean> featureOccurrenceMap = new HashMap<>();
        for(InputDataPoint dataPoint: data.getInputDataList()) {
            for(RawFeature rawFeature: dataPoint.getFeatures()) {
                if(!featureOccurrenceMap.containsKey(rawFeature.getName())) {
                    featureOccurrenceMap.put(rawFeature.getName(), true);
                }
            }
        }
        for(Map.Entry<String, Boolean> entry: featureOccurrenceMap.entrySet()) {
            features.add(entry.getKey());
        }
        return features;
    }

    /**
     * Returns the regional processed data map after pre-processing.
     * The Processed data will be padded in both time and features, in other words, if a regional data
     * is missing the data on one day or one feature, it will be padding with 0 values.
     *
     * @param data input data
     * @param dates overall time interval, for padding purpose
     * @param features overall features, for padding purpose
     * @return generated regional proessed data map
     */
    private Map<String, RegionalProcessedData> getRegionalProcessedDataMap(InputData data, List<Date> dates, List<String> features) {
        Map<String, List<InputDataPoint>> regionalInputDataMap = getRegionalInputDataMap(data);
        // common feature for all regions
        List<Long> datesVal = dates.stream().map(Date::getTime).collect(Collectors.toList());
        ProcessedFeature timeFeature = new ProcessedFeature(TIME_FEATURE_NAME,  datesVal);

        // data structures for padding, use a sorted map for visited dates
        Map<Date, Integer> visitedDates = new TreeMap<>();
        for (int i = 0; i < dates.size(); i ++) {
            visitedDates.put(dates.get(i), i);
        }

        Map<String, RegionalProcessedData> regionalProcessedDataMap = new HashMap<>();
        for(Map.Entry<String, List<InputDataPoint>> entry: regionalInputDataMap.entrySet()) {
            String region = entry.getKey();
            List<InputDataPoint> inputDataList = entry.getValue();
            Collections.sort(inputDataList);

            // do some padding in time
            padDataInDate(visitedDates, inputDataList, features, region);
            resetVisitedDates(visitedDates, dates);

            // do some padding among features
            Map<String, List<Long>> featureList = getPaddedFeaturesList(inputDataList, features);

            RegionalProcessedData regionalProcessedData = new RegionalProcessedData(region);
            regionalProcessedData.addFeature(timeFeature);
            for(Map.Entry<String, List<Long>> featureEntry: featureList.entrySet()) {
                ProcessedFeature processedFeature = new ProcessedFeature(featureEntry.getKey(), featureEntry.getValue());
                regionalProcessedData.addFeature(processedFeature);
            }
            regionalProcessedDataMap.put(region, regionalProcessedData);
        }
        return regionalProcessedDataMap;
    }

    /**
     * Returns a map that indicates input data points that are split into different regions.
     *
     * @param data input data
     * @return regional input data map
     */
    private Map<String, List<InputDataPoint>> getRegionalInputDataMap(InputData data) {
        Map<String, List<InputDataPoint>> regionalInputDataMap = new HashMap<>();
        for(InputDataPoint dataPoint: data.getInputDataList()) {
            String region = dataPoint.getRegion();
            if(!regionalInputDataMap.containsKey(region)) {
                List<InputDataPoint> inputDataPointList = new ArrayList<>();
                regionalInputDataMap.put(region, inputDataPointList);
            }
            List<InputDataPoint> regionalDataList = regionalInputDataMap.get(region);
            regionalDataList.add(dataPoint);
        }
        return regionalInputDataMap;
    }

    /**
     * Pads the List of input data point in a region with the overall input data time interval.
     *
     * @param visitedDates the map that tracks occurrence of the dates in the current regional data
     * @param inputDataList list of input data points in this region
     * @param features list of overall features in the input data, to create padding data point
     * @param region the region that the given input data points belong to
     */
    private void padDataInDate(Map<Date, Integer> visitedDates, List<InputDataPoint> inputDataList,
                               List<String> features, String region) {
        int visitedFlag = -1;
        for(InputDataPoint inputDataPoint: inputDataList) {
            if (!visitedDates.containsKey(inputDataPoint.getDate())) {
                throw new UnsupportedOperationException("List of Date not complete");
            }
            if (visitedDates.get(inputDataPoint.getDate()) == visitedFlag) {
                String exceptionMsg = String.format("Duplicate InputDataPoint in %s, on %s", region, inputDataPoint.getDate().toString());
                throw new UnsupportedOperationException(exceptionMsg);
            }
            visitedDates.put(inputDataPoint.getDate(), visitedFlag);
        }
        for(Map.Entry<Date,Integer> visitedDateEntry: visitedDates.entrySet()) {
            int index = visitedDateEntry.getValue();
            Date date = visitedDateEntry.getKey();
            if (visitedDateEntry.getValue() != visitedFlag) {
                InputDataPoint paddingPoint = createPaddingInputDataPoint(features, region, date);
                inputDataList.add(index, paddingPoint);
            }
        }
    }

    /**
     * Creates a new input data point that is generated for the purpose of padding.
     *
     * @param features list of overall features in the Input data
     * @param region the region that this input data point belongs to
     * @param date date of this input data point
     * @return new input data point for padding
     */
    private InputDataPoint createPaddingInputDataPoint(List<String> features, String region, Date date) {
        List<RawFeature> paddingRawFeatures = new ArrayList<>();
        for (String featureName: features) {
            RawFeature paddingRawFeature = new RawFeature(featureName, 0);
            paddingRawFeatures.add(paddingRawFeature);
        }
        return new InputDataPoint(region, date, paddingRawFeatures);
    }

    /**
     * Resets the visited dates map after using it.
     *
     * @param visitedDates the map used for padding
     * @param dates the overall time interval of the input data
     */
    private void resetVisitedDates(Map<Date, Integer> visitedDates, List<Date> dates) {
        for (int i = 0; i < dates.size(); i ++) {
            visitedDates.put(dates.get(i), i);
        }
    }

    /**
     * Returns a map indicating a list of features and their values with padded in features.
     *
     * @param inputDataList the list of input data points
     * @param features the list of over all features
     * @return the padded features list in map<featureName, featureValue>
     */
    private Map<String, List<Long>> getPaddedFeaturesList(List<InputDataPoint> inputDataList, List<String> features){
        Map<String, List<Long>> featureMap = new HashMap<>();
        // initialize featureMap
        for(String feature: features) {
            List<Long> initValue = new ArrayList<>();
            for (int i = 0; i < inputDataList.size(); i++) {
                initValue.add(0L);
            }
            featureMap.put(feature, initValue);
        }
        for(int j = 0; j < inputDataList.size(); j++) {
            for(RawFeature rawFeature: inputDataList.get(j).getFeatures()) {
                featureMap.get(rawFeature.getName()).set(j,rawFeature.getValue());
            }
        }
        return featureMap;
    }

    /**
     * Predicts the data from the processed data.
     *
     * @param data ProcessedData that has been preprocessed
     * @return true if prediction success, false if the feature has already been predicted
     * @throws UnsupportedOperationException if training model failed, or prediction failed
     */
    private boolean predictData(ProcessedData data, FeatureSelection featureSelection) {
        List<Date> timeFeature = data.getDates();
        for(String featureName: featureSelection.getSelectedFeatureNames()) {
            if(data.isPredictFeature(featureName)) {
                return false;
            }
            ArrayList<Attribute> attributes = getAttributeList(featureName);
            String predictionFeatureName = PREDICT_PREFIX + featureName;
            Map<String, ProcessedFeature> predictedFeatureMap = new HashMap<>();
            for(Map.Entry<String, RegionalProcessedData> regionalDataEntry: data.getRegionalProcessedDataMap().entrySet()) {
                ProcessedFeature predictFeature = regionalDataEntry.getValue().getFeature(featureName);
                Instances trainingData = getTrainingData(timeFeature, predictFeature, attributes);
                LinearRegression model = new LinearRegression();
                try {
                    model.buildClassifier(trainingData);
                } catch (Exception e) {
                    String errorMsg = "training model failed: " + e.getMessage();
                    throw new UnsupportedOperationException(errorMsg);
                }
                List<Long> predictions = getPredictions(model, attributes, timeFeature.get(timeFeature.size() - 1));
                ProcessedFeature predictionFeature = new ProcessedFeature(predictionFeatureName, predictions);
                predictedFeatureMap.put(regionalDataEntry.getKey(), predictionFeature);
            }

            // prediction only success when all the regions has successful predictions
            if (predictedFeatureMap.size() == data.getRegionalProcessedDataMap().size()) {
                List<ProcessedFeature> predictedFeatures = new ArrayList<>();
                for(Map.Entry<String, ProcessedFeature> predictedEntry: predictedFeatureMap.entrySet()) {
                    data.addFeature(predictedEntry.getKey(), predictedEntry.getValue());
                    data.addPredictFeature(featureName);
                    predictedFeatures.add(predictedEntry.getValue());
                }
                notifyFeaturePredicted(data.getName(),predictedFeatures);
            }
        }
        return true;
    }

    /**
     * Generate Attributes object for the model with a given predict feature name,
     * the attributes contains two dimensions: time, selected feature.
     *
     * @param predictFeatureName name of the selected feature for prediction
     * @return a list of attribute object
     */
    private ArrayList<Attribute> getAttributeList(String predictFeatureName) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        Attribute predictAttribute = new Attribute(predictFeatureName);
        attributes.add(predictAttribute);
        Attribute timeAttribute = new Attribute(TIME_FEATURE_NAME);
        attributes.add(timeAttribute);
        return attributes;
    }

    /**
     * Constructs the training data set in a {@link Instances} object, while
     * getting values from the processed feature and filter out zero values.
     *
     * @param time the list of Date from the given processed data
     * @param predictFeature the feature to be predicted
     * @param attributes the attributes defined for the model
     * @return the training data set
     */
    private Instances getTrainingData(List<Date> time, ProcessedFeature predictFeature ,
                                      ArrayList<Attribute> attributes) {
        List<Long> featureValue = predictFeature.getValue();
        String predictFeatureName = predictFeature.getFeatureName();
        int capacity = featureValue.size();
        Instances trainingData = new Instances(predictFeatureName, attributes , capacity);
        trainingData.setClassIndex(trainingData.numAttributes()-1);
        for(int i = 0; i < featureValue.size(); i ++) {
            // filter out zero values
            if(featureValue.get(i) != 0L) {
                Instance instance = new DenseInstance(2);
                instance.setValue(attributes.get(0), time.get(i).getTime());
                instance.setValue(attributes.get(1), featureValue.get(i));
                trainingData.add(instance);
            }
        }
        return trainingData;
    }

    /**
     * Predicts the feature value in the next 7 days.
     *
     * @param model the model that has been trained
     * @param attributes the attributes that have been defined
     * @param lastDate the last date of the feature value
     * @return the list of prediction results in the next 7 days
     * @throws UnsupportedOperationException if prediction failed
     */
    private List<Long> getPredictions(LinearRegression model, ArrayList<Attribute> attributes, Date lastDate) {
        int predictionCount = 7;
        List<Long> predictions = new ArrayList<>();
        Instances predictInstances = new Instances("predictionSet", attributes, predictionCount);
        predictInstances.setClassIndex(1);
        for(int i =0; i < predictionCount; i++) {
            Instance predictInstance = new DenseInstance(2);
            long newTime = lastDate.getTime() +  ((i+1)* 86400000L);
            predictInstance.setValue(attributes.get(0), newTime);
            predictInstances.add(predictInstance);
            try {
                double prediction  = model.classifyInstance(predictInstances.get(i));
                predictions.add((long)prediction);
            } catch (Exception e) {
                String errorMsg = "predict data failed: " + e.getMessage();
                throw new UnsupportedOperationException(errorMsg);
            }
        }
        return predictions;
    }

    /**
     * Notifies DataChangeListeners when an input data has been loaded.
     *
     * @param processedData the loaded data
     */
    private void notifyLoadInputData(ProcessedData processedData) {
        for(DataChangeListener listener: dataChangeListenerList) {
            listener.onInputDataLoaded(processedData);
        }
    }

    /**
     * Notifies DataChangeListeners when a data source has been deleted.
     *
     * @param dataSourceName the data source that has been deleted
     */
    private void notifyDeleteInputData(String dataSourceName) {
        for(DataChangeListener listener: dataChangeListenerList) {
            listener.onInputDataDeleted(dataSourceName);
        }
    }

    /**
     * Notifies DataChangeListeners when the display plugin indicate a failure visualization.
     *
     * @param displayConfig the data source that has been deleted
     */
    private void notifyDisplayFailed(DisplayConfig displayConfig) {
        for(DataChangeListener listener: dataChangeListenerList) {
            listener.onDisplayFailed(displayConfig.getDataSourceName(), displayConfig.getDataSourceName());
        }
    }

    /**
     * Notifies DataChangeListeners when the predictions were made.
     *
     * @param predictedFeatures the predicted features
     */
    private void notifyFeaturePredicted(String dataSourceName, List<ProcessedFeature> predictedFeatures) {
        for(DataChangeListener listener: dataChangeListenerList) {
            listener.onFeaturePredicted(dataSourceName,predictedFeatures);
        }
    }
}