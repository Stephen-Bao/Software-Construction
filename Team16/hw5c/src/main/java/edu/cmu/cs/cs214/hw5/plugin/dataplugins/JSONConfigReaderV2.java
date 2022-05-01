package edu.cmu.cs.cs214.hw5.plugin.dataplugins;

import java.util.Arrays;

/**
 * JSONConfigReader class that read a config file
 */
public class JSONConfigReaderV2 {
    /**
     * config meta data
     */
    public static class JSONMetas {
        public String geoScope;
        public String dateFormat;
        public String XMLDataPointKey;
        public String dataPointRegionKey;
        public String dataPointDateKey;
        // only specified white-listed number keys could be parsed as
        // RawFeatures
        public String[] whitelistRawFeatureKeys;

        @Override
        public String toString() {
            return "JSONMetas{" +
                    "geoScope='" + geoScope + '\'' +
                    ", dateFormat='" + dateFormat + '\'' +
                    ", regionKey='" + dataPointRegionKey + '\'' +
                    ", dateKey='" + dataPointDateKey + '\'' +
                    ", XMLDataPointKey='" + XMLDataPointKey + '\'' +
                    ", whitelistNumberKeys=" +
                    Arrays.toString(whitelistRawFeatureKeys) +
                    '}';
        }
    }
}
