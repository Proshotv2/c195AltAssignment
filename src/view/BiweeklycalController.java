/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Interfaces.appointmentInterface;
import applicationscheduling.ApplicationScheduling;
import applicationscheduling.DBConnection;
import dao.DBQuery;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.appointment;
import static view.MonthlycalController.selectedAppointment;

/**
 * FXML Controller class
 *
 * @author jnorr23
 */
public class BiweeklycalController implements Initializable {

    //Link the main stage for scene switching from Application Scheduling
    private Stage stage;

    @FXML
    //tableview instance
    private TableView<appointment> biweeklyTableview;
    @FXML private TableColumn<appointment, String> nameColumn;
    @FXML private TableColumn<appointment, String> timeColumn;
    @FXML private TableColumn<appointment, String> typeColumn;
    @FXML private TableColumn<appointment, String> noteColumn;
    
    public static appointment selectedAppointmentbiweekly;
    
    appointmentInterface appData = new DBQuery();
    ObservableList<appointment> listData;
    
    //Connection Call for query use
    java.sql.Connection conn = DBConnection.conn;
    
    /**
     * Bind the main stage for scene switching
     * @param app
     */
    public void bind(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    public void apptScreen(ActionEvent event) throws IOException{
    
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/genAppointment.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");

        GenAppointmentController controller = loader.getController(); 
        controller.bind(stage);
        
        stage.show();
        
    }
   
    @FXML
    public void patientScreen(ActionEvent event) throws IOException{
    
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
    
    @FXML
    private void weeklyCal(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/weeklycal.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");
               
        WeeklycalController controller = loader.getController(); 
        controller.bind(stage);
        
        stage.show();
    }
    
    @FXML
    public void CalScreen(ActionEvent event) throws IOException{
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
    
    @FXML
    private void reportType(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/ReportbyCount.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");
        
        ReportbyCountController controller = loader.getController(); 
        controller.bind(stage);
        

        stage.show();
    }
    
    @FXML
    private void stateReportType(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/ReportTotalsByState.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");
        
        ReportTotalsByStateController controller = loader.getController(); 
        controller.bind(stage);
        

        stage.show();
    }
    
    @FXML
    private void counselorReportType(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/ReportTotalByCounselor.fxml"));
        AnchorPane root = (AnchorPane) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");
        
        ReportTotalByCounselorController controller = loader.getController(); 
        controller.bind(stage);
        

        stage.show();
    }
    
    
    
    @FXML
    public void editAppointment(ActionEvent event) throws IOException{
        selectedAppointment = biweeklyTableview.getSelectionModel().getSelectedItem();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/genAppointment.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");

        GenAppointmentController controller = loader.getController();
        controller.bind(stage);
        
        stage.show(); 
    }
    
    
    /**
    * Delete appointment is meant to connect to the interface and use the DAO as a new constructor so that we can interact with our model
    * This allows us to get the selected item and interface with the model while utilizing the response filter to select and delete the right appointment.
    */
    @FXML
    public void deleteappointment(ActionEvent event) throws IOException{
    
        appointment app = biweeklyTableview.getSelectionModel().getSelectedItem();
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.initModality(Modality.NONE);
        a.setTitle("Delete Appointment");
        a.setHeaderText("Delete Appointment");
        a.setContentText("Are you sure you want to delete the appointment?");
        a.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    DBQuery pDAO = new DBQuery();
                    pDAO.deleteAppointment(app);
                    showAppointment();
                });
    
    }
    
    /**
     * For closing screen from file menu
     */
    public void closeScreen(){
        Platform.exit();
    }
    
    private void showAppointment(){
        listData = appData.getAllbiWeekly();
        biweeklyTableview.setItems(listData);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("ptName"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        showAppointment();
    }    
    
    
}
