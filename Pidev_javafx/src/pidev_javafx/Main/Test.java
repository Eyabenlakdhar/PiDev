/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pidev_javafx.Main;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pidev_javafx.Entity.User;
import pidev_javafx.Utils.Session;

/**
 *
 * @author MSI
 */
public class Test  extends Application  {

    public static Session Session;
    private  Stage primaryStage;
    private static ObservableList<Boolean> isAdminMode ;
    public static ObservableList<Boolean> getModes(){
        return isAdminMode;
    }
   
        @Override
    public void start(Stage primaryStage) {
        Session.getSession().addListener((ListChangeListener.Change<? extends User> c) -> {
            if(c.next()){
                if(c.wasAdded()){
                    System.out.println("change");
                    FrontOffice();
                }
                if(c.wasRemoved()){
                    AuthenticationView();
                }
            }
        });
        isAdminMode = FXCollections.observableArrayList(false);
        isAdminMode.addListener(new ListChangeListener<Boolean>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Boolean> c) {
                System.out.println(c);
            }
        });
         try{
            scene = new Scene(FXMLLoader.load(getClass().getResource("/pidev_javafx/Resources/Authentication.fxml")), 800, 600);
            primaryStage.setTitle("Pidev Septembre 2K23");
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(500);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(Exception e){
        }

    }
    private static Scene scene;
    public void AuthenticationView(){
             try {
            scene.setRoot(FXMLLoader.load(getClass().getResource("/pidev_javafx/Resources/Authentication.fxml")));
        } catch (IOException ex) {
        }

     
    }
     public void FrontOffice(){
        try {
            System.out.println("sign in");
            scene.setRoot(FXMLLoader.load(getClass().getResource("/pidev_javafx/Resources/FrontOffice/Base.fxml")));
        } catch (IOException ex) {
        }
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
