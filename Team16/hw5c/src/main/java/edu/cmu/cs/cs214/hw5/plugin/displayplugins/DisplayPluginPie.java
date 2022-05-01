package edu.cmu.cs.cs214.hw5.plugin.displayplugins;

import edu.cmu.cs.cs214.hw5.core.Dimension;
import edu.cmu.cs.cs214.hw5.core.DisplayConfig;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.ProcessedData;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;

import javax.swing.WindowConstants;
import java.util.List;

/**
 * The pie chart display plugin that generates a pie chart for a feature in different regions.
 */
public class DisplayPluginPie implements DisplayPlugin {

    private static final String NAME = "DisplayPluginPie";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Dimension getDimension() {
        return Dimension.ONE_DIMENSIONAL;
    }

    @Override
    public boolean display(ProcessedData processedData, DisplayConfig displayConfig) {
        // Get user selected regions & feature name
        List<String> regions = displayConfig.getFeatureSelections().get(1).getSelectedFeatureNames();
        String featureName = displayConfig.getFeatureSelections().get(0).getSelectedFeatureNames().get(0);

        // Create Chart
        PieChart chart = new PieChartBuilder().width(800).height(600).title(this.getName()).build();

        // Series
        for (String region : regions) {
            List<Long> timeSeriesValues = processedData.getRegionalProcessedDataMap()
                    .get(region).getFeature(featureName).getValue();
            chart.addSeries(region, timeSeriesValues.get(timeSeriesValues.size() - 1));
        }

        // show chart in new thread
        Thread t = new Thread(() -> new SwingWrapper(chart).displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE));
        t.start();

        return true;
    }

}









