package com.Predictor.application;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.io.File;

import com.Predictor.Settings.*;


public class FileInController {
	File file;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button backButton;

	@FXML
	private AnchorPane childAnchor;

	/*@FXML
	private Spinner<ObservableList<Integer>> dateCount;

	@FXML
	private ComboBox<String> dateType;

	@FXML
	private ComboBox<Integer> epochCounter;
	*/

	@FXML
	private AnchorPane mainAnchor;

	@FXML
	private Button predictButton;
	
	@FXML
    private Button chooseButton;
	
	@FXML
    private TableView<FileIn> fileTableView;
	
	@FXML
    private TableColumn<FileIn, Double> tableClose;

    @FXML
    private TableColumn<FileIn, String> tableDate;

    @FXML
    private TableColumn<FileIn, Double> tableHigh;

    @FXML
    private TableColumn<FileIn, Double> tableLow;

    @FXML
    private TableColumn<FileIn, Double> tableOpen;

    @FXML
    private TableColumn<FileIn, Long> tableVolume; 
    
	@FXML
    void chooseButtonOnAction(ActionEvent event) {
		file = chooseFile();
		tableDate.setCellValueFactory(new PropertyValueFactory<>("dateString"));
		tableOpen.setCellValueFactory(new PropertyValueFactory<>("open"));
		tableHigh.setCellValueFactory(new PropertyValueFactory<>("high"));
		tableLow.setCellValueFactory(new PropertyValueFactory<>("low"));
		tableClose.setCellValueFactory(new PropertyValueFactory<>("close"));
		tableVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
		String CSVpath = file.getAbsolutePath();
		
		// CSV dosyasını oku ve TableView'a ekle
        ObservableList<FileIn> stockDataList = FileUploader.readCsv(CSVpath);
        fileTableView.setItems(stockDataList);    }

	@FXML
	void backButtonAction(ActionEvent event) {
		try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("/com.Predictor.application/FileIn.fxml"));
			childAnchor.getChildren().setAll(panel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	void predictButtonAction(ActionEvent event) {
		try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("DataGraph.fxml"));
			childAnchor.getChildren().setAll(panel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		//comboBoxAdder();
		tableDate.setCellValueFactory(new PropertyValueFactory<>("dateString"));
	    tableClose.setCellValueFactory(new PropertyValueFactory<>("close"));
	    tableVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
	    tableOpen.setCellValueFactory(new PropertyValueFactory<>("open"));
	    tableHigh.setCellValueFactory(new PropertyValueFactory<>("high"));
	    tableLow.setCellValueFactory(new PropertyValueFactory<>("low"));
	}

	//void comboBoxAdder() {
	//	ComboBoxHelper.setComboBoxStringItems(dateType, "Daily(Recommended)", "Monthly");
	//	ComboBoxHelper.setComboBoxIntegerItems(epochCounter, 4, 8, 16, 32, 64, 128, 256);
	//}
	File chooseFile() {
		Chooser e = new Chooser();
		file = e.chooseFile();
		return file;
	}
}
