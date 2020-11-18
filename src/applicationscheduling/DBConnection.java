package applicationscheduling;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jnorr23
 */
public class DBConnection {
    
    //JDBC Parts
    private static final String proto = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ip = "//3.227.166.251/<dbname>";
    private static String pass = "";
    
    
    //JDBC Final URL 
    private static final String jdbcURL = proto + vendor + ip;
    
    //Driver Interface, Connection Interface
    private static final String mysqlJDBCDriver = "com.mysql.jdbc.Driver";
    public static Connection conn = null;
    
    
    private static final String username = ""; //Username for db
    private static final String password = ""; //Pass for db
    
    public static void startConnection(){
    
        try{
            Class.forName(mysqlJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    
    }
    
    public static void closeConnection(){
    
        try{
            conn.close();
            System.out.println("Connection Closed");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    
}
