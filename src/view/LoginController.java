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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.appointment;
import model.counselor;
import model.reportbyCounselor;

/**
 * FXML Controller class
 *
 * @author jnorr23
 */
public class LoginController implements Initializable {
        
    //Initialize a resource bundle for logging in.
    private ResourceBundle bundle;
    
    //Initialize a locale to find where the desktop is located. 
    private Locale userLocale;
    
    //Initialize username label for possible change to different locale
    @FXML
    private Label usernameLabel;
    
    //Initialize a password label for possible change to a different locale
    @FXML
    private Label passwordLabel;
    
    //Initialize a pin label for possible change to a different locale
    @FXML
    private Label pinLabel;
    
    //Initialize Main Office Label
    @FXML
    private Label mainofficeLabel;
    
    //Initialize the CurrentOfficeLabel
    @FXML
    private Label currentofficeLabel;
    
    @FXML
    private Label mainlbl;
    
    @FXML
    private Label currentlbl;
    
    @FXML
    private Label apploginlbl;
    
    @FXML
    private Button cancelbtn;
    
    @FXML 
    private Button loginbtn;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwdField;
    
    @FXML
    private PasswordField pinField;
    
    //Link the main stage for scene switching from Application Scheduling
    private Stage stage;
    
    //connection
    java.sql.Connection conn = DBConnection.conn;
    
    private boolean validate;
            
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
    void handleCancel(ActionEvent event) {
        this.userLocale = Locale.getDefault();
        this.bundle = ResourceBundle.getBundle("resource", this.userLocale);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(this.bundle.getString("titleconfirmation"));
        alert.initModality(Modality.NONE);
        alert.setHeaderText(this.bundle.getString("titleconfirmation"));
        alert.setContentText(this.bundle.getString("textconfirmation"));
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.exit(0));
    }
    
    public void validateUser(TextField user, PasswordField password, PasswordField pin) throws SQLException{
        String query = "select * from counselor where c_name = ? and c_password =? and c_pin = ?";
        counselor c = new counselor();
        
        PreparedStatement stmt = conn.prepareStatement(query);
        
        stmt.setString(1, user.getText());
        stmt.setString(2, password.getText());
        stmt.setString(3, pin.getText());
        
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()){
            c.setcName(rs.getString("c_name"));
            c.setcPass(rs.getString("c_password"));
            c.setcPin(rs.getInt("c_pin"));
        }
        
        int fPin = Integer.parseInt(pin.getText());
        
        if(user.getText().equals(c.getcName()) & password.getText().equals(c.getcPass()) & fPin == c.getcPin()){
                validate = true;
        }else{
            validate = false;
        }
    }
    
    @FXML
    void LoginBtn (ActionEvent event) throws IOException, SQLException{
        
        ObservableList<String> list = FXCollections.observableArrayList();
        
        this.userLocale = Locale.getDefault();
        this.bundle = ResourceBundle.getBundle("resource", this.userLocale);
        
        validateUser(usernameField, passwdField, pinField);
        
        if(validate == true){
            logInfo("Successfully logged in at: " + LocalDateTime.now());
            
            //query database for all appointments within 4 hours, we must also set the timezone for consistency
            String timezone = "set time_zone = ?";
            PreparedStatement t = conn.prepareStatement(timezone);
            t.setString(1, "-05:00");
            t.execute();
            
            String query = "select * from appointment as a join patient as p on a.pt_id = p.pt_id";
            appointment a = new appointment();
             
            String finalAppt = null;
            ResultSet rs = conn.createStatement().executeQuery(query);
                while (rs.next()){
                    a.setPtName(rs.getString("pt_name"));
                    a.setStartDateTime(rs.getTimestamp("a.start_datetime"));
                    
                    //convert timestamp into LDT so we can split down to LocalTime and Date
                    LocalDateTime LDT = a.getStartDateTime().toLocalDateTime();
                    LocalDateTime localLDT = LocalDateTime.now();
                    ZonedDateTime localZDT = localLDT.atZone(ZoneId.of(ZoneId.systemDefault().getId()));
                    ZonedDateTime localConversion = localZDT.withZoneSameInstant(ZoneId.of("America/New_York"));
                    LocalTime ti = localConversion.toLocalTime();
                    
                    //get time params from above change to int to compare then re-apply
                    int dbTime = LDT.toLocalTime().getHour();
                    int localTime = ti.getHour();
                    
                    //calculation of 4 hours from login
                    if(LDT.toLocalDate().equals(localConversion.toLocalDate())){
                        if(localTime < dbTime
                            && (localTime + 4) >= dbTime){
                            finalAppt = a.getPtName() + " " + a.getStartDateTime() + " EST";
                            list.add(finalAppt);
                        }
                    }
                }
            try{        
                if(list.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointments");
                    alert.setHeaderText("There were no appointments found within 4 hours");
                    alert.setContentText("No appointments found.");
                    alert.showAndWait();
                }else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointments");
                    alert.setHeaderText("The following Appointments were found");
                    alert.setContentText("Appointments Found: " + list);
                    alert.setResizable(true);
                    alert.showAndWait();
                }
                CalScreen();
                
            }catch(Exception e){
                e.printStackTrace();
            }  
        }else{
            failedlogInfo("The ID, Password, and Pin do not match at: " + LocalDateTime.now());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(this.bundle.getString("loginFail"));
            alert.initModality(Modality.NONE);
            alert.setHeaderText(this.bundle.getString("loginFail"));
            alert.setContentText(this.bundle.getString("loginFail"));
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void registerBtn(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationScheduling.class.getResource("/view/register.fxml"));
        Parent root = (Parent) loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Fernandes Group Scheduler");

        RegisterController controller = loader.getController(); 
        controller.bind(stage);
        
        stage.show(); 
    }
    
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
        
        //This short lambda should exit the whole platform regardless of what's open if we hit the X in the top right.
        stage.setOnCloseRequest(e -> Platform.exit());
        
    }
    

    /**
     * Log every successful login. 
     * @param log 
     */
    public void logInfo(String log){
        Logger logger = Logger.getLogger("LoginSuccessLog");
        FileHandler file;
        
        try{
            file = new FileHandler("SuccessLogin.txt", true);
            logger.addHandler(file);
            SimpleFormatter format = new SimpleFormatter();
            file.setFormatter(format);
            
            logger.info(log);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     * Login failure method. Log everything to FailedLogin.txt
     * @param log 
     */
    public void failedlogInfo(String log){
        Logger logger = Logger.getLogger("LoginFailureLog");
        FileHandler file;
        
        try{
            file = new FileHandler("FailedLogin.txt", true);
            logger.addHandler(file);
            SimpleFormatter format = new SimpleFormatter();
            file.setFormatter(format);
            
            logger.info(log);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Get system default locale, and call the resource bundle. 
        this.userLocale = Locale.getDefault();
        this.bundle = ResourceBundle.getBundle("resource", this.userLocale);
        
        //Use the locale to change the username, password, pin, login, cancel when in a different locale
        usernameLabel.setText(this.bundle.getString("username"));
        passwordLabel.setText(this.bundle.getString("password"));
        pinLabel.setText(this.bundle.getString("pin"));
        
        //Change the text labels to reflect the time in PA (Main Office) and Current Time to the User.
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        df.setTimeZone(TimeZone.getDefault());
        Date dateObj = new Date();
        String dt = df.format(dateObj);
        
        //Set labels with bundled resources
        currentlbl.setText(this.bundle.getString("currenttime"));
        currentofficeLabel.setText(dt);
        
        DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        df2.setTimeZone(TimeZone.getTimeZone("EST"));
        Date date = new Date();
        String dt2 = df2.format(date);
        
        //Set labels based on locale.
        mainlbl.setText(this.bundle.getString("maintime"));
        mainofficeLabel.setText(dt2);
        apploginlbl.setText(this.bundle.getString("applogin"));
        cancelbtn.setText(this.bundle.getString("cancel"));
        loginbtn.setText(this.bundle.getString("login"));
    }    

    
    
}
