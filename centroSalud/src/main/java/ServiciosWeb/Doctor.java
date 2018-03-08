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
     * Registro de un Doctor
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
            
           
            
            sql = "insert into Doctor (Nombre,LicenciaMedica,Fecha_Nac,Especialidad)"+
                    " values ('"+nombre+"','"+licenciaMedica+"','"+fechaNacimiento+"','"+especialidad+"')";
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
    
    @WebMethod(operationName = "consultar_Doctores")
    public String consultar_Paciente() throws SQLException {
        
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
           
            
            sql = "select idDoctor, nombre, licenciaMedica, Fecha_Nac, Especialidad from Doctor;";
            result = stmt.executeQuery(sql);
            String returns="{\n";
            if(result!= null){
                while(result.next()){
                    String id = result.getString("idDoctor");
                    String nombre = result.getString("nombre");
                    String fecha_nac = result.getString("fecha_nac");
                    String licencia = result.getString("licenciaMedica");
                    String especialidad = result.getString("Especialidad");
                    returns+= id+":{\n" 
                             + "\"nombre\": \""+nombre+"\",\n"
                             +"\"licencia\": \""+licencia+"\",\n"
                             +"\"fecha_nac\": \""+fecha_nac+"\",\n"
                             +"\"especialidad\": \""+especialidad+"\" \n}\n";
                }
                returns+="}";
                return returns;
            }
            else{
                return "{\"error\"}";
            }
            
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return ""+se;
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
