package edu.cmu.cs.cs214.hw5.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class DatasetTest {

    private Dataset dataset;

    @BeforeEach
    public void setUp() {
        dataset = new Dataset();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(dataset.isEmpty());
        Dataset.VaccineDataRow row1 = new Dataset.VaccineDataRow(
                "Alaska",
                LocalDate.parse("2021-04-18"),
                "ALL",
                508630,
                746705,
                525000,
                List.of(290265, 234735)
        );
        dataset.setLastHourVaccineData(List.of(row1));
        assertFalse(dataset.isEmpty());
    }

    @Test
    public void testDataset() {
        Dataset.VaccineDataRow row1 = new Dataset.VaccineDataRow(
                "Alaska",
                LocalDate.parse("2021-04-18"),
                "ALL",
                508630,
                746705,
                525000,
                List.of(290265, 234735)
        );
        Dataset.VaccineDataRow row2 = new Dataset.VaccineDataRow(
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
        dataset.setTimelineVaccineData(List.of(row1, row2));
        dataset.setLastHourVaccineData(List.of(row1));
        dataset.setPopulationData(List.of(popRow));

        System.out.println(dataset.getTimelineVaccineData());
        System.out.println(dataset.getLastHourVaccineData());
        System.out.println(dataset.getPopulationData());
    }

}
