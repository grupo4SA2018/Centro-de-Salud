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
import org.json.JSONObject;


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
    public String registro_TrasladoPaciente(@WebParam(name = "entrada") String entrada) throws SQLException {
        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;
        try {
            JSONObject jObject = new JSONObject(entrada);
            String dpi = (String) jObject.get("Paciente").toString();
            String destino = (String) jObject.get("Destino").toString();
            String origen = (String) jObject.get("Origen").toString();

            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "INSERT INTO Traslado_Paciente (Fecha,Destino,Origen,Paciente) VALUES (CURDATE()," + destino + "," + origen + ",'" + dpi + "')";
            result = stmt.execute(sql);
            result = true;

        } catch (Exception se) {
            //Handle errors for JDBC
            return "{\"Exito\":\"0\",\n"
                    + "\"Error\":\"Algo sucedio mal\"}";
        } finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };
        if (result) {
            return "{\n\"Exito\":\"1\",\n"
                    + "\"Error\":\"\"}";
        }
        return "{\"Exito\":\"0\",\n"
                + "\"Error\":\"Algo sucedio mal\"}";
    }
}
