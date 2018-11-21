package ar.com.gestion.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FxTable<E> {
    
    public static void removeCol(TableView tv, int index){
        tv.getColumns().remove(index);
    }

    public void cargar(List<E> lista,TableView tv) {
        tv.getColumns().clear(); //limpia las columnas
        tv.getItems().clear();  //limpia los items
        if(lista != null && lista.size() > 0){ // si la lista no esta vacia y es mayor a 0
            
            E obj = lista.get(0); //carga el objeta vacio
            Field[] campos = obj.getClass().getDeclaredFields(); //arreglo para la lista de campos
            //TableColumn[] campos = array;
            int cantidad = campos.length; // cuenta la cantidad de campos
            TableColumn col;    // variable col de tipo table column
            String nombreCol; // variable con el nombre de la columna
            for (int a = 0; a < cantidad; a++) {    //del primer campo hasta el ultimo
                nombreCol = campos[a].getName();    // guarda el nombre de columna
                //nombreCol = campos[a].toString();
                col = new TableColumn(nombreCol);   // agrega cada columna con su respectivo nombre
                tv.getColumns().add(col);
                col.setCellValueFactory(new PropertyValueFactory<>(nombreCol)); //le da el nombre
            }
            //removeCol(tv,0);
        }
        tv.getItems().addAll(lista);    //-agreg el elmento
        
    }
}
