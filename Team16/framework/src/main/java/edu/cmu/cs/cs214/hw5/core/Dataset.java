package edu.cmu.cs.cs214.hw5.core;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Core data structures stored in vaccine analysis framework.
 */
public class Dataset {
    // File-loaded data
    private List<VaccineDataRow> timelineVaccineData = new ArrayList<>();
    private List<VaccineDataRow> lastHourVaccineData = new ArrayList<>();
    private List<PopulationDataRow> populationData = new ArrayList<>();

    // Data that are generated through transformation
    private Map<String, Integer> transStatePopulationMap = new HashMap<>();
    private Map<LocalDate, Integer> transCumulativeAdministeredPerDay
            = new TreeMap<>();

    /**
     * Default constructor.
     */
    public Dataset() {

    }

    /**
     * Constructor using a copy of dataset.
     * @param copy another Dataset class instance
     */
    public Dataset(final Dataset copy) {
        timelineVaccineData = new ArrayList<>();
        lastHourVaccineData = new ArrayList<>();
        populationData = new ArrayList<>();

        this.setLastHourVaccineData(copy.getLastHourVaccineData());
        this.setTimelineVaccineData(copy.getTimelineVaccineData());
        this.setPopulationData(copy.getPopulationData());
    }

    /**
     * Check whether dataset is empty.
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return timelineVaccineData.isEmpty()
                && lastHourVaccineData.isEmpty()
                && populationData.isEmpty();
    }

    /**
     * Getter with defensive copy.
     * @return timelineVaccineData
     */
    public List<VaccineDataRow> getTimelineVaccineData() {
        return new ArrayList<>(timelineVaccineData);
    }

    /**
     * Getter with defensive copy.
     * @return lastHourData
     */
    public List<VaccineDataRow> getLastHourVaccineData() {
        return new ArrayList<>(lastHourVaccineData);
    }

    /**
     * Getter with defensive copy.
     * @return populationData
     */
    public List<PopulationDataRow> getPopulationData() {
        return new ArrayList<>(populationData);
    }

    /**
     * Getter with defensive copy.
     * @return transStatePopulationMap
     */
    public Map<String, Integer> getTransStatePopulationMap() {
        return new HashMap<>(transStatePopulationMap);
    }

    /**
     * Getter with defensive copy.
     * @return transCumulativeAdministeredPerDay
     */
    public Map<LocalDate, Integer> getTransCumulativeAdministeredPerDay() {
        return new TreeMap<>(transCumulativeAdministeredPerDay);
    }

    /**
     * Setter for timelineVaccineData.
     * @param newTimelineVaccineData input arg
     */
    public void setTimelineVaccineData(
            final List<VaccineDataRow> newTimelineVaccineData) {
        timelineVaccineData = new ArrayList<>(newTimelineVaccineData);
        computeCumulativeDosesAdministeredPerDay();
    }

    /**
     * Setter for lastHourVaccineData.
     * @param newLastHourVaccineData input arg
     */
    public void setLastHourVaccineData(
            final List<VaccineDataRow> newLastHourVaccineData) {
        lastHourVaccineData = new ArrayList<>(newLastHourVaccineData);
    }

    /**
     * Setter for populationData.
     * @param newPopulationData input arg
     */
    public void setPopulationData(
            final List<Dataset.PopulationDataRow> newPopulationData
    ) {
        populationData = new ArrayList<>(newPopulationData);
        transformPopulationData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dataset)) return false;
        Dataset dataset = (Dataset) o;
        return timelineVaccineData.equals(dataset.timelineVaccineData)
                && lastHourVaccineData.equals(dataset.lastHourVaccineData)
                && populationData.equals(dataset.populationData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timelineVaccineData, lastHourVaccineData, populationData);
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "timelineVaccineDataRows=" + timelineVaccineData.size() +
                ", lastHourVaccineDataRows=" + lastHourVaccineData.size() +
                ", populationDataRows=" + populationData.size() +
                '}';
    }

    private void transformPopulationData() {
        HashMap<String, Integer> map = new HashMap<>();
        for (PopulationDataRow row : populationData) {
            map.put(row.getProvinceState(), row.getPopulation());
        }
        transStatePopulationMap = map;
    }

    private void computeCumulativeDosesAdministeredPerDay() {
        if (timelineVaccineData.size() == 0) {
            System.out.println("No timeline vaccine data specified, cannot" +
                    "compute cumulative dose data!");
            return;
        }

        // Get rows with vaccine type "All"
        List<VaccineDataRow> tempList = new ArrayList<>();
        for (VaccineDataRow row : timelineVaccineData) {
            if (row.getVaccineType().equals("All")) {
                tempList.add(row);
            }
        }
        if (tempList.isEmpty()) {
            return;
        }

        // Sort rows by date
        tempList.sort(new CompareByDate());

        // Merge rows with same date
        Map<LocalDate, Integer> dataMap = new TreeMap<>();
        LocalDate currentDate = tempList.get(0).getDate();
        int currentDateDoses = 0;
        for (VaccineDataRow row : tempList) {
            if (row.getDate().equals(currentDate)
                    && row.getDosesAdmin() != -1) {
                currentDateDoses += row.getDosesAdmin();
            } else {
                dataMap.put(currentDate, currentDateDoses);
                currentDate = row.getDate();
                currentDateDoses = row.getDosesAdmin() == -1
                        ? 0 : row.getDosesAdmin();
            }
        }
        dataMap.put(currentDate, currentDateDoses);

        transCumulativeAdministeredPerDay = dataMap;
    }

    /**
     * Public inner class that represents a row of vaccine data.
     */
    public static final class VaccineDataRow {

        private final String provinceState;
        private final LocalDate date;
        private final String vaccineType;
        private final int dosesAlloc;
        private final int dosesShipped;
        private final int dosesAdmin;
        private final List<Integer> stageDoses;

        /**
         * Constructor.
         * @param newProvinceState province state
         * @param newDate date
         * @param newVaccineType vaccine type
         * @param newDosesAlloc allocated doses
         * @param newDosesShipped shipped doses
         * @param newDosesAdmin administered doses
         * @param newStageDoses a list representing administered doses of
         *                      different stages
         */
        public VaccineDataRow(
                final String newProvinceState,
                final LocalDate newDate,
                final String newVaccineType,
                final int newDosesAlloc,
                final int newDosesShipped,
                final int newDosesAdmin,
                final List<Integer> newStageDoses
        ) {
            provinceState = newProvinceState;
            date = newDate;
            vaccineType = newVaccineType;
            dosesAlloc = newDosesAlloc;
            dosesShipped = newDosesShipped;
            dosesAdmin = newDosesAdmin;
            stageDoses = new ArrayList<>(newStageDoses);
        }

        /**
         * Getter for province state.
         * @return province state
         */
        public String getProvinceState() {
            return provinceState;
        }

        /**
         * Getter for date.
         * @return date
         */
        public LocalDate getDate() {
            return date;
        }

        /**
         * Getter for vaccine type.
         * @return vaccine type
         */
        public String getVaccineType() {
            return vaccineType;
        }

        /**
         * Getter for allocated doses.
         * @return allocated doses
         */
        public int getDosesAlloc() {
            return dosesAlloc;
        }

        /**
         * Getter for shipped doses.
         * @return shipped doses
         */
        public int getDosesShipped() {
            return dosesShipped;
        }

        /**
         * Getter for administered doses.
         * @return administered doses.
         */
        public int getDosesAdmin() {
            return dosesAdmin;
        }

        /**
         * Getter for list of different stage doses.
         * @return stage doses
         */
        public List<Integer> getStageDoses() {
            return new ArrayList<>(stageDoses);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof VaccineDataRow)) return false;
            VaccineDataRow that = (VaccineDataRow) o;
            return dosesAlloc == that.dosesAlloc
                    && dosesShipped == that.dosesShipped
                    && dosesAdmin == that.dosesAdmin
                    && Objects.equals(provinceState, that.provinceState)
                    && Objects.equals(date, that.date)
                    && Objects.equals(vaccineType, that.vaccineType)
                    && Objects.equals(stageDoses, that.stageDoses);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    provinceState,
                    date,
                    vaccineType,
                    dosesAlloc,
                    dosesShipped,
                    dosesAdmin,
                    stageDoses);
        }

        @Override
        public String toString() {
            return "VaccineDataRow{" +
                    "provinceState='" + provinceState + '\'' +
                    ", date=" + date +
                    ", vaccineType='" + vaccineType + '\'' +
                    ", dosesAlloc=" + dosesAlloc +
                    ", dosesShipped=" + dosesShipped +
                    ", dosesAdmin=" + dosesAdmin +
                    ", stageDoses=" + stageDoses +
                    '}';
        }
    }

    /**
     * This class represents a row of population data.
     */
    public static final class PopulationDataRow {

        private final String provinceState;
        private final int population;

        /**
         * Construtor.
         * @param newProvinceState province state
         * @param newPopulation population
         */
        public PopulationDataRow(
                final String newProvinceState,
                final int newPopulation
        ) {
            provinceState = newProvinceState;
            population = newPopulation;
        }

        /**
         * Getter for province state.
         * @return province state
         */
        public String getProvinceState() {
            return provinceState;
        }

        /**
         * Getter for population.
         * @return population
         */
        public int getPopulation() {
            return population;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PopulationDataRow)) return false;
            PopulationDataRow that = (PopulationDataRow) o;
            return population == that.population
                    && Objects.equals(provinceState, that.provinceState);
        }

        @Override
        public int hashCode() {
            return Objects.hash(provinceState, population);
        }

        @Override
        public String toString() {
            return "PopulationDataRow{" +
                    "provinceState='" + provinceState + '\'' +
                    ", population=" + population +
                    '}';
        }
    }

    /**
     * Comparator class for sorting VaccineDataRow by date.
     */
    public static class CompareByDate implements Comparator<VaccineDataRow> {
        @Override
        public int compare(final VaccineDataRow row1,
                           final VaccineDataRow row2) {
            if (row1.getDate().isBefore(row2.getDate())) {
                return -1;
            } else if (row1.getDate().equals(row2.getDate())) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
