package edu.cmu.cs.cs214.hw5.plugins.display;

import edu.cmu.cs.cs214.hw5.core.Dataset;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.VaccineAnalysisFramework;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * An example display plugin drawing a line chart for cumulative administered
 * doses versus different date.
 */
public class CumulativeDosesAdministeredPerDayLineChart
        implements DisplayPlugin {
    private static final int DATE_COUNT = 15;
    private VaccineAnalysisFramework core;

    /**
     * Defines the name of the display plugin and will be shown in the GUI menu
     * selection.
     * @return name of the plugin
     */
    @Override
    public String name() {
        return "CumulativeDosesAdministeredPerDayLineChart";
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
     * @param dataset  current framework dataset
     * @return JPanel of the chart
     * @throws Exception error when drawing the chart, will be shown in
     *                   the error dialogue.
     */
    @Override
    public JPanel draw(Dataset dataset) {
        // Filter data
        Map<LocalDate, Integer> parentData = dataset
                .getTransCumulativeAdministeredPerDay();
        if (parentData.size() < DATE_COUNT) {
            throw new IllegalArgumentException(
                    "Not enough data to draw the line chart!"
            );
        }
        Map<LocalDate, Integer> data = new TreeMap<>();
        List<LocalDate> dateList = new ArrayList<>(parentData.keySet());
        int index = dateList.size() - 1;
        for (int i = 0; i < DATE_COUNT; i++) {
            LocalDate currentDate = dateList.get(index);
            data.put(currentDate, parentData.get(currentDate));
            index--;
        }

        List<Date> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();

        for (LocalDate localDate : data.keySet()) {
            ZonedDateTime zonedDateTime = localDate.atStartOfDay(
                    ZoneId.systemDefault());
            Date date = Date.from(zonedDateTime.toInstant());
            xData.add(date);
            yData.add(data.get(localDate));
        }

        // Create Chart
        XYChart chart = new XYChartBuilder()
                .width(800).height(600)
                .title(name())
                .xAxisTitle("Date").yAxisTitle("Dose Number")
                .theme(Styler.ChartTheme.GGPlot2).build();

        // Customize Chart
        chart.getStyler().setCursorEnabled(true);
        chart.getStyler().setChartTitleFont(
                new Font(Font.MONOSPACED, Font.BOLD, 24));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSE);
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler().setDecimalPattern("###,###");
        chart.getStyler().setAxisTitleFont(
                new Font(Font.SANS_SERIF, Font.ITALIC, 18));
        chart.getStyler().setDatePattern("MM-dd");
        chart.getStyler().setTimezone(TimeZone.getTimeZone("GMT"));

        // Series
        XYSeries series = chart.addSeries
                ("Administered Vaccine Data", xData, yData);
        series.setLineColor(XChartSeriesColors.BLUE);
        series.setMarkerColor(Color.ORANGE);
        series.setMarker(SeriesMarkers.CIRCLE);
        series.setLineStyle(SeriesLines.SOLID);

        return new XChartPanel<>(chart);
    }

}