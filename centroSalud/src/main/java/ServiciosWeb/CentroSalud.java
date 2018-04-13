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
@WebService(serviceName = "CentroSalud")
public class CentroSalud {

    /**
     * Registro de un centro de salud
     */
    @WebMethod(operationName = "registro_CentroSalud")
    public String registro_Enfermedad(@WebParam(name = "nombre") String nombre, @WebParam(name = "direccion") String direccion) throws SQLException {
        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "insert into Centro_Salud ( nombre,direccion)"
                    + " values ('" + nombre + "','" + direccion + "')";

            result = stmt.execute(sql);
            result = true;
            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            result = false;
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
            return "{\"estado:exito\"}";
        }
        return "{\"estado:error\"}";
    }

    @WebMethod(operationName = "reporteMorbilidad")
    public String reporteMorbilidad() throws SQLException {
        String sql = "";
        Connection conn = null;
        Statement stmt = null;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "SELECT IFNULL(Enfermedad.Nombre,\"Totales\") as Nombre, \n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <= 5 THEN 1 ELSE 0 END) as '0 - 5',\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 5 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=15 THEN 1 ELSE 0 END) as '6 - 15',\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 15 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=21 THEN 1 ELSE 0 END) as '16 - 20',\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 21 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=45 THEN 1 ELSE 0 END) as '21 - 45',\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 45 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=60 THEN 1 ELSE 0 END) as '46 - 60',\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) >= 61 THEN 1 ELSE 0 END) as '61 - ...',\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <= 5 THEN 1 ELSE 0 END) +\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 5 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=15 THEN 1 ELSE 0 END) +\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 5 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=15 THEN 1 ELSE 0 END) +\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 15 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=21 THEN 1 ELSE 0 END) +\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 21 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=45 THEN 1 ELSE 0 END) +\n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) > 45 AND TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) <=60 THEN 1 ELSE 0 END) + \n"
                    + "SUM(CASE WHEN TIMESTAMPDIFF(YEAR, C.fecha_nac, CURDATE()) >= 61 THEN 1 ELSE 0 END) as Total\n"
                    + "FROM Diagnostico as A \n"
                    + "INNER JOIN Enfermedad \n"
                    + "INNER JOIN Paciente as C\n"
                    + "INNER JOIN Cita as D\n"
                    + "ON A.Enfermedad_idEnfermedad = Enfermedad.idEnfermedad \n"
                    + "WHERE C.idPaciente = D.Paciente AND D.idCita = A.Cita \n"
                    + "Group BY Enfermedad.Nombre\n"
                    + "WITH ROLLUP;";
            ResultSet result = null;
            result = stmt.executeQuery(sql);
            String salida = "{\n";
            if (result != null) {
                while (result.next()) {
                     String nombre = result.getString("Nombre");
                     String e5 = result.getString("0 - 5");
                     String e15 = result.getString("6 - 15");
                     String e20 = result.getString("16 - 20");
                     String e45 = result.getString("21 - 45");
                     String e60 = result.getString("46 - 60");
                     String e61 = result.getString("61 - ...");
                     String Tot = result.getString("Total");
                     
                     salida += "\""+nombre+"\":{\n"
                             + "\"0 - 5\":"+e5 +",\n"
                             + "\"6 - 15\":"+e15 +",\n"
                             + "\"16 - 20\":"+e20 +",\n"
                             + "\"21 - 45\":"+e45 +",\n"
                             + "\"46 - 60\":"+e60 +",\n"
                             + "\"61 - ...\":"+e61 +",\n"
                             + "\"Total\":"+Tot+"\n},\n";
                }
            }
            
            if(salida.length()>5){
                salida = salida.substring(0, salida.length() - 2);
                salida = salida + "\n}";
                return salida;
            }
        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "{\"estado:error\"}";
        } finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };

        return "{\"estado:error\"}";
    }

}
