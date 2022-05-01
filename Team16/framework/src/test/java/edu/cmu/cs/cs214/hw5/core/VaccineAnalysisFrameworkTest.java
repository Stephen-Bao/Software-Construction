package edu.cmu.cs.cs214.hw5.core;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VaccineAnalysisFrameworkTest {
    VaccineAnalysisFramework core;
    DataPlugin dataPluginStub;
    DisplayPlugin displayPluginStub;
    FrameworkChangeListener listenerStub;

    @BeforeEach
    void setUp() {
        core = new VaccineAnalysisFramework();
        dataPluginStub = new DataPluginStub();
        displayPluginStub = new DisplayPluginStub();
        listenerStub = new ListenerStub();
        core.setFrameworkChangeListener(listenerStub);
    }

    @Test
    void testRunDataPlugin() {
        core.runDataPlugin(dataPluginStub);
    }

    @Test
    void getDataSet() {
        core.runDataPlugin(dataPluginStub);
        Dataset target = null;
        try {
            target = dataPluginStub.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(core.getDataSet(), target);
    }

    @Test
    void setDisplayChangeListener() {
        core.setFrameworkChangeListener(listenerStub);
    }

    @Test
    void registerDisplayPlugin() {
        core.registerDisplayPlugin(displayPluginStub);
    }

    @Test
    void registerDataPlugin() {
        core.registerDataPlugin(dataPluginStub);
    }

    @Test
    void startNewDisplay() {
        core.startNewDisplay(displayPluginStub);
    }

    @Test
    void notifyDatasetLoaded() {
        core.notifyDatasetLoaded();
    }
}