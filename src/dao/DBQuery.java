/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import Interfaces.appointmentInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import applicationscheduling.DBConnection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.appointment;
import model.patient;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jnorr23
 */
public class DBQuery implements appointmentInterface{
    
    private final static Connection conn = DBConnection.conn;
    private static int id;
    private static int pID;
    List<String> getPatientList;
    private int typeID;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    private int ptID;    
    private int aptID;
    private int stmtID;
    private int ptVar;
    
    
    /**
     * Returns all appointments by month. Converts date and time from EST Zone to Local Time Zone of user. 
     * @return 
     */
    @Override
    public ObservableList<appointment> getAllMonthly(){
    
        ObservableList<appointment> listData = FXCollections.observableArrayList();
        
        try{
            String sql = "select p.pt_id, a.apt_id, a.cr_id, a.apt_type_id, p.pt_name, t.description, a.notes, a.start_datetime from appointment a, patient p, APTtype t where p.pt_id = a.pt_id and a.apt_type_id = t.APTtype_id and month(a.start_datetime) = month(CURRENT_DATE())";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()){
                appointment a = new appointment();
                a.setApt_id(rs.getInt("a.apt_id"));
                a.setPtName(rs.getString("p.pt_name"));
                a.setDescription(rs.getString("t.description"));
                a.setNotes(rs.getString("a.notes"));
                Timestamp timestamp = rs.getTimestamp("a.start_datetime");
                
                //DB stores in Eastern. We take the system default and estzone then convert from EST to LocalZone.
                ZoneId localZone = ZoneId.systemDefault();
                
                LocalTime t = timestamp.toInstant().atZone(localZone).toLocalTime();
                LocalDate d = timestamp.toInstant().atZone(localZone).toLocalDate();
                LocalDateTime ldt = LocalDateTime.of(d, t);
                ZonedDateTime ZDT = ldt.atZone(ZoneId.of("America/New_York"));
                ZonedDateTime localZDT = ZDT.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().getId()));
                
                LocalDate d2 = localZDT.toLocalDate();
                LocalTime t2 = localZDT.toLocalTime();
                
                String startLocal = d2 + " " + t2 + ":00";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                java.util.Date parsedDate = dateFormat.parse(startLocal);
                Timestamp finalTimestamp = new java.sql.Timestamp(parsedDate.getTime());
                
                a.setStartDateTime(finalTimestamp);
                
                listData.add(a);
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listData;
        
    }
    
    @Override
    public ObservableList<appointment> getAllWeekly(){
    
        ObservableList<appointment> listData = FXCollections.observableArrayList();
        
        try{
            String sql = "select p.pt_id, a.apt_id, a.cr_id, a.apt_type_id, p.pt_name, t.description, a.notes, a.start_datetime from appointment a, patient p, APTtype t where p.pt_id = a.pt_id and a.apt_type_id = t.APTtype_id and week(a.start_datetime) = week(CURRENT_DATE())";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()){
                appointment a = new appointment();
                a.setApt_id(rs.getInt("a.apt_id"));
                a.setPtName(rs.getString("p.pt_name"));
                a.setDescription(rs.getString("t.description"));
                a.setNotes(rs.getString("a.notes"));
                Timestamp timestamp = rs.getTimestamp("a.start_datetime");
                
                //DB stores in Eastern. We take the system default and estzone then convert from EST to LocalZone.
                ZoneId localZone = ZoneId.systemDefault();
                
                LocalTime t = timestamp.toInstant().atZone(localZone).toLocalTime();
                LocalDate d = timestamp.toInstant().atZone(localZone).toLocalDate();
                LocalDateTime ldt = LocalDateTime.of(d, t);
                ZonedDateTime ZDT = ldt.atZone(ZoneId.of("America/New_York"));
                ZonedDateTime localZDT = ZDT.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().getId()));
                
                LocalDate d2 = localZDT.toLocalDate();
                LocalTime t2 = localZDT.toLocalTime();
                
                String startLocal = d2 + " " + t2 + ":00";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                java.util.Date parsedDate = dateFormat.parse(startLocal);
                Timestamp finalTimestamp = new java.sql.Timestamp(parsedDate.getTime());
                
                
                a.setStartDateTime(finalTimestamp);
                
                listData.add(a);
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listData;
        
    }
    
    @Override
    public ObservableList<appointment> getAllbiWeekly(){
    
        ObservableList<appointment> listData = FXCollections.observableArrayList();
        
        try{
            String sql = "select p.pt_id, a.apt_id, a.cr_id, a.apt_type_id, p.pt_name, t.description, a.notes, a.start_datetime from appointment a, patient p, APTtype t where p.pt_id = a.pt_id and a.apt_type_id = t.APTtype_id and a.start_datetime < date_add(CURDATE(), Interval 2 WEEK)";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while(rs.next()){
                appointment a = new appointment();
                a.setApt_id(rs.getInt("a.apt_id"));
                a.setPtName(rs.getString("p.pt_name"));
                a.setDescription(rs.getString("t.description"));
                a.setNotes(rs.getString("a.notes"));
                Timestamp timestamp = rs.getTimestamp("a.start_datetime");
                
                //DB stores in Eastern. We take the system default and estzone then convert from EST to LocalZone.
                ZoneId localZone = ZoneId.systemDefault();
                
                LocalTime t = timestamp.toInstant().atZone(localZone).toLocalTime();
                LocalDate d = timestamp.toInstant().atZone(localZone).toLocalDate();
                LocalDateTime ldt = LocalDateTime.of(d, t);
                ZonedDateTime ZDT = ldt.atZone(ZoneId.of("America/New_York"));
                ZonedDateTime localZDT = ZDT.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().getId()));
                
                LocalDate d2 = localZDT.toLocalDate();
                LocalTime t2 = localZDT.toLocalTime();
                
                String startLocal = d2 + " " + t2 + ":00";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                java.util.Date parsedDate = dateFormat.parse(startLocal);
                Timestamp finalTimestamp = new java.sql.Timestamp(parsedDate.getTime());
                
                
                a.setStartDateTime(finalTimestamp);
                
                listData.add(a);
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listData;
        
    }
    
    
    //Return Statement Object for patients
    public void getPatients(){
        
        
        String getPatientInfoQuery = "SELECT p.pt_name, p.INS_PR, a.addressline_1, addressline_2, city, state, postal_code, phone FROM patient p left join address a on p.pt_id = a.address_id";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(getPatientInfoQuery);
            
            //Execute Statement
            ResultSet ps1 = stmt.executeQuery();
            
            while(ps1.next()){
            
                String nName = ps1.getString("pt_name");
                String iNS = ps1.getString("INS_PR");
                String addr1 = ps1.getString("addressline_1");
                String addr2 = ps1.getString("addressline_2");
                String dCity = ps1.getString("city");
                String dState = ps1.getString("state");
                String dPostal = ps1.getString("postal_code");
                String dPhone = ps1.getString("phone");
                
                ObservableList<String> pList = FXCollections.observableArrayList(nName, iNS, addr1, addr2, dCity, dState, dPostal, dPhone);
            }

            //Confirm rows
            if(stmt.getUpdateCount() > 0){
                System.out.println(stmt.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void insertPatients(String pName, String insName, String address, String cCity, String sState, String postal, String pPhone) {
        
        String addPatQuery = "insert into patient(pt_name, address_id, INS_PR, address_address_id) values(?, ?, ?, ?);";
        String addAddressQuery = "insert into address(Addressline_1, city, state, postal_code, phone) Values (?, ?, ?, ?, ?);";
        
        try{
            PreparedStatement stmt = conn.prepareStatement(addAddressQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, address);
            stmt.setString(2, cCity);
            stmt.setString(3, sState);
            stmt.setString(4, postal);
            stmt.setString(5, pPhone);
            
            //Execute Statement
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                id = rs.getInt(1);
            }
            System.out.println(id);
            
            
            
            PreparedStatement stmt4 = conn.prepareStatement(addPatQuery, PreparedStatement.RETURN_GENERATED_KEYS); 
            stmt4.setString(1, pName);
            stmt4.setInt(2, id);
            stmt4.setString(3, insName);
            stmt4.setInt(4, id);
            stmt4.executeUpdate();
            ResultSet rs2 = stmt4.getGeneratedKeys();
            
            if(rs2.next()){
                pID = rs2.getInt(1);
            }
            System.out.println(pID);
            
            
            //Confirm rows
            if(stmt.getUpdateCount() > 0){
                System.out.println(stmt.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Update Patients as a static method
     * 
     */
    public void updatePatients(int ptid, int addrID, String pName, String insName, String address, String cCity, String sState, String postal, String pPhone) throws SQLException {
        
        String updateQuery = "SET foreign_key_checks = 0;";
        
        //Prepared statements to run
        PreparedStatement s = conn.prepareStatement(updateQuery);
        s.execute();
        
        try{
        String query = "UPDATE patient set pt_name = ?, INS_PR = ? where pt_id = ?";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, pName);
            stmt.setString(2, insName);
            stmt.setInt(3, ptid);
            stmt.execute();
            
            
            String query2 = "Update address set addressline_1 = ?, city = ?, state = ?, postal_code = ?, phone = ? where address_id = ?";
            
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setString(1, address);
            stmt2.setString(2, cCity);
            stmt2.setString(3, sState);
            stmt2.setString(4, postal);
            stmt2.setString(5, pPhone);
            stmt2.setInt(6, addrID);
            stmt2.execute();
            
            //Confirm sql rows
            if(stmt.getUpdateCount() > 0){
                System.out.println(stmt.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            
            if(stmt2.getUpdateCount() > 0){
                System.out.println(stmt2.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
        }
        catch(Exception e){
            e.printStackTrace();
    
        }
    }

    /**
     * Delete patients method
     * @param p 
     */
    public void deletePatients(patient p) {
        try{
            
            //query
            String query = "SET foreign_key_checks = 0";
            String query2 = "DELETE from address where address_id = ?";
            String query3 = "DELETE from patient where pt_id = ?";
            String query4 = "SET foreign_key_checks = 1";
            
            
            //Prepared statements to run
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.execute();
            
            PreparedStatement stmt2 = conn.prepareStatement(query2);
            stmt2.setInt(1, p.getAddressId());
            stmt2.executeUpdate();
            
            PreparedStatement stmt3 = conn.prepareStatement(query3);
            stmt3.setInt(1, p.getPt_id());
            stmt3.executeUpdate();
            
            PreparedStatement stmt4 = conn.prepareStatement(query4);
            stmt.execute();
            
            //Confirm rows
            if(stmt.getUpdateCount() > 0){
                System.out.println(stmt.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            
            if(stmt2.getUpdateCount() > 0){
                System.out.println(stmt2.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            if(stmt3.getUpdateCount() > 0){
                System.out.println(stmt3.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            if(stmt4.getUpdateCount() > 0){
                System.out.println(stmt4.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Insert Appointments method
     * @param a 
     */
    @Override
    public void insertAppointment(appointment a){
        String updateQuery = "SET foreign_key_checks = 0";
        
        
        try{
            //Prepared statements to run
            PreparedStatement s = conn.prepareStatement(updateQuery);
            s.execute();

            String addAptQuery = "INSERT INTO appointment(apt_type_id, APTtype_APTtype_id, counselor_c_id, cr_id, notes, patient_pt_id, pt_id, start_datetime) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            String aptTypeQuery = "select APTtype_id from APTtype where description = '" + a.getDescription() + "'";
            String ptQuery = "select pt_id, pt_name from patient where pt_name = '" + a.getPtName() + "'";
            
            //Set prepared statements and return Database ID
            PreparedStatement type = conn.prepareStatement(aptTypeQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement stmt = conn.prepareStatement(addAptQuery, PreparedStatement.RETURN_GENERATED_KEYS); 
            PreparedStatement pt = conn.prepareStatement(ptQuery);
            
            
            //APTtype Table
            ResultSet typeSet = type.executeQuery();
            if(typeSet.next()){
                typeID = typeSet.getInt(1);
                System.out.println(typeID);
            }
            
            
            ResultSet ptSet = pt.executeQuery();
            if(ptSet.next()){
                ptID = ptSet.getInt(1);
                System.out.println(ptID);
            }
            
            
            
            //Appointment Table
            stmt.setInt(1, typeID);
            stmt.setInt(2, typeID);
            stmt.setInt(3, 2);
            stmt.setInt(4, 2);
            stmt.setString(5, a.getNotes());
            stmt.setInt(6, ptID);
            stmt.setInt(7, ptID);
            stmt.setTimestamp(8, a.getStartDateTime());
            
            
            //Execute Statements
            stmt.executeUpdate();
            
            
            //Result of generatedkeys
            ResultSet Stmtrs = stmt.getGeneratedKeys();
            
            if(Stmtrs.next()){
                stmtID = Stmtrs.getInt(1);
            }
            
            
            //Confirm rows
            if(type.getUpdateCount() > 0){
                System.out.println(type.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            
            
            //Confirm rows
            if(stmt.getUpdateCount() > 0){
                System.out.println(stmt.getUpdateCount() + " row(s) affected. ");
            }
            else{
                System.out.println("No Change!");
            }
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Update Appointments method
     * @param a 
     */
    @Override
    public void updateAppointment(appointment a) {
        
        String updateQuery = "SET foreign_key_checks = 0";
        
        
        try{
            
            //Set Foreign Keys for update 
            PreparedStatement s = conn.prepareStatement(updateQuery);
            s.execute();
            
            String query = "select pt_id from patient where pt_name = '" + a.getPtName() + "'";
            PreparedStatement ptQ = conn.prepareStatement(query);
            ResultSet rs = ptQ.executeQuery();
            if(rs.next()){
                ptVar = rs.getInt(1);
            }
            
            //Get the APT type ID from the database for use in update
            String aptQuery = "select apt_type_id from appointment where apt_id = '" + a.getApt_id() + "'";
            PreparedStatement appt = conn.prepareStatement(aptQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ResultSet aptSet = appt.executeQuery();
            if(aptSet.next()){
                aptID = aptSet.getInt(1);
            }
            
            //Set the queries
            String aptTypeQuery = "Update APTtype set description = ? where APTtype_id = ?";
            String aQuery = "Update appointment set pt_id = ?, notes = ?, start_datetime = ? where apt_id = ?";
            
            
            
            //Set prepared statements and return Database ID
            PreparedStatement apt = conn.prepareStatement(aptTypeQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement app = conn.prepareStatement(aQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            
            //SafeGuard the data by placing it in sets vs using the query directly. 
            apt.setString(1, a.getDescription());
            apt.setInt(2, aptID);
            
            app.setInt(1, ptVar);
            app.setString(2, a.getNotes());
            app.setTimestamp(3, a.getStartDateTime());
            app.setInt(4, a.getApt_id());
            
            //pat.execute();
            apt.execute();
            app.execute();
            
            //Confirm rows
            if (apt.getUpdateCount() > 0){
                System.out.println(apt.getUpdateCount() + " row(s) affected. APT Type");
            }
            else{
                System.out.println("No Change!");
            }
            if(app.getUpdateCount() > 0){
                System.out.println(app.getUpdateCount() + " row(s) affected. Appointmnet");
            }
            else{
                System.out.println("No Change!");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Delete appointments method
     * @param a 
     */
    @Override
    public void deleteAppointment(appointment a) {
        String query = "SET foreign_key_checks = 0";
        String appQuery = "delete from appointment where apt_id = ?";
        
        
        try{
            //Set Foreign Keys for update 
            PreparedStatement s = conn.prepareStatement(query);
            s.execute();
            
            PreparedStatement app = conn.prepareStatement(appQuery);
            app.setInt(1, a.getApt_id());
            app.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }



    
    
}
