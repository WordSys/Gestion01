package ar.com.gestion.test;



import ar.com.gestion.connectors.ConnectorMySql;
import java.sql.Connection;
import javax.swing.JOptionPane;

public class MyTestConnector {
    public static void main(String[] args) throws Exception{
        Connection conn = ConnectorMySql.getConnection();
        //Connection conn = ConnectorSQL.getConnection();
        try {
            if(conn != null) JOptionPane.showMessageDialog(null, "La conexión tuvo éxito.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La conexión no tuvo éxito.");
        }
        conn.close();
    }
    
//        try {
//            if(conn != null) JOptionPane.showMessageDialog(null, "La conexión tuvo éxito.");
//            } catch (Exception exep) {
//                    JOptionPane.showMessageDialog(null, "La conexión no tuvo éxito.");
//                    }
//        conn.close();
}
