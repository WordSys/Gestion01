/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author walter
 */
public class MarcoController implements Initializable,InterfacePantallas {
    
    private Gestion mainApp;
    @FXML
    private Menu Inicio;
    @FXML
    private MenuItem Login;
    @FXML
    private MenuItem Salir;
    @FXML
    private Menu Gestion;
    @FXML
    private MenuItem stock;
    @FXML
    private MenuItem clientes;
    @FXML
    private MenuItem proveedores;
    @FXML
    private MenuItem empleados;
    @FXML
    private Menu Ayuda;
    @FXML
    private AnchorPane contenedorPrimario;
    @FXML
    private MenuBar menuBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void salir(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void iniciarStock(ActionEvent event) {
        mainApp.cargarVentana(mainApp.ventana2);
    }

    @FXML
    private void iniciarClientes(ActionEvent event) {
        mainApp.cargarVentana(mainApp.ventana3);
    }

    @FXML
    private void iniciraProveedores(ActionEvent event) {
        mainApp.cargarVentana(mainApp.ventana5);
    }

    @FXML
    private void iniciarEmpleados(ActionEvent event) {
        mainApp.cargarVentana(mainApp.ventana4);
    }
    @Override
    public void setMainApp(Gestion mainApp){
        this.mainApp = mainApp;
    }
       
        
}
