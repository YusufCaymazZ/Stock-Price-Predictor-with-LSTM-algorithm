package com.Predictor.application;

import java.io.IOException;
import java.util.ResourceBundle;

import com.PredictorMysql.Util.DatabaseConnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.*;

public class SignUpController {

	public SignUpController() {
		baglanti = DatabaseConnection.getConnection();
	}

	Connection baglanti = null;
	PreparedStatement sorguIfadesi = null;
	ResultSet getirilen = null;
	String sql;
	
	@FXML
	private PasswordField PasswordField;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private AnchorPane child_anchor;

	@FXML
	private Button signUpButton;

	@FXML
	private TextField usernameTextField;

	@FXML
	private Button backButton;

	@FXML
	public void SignUpOnAction(ActionEvent event) throws IOException {
		sql = "insert into user(usrname,pswrd) values(?,?)";
		try {
			sorguIfadesi = baglanti.prepareStatement(sql);
			sorguIfadesi.setString(1, usernameTextField.getText().trim());
			sorguIfadesi.setString(2, PasswordField.getText().trim());
			sorguIfadesi.executeUpdate();
			Alert alert = new Alert(AlertType.CONFIRMATION);//hata olursa uygulama içinde alert penceresi belirecek
            alert.setTitle("Succes!");
            alert.setHeaderText(null);
            alert.setContentText("Sign up has completed successfully.");
            alert.showAndWait();
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
		}

		// Form sayfası değişimi
		try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			child_anchor.getChildren().setAll(panel);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@FXML
	void backOnAction(ActionEvent event) {
		try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			child_anchor.getChildren().setAll(panel);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	void initialize() throws IOException {

	}
	
}