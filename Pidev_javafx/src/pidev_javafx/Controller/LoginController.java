/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import pidev_javafx.Entity.User;
import pidev_javafx.Exception.InvalidCredentials_Exception;
import pidev_javafx.Exception.NotFound_Exception;
import pidev_javafx.Service.UserService;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class LoginController implements Initializable {
    private AuthenticationController parent;
    @FXML
    private AnchorPane document;
    @FXML
    private GridPane parentGrid;
    ChangeListener<Number> fonts_listener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> {
                    int val = Math.min((int)(document.getBoundsInLocal().getWidth() / 370.0),(int)(document.getBoundsInLocal().getHeight()/ 300.0));
                    fonts.forEach(node -> {
                        node.setStyle("-fx-font-size:"+Math.max(16, 16*val));
                    });
                    parentGrid.setHgap(5*Math.max(1,val));
                        
                });
            }
        };
    Set<Node> fonts ;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fonts =  new HashSet<>();
        fonts.addAll(document.lookupAll(".button"));
        fonts.addAll(document.lookupAll(".label"));
        fonts.addAll(document.lookupAll(".text-field"));
        document.widthProperty().addListener(fonts_listener);
        document.heightProperty().addListener(fonts_listener);
    }    

    @FXML
    private void login(ActionEvent event) {
        if(emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Form fields can't be empty.", ButtonType.OK).show();
        }else {
            try {
                new UserService().Login(new User(emailField.getText(), passwordField.getText()));
            }  catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Server is Down.", ButtonType.OK).show();
            } catch (NotFound_Exception ex) {
                new Alert(Alert.AlertType.ERROR, "User with given Email not found.", ButtonType.OK).show();
            } catch (InvalidCredentials_Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Given Credentials doesn't datch", ButtonType.OK).show();
            }
        }
  
    }

    @FXML
    private void SignUpView(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/Signup.fxml"));
            AnchorPane root = loader.load();
            SignupController sc = loader.getController();
            sc.setParent(this.parent);
            this.parent.getDocument().getChildren().setAll(root);
        }catch(Exception e){
            
        }
    }
    public void setParent(AuthenticationController c){
        this.parent = c;
    }
}
