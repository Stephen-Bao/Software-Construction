package edu.cmu.cs.cs214.rec09;

import edu.cmu.cs.cs214.rec09.framework.core.GameFrameworkImpl;
import edu.cmu.cs.cs214.rec09.framework.core.GamePlugin;
import edu.cmu.cs.cs214.rec09.framework.gui.GameFrameworkGui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndStartFramework);
    }

    private static void createAndStartFramework() {
        GameFrameworkImpl core = new GameFrameworkImpl();
        GameFrameworkGui gui = new GameFrameworkGui(core);
        core.setGameChangeListener(gui);

        core.addPlayer("X");
        core.addPlayer("O");

        List<GamePlugin> plugins = loadPlugins();
        plugins.forEach(core::registerPlugin);
    }

    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<GamePlugin> loadPlugins() {
        ServiceLoader<GamePlugin> plugins = ServiceLoader.load(GamePlugin.class);
        List<GamePlugin> result = new ArrayList<>();
        for (GamePlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getGameName());
            result.add(plugin);
        }
        return result;
    }
}
