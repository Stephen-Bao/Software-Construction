package edu.cmu.cs.cs214.hw5.plugins.display;

import edu.cmu.cs.cs214.hw5.core.Dataset;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.VaccineAnalysisFramework;

import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.LegendPosition;

/**
 * Show the first dose and second dose numbers for each state in United States
 * based on the input last hour vaccine data.
 */
public class FirstAndSecondDoseBarChart implements DisplayPlugin {
    VaccineAnalysisFramework core;

    /**
     * Defines the name of the display plugin and will be shown in the GUI menu
     * selection.
     * @return name of the plugin
     */
    @Override
    public String name() {
        return "FirstAndSecondDoseBarChart";
    }

    /**
     * On registered and pass the framework instance to plugin for the future
     * invoke.
     * @param framework instance of current framework
     */
    @Override
    public void onRegister(VaccineAnalysisFramework framework) {
        core = framework;
    }

    /**
     * Draw charts with current dataset.
     * @param data  current framework dataset
     * @return JPanel of the chart
     * @throws Exception error when drawing the chart, will be shown in
     *                   the error dialogue.
     */
    @Override
    public JPanel draw(Dataset data) {
        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800).height(600)
                .title("First v.s. Second Dose per State")
                .xAxisTitle("States").yAxisTitle("Doses")
                .theme(Styler.ChartTheme.GGPlot2).build();

        // Customize Chart
        chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(.96);
        chart.getStyler().setToolTipsEnabled(true);
        chart.getStyler().setAxisTickLabelsFont(new Font(null, 0, 10));
        chart.getStyler().setYAxisDecimalPattern("###,###.#");
        chart.getStyler().setXAxisLabelRotation(45);

        // Series
        chart.addSeries("First Dose",
                getXSeriesData(data),
                getYSeriesData(data, 0)
        );
        chart.addSeries("Second Dose",
                getXSeriesData(data),
                getYSeriesData(data, 1)
        );

        return new XChartPanel<>(chart);
    }

    /**
     * Get the X series of the data.
     * @param data input dataset
     * @return List of X data string
     */
    private List<String> getXSeriesData(Dataset data) {
        return data.getLastHourVaccineData()
                .stream()
                .filter(row -> row.getVaccineType().equals("All"))
                .map(row -> row.getProvinceState())
                .collect(Collectors.toList());
    }

    /**
     * Get the Y series of the data.
     * @param data input dataset
     * @param doseIndex input dose index to get target phase
     * @return List of Y data string
     */
    private List<Integer> getYSeriesData(Dataset data, int doseIndex) {
        return data.getLastHourVaccineData()
                .stream()
                .filter(row -> row.getVaccineType().equals("All"))
                .map(row -> row.getStageDoses().get(doseIndex))
                .collect(Collectors.toList());
    }
}
