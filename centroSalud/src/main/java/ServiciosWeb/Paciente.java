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
import java.util.ArrayList;
import java.util.Iterator;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.json.*;

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
            @WebParam(name = "dpi") String dpi,
            @WebParam(name = "nombrePaciente") String nombresPaciente,
            @WebParam(name = "fecha_nac") String fechaNacimiento,
            @WebParam(name = "genero") int genero,
            @WebParam(name = "direccion") String direccion,
            @WebParam(name = "telefono") String telefono,
            @WebParam(name = "estado") int estado,
            @WebParam(name = "correo") String correo) throws SQLException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        boolean result = false;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "insert into Paciente(nombre,fecha_nac,Genero,DIreccion,DPI,Telefono,Estado,Correo)"
                    + " values ('" + nombresPaciente + "','" + fechaNacimiento + "'," + genero + ",'" + direccion + "','"
                    + dpi + "','" + telefono + "'," + estado + ",'" + correo + "')";
            result = stmt.execute(sql);
            result = true;
            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC

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

    @WebMethod(operationName = "consultar_Paciente")
    public String consultar_Paciente(@WebParam(name = "dpi") String dpi) throws SQLException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "select nombre, fecha_nac, Genero, direccion, telefono, estado, correo from Paciente Where dpi = " + dpi + ";";
            result = stmt.executeQuery(sql);
            if (result != null) {
                while (result.next()) {
                    String nombre = result.getString("nombre");
                    String fecha_nac = result.getString("fecha_nac");
                    String Genero = result.getString("Genero");
                    String direccion = result.getString("direccion");
                    String telefono = result.getString("telefono");
                    String estado = result.getString("estado");
                    String correo = result.getString("correo");
                    return "{\n"
                            + "\"nombre\": \"" + nombre + "\",\n"
                            + "\"fecha_nac\": \"" + fecha_nac + "\",\n"
                            + "\"genero\": \"" + Genero + "\",\n"
                            + "\"direccion\": \"" + direccion + "\",\n"
                            + "\"telefono\": \"" + telefono + "\",\n"
                            + "\"estado\": \"" + estado + "\",\n"
                            + "\"correo\": \"" + correo + "\"\n"
                            + "}";
                }
            } else {
                return "{\"error\"}";
            }

            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "{error}";
        } finally {
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

    @WebMethod(operationName = "historial_Paciente")
    public String historial_Paciente(@WebParam(name = "dpi") String dpi) throws SQLException {

       JSONObject jObject = new JSONObject(dpi);
       dpi = (String) jObject.get("dpi").toString();

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();

            sql = "select P.nombre as 'Nombre Paciente', P.Genero, D.nombre as 'Nombre Doctor', C.Fecha, C.idCita  from Paciente as P, Cita as C , Doctor as D where P.dpi = '" + dpi + "' and P.idPaciente = C.Paciente and C.Doctor = D.idDoctor;";
            result = stmt.executeQuery(sql);
            String Paciente = "";
            String hCita = "";
            if (result != null) {
                while (result.next()) {

                    String nombrePac = result.getString("Nombre Paciente");
                    String genero = result.getString("Genero");

                    String nombreDoc = result.getString("Nombre Doctor");
                    String fecha = result.getString("Fecha");
                    String idCita = result.getString("idCita");

                    Paciente = "{"
                            + "\"nombrePaciente\": \"" + nombrePac + "\",\n"
                            + "\"genero\": \"" + genero + "\",\n";

                    hCita += idCita + ":{"
                            + "\"nombreDoctor\": \"" + nombreDoc + "\",\n"
                            + "\"fecha\": \"" + fecha + "\",\n"
                            + "\"diagnostico\":{\n";

                    sql = "Select E.Nombre as 'Nombre Enfermedad' FROM Cita as C, Diagnostico as D, Enfermedad as E WHERE C.idCita = " + idCita + " and D.Cita = C.idCita and D.Enfermedad_idEnfermedad = E.idEnfermedad;";
                    ResultSet result2 = stmt2.executeQuery(sql);
                    while (result2.next()) {
                        String nombreEnf = result2.getString("Nombre Enfermedad");
                        hCita += "\"enfermedad: \"" + nombreEnf + "\",\n";
                    }
                    hCita = hCita.substring(0, hCita.length() - 2);
                    hCita += "\n}\n";
                    hCita += "\n},\n";

                }
                hCita = hCita.substring(0, hCita.length() - 2);

                return Paciente + hCita + "\n}";
            } else {
                return "{\"error\"}";
            }

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "{\"error\"} ";
        } finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        }
    }

    @WebMethod(operationName = "obtenerIdPaciente")
    public String obtenerIdPaciente(@WebParam(name = "dpi") String dpi) throws SQLException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "select idPaciente from Paciente Where dpi = " + dpi + ";";
            result = stmt.executeQuery(sql);
            if (result != null) {
                while (result.next()) {
                    return result.getString("idPaciente");
                }
            } else {
                return "{\"error\"}";
            }

            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "" + se;
        } finally {
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

    
     @WebMethod(operationName = "obtenerIdPaciente")
    public String trasladoPaciente(@WebParam(name = "dpi") String dpi) throws SQLException {

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();

            sql = "select idPaciente from Paciente Where dpi = " + dpi + ";";
            result = stmt.executeQuery(sql);
            if (result != null) {
                while (result.next()) {
                    return result.getString("idPaciente");
                }
            } else {
                return "{\"error\"}";
            }

            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "" + se;
        } finally {
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
