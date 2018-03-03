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
@WebService(serviceName = "Doctor")
public class Doctor {

    /**
     * This is a sample web service operation
     */
   @WebMethod(operationName = "registro_Doctor")
    public String registro_Doctor(
            @WebParam(name = "nombre") String nombre,
            @WebParam(name = "licenciaMedica") String licenciaMedica,
            @WebParam(name = "fecha_nac") String fechaNacimiento,
            @WebParam(name = "especialidad")String especialidad) 
            throws SQLException {
        
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            
            String codigoDoctor = "";
            ResultSet idDoc = stmt.executeQuery("select max(idDoctor) idDoctor from Doctor");
            
            while ( idDoc.next() ) {
                String cod = idDoc.getString("idDoctor");
                codigoDoctor = cod;
            }
            
            int codDoctor = Integer.parseInt(codigoDoctor)+1;
            
            sql = "insert into Doctor ( idDoctor,Nombre,LicenciaMedica,Fecha_Nac,Especialidad)"+
                    " values ("+codDoctor+" , '"+nombre+"','"+licenciaMedica+"','"+fechaNacimiento+"','"+especialidad+"')";
            result = stmt.execute(sql);
            result=true;
            System.out.println(sql);
            
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
            return "{\"estado:exito\"}";
        return "{\"estado:error\"}";
    }
}
