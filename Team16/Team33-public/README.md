# README

## 1. Overview of the framework domain

### 1.1 Framework
This framework reads data from the DataPlugins, performs data processing and prediction on regional data in time series, and outputs
the data to DisplayPlugins for visualization.
The framework contains two main modules: framework core and framework graphic user interface (GUI).

The framework reads input data from the DataPlugins and utilizes DisplayPlugins to visualize the processed data.
The input data can be a list of any daily statistical facts (that can be represented in a `long` value) in a specific region.
For example, in the COVID-19 data analysis scenario, the features can be the daily confirmed, case daily death,
the curriculum reported cases, and the percentage of the confirmed case in Texas and California from April 1st to April 21st. 
The features can also be the car crash fatalities or numerical weather conditions that change over time. The regions can be 
any string representation of a state, a province, or a country, for users to have some visualizations and predictions.

The framework processes discrete data points from different regions at different date into an array of data 
in time series of different regions. 
Moreover, the framework implements a Linear Regression model that will be trained on the input data 
and predicts the corresponding features in the next 7 days


### 1.2 Overview of important APIs

In addition to the API provided to the GUI, two sets of plugin APIs are provided for two types of plugins, 
data plugin and display plugin, to integrate with the framework.

#### Interface for GUI
The Framework API implements the interface to interact with GUI, such as load data, delete data, 
predict selected features from data, configure visualization options, and plot them.
#### Interface for data plugins
The DataPlugin API implements the interface of the framework to interact with DataPlugins, 
such as get input data and get the name of the data plugin. 
The input data is passed in the InputData object, which contains the user defined data source name, 
the geometric scope of the input data, and a list of data in different regions at different time.
#### Interface for display plugins
The DisplayPlugin API implements the interface of the framework to interact with DisplayPlugins, 
such as display a processed data based on the display configurations defined by the user, 
get the dimension of the display plugin, and get the name of the display plugin. 
The ProcessedData contains the name of the data source, the geometric scope of the data, 
and all the processed features that are spliced into different regions and constructed into arrays 
for visualization.

### 1.3 Full guide for running GUI
After launching the graphic user interface shown below, Users can upload data sources, delete existing data sources, config analysable features,
config visualization features, and execute plotting.
Note that for the COVID_CSV data plugin that is provided as the sample DataPlugin, a config file is required. 
The sample data source and config files can be found in ```plugins/src/main/resources``` directory

![GUI empty](https://user-images.githubusercontent.com/31769606/115939298-b3cdd880-a46b-11eb-8e92-40f6196a3496.png)


1. Select a data plugin from the drop-down box, fill data name (alias), path, etc., and click `upload` button. Once the data is loaded, it will be processed into Processed data and stored.
   Delete loaded data sources if needed.
![Data config](https://user-images.githubusercontent.com/31769606/115939096-fe028a00-a46a-11eb-880e-23bb4d64c4ce.png)


2. Select a data source to add prediction configurations.
   Choose a feature on which the prediction would be performed by the framework. The prediction result of the predicted feature at 7th day after the final data point will be shown on the GUI.
![Analysis config](https://user-images.githubusercontent.com/31769606/115939104-08bd1f00-a46b-11eb-95f7-b5eebb3ab0e5.png)
   

3. Select a data source to add visualization configurations. All registered display plugins are shown in this panel, from where a user
   can choose feature(s) to display for each dimension. While the first dimension is a single choice JList and other dimensions allow to choose multiple features.
   To make a multiple choose, users may user `shift` or `ctrl`(on Windows)/`command`(on Mac). Then, click `Add Chart` button to add a display associated with selected features.
![Visual config](https://user-images.githubusercontent.com/31769606/115939116-11adf080-a46b-11eb-9b9d-f1b997cabeac.png)


4. Click `Finish Config and Plot` button to finish the configuration and start data visualization. Then the framework
   would call corresponding display plugins to produce configured visualizations (charts).
   

A sample geo map chart and a bar chart are shown below.

![Geomap](https://user-images.githubusercontent.com/31769606/115939148-28544780-a46b-11eb-8c54-8f3bb8205cbd.png)


![Bar](https://user-images.githubusercontent.com/31769606/115939163-35713680-a46b-11eb-8ddd-57efaea6b259.png)


## 2. Instructions

### 2.1 How to implement plugins

#### 2.1.1 Data plugins
To implement data plugins, under your plugin project, create a new class that implements `edu.cmu.cs.cs214.hw5.core.DataPlugin` interface.
This new class should implement 2 public methods: `getName()`, `getInputData(String name, List<String> args)`.
The `getInputData()` method returns an instance of `InputData` with a ```name``` argument, which is a user-specified alias of the `InputData`,
and the `args` arguments passed from the framework has the following conventions:
1. Based on the current GUI implementation, the length of `args` is limited to up to 2.
2. The way of parsing the ```args``` depends on the implementation of the data plugin to get data from your own data sources.

For example, a typical csv data plugin may take the ```args``` as:
* first string is the data path (e.g. URL, local file path, etc.),
* second string is the path to your config json file.

####### InputData
The data plugin should return a ```InputData``` object that contains a name of the data source from the user input, a
```GeoScope``` that indicating the geometric scope of the input data, which is optional and can be set as ```GeoScope.OTHER```
if not defiened, and a list of input data points.

The ```InputDataPoint``` should contain at least a region (```String```), a date (```java.util.Date```) as mandatory input,
otherwise, throw an ```IllegalArgumentException```. Optionally, the ```InputDataPoint``` contains
features represented by the ```edu.cmu.cs.cs214.hw5.core.RowFeature``` Object, which contains a feature name (```String```) and value (```long```).


#### 2.1.2 Display plugins
To implement display plugins, under your plugin project, create a new class that implements `DisplayPlugin`.
This new class should implement 3 public methods `getName()`, `getDimension()`, `display(ProcessedData processedData,
DisplayConfig displayConfig)`.  The `display()` method generates a Java Swing window that contains the visualization plots,
the data for these plots are passed through  `processedData`, and the display configuration is passed through `displayConfig`.

####### DisplayConfig
`DisplayConfig` contains a list of ```FeatureSelection```object, each ```FeatureSelection``` represents the selected features in each dimension. Moreover,
the last ```FeatureSelection``` indicates the selection of regions.

For example:
1. For 2D plots (e.g. bar chart, line chart), there will be three ```FeatureSelection``` in the list, which follow the following conventions:
* First element in the list is x axis selection (a list with only 1 item)
* Second element is y axis selections (a list with 1 or multiple items)
* Third element is region selections (a list with 1 or multiple regions)
2. For 1D plots (e.g. map), there will be two ```FeatureSelection``` in the list, which follow the following conventions:
* First element is the feature selection (a list with only 1 item)
* Second element is the region selections (a list with 1 or multiple regions)

####### ProcessedData
`ProcessedData` contains a `RegionalProcessedDataMap` which maps a region with its regional processed data, each regional processed data further contains a map between a feature name and its value in time series.
We can assume that the regional processed data will all have same length of the date interval and every feature has an array of equal length while some might be zero because of padding.
Moreover, when one region should only expect one InputDataPoint on one day, otherwise, an exception will be thrown.
Using this processed data, once can conveniently implement plugins to plot customizable graphs.

Moreover, the `processedData` contains a map of `predictFeatures`, which tells if a feature has a prediction. If the feature has a prediction, we can get the prediction value by reading the value from the ```RegionalFeatureMap```
by adding the ```edu.cmu.cs.cs214.hw5.core.Constant.PREDICT_PREFIX``` as a prefix to the feature name.

Note that, in the sample line chart DisplayPlugin, since the XChart only supports charts with equal length on the x and y asixs, the sample line chart
display plugin will draw a line with prediction only if both features on x and y axis are predicted features or the time feature.

####### Return
On a successful execution of the `display()` method, the display plugin should return true to notify the framework that the job has been done successfully, otherwise return false.
It is also acceptable to return Runtime Exceptions while encountering unexpected behaviors, the framework will handle it by catching the exception without terminating itself, the
detailed error message will be shown in an error dialog box from GUI.


### 2.2 How to run framework with new plugins
Once a new project containing a third-party plugin implementation is ready to integrate the framework, the project need 
to complete the following steps to set up and run the framework:

1. Create ```src/main/resources/META-INF.services``` directory and corresponding config file for either a data plugin or a display plugin.
2. Add ```[plugin].class``` into the configuration files of the framework project. 
   Under the ```src/main/resources/META-INF.services``` directory:
   * For data plugins, add ```[plugin].class``` into the ```edu.cmu.cs.cs214.hw5.core.DataPlugin``` file;
   * For display plugins, add ```[plugin].class``` into the ```edu.cmu.cs.cs214.hw5.core.DisplayPlugin``` file.
3. Create ```build.gradle``` and ```settings.gradle``` files for your project
4. Add ```rootProject.name``` and ```include``` framework project
5. Add dependencies on the framework to your ```build.gradle``` file. 
   More detailed information about how to add dependencies can be found in the sample ```plugin``` project. 
6. Configure ```edu.cmu.cs.cs214.hw5.Main``` class as the mainClassName your plugin project's ```build.gradle``` to launch the framework and its graphic user interface.


### 2.3 How to launch the application with the sample plugins (using the example plugin project)
In command line:

1. stays in either the root directory or the ```plugins``` directory
2. run the application with gradle run commend

    ```gradle run```


*** Sample data sources can be found in the ```plugins/src/main/resources``` directory:
~~~
1. CSV sample data 1: (which is a revision of the data from an open resource WebAPI)
   src/main/resources/world_covid_csv.csv
   src/main/resources/world_covid_config_csv.json

1. CSV sample data 2:
   src/main/resources/pennsylvania-history.csv
   src/main/resources/pen-history-config.json