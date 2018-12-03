
package gestion;

import ar.com.gestion.entities.Condtrib;
import ar.com.gestion.entities.DetalleProveedor;
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.entities.Libretacontacto;
import ar.com.gestion.entities.Localidad;
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.entities.Provincia;
import ar.com.gestion.entities.Tcontacto;
import ar.com.gestion.entities.Tdoc;
import ar.com.gestion.repositories.CondtribJpaController;
import ar.com.gestion.repositories.DireccionJpaController;
import ar.com.gestion.repositories.LibretacontactoJpaController;
import ar.com.gestion.repositories.LocalidadJpaController;
import ar.com.gestion.repositories.ProveedorJpaController;
import ar.com.gestion.repositories.ProvinciaJpaController;
import ar.com.gestion.repositories.TcontactoJpaController;
import ar.com.gestion.repositories.TdocJpaController;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.utils.FxCbo;
import ar.com.gestion.utils.FxTable;
import ar.com.gestion.utils.Validator;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;


/**
 * FXML Controller class
 *
 * @author walter
 */
public class ProveedoresController implements Initializable {

    @FXML    private AnchorPane contenedorPrimario;
    @FXML    private Pane direPane;
    @FXML    private Pane proveePane;
    @FXML    private Pane libPane;
        
    @FXML    private TextField txtIdProvee;
    @FXML    private TextField txtRazon;
    @FXML    private TextField txtNroDoc;
    @FXML    private TextField txtIdDire;
    @FXML    private TextField txtCalle;
    @FXML    private TextField txtNro;
    @FXML    private TextField txtCruce1;
    @FXML    private TextField txtCruce2;
    @FXML    private TextField txtPiso;
    @FXML    private TextField txtDpto;
    @FXML    private TextField txtCodPos;
    @FXML    private TextField txtContacto;
    @FXML    private TextField txtBuscar;
    
    @FXML    private ComboBox<?> cboTipoDoc;
    @FXML    private ComboBox<?> cboCondTrib;
    @FXML    private ComboBox<?> cboTipoContacto;
    @FXML    private ComboBox<?> cboLocalidad;
    @FXML    private ComboBox<?> cboProvincia;
    
    List<Tdoc> listTdoc;
    List<Condtrib> listCondtrib;
    List<Tcontacto> listTcontacto;
    List<Provincia> listProvincia;
    List<Localidad> listLocalidad;
    
    @FXML    private Button cmdNuevo;
    @FXML    private Button cmdCancelar;
    @FXML    private Button cmdModificar;
    @FXML    private Button cmdGenerar;
    @FXML    private Button cmdGuardar;
    @FXML    private Button cmdAgregar;
    @FXML    private Button cmdModContacto;
    
    @FXML    private TableView<DetalleProveedor> tblProveedores;
    private  TableColumn codigo;
    private  TableColumn razon;
    private  TableColumn tipoDoc;
    private  TableColumn nDoc;
    private  TableColumn condtribtc;
    private  TableColumn calle;
    private  TableColumn nro;
    private  TableColumn calle1;
    private  TableColumn calle2;
    private  TableColumn piso;
    private  TableColumn dpto;
    private  TableColumn localidadtc;
    private  TableColumn cp;
    private  TableColumn provinciatc;
    List<Proveedor> listProveedor;
    List<DetalleProveedor> listDetalle;
    ObservableList<DetalleProveedor> ObListDetalle;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarTabla();
        
    }
        
    public void iniciarCombos(){
        TdocJpaController tDocJPA = new TdocJpaController();
        CondtribJpaController condTribJPA = new CondtribJpaController();
        TcontactoJpaController tContactoJPA = new TcontactoJpaController();
        ProvinciaJpaController provinciaJPA = new ProvinciaJpaController();
        
        listTdoc = tDocJPA.getAll();
        listCondtrib = condTribJPA.getAll();
        listTcontacto = tContactoJPA.getAll();
        listProvincia = provinciaJPA.getAll();
        
        FxCbo tdoc = new FxCbo();
        FxCbo condtrib = new FxCbo();
        FxCbo tcontacto = new FxCbo();
        FxCbo provincia = new FxCbo();
        
        tdoc.cargar(listTdoc, cboTipoDoc);
        condtrib.cargar(listCondtrib, cboCondTrib);
        tcontacto.cargar(listTcontacto, cboTipoContacto);
        provincia.cargar(listProvincia, cboProvincia);
        
    }
    
    public void limpiarCombos(){
        
        cboProvincia.getItems().clear();
        cboLocalidad.getItems().clear();
        cboTipoDoc.getItems().clear();
        cboCondTrib.getItems().clear();
        cboTipoContacto.getItems().clear();
    }
    
    public void limpiarTextos(){
        txtIdProvee.setText("");
        txtRazon.setText("");
        txtNroDoc.setText("");
        txtCalle.setText("");
        txtNro.setText("");
        txtCruce1.setText("");
        txtCruce2.setText("");
        txtPiso.setText("");
        txtDpto.setText("");
        txtCodPos.setText("");
    }

    public void cargarTabla() {
        
        listDetalle = new ProveedorJpaController().getDetalleProveedor();
        
        new FxTable().cargar(listDetalle,tblProveedores);
        tblProveedores.scrollTo(listDetalle.size());
        
    }
    
    private void setupTableColumn(TableView tabla){
        codigo      = new TableColumn("Código");
        codigo.setCellFactory(new PropertyValueFactory("codigo"));
        razon       = new TableColumn("Razon");
        razon.setCellFactory(new PropertyValueFactory("razon"));
        tipoDoc     = new TableColumn("Tipo Doc");
        tipoDoc.setCellFactory(new PropertyValueFactory("tipoDoc"));
        nDoc        = new TableColumn("Nro Doc");
        nDoc.setCellFactory(new PropertyValueFactory("nDoc"));
        condtribtc  = new TableColumn("Cond Trib");
        condtribtc.setCellFactory(new PropertyValueFactory("condtribtc"));
        calle       = new TableColumn("Calle");
        calle.setCellFactory(new PropertyValueFactory("calle"));
        nro         = new TableColumn("Nro");
        nro.setCellFactory(new PropertyValueFactory("nro"));
        calle1      = new TableColumn("Calle 1");
        calle1.setCellFactory(new PropertyValueFactory("calle1"));
        calle2      = new TableColumn("Calle 2");
        calle2.setCellFactory(new PropertyValueFactory("calle2"));
        piso        = new TableColumn("Piso");
        piso.setCellFactory(new PropertyValueFactory("piso"));
        dpto        = new TableColumn("Dpto");
        dpto.setCellFactory(new PropertyValueFactory("dpto"));
        cp          = new TableColumn("CP");
        cp.setCellFactory(new PropertyValueFactory("cp"));
        localidadtc = new TableColumn("Localidad");
        localidadtc.setCellFactory(new PropertyValueFactory("localidadtc"));        
        provinciatc = new TableColumn("Provincia");
        provinciatc.setCellFactory(new PropertyValueFactory("provinciatc"));
        
        tabla.getColumns().addAll(codigo,razon,tipoDoc,nDoc,condtribtc,calle,nro,calle1,calle2,piso,dpto,cp,localidadtc,provinciatc);
    }
    
    private ScrollBar findScrollBar(TableView<?> table, Orientation orientation) {

        Set<Node> set = table.lookupAll(".scroll-bar");
        for( Node node: set) {
            ScrollBar bar = (ScrollBar) node;
            if( bar.getOrientation() == orientation) {
                return bar;
            }
        }
        return null;
    }
    
    private void cargarLocalidades() {

        try {
            String prov = cboProvincia.getSelectionModel().getSelectedItem().toString();

            List<Localidad> lista = new ArrayList();
            List<String> listaString = new ArrayList();
            LocalidadJpaController Loc = new LocalidadJpaController();

            cboLocalidad.getItems().removeAll(lista);
            txtCodPos.setText("");

            int provId = new ProvinciaJpaController().getByIdOfProv(prov);

            lista = new LocalidadJpaController().getByLocaIdOfProv(provId);
            if (lista != null && lista.size() > 0) { // si la lista no esta vacia y es mayor a 0
                int cantidad = lista.size();
                String nombre;
                for (int a = 0; a < cantidad; a++) {    //del primer campo hasta el ultimo
                    nombre = lista.get(a).toString();
                    if (nombre.isEmpty()) {
                    } else {
                        listaString.add(nombre);
                    }
                }
            }
            new FxCbo().cargar(listaString, cboLocalidad);
        } catch (Exception e) {
        }

    }
    /**
     * 
     * @param event 
     */
    @FXML
    private void cargarLista(ActionEvent event) {
        
        cargarLocalidades();
        
    }
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void setCodigoPostal(Event event) {
        String value = txtCodPos.getText();
        if (value.isEmpty()) {
            try {
                String prov = cboProvincia.getSelectionModel().getSelectedItem().toString();
                int provId = new ProvinciaJpaController().getByIdOfProv(prov);
                String nombre = cboLocalidad.getSelectionModel().getSelectedItem().toString();
                int codigo = new LocalidadJpaController().getByCodigoPostal(nombre, provId);
                String codPos = String.valueOf(codigo);
                txtCodPos.setText("");
                txtCodPos.setText(codPos);
            } catch (Exception e) {
            }
        }

    }

    @FXML
    private void buscar(KeyEvent event) {
        List<DetalleProveedor> lista = new ProveedorJpaController().getLikeRazon(txtBuscar.getText());
        if (lista != null && lista.size() > 0) {
            new FxTable().cargar(lista,tblProveedores);
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        if (validar()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Dialogo de confirmación");
            alert.setHeaderText("Modificar los datos.");
            alert.setContentText("Esta seguro que desea modificar ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                DetalleProveedor detalle = tblProveedores.getSelectionModel().getSelectedItem();
                int proId = detalle.getCodigo();
                int dirId = new DireccionJpaController().getDirIdByProve(proId);
                String razon = txtRazon.getText();
                int tdoc = new TdocJpaController().getByIdOfName(cboTipoDoc.getValue().toString());
                long ndoc = Long.valueOf(txtNroDoc.getText());
                int condtrib = new CondtribJpaController().getByIdOfName(cboCondTrib.getValue().toString());
                String calle = txtCalle.getText();
                int nro = Integer.parseInt(txtNro.getText());
                String cruce1 = txtCruce1.getText();
                String cruce2 = txtCruce2.getText();
                int piso = piso();
                String dpto = dpto();
                short cp = Short.parseShort(txtCodPos.getText());
                int loc = new LocalidadJpaController().getIdLocalidad(cboLocalidad.getValue().toString());
                short pcia = new ProvinciaJpaController().getByIdOfProv(cboProvincia.getValue().toString());

                Direccion direccion = new Direccion(
                        calle, nro, cruce1, cruce2, piso, dpto, cp, loc, pcia
                );
                new DireccionJpaController().update(direccion, dirId);

                Proveedor proveedor = new Proveedor(
                        razon, tdoc, ndoc, condtrib, dirId
                );
                new ProveedorJpaController().update(proveedor, proId);
                cargarTabla();
                tblProveedores.requestFocus();
                tblProveedores.getSelectionModel().select(proId - 1);

            } else {
                limpiarCombos();
                limpiarTextos();
                cargarTabla();
                desHabilitar();
                cmdModificar.setDisable(true);
                cmdCancelar.opacityProperty().set(0);
                cmdCancelar.setDisable(true);
                cmdNuevo.opacityProperty().set(1);
                cmdNuevo.setDisable(false);
            }
            limpiarCombos();
            limpiarTextos();
            cargarTabla();
            desHabilitar();
            cmdModificar.setDisable(true);
            cmdCancelar.opacityProperty().set(0);
            cmdCancelar.setDisable(true);
            cmdNuevo.opacityProperty().set(1);
            cmdNuevo.setDisable(false);
        }

    }

    private void generarProvedor() throws IllegalOrphanException {
        if (validar()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Dialogo de confirmación");
            alert.setHeaderText("Guardar los datos.");
            alert.setContentText("Esta seguro que desea guardar los datos ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                String razon = txtRazon.getText();
                int tdoc = new TdocJpaController().getByIdOfName(cboTipoDoc.getValue().toString());
                long ndoc = Long.valueOf(txtNroDoc.getText());
                int condtrib = new CondtribJpaController().getByIdOfName(cboCondTrib.getValue().toString());
                String calle = txtCalle.getText();
                int nro = Integer.parseInt(txtNro.getText());
                String cruce1 = txtCruce1.getText();
                String cruce2 = txtCruce2.getText();
                int piso = piso();
                String dpto = dpto();
                short cp = Short.parseShort(txtCodPos.getText());
                int loc = new LocalidadJpaController().getIdLocalidad(cboLocalidad.getValue().toString());
                short pcia = new ProvinciaJpaController().getByIdOfProv(cboProvincia.getValue().toString());
                Direccion direccion = new Direccion(
                        calle, nro, cruce1, cruce2, piso, dpto, cp, loc, pcia
                );
                new DireccionJpaController().save(direccion);
                int dirId = direccion.getDirId();
                txtIdDire.setText(String.valueOf(dirId));
                Proveedor proveedor = new Proveedor(
                        razon, tdoc, ndoc, condtrib, dirId
                );
                new ProveedorJpaController().save(proveedor);
                txtIdProvee.setText(String.valueOf(proveedor.getProveId()));
            } else {
                limpiarTextos();
                limpiarCombos();
                desHabilitar();
                
            }
        }

    }

    private void agregarContacto() {
        if (!txtIdDire.getText().isEmpty()) {
            int codigoActual = new LibretacontactoJpaController().getUltimoId();
            int codigoNuevo = codigoActual++;
            int tcontactoId = new TcontactoJpaController().getByIdOfName(cboTipoContacto.getValue().toString());
            int dirId = Integer.parseInt(txtIdDire.getText());
            String libContacto = txtContacto.getText();
            Libretacontacto contacto = new Libretacontacto(
                    codigoNuevo, tcontactoId, dirId, libContacto
            );
        }
    }

    @FXML
    private void generar(ActionEvent event) {
        try {
            generarProvedor();

            limpiarCombos();
            limpiarTextos();
            cargarTabla();
            desHabilitar();
            cmdCancelar.opacityProperty().set(0);
            cmdCancelar.setDisable(true);
            cmdNuevo.opacityProperty().set(1);
            cmdNuevo.setDisable(false);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ProveedoresController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int piso() {
        if (!txtPiso.getText().isEmpty()) {
            int piso = Integer.parseInt(txtPiso.getText());
            return piso;
        } else {
            return 0;
        }
    }

    public String dpto() {
        if (!txtDpto.getText().isEmpty()) {
            String dpto = txtPiso.getText();
            return dpto;
        } else {
            return null;
        }
    }

    public void habilitar(){
        txtRazon.setDisable(false);
        cboTipoDoc.setDisable(false);
        txtNroDoc.setDisable(false);
        cboCondTrib.setDisable(false);
        txtCalle.setDisable(false);
        txtNro.setDisable(false);
        txtCruce1.setDisable(false);
        txtCruce2.setDisable(false);
        txtPiso.setDisable(false);
        txtDpto.setDisable(false);
        cboProvincia.setDisable(false);
        cboLocalidad.setDisable(false);
        habilitarContacto();
    }
    
    public void desHabilitar(){
        txtRazon.setDisable(true);
        cboTipoDoc.setDisable(true);
        txtNroDoc.setDisable(true);
        cboCondTrib.setDisable(true);
        txtCalle.setDisable(true);
        txtNro.setDisable(true);
        txtCruce1.setDisable(true);
        txtCruce2.setDisable(true);
        txtPiso.setDisable(true);
        txtDpto.setDisable(true);
        cboProvincia.setDisable(true);
        cboLocalidad.setDisable(true);
        deshabilitarContacto();
    }

    @FXML
    private void habilitarText(ActionEvent event) {
    }

    @FXML
    private void esNumDoc(KeyEvent event) {
    }

    @FXML
    private void nuevo(ActionEvent event) {
        iniciarCombos();
        habilitar();
        cmdCancelar.opacityProperty().set(1);
        cmdCancelar.setDisable(false);
        cmdNuevo.opacityProperty().set(0);
        cmdNuevo.setDisable(true);
        cmdGenerar.setDisable(false);
        txtRazon.requestFocus();
        
        int id = new ProveedorJpaController().getUltimoId();
        id++;
        txtIdProvee.setText(String.valueOf(id));
        
    }

    @FXML
    private void cancelar(ActionEvent event) {
        
        try {
            limpiarTextos();
            limpiarCombos();
            desHabilitar();
        } catch (Exception e) {
            System.out.println("Se lanzo una exception" + e);
        }
        
        cmdCancelar.opacityProperty().set(0);
        cmdCancelar.setDisable(true);
        cmdNuevo.opacityProperty().set(1);
        cmdNuevo.setDisable(false);
        cmdGenerar.setDisable(true);
        cmdModificar.setDisable(true);
    }

    @FXML
    private void guardar(ActionEvent event) {
    }

    @FXML
    private void sleccionar(MouseEvent event) {
        iniciarCombos();
        habilitar();
        
        cmdModificar.setDisable(false);
        cmdNuevo.setDisable(true);
        cmdNuevo.opacityProperty().set(0);
        cmdCancelar.setDisable(false);
        cmdCancelar.opacityProperty().set(1);
        
        DetalleProveedor detalle = tblProveedores.getSelectionModel().getSelectedItem();
        txtIdProvee.setText(String.valueOf(detalle.getCodigo()));
        txtRazon.setText(detalle.getRazon());
        cboTipoDoc.getSelectionModel().select(
                new TdocJpaController().getByIdOfName(detalle.getT_doc()) - 1
        );
        txtNroDoc.setText(String.valueOf(detalle.getN_doc()));
        cboCondTrib.getSelectionModel().select(
                new CondtribJpaController().getByIdOfName(detalle.getCondicion()) - 1
        );
        txtCalle.setText(String.valueOf(detalle.getCalle()));
        txtNro.setText(String.valueOf(detalle.getNro()));
        txtCruce1.setText(String.valueOf(detalle.getCalle1()));
        txtCruce2.setText(String.valueOf(detalle.getCalle2()));
        txtPiso.setText(String.valueOf(detalle.getPiso()));
        txtDpto.setText(String.valueOf(detalle.getDpto()));
        cboProvincia.getSelectionModel().select(
                new ProvinciaJpaController().getByIdOfProv(detalle.getProvincia()) - 1
        );
        txtCodPos.setText(String.valueOf(detalle.getCp()));
        new FxCbo().posicion(detalle.getLocalidad(), cboLocalidad);
    }
    
    private boolean validar(){
        String doc = cboTipoDoc.getValue().toString();
        switch(doc){
            case "CUIL":
                if(!new Validator(txtNroDoc).isCuilOrCuit(11)) return false;
                break;
            case "CUIT":
                if(!new Validator(txtNroDoc).isCuilOrCuit(11)) return false;
                break;
            case "DNI":
                if(!new Validator(txtNroDoc).isDni(8)) return false;
                break;
        }
        if(!new Validator(txtRazon).length(5, 200)) return false;
        if(!new Validator(txtCalle).length(1, 50)) return false;
        if(!new Validator(txtNro).unId(0, 99999)) return false;
        return true;
    }
    private void habilitarContacto(){
        cboTipoContacto.setDisable(false);
        txtContacto.setDisable(false);
        cmdAgregar.setDisable(false);
    }
    private void deshabilitarContacto(){
        cboTipoContacto.setDisable(true);
        txtContacto.setDisable(true);
        cmdAgregar.setDisable(true);
    }

    @FXML
    private void addContact(ActionEvent event) {
        DetalleProveedor detalle = tblProveedores.getSelectionModel().getSelectedItem();
        ProveedorJpaController proveedor = new ProveedorJpaController();
        LibretacontactoJpaController libContacto = new LibretacontactoJpaController();
        TcontactoJpaController tipoCont = new TcontactoJpaController();
        
        int dirId = proveedor.getDirIdByProveId(detalle.getCodigo());
        String valueContacto = cboTipoContacto.getValue().toString();
        int tconId = tipoCont.getByIdOfName(valueContacto);
        String contacto = txtContacto.getText();
        
        Libretacontacto lcontacto = new Libretacontacto(tconId,dirId,contacto);
        libContacto.save(lcontacto);
        
        cancelar(event);
        deshabilitarContacto();
        txtContacto.setText("");
        
    }
}
