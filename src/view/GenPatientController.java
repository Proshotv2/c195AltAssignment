/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import applicationscheduling.ApplicationScheduling;
import applicationscheduling.DBConnection;
import dao.DBQuery;
import view.PatientOverviewController;
import exception.GeneralException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.patient;
import static view.PatientOverviewController.selectedCustomer;

/**
 * FXML Controller class
 *
 * @author jnorr23
 */
public class GenPatientController implements Initializable {
    
    //Link the main stage for scene switching from Application Scheduling
    private Stage stage;
    
    //Initialize a resource bundle for logging in.
    private ResourceBundle bundle;
    
    //Initialize a locale to find where the desktop is located. 
    private Locale userLocale;
    
    //Exception string for error messages
    private String errormessage = new String();
    
    //Connection Call for query use
    java.sql.Connection conn = DBConnection.conn;
    
    //comboboxes
    @FXML private TextField patName;
    @FXML private TextField ins;
    @FXML private TextField addr;
    @FXML private TextField city;
    @FXML private TextField state;
    @FXML private TextField p_code;
    @FXML private TextField phone;
    private int id;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(selectedCustomer != null){
            patName.setText(selectedCustomer.getPtName());
            ins.setText(selectedCustomer.getInspr());
            addr.setText(selectedCustomer.getAddress1());
            city.setText(selectedCustomer.getCity());
            state.setText(selectedCustomer.getState());
            p_code.setText(selectedCustomer.getPostalCode());
            phone.setText(selectedCustomer.getPhone());
        }
    }
            
    /**
     * Bind the main stage for scene switching
     * @param app
     */
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Method to call the add patient DAO
     */
    public void addPatient(){
        String pName = patName.getText();
        String insName = ins.getText();
        String address = addr.getText();
        String cCity = city.getText();
        String sState = state.getText();
        String postal = p_code.getText();
        String pPhone = phone.getText();
        
        
        DBQuery pDao = new DBQuery();
        pDao.insertPatients(pName, insName, address, cCity, sState, postal, pPhone);
    
    }
    
    /**
     * Method to call the update patient DAO
     * @throws SQLException 
     */
    public void modPatient() throws SQLException{
        
    
        String pName = patName.getText();
        String insName = ins.getText();
        String address = addr.getText();
        String cCity = city.getText();
        String sState = state.getText();
        String postal = p_code.getText();
        String pPhone = phone.getText();
        
        id = selectedCustomer.getPt_id();
        
        DBQuery pDao = new DBQuery();
        pDao.updatePatients(id, selectedCustomer.getAddressId(), pName, insName, address, cCity, sState, postal, pPhone);
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
                patientScreen();
            } catch (IOException ex) {
                Logger.getLogger(GenPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     * Handle the save button logic. This method will execute the validation and has logic for 
     * adding patients or modifying them with alert controls.
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        
        try{
            if (validate() == false){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Please fill out the entire form. All fields are required!");
                alert.setContentText("Please fill in every field!");
                alert.showAndWait();
            }else{
                if(selectedCustomer != null){
                    modPatient();
                    patientScreen();
                }else{
                    addPatient();
                    patientScreen();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot add Patient");
            alert.setContentText("Exception Please print the stack trace.");
            alert.showAndWait();
        }
        
    
    }
    
    @FXML
    private void patientScreen() throws IOException{
    
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/patientOverview.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");

        PatientOverviewController controller = loader.getController(); 
        controller.bind(stage);
        
        stage.show(); 
        
    }
    
    
    public void genPatientScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/genPatient.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");

        GenPatientController controller = loader.getController();
        controller.bind(stage);
        
        stage.show(); 
        
    }
    
    /**
     * Validate the Text fields. 
     * @return 
     */
    public boolean validate(){
        if(patName.getText().trim().length() == 0){
            return false;
        }
        if(ins.getText().trim().length() == 0){
            return false;
        }
        if(addr.getText().trim().length() == 0){
            return false;
        }
        if(city.getText().trim().length() == 0){
            return false;
        }
        if(state.getText().trim().length() == 0){
            return false;
        }
        if(p_code.getText().trim().length() == 0){
            return false;
        }
        if(phone.getText().trim().length() == 0){
            return false;
        }
        
        return true;
    }
            
    
    
}
