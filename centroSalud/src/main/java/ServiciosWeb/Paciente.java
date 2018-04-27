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
        dpi = (String) jObject.get("DPI").toString();

        String sql = "";
        Connection conn = null;
        Statement stmt = null;
        Statement stmt0 = null;
        Statement stmt2 = null;
        ResultSet result;

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/CentroSaludDS");
            conn = ds.getConnection();
            stmt = conn.createStatement();
            stmt0 = conn.createStatement();
            stmt2 = conn.createStatement();

            sql = "SELECT Nombre, Genero FROM Paciente WHERE DPI = " + dpi + ";";
            ResultSet result0;
            result0 = stmt0.executeQuery(sql);
            if (result0.next()) {

                String Paciente = "";
                String nombrePac = result0.getString("Nombre");
                String genero = result0.getString("Genero");
                
                Paciente = "{\n"
                        + "\"Exito\":1\n"
                        + "\"DPI\":\"" + dpi + "\",\n"
                        + "\"Nombre\": \"" + nombrePac + "\",\n"
                        + "\"Apellido\": \"" + nombrePac + "\",\n"
                        + "\"Telefono\":\"\",\n"
                        + "\"Direccion\":\"\",\n"
                        + "\"Fecha_Nacimiento\":\"\",\n"
                        + "\"Historial\":[\n";

                sql = "select P.nombre as 'Nombre Paciente', P.Genero, D.nombre as 'Nombre Doctor', C.Fecha, C.idCita  from Paciente as P, Cita as C , Doctor as D where P.dpi = '" + dpi + "' and P.idPaciente = C.Paciente and C.Doctor = D.idDoctor;";
                result = stmt.executeQuery(sql);
                String hCita = "";
                String nuevoCita = "";
                if (result != null) {
                    
                    while (result.next()) {

                        String nombreDoc = result.getString("Nombre Doctor");
                        String fecha = result.getString("Fecha");
                        String idCita = result.getString("idCita");

                        nuevoCita += "{\n"
                                + "\"Fecha\": \"" + fecha + "\",\n"
                                + "\"Descripcion\": \"Esta enfermo \",\n"
                                + "\"Diagnosticos\":[\n";

                        sql = "Select E.Nombre as 'Nombre Enfermedad' FROM Cita as C, Diagnostico as D, Enfermedad as E WHERE C.idCita = " + idCita + " and D.Cita = C.idCita and D.Enfermedad_idEnfermedad = E.idEnfermedad;";
                        ResultSet result2 = stmt2.executeQuery(sql);
                        String nuevo="";
                        while (result2.next()) {
                            String nombreEnf = result2.getString("Nombre Enfermedad");
                            nuevo += "{\n"
                                    + "\"Diagnostico\": \"" + nombreEnf + "\"\n"
                                    + "},";
                        }
                        if (nuevo.length() > 2) {
                            nuevo = nuevo.substring(0, nuevo.length() - 2);
                            nuevo += "\n}\n"; //cierre enfermedades
                            nuevo += "]\n";
                            nuevo += "\n},\n"; // cierre cita
                        } else {
                            nuevo += "{ } ]\n},\n";
                        }
                        nuevoCita += nuevo;
                        hCita += nuevoCita;
                    }
                    
                    if (hCita.length() > 2) {
                        hCita = hCita.substring(0, hCita.length() - 2);
                        hCita += "],\n";
                    }

                    hCita += "\"traslados\":[";
                    sql = "SELECT Fecha, Destino, Origen "
                            + "FROM Traslado_Paciente, Paciente\n"
                            + "WHERE Paciente.DPI = Traslado_Paciente.Paciente AND Traslado_Paciente.Paciente = '" + dpi + "';";
                    ResultSet result3 = stmt2.executeQuery(sql);
                    int cont = 1;
                    while (result3.next()) {
                        String fecha = result3.getString("Fecha");
                        String destino = result3.getString("Destino");
                        String origen = result3.getString("Origen");
                        hCita += "\n{\n"
                                + "\"origen\":" + origen + " ,\n"
                                + "\"destino\":" + destino + ",\n"
                                + "\"Fecha\":\"" + fecha + "\""
                                + "\n},\n";

                        cont++;
                    }
                    if (hCita.length() > 2) {
                        hCita = hCita.substring(0, hCita.length() - 2);
                        hCita += "\n]\n";
                    }
                    if (Paciente.length() > 3) {
                        return Paciente + hCita + "\n}"; // cierre
                    } else {
                        return "{\"Exito\":\"0\",\n"
                + "\"Error\":\"Algo sucedio mal\"}";
                    }
                } else {
                    return "{\"Exito\":\"0\",\n"
                + "\"Error\":\"Algo sucedio mal\"}";
                }
            } else {
                return "{\"Exito\":\"0\",\n"
                + "\"Error\":\"Algo sucedio mal\"}";
            }
        } catch (NumberFormatException | SQLException | NamingException se) {
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
                return "{\"estado\":\"error\"}";
            }

            System.out.println(sql);

        } catch (NumberFormatException | SQLException | NamingException se) {
            //Handle errors for JDBC
            return "{\"estado\":\"error\"}";
        } finally {
            //finally block used to close resources
            if (stmt != null) {
                conn.close();
            } // do nothing
            if (conn != null) {
                conn.close();
            } //end finally try
        };
        return "{\"estado\":\"error\"}";
    }

    @WebMethod(operationName = "trasladoPaciente")
    public String trasladoPaciente(@WebParam(name = "entrada") String entrada) throws SQLException {
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
