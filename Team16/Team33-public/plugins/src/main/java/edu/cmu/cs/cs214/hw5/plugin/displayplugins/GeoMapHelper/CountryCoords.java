package edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

import static edu.cmu.cs.cs214.hw5.plugin.displayplugins.Constant.COUNTRY_COORDS_PATH;

/**
 * CountryCoords that implements Coords
 * stores json object of country coordinates
 */
public class CountryCoords implements Coords{
    private final JSONObject obj;

    /**
     * constructor for CountryCoords
     */
    public CountryCoords(){
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(COUNTRY_COORDS_PATH), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        String jsonString = contentBuilder.toString();
        obj = new JSONObject(jsonString);

    }
    @Override
    public int getLatDegrees(String region) {
        return ((BigDecimal)  obj.getJSONObject(region).get("latitude")).intValue();
    }

    @Override
    public int getLonDegrees(String region) {
        return ((BigDecimal)  obj.getJSONObject(region).get("longitude")).intValue();
    }

    @Override
    public String getName(String region) {
        return (String) obj.getJSONObject(region).get("name");
    }

    @Override
    public boolean hasRegion(String region){
        return obj.has(region);
    }

    /**
     * get random country, for testing only
     * @return random country name
     */
    public String getRandomCountry() {
        String[] someStates = {"CN","CA","US"};
        Random random = new Random();
        int index = random.nextInt(someStates.length);
        return someStates[index];
    }

}
