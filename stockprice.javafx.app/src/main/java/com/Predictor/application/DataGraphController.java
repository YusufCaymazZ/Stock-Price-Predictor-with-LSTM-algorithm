package com.Predictor.application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.Predictor.Model.StockPriceModelTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.application.Platform;

public class DataGraphController {

    @FXML
    private Button startButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private AnchorPane mainAnchor;
    
    @FXML
    void handleStartButtonAction(ActionEvent event) {
        StockPriceModelTask modelTask = new StockPriceModelTask();
        progressBar.progressProperty().bind(modelTask.progressProperty());

        modelTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                progressBar.progressProperty().unbind();
                progressBar.setProgress(1.0);
                updateChart(modelTask.getActualValues(), modelTask.getPredictedValues(), modelTask.getFuturePredictedValues());
            });
        });

        modelTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0);
                System.out.println("Model execution failed: " + modelTask.getException().getMessage());
            });
        });

        Thread thread = new Thread(modelTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void updateChart(List<Double> actualValues, List<Double> predictedValues, List<Double> futurePredictedValues) {
        XYChart.Series<Number, Number> actualSeries = new XYChart.Series<>();
        actualSeries.setName("Actual Prices");
        for (int i = 0; i < actualValues.size(); i++) {
            actualSeries.getData().add(new XYChart.Data<>(i, actualValues.get(i)));
        }

        XYChart.Series<Number, Number> predictedSeries = new XYChart.Series<>();
        predictedSeries.setName("Predicted Prices");
        for (int i = 0; i < predictedValues.size(); i++) {
            predictedSeries.getData().add(new XYChart.Data<>(i, predictedValues.get(i)));
        }

        Platform.runLater(() -> {
            lineChart.getData().clear();
            lineChart.getData().addAll(actualSeries, predictedSeries);
        });
    }

    @FXML
    void initialize() {
        assert lineChart != null : "fx:id=\"lineChart\" was not injected: check your FXML file 'DataGraph.fxml'.";
        assert mainAnchor != null : "fx:id=\"mainAnchor\" was not injected: check your FXML file 'DataGraph.fxml'.";
        lineChart.setStyle("-fx-shape:\"\"; -fx-padding:0px");
        startButton.setOnAction(this::handleStartButtonAction);
        
    }
}
