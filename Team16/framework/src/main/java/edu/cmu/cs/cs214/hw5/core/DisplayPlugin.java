package edu.cmu.cs.cs214.hw5.core;

import javax.swing.JPanel;

/**
 * DisplayPlugin interface defines methods that a display plugin needs.
 */
public interface DisplayPlugin {
    /**
     * Defines the name of the display plugin and will be shown in the GUI menu
     * selection.
     * @return name of the plugin
     */
    String name();

    /**
     * On registered and pass the framework instance to plugin for the future
     * invoke.
     * @param framework instance of current framework
     */
    void onRegister(VaccineAnalysisFramework framework);

    /**
     * Draw charts with current dataset.
     * @param data  current framework dataset
     * @return JPanel of the chart
     * @throws Exception error when drawing the chart, will be shown in
     *                   the error dialogue.
     */
    JPanel draw(Dataset data) throws Exception;
}
