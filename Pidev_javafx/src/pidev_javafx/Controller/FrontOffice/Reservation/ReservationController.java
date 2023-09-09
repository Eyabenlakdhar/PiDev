/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice.Reservation;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.Notifications;
import pidev_javafx.Controller.FrontOffice.Reclamation.ReclamationsController;
import pidev_javafx.Entity.Reservation;
import pidev_javafx.Main.Test;
import pidev_javafx.Service.ReclamationService;
import pidev_javafx.Service.ReservationService;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class ReservationController implements Initializable {

    @FXML
    private TextFlow resType;
    @FXML
    private TextFlow resComment;
    @FXML
    private TextFlow resDate;
    @FXML
    private AnchorPane actions;
    @FXML
    private FlowPane component;
    @FXML
    private Button deleteBtn;
    @FXML
    private Text typeText;
    @FXML
    private Text commentText;
    @FXML
    private Text dateText;
    @FXML
    private Button editBtn;
    @FXML
    private TextFlow cost;
    @FXML
    private Text costText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()  ->{
            resType.minWidthProperty().bind(component.maxWidthProperty().add(-20));
            resDate.minWidthProperty().bind(component.maxWidthProperty().add(-20));
            resComment.minWidthProperty().bind(component.maxWidthProperty().add(-20));
            cost.minWidthProperty().bind(component.maxWidthProperty().add(-20));
            cost.maxWidthProperty().bind(component.maxWidthProperty().add(-20));
            resType.maxWidthProperty().bind(component.maxWidthProperty().add(-20));
            resDate.maxWidthProperty().bind(component.maxWidthProperty().add(-20));
            resComment.maxWidthProperty().bind(component.maxWidthProperty().add(-20));
        });
    }    
    private Reservation reservation;
    private ReservationsController parent;
    public void setData(Reservation r,ReservationsController controller){
        this.parent = controller;
        this.reservation = r;
        typeText.setText(r.getType());
        commentText.setText(r.getComment());
        dateText.setText(String.valueOf(r.getDate_res()));
        costText.setText(r.getPrice()+" $/hr");
        if(r.getUser_id() != Test.Session.getUser().getId()){
            deleteBtn.setVisible(false);
            deleteBtn.setManaged(false);
            deleteBtn.setDisable(true);
            editBtn.setVisible(false);
            editBtn.setManaged(false);
            editBtn.setDisable(true);
        }
        if(r.getDate_res().before(Date.from(Instant.now()))){
            deleteBtn.setVisible(false);
            deleteBtn.setManaged(false);
            deleteBtn.setDisable(true);
            editBtn.setVisible(false);
            editBtn.setManaged(false);
            editBtn.setDisable(true);
        };
    }
    @FXML
    private void deleteReservation(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reservation Delete");
        alert.setHeaderText(null);
        alert.setContentText("Are you Sure you Want to delete this Reservation?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK ) {
             new ReservationService().delete(this.reservation);
            Notifications notification = Notifications.create()
           .title("Reservation Delete")
           .text("Your Reservation has been deleted.")
           .darkStyle()
           .hideAfter(javafx.util.Duration.seconds(5)); 
           notification.showInformation();
           ((VBox)component.getParent()).getChildren().remove(component);
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        this.parent.setUpdating(this.reservation);
    }
    
}
