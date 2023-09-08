/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pidev_javafx.Entity.User;
import pidev_javafx.Main.Test;

/**
 *
 * @author MSI
 */
public class Session {
    private static ObservableList<User> session = FXCollections.observableArrayList();
    private final  String ADMIN = "ROLE_ADMIN";
    private final String USER = "ROLE_USER";
    public  void SUPER_SAYAN_MODE(){
        Test.getModes().add(isGranted(ADMIN)) ;
    }
    public void SAYAN_MODE(){
        Test.getModes().add(isGranted(USER)) ;
    }
    public static boolean isGranted(String role){
        return session.get(0).getRoles().toLowerCase().contains(role.toLowerCase());
    }
    public static ObservableList<User> getSession() {
        return session;
    }
    private Session(){
    }
    public Session(User user){
        if(session.isEmpty()){
            System.out.println("added");
            session.setAll(user);
        }
    }
    public static User getUser(){
        if(!session.isEmpty()){
            return session.get(0);
        }
        return null;
    }
    public static void Logout(){
        session.clear();
    }
}
