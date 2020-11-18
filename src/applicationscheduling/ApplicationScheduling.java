/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationscheduling;

import applicationscheduling.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginController;

/**
 *
 * @author jnorr23
 */
public class ApplicationScheduling extends Application {

    private static String DRIVER;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    public static Connection connection = null;
    private final static Logger logger = Logger.getLogger("C195Log");
        //Initialize a resource bundle for logging in.
    private ResourceBundle bundle;
    
    //Initialize a locale to find where the desktop is located. 
    private Locale userLocale;
    
    // FXML Stage Main
    private Stage stage;
    
    
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException, ClassNotFoundException {
        primaryStage.setTitle("Testing");
        
        LoginScreen();
    }
    
    private static void createLog() throws IOException{
    
        FileHandler log;
        try{
            log = new FileHandler("SuccessLogin.txt", true);
            logger.addHandler(log);
            SimpleFormatter format = new SimpleFormatter();
            log.setFormatter(format);
            System.out.println("Successfully creating Log File");
        }
        catch(SecurityException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
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
     * @param args the command line arguments
     */
    public static void main(String[] args){
            
        DBConnection.startConnection();
        
        launch(args);
        
        DBConnection.closeConnection();
        
    }
    
}
