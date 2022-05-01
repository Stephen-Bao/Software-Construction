package edu.cmu.cs.cs214.hw5.plugin.displayplugins;

import edu.cmu.cs.cs214.hw5.core.Dimension;
import edu.cmu.cs.cs214.hw5.core.DisplayConfig;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.GeoScope;
import edu.cmu.cs.cs214.hw5.core.ProcessedData;
import edu.cmu.cs.cs214.hw5.core.ProcessedFeature;
import edu.cmu.cs.cs214.hw5.core.RegionalProcessedData;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.markers.SeriesMarkers;

import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.PLOT_HEIGHT;
import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.PLOT_WIDTH;

/**
 * Display plugin to show Bar chart.
 */
public class DisplayPluginBar implements DisplayPlugin {
    /**
     * Display plugin name.
     */
    private static final String NAME = "DisplayPluginBar";

    /**
     * Display plugin dimension.
     */
    private static final Dimension DIMENSION = Dimension.TWO_DIMENSIONAL;

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
        List<String> regions = displayConfig
                .getFeatureSelections().get(2).getSelectedFeatureNames();

        GeoScope scope = processedData.getGeoScope();
        for(String region : regions){
            RegionalProcessedData data = processedData
                    .getRegionalProcessedDataMap().get(region);
            // continue if data is null or region is not in our coordinate resources
            if(data==null){
                continue;
            }
            // get x axis name
            String xAxisName = displayConfig
                    .getFeatureSelections().get(0)
                    .getSelectedFeatureNames().get(0);
            List<Long> xAxisData = data
                    .getFeature(xAxisName).getValue();
            // get y axis names (1 or multiple)
            List<String> featureNames = displayConfig
                    .getFeatureSelections().get(1)
                    .getSelectedFeatureNames();
            // get chart
            CategoryChart chart = new CategoryChartBuilder()
                    .width(PLOT_WIDTH)
                    .height(PLOT_HEIGHT)
                    .title(getClass().getSimpleName())
                    .build();
            chart.getStyler().setDatePattern("MM/dd");
            chart.getStyler().setXAxisTickMarkSpacingHint(50);
            chart.getStyler().setAvailableSpaceFill(.96);
            chart.getStyler().setToolTipsEnabled(true);
            chart.getStyler().setAxisTickLabelsFont(new Font(null, 0, 10));
            chart.getStyler().setYAxisDecimalPattern("###,###.#");
            chart.getStyler().setXAxisLabelRotation(90);
            chart.setTitle("Line chart for " + scope + ":" + region);

            for(String featureName: featureNames){
                chart.addSeries(
                        featureName,
                        xAxisData,
                        data.getFeature(featureName).getValue()
                ).setMarker(SeriesMarkers.NONE);
            }
            // show chart in new thread
            Thread t = new Thread(
                    () -> new SwingWrapper(chart)
                            .displayChart()
                            .setDefaultCloseOperation(
                                    WindowConstants.HIDE_ON_CLOSE));
            t.start();
        }
        return true;
    }
}