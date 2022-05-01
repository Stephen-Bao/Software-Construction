package edu.cmu.cs.cs214.hw5;

import edu.cmu.cs.cs214.hw5.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw5.core.PluginLoader;
import edu.cmu.cs.cs214.hw5.gui.FrameworkGUI;

import java.util.List;

/**
 * This is a template main method that can be executed in the project where the user has
 * properly defined the plugins in the resources/META-INF.services, thus the PluginLoader can
 * load the plugins properly and execute the framework demo.
 */
public class Main {
    /**
     * Builds a FrameWork Demo by loading all configured plugins
     *
     * @param args input arguments, ignorable
     */
    public static void main(String[] args) {
        FrameworkImpl framework = new FrameworkImpl();
        FrameworkGUI gui = new FrameworkGUI(framework);
        framework.addDataChangeListener(gui);

        List<DataPlugin> dataPluginList =  PluginLoader.loadDataPlugins();
        List<DisplayPlugin> displayPluginList = PluginLoader.loadDisplayPlugins();

        framework.registerDataPlugins(dataPluginList);
        framework.registerDisplayPlugins(displayPluginList);
    }
}
