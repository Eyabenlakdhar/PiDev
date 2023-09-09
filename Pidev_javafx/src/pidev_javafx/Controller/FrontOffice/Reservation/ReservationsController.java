/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice.Reservation;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.Notifications;
import pidev_javafx.Entity.Reservation;
import pidev_javafx.Main.Test;
import pidev_javafx.Service.ReservationService;
import pidev_javafx.Utils.Session;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class ReservationsController implements Initializable {
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList(); // Your list of reservations
    @FXML
    private AnchorPane document;
    @FXML
    private AnchorPane listSection;
    private static int itemsPerPage = 10;
    @FXML
    private ComboBox<String> visibilityFilter;
    @FXML
    private ComboBox<Integer> paginationFilter;
    private final String [] visibilities = {"Mine","Others","All"};
    private final String [] reservationTypes = {"mecanicien","femme de ménage","plombier","medecin"};
    private final Integer [] paginationItemsCount = {10,20,40};
    @FXML
    private TextField searchField;
    @FXML
    private Label title;
    @FXML
    private ComboBox<String> type;
    @FXML
    private TextArea comment;
    @FXML
    private Button submitBtn;
    @FXML
    private Button cancel;
    @FXML
    private DatePicker date_res;
    @FXML
    private TextField price;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        type.getItems().setAll(reservationTypes);
        date_res.setValue(LocalDate.now());
        date_res.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            Platform.runLater(() -> { 
                if(date_res.getValue().isBefore(LocalDate.now())){
                    date_res.setValue(LocalDate.now());
                    Notifications notification = Notifications.create()
                    .title("Reservation Validation")
                    .text("Date of the reservation should be atleast today.")
                    .darkStyle()
                    .hideAfter(javafx.util.Duration.seconds(5)); 
                    notification.showInformation();
                }
            });
        });
        submitBtn.setOnAction(e -> save());
        cancel.setOnAction(e -> clearForm());
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            showReservations();
        });
        visibilityFilter.getItems().setAll(visibilities);
        paginationFilter.getItems().setAll(paginationItemsCount);
        paginationFilter.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            Platform.runLater(() -> {
                itemsPerPage = newValue;
                showReservations();
            });
        });
        visibilityFilter.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Platform.runLater(() -> {
                showReservations();
            });
        });
         
        listSection.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            if(c.next()){
                if(c.wasAdded()){
                    AnchorPane.setTopAnchor(c.getAddedSubList().get(0), 0.0);
                    AnchorPane.setRightAnchor(c.getAddedSubList().get(0), 0.0);
                    AnchorPane.setBottomAnchor(c.getAddedSubList().get(0), 0.0);
                    AnchorPane.setLeftAnchor(c.getAddedSubList().get(0), 0.0);
                }
            }
        });
        showReservations();

        
        
    }
    private void save(){
        List<String> errors = new ArrayList<String>();
        double cost = 0;
        if(type.getValue() == null){
            errors.add("Select a Service Type.");
        }
        if(comment.getText().isEmpty()){
            errors.add("Description can not be empty.");
        }
        try{
             cost = Double.valueOf(price.getText());
         }catch(Exception e){
             errors.add("Service cost must only contain numbers.");
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
            new ReservationService().create(new Reservation(Date.valueOf(date_res.getValue()), type.getValue(), comment.getText(), cost, Session.getUser().getId()));
             Notifications notification = Notifications.create()
            .title("Reservation Form")
            .text("Your Reservation has been submitted.")
            .darkStyle()
            .hideAfter(javafx.util.Duration.seconds(5)); 
            notification.showInformation();
            showReservations();
            clearForm();
        }
    }
    private Reservation updating;

    public void setUpdating(Reservation updating) {
        this.updating = updating;
        comment.setText(updating.getComment());
        type.setValue(updating.getType());
        price.setText(String.valueOf(updating.getPrice()));
        date_res.setValue(updating.getDate_res().toLocalDate());
        submitBtn.setOnAction(e -> update());
        submitBtn.setText("Update");
        title.setText("Updating reservation");

    }
    private void update(){
        List<String> errors = new ArrayList<String>();
        double cost = 0;
        if(type.getValue() == null){
            errors.add("Select a Service Type.");
        }
        if(comment.getText().isEmpty()){
            errors.add("Description can not be empty.");
        }
        try{
             cost = Double.valueOf(price.getText());
         }catch(Exception e){
             errors.add("Service cost must only contain numbers.");
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
            updating.setComment(comment.getText());
            updating.setType(type.getValue());
            updating.setDate_res(Date.valueOf(date_res.getValue()));
            updating.setPrice(cost);
            new ReservationService().update(updating);
             Notifications notification = Notifications.create()
            .title("Reservation Form")
            .text("Your Reservation has been updated.")
            .darkStyle()
            .hideAfter(javafx.util.Duration.seconds(5)); 
            notification.showInformation();
            showReservations();            
            clearForm();
        }
    }
    private void clearForm(){
        comment.clear();
        type.getSelectionModel().clearSelection();
        price.clear();
        date_res.setValue(LocalDate.now());
        submitBtn.setText("Save");
        submitBtn.setOnAction(e -> save());
        title.setText("Create new Reservation");
    }
    private void showReservations(){
        reservations.setAll(new ReservationService().findAll().values().stream().collect(Collectors.toList()));
        List<Reservation> rs = reservations.stream().collect(Collectors.toList());
        if(visibilityFilter.getValue() != null){
             switch(visibilityFilter.getValue()){
                    case "Mine": {
                        rs.clear();
                        rs.addAll(reservations.stream().filter( r ->    r.getUser_id() ==  Test.Session.getUser().getId()   ).collect(Collectors.toList()));
                        break;
                    }
                    case "Others":{
                        rs.clear();
                        rs.addAll(reservations.stream().filter( r ->  r.getUser_id() != Test.Session.getUser().getId() ).collect(Collectors.toList()));
                        break;

                    }
                    default: {
                        rs.clear();
                        rs.addAll(reservations.stream().collect(Collectors.toList()));
                    }
                }
        }
       if(!searchField.getText().isEmpty()){
           List<Reservation> filtered = rs.stream().collect(Collectors.toList());
           rs.clear();
           String searchable = searchField.getText().toLowerCase();
           rs.addAll(filtered.stream().filter( res -> res.getComment().toLowerCase().contains(searchable) || String.valueOf(res.getDate_res()).contains(searchable) || res.getType().toLowerCase().contains(searchable) || String.valueOf(res.getPrice()).contains(searchable)).collect(Collectors.toList()));
           System.out.println(rs);
       }
        if(rs.isEmpty()){
            Label msg = new Label("No records were found.");
            msg.setTextAlignment(TextAlignment.CENTER);
            VBox BOX = new VBox();
            BOX.setAlignment(Pos.CENTER);
            BOX.getChildren().setAll(msg);
            listSection.getChildren().setAll(BOX);
        }else {
            VBox pageContent = new VBox();
            pageContent.setAlignment(Pos.TOP_CENTER);
            pageContent.setPadding(new Insets(10));
            pageContent.setSpacing(10);
            Pagination pagination = new Pagination();
            pagination.getStyleClass().setAll("pagination");
            pagination.setPageFactory(pageIndex -> {
            pageContent.getChildren().clear();
            int pageCount = calculatePageCount();
            pagination.setPageCount(pageCount);
            int fromIndex = pageIndex * itemsPerPage;
            int toIndex = Math.min(fromIndex + itemsPerPage, rs.size());
            List<Reservation> pageReservations = rs.subList(fromIndex, toIndex);
            ScrollPane sp = new ScrollPane();
            Platform.runLater(() -> {
                pageContent.maxWidthProperty().bind(sp.widthProperty());
                pageContent.prefWidthProperty().bind(sp.widthProperty());
                pageContent.minWidthProperty().bind(sp.widthProperty());
            });
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setHmax(0);
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            for (Reservation reservation : pageReservations) {
                 try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/FrontOffice/Reservation/Reservation.fxml"));
                     FlowPane root =  loader.load();
                     ReservationController cn = loader.getController();
                     cn.setData(reservation,this);
                     Platform.runLater(() -> {
                        root.maxWidthProperty().bind(pageContent.maxWidthProperty().add(-70));
                        root.prefWidthProperty().bind(pageContent.maxWidthProperty().add(-70));
                        root.minWidthProperty().bind(pageContent.maxWidthProperty().add(-70));
                        root.minHeightProperty().bind(root.prefHeightProperty());
                     });
                    pageContent.getChildren().add(root);

                }catch(Exception e){
                }
            }

            sp.setContent(pageContent);

            sp.setPannable(true);
            sp.setFitToHeight(true);
            sp.setFitToWidth(true);
            return sp;
            });
            listSection.getChildren().setAll(pagination);
        }
        
    }
     
     private int calculatePageCount() {
        if (reservations.isEmpty()) {
            return 1; 
        }

        int pageCount = (int) Math.ceil((double) reservations.size() / itemsPerPage);
        return Math.max(1, pageCount); 
    }
     
    @FXML
    private void visibilityFilter(ActionEvent event) {
    }
}
