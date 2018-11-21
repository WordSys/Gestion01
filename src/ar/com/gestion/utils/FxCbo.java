package ar.com.gestion.utils;

import java.util.List;
import javafx.scene.control.ComboBox;
import static javafx.scene.input.KeyCode.T;

public class FxCbo<E> {
    
    public void removeItems(ComboBox cbo){
        //List lista = cbo.getItems();
        //cbo.getItems().removeAll(lista);
        cbo.getItems().clear();
    }
    public void cargar(List<E> lista, ComboBox cbo){
        cbo.getItems().clear();
        cbo.setPromptText("Elija uno --");
//        if(lista != null && lista.size() > 0){
//            int cantidad = lista.size();
//            String nombreItem;
//            Field[] item = obj.getClass().getDeclaredFields();
//            for (int a = 0; a < cantidad; a++){
//                
//            }
//        }
        cbo.getItems().addAll(lista);    //agrega el elemento
        
    }
    public void posicion(String cadena,ComboBox cbo){
        if (cadena == null){
            cbo.getSelectionModel().select(-1);
            cbo.selectionModelProperty().set(null);
        }
        List lista = cbo.getItems();
        final int itemCount = lista.size();

        for (int i = 0; i < itemCount; i++) {
            String value = cbo.getItems().get(i).toString();
            if (value != null && value.equals(cadena)) {
                cbo.getSelectionModel().select(i);
                return;
            }
        }
    }
}
