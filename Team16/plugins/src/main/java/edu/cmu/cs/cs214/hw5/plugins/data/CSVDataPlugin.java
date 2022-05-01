package edu.cmu.cs.cs214.hw5.plugins.data;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.Dataset;
import edu.cmu.cs.cs214.hw5.core.VaccineAnalysisFramework;
import edu.cmu.cs.cs214.hw5.plugins.data.utils.Utility;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * CSVDataPlugin read CSV files and return a dataset.
 */
public class CSVDataPlugin implements DataPlugin {
    VaccineAnalysisFramework core;

    private String timelineVaccineDataSource;
    private String lastHourVaccineDataSource;
    private String populationDataSource;

    private static final int TIMELINE_PROVINCE_INDEX = 0;
    private static final int TIMELINE_DATE_INDEX = 1;
    private static final int TIMELINE_VACCINE_TYPE_INDEX = 2;
    private static final int HOUR_PROVINCE_INDEX = 1;
    private static final int HOUR_DATE_INDEX = 3;
    private static final int HOUR_VACCINE_TYPE_INDEX = 6;
    private static final int DOSES_ALLOC_INDEX = 7;
    private static final int DOSES_SHIPPED_INDEX = 8;
    private static final int DOSES_ADMIN_INDEX = 9;
    private static final int STAGE_ONE_DOSES_INDEX = 10;
    private static final int STAGE_TWO_DOSES_INDEX = 11;

    private static final int POPULATION_PROVINCE_INDEX = 0;
    private static final int POPULATION_INDEX = 1;

    /**
     * Gets the name of the data plugin.
     * @return plugin name
     */
    @Override
    public String name() {
        return "CSVDataPlugin";
    }

    /**
     * Gets the source instruction of the data plugin. It will be displayed in
     * the label of source input dialogue.
     * @return instruction string
     */
    @Override
    public String sourceInstruction() {
        return "Make sure your CSV file path starts with " +
                "`src/main/resources/` and ends with .csv";
    }

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * (if necessary).
     * @param framework The {@link VaccineAnalysisFramework} instance with which
     * the plugin was registered.
     */
    @Override
    public void onRegister(VaccineAnalysisFramework framework) {
        core = framework;
    }

    /**
     * Specify the source for timeline vaccine data.
     * @param src timeline data source
     */
    @Override
    public void setTimelineVaccineDataSource(String src) {
        timelineVaccineDataSource = src;
    }

    /**
     * Specify the source for last hour vaccine data.
     * @param src last hour data source
     */
    @Override
    public void setLastHourVaccineDataSource(String src) {
        lastHourVaccineDataSource = src;
    }

    /**
     * Specify the source for population data.
     * @param src population data source
     */
    @Override
    public void setPopulationDataSource(String src) {
        populationDataSource = src;
    }

    /**
     * Based on the specified data sources, parse each source to
     * create and return a new Dataset instance.
     * @return a new Dataset instance
     * @throws Exception error when accessing data sources, will be shown in
     *                   the error dialogue.
     */
    @Override
    public Dataset getData() throws IOException {
        Dataset data = new Dataset();

        data.setTimelineVaccineData(getTimelineData());
        data.setLastHourVaccineData(getLastHourData());
        data.setPopulationData(getPopulationData());

        return data;
    }

    /**
     * Read the file and get the timeline vaccine data.
     * @return List of VaccineDataRow
     * @throws IOException error when read the file
     */
    private List<Dataset.VaccineDataRow> getTimelineData() throws IOException {
        List<Dataset.VaccineDataRow> rows = new ArrayList<>();
        if (timelineVaccineDataSource.isEmpty()) {
            return rows;
        }

        return readVaccineRowsFromCSV(timelineVaccineDataSource);
    }

    /**
     * Read the file and get the last hour vaccine data.
     * @return List of VaccineDataRow
     * @throws IOException error when read the file
     */
    private List<Dataset.VaccineDataRow> getLastHourData() throws IOException {
        List<Dataset.VaccineDataRow> rows = new ArrayList<>();
        if (lastHourVaccineDataSource.isEmpty()) {
            return rows;
        }

        return readVaccineRowsFromCSV(lastHourVaccineDataSource);
    }

    /**
     * Read the CSV file and parse the data into List of VaccineDataRow.
     * @param filename CSV file pathname
     * @return List of VaccineDataRow
     * @throws IOException error when read the file
     */
    private List<Dataset.VaccineDataRow> readVaccineRowsFromCSV(
            String filename
    ) throws IOException {
        List<Dataset.VaccineDataRow> rows = new ArrayList<>();
        Reader reader = Files.newBufferedReader(Paths.get(filename));
        CSVParser csvParser = new CSVParser(
                reader,
                CSVFormat.DEFAULT.withTrim()
        );

        boolean isTimeline = filename.equals(timelineVaccineDataSource);
        int provinceIndex = isTimeline
                ? TIMELINE_PROVINCE_INDEX : HOUR_PROVINCE_INDEX;
        int dateIndex = isTimeline
                ? TIMELINE_DATE_INDEX : HOUR_DATE_INDEX;
        int vaccineTypeIndex = isTimeline
                ? TIMELINE_VACCINE_TYPE_INDEX : HOUR_VACCINE_TYPE_INDEX;

        csvParser.iterator().next(); // skip header
        for (CSVRecord record : csvParser) {
            Dataset.VaccineDataRow row = new Dataset.VaccineDataRow(
                    record.get(provinceIndex),
                    LocalDate.parse(record.get(dateIndex)),
                    record.get(vaccineTypeIndex),
                    Utility.strToInt(record.get(DOSES_ALLOC_INDEX)),
                    Utility.strToInt(record.get(DOSES_SHIPPED_INDEX)),
                    Utility.strToInt(record.get(DOSES_ADMIN_INDEX)),
                    List.of(
                        Utility.strToInt(record.get(STAGE_ONE_DOSES_INDEX)),
                        Utility.strToInt(record.get(STAGE_TWO_DOSES_INDEX))
                    )
            );
            rows.add(row);
        }

        return rows;
    }

    /**
     * Read the file and get the population data.
     * @return List of PopulationDataRow
     * @throws IOException error when read the file
     */
    private List<Dataset.PopulationDataRow> getPopulationData()
            throws IOException {
        List<Dataset.PopulationDataRow> rows = new ArrayList<>();
        if (populationDataSource.isEmpty()) {
            return rows;
        }

        return readPopulationRowsFromCSV();
    }

    /**
     * Read the population CSV file and parse the data into List of
     * PopulationDataRow.
     * @return List of PopulationDataRow
     * @throws IOException error when read the file
     */
    private List<Dataset.PopulationDataRow> readPopulationRowsFromCSV()
            throws IOException {
        List<Dataset.PopulationDataRow> rows = new ArrayList<>();

        Reader reader = Files.newBufferedReader(Paths.get(
                populationDataSource
        ));
        CSVParser csvParser = new CSVParser(
                reader,
                CSVFormat.DEFAULT.withTrim()
        );
        csvParser.iterator().next(); // skip header
        for (CSVRecord record : csvParser) {
            Dataset.PopulationDataRow row = new Dataset.PopulationDataRow(
                    record.get(POPULATION_PROVINCE_INDEX),
                    Utility.strToInt(record.get(POPULATION_INDEX))
            );
            rows.add(row);
        }

        return rows;
    }
}
