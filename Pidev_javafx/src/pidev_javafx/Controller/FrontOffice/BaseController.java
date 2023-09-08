/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pidev_javafx.Controller.SignupController;
import pidev_javafx.Main.Test;
import pidev_javafx.Utils.Session;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class BaseController implements Initializable {
    Set<Node> nav_links = new HashSet<>();
    @FXML
    private AnchorPane document;
    private ObservableList<Integer> router = FXCollections.observableArrayList();
    @FXML
    private Button reclamations_btn;
    @FXML
    private Button reservation_btn;
    @FXML
    private Button home_btn;
    @FXML
    private MenuButton emailLabel;
    @FXML
    private Label rootLabel;
    @FXML
    private AnchorPane section;
    @FXML
    private MenuItem adminSwitch;
    @FXML
    private Slider roleSlider;
    private  boolean [] ifs = { false,true };
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailLabel.setText(Session.getUser().getEmail().toLowerCase());
        if(!Session.isGranted("ROLE_ADMIN")){
            roleSlider.getParent().setDisable(true);
            roleSlider.getParent().setVisible(false);
            roleSlider.getParent().setManaged(false);
        }
       roleSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Platform.runLater(() -> {
                     if(roleSlider.getValue() > 0){
                    Test.Session.SUPER_SAYAN_MODE();
                }else {
                    Test.Session.SAYAN_MODE();
                }
                });
               
            }
        });
        nav_links.addAll(document.lookupAll(".nav_link"));
        section.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                if(c.next()){
                    if(c.wasAdded()){
                        AnchorPane.setBottomAnchor(c.getAddedSubList().get(0), 0.0);
                        AnchorPane.setTopAnchor(c.getAddedSubList().get(0), 0.0);
                        AnchorPane.setRightAnchor(c.getAddedSubList().get(0), 0.0);
                        AnchorPane.setLeftAnchor(c.getAddedSubList().get(0), 0.0);
                    }
                }
            }
        });
        router.addListener(new ListChangeListener<Integer> () {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Integer> c) {
                if(c.next()){
                    if(c.wasAdded()){
                        switch((int)c.getAddedSubList().get(0)){
                            case 0 : {
                                nav_links.forEach(btn -> btn.setDisable(false));
                                home_btn.setDisable(true);     
                                rootLabel.setText("Home");
                                break;

                            }
                            case 1 : {
                                nav_links.forEach(btn -> btn.setDisable(false));
                                reclamations_btn.setDisable(true);
                                rootLabel.setText("Reclamations");
                                try{
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/FrontOffice/Reclamations.fxml"));
                                    AnchorPane root = loader.load();
                                    section.getChildren().setAll(root);
                                }catch(Exception e){

                                }
                                break;

                            }
                            case 2 : {
                                nav_links.forEach(btn -> btn.setDisable(false));
                                reservation_btn.setDisable(true);
                                rootLabel.setText("Reservations");
                                break;
                            }
                            default:{
                                new Alert(Alert.AlertType.ERROR).show();
                            }
                        }
                    }
                }
            }
        });
        router.add(0);
    }    

    @FXML
    private void ReclamationsView(ActionEvent event) {
        router.clear();
        router.add(1);
    }

    @FXML
    private void ReservationView(ActionEvent event) {
        router.clear();
        router.add(2);
    }

    @FXML
    private void HomeView(ActionEvent event) {
        router.clear();
        router.add(0);
    }

    @FXML
    private void logout(ActionEvent event) {
        Session.Logout();
    }

    @FXML
    private void sliderAdjustor(MouseEvent event) {
        roleSlider.setValue(roleSlider.getValue() == 0 ? 0.1 : 0);
    }
    
}
