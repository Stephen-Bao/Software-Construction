package edu.cmu.cs.cs214.hw5.plugin.dataplugins;

import com.google.gson.Gson;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GeoScope;
import edu.cmu.cs.cs214.hw5.core.InputData;
import edu.cmu.cs.cs214.hw5.core.InputDataPoint;
import edu.cmu.cs.cs214.hw5.core.RawFeature;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * DataPluginCSV class that implements DataPlugin and
 */
public class DataPluginCSV implements DataPlugin {
    private static final String NAME = "COVID_CSV";


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public InputData getInputData(String name, List<String> args) {
        JSONConfigReader.JSONMetaReader jsonMetaReader = readConfig(args.get(1));
        return readCSV(args.get(0), name, jsonMetaReader);
    }

    private JSONConfigReader.JSONMetaReader readConfig(String configPath){
        return parse(configPath);
    }

    /**
     * Parse a json config file
     * @param configPath path to config file
     * @return JSONMetaReader instance
     */
    public static JSONConfigReader.JSONMetaReader parse(String configPath) {
        Gson gson = new Gson();
        if(configPath.trim().equals("")){
            throw new IllegalArgumentException("Missing config file");
        }
        try {
            InputStream inputStream = new FileInputStream(configPath.trim());
            Reader fileReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            return gson.fromJson(fileReader, JSONConfigReader.JSONMetaReader.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error when reading config file: " + configPath, e);
        }
    }

    /**
     * Check if a string is numeric using regular expression
     * reference: https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
     * @param data data to check
     * @return true if numeric, false otherwise
     */
    public boolean isNumber(String data) {
        return data.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Read CSV and parse CSV into InputData instance, based on the metadata from json config file
     * @param path path of CSV
     * @param metaReader metadata instance
     * @return InputData instance
     */
    private InputData readCSV(String path, String name, JSONConfigReader.JSONMetaReader metaReader){
        InputData inputData = new InputData(name, GeoScope.valueOf(metaReader.geoScope));
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path.trim()));
            String firstrow = csvReader.readLine();
            String[] columns = firstrow.split(",");
            // get index for region and date
            int regionIndex = -1;
            int dateIndex = -1;
            for(int i = 0; i < columns.length; i++){
                String col = columns[i].replaceAll("^\"|\"$", "");
                if(col.equals(metaReader.fields.region)){
                    regionIndex = i;
                } else if (col.equals(metaReader.fields.date)) {
                    dateIndex = i;
                }
            }
            checkRequiredFields(regionIndex, dateIndex);

            String row;
            SimpleDateFormat formatter = new SimpleDateFormat(metaReader.dateformat, Locale.ENGLISH);
            // iterate through csv data
            while ((row = csvReader.readLine()) != null) {
                addInputDataPoint(inputData, columns, regionIndex, dateIndex, row, formatter);
            }
            csvReader.close();

        } catch (IOException e){
            throw new IllegalArgumentException("File path is invalid "+e.getMessage());
        } catch (ParseException e){
            throw new UnsupportedOperationException("Date format in config file is incorrect "+e.getMessage());
        }
        return inputData;
    }

    private void checkRequiredFields(int regionIndex, int dateIndex) {
        if(regionIndex == -1 || dateIndex == -1){
            throw new IllegalArgumentException("Missing required data field");
        }
    }

    private void addInputDataPoint(InputData inputData, String[] columns, int regionIndex, int dateIndex, String row, SimpleDateFormat formatter)
            throws ParseException {
        String[] data = row.split(",");
        List<RawFeature> rawFeatures = new ArrayList<>();
        String region = null;
        Date date = null;
        for(int i = 0; i < data.length; i++){
            if(i == regionIndex){
                region = data[i].replaceAll("^\"|\"$", "");
                continue;
            }
            if(i == dateIndex){
                String dateStr = data[i].replaceAll("^\"|\"$", "");
                date = formatter.parse(dateStr);
                continue;
            }
            if(isNumber(data[i])){
                // only include fields that are numeric, because RawFeature value is int type
                rawFeatures.add(new RawFeature(columns[i].replaceAll("^\"|\"$", ""), Integer.parseInt(data[i])));
            }

        }
        // add datapoint if required fields are not empty
        if(region != null && date != null ){
            InputDataPoint point = new InputDataPoint(region, date, rawFeatures);
            inputData.addData(point);
        }
    }
}