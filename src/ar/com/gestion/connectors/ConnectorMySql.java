package ar.com.gestion.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class ConnectorMySql {
    private static String driver="com.mysql.jdbc.Driver";
    private static String vendor="mysql";
    private static String server="192.168.1.141";
    private static String port="3306";
    private static String bd="tablon";
    private static String user="root";
    private static String pass="";
    
    private static String url="jdbc:"+vendor+"://"+server+":"+port+"/"+bd;
    
    private static Connection conn=null;
    
    private ConnectorMySql(){}
    
    public static Connection getConnection(){
        if(conn!=null){
            return conn;
        }else{
            try {
                Class.forName(driver);
                conn=DriverManager.getConnection(url, user, pass);
                return conn;
            } catch (Exception e) {
                System.out.println(e);
                
                return null;
            }
        }
    }

    @Override
    public String toString() {
        JOptionPane.showMessageDialog(null, "Servidor MySql");
        return null;
    }
    
    
}
