/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import applicationscheduling.ApplicationScheduling;
import applicationscheduling.DBConnection;
import dao.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.patient;

/**
 * FXML Controller class
 *
 * @author clink
 */
public class PatientOverviewController implements Initializable {

    private Stage stage;
    
    public String editP = "false";
    
    @FXML
    //table instance this connects to DBQuery
    private TableView<patient> patientTable;
    
    @FXML private TableColumn<patient, String> pNameColumn;
    @FXML private TableColumn<patient, String> insColumn;
    @FXML private TableColumn<patient, String> addrColumn;
    @FXML private TableColumn<patient, String> cityColumn;
    @FXML private TableColumn<patient, String> stateColumn;
    @FXML private TableColumn<patient, String> postalColumn;
    @FXML private TableColumn<patient, String> phoneColumn;
    
    //Observable List for patients
    private ObservableList<patient> data = null;
    
    //Connection Call for query use
    java.sql.Connection conn = DBConnection.conn;
    
    public static patient selectedCustomer;
    
    /**
     * Bind the main stage for scene switching
     * @param app
     */
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * InitClass was meant to hold the patient info and set the cell value factory. 
     */
    public void initClass(){
    
        String getPatientInfoQuery = "SELECT pt_id, address.address_id, pt_name, INS_PR, addressline_1, city, state, postal_code, phone FROM patient left join address on patient.address_address_id = address.address_id";
        
        try{
            java.sql.Connection conn = DBConnection.conn;
            data = FXCollections.observableArrayList();
            
            ResultSet rs = conn.createStatement().executeQuery(getPatientInfoQuery);
            while(rs.next()){
                data.add(new patient(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
            
        }catch(Exception e){
        
            e.printStackTrace();
        
        }
        
        pNameColumn.setCellValueFactory(new PropertyValueFactory<>("ptName"));
        insColumn.setCellValueFactory(new PropertyValueFactory<>("inspr"));
        addrColumn.setCellValueFactory(new PropertyValueFactory<>("address1"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println(data);
        patientTable.setItems(null);
        initClass();
        System.out.println(data);
        patientTable.setItems(data);
    }    
    

    @FXML
    public void patientScreen(ActionEvent event) throws IOException{
    
        selectedCustomer = null;
        
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
    
    @FXML
    public void returntoMonthly(ActionEvent event) throws IOException{
    
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/MonthlyCal.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");

        MonthlycalController controller = loader.getController(); 
        controller.bind(stage);
        
        stage.show(); 
        
    }
    
    /**
    * Delete patient is meant to connect to the interface and use the DAO as a new constructor so that we can interact with our model
    * This allows us to get the selected item and interface with the model while utilizing the response filter to select and delete the right model.
    */
    @FXML
    public void deletepatient(ActionEvent event) throws IOException{
    
        patient p = patientTable.getSelectionModel().getSelectedItem();
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.initModality(Modality.NONE);
        a.setTitle("Delete Patient");
        a.setHeaderText("Delete Patient");
        a.setContentText("Are you sure you want to delete the Patient " + p.getPtName() + "?");
        a.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    DBQuery pDAO = new DBQuery();
                    pDAO.deletePatients(p);
                    reinit();
                });
    
    }
    
    /**
     * This class is to "Refresh the screen" when an update has been applied. I found that due to a bug with javafx one of the easier ways to deal with refreshing is to drop
     * the observable list and get it again via the database. The operation is not as friendly for handling large amounts of IO but is perfect in smaller based apps.
     */
    public void reinit(){
    
        patientTable.setItems(null);
        initClass();
        patientTable.setItems(data);
    
    }
    
    @FXML
    public void editPatient(ActionEvent event) throws IOException{
        selectedCustomer = patientTable.getSelectionModel().getSelectedItem();
        
        
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

    
}
