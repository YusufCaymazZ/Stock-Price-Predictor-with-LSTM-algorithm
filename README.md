# Maven Stock Price Predictor Project Overview

This project is configured as a Maven project, utilizing Maven version 3.9.6 and Java 17 LTS for development. It includes dependencies from libraries such as DeepLearning4j and datavec in the `pom.xml`. Notably, JavaFX version 21 and the MySQL connector are manually imported due to previous configurations.

## Project Structure

The project is organized into four different packages, each serving a distinct role:

### 1. `application` Package
This package contains the user interface components of the application, providing the graphical front-end that users interact with.

### 2. `settings` Package
This package supports the functions in the interface controller by hosting auxiliary classes, reinforcing the object-oriented nature of the project.

### 3. `model` Package
The model in this package currently operates on the `APPLE.csv` file. The operations are triggered by the "Start Predicting" button in the `DataGraphController` of the `application` package. The model is defined as a JavaFX task and uses an LSTM model, requiring configuration for epochs and iterations:
```java
network.fit(dataSetIterator, 64);
network.setListeners(new ScoreIterationListener(10));
