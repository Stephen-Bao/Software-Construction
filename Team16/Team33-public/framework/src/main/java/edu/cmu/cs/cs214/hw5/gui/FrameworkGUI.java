package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

/**
 * A graphic user interface of the COVID-19 framework.
 */
public class FrameworkGUI implements DataChangeListener {
    /** A framework core instance with which the GUI interact */
    private final Framework core;
    /** A map maintaining mapping from data plugin name to the plugin instance */
    private final Map<String, DataPlugin> dataPluginMap;
    /** A map maintaining mapping from display plugin name to the plugin instance */
    private final Map<String, DisplayPlugin> displayPluginMap;
    /** A map maintaining mapping from data source name to the processed data instance */
    private Map<String, ProcessedData> processedDataMap;
    
    /** A JFrame instance representing the GUI window */
    private final JFrame frame;
    /** A JPanel instance representing the whole panel on the GUI window */
    private JPanel panel;
    
    //Data panel
    /** A combo box maintaining all registered data plugins */
    private final JComboBox<String> dataPluginJComboBox;
    /** A text field for user input representing data source name */
    private final JTextField dataName;
    /** A text field for user input representing data source path */
    private final JTextField dataPath;
    /** A text field for user input representing the path of a data mapping configuration file */
    private final JTextField dataMappingConfig;
    /** A label maintaining all uploaded data sources */
    private final JLabel updatedList;
    /** A button for uploading a data source */
    private final JButton uploadButton;
    /** A combo box maintaining all uploaded data sources for management usage */
    private final JComboBox<String> dataManageComboBox;
    /** A button for deleting uploaded data sources */
    private final JButton deleteButton;
    
    //Predict config panel
    /** A combo box maintaining all uploaded data sources for analysis configuration */
    private final JComboBox<String> dataAnalysisConfigComboBox;
    /** A map maintaining the mapping from data source name to its {@code AnalysisConfigScheme} */
    private final Map<String, AnalysisConfigScheme> dataAnalysisConfigMap;
    /** A scrollable pane containing an analysis config panel */
    private final JScrollPane dataAnalysisSP;
    /** A default analysis config panel shown if no data source has been added */
    private final JPanel dataAnalysisPanelDefault;
    
    //Visual panel
    /** A map from dataName to visualConfigScheme */
    private final Map<String, VisualConfigScheme> configSchemeMap;
    /** A combo box maintaining all uploaded data sources for visualization configuration */
    private final JComboBox<String> dataVisualConfigComboBox;
    /** A scrollable pane containing an visualization config panel */
    private final JScrollPane sp;
    /** A default visualization config panel shown if no data source has been added */
    private final JPanel defaultVisualPanel;
    /** A button for plotting all charts */
    private final JButton plotButton;
    
    /**
     * Constructs a GUI instance by taking a framework core object and show the GUI
     * for user interaction.
     *
     * @param core  the framework core object
     */
    public FrameworkGUI(Framework core){
        // Set the framework core instance that the GUI will talk to in response to GUI-related events.
        this.core = core;
        dataPluginMap = new HashMap<>();
        displayPluginMap = new HashMap<>();
        processedDataMap = new LinkedHashMap<>();
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //data panel
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 900));
        dataPluginJComboBox = new JComboBox<>();
        dataPluginJComboBox.setEditable(true);
        dataName = new JTextField();
        dataPath = new JTextField();
        dataMappingConfig = new JTextField();
        updatedList = new JLabel("Uploaded: 0", JLabel.LEFT);
        uploadButton = new JButton("Upload Data");
        
        dataManageComboBox = new JComboBox<>();
        dataManageComboBox.setEditable(true);
        deleteButton = new JButton("Delete Data");
        
        //predict config panel
        dataAnalysisConfigComboBox = new JComboBox<>();
        dataAnalysisConfigComboBox.setEditable(true);
        dataAnalysisConfigMap = new LinkedHashMap<>();
        dataAnalysisSP = new JScrollPane();
        dataAnalysisSP.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        dataAnalysisSP.setPreferredSize(new Dimension(200, 300));
        dataAnalysisSP.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        dataAnalysisPanelDefault = new JPanel();
        dataAnalysisPanelDefault.setPreferredSize(new Dimension(200, 300));
        dataAnalysisSP.setViewportView(dataAnalysisPanelDefault);
        dataAnalysisConfigComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String configDataName = (String) e.getItem();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    dataAnalysisSP.setViewportView(dataAnalysisConfigMap.get(configDataName));
                }
            }
        });
        
        //visual panel
        sp = new JScrollPane();
        sp.setPreferredSize(new Dimension(700, 500));
        sp.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        defaultVisualPanel = new JPanel();
        defaultVisualPanel.setPreferredSize(new Dimension(700, 500));
        sp.setViewportView(defaultVisualPanel);
        processedDataMap = new HashMap<>();
        configSchemeMap = new LinkedHashMap<>();
        dataVisualConfigComboBox = new JComboBox<>();
        dataVisualConfigComboBox.setEditable(true);
        dataVisualConfigComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String configDataName = (String) e.getItem();
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sp.setViewportView(configSchemeMap.get(configDataName));
                }
            }
        });
        plotButton = new JButton("Finish Config and Plot");
        plotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    core.plot();
                } catch (Exception exception) {
                    showMessageWin("Visualization Failed", exceptionCauseMessageIfAvailable(exception));
                }
                clearConfigs();
            }
        });
        
        panel.setLayout(new BorderLayout());
        panel.add(createDataPanel(), BorderLayout.NORTH);
        panel.add(createPredictConfigPanel(), BorderLayout.CENTER);
        panel.add(createVisualPanel(), BorderLayout.SOUTH);
    
        frame.add(panel);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Returns a panel for data uploaded and management.
     *
     * @return      the panel
     */
    private JPanel createDataPanel() {
        JPanel dataUploadPanel = new JPanel();
        dataUploadPanel.setLayout(new BoxLayout(dataUploadPanel, BoxLayout.Y_AXIS));
        dataUploadPanel.setPreferredSize(new Dimension(700, 240));
        dataUploadPanel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
        
        dataUploadPanel.add(createInputPanel("type ", dataPluginJComboBox));
        dataUploadPanel.add(createInputPanel("name ", dataName));
        dataUploadPanel.add(createInputPanel("path ", dataPath));
        dataUploadPanel.add(createInputPanel("mapping config (Optional) ", dataMappingConfig));
        
        uploadButton.addActionListener(e -> {
            DataPlugin dataPlugin = dataPluginMap.get((String) dataPluginJComboBox.getSelectedItem());
            String nameInput = dataName.getText();
            String pathInput = dataPath.getText();
            String configInput = dataMappingConfig.getText();
            
            if (processedDataMap.containsKey(nameInput)) {
                showMessageWin("Invalid Input", "Exist data name.");
            } else if (nameInput.length() != 0 && pathInput.length() != 0) {
                try {
                    core.loadInputData(new InputDataConfig(dataPlugin, nameInput, Arrays.asList(pathInput, configInput)));
                } catch (Exception exception) {
                    showMessageWin( "Invalid Input", exceptionCauseMessageIfAvailable(exception));
                }
            } else {
                showMessageWin( "Invalid Input", "Empty name or path.");
            }
        });
        
        JPanel dataUploadedPanel = new JPanel();
        dataUploadedPanel.setLayout(new FlowLayout());
        dataUploadedPanel.add(updatedList);
        dataUploadedPanel.add(uploadButton);
    
        JPanel dataUploadedOut = new JPanel();
        dataUploadedOut.setLayout(new BorderLayout());
        dataUploadedOut.add(dataUploadedPanel, BorderLayout.EAST);
        
        dataUploadPanel.add(dataUploadedOut);
        dataUploadPanel.add(Box.createRigidArea(new Dimension(0, 10))); //gap
        dataUploadPanel.add(createDeletePanel());
        
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.X_AXIS));
        dataPanel.add(dataUploadPanel);
        dataPanel.setBorder(BorderFactory.createTitledBorder("Data Configuration"));
        
        return dataPanel;
    }
    
    /**
     * Returns a subpanel for a data panel, containing a text label and input component,
     * such as a text field for text input, or a button.
     *
     * @param keyword       the keyword to shown what does the input text mean
     * @param component     the component
     * @return              the panel
     */
    private JPanel createInputPanel(String keyword, JComponent component) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        JLabel inputName = new JLabel("Data " + keyword);
        inputName.setMaximumSize(new Dimension(100, 20));
        inputPanel.add(inputName);
        component.setPreferredSize(new Dimension(350, 20));
        inputPanel.add(component);
        
        return inputPanel;
    }
    
    /**
     * Returns a panel for data management usage, from where a user can delete existing
     * data sources.
     *
     * @return      the panel
     */
    private JPanel createDeletePanel() {
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new BoxLayout(deletePanel, BoxLayout.X_AXIS));
        JLabel inputName = new JLabel("Data management  ");
        deletePanel.add(inputName);
        deletePanel.add(dataManageComboBox);
        
        deleteButton.addActionListener(e -> {
            String deleteDataName = (String) dataManageComboBox.getSelectedItem();
            if (deleteDataName != null && deleteDataName.length() != 0) {
                try {
                    core.deleteInputData(deleteDataName);
                } catch (Exception exception) {
                    showMessageWin("Error", exceptionCauseMessageIfAvailable(exception));
                }
            }
        });
        deletePanel.add(deleteButton);
        return deletePanel;
    }
    
    /**
     * Returns a panel for visualization configuration. This panel shows a
     * data-specific panel for each selected data in the {@code dataVisualConfigComboBox}.
     *
     * @return      the panel
     */
    private JPanel createVisualPanel() {
        JPanel inputDataPanel = new JPanel();
        inputDataPanel.add(new JLabel("Select data to config:", JLabel.LEFT));
        inputDataPanel.add(dataVisualConfigComboBox);
        inputDataPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        JPanel inputDataOut = new JPanel();
        inputDataOut.setLayout(new BorderLayout());
        inputDataOut.add(inputDataPanel, BorderLayout.EAST);
        
        JPanel plotButtonPanel = new JPanel();
        plotButtonPanel.setLayout(new BorderLayout());
        plotButtonPanel.add(plotButton, BorderLayout.EAST);
        
        JPanel visualPanel = new JPanel();
        visualPanel.setLayout(new BoxLayout(visualPanel, BoxLayout.Y_AXIS));
        visualPanel.setBorder(BorderFactory.createTitledBorder("Visualization Configuration"));
        visualPanel.add(inputDataOut);
        visualPanel.add(sp);
        visualPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        visualPanel.add(plotButtonPanel);
        visualPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        visualPanel.setPreferredSize(new Dimension(700, 300));
        
        return visualPanel;
    }
    
    /**
     * Returns a panel for data analysis configuration. This panel shows a
     * data-specific panel for each selected data in the {@code dataAnalysisConfigComboBox}.
     *
     * @return      the panel
     */
    private JPanel createPredictConfigPanel() {
        JPanel predictPanel = new JPanel();
        predictPanel.setLayout(new BorderLayout());
        predictPanel.setBorder(BorderFactory.createTitledBorder("Analysis Configuration (Optional)"));
        predictPanel.setPreferredSize(new Dimension(700, 200));
        
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new FlowLayout());
        dataPanel.add(new JLabel("Select data to config: "));
        dataPanel.add(dataAnalysisConfigComboBox);
        dataPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        JPanel dataPanelOut = new JPanel();
        dataPanelOut.setLayout(new BorderLayout());
        dataPanelOut.add(dataPanel, BorderLayout.EAST);
        dataPanelOut.setPreferredSize(new Dimension(600, 40));
        
        predictPanel.add(dataPanelOut, BorderLayout.NORTH);
        predictPanel.add(dataAnalysisSP, BorderLayout.CENTER);
        return predictPanel;
    }
    
    /**
     * Clear all added visualization configurations. This method is called when a user click
     * {@code plotButton}.
     */
    private void clearConfigs() {
        for (VisualConfigScheme scheme : configSchemeMap.values()) {
            scheme.clearConfig();
        }
    }
    
    /**
     * Stores the registered data plugin and updates related fields on GUI.
     *
     * @param dataPlugin        the registered data plugin
     */
    @Override
    public void onDataPluginRegistered(DataPlugin dataPlugin) {
        // add item to data selection
        String name = dataPlugin.getName();
        if (!dataPluginMap.containsKey(name)) {
            dataPluginMap.put(name, dataPlugin);
            dataPluginJComboBox.addItem(name);
        }
    }
    
    /**
     * Stores the registered display plugin and updates related fields on GUI.
     *
     * @param displayPlugin        the registered display plugin
     */
    @Override
    public void onDisplayPluginRegistered(DisplayPlugin displayPlugin) {
        // add checkbox to display panel
        String name = displayPlugin.getName();
        if (!displayPluginMap.containsKey(name)) {
            displayPluginMap.put(name, displayPlugin);
        }
    }
    
    /**
     * Stores necessary information of the uploaded data source adn updates
     * related fields on GUI.
     *
     * @param data      the uploaded data source
     */
    @Override
    public void onInputDataLoaded(ProcessedData data) {
        //clear input areas (name, path)
        dataName.setText("");
        dataPath.setText("");
        dataMappingConfig.setText("");
        
        String newName = data.getName();
        if (processedDataMap.containsKey(newName)) {
            return;
        }
        processedDataMap.put(newName, data);
    
        //update data panel (uploaded: num, delete comboBox)
        updatedList.setText("Uploaded: " + processedDataMap.size());
        dataManageComboBox.addItem(newName);
        
        //update analysis panel
        dataAnalysisConfigComboBox.addItem(newName);

        FeatureSelection featureSelection = new FeatureSelection(Selection.SINGLE, new ArrayList<>());
        try {
            featureSelection = core.getPredictableFeatureSelection(newName);
        } catch (Exception exception) {
            showMessageWin("Empty Feature", "No invalid feature for prediction.");
        }

        AnalysisConfigScheme analysisConfigScheme = new AnalysisConfigScheme(newName, featureSelection, frame, core);
        dataAnalysisConfigMap.put(newName, analysisConfigScheme);
        
        //update visual panel
        dataVisualConfigComboBox.addItem(newName);
        List<DisplayConfig> displayConfigs = core.getAvailableDisplayPluginConfig(newName);
        VisualConfigScheme configScheme = new VisualConfigScheme(newName, displayConfigs, core, frame);
        configSchemeMap.put(newName, configScheme);
        
        if (configSchemeMap.size() == 1) {
            sp.setViewportView(configSchemeMap.get(newName));
            dataAnalysisSP.setViewportView(dataAnalysisConfigMap.get(newName));
        }
    }
    
    /**
     * Deletes the given data source and updates related fields on GUI.
     *
     * @param dataSourceName      the name of the data source
     */
    @Override
    public void onInputDataDeleted(String dataSourceName) {
        processedDataMap.remove(dataSourceName);
        updatedList.setText("Uploaded: " + processedDataMap.size());
        dataManageComboBox.removeItem(dataSourceName);
        
        dataAnalysisConfigComboBox.removeItem(dataSourceName);
        dataAnalysisConfigMap.remove(dataSourceName);
        
        dataVisualConfigComboBox.removeItem(dataSourceName);
        configSchemeMap.remove(dataSourceName);
        
        if (configSchemeMap.isEmpty()) {
            sp.setViewportView(defaultVisualPanel);
            dataAnalysisSP.setViewportView(dataAnalysisPanelDefault);
        }
    }
    
    @Override
    public void onDisplayFailed(String dataSourceName, String displayPluginName) {
        showMessageWin("Visualization Failed", dataSourceName + " visualized failed on " + displayPluginName);
    }
    
    @Override
    public void onFeaturePredicted(String dataSourceName, List<ProcessedFeature> predictedFeature) {
        AnalysisConfigScheme analysisConfigScheme = dataAnalysisConfigMap.get(dataSourceName);
        analysisConfigScheme.addPredictResult(predictedFeature);
    }
    
    private void showMessageWin(String title, String message) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String exceptionCauseMessageIfAvailable(Exception details) {
        if (details.getCause() == null) {
            return details.getMessage();
        }
        return details.getCause().getMessage();
    }
}
