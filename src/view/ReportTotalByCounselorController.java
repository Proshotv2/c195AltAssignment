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
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.data;
import model.reportbyCounselor;


/**
 * FXML Controller class
 *
 * @author clink
 */
public class ReportTotalByCounselorController implements Initializable {

    //Stage Switching
    private Stage stage;
    
    //Connection Call for query use
    java.sql.Connection conn = DBConnection.conn;
    
    @FXML
    //tableview instance
    private TableView<reportbyCounselor> ReportTable;
    @FXML private TableColumn<reportbyCounselor, String> cField;
    @FXML private TableColumn<reportbyCounselor, String> totalField;

    //Bind the main stage for scene switching
    void bind(Stage stage) {
        this.stage = stage;
    }
    
    private void reportCount() throws SQLException{
        
        ObservableList<reportbyCounselor> list = FXCollections.observableArrayList();      
        String query = "select c_name, count(*) from appointment as a join counselor as c where a.cr_id = c.c_id group by c_id,  year(2020)";
        
        ResultSet rs = conn.createStatement().executeQuery(query);
        while(rs.next()){
            String c = rs.getString("c_name");
            int count = rs.getInt("count(*)");
            
            reportbyCounselor r = new reportbyCounselor();
            r.setCounselor(c);
            r.setTotal(count);
            
            list.add(r);
        }
        
        ReportTable.setItems(list);
        
        
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set columns
        cField.setCellValueFactory(new PropertyValueFactory<>("counselor"));
        totalField.setCellValueFactory(new PropertyValueFactory<>("total"));
        try {
            reportCount();
        } catch (SQLException ex) {
            Logger.getLogger(ReportbyCountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
