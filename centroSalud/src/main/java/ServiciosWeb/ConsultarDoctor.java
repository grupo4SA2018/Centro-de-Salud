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
 * @author gerson
 */
@WebService(serviceName = "ConsultarDoctor")
public class ConsultarDoctor {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "idDoctor") String idoctor) throws SQLException {
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
                      
            sql = "select Nombre, LicenciaMedica,Fecha_Nac, Especialidad from Doctor where idDoctor="+idoctor;
            
            rs = stmt.executeQuery(sql);
            result=true;            
            while(rs.next()){
                json+= "{\n \"nombre\" : \""+rs.getString("Nombre")+" \",\n"+
                        "\"licencia\" : \""+rs.getString("LicenciaMedica")+"\",\n"+
                        "\"fecha\" : \""+rs.getString("Fecha_Nac")+"\",\n"+
                        "\"especialidad\" : \""+rs.getString("Especialidad")+"\",\n}";
            }
            System.out.println(sql+"\n"+json);
            
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return sql+" ERROR -> "+se.toString();
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
