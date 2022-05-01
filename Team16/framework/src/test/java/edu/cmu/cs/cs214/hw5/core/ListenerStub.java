package edu.cmu.cs.cs214.hw5.core;

public class ListenerStub implements FrameworkChangeListener {
    @Override
    public void onDataPluginRegistered(DataPlugin plugin) {
        System.out.println("registered data plugin " + plugin);
    }

    @Override
    public void onDisplayPluginRegistered(DisplayPlugin plugin) {
        System.out.println("registered display plugin " + plugin);
    }

    @Override
    public void onNewDisplay(DisplayPlugin plugin) {
        System.out.println("new display with " + plugin);
    }

    @Override
    public void onDatasetLoaded() {
        System.out.println("dataset loaded");
    }

    @Override
    public void onDataPluginError(String errMessage) {
        System.out.println("data plugin error: " + errMessage);
    }

    @Override
    public void onDisplayPluginError(String errMessage) {
        System.out.println("display plugin error: " + errMessage);
    }
}
