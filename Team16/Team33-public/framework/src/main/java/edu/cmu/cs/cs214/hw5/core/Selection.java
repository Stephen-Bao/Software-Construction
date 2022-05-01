package edu.cmu.cs.cs214.hw5.core;

/**
 * Enum that represents the available selection of the {@link FeatureSelection}
 */
public enum Selection {
    SINGLE("Single"),
    MULTIPLE("Multiple");
    private String name;

    Selection(String name) {
        this.name = name;
    }

    /**
     * Getter of the selection name
     *
     * @return the name of the selection
     */
    public String getName() {
        return this.name;
    }
}
