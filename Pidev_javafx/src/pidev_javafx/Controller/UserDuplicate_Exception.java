/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller;

/**
 *
 * @author MSI
 */
public class UserDuplicate_Exception extends Exception {
    public UserDuplicate_Exception(){
        super("user already exists");
    }
}
