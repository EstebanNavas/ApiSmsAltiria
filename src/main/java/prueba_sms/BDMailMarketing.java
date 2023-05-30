package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;    //pool de conexiones jdbc

public class BDMailMarketing {
    public static void main(String[] args) {
        // Obtenemos la conexión a la base de datos
        Connection connection = conexionSQLMailMarketing.getConexionMailMarketing();

        // Verificamos si la conexión es exitosa o no
        if (connection != null) {
            try {
                // Llamamos al método consultarTextoSMS
                consultarTextoSMS(connection, 100);
                
                consultarFechayHora(connection, 100);
                
                
                // Se cierra la conexión a la base de datos
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

                                             // EXTRAEMOS LA RAZON SOCIAL DEL LOCAL
    public static String consultarTextoSMS(Connection connection, int idLocal) throws SQLException {
        String textoSMS = "";

        // Ejecutamos una consulta parametrizada
        String queryTextoSMS = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
        PreparedStatement statement = connection.prepareStatement(queryTextoSMS);

        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, 100); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, 18); // idCampaign

        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta
        while (resultSet.next()) {
            // Obtenemos los valores de cada columna en el registro actual
            int idLocales = resultSet.getInt("IDLOCAL");
            String NombreCampaign = resultSet.getString("NombreCampaign");
            String sistema = resultSet.getString("sistema");
            int idCampaign = resultSet.getInt("idCampaign");
            textoSMS = resultSet.getString("textoSMS");

            // Mostramos los valores por consola
            System.out.println("IDlocal: " + idLocales + " NombreCampaign: " + NombreCampaign + " sistema: " + sistema
                    + " idCampaign: " + idCampaign + " textoSMS: " + textoSMS);
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        

        // Retornamos el texto del SMS
        return textoSMS;
    }
    
                                                 // EXTRAEMOS LA FECHA Y HORA QUE ESTÁ PROGRAMADO EL ENVIO DEL SMS
    
    public static String consultarFechayHora(Connection connection, int idLocal)throws SQLException  {
    	String fechayHora = "";
    	
    	// Ejecutamos una consulta parametrizada
        String queryFechayHora = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
        PreparedStatement statement = connection.prepareStatement(queryFechayHora);
    	
        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, 100); // idlocal
        statement.setString(2, "aquamovil"); // sistema
        statement.setInt(3, 18); // idCampaign
    	
        // Ejecutamos la consulta y obtenemos el resultado
        ResultSet resultSet = statement.executeQuery();
        
     // Recorremos los resultados de la consulta
        while (resultSet.next()) {
            // Obtenemos los valores de cada columna en el registro actual
            int idLocales = resultSet.getInt("IDLOCAL");
            String NombreCampaign = resultSet.getString("NombreCampaign");
            String sistema = resultSet.getString("sistema");
            int idCampaign = resultSet.getInt("idCampaign");
            fechayHora = resultSet.getString("fecha/hora");
            

            // Mostramos los valores por consola
            System.out.println("IDlocal: " + idLocales + " NombreCampaign: " + NombreCampaign + " sistema: " + sistema
                    + " idCampaign: " + idCampaign + " Fecha/Hora: " + fechayHora);
        }

        // Cerramos los recursos utilizados
        resultSet.close();
        statement.close();
        connection.close();

        // Retornamos el texto del SMS
        
		return fechayHora;
    }
}















