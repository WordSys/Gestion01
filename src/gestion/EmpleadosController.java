/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author walter
 */
public class EmpleadosController implements Initializable {

    @FXML
    private AnchorPane contenedorPrimario;
    @FXML
    private Pane direPane;
    @FXML
    private TextField txtCalle;
    @FXML
    private TextField txtNro;
    @FXML
    private TextField txtCruce1;
    @FXML
    private TextField txtCruce2;
    @FXML
    private TextField txtPiso;
    @FXML
    private TextField txtDpto;
    @FXML
    private TextField txtCodPos;
    @FXML
    private ComboBox<?> cboLocalidad;
    @FXML
    private ComboBox<?> cboProvincia;
    @FXML
    private TextField txtIdDire;
    @FXML
    private Button cmdGenerar;
    @FXML
    private Button cmdModificar;
    @FXML
    private Pane libPane;
    @FXML
    private TextField txtContacto;
    @FXML
    private ComboBox<?> cboTipoContacto;
    @FXML
    private Button cmdAgregar;
    @FXML
    private Button cmdModContacto;
    @FXML
    private TableView<?> tblEmpleados;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button cmdBuscar;
    @FXML
    private Pane empPane;
    @FXML
    private TextField txtIdCiente;
    @FXML
    private TextField txtFechaAlta;
    @FXML
    private TextField txtRazon;
    @FXML
    private TextField txtRazon1;
    @FXML
    private TextField txtFechaAlta1;
    @FXML
    private TextField txtFechaAlta11;
    @FXML
    private TextField txtFechaAlta12;
    @FXML
    private TextField txtNroDoc;
    @FXML
    private ComboBox<?> cboTipoDoc;
    @FXML
    private Button cmdAlta;
    @FXML
    private Button cmdModificar1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
