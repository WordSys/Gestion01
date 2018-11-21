/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author walter
 */
public class Gestion extends Application {
    
    private Stage mainStage;
    private Group mainScene;
    
    public static String marco = "Marco.fxml";
    public static String ventana1 = "StagePrimary.fxml";
    public static String ventana2 = "StageStock.fxml";
    public static String ventana3 = "Clientes.fxml";
    public static String ventana4 = "Empleados.fxml";
    public static String ventana5 = "Proveedores.fxml";
    
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        this.mainStage = stage;
        this.mainScene = root;
        this.cargarVentana(ventana1);
        this.cargarVentana(marco);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        
        stage.show();
    }
    
    public Stage getMainStage(){
        return mainStage;
    }
    
    public boolean cargarVentana(String archivoFXML){
            try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(archivoFXML));
            AnchorPane screen = (AnchorPane) myLoader.load();
                if (marco.equals(archivoFXML) || mainScene.getChildren().isEmpty()) {
                    mainScene.getChildren().add(screen);
                }
                else{
                    mainScene.getChildren().remove(0);
                    mainScene.getChildren().add(0,screen);
                } 
            InterfacePantallas myScreenControler = ((InterfacePantallas) myLoader.getController());
            myScreenControler.setMainApp(this);
            //myScreenControler.setScreenParent(this);
            //addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
