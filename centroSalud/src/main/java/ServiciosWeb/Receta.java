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
@WebService(serviceName = "Receta")
public class Receta {

    /**
     * Registro de una receta
     */
     @WebMethod(operationName = "registro_Receta")
   public String registro_Receta(@WebParam(name = "idCita") int cita, @WebParam(name = "cantidad") int cantidad, @WebParam(name = "idMedicamento") int medicamento) throws SQLException {
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            
            sql = "insert into Receta (cita)"+
                    " values ("+cita+")";
            
            result = stmt.execute(sql);
            
            sql = "select max(idReceta) idReceta from Receta;";
            String codigoReceta="1";
            ResultSet idTran = stmt.executeQuery(sql); 
              while ( idTran.next() ) {
                String cod = idTran.getString("idReceta");
                codigoReceta = cod;
            }
            
            
            sql = "insert into Detalle_Receta ( cantidad,receta,medicamento)"+
                    " values ("+cantidad+","+codigoReceta+","+medicamento+")";
            
            result = stmt.execute(sql);
            
            result=true;
            
            
            
            
            
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
   
   @WebMethod(operationName = "consultar_Receta")
    public String consultarReceta(@WebParam(name = "idReceta") String idReceta) throws SQLException {
        boolean result = false;
        String sql = "";
        String sql1 = "";
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        Statement stmt = null;
        String json="";
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
           stmt = conn.createStatement();
           
            sql = "select m.Nombre as Nombre, dr.cantidad as Cantidad from Detalle_Receta dr "
                    + "inner join Receta r on dr.Receta = r.idReceta inner join"
                    + " Medicamento m on m.idMedicamento = dr.Medicamento where r.idReceta ="+idReceta;
            
            rs = stmt.executeQuery(sql);
            result=true;    
            json = "{\n";
            while(rs.next()){
                json+= "{\n\"nombre\" : \""+rs.getString("Nombre")+" \",\n"
                        +"\"cantidad\" : \""+rs.getString("Cantidad")+" \"},\n";
            }
            json = json.substring(0, json.length()-2);
            json += "}";
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
        return "{\"error\"}";
    }
}
