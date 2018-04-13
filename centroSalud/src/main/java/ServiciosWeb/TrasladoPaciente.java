/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServiciosWeb;

import java.sql.Connection;
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
@WebService(serviceName = "TrasladoPaciente")
public class TrasladoPaciente {

    /**
     * registro de un traslado de paciente
     */
     @WebMethod(operationName = "registro_TrasladoPaciente")
    public String registro_TrasladoPaciente(@WebParam(name = "fecha") String fecha, @WebParam(name = "idDestino") int destino, @WebParam(name = "idOrigen") int origen, @WebParam(name = "idPaciente") int paciente) throws SQLException {
        String sql="";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        
        try {
           InitialContext ctx = new InitialContext();
           DataSource ds = (DataSource)ctx.lookup("java:/CentroSaludDS");
           conn =  ds.getConnection();
            stmt = conn.createStatement();
            
            sql = "insert into Traslado_Paciente ( fecha,destino,origen,paciente)"+
                    " values ('"+fecha+"',"+destino+","+origen+","+paciente+")";
            
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
