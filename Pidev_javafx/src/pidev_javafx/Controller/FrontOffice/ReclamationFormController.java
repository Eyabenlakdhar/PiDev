/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import pidev_javafx.Entity.Reclamation;
import pidev_javafx.Service.ReclamationService;
import pidev_javafx.Utils.Session;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class ReclamationFormController  {

    @FXML
    private Label title;
    @FXML
    private TextField rec_text;
    @FXML
    private TextArea rec_subject;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancel;
    private Reclamation updating;
    private ReclamationsController parent;
    public void setData(ReclamationsController parent, Popup popup,String formType,Reclamation r){
        this.parent = parent;
        if(formType.toLowerCase().equals("save")){
            title.setText("Create new Reclamation");
            submitBtn.setText("Save");
            submitBtn.setOnAction(e -> {
                this.Create(popup);
            });
        }
        else{
            title.setText("Update Reclamation");
           submitBtn.setText("Update");
           updating = r;
           rec_subject.setText(r.getRec_subject());
           rec_text.setText(r.getRec_text());
           submitBtn.setOnAction(e -> { 
               this.Update(popup);
           });
        }
        cancel.setOnAction(e -> popup.hide());
    }
    private void Create(Popup popup){
           List<String> errors = new ArrayList<String>();
        if(rec_text.getText().isEmpty()){
            errors.add("Reclamation Subject must not be empty.");
        }
        if(rec_subject.getText().isEmpty()){
            errors.add("Reclamation Description must not be empty.");
        }
        if(!errors.isEmpty()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Errors");
                alert.setHeaderText(null);
                alert.setContentText(errors.size() + " error" + (errors.size() > 1 ? "s" : "") + " have occurred:\n" + errors.stream().collect(Collectors.joining("\n")));
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() ) {
                    popup.show(parent.window);
                }
            });
        }
        else {
            new ReclamationService().create(new Reclamation(rec_text.getText(), new Date(System.currentTimeMillis()), rec_subject.getText(), Session.getUser().getId(), Boolean.FALSE, ""));
             Notifications notification = Notifications.create()
            .title("Reclamation Form")
            .text("Your Reclamation has been submitted.")
            .darkStyle()
            .hideAfter(javafx.util.Duration.seconds(5)); 
            notification.showInformation();
            popup.hide();
            parent.reclamationView("");
        }
       
    }
     private void Update(Popup popup){
           List<String> errors = new ArrayList<String>();
        if(rec_text.getText().isEmpty()){
            errors.add("Reclamation Subject must not be empty.");
        }
        if(rec_subject.getText().isEmpty()){
            errors.add("Reclamation Description must not be empty.");
        }
        if(!errors.isEmpty()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Errors");
                alert.setHeaderText(null);
                alert.setContentText(errors.size() + " error" + (errors.size() > 1 ? "s" : "") + " have occurred:\n" + errors.stream().collect(Collectors.joining("\n")));
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() ) {
                    popup.show(parent.window);
                }
            });
        }
        else {
            updating.setRec_subject(rec_subject.getText());
            updating.setRec_text(rec_text.getText());
            new ReclamationService().update(updating);
             Notifications notification = Notifications.create()
            .title("Reclamation Form")
            .text("Your Reclamation has been updated.")
            .darkStyle()
            .hideAfter(javafx.util.Duration.seconds(5)); 
            notification.showInformation();
            popup.hide();
            parent.reclamationView("");

        }
       
    }
}
