
package ar.com.gestion.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;

public class Validator {
    
    private TextField textField;

    public Validator(TextField textfield) {
        this.textField = textfield;
    }
    
    public boolean isInteger(int min,int max){
        try {
            int nro = Integer.parseInt(textField.getText());
            if (nro>=min && nro<=max) return true;
            else{
                JOptionPane.showMessageDialog(null,"Debe ingresar un numero entero "
                + " entre "+ min +" y " + max +" !!");
            textField.requestFocus();
            textField.selectAll();
            return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Debe ingresar un numero entero.");
            textField.requestFocus();
            textField.selectAll();
            return false;
        }
    }
    public boolean isCuilOrCuit(int numero){
        try {
            String nro = textField.getText();
            int cantDigitos = nro.length();
            if (cantDigitos == numero) return true;
            else{
                JOptionPane.showMessageDialog(null,"Debe ingresar un numero valido "
                + " debe tener : "+numero+" digitos!!");
            textField.requestFocus();
            textField.selectAll();
            return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Debe ingresar un numero entero.");
            textField.requestFocus();
            textField.selectAll();
            return false;
        }
    }
    public boolean isDni(int numero){
        try {
            String nro = textField.getText();
            int cantDigitos = nro.length();
            if (cantDigitos == numero) return true;
            else{
                JOptionPane.showMessageDialog(null,"Debe ingresar un numero valido "
                + " debe tener "+numero+" digitos!!");
            textField.requestFocus();
            textField.selectAll();
            return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Debe ingresar un numero entero.");
            textField.requestFocus();
            textField.selectAll();
            return false;
        }
    }
    public boolean length (int min, int max){
        int length = textField.getText().length();
        if(length >= min && length <= max) return true;
        else{
            JOptionPane.showMessageDialog(null,"Debe ingresar textro entre "
            + min + " y " + max + " caracteres.");
            textField.requestFocus();
            textField.selectAll();
            return false;
        }
        
    }
    public boolean unId(int min,int max){
        try {
            int nro = Integer.parseInt(textField.getText());
            if (nro>=min && nro<=max) return true;
            else{
                JOptionPane.showMessageDialog(null,"Debe ingresar un código "
                + " entre "+ min +" y " + max +" !!");
            textField.requestFocus();
            textField.selectAll();
            return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Debe ingresar un numero entero.");
            textField.requestFocus();
            textField.selectAll();
            return false;
        }
        
    }
    
    public boolean esFecha(){
        
        String miFecha = textField.getText();
        Date fecha;
        SimpleDateFormat miFormato = new SimpleDateFormat("dd/mm/yyyy");
        
        try {
            fecha = miFormato.parse(miFecha);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Debe ingresar una fecha valida.");
            textField.setText("DD/MM/AAAA");
            textField.requestFocus();
            textField.selectAll();
            return false;
        }
        return true;
    }
    
    public boolean isInteger(String numero) {
        try {
            Integer.parseInt(numero);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
