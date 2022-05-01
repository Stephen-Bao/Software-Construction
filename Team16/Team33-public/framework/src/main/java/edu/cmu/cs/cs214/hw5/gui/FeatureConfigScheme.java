package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DisplayConfig;
import edu.cmu.cs.cs214.hw5.core.FeatureSelection;
import edu.cmu.cs.cs214.hw5.core.Framework;
import edu.cmu.cs.cs214.hw5.core.Selection;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A feature selection and configuration panel maintaining both data source information
 * and all features which can be visualized. This class also maintains JFrame components
 * to facilitate user interaction.
 */
public class FeatureConfigScheme extends JPanel {
    /** A mapping from a list of features to a list of swing components */
    private final Map<FeatureSelection, JList<String>> featureSelectionMap;
    
    /**
     * Constructs a feature configuration panel instance associated with a given
     * data source, a display plugin. This class belongs to a {@link VisualConfigScheme}
     * of the given data source.
     *
     * @param dataSourceName    the data source name
     * @param displayConfig     the display configuration object
     * @param parent            the parent panel which is a {@link VisualConfigScheme}
     * @param core              the framework core instance
     * @param frame             the parent frame for showing message pop-ups
     */
    public FeatureConfigScheme(String dataSourceName, DisplayConfig displayConfig, VisualConfigScheme parent, Framework core, JFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setBackground(Color.WHITE);
        featureSelectionMap = new LinkedHashMap<>();
    
        JLabel displayName = new JLabel(displayConfig.getDisplayPlugin().getName(), JLabel.LEFT);
        JButton addChartButton = new JButton("Add Chart");
        setBorder(BorderFactory.createTitledBorder(displayConfig.getDisplayPlugin().getName()));
        
        JPanel nameAndButton = new JPanel();
        nameAndButton.setLayout(new BoxLayout(nameAndButton, BoxLayout.X_AXIS));
        nameAndButton.setBackground(Color.WHITE);
        nameAndButton.add(displayName);
        nameAndButton.add(addChartButton);
    
        JPanel dimensionsPanel = new JPanel();
        dimensionsPanel.setLayout(new BoxLayout(dimensionsPanel, BoxLayout.X_AXIS));
        dimensionsPanel.setBackground(Color.WHITE);
        dimensionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        int dimensionIndex = 1;
        for (FeatureSelection fs : displayConfig.getFeatureSelections()) {
            List<String> featureNames = fs.getSelectedFeatureNames();
            String[] array = featureNames.toArray(new String[0]);
            JList<String> component = new JList<>(array);
            component.setVisibleRowCount(10);
            
            boolean singleSelect = Selection.SINGLE.equals(fs.getSelection());
            component.setSelectionMode(singleSelect ? ListSelectionModel.SINGLE_SELECTION :
                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            
            JScrollPane sp = new JScrollPane(component);
            sp.setBorder(BorderFactory.createTitledBorder("Dimension " + dimensionIndex +
                    " (" + fs.getSelection().getName() + ")"));
            dimensionIndex += 1;
            sp.setPreferredSize(new Dimension(60, 150));
            dimensionsPanel.add(sp);
            featureSelectionMap.put(fs, component);
        }
        
        addChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<FeatureSelection> afterConfigs = new ArrayList<>();
                boolean emptyFlag = false;
                for (Map.Entry<FeatureSelection, JList<String>> entry : featureSelectionMap.entrySet()) {
                    List<String> features = entry.getValue().getSelectedValuesList();
                    if (features.size() == 0) {
                        emptyFlag = true;
                    }
                    entry.getValue().clearSelection();
                    FeatureSelection newSelection = new FeatureSelection(entry.getKey().getSelection(), features);
                    afterConfigs.add(newSelection);
                }
                if (emptyFlag) {
                    JOptionPane.showMessageDialog(parent, "Please choose at least one feature.",
                            "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    core.addDisplayConfig(new DisplayConfig(dataSourceName, displayConfig.getDisplayPlugin(), afterConfigs));
                    parent.increaseChart();
                }
            }
        });
        
        add(nameAndButton);
        add(dimensionsPanel);
    }
    
    /**
     * Clears user selection in all JLists.
     */
    protected void clearConfig() {
        for (JList<String> jList : featureSelectionMap.values()) {
            jList.clearSelection();
        }
    }
}
