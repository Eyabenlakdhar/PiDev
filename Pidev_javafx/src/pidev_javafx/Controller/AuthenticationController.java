/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class AuthenticationController implements Initializable {

    @FXML
    private StackPane document;
    public StackPane getDocument(){
        return this.document;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         LoginView();
    }  
    private void LoginView(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/Login.fxml"));
            AnchorPane root =  loader.load();
            LoginController lc = loader.getController();
            lc.setParent(this);
            document.getChildren().setAll(root);
        }catch(Exception e){
            System.out.println("hi");
            System.out.println(e.getLocalizedMessage());
        }
    }
    
}
