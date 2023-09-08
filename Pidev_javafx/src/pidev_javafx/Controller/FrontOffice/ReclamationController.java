/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.Notifications;
import pidev_javafx.Entity.Reclamation;
import pidev_javafx.Main.Test;
import pidev_javafx.Service.ReclamationService;
import pidev_javafx.Utils.Session;

/**
 * FXML Controller class
 *
 * @author MSI 
 */
public class ReclamationController {
    private Reclamation rec;
    private ReclamationsController parent;
    @FXML
    private Label date;
    @FXML
    private Label text;
    @FXML
    private TextFlow subject;
    @FXML
    private VBox statusContainer;
    @FXML
    private Label status;
    @FXML
    private Button editBtn;
    @FXML
    private AnchorPane component;
    @FXML
    private Button deleteBtn;
    public void setData(Reclamation reclamation,ReclamationsController cn){
        Test.getModes().addListener(new ListChangeListener<Boolean>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Boolean> c) {
                    if(c.next()){
                         if(c.wasAdded()){
                            if(c.getAddedSubList().get(0)){
                               deleteBtn.setDisable(true);
                               deleteBtn.setVisible(false);
                               deleteBtn.setManaged(false);
                               editBtn.setText("Respond");
                               editBtn.setOnAction(ev -> System.out.print("Responding"));
                            }else {
                                deleteBtn.setDisable(false);
                               deleteBtn.setVisible(true);
                               deleteBtn.setManaged(true);
                            }
                    }
                }
            }
        });
        if(Test.getModes().get(Test.getModes().size()-1)){
            deleteBtn.setDisable(true);
            deleteBtn.setVisible(false);
            deleteBtn.setManaged(false);
            editBtn.setText("Respond");
            editBtn.setOnAction(ev -> System.out.print("Responding"));
         }else {
             deleteBtn.setDisable(false);
            deleteBtn.setVisible(true);
            deleteBtn.setManaged(true);
         }
        this.parent = cn;
        this.rec = reclamation;
        
        date.setText(String.valueOf(reclamation.getDate_rec()));
        subject.getChildren().setAll(new Text(reclamation.getRec_subject()));
        text.setText(reclamation.getRec_text());
        boolean isOpen = reclamation.getEtat();
        statusContainer.setStyle("-fx-background-color:"+( isOpen ? "green;" : "red;"));
        status.setText( isOpen ? "Closed;" : "Open");
        editBtn.setDisable(isOpen);
        if(!isOpen){
            component.getStyleClass().remove("button");
        }
    }

    @FXML
    private void editReclamation(ActionEvent event) {
        parent.updateForm(rec);
    }

    @FXML
    private void deleteReclamation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reclamation Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you Sure you Want to delete this Reclamation?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK ) {
             new ReclamationService().delete(rec);
            Notifications notification = Notifications.create()
           .title("Reclamation Delete")
           .text("Your Reclamation has been deleted.")
           .darkStyle()
           .hideAfter(javafx.util.Duration.seconds(5)); 
           notification.showInformation();
           parent.reclamationView("");
        }
    }

    
    
}
