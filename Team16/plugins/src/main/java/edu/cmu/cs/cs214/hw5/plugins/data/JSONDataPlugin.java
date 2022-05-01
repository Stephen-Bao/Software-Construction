package edu.cmu.cs.cs214.hw5.plugins.data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.Dataset;
import edu.cmu.cs.cs214.hw5.core.VaccineAnalysisFramework;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example data plugin for parsing and loading local JSON files
 * to be registered with the the {@link VaccineAnalysisFramework}.
 */
public class JSONDataPlugin implements DataPlugin {
    private String timelineVaccineDataSource;
    private String lastHourVaccineDataSource;
    private String populationDataSource;
    private VaccineAnalysisFramework core;

    /**
     * Default constructor.
     */
    public JSONDataPlugin() {
        timelineVaccineDataSource = null;
        lastHourVaccineDataSource = null;
        populationDataSource = null;
    }

    /**
     * Gets the name of the data plugin.
     * @return plugin name
     */
    @Override
    public String name() {
        return "JSONDataPlugin";
    }

    /**
     * Gets the source instruction of the data plugin. It will be displayed in
     * the label of source input dialogue.
     * @return instruction string
     */
    @Override
    public String sourceInstruction() {
        return "Make sure your JSON file path starts with " +
                "`src/main/resources/` and ends with .json";
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
    public Dataset getData() {
        Dataset data = new Dataset();

        data.setTimelineVaccineData(parseTimelineVaccineData());
        data.setLastHourVaccineData(parseLastHourVaccineData());
        data.setPopulationData(parsePopulationData());

        return data;
    }

    /**
     * Parse timeline data from JSON file.
     * @return List of VaccineDataRowV
     */
    private List<Dataset.VaccineDataRow> parseTimelineVaccineData() {
        if (timelineVaccineDataSource.isEmpty()) {
            System.out.println("Timeline vaccine data source not specified!");
            return new ArrayList<>();
        }

        return parseVaccineDataRowList(timelineVaccineDataSource);
    }

    /**
     * Parse last hour data from JSON file.
     * @return List of VaccineDataRowV
     */
    private List<Dataset.VaccineDataRow> parseLastHourVaccineData() {
        if (lastHourVaccineDataSource.isEmpty()) {
            System.out.println("Last hour vaccine data source not specified!");
            return new ArrayList<>();
        }

        return parseVaccineDataRowList(lastHourVaccineDataSource);
    }

    /**
     * Parse population data from JSON file.
     * @return List of PopulationDataRow
     */
    private List<Dataset.PopulationDataRow> parsePopulationData() {
        if (populationDataSource.isEmpty()) {
            System.out.println("Population data source not specified!");
            return new ArrayList<>();
        }

        List<Dataset.PopulationDataRow> populationData = new ArrayList<>();
        Gson gson = new Gson();

        try (Reader reader = new FileReader(populationDataSource)) {
            JSONPopulationDataRow[] result = gson.fromJson(reader,
                    JSONPopulationDataRow[].class);
            for (JSONPopulationDataRow jsonPopulationDataRow : result) {
                populationData.add(new Dataset.PopulationDataRow(
                        jsonPopulationDataRow.provinceState,
                        jsonPopulationDataRow.population
                ));
            }
            return populationData;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading file: "
                    + populationDataSource, e);
        }
    }

    /**
     * Parse VaccineData rows from JSON file.
     * @param filename
     * @return List of VaccineDataRowV
     */
    private List<Dataset.VaccineDataRow> parseVaccineDataRowList(
            final String filename) {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(filename)) {
            JSONVaccineDataRow[] result = gson.fromJson(reader,
                    JSONVaccineDataRow[].class);
            List<Dataset.VaccineDataRow> vaccineDataRowList = new ArrayList<>();

            for (JSONVaccineDataRow jsonVaccineDataRow : result) {
                int dosesAlloc = parseFieldAsInt(jsonVaccineDataRow.dosesAlloc);
                int dosesShipped = parseFieldAsInt(
                        jsonVaccineDataRow.dosesShipped);
                int dosesAdmin = parseFieldAsInt(jsonVaccineDataRow.dosesAdmin);
                int stageOneDoses = parseFieldAsInt(
                        jsonVaccineDataRow.stageOneDoses);
                int stageTwoDoses = parseFieldAsInt(
                        jsonVaccineDataRow.stageTwoDoses);
                List<Integer> stageDoses = List.of(
                        stageOneDoses, stageTwoDoses);

                vaccineDataRowList.add(
                        new Dataset.VaccineDataRow(
                                jsonVaccineDataRow.provinceState,
                                LocalDate.parse(jsonVaccineDataRow.date),
                                jsonVaccineDataRow.vaccineType,
                                dosesAlloc,
                                dosesShipped,
                                dosesAdmin,
                                stageDoses
                        ));
            }
            return vaccineDataRowList;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error when loading file: "
                    + filename, e);
        }
    }

    /**
     * Parse string number into integer.
     * @param field input string
     * @return field integer, -1 if empty
     */
    private int parseFieldAsInt(String field) {
        return field.equals("") ?
                -1 : (int)Double.parseDouble(field);
    }

    //CHECKSTYLE:OFF
    private static class JSONVaccineDataRow {

        @SerializedName(value = "Province_State")
        public String provinceState;

        @SerializedName(value = "Date")
        public String date;

        @SerializedName(value = "Vaccine_Type")
        public String vaccineType;

        @SerializedName(value = "Doses_alloc")
        public String dosesAlloc;

        @SerializedName(value = "Doses_shipped")
        public String dosesShipped;

        @SerializedName(value = "Doses_admin")
        public String dosesAdmin;

        @SerializedName(value = "Stage_One_Doses")
        public String stageOneDoses;

        @SerializedName(value = "Stage_Two_Doses")
        public String stageTwoDoses;

        @Override
        public String toString() {
            return "JSONVaccineDataRow{" +
                    "provinceState='" + provinceState + '\'' +
                    ", date='" + date + '\'' +
                    ", vaccineType='" + vaccineType + '\'' +
                    ", dosesAlloc='" + dosesAlloc + '\'' +
                    ", dosesShipped='" + dosesShipped + '\'' +
                    ", dosesAdmin='" + dosesAdmin + '\'' +
                    ", stageOneDoses='" + stageOneDoses + '\'' +
                    ", stageTwoDoses='" + stageTwoDoses + '\'' +
                    '}';
        }
    }

    private static class JSONPopulationDataRow {

        @SerializedName(value = "Province_State")
        public String provinceState;

        @SerializedName(value = "Population")
        public int population;

    }
}
