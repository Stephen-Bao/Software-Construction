package edu.cmu.cs.cs214.hw5.core;

/**
 * The object that represents the raw feature indicating the name and the value of a feature,
 * in the {@link InputDataPoint}
 */
public class RawFeature {
    private final String name;
    private final long value;

    /**
     * Return a RawFeature object with specified name and value
     *
     * @param name the name of the feature
     * @param val the value of the feature
     */
    public RawFeature(String name, long val) {
        this.name = name;
        this.value = val;
    }

    /**
     * Getter of the value of the feature
     *
     * @return value
     */
    public long getValue() {
        return value;
    }

    /**
     * Getter of the name of the feature
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RawFeature{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
