package com.Predictor.application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import com.PredictorMysql.Util.DatabaseConnection; 
public class LoginPageController {
	public LoginPageController() {
		baglanti = DatabaseConnection.getConnection();
	}
	
	Connection baglanti = null;
	PreparedStatement sorguIfadesi = null;
	ResultSet getirilen = null;
	String sql;
	public String username;
	public String password;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label DatabaseCheckLabel;

    @FXML
    private Button LoginButton;

    @FXML
    private Button SignInButton;

    @FXML
    private AnchorPane anch1;

    @FXML
    private AnchorPane anchor1;

    @FXML
    private AnchorPane anchor2;

    @FXML
    private Label header1;

    @FXML
    private Label labelAbout;

    @FXML
    private TextField pswrdTextField;

    @FXML
    private TextField usrnameTextField;
    
    
    @FXML
    void loginOnAction(ActionEvent event) {
    	sql = "select * from user where usrname=? and pswrd=?";
    	try {
    		sorguIfadesi = baglanti.prepareStatement(sql);
    		sorguIfadesi.setString(1, usrnameTextField.getText().trim());
    		sorguIfadesi.setString(2, pswrdTextField.getText().trim());
    		
    		ResultSet getirilen = sorguIfadesi.executeQuery();
    		if(!getirilen.next()) {
    			DatabaseCheckLabel.setText("kul ad veya şifre hatalı");
    		}
    		else {
    			System.out.println("kullanici_id:"+ getirilen.getString("kullanici_id"));
    			username = getirilen.getString(2);
    			System.out.println("kul_ad:"+ getirilen.getString(2));
    			password = getirilen.getString(3);
    			System.out.println("pswrd:"+ getirilen.getString(3));
    			System.out.println("oluşturulma tarihi:"+ getirilen.getString(4));
    			Alert alert = new Alert(AlertType.CONFIRMATION);//hata olursa uygulama içinde alert penceresi belirecek
                alert.setTitle("Succes!");
                alert.setHeaderText(null);
                alert.setContentText("Log in has completed successfully.");
                alert.showAndWait();
                if (username != null && !username.isEmpty()) {
                    // Geçerli kullanıcı adı var, diğer kontrolöre geçiş yapın
                    // Örneğin, bir sahne değişikliği yapabilirsiniz
                    // openFileChooser();
                    try {
        				AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("FileChooser.fxml"));
        				anchor2.getChildren().setAll(panel);
        			} catch (Exception e) {
        				// TODO: handle exception
        				e.printStackTrace();
        			}
                }
    		}

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println((e.getMessage().toString()));
		}
    }

    @FXML
    void signInOnAction(ActionEvent event) {
    	try {
			AnchorPane panel = (AnchorPane) FXMLLoader.load(getClass().getResource("SignUp.fxml"));
			anchor2.getChildren().setAll(panel);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    @FXML
    void initialize() {
        
    }
	
    /*private void openFileChooser() {
        // FileChooser penceresi açın ve username bilgisini diğer kontrolöre aktarın
        FileChooserController fileChooserController = new FileChooserController();
        fileChooserController.setUsername(username);
        fileChooserController.setPassword(password);
    }*/


}
