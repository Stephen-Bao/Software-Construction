package edu.cmu.cs.cs214.hw5.core;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The Immutable and comparable Object that represents a single instance of input data.
 * This object is comparable, can be sorted based on the date.
 */
public final class InputDataPoint implements Comparable<InputDataPoint> {
    private final String region;
    private final Date date;
    private final List<RawFeature> features;

    /**
     * Returns a InputDataPoint object indicating a input data at certain region with
     * values of {@link RawFeature} at a certain date.
     *
     * @param region the region of this input data point
     * @param date the date of this input data point
     * @param features a list of {@link RawFeature} contained in this input data point
     */
    public InputDataPoint(String region, Date date, List<RawFeature> features){
        this.region = region;
        this.date = new Date(date.getTime());
        this.features = features;
    }

    /**
     * Getter of the region.
     *
     * @return region of this input data point
     */
    public String getRegion() {
        return region;
    }

    /**
     * Getter of the date.
     *
     * @return date of this input data point
     */
    public Date getDate() {
        return new Date(date.getTime());
    }

    /**
     * Getter of the features.
     *
     * @return a list of {@link RawFeature} in this input data point
     */
    public List<RawFeature> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return "InputDataPoint{" +
                "region='" + region + '\'' +
                ", date=" + date +
                ", features=" + features +
                "}\n";

    }

    @Override
    public int compareTo(InputDataPoint o) {
        return getDate().compareTo(o.getDate());
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof InputDataPoint)) {
            return false;
        }
        InputDataPoint other = (InputDataPoint) o;
        return this.hashCode() == other.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, date, features);
    }
}
