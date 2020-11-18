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
import model.apttype;
import model.data;

/**
 * FXML Controller class
 *
 * @author clink
 */
public class ReportTotalsByStateController implements Initializable {

    //Stage Switching
    private Stage stage;
    
    @FXML
    //tableview instance
    private TableView<data> ReportTable;
    @FXML private TableColumn<data, String> stateField;
    @FXML private TableColumn<data, String> totalField;
    
    //Connection Call for query use
    java.sql.Connection conn = DBConnection.conn;

    //Bind the main stage for scene switching
    void bind(Stage stage) {
        this.stage = stage;
    }
    
    private void reportCount() throws SQLException{
        
        ObservableList<data> list = FXCollections.observableArrayList();      
        String query = "select state, count(*) from appointment as a join patient p, address d where a.pt_id = p.pt_id and p.address_id = d.address_id group by state,  year(2020)";
        
        ResultSet rs = conn.createStatement().executeQuery(query);
        while(rs.next()){
            String s = rs.getString("state");
            int c = rs.getInt("count(*)");
            
            data d = new data();
            d.setState(s);
            d.setTotal(c);
            
            list.add(d);
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
        stateField.setCellValueFactory(new PropertyValueFactory<>("state"));
        totalField.setCellValueFactory(new PropertyValueFactory<>("total"));
        try {
            reportCount();
        } catch (SQLException ex) {
            Logger.getLogger(ReportbyCountController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
