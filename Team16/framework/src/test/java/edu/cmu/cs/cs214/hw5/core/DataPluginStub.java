package edu.cmu.cs.cs214.hw5.core;

import java.time.LocalDate;
import java.util.List;

public class DataPluginStub implements DataPlugin {

    private String timelineVaccineDataSource;
    private String lastHourVaccineDataSource;
    private String populationDataSource;

    @Override
    public String name() {
        return "Stub plugin";
    }

    @Override
    public String sourceInstruction() {
        return null;
    }

    @Override
    public void onRegister(VaccineAnalysisFramework framework) {

    }

    @Override
    public void setTimelineVaccineDataSource(String src) {
        timelineVaccineDataSource = src;
    }

    @Override
    public void setLastHourVaccineDataSource(String src) {
        lastHourVaccineDataSource = src;
    }

    @Override
    public void setPopulationDataSource(String src) {
        populationDataSource = src;
    }

    @Override
    public Dataset getData() throws Exception {
        Dataset dataset = new Dataset();
        Dataset.VaccineDataRow vaccineRow1 = new Dataset.VaccineDataRow(
                "Alaska",
                LocalDate.parse("2021-04-18"),
                "ALL",
                508630,
                746705,
                525000,
                List.of(290265, 234735)
        );
        Dataset.VaccineDataRow vaccineRow2 = new Dataset.VaccineDataRow(
                "Georgia",
                LocalDate.parse("2021-04-18"),
                "ALL",
                6216965,
                8033225,
                5462269,
                List.of(3443914, 1891093)
        );
        Dataset.PopulationDataRow popRow = new Dataset.PopulationDataRow(
                "Florida", 21477737);

        dataset.setTimelineVaccineData(List.of(vaccineRow1, vaccineRow2));
        dataset.setLastHourVaccineData(List.of(vaccineRow1));
        dataset.setPopulationData(List.of(popRow));

        return dataset;
    }
}
