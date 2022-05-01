package edu.cmu.cs.cs214.hw5.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * The object that is used to load data plugins and display plugins from the resources
 */
public class PluginLoader {
    /**
     * Returns a list of {@link DataPlugin} that have been loaded for the project.
     *
     * @return a list of {@link DataPlugin}
     */
    public static List<DataPlugin> loadDataPlugins() {
        ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
        List<DataPlugin> result = new ArrayList<>();
        for (DataPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getName());
            result.add(plugin);
        }
        return result;
    }

    /**
     * Returns a list of {@link DisplayPlugin} that have been loaded for the project.
     *
     * @return a list of {@link DisplayPlugin}
     */
    public static List<DisplayPlugin> loadDisplayPlugins() {
        ServiceLoader<DisplayPlugin> plugins = ServiceLoader.load(DisplayPlugin.class);
        List<DisplayPlugin> result = new ArrayList<>();
        for (DisplayPlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getName());
            result.add(plugin);
        }
        return result;
    }
}
