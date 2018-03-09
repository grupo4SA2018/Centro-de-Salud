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
@WebService(serviceName = "Enfermedad")
public class Enfermedad {

    /**
     * This is a sample web service operation
     */
   @WebMethod(operationName = "registro_Enfermedad")
    public String registro_Enfermedad(@WebParam(name = "nombre") String nombre) throws SQLException {
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            
            sql = "insert into Enfermedad ( nombre)"+
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
    
    
    @WebMethod(operationName = "consultar_Enfermedades")
    public String consultar_Enfermedades() throws SQLException {
        
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
           
            
            sql = "select idEnfermedad, nombre from Enfermedad;";
            result = stmt.executeQuery(sql);
            String returns="{\n";
            if(result!= null){
                while(result.next()){
                    String id = result.getString("idEnfermedad");
                    String nombre = result.getString("nombre");
                    returns+= id+" : {\n"
                             +"\"nombre\": \""+nombre+"\"\n"
                             +"},\n";
                }
                returns = returns.substring(0, returns.length()-2);
                returns+="\n}";
                return returns;
            }
            else{
                return "{\"error\"}";
            }
            
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
        }
    }
    
}
