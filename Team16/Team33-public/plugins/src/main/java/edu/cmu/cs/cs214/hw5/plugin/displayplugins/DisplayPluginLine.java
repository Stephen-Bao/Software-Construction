package edu.cmu.cs.cs214.hw5.plugin.displayplugins;

import edu.cmu.cs.cs214.hw5.core.Dimension;
import edu.cmu.cs.cs214.hw5.core.DisplayConfig;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GeoScope;
import edu.cmu.cs.cs214.hw5.core.ProcessedData;
import edu.cmu.cs.cs214.hw5.core.ProcessedFeature;
import edu.cmu.cs.cs214.hw5.core.RegionalProcessedData;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.CountryCoords;
import edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper.StateCoords;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.WindowConstants;
import java.util.ArrayList;
import java.util.List;

import static edu.cmu.cs.cs214.hw5.core.Constant.PREDICT_PREFIX;
import static edu.cmu.cs.cs214.hw5.core.Constant.TIME_FEATURE_NAME;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.DAY_INT;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.PLOT_HEIGHT;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.PLOT_WIDTH;

/**
 * DisplayPluginLine that implements DisplayPlugin
 * and displays line chart visualization
 * Visualization library referenced from XChart https://knowm.org/open-source/xchart/xchart-example-code/
 */
public class DisplayPluginLine implements DisplayPlugin {
    private static final String NAME = "DisplayPluginLine";
    private static final Dimension DIMENSION = Dimension.TWO_DIMENSIONAL;
    private static final CountryCoords COUNTRY_COORDS = new CountryCoords();
    private static final StateCoords STATE_COORDS = new StateCoords();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Dimension getDimension() {

        return DIMENSION;
    }

    @Override
    public boolean display(ProcessedData processedData, DisplayConfig displayConfig) {
        // get list of regions to show plot
        List<String> regions = displayConfig.getFeatureSelections().get(2).getSelectedFeatureNames();

        GeoScope scope = processedData.getGeoScope();
        for(String region : regions){
            RegionalProcessedData data = processedData.getRegionalProcessedDataMap().get(region);
            // continue if data is null or region is not in our coordinate resources
            if(data==null){
                continue;
            }
            // get x axis name
            String xAxisName = displayConfig.getFeatureSelections().get(0).getSelectedFeatureNames().get(0);
            List<Long> xAxis = data.getFeature(xAxisName).getValue();
            // get y axis names (1 or multiple)
            List<String> featureNames = displayConfig.getFeatureSelections().get(1).getSelectedFeatureNames();
            // get chart
            XYChart chart = getXyChart(scope, region);

            if(isAppendable(processedData, xAxisName)){
                List<Long> newXAxis = appendPrediction(processedData, xAxisName, data);
               for(String featureName: featureNames){
                   if(isAppendable(processedData, featureName)){
                       List<Long> newYAxis = appendPrediction(processedData, featureName, data);
                       // new predicted line
                       addSeriesToChart(chart, newXAxis, newYAxis, xAxisName, featureName);
                   } else{
                       addSeriesToChart(chart, xAxis, data.getFeature(featureName).getValue(), xAxisName, featureName);
                   }
               }
            } else{
                for(String featureName: featureNames){
                    addSeriesToChart(chart, xAxis, data.getFeature(featureName).getValue(), xAxisName, featureName);
                }
            }
            // show chart in new thread
            Thread t = new Thread(() -> new SwingWrapper(chart).displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE));
            t.start();
        }
        return true;
    }

    /**
     * Get the line chart object
     * @param scope scope of the plot
     * @param region region of the plot
     * @return line chart object
     */
    private XYChart getXyChart(GeoScope scope, String region) {
        XYChart chart = new XYChartBuilder().width(PLOT_WIDTH).height(PLOT_HEIGHT).title(getClass().getSimpleName()).build();
        chart.getStyler().setDatePattern("MM/dd");
        chart.getStyler().setXAxisTickMarkSpacingHint(50);

        try{
            if(scope == GeoScope.GLOBAL){
                chart.setTitle("Line chart for "+ COUNTRY_COORDS.getName(region));
            } else if (scope == GeoScope.US) {
                chart.setTitle("Line chart for "+ STATE_COORDS.getName(region));
            }
        } catch (IllegalArgumentException e){
            // the region name is not in our coordinate resources, display region code instead
            chart.setTitle("Line chart for " + region);
        }
        return chart;
    }

    /**
     * Add series (line) to the line chart
     * @param chart line chart object
     * @param x x axis
     * @param y y axis
     * @param xName name of x axis
     * @param yName name of y axis
     */
    private void addSeriesToChart(XYChart chart, List<?> x, List<Long> y, String xName, String yName){
        if(x.size() != y.size()){
            throw new UnsupportedOperationException("X axis "+xName+" and y axis "+yName+" not the same size");
        }
        chart.addSeries(yName, x, y).setMarker(SeriesMarkers.NONE);
    }

    /**
     * Combine predicted and original data
     * @param processedData processed data from the framework
     * @param featureName name of feature
     * @param data processed data of the current region being plotted
     * @return combined data
     */
    private List<Long> appendPrediction(ProcessedData processedData, String featureName, RegionalProcessedData data){
        List<Long> axis = new ArrayList<>(data.getFeature(featureName).getValue());
        if(processedData.isPredictFeature(featureName)){
            String predictedFeatureName = PREDICT_PREFIX + featureName;
            ProcessedFeature predictedFeature = data.getFeature(predictedFeatureName);
            List<Long> predictedValueList = predictedFeature.getValue(); // y
            predictedValueList.addAll(0, axis);
            return predictedValueList;
        }

        Long lastDay = axis.get(axis.size() - 1);
        for(int i = 0; i < 7; i++){
            axis.add(lastDay + DAY_INT*(i+1));
        }
        return axis;
    }

    /**
     * checks if a feature can be appended
     * @param processedData processed data from the framework
     * @param featureName name of feature
     * @return true if feature can be appended, else false
     */
    private boolean isAppendable(ProcessedData processedData, String featureName){
        return (processedData.isPredictFeature(featureName) || featureName.equals(TIME_FEATURE_NAME));
    }


}