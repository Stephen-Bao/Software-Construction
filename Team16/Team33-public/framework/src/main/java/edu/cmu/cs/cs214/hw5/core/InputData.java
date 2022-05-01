package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The object that represents the input data source returned from the data plugin
 */
public class InputData {
    private final String dataSourceName;
    private final GeoScope geoScope;
    private final List<InputDataPoint> inputDataList;

    /**
     * Returns an input data object that has been initialized in {@link DataPlugin}
     * DataPlugin should specify the name of the data source and the geometric scope to be loaded.
     *
     * @param dataSourceName the name of the data source
     * @param geoScope the geometric scope of the input data
     */
    public InputData(String dataSourceName, GeoScope geoScope){
        this.geoScope = geoScope;
        this.dataSourceName = dataSourceName;
        inputDataList = new ArrayList<>();
    }

    /**
     * Adds a {@link InputDataPoint} to the input data.
     *
     * @param inputDataPoint the input data point
     */
    public void addData(InputDataPoint inputDataPoint){
        inputDataList.add(inputDataPoint);
    }

    /**
     * Returns the name of the input data.
     *
     * @return data source name
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * Returns the geoScope of the input data.
     *
     * @return geoScope
     */
    public GeoScope getGeoScope() {return geoScope;}

    /**
     * Returns the list of input data points from the current data source.
     *
     * @return a defensive copy of input data points
     */
    public List<InputDataPoint> getInputDataList() {
        return new ArrayList<>(inputDataList);
    }

    @Override
    public String toString() {
        return "InputData{" +
                "name='" + dataSourceName + '\'' +
                ", inputDataList=" + inputDataList +
                '}';
    }
}
