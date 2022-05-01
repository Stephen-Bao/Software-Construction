package edu.cmu.cs.cs214.hw5.gui;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.FrameworkChangeListener;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.VaccineAnalysisFramework;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

/**
 * GUI for VaccineAnalysisFramework.
 */
public class VaccineAnalysisFrameworkGUI implements FrameworkChangeListener {
    // Strings.
    private static final String DEFAULT_TITLE = "Vaccine Analysis Framework";
    private static final String MENU_TITLE = "File";
    private static final String MENU_NEW_DATA_PLUGIN = "New Data...";
    private static final String MENU_NEW_DISPLAY_PLUGIN = "New Display...";
    private static final String MENU_EXIT = "Exit";
    private static final String ERROR_TITLE = "Error!";
    private static final String ERROR_NO_DATASET_MSG = "No dataset is " +
            "imported to the framework";
    private static final String DEFAULT_FOOTER_TEXT = "Load data & display!";
    private static final String CLEAN_DATASET_WARNING_TITLE =  "Clean Dataset";
    private static final String CLEAN_DATASET_WARNING = "There is existing" +
        "dataset. Are you sure to clean the old dataset and import new one?";
    private static final String DATASET_LOADED_TEXT =  "New dataset loaded.";
    private static final String DATA_PLUGIN_ERR_TITLE =  "Load Data Error";
    private static final String DISPLAY_PLUGIN_ERR_TITLE =  "Data Display " +
            "Error";
    private static final String SOURCE_INPUT_TITLE = "Please Enter Data Source";
    private static final String SOURCE_INPUT_HEAD = "Please input data source" +
            "file path below and partial data source is acceptable.";
    private static final String TIMELINE_SOURCE_INPUT = "Vaccine timeline " +
            "source:";
    private static final String LATEST_SOURCE_INPUT = "Last hour vaccine data" +
            " source:";
    private static final String POPULATION_SOURCE_INPUT = "Population data " +
            "source:";
    private static final String SOURCE_INPUT_ERR = "No input source is found.";

    // Widgets.
    private final JFrame frame;
    private final ButtonGroup dataPluginGroup = new ButtonGroup();
    private final JMenu dataPluginMenu;
    private final ButtonGroup displayPluginGroup = new ButtonGroup();
    private final JMenu displayPluginMenu;
    private final JPanel outerPanel;
    private final JPanel innerPanel;
    private final JLabel footerLabel;

    /**
     * Framework instance.
     */
    private VaccineAnalysisFramework core;

    /**
     * Constructor to create GUI instance.
     * @param framework input framework instance
     */
    public VaccineAnalysisFrameworkGUI(
            final VaccineAnalysisFramework framework) {
        core = framework;

        frame = new JFrame(DEFAULT_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 900));

        // set-up panels
        outerPanel = new JPanel(new BorderLayout());

        footerLabel = new JLabel(DEFAULT_FOOTER_TEXT);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel.add(footerLabel, BorderLayout.SOUTH);

        innerPanel = new JPanel();
        innerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        outerPanel.add(innerPanel, BorderLayout.CENTER);
        frame.add(outerPanel);

        // Set-up the menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(MENU_TITLE);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        // add data & display menus
        dataPluginMenu = new JMenu(MENU_NEW_DATA_PLUGIN);
        dataPluginMenu.setMnemonic(KeyEvent.VK_N);
        fileMenu.add(dataPluginMenu);
        displayPluginMenu = new JMenu(MENU_NEW_DISPLAY_PLUGIN);
        displayPluginMenu.setMnemonic(KeyEvent.VK_N);
        fileMenu.add(displayPluginMenu);

        // Add an 'Exit' menu item.
        fileMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem(MENU_EXIT);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Handling data plugin registered.
     * @param plugin registered data plugin
     */
    @Override
    public void onDataPluginRegistered(final DataPlugin plugin) {
        // add data plugin to menu
        JRadioButtonMenuItem dataMenuItem =
                new JRadioButtonMenuItem(plugin.name());
        dataMenuItem.setSelected(false);
        dataMenuItem.addActionListener(event -> {
            if (!core.getDataSet().isEmpty()) {
                int confirmation = showConfirmationDialog(
                    CLEAN_DATASET_WARNING_TITLE,
                    CLEAN_DATASET_WARNING);
                // 0=yes, 1=no
                if (confirmation == 1) {
                    return;
                }
            }

            if (getDataPluginResourcesInputDialog(plugin)) {
                core.runDataPlugin(plugin);
            } else {
                dataPluginGroup.clearSelection();
            }
        });
        dataPluginGroup.add(dataMenuItem);
        dataPluginMenu.add(dataMenuItem);
    }

    /**
     * Handling display plugin registered.
     * @param plugin registered display plugin
     */
    @Override
    public void onDisplayPluginRegistered(final DisplayPlugin plugin) {
        // add plugin to menu
        JRadioButtonMenuItem displayMenuItem =
                new JRadioButtonMenuItem(plugin.name());
        displayMenuItem.setSelected(false);
        displayMenuItem.addActionListener(event -> {
            // need to add it back after data plugin flow is ready
            if (core.getDataSet().isEmpty()) {
                // Can't start drawing without dataset
                showErrorDialog(ERROR_TITLE, ERROR_NO_DATASET_MSG);
                displayPluginGroup.clearSelection();
            } else {
                // start new display
                core.startNewDisplay(plugin);
            }
        });
        displayPluginGroup.add(displayMenuItem);
        displayPluginMenu.add(displayMenuItem);
    }

    /**
     * Handling display plugin is triggered.
     * @param plugin display plugin to be run
     */
    @Override
    public void onNewDisplay(final DisplayPlugin plugin) {
        JPanel chart = null;
        try {
            chart = plugin.draw(core.getDataSet());
        } catch (Exception e) {
            core.notifyDisplayPluginError(e.toString());
            return;
        }
        innerPanel.removeAll();
        innerPanel.setLayout(new BorderLayout());
        innerPanel.add(chart, BorderLayout.CENTER);
        frame.setTitle(plugin.name());
        frame.pack();
    }

    /**
     * Handling data set is loaded from a data plugin.
     */
    @Override
    public void onDatasetLoaded() {
        footerLabel.setText(DATASET_LOADED_TEXT);
    }

    /**
     * Handling data plugin running error.
     * @param errMessage error message
     */
    @Override
    public void onDataPluginError(final String errMessage) {
        dataPluginGroup.clearSelection();
        showErrorDialog(DATA_PLUGIN_ERR_TITLE, errMessage);
    }

    /**
     * Handling display plugin running error.
     * @param errMessage error message
     */
    @Override
    public void onDisplayPluginError(final String errMessage) {
        displayPluginGroup.clearSelection();
        showErrorDialog(DISPLAY_PLUGIN_ERR_TITLE, errMessage);
    }

    /**
     * Trigger to show error dialog.
     * @param title dialog title
     * @param msg dialog message
     */
    private void showErrorDialog(final String title, final String msg) {
        JOptionPane.showMessageDialog(
                frame, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Trigger to show confirmation dialog.
     * @param title dialog title
     * @param msg dialog message
     */
    private int showConfirmationDialog(
            final String title,
            final String msg
    ) {
        return JOptionPane.showConfirmDialog(frame, msg, title,
                JOptionPane.YES_NO_OPTION);
    }

    /**
     * Trigger to show get data sources dialog.
     * @param plugin target data plugin
     */
    private boolean getDataPluginResourcesInputDialog(final DataPlugin plugin) {
        JLabel head = new JLabel(SOURCE_INPUT_HEAD);
        JLabel info = new JLabel(plugin.sourceInstruction());
        JTextField timelineVaccineDataSourceField = new JTextField(10);
        JTextField latestVaccineDataSourceField = new JTextField(10);
        JTextField populationDataSourceField = new JTextField(10);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(head);
        inputPanel.add(info);
        inputPanel.add(Box.createVerticalStrut(20)); // a spacer
        inputPanel.add(new JLabel(LATEST_SOURCE_INPUT));
        inputPanel.add(latestVaccineDataSourceField);
        inputPanel.add(new JLabel(TIMELINE_SOURCE_INPUT));
        inputPanel.add(timelineVaccineDataSourceField);
        inputPanel.add(new JLabel(POPULATION_SOURCE_INPUT));
        inputPanel.add(populationDataSourceField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel,
                SOURCE_INPUT_TITLE, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String timelineSource = timelineVaccineDataSourceField.getText();
            String lastHourSource = latestVaccineDataSourceField.getText();
            String populationSource = populationDataSourceField.getText();

            if (timelineSource.isEmpty()
                    && lastHourSource.isEmpty()
                    && populationSource.isEmpty()
            ) {
                showErrorDialog(ERROR_TITLE, SOURCE_INPUT_ERR);
                return false;
            }

            plugin.setLastHourVaccineDataSource(lastHourSource);
            plugin.setTimelineVaccineDataSource(timelineSource);
            plugin.setPopulationDataSource(populationSource);
            return true;
        }

        return false;
    }
}
