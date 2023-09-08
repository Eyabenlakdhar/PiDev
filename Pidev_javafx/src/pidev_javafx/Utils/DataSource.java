/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Utils;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author MSI
 */
public class DataSource {
    private Connection cnx;

    public Connection getCnx() {
        return cnx;
    }
    private static DataSource instance;
    private DataSource(){
        try{
            cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/sepp","root","");
        }catch(SQLException e){
            System.out.println("##### JDBC Connection has failed. #####");
        }
    }
    public static  DataSource getInstance(){
        if(instance == null ){
            instance = new DataSource();
        }
        return instance;
    }
}
