/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Interfaces.appointmentInterface;
import applicationscheduling.ApplicationScheduling;
import applicationscheduling.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dao.DBQuery;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.DatePicker;
import static view.MonthlycalController.selectedAppointment;
import static view.WeeklycalController.selectedAppointmentweekly;
import static view.BiweeklycalController.selectedAppointmentbiweekly;

/**
 * FXML Controller class
 *
 * @author clink
 */
public class GenAppointmentController implements Initializable {
    
    //Link the main stage for scene switching from Application Scheduling
    private Stage stage;
    
    //Initialize a resource bundle for logging in.
    private ResourceBundle bundle;
    
    //Initialize a locale to find where the desktop is located. 
    private Locale userLocale;
    
    //connection
    java.sql.Connection conn = DBConnection.conn;
    
    @FXML private DatePicker dPick;
    @FXML private ComboBox<String> counselorCombo;
    @FXML private ComboBox<String> patientCombo;
    @FXML private ComboBox<String> typeCombo;
    @FXML private TextArea noteTextarea;
    @FXML private ComboBox<String> startCombo;
    private String errormessage = new String();
    private String ErrorMessage = new String();
    private LocalDate localDate;
    private ObservableList<String> patientData;
    private ObservableList<String> counselor;
    
    appointmentInterface aInt = new DBQuery();
    public static int tptID;
    private String ptname;
    
    
    /**
     * Bind the main stage for scene switching
     * @param app
     */
    public void bind(Stage stage) {
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
                CalScreen();
            } catch (IOException ex) {
                Logger.getLogger(GenPatientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    @FXML
    public void CalScreen() throws IOException{
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
     * One of the beefy classes due to checking for Major Holidays, Overlaps, business hours and whether we are adding or modifying.
     * @param event
     * @throws IOException
     * @throws SQLException
     * @throws ParseException 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException, SQLException, ParseException {
        
        LocalDate date = dPick.getValue();
        String cr = counselorCombo.getValue();
        String pt = patientCombo.getValue();
        String type = typeCombo.getValue();
        String note = noteTextarea.getText();
        String start = startCombo.getValue();
        
        //Convert startCombo from string to Timestamp for easy entry into the database. 
        String comboDate = date + " " + start + ":00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parsedDate = dateFormat.parse(comboDate);
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        
        if(validate() == true){
            //Check for holidays
            if(isMajorHoliday(parsedDate) == false){
                //check for any overlap against the entire database
                if(getOverlap(timestamp, pt) == true){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Overlapping Appointment Time/Patient/Counselor");
                    alert.setHeaderText("Cannot add appointment");
                    alert.setContentText("Found an overlapping appointment for one of the following: " + timestamp + " or Patient or Counselor");
                    alert.showAndWait();
                }else{
                    //check for business hours
                    if(isOpen(parsedDate) == false){
                        try{
                            //create a new appointment to hold the values
                            appointment a = new appointment();
                            a.setDescription(type);
                            a.setNotes(note);
                            a.setStartDateTime(timestamp);

                            if(selectedAppointment != null){
                                a.setApt_id(selectedAppointment.getApt_id());
                                a.setPtName(pt);
                                aInt.updateAppointment(a);
                                CalScreen();
                            }else if(selectedAppointmentweekly != null){
                                a.setApt_id(selectedAppointmentweekly.getApt_id());
                                a.setPtName(pt);
                                aInt.updateAppointment(a);
                                CalScreen();
                            }else if(selectedAppointmentbiweekly != null){
                                a.setApt_id(selectedAppointmentbiweekly.getApt_id());
                                a.setPtName(pt);
                                aInt.updateAppointment(a);
                                CalScreen();
                            }else{
                                a.setPtName(pt);
                                aInt.insertAppointment(a);
                                CalScreen();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Error");
                            alert.setHeaderText("Cannot add appointment");
                            alert.setContentText("Failure");
                            alert.showAndWait();
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Outside of Business hours");
                        alert.setHeaderText("Cannot add appointment");
                        alert.setContentText("You have attempted to schedule an appointment outside of business hours 9am EST - 9pm EST, Monday through Friday.");
                        alert.showAndWait();
                    }
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Major Holiday");
                alert.setHeaderText("Cannot add appointment");
                alert.setContentText("You have attempted to schedule an appointment on one of the following holidays: Martin Luther King Jr Day, Memorial Day, Thanksgiving, Day after Thanksgiving.");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Cannot Add Appointment");
            alert.setContentText("All fields are required. Please try again.");
            alert.showAndWait();
        }
        
        
    }
    
    /**
     * Get all appointments in database by timestamp. Then run through this function to identify overlap.
     * Separate localdate, and localtime then compare them with current form.
     * @param t
     * @param pt
     * @return
     * @throws SQLException 
     */
    public boolean getOverlap(Timestamp t, String pt) throws SQLException{
        
        //Array to hold all of them.
        ObservableList<String> list = FXCollections.observableArrayList();
        ObservableList<String> listNames = FXCollections.observableArrayList();
        String query = "select * from appointment as a join patient as p on a.pt_id = p.pt_id";
        
        //Get all appointments
        ResultSet rs = conn.createStatement().executeQuery(query);
        while(rs.next()){
            Timestamp time = rs.getTimestamp("a.start_datetime");
            String sTime = time.toString();
            list.add(sTime);
            
            String name = rs.getString("pt_name");
            listNames.add(name);
        }
        
        boolean timeMatch = false;
        boolean nameMatch = false;
        boolean Final = false;
        //Evaluate timestamp against timestamp from database, if true there is a conflict. 
        for(String x : list){
            if(t.toString().equals(x)){
                timeMatch = true;
            }
        }
        for(String x : listNames){
            if(pt == x){
                nameMatch = true;
            }
        }
        
        if(timeMatch == true){
            //If start time is the same then set to true.
            Final = true;
        }else if(nameMatch == true & timeMatch == true){
            //overlapping the same patient at the same time
            Final = true;
        }
        
        return Final;
        
        
    }
    
    
    /**
     * Method to find major holidays
     * @param date
     * @return 
     */
    public static boolean isMajorHoliday(Date date){
              
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        //Check Memorial Day
        if(cal.get(Calendar.MONTH) == Calendar.MAY 
            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
            && cal.get(Calendar.DAY_OF_MONTH) > (31 - 7) ){
            return true;
        }
        
        //Martin Luther King Jr Day
        if(cal.get(Calendar.MONTH) == Calendar.JANUARY
            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
            && cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3 ){
            return true;
        }
        
        //Veterans Day
        if(cal.get(Calendar.MONTH) == Calendar.NOVEMBER
            && cal.get(Calendar.DAY_OF_MONTH) == 11){
            return true;
        }
        
        
        //Thanksgiving Day 
        if(cal.get(Calendar.MONTH) == Calendar.NOVEMBER
            && cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 4
            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            return true;
        }
        
        
        //Day after Thanksgiving
        if(cal.get(Calendar.MONTH) == Calendar.NOVEMBER
            && cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 4
            && cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
            return true;
        }
        
        //if nothing found return false
        return false;
    }
    
    /**
     * Method to find business days/hours.
     * @param date
     * @return 
     */
    public static boolean isOpen(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        //Check day of week
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            return true;
        }
        
        if(cal.get(Calendar.HOUR_OF_DAY) < 9
                || cal.get(Calendar.HOUR_OF_DAY) > 21){
            return true;
        }
        
        
        return false;
    }
    
     /**
      * This method helps combine code and calls the database in order to gather info to place into combo boxes. 
      */
     public void initClass(){
     
        String query = "SELECT p.pt_id, p.pt_name, p.INS_PR, a.addressline_1, addressline_2, city, state, postal_code, phone FROM patient p left join address a on p.pt_id = a.address_id";
        String cQuery = "select * from counselor";
        
        appointment a = new appointment();
        
        //Most of this section is getting the combo list field setup from the database based on patients who are in the system. 
        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            patientData = FXCollections.observableArrayList();
            while(rs.next()){
                String pName = rs.getString(2);
                patientData.add(pName);
            }
            PreparedStatement stmt2 = conn.prepareStatement(cQuery);
            ResultSet rs2 = stmt2.executeQuery();
            counselor = FXCollections.observableArrayList();
            
            while(rs2.next()){
                String cName = rs2.getString("c_name");
                counselor.add(cName);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            if(selectedAppointment != null){
                //Convert Timestamp back to eastern.
                ZonedDateTime timeLDT = selectedAppointment.getStartDateTime().toLocalDateTime().atZone(ZoneId.of(ZoneId.systemDefault().getId()));
                ZonedDateTime estZDT = timeLDT.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime t = estZDT.toLocalTime();
                
                //get fields
                counselorCombo.setValue("admin");
                patientCombo.setValue(selectedAppointment.getPtName());
                typeCombo.setValue(selectedAppointment.getDescription());
                noteTextarea.setText(selectedAppointment.getNotes());
                startCombo.setValue(t.toString());
                dPick.setValue(selectedAppointment.getStartDateTime().toLocalDateTime().toLocalDate());
                
                //Load fields
                patientCombo.getItems().addAll(patientData);
                typeCombo.getItems().addAll("Checkup", "vaccination");
                startCombo.getItems().addAll("8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00");
            }else{
                counselorCombo.getItems().addAll(counselor);
                patientCombo.getItems().addAll(patientData);
                typeCombo.getItems().addAll("Checkup", "vaccination");
                startCombo.getItems().addAll("8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00");
            }
     }
     
     /**
      * Validate fields to ensure no nulls are found.
      * @return 
      */
     public boolean validate(){
         
         if(patientCombo.getSelectionModel().getSelectedItem() == null){
             return false;
         }
         if(typeCombo.getSelectionModel().getSelectedItem() == null){
             return false; 
         }
         if(noteTextarea.getText().trim().length() == 0){
             return false;
         }
         if(startCombo.getSelectionModel().getSelectedItem() == null){
             return false;
         }
         if(dPick.getValue() == null){
             return false;
         }
         return true;
     }
     

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
        initClass();
        
        
    }    
    
}
