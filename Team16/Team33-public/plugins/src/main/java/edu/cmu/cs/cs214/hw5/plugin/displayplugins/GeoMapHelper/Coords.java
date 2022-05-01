package edu.cmu.cs.cs214.hw5.plugin.displayplugins.GeoMapHelper;

/**
 * Interface for US states and Countries coordinates
 */
public interface Coords {
    /**
     * this method returns the latitude of a region
     * @param region region
     * @return latitude
     */
    int getLatDegrees(String region);

    /**
     * this method returns the longitude of a region
     * @param region region
     * @return longitude
     */
    int getLonDegrees(String region);

    /**
     * this method returns the full name of a region
     * @param region region
     * @return region name
     */
    String getName(String region);

    /**
     * this method checks if a region is in the country code list
     * @param region region
     * @return true if exist, else false
     */
    boolean hasRegion(String region);
}
