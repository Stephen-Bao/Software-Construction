package edu.cmu.cs.cs214.hw5.plugin.dataplugins;

import com.google.gson.Gson;
import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.GeoScope;
import edu.cmu.cs.cs214.hw5.core.InputData;
import edu.cmu.cs.cs214.hw5.core.InputDataPoint;
import edu.cmu.cs.cs214.hw5.core.RawFeature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * DataPlugin for XML data.
 * @author: chenxizh
 */
public class DataPluginXML implements DataPlugin {
    /**
     * Plugin Name.
     */
    private static final String NAME = "COVID_XML";

    /**
     * InputData result to be returned.
     */
    private InputData result = null;

    /**
     * Plugin name getter.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Parse and return input data.
     */
    @Override
    public InputData getInputData(String name, List<String> args) {
        String XMLPath = args.get(0);
        String configPath = args.get(1);
        JSONConfigReaderV2.JSONMetas jsonMetas = readConfig(configPath);
        result = new InputData(name, GeoScope.valueOf(jsonMetas.geoScope));
        parse(
                XMLPath,
                jsonMetas.XMLDataPointKey,
                jsonMetas.dataPointRegionKey,
                jsonMetas.dataPointDateKey,
                new SimpleDateFormat(jsonMetas.dateFormat),
                Arrays.asList(jsonMetas.whitelistRawFeatureKeys)
        );

        return result;
    }

    /**
     * Parse XMLFile with DocumentBuilder.
     */
    private void parse(
            String XMLFilePath,
            String XMLDataPointKey,
            String dataPointRegionKey,
            String dataPointDateKey,
            DateFormat dateFormat,
            List<String> whitelistRawFeatureKeys
    ) {
        try {
            File XMLFile = new File(XMLFilePath);
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(XMLFile);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName(XMLDataPointKey);
            // parse DataPoints
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);;
                String region = getElementData(element, dataPointRegionKey);
                Date date = dateFormat.parse(getElementData(element,
                        dataPointDateKey));
                List<RawFeature> features = new ArrayList<>();
                for (String featureKey : whitelistRawFeatureKeys) {
                    long value;
                    try {
                        value = Long.parseLong(getElementData(element,
                                featureKey));
                    } catch (NumberFormatException e) {
                        value = 0;
                    }
                    RawFeature newFeature = new RawFeature(featureKey, value);
                    features.add(newFeature);
                }
                InputDataPoint dataPoint = new InputDataPoint(region, date,
                        features);
                result.addData(dataPoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get DOM element data.
     */
    private String getElementData(Element element, String key) {
        return element.getElementsByTagName(key)
                .item(0).getTextContent();
    }

    /**
     * Read JSONConfig.
     */
    private JSONConfigReaderV2.JSONMetas readConfig(String configPath){
        Gson gson = new Gson();
        try {
            InputStream inputStream = new FileInputStream(configPath.trim());
            Reader fileReader = new InputStreamReader(
                    inputStream, StandardCharsets.UTF_8);
            return gson.fromJson(
                    fileReader,
                    JSONConfigReaderV2.JSONMetas.class
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "Error when reading config file: " + configPath, e);
        }
    }
}
