package edu.cmu.cs.cs214.hw5.core;

import java.util.List;

/**
 * A list of features that can be selected to be visualized for a dimension,
 * associating with the selection mode (single or multiply selection) of
 * this dimension.
 */
public class FeatureSelection {
    /** A enum representing selection mode, either single or multiple selection */
    private final Selection selection;
    /** A list of feature names */
    private final List<String> selectedFeatureNames;
    
    /**
     * Constructs a feature selection instance by indicating the selection mode
     * and the list of feature names.
     *
     * @param selection             the selection mode
     * @param selectedFeatureNames  the list of feature names
     */
    public FeatureSelection(Selection selection, List<String> selectedFeatureNames) {
        this.selection = selection;
        this.selectedFeatureNames = selectedFeatureNames;
    }
    
    /**
     * Getter method of the Selection field.
     *
     * @return      the Selection field
     */
    public Selection getSelection() {
        return selection;
    }
    
    /**
     * Getter method of the selectedFeatureNames field.
     *
     * @return      the list of feature names
     */
    public List<String> getSelectedFeatureNames() {
        return selectedFeatureNames;
    }
}
