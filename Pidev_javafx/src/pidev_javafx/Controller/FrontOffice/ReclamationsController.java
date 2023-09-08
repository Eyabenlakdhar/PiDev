/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Controller.FrontOffice;

import java.io.IOException;
import java.net.URL;
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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pidev_javafx.Controller.SignupController;
import pidev_javafx.Entity.Reclamation;
import pidev_javafx.Main.Test;
import pidev_javafx.Service.ReclamationService;
import pidev_javafx.Utils.Session;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class ReclamationsController implements Initializable{
    public Window window ;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private FlowPane items;
    @FXML
    private TextField searchReclamation;
    @FXML
    private Button reclamations_Button;
    
  

    @FXML
    private void newForm() {
        try{
            Popup popup = new Popup();
            popup.setHeight(600);
            popup.setWidth(600);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/FrontOffice/ReclamationForm.fxml"));
            AnchorPane root = loader.load();
            ReclamationFormController controller = loader.getController();
            controller.setData(this,popup,"save",null);
            FlowPane popupContent = new FlowPane();
            popupContent.setPrefSize(600, 600);
            popupContent.setAlignment(Pos.CENTER);
            popupContent.getChildren().setAll(root);
            popupContent.setStyle("-fx-background-color: white; -fx-padding: 10px;-fx-background-radius:24;-fx-border-radius:24;-fx-border-color:rgba(0,0,0,0.8);-fx-border-width:5;");
            popup.getContent().addAll(popupContent);
            popup.setAutoHide(true); 
            window = items.getScene().getWindow();
            popup.show(window);
        }catch(IOException e){
            
        }
       
    }
    public void updateForm(Reclamation reclamation){
        try{
            Popup popup = new Popup();
            popup.setHeight(600);
            popup.setWidth(600);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/FrontOffice/ReclamationForm.fxml"));
            AnchorPane root = loader.load();
            ReclamationFormController controller = loader.getController();
            controller.setData(this,popup,"update",reclamation);
            FlowPane popupContent = new FlowPane();
            popupContent.setPrefSize(600, 600);
            popupContent.setAlignment(Pos.CENTER);
            popupContent.getChildren().setAll(root);
            popupContent.setStyle("-fx-background-color: white; -fx-padding: 10px;-fx-background-radius:24;-fx-border-radius:24;-fx-border-color:rgba(0,0,0,0.8);-fx-border-width:5;");
            popup.getContent().addAll(popupContent);
            popup.setAutoHide(true); 
            window = items.getScene().getWindow();
            popup.show(window);
        }catch(IOException e){
            
        }
    }
    ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        searchReclamation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(() -> {
                    reclamationView(searchReclamation.getText());
                });
            }
        });
        items.getChildren().clear();
        ReclamationsController p = this;
        reclamations.addListener(new ListChangeListener<Reclamation>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Reclamation> c) {
                if(c.next()){
                    items.getChildren().clear();
                    scrollPane.setHvalue(0);
                    items.setAlignment(Pos.TOP_CENTER);
                    if(c.wasAdded()){
                        c.getAddedSubList().forEach(reclamation -> {
                            try{
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/pidev_javafx/Resources/FrontOffice/Reclamation.fxml"));
                                AnchorPane root = loader.load();
                                ReclamationController cc = loader.getController();
                                cc.setData(reclamation,p);
                                items.getChildren().add(root);
                            }catch(Exception e){

                            }
                        });
                    }
                    if(reclamations.isEmpty()){
                        if(Test.getModes().get(Test.getModes().size()-1)){
                            Label emptyReclamation = new Label("No records were found Mr Admin.");
                            items.getChildren().setAll(emptyReclamation);
                            items.setAlignment(Pos.CENTER);

                        }else {
                            Label emptyReclamation = new Label("No records were found click here to add a new Reclamation.");
                            emptyReclamation.setOnMouseClicked(e -> newForm());
                            items.getChildren().setAll(emptyReclamation);
                            items.setAlignment(Pos.CENTER);
                        }
                        
                    }  
              
                }
            }
        });
        reclamationView("");
        Test.getModes().addListener(new ListChangeListener<Boolean>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Boolean> c) {
                    if(c.next()){
                         if(c.wasAdded()){
                        if(c.getAddedSubList().get(0)){
                            reclamations_Button.setText("Statistics");
                            reclamations_Button.setOnAction( e -> statPop());
                            adminReclamationView("");
                        }else {
                            reclamations_Button.setText("New Reclamation");
                            reclamations_Button.setOnAction( e -> newForm());
                            reclamationView("");
                        }
                    }
                }
            }
        });
    }
    private void statPop(){
        long trueCount = reclamations.stream().filter(reclamation -> reclamation.getEtat()).count();
        long falseCount = reclamations.stream().filter(reclamation -> !reclamation.getEtat()).count();
        Popup popup = new Popup();
        popup.setHeight(600);
        popup.setWidth(600);
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Reclamations Status");
        xAxis.setLabel("Status");
        yAxis.setLabel("Count");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Open / Not Responded", trueCount));
        series.getData().add(new XYChart.Data<>("Closed / Responded", falseCount));
        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList(series);
        barChart.setData(seriesList);
        FlowPane pane = new FlowPane();
        barChart.setPrefSize(600, 600);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color:white;    -fx-border-width: 2px;-fx-border-color: transparent;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 5, 0, 0, 2);");
        pane.getChildren().setAll(barChart);
        popup.getContent().add(pane);
        popup.setAutoHide(true);
        popup.show(reclamations_Button.getScene().getWindow());
    }
    public void reclamationView(String criteria){
        if(Test.getModes().get(Test.getModes().size()-1)){
            adminReclamationView(criteria);
        }
        else {
             reclamations.setAll((new ReclamationService().findAll().values().stream().filter(rc -> rc.getUser_id().equals(Session.getUser().getId())).collect(Collectors.toList())).stream().filter(rec -> String.valueOf(rec.getDate_rec()).contains(criteria.toLowerCase()) || rec.getRec_subject().toLowerCase().contains(criteria.toLowerCase()) || rec.getRec_text().toLowerCase().contains(criteria.toLowerCase())).collect(Collectors.toList()));
            if(reclamations.isEmpty()){
               Label emptyReclamation = new Label("No records were found click here to add a new Reclamation.");
               emptyReclamation.setOnMouseClicked(e -> newForm());
               items.getChildren().setAll(emptyReclamation);
               items.setAlignment(Pos.CENTER);
           }
        }
       
    }
    public void adminReclamationView(String criteria){
         reclamations.setAll((new ReclamationService().findAll().values().stream().filter(rc -> !rc.getUser_id().equals(Session.getUser().getId())).collect(Collectors.toList())).stream().filter(rec -> String.valueOf(rec.getDate_rec()).contains(criteria.toLowerCase()) || rec.getRec_subject().toLowerCase().contains(criteria.toLowerCase()) || rec.getRec_text().toLowerCase().contains(criteria.toLowerCase())).collect(Collectors.toList()));
         if(reclamations.isEmpty()){
            Label emptyReclamation = new Label("No records were found Mr admin.");
            items.setAlignment(Pos.CENTER);

        }
    }
    
}
