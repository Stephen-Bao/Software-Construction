package edu.cmu.cs.cs214.hw5.core;

import javax.swing.*;

public class DisplayPluginStub implements DisplayPlugin {
    @Override
    public String name() {
        return "DisplayPluginStub";
    }

    @Override
    public void onRegister(VaccineAnalysisFramework framework) {
        System.out.println("registered " + framework);
    }

    @Override
    public JPanel draw(Dataset data) throws Exception {
        System.out.println("draw with data " + data);
        return null;
    }
}
