# Team 16: Vaccine Data Analysis & Visualization Framework

Team members: Chenxi Zhang (chenxizh), Xinyu Bao (xinyub)

[slides](https://docs.google.com/presentation/d/1umx-eiRj6Wh41PrgCEetwZg9xIKyyXwTU0ao3P01CZ8)

## Framework domain:
Our framework focuses on the analysis and visualization for vaccination data of a certain virus, in our example, the COVID-19. We specified the core data structures stored in the framework as three: `timelineVaccineData`, `lastHourVaccineData` and `populationData`. The `timelineVaccineData` is for recording relative vaccine information with respect to a long period of time, for example, the cumulative allocated doses of each state in the United States since the breakout date of COVID-19. The `lastHourVaccineData` is on the other hand the most up-to-date data of this vaccine. In our example, we used the hourly updated coronavirus vaccine data provided by JHU official site. Finally the `populationData` is just a dataset which documents the population of each state. The specific dictionary key of each dataset should be specified exactly as the example dataset provided in our plugins directory. 

## API Overview:
Here I would introduce some of the most important APIs in our vaccine analysis framework following its lifecycle. The framework would first register a data plugin after the user has chosen one from the top-down menu in our GUI with the `registerDataPlugin` and `registerDisplagPlugin` methods. The user would then be asked to specify the data sources, which would in turn invoke the `runDataPlugin` method to load data from each source. After selected a target display plugin, `startNewDisplay` would be invoked to load a new display plugin and do the visualization after the user’s choosing as well.

## Instructions for how to implement plugins:
Users who want to use our framework should focus on 2 interfaces: `DataPlugin` and `DisplayPlugin`.

### DataPlugin interface

normally the class should have 3 fields corresponding to each data source of the required dataset: `timelineVaccineDataSource`, `lastHourVaccineDataSource`, and `populationDataSource`. Each data source should be specified in the setter method of data plugin interface, which is called by the framework after the users have entered the sources. The key method `getData` will be invoked by the framework, where the implementation class should create a dataset instance, parse and load each source into this instance and return the dataset. For details:

- `name()`: defines the name of data plugin, it will be shown in GUI menu.
- `sourceInstruction()`: defines the instruction of input file name or URL format, it will be shown in the source input dialog.
- `onRegister(VaccineAnalysisFramework framework)`: would be called (only once) when the plug-in is first registered with the framework, giving the plug-in a chance to perform any initial set-up (if necessary).
- `setTimelineVaccineDataSource(String src)`: API for GUI set `timelineVaccineDataSource`.
- `setLastHourVaccineDataSource(String src)`: API for GUI set `lastHourVaccineDataSource`.
- `setPopulationDataSource(String src)`: API for GUI set `populationDataSource`.
- `Dataset getData() throws Exception`: read file or URL, parse data and create a `Dataset` instance. Note that the error should be thrown out, the framework will catch and show in GUI for you.

### DisplayPlugin interface

The most important method is `draw`, which takes in a loaded dataset as an argument. During implementation, it’s assumed that necessary data has already been loaded into the dataset, and the client should generate the visualization in a JPanel and return, by which the graph could be displayed on the GUI. For details:

- `name()`: defines the name of display plugin, it will be shown in GUI menu.
- `onRegister(VaccineAnalysisFramework framework)`: Same as DataPlugin, when the display plugin is registered and a framework instance is passed in for the future invoke (if necessary).
- `JPanel draw(Dataset data) throws Exception`: draw a UI with the input dataset, throw the error when encountered.

## Instructions for how to register plugins and run the framework:
Plugin implementation should be done solely in the plugins directory, which is independent from the framework folder. After implementation, the client should register the new plugin’s package path and name in the corresponding resource file under plugins/src/main/resources/META-INF/services. After that, the framework can be easily run with “gradle run” command from the plugins directory, and the newly registered plugin should be available from the GUI menu. 
One thing to notice is that, the user does not need to specify all three data sources in GUI in order to make the framework work. We designed it this way to provide some flexibility and make use easier. However, some display plugins may not work in case certain data is missing. For example, the FirstAndSecondDoseBarChart would not work if the last hour vaccine data is not specified. The user should be responsible for specifying the right dataset in order to make desired display plugins work well. 


## Development Plan

### Tasks

Core implementation: critical classes that require all team member's input and code review for each other
- `COVIDVaccineFramework` & plugin interfaces
- `COVIDVaccineFrameworkGUI`
- `DataSet` & `VaccineDataRow`
- Tests for above implementation

Plugins implementation:

**chenxizh**:
- DataPlugin: `HTMLDataPlugin`
- DisplayPlugin: `VaccineTypePieChart`

**xinyub**:
- DataPlugin: `JSONDataPlugin`
- DisplayPlugin: `TotalVaccinedPerDayLineChart`

Testing owner: xinyub, should ensure each PR passed unit tests.

Code review guideline: there should at least one approval for each PR before mergeable.

### Milestones
- 2021-04-16(Fri) complete core `COVIDVaccineFramework` and dataset related classes `DataSet` & `VaccineDataRow`
- 2021-04-18(Sun) complete GUI and unit tests
- 2021-04-18(Tue) complete plugins development and integration tests
