/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestion;

import ar.com.gestion.entities.Producto;
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.repositories.ProductoJpaController;
import ar.com.gestion.repositories.ProveedorJpaController;
import ar.com.gestion.utils.FxCbo;

import ar.com.gestion.utils.FxTable;
import ar.com.gestion.utils.Validator;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author walter
 */
public class StageStockController implements Initializable,InterfacePantallas {
    
    private Gestion mainApp;

    @FXML    private TextField txtDescripcion;
    @FXML    private TextField txtCantidad;
    @FXML    private TextField txtpunitario;
    @FXML    private TextField txtCodigo;
    @FXML    private TextField txtBuscar;
    
    @FXML    private Button cmdAgregar;
    @FXML    private Button cmdModificar;
    @FXML    private Button cmdEliminar;
    @FXML    private Button cmdNuevo;
    
    @FXML    private AnchorPane contenedorPrimario;

    @FXML    private TableView<?> tblProductos;
    @FXML    private ComboBox<?> cboProveedores;

    List<Proveedor> listProveedor;
    List<Producto> listDetalle;
    private Label lblInfo;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargar();
    }
    
    public void cargar(){
        iniciarCombos();
    }
    
    public void iniciarCombos(){
        
        ProveedorJpaController proveedorJPA = new ProveedorJpaController();
        
        listProveedor = proveedorJPA.getIdAndDesc();
        
        FxCbo provee = new FxCbo();
        
        provee.cargar(listProveedor, cboProveedores);
        
    }
    
    public void cargarTabla() {
        
        listDetalle = new ProductoJpaController().getAll();
        
        new FxTable().cargar(listDetalle,tblProductos);
        tblProductos.scrollTo(listDetalle.size());
        
    }
    
    @FXML
    private void agregar(ActionEvent event) throws Exception {
        if(validar()){
            Proveedor pro = new Proveedor();
            Producto p = new Producto(
                    Integer.valueOf(txtCodigo.getText()),
                    txtDescripcion.getText(),
                    Double.valueOf(txtCantidad.getText()),
                    Double.valueOf(txtpunitario.getText()),
                    Integer.valueOf(cboProveedores.getId())
            );
            new ProductoJpaController().save(p);
            //lblInfo.setText("Se dio de alta alumno id "+p.getProdId());
            limpiar();
            
        }else{
            lblInfo.setText(" Datos incorrectos ! ");
        }
        cargar();
    }
    
    private void limpiar(){
        txtDescripcion.setText("");
        txtCantidad.setText("0.00");
        txtpunitario.setText("0.00");
        
    }
    
    private boolean validar(){
        if(!new Validator(txtDescripcion).length(2, 20)) return false;
        if(!new Validator(txtCantidad).isInteger(1, 100000)) return false;
        //if(!new Validator(txtpunitario).isInteger(0.00, 90)) return false;
        if(!new Validator(txtBuscar).length(2, 200)) return false;
        return true;
    }

    @FXML
    private void modificar(ActionEvent event) {
//         List<Producto> lista = new ProductoJpaController().getByDescripcion(txtBuscar.getText());
//            if(lista!=null && lista.size()>0){
//                new FxTable().cargar(lista, tblProductos);
//                new FxTable().removeCol(tblProductos,0);
//                } 
    }
    
    @FXML
    private void borrar(ActionEvent event) {
        if(JOptionPane.showConfirmDialog(null,"Desea eliminar un producto ? ","pregunta",0)==0){
            new ProductoJpaController().remove((Producto) tblProductos.getSelectionModel().getSelectedItem());
            cargar();
            }
    }
    
    @FXML
    private void nuevo(ActionEvent event){
        cmdNuevo.setDisable(true);
        cmdAgregar.setDisable(false);
        
    }
    
    private void habilitar(){
        txtDescripcion.setDisable(false);
        txtpunitario.setDisable(false);
        txtCantidad.setDisable(false);
        txtDescripcion.requestFocus();
        cboProveedores.setDisable(false);
    }
    
    private void desabilitar(){
        txtDescripcion.setDisable(true);
        txtpunitario.setDisable(true);
        txtCantidad.setDisable(true);
        cmdNuevo.requestFocus();
        cboProveedores.setDisable(true);
    }
    
    @Override
    public void setMainApp(Gestion mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void buscarProducto(KeyEvent event) {
        List<Producto> lista = new ProductoJpaController().getByFiltro(txtBuscar.getText());
        if (lista != null && lista.size() > 0) {
            new FxTable().cargar(lista,tblProductos);
        }
    }

    @FXML
    private void buscarProveedor(KeyEvent event) {
        List<Proveedor> lista = new ProveedorJpaController().getLikeRazonProvee(cboProveedores.getValue().toString());
        if (lista != null && lista.size() > 0) {
            //cboProveedores.getSelectionModel().;
            //new FxCbo().cargar(lista,cboProveedores);
            
        }
    }

    
    
}
