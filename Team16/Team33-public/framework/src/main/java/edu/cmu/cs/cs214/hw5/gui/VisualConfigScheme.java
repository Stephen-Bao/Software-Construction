package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DisplayConfig;
import edu.cmu.cs.cs214.hw5.core.Framework;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A data visualization configuration panel maintaining both data source information
 * and corresponding visualisable features. This class also maintains JFrame components
 * to facilitate user interaction.
 */
public class VisualConfigScheme extends JPanel {
    /** A label maintaining the number of added visualization configuration */
    private final JLabel chartCountLabel;
    /** An integer representing the number of added visualization configuration */
    private int chartCount;
    /** A map maintaining the mapping from display plugin to feature configuration panel */
    private final Map<DisplayConfig, FeatureConfigScheme> featureSchemeMap;
    
    /**
     * Constructs a visualization configuration panel instance associated with a given
     * data source and all its applicable display plugins.
     *
     * @param dataSourceName    the data source name
     * @param displayConfigs    a list of all applicable display plugins and their
     *                          specific applicable configurations
     * @param core              the framework core object
     * @param frame             the parent frame for showing message pop-ups
     */
    public VisualConfigScheme(String dataSourceName, List<DisplayConfig> displayConfigs, Framework core, JFrame frame) {
        this.chartCountLabel = new JLabel("[" + dataSourceName + "] added visualization configurations: " + 0);
        this.chartCount = 0;
        this.featureSchemeMap = new LinkedHashMap<>();
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(new Insets(20, 20, 50, 20)));
        
        add(chartCountLabel);
        add(Box.createRigidArea(new Dimension(0, 20))); //gap
    
        if (displayConfigs != null && displayConfigs.size() > 0) {
            for (DisplayConfig displayConfig : displayConfigs) {
                FeatureConfigScheme featureConfigScheme = new FeatureConfigScheme(dataSourceName, displayConfig, this, core, frame);
                featureSchemeMap.put(displayConfig, featureConfigScheme);
                add(featureConfigScheme);
                add(Box.createRigidArea(new Dimension(0, 20))); //gap
            }
        }
    }
    
    /**
     * Increases visualization configurations by one. This method is called each time
     * a user click [Add Chart] button to add a visualization configuration.
     */
    protected void increaseChart() {
        this.chartCount += 1;
        this.chartCountLabel.setText("Added visualization configurations: " + chartCount);
    }
    
    /**
     * Clears user configuration for this data source. This method is called when
     * a user click [Finish Config and Plot] button to produce all configged charts.
     */
    protected void clearConfig() {
        chartCount = 0;
        chartCountLabel.setText("Added visualization configurations: " + 0);
        
        for (FeatureConfigScheme scheme : featureSchemeMap.values()) {
            scheme.clearConfig();
        }
    }
}
