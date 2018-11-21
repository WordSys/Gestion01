package ar.com.gestion.test;

import ar.com.gestion.entities.Producto;
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.repositories.ProductoJpaController;

import java.math.BigDecimal;

public class MyTestRepositories {
    
    
    public static void cargarProducto() throws Exception{
        //int codigo = 1;
        String nombre = "Camiseta San Lorenzo";
        double cantidad = Double.valueOf(44.52);
        double precio = Double.valueOf(558.23);
        //Proveedor id = new Proveedor(1);
        
        Producto producto = new Producto(nombre,cantidad,precio);
        //Producto producto = new Producto(codigo,nombre,cantidad,precio,id);
        ProductoJpaController prodRep = new ProductoJpaController();
        producto.toString();
        prodRep.save(producto);
        
    }
    
    public static void buscaProducto(String nombre){
        ProductoJpaController buscado = new ProductoJpaController();
        //buscado.findProductoEntities(nombre);
    }
    
    
    
    public static void main(String[] args) throws Exception {
        cargarProducto();
                
    }
    
}
