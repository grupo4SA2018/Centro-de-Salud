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
@WebService(serviceName = "Diagnostico")
public class Diagnostico {

    /**
     * Registro de un Diagnostico
     */
    @WebMethod(operationName = "registro_Diagnostico")
   public String registro_TrasladoPaciente(@WebParam(name = "descripcion") String descripcion, @WebParam(name = "idEnfermedad") int enfermedad) throws SQLException {
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            
            sql = "select max(idCita) idCita from Cita;";
            String codigoReceta="1";
            
            
            ResultSet idTran = stmt.executeQuery(sql); 
            
            while ( idTran.next() ) {
                String cod = idTran.getString("idCita");
                codigoReceta = cod;
                
            }
            
            int cita = Integer.parseInt(codigoReceta);
            
            sql = "insert into Diagnostico ( descripcion,cita,Enfermedad_idEnfermedad)"+
                    " values ('"+descripcion+"',"+cita+","+enfermedad+")";
            
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
}
