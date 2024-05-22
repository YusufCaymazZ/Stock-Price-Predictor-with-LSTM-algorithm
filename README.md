# Maven Stock Price Predictor Project Overview

This project is configured as a Maven project, utilizing Maven version 3.9.6 and Java 17 LTS for development. It includes dependencies from libraries such as DeepLearning4j, datavec, and Apache in the `pom.xml`. Notably, JavaFX version 21 and the MySQL connector are manually imported due to previous configurations.

## Project Structure

The project is organized into four different packages, each serving a distinct role:

### 1. `application` Package
![image](https://github.com/YusufCaymazZ/Stock-Price-Predictor-with-LSTM-algorithm/assets/113033309/c9322121-dda5-4c4d-95f3-3ef4945f929c)
*Figure 1: This image shows the GUI.*

This package contains the user interface components of the application, providing the graphical front-end that users interact with.

### 2. `settings` Package
This package supports the functions in the interface controller by hosting auxiliary classes, reinforcing the object-oriented nature of the project.

### 3. `model` Package
![image](https://github.com/YusufCaymazZ/Stock-Price-Predictor-with-LSTM-algorithm/assets/113033309/ed7115ed-ffee-425e-aaa8-5d8d7957d8c7)
*Figure 2: This image shows the graphical output of the model running with 32 epochs and a listener interval of 10.*
The model in this package currently operates on the `APPLE.csv` file. The operations are triggered by the "Start Predicting" button in the `DataGraphController` of the `application` package. The model is defined as a JavaFX task and uses an LSTM model, requiring configuration for epochs and iterations:
```java
network.fit(dataSetIterator, 64);
network.setListeners(new ScoreIterationListener(10));
```
This code snippet demonstrates setting the model to run for 64 epochs and setting the listener to check the score every 10 iterations.
### 4. `mysqlutil` Package
This package is specifically designed for managing the MySQL database connections. It includes utilities and helpers that facilitate interaction with the pricepredictor.sql database, ensuring smooth data transactions and connectivity.

### Consumer Information
Consumers of this repository should rely on the above information to correctly setup and utilize the project's capabilities.
