/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import pidev_javafx.Service.UserService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * FXML Controller class
 *
 * @author MSI
 */
public class SignupController implements Initializable {

    @FXML
    private GridPane parentGrid;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordField1;
    private AuthenticationController parent;
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
    private AnchorPane document;
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
    private void signUp(ActionEvent event) {
        List<String> errors = new ArrayList<String>();
        if(!isValidEmail(emailField.getText())){
            errors.add("Email must be valid.");
        }
        if(passwordField.getText().length() < 8){
            errors.add("Password must be atleast 8 letters.");
        }else {
            if(!(passwordField.getText().equals(passwordField1.getText()))){
                errors.add("Password confirmation must match.");
            }
        }
        if(!errors.isEmpty()){
            new Alert(Alert.AlertType.ERROR, errors.size()+" error"+(errors.size() > 1 ? "s " : " ") + " has occured: \n"+errors.stream().collect(Collectors.joining("\n")), ButtonType.OK).show();

        }else {
            try {
               new UserService().SignUp(new User(emailField.getText(), passwordField.getText()));
           } catch (SQLException ex) {
               System.out.println(ex.getMessage());
               new Alert(Alert.AlertType.ERROR, "Server is Down.", ButtonType.OK).show();
           } catch (UserDuplicate_Exception ex) {
               new Alert(Alert.AlertType.ERROR, "Email is already in use.", ButtonType.OK).show();
           }
        }
    }
    private static final String EMAIL_REGEX =
                "^[A-Za-z0-9+_.-]+@(.+)$"; 

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @FXML
    private void LoginView(ActionEvent event) {
         try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/Login.fxml"));
            AnchorPane root = loader.load();
            LoginController sc = loader.getController();
            sc.setParent(this.parent);
            this.parent.getDocument().getChildren().setAll(root);
        }catch(Exception e){
            
        }
    }
     public void setParent(AuthenticationController c){
        this.parent = c;
    }
    
}
