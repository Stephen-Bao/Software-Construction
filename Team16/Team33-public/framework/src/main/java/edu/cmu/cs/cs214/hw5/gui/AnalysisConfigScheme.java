package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.FeatureSelection;
import edu.cmu.cs.cs214.hw5.core.Framework;
import edu.cmu.cs.cs214.hw5.core.ProcessedFeature;
import edu.cmu.cs.cs214.hw5.core.Selection;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A data analysis configuration panel maintaining both data source information
 * and corresponding analysable features, which can be selected for further analysis
 * and prediction. This class also maintains JFrame components to facilitate user
 * interaction.
 */
public class AnalysisConfigScheme extends JPanel {
    /** Name of the data source */
    private final String dataSourceName;
    private final Framework core;
    private final JFrame parent;
    /** A JList maintaining a list of analysable features for user selection */
    private JList<String> unSelectedFeatures;
    private DefaultListModel<String> unSelectedModel;
    private JList<String> selectedFeatures;
    private DefaultListModel<String> selectedModel;
    private final JButton addButton;
    private JList<String> predictResultList;
    private DefaultListModel<String> predictResultModel;
    
    /**
     * Constructs a analysis configuration panel instance associated with a given
     * data source and a {@link FeatureSelection}.
     *
     * @param dataSourceName    the data source name
     * @param featureSelection  the featureSelection object
     */
    public AnalysisConfigScheme(String dataSourceName, FeatureSelection featureSelection, JFrame frame, Framework core) {
        setLayout(new FlowLayout());
        this.dataSourceName = dataSourceName;
        this.core = core;
        this.parent = frame;
        
        if (featureSelection == null || featureSelection.getSelectedFeatureNames().size() == 0) {
            unSelectedFeatures = new JList<>();
        } else {
            List<String> featureNames = featureSelection.getSelectedFeatureNames();
            Collections.sort(featureNames);
            unSelectedModel = new DefaultListModel<>();
            unSelectedFeatures = createList(unSelectedModel, featureNames);
        }
        JScrollPane spUnselect = new JScrollPane(unSelectedFeatures);
        spUnselect.setBorder(BorderFactory.createTitledBorder("Unselected Features"));
        spUnselect.setPreferredSize(new Dimension(200, 150));
        spUnselect.setBackground(Color.WHITE);
        
        selectedModel = new DefaultListModel<>();
        selectedFeatures = createList(selectedModel, new ArrayList<>());
        JScrollPane spSelect = new JScrollPane(selectedFeatures);
        spSelect.setBorder(BorderFactory.createTitledBorder("Selected Features"));
        spSelect.setPreferredSize(new Dimension(200, 150));
        spSelect.setBackground(Color.WHITE);
        
        addButton = new JButton("Add Feature");
        addButton.addActionListener(e -> {
            try {
                String feature = unSelectedFeatures.getSelectedValue();
                core.predictFeature(dataSourceName, new FeatureSelection(Selection.SINGLE, Collections.singletonList(feature)));
            } catch (Exception exception) {
                showMessageWin("Add Prediction Failed", exception.getMessage());
            }
        });
    
        predictResultModel = new DefaultListModel<>();
        predictResultList = createList(predictResultModel, new ArrayList<>());
        JScrollPane spPredict = new JScrollPane(predictResultList);
        spPredict.setBorder(BorderFactory.createTitledBorder("Prediction in 7th Day"));
        spPredict.setPreferredSize(new Dimension(200, 150));
        spPredict.setBackground(Color.WHITE);
        
        add(spUnselect);
        add(addButton);
        add(spSelect);
        add(spPredict);
    }
    
    private JList<String> createList(DefaultListModel<String> model, List<String> featureNames) {
        for (String str : featureNames) model.addElement(str);
        JList<String> list = new JList<>(model);
        list.setVisibleRowCount(5);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBackground(Color.WHITE);
        
        return list;
    }
    
    public void addPredictResult(List<ProcessedFeature> predictedFeature) {
        if (predictedFeature != null
                && !predictedFeature.isEmpty()
                && !predictedFeature.get(0).getValue().isEmpty()) {
            
            String featureName = predictedFeature.get(0).getFeatureName();
            featureName = featureName.split("_")[1];
            
            unSelectedModel.removeElement(featureName);
            selectedModel.addElement(featureName);
            
            String predictResult = featureName + ": " + predictedFeature.get(0).getValue().get(0).toString();
            predictResultModel.addElement(predictResult);
        }
    }
    
    private void showMessageWin(String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
