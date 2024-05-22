package com.Predictor.application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;

import com.PredictorMysql.Util.DatabaseConnection;
import com.Predictor.Settings.Chooser;
import com.Predictor.Settings.FileUploader;
import com.Predictor.Settings.FileInfos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FileChooserController {
	public FileChooserController() {
		baglanti = DatabaseConnection.getConnection();
	}

	Connection baglanti = null;
	PreparedStatement sorguIfadesi = null;
	ResultSet getirilen = null;
	String sql;
	// FILE CHOOSER PATH'I AŞAĞIDAKİ STRING NESNESİNDEN ALIP EXCELL'İ CSV'YE VE
	// CSV'Yİ DE DATABASE'E EKLEYEN CLASS'A BİR STATIC FOKSİYON YARDIMIYLA
	// GÖNDERECEK.
	String path = null;
	File file = null;
	String fileType = null;
	String fileName = null;
	int kullaniciId;
	
	@FXML
	private AnchorPane childAnch;

	@FXML
	private TextField pswrd;
	 
	@FXML
	private TextField usrname;
	 
	@FXML
	private Button backButton; // LOG OUT BUTTON

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button deleteButton;

	@FXML
	private TextField idTextField;

	@FXML
	private Button selectFileButton;

	@FXML
	private Button showButton;

	@FXML
	private TableColumn<FileInfos, String> tableFileName;

	@FXML
	private TableColumn<FileInfos, String> tableFileType;

	@FXML
	private TableColumn<FileInfos, Integer> tableID;

	@FXML
	private TableView<FileInfos> tableView;

	@FXML
	void deleteOnAction(ActionEvent event) {
		sql = "DELETE FROM files WHERE file_id= ?";
		try {
			sorguIfadesi = baglanti.prepareStatement(sql);
			sorguIfadesi.setInt(1, Integer.valueOf(idTextField.getText().trim()));
			sorguIfadesi.executeUpdate();
			BringTable(tableView);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage().toString());
		}
	}

	@FXML
	void select_to_add_on_click(ActionEvent event) { // SIRASIYLA DATE OPEN CLOSE HIGH LOW VOLUME OLMALI
		Chooser e = new Chooser();
		file = e.chooseFile();
		fileName = FileUploader.getFileNameWithoutExtension(file);
		fileType = FileUploader.getFileExtension(file);
		System.out.println(file.getAbsolutePath() + " - " + fileName + " - " + fileType);
		sql = "SELECT file_id, file_name, type FROM files";

		if (fileType.equals("csv")) {
			try {
				try {
				kullaniciId = FileUploader.getUserId(baglanti, usrname.getText().trim(), pswrd.getText().trim());
				}catch(Exception e1) {
					System.out.println(e1.getMessage().toString());
				}
				FileUploader.insertRecord(baglanti, kullaniciId, fileType, fileName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} /*
			 * else if (fileType.equals("xls") || fileType.equals("xlsx")) { //ŞU ANDA
			 * TEHLİKELİ try { FileUploader.processExcelFile(file, baglanti, kullaniciId,
			 * fileName); } catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); } catch (SQLException e1) { // TODO Auto-generated
			 * catch block e1.printStackTrace(); } }
			 */

		BringTable(tableView);
	}

	public void BringTable(TableView<FileInfos> table) {// değerleri getirmek için yazdığımız fonksiyon
		sql = "SELECT file_id, file_name, type FROM files";
		ObservableList<FileInfos> FileInfos1 = FXCollections.observableArrayList();

		try {
			sorguIfadesi = baglanti.prepareStatement(sql);
			ResultSet iterator = sorguIfadesi.executeQuery();
			while (iterator.next()) {
				FileInfos1.add(new FileInfos(iterator.getInt("file_id"), iterator.getString("file_name"),
						iterator.getString("type")));
			}
			tableID.setCellValueFactory(new PropertyValueFactory<>("file_id"));
			tableFileName.setCellValueFactory(new PropertyValueFactory<>("file_name"));
			tableFileType.setCellValueFactory(new PropertyValueFactory<>("file_type"));
			tableView.setItems(FileInfos1);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/*@FXML
    private void yeniFormuAc(String FormPath) {
        try {
            // Yeni formun FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FormPath));
            Parent root = loader.load();

            // Yeni bir sahne (Scene) oluştur
            Scene scene = new Scene(root);

            // Yeni bir sahne (Stage) oluştur ve sahneyi ayarla
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("See the File");

            // Yeni sahneyi göster
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
	@FXML
	void showOnAction(ActionEvent event) throws IOException {
		try {
            // Yeni formun FXML dosyasını yükle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FileIn.fxml"));
            Parent root = loader.load();

            // Yeni bir sahne (Scene) oluştur
            Scene scene = new Scene(root, 1200,800);

            // Yeni bir sahne (Stage) oluştur ve sahneyi ayarla
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Yeni Form");

            // Yeni sahneyi göster
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@FXML
	void backOnAction(ActionEvent event) { // LOG OUT BUTTON
		try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			childAnch.getChildren().setAll(panel);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		BringTable(tableView);
	}

	void SceneSwitcher() {
		try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("FileIn.fxml"));
			childAnch.getChildren().setAll(panel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
