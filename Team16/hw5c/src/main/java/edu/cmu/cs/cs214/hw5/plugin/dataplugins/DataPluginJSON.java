package edu.cmu.cs.cs214.hw5.plugin.dataplugins;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GeoScope;
import edu.cmu.cs.cs214.hw5.core.InputData;
import edu.cmu.cs.cs214.hw5.core.InputDataPoint;
import edu.cmu.cs.cs214.hw5.core.RawFeature;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The JSON data plugin that is able to load data from a JSON file with a config file.
 */
public class DataPluginJSON implements DataPlugin {

    private static final String NAME = "COVID_JSON";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public InputData getInputData(String name, List<String> args) {
        Gson gson = new Gson();
        String configFile = args.get(1);

        try (Reader reader = new FileReader(new File(configFile))) {

            // Get config data
            JSONConfigData configData = gson.fromJson(reader, JSONConfigData.class);

            InputData inputData = new InputData(name, GeoScope.valueOf(configData.geoScope));
            addInputDataPoint(inputData, args.get(0), configData);

            return inputData;

        } catch (IOException e) {
            throw new IllegalArgumentException("Error when reading file: " + configFile, e);
        }
    }

    private void addInputDataPoint(InputData inputData, String filePath, JSONConfigData configData) {
        SimpleDateFormat formatter = new SimpleDateFormat(configData.dateformat);

        try (Reader reader = new FileReader(new File(filePath))) {

            // Directly parse the data json file as a JsonElement
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(reader);

            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();

                // Parse each JSON object
                for (int i = 0; i < array.size(); i++) {
                    JsonObject object = array.get(i).getAsJsonObject();

                    // Get region and date
                    String region = object.get(configData.regionAndDate.region).getAsString();
                    Date date = formatter.parse(object.get(configData.regionAndDate.date).getAsString());

                    // Get raw fearures
                    List<RawFeature> rawFeatures = new ArrayList<>();
                    for (String feature : configData.features) {
                        int value = parseFieldAsInt(object.get(feature).getAsString());
                        rawFeatures.add(new RawFeature(feature, value));
                    }

                    inputData.addData(new InputDataPoint(region, date, rawFeatures));
                }
            }

        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("Error when reading file: " + filePath, e);
        }
    }

    private int parseFieldAsInt(String field) {
        int ret = 0;
        try {
            ret = Integer.parseInt(field.replaceAll("^\"|\"$", ""));
            return ret;
        }
        catch (IllegalArgumentException e) {
            return 0;
        }
    }

    /**
     * The helper class to load data from the json config file
     */
    //CHECKSTYLE:OFF
    private static class JSONConfigData {
        public String geoScope;
        public String dateformat;
        public JSONRegionAndDateData regionAndDate;
        public String[] features;

        public static class JSONRegionAndDateData {
            public String region;
            public String date;
        }
    }
    //CHECKSTYLE:ON

}
