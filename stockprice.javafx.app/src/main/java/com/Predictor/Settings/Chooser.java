package com.Predictor.Settings;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Chooser implements File_in_Settings {
	public File chooseFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Dosya Seç");

		// Sadece Excel dosyalarını gösterme filtresi
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"Excel veya CSV Dosyaları (*.xlsx, *.xls, *.csv)", "*.xlsx", "*.xls", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);

		// Dosya seçme penceresini açma ve seçilen dosyanın yolunu alma
		Stage stage = new Stage(); // Butonun sahne nesnesini alarak sahneyi kullanabilirsiniz.
		File dosya = null;
		try {
			dosya = fileChooser.showOpenDialog(stage);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dosya;
	}
}