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
import model.appointment;
import model.apttype;

/**
 * FXML Controller class
 *
 * @author clink
 */
public class ReportbyCountController implements Initializable {

    //Stage Switching
    private Stage stage;
    
    //Connection Call for query use
    java.sql.Connection conn = DBConnection.conn;
    
    @FXML
    //tableview instance
    private TableView<apttype> ReportTable;
    @FXML private TableColumn<apttype, String> typeField;
    @FXML private TableColumn<apttype, String> countField;

    //Bind the main stage for scene switching
    void bind(Stage stage) {
        this.stage = stage;
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
    
    private void reportCount() throws SQLException{
        
        ObservableList<apttype> list = FXCollections.observableArrayList();
        String query = "select a.apt_type_id, b.description,count(*) from appointment as a join APTtype as b on a.apt_type_id = b.APTtype_id group by apt_type_id,  year(2020)";
        
        ResultSet rs = conn.createStatement().executeQuery(query);
        while(rs.next()){
            int typeID = rs.getInt("apt_type_id");
            String desc = rs.getString("description");
            int c = rs.getInt("count(*)");
            
            apttype a = new apttype();
            a.setAPTtypeID(typeID);
            a.setDescription(desc);
            a.setCount(c);
            
            list.add(a);
        }
        
        ReportTable.setItems(list);
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set columns
        typeField.setCellValueFactory(new PropertyValueFactory<>("description"));
        countField.setCellValueFactory(new PropertyValueFactory<>("count"));
        try {
            reportCount();
        } catch (SQLException ex) {
            Logger.getLogger(ReportbyCountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
