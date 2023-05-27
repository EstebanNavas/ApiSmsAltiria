package prueba_sms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bdaquamovil {

	public static void main(String[] args) {
		
		//Obtenemos la conexión a la base de datos
        Connection connection = conexionSQL.getConexion();
        
        // Verificamos si la coenxion es éxitosa o no 
        if (connection != null) {
            try {
                // Creamos una declaración con el objeto Statement
                Statement statement = connection.createStatement();

                // Ejecutamos una consulta donde le pasamos el nombre de la tabla a consultar, el resultado se guarda en el objeto resultSet
                String query = "SELECT * FROM bdaquamovil.dbo.tblLocales";                                 
                ResultSet resultSet = statement.executeQuery(query);

     
                
                // Recorremos los resultados de la consulta usando el metodo next() para recorrer fila por fila los registros obtenidos en la consulta
                while (resultSet.next()) {
                	
                    //  Utilizamos los métodos getInt() y getString() del ResultSet para obtener los valores de cada columna en el registro actual
                    int idLocalTblLocales = resultSet.getInt("IDLOCAL");                   
                    String NombreLocal = resultSet.getString("NombreLocal");
                    

                    // Mostramos los valores por consola
                      System.out.println("IDlocal: " + idLocalTblLocales + "  NombreCampaign " + NombreLocal );                    
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
