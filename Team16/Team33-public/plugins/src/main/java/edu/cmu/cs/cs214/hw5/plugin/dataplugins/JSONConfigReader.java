package edu.cmu.cs.cs214.hw5.plugin.dataplugins;

/**
 * JSONConfigReader class that read a config file
 */
public class JSONConfigReader {

    /**
     * required data fields
     */
    public static class JSONFieldReader {
        public String region;
        public String date;
    }

    /**
     * config meta data
     */
    public static class JSONMetaReader {
        public String geoScope;
        public String dateformat;
        public JSONFieldReader fields;
    }
}
