/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiciosWeb;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@WebService(serviceName = "Paciente")
public class Paciente {

    /**
     * Registro de un paciente
     */
    @WebMethod(operationName = "registro_Paciente")
    public String registro_Paciente(
            @WebParam(name = "dpi")String dpi, 
            @WebParam(name = "nombrePaciente") String nombresPaciente, 
            @WebParam(name = "fecha_nac") String fechaNacimiento,
            @WebParam(name = "genero")int genero, 
            @WebParam(name = "direccion")String direccion, 
            @WebParam(name = "telefono")String telefono, 
            @WebParam(name = "estado")int estado,
            @WebParam(name = "correo")String correo) throws SQLException {
        
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
           
            
            sql = "insert into Paciente(nombre,fecha_nac,Genero,DIreccion,DPI,Telefono,Estado,Correo)"+
                    " values ('"+nombresPaciente+"','"+fechaNacimiento+"',"+genero+",'"+direccion+"','"
                    +dpi+"','"+telefono+"',"+estado+",'"+correo+"')";
            result = stmt.execute(sql);
            result=true;
            System.out.println(sql);
            
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            
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
    
    
    @WebMethod(operationName = "consultar_Paciente")
    public String consultar_Paciente(@WebParam(name = "dpi")String dpi) throws SQLException {
        
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
           
            
            sql = "select nombre, fecha_nac, Genero, direccion, telefono, estado, correo from Paciente Where dpi = "+dpi+";";
            result = stmt.executeQuery(sql);
            if(result!= null){
                while(result.next()){
                    String nombre = result.getString("nombre");
                    String fecha_nac = result.getString("fecha_nac");
                    String Genero = result.getString("Genero");
                    String direccion = result.getString("direccion");
                    String telefono = result.getString("telefono");
                    String estado = result.getString("estado");
                    String correo = result.getString("correo");
                    return "{"
                            +"\"nombre\": \""+nombre+"\","
                            +"\"fecha_nac\": \""+fecha_nac+"\","
                            +"\"genero\": \""+Genero+"\","
                            +"\"direccion\": \""+direccion+"\","
                            +"\"telefono\": \""+telefono+"\","
                            +"\"estado\": \""+estado+"\","
                            +"\"correo\": \""+correo+"\""
                            +"}";
                }
            }
            else{
                return "{\"error\"}";
            }
            
            System.out.println(sql);
            
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
        };
        return "{\"error\"}";
    }
    
    
}
