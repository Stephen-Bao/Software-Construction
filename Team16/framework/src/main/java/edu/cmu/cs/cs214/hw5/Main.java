package edu.cmu.cs.cs214.hw5;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.VaccineAnalysisFramework;
import edu.cmu.cs.cs214.hw5.gui.VaccineAnalysisFrameworkGUI;
import java.util.ServiceLoader;
import javax.swing.SwingUtilities;

/**
 * Main entry point for the framework.
 */
public class Main {
    /**
     * Main entry method.
     * @param args input arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndStartFramework);
    }

    /**
     * Create and start the framework.
     */
    private static void createAndStartFramework() {
        VaccineAnalysisFramework core = new VaccineAnalysisFramework();
        VaccineAnalysisFrameworkGUI gui = new VaccineAnalysisFrameworkGUI(core);
        core.setFrameworkChangeListener(gui);
        loadPlugins(core);
    }

    /**
     * Load plugins to GUI.
     * @param core input framework instance
     */
    private static void loadPlugins(VaccineAnalysisFramework core) {
        System.out.println("Loading Data plugins:");
        for (DataPlugin p : ServiceLoader.load(DataPlugin.class)) {
            core.registerDataPlugin(p);
            System.out.println("- " + p.name());
        }


        System.out.println("Loading Display plugins:");
        for (DisplayPlugin p : ServiceLoader.load(DisplayPlugin.class)) {
            core.registerDisplayPlugin(p);
            System.out.println("- " + p.name());
        }
    }
}
