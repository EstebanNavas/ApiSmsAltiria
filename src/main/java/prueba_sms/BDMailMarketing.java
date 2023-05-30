package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;    //pool de conexiones jdbc

public class BDMailMarketing {
	
    public static void main(String[] args) {
    	
    	//Obtenemos la conexión a la base de datos
        Connection connection = conexionSQLMailMarketing.getConexionMailMarketing();
        
        // Verificamos si la coenxion es éxitosa o no 
        if (connection != null) {
            try {
              

                // Ejecutamos una consulta parametrizada. . Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
                String query = "SELECT * FROM tblMailCampaign WHERE idlocal = ? AND sistema = ? AND idCampaign = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                
             // Asignamos los valores de los parámetros en la consulta
                statement.setInt(1, 100); //idlocal
                statement.setString(2, "aquamovil");  //sistema
                statement.setInt(3, 18); //idCampaign
                
                		                              
              //Se ejecuta la consulta utilizando el método executeQuery() del objeto PreparedStatement y se guarda el resultado en el objeto ResultSet.  
                ResultSet resultSet = statement.executeQuery();

     
                
                
                // Recorremos los resultados de la consulta usando el metodo next() para recorrer fila por fila los registros obtenidos en la consulta
                while (resultSet.next()) {
                	
                    //  Utilizamos los métodos getInt() y getString() del ResultSet para obtener los valores de cada columna en el registro actual
                    int idLocal = resultSet.getInt("IDLOCAL");                   
                    String NombreCampaign = resultSet.getString("NombreCampaign");
                    String sistema = resultSet.getString("sistema");
                    int idCampaign = resultSet.getInt("idCampaign");
                    String textoSMS = resultSet.getString("textoSMS");
                    

                    // Mostramos los valores por consola
                      System.out.println("IDlocal: " + idLocal + "  NombreCampaign " + NombreCampaign + " sistema " + sistema + " idCampaign " + idCampaign + " textoSMS " + textoSMS);                    
                }

                // Se cierran los recursos utilizados para la conexión y consulta a la base de datos
                resultSet.close();
                statement.close();
                connection.close();
                
                //En caso de que ocurra una excepción durante el cierre de los recursos, se captura la excepción SQLException y se imprime la traza de la excepción utilizando el método printStackTrace()
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

