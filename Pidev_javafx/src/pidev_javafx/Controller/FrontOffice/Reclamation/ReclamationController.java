/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice.Reclamation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import org.controlsfx.control.Notifications;
import pidev_javafx.Entity.Reclamation;
import pidev_javafx.Main.Test;
import pidev_javafx.Service.ReclamationService;

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
    @FXML
    private GridPane reclamationLayer;
    @FXML
    private GridPane replyLayer;
    @FXML
    private AnchorPane replyForm;
    @FXML
    private TextArea reply;
    @FXML
    private Label emailField;
    @FXML
    private AnchorPane editContainer;
    @FXML
    private AnchorPane deleteContainer;
    public void setData(Reclamation reclamation,ReclamationsController cn){
        deleteContainer.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                if(c.next()){
                    if(c.wasAdded()){
                        AnchorPane.setBottomAnchor(c.getAddedSubList().get(0), 10.0);
                        AnchorPane.setLeftAnchor(c.getAddedSubList().get(0), 50.0);
                        AnchorPane.setRightAnchor(c.getAddedSubList().get(0), 50.0);
                        AnchorPane.setTopAnchor(c.getAddedSubList().get(0), 10.0);
                    }
                }
            }
        });
        editContainer.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Node> c) {
                if(c.next()){
                    if(c.wasAdded()){
                        AnchorPane.setBottomAnchor(c.getAddedSubList().get(0), 10.0);
                        AnchorPane.setLeftAnchor(c.getAddedSubList().get(0), 50.0);
                        AnchorPane.setRightAnchor(c.getAddedSubList().get(0), 50.0);
                        AnchorPane.setTopAnchor(c.getAddedSubList().get(0), 10.0);
                    }
                }
            }
        });
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
                                emailField.setText(reclamation.getUser().getEmail().toLowerCase());
                                deleteContainer.getChildren().add(editBtn);

                            }else {
                                deleteBtn.setDisable(false);
                               deleteBtn.setVisible(true);
                               deleteBtn.setManaged(true);
                                emailField.setText("");
                                editContainer.getChildren().add(editBtn);

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
            editBtn.setOnAction(ev -> respondView());
            emailField.setText(reclamation.getUser().getEmail().toLowerCase());
            if(editBtn.getParent() != deleteContainer)
                deleteContainer.getChildren().add(editBtn);
         }else {
             deleteBtn.setDisable(false);
            deleteBtn.setVisible(true);
            deleteBtn.setManaged(true);
            emailField.setText("");
            if(editBtn.getParent() != editContainer)
                editContainer.getChildren().add(editBtn);

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
            component.setOnMouseClicked(e -> {});
        }else {
            if(!component.getStyleClass().contains("button")){
                component.getStyleClass().add("button");
            }
            component.setOnMouseClicked(e -> {
                popDetails();
                });

        }
    }
    private void popDetails(){
         Popup popup = new Popup();
        
        VBox box = new VBox();
        box.setStyle("-fx-background-color:white;-fx-border-width: 2px;-fx-border-color: transparent;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0, 0, 2);");
        box.setPrefSize(600, 600);
        TextFlow flow = new  TextFlow(new Text(rec.getReponse()));
        flow.setTextAlignment(TextAlignment.CENTER);
        box.getChildren().setAll(new Label("Reclamation Response:"),flow);
        VBox.setVgrow(flow, Priority.ALWAYS);
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(10, 10, 10, 10));
        popup.getContent().add(box);
        popup.setAutoHide(true);
        popup.show(component.getScene().getWindow());
    }
    private void respondView(){
        replyForm.setVisible(true);
        replyLayer.toFront();
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

    @FXML
    private void cancelReply() {
        replyForm.setVisible(false);
        replyLayer.toBack();
    }

    @FXML
    private void Reply(ActionEvent event) {
         List<String> errors = new ArrayList<String>();
        if(reply.getText().isEmpty()){
            errors.add("Reclamation Reply must not be empty.");
        }
       
        if(!errors.isEmpty()){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Errors");
                alert.setHeaderText(null);
                alert.setContentText(errors.size() + " error" + (errors.size() > 1 ? "s" : "") + " have occurred:\n" + errors.stream().collect(Collectors.joining("\n")));
                alert.show();
            });
        }
        else {
            rec.setEtat(Boolean.TRUE);
            rec.setReponse(reply.getText());
            new ReclamationService().update(rec);
             Notifications notification = Notifications.create()
            .title("Reply Form")
            .text("Your Reply has been inserted.")
            .darkStyle()
            .hideAfter(javafx.util.Duration.seconds(5)); 
             notification.showInformation();
             cancelReply();
            setData(rec, parent);

        }

    }

    
    
}
