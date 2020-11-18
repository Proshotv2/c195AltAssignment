/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import applicationscheduling.ApplicationScheduling;
import applicationscheduling.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author clink
 */
public class RegisterController implements Initializable {

    private Stage stage;
    
    //Initialize a resource bundle for logging in.
    private ResourceBundle bundle;
    
    //Initialize a locale to find where the desktop is located. 
    private Locale userLocale;
    
    @FXML
    private TextField rUsername;
    
    @FXML
    private PasswordField rPassword;
    
    @FXML
    private PasswordField rPin;
    
    //connection
    java.sql.Connection conn = DBConnection.conn;

    
    //Bind the main stage for scene switching
    void bind(Stage stage) {
        this.stage = stage;
    }
    
        /** When any cancel button is clicked the event will fire and ask to cancel. 
    * Alert is used as a popup box to confirm and the rb we initialized will help get a referenced set of files in the correct language. 
    * Using a lambda expression to look for a response and exit the system or exit the prompt
    * this.userLocale and this.rb will get the default location and pull the resource for the titleconfirmation and textconfirmation
    **/
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        this.userLocale = Locale.getDefault();
        this.bundle = ResourceBundle.getBundle("resource", this.userLocale);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(this.bundle.getString("titleconfirmation"));
        alert.initModality(Modality.NONE);
        alert.setHeaderText(this.bundle.getString("titleconfirmation"));
        alert.setContentText(this.bundle.getString("textconfirmation"));
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
            try {
                LoginScreen();
            } catch (IOException ex) {
                Logger.getLogger(GenPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    void handleSave(ActionEvent event) throws IOException{
    
        String user = rUsername.getText();
        String pass = rPassword.getText();
        String pin = rPin.getText();
        
        if(user != null && pass != null && pin != null){
            try{
                String query = "INSERT INTO counselor(c_name, c_password, c_pin) values (?,?,?)";
                String updateQuery = "SET foreign_key_checks = 0";

                PreparedStatement s = conn.prepareStatement(updateQuery);
                s.execute();

                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.setString(3, pin);
                stmt.executeUpdate();

                //Confirm rows
                if(stmt.getUpdateCount() > 0){
                    System.out.println(stmt.getUpdateCount() + " row(s) affected. ");
                }
                else{
                    System.out.println("No Change!");
                }
                
                LoginScreen();

            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Register");
                    alert.setHeaderText("All fields need to be filled in to register.");
                    alert.setContentText("Please check the fields and try again.");
                    alert.showAndWait();
                    
        }
    
    }
    
    public void LoginScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/Login.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        
    
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");
        
        LoginController controller = loader.getController(); 
        controller.bind(stage);
        
        
        stage.show();  
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
