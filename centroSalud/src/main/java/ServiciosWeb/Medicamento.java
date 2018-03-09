/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiciosWeb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author cyno
 */
@WebService(serviceName = "Medicamento")
public class Medicamento {

    /**
     * Registro de medicamento
     */
    
    @WebMethod(operationName = "registro_Medicamento")
    public String registro_Medicamento(@WebParam(name = "nombre") String nombre) throws SQLException {
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            
            sql = "insert into Medicamento ( nombre)"+
                    " values ('"+nombre+"')";
            
            result = stmt.execute(sql);
            result=true;
            System.out.println(sql);
            
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            result = false;
        }
        finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };
        
        
        if(result)
            return "{\"estado:exito\"}";
        return "{\"estado:error\"}";
    }
    
    @WebMethod(operationName = "listado_Medicamento")
    public String listadoMedicamento() throws SQLException {
        boolean result = false;
        String sql = "";
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt = null;
        String json="";
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
           stmt = conn.createStatement();
                      
            sql = "select * from Medicamento";
            
            rs = stmt.executeQuery(sql);
            result=true;        
            json = "{\n";
            while(rs.next()){
                json    += rs.getString("idMedicamento")+":{\n\"nombre\" : \""
                        +rs.getString("Nombre")+"\"},\n";
            }
            json = json.substring(0, json.length()-2);
            json += "\n}";
            System.out.println(sql+"\n"+json);
            
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "{\"error\"}";
        }
        finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };
        
        if(result)
            return json;
        return "{\"estado:error\"}";
    }
    
}
