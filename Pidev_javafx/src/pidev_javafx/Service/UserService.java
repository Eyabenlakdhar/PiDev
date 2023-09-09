/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Service;

import com.sun.deploy.security.UserDeclinedException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pidev_javafx.Controller.UserDuplicate_Exception;
import pidev_javafx.Entity.User;
import pidev_javafx.Exception.InvalidCredentials_Exception;
import pidev_javafx.Exception.NotFound_Exception;
import pidev_javafx.Main.Test;
import pidev_javafx.Utils.BCrypt;
import pidev_javafx.Utils.DataSource;
import pidev_javafx.Utils.Session;

/**
 *
 * @author MSI
 */
public class UserService {
    private final Connection cnx;
    public UserService(){
        this.cnx = DataSource.getInstance().getCnx();
    }
    public User findOne(Integer id){
        try{
            PreparedStatement ps = cnx.prepareStatement("SELECT * FROM USER WHERE ID = ? LIMIT 1");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                return new User(rs.getInt("id"), rs.getString("email"),null, null, null);
            }
            return null;
        }catch(Exception e){
            return null;
        }
        
    }
    public void Login(User u) throws SQLException,NotFound_Exception,InvalidCredentials_Exception{
        PreparedStatement ps = cnx.prepareStatement("SELECT * FROM USER WHERE EMAIL = ? LIMIT 1");
        ps.setString(1,u.getEmail().toLowerCase());
        ResultSet rs = ps.executeQuery();
        
        if(rs.next()){
            if (BCrypt.checkpw(u.getPassword(), rs.getString("password"))){
                u.setRoles(rs.getString("roles"));
                u.setId(rs.getInt("id"));
                Test.Session  = new Session(u);
                
            } else {
                throw new InvalidCredentials_Exception();
            }
        }else {
            throw new NotFound_Exception();
        } 
       
    }
    public void SignUp(User u ) throws SQLException, UserDuplicate_Exception{
            PreparedStatement pss = cnx.prepareStatement("SELECT * FROM USER WHERE EMAIL = ? LIMIT 1");
            pss.setString(1,u.getEmail().toLowerCase());
            ResultSet rs = pss.executeQuery();
            if(rs.next()){
                throw new UserDuplicate_Exception();
            }
            PreparedStatement ps = cnx.prepareStatement("INSERT INTO USER (email,password,roles) values (?,?,?) ");
            ps.setString(1,u.getEmail().toLowerCase());
            String hashed = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt(13));
            ps.setString(2, hashed);
            ps.setString(3, "[]");
            ps.executeUpdate();
            u.setRoles("[]");
            try{
                
             Login(u);
            }catch(Exception e){
                
            }

    }
}
