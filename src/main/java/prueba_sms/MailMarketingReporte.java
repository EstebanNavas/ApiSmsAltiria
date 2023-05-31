package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MailMarketingReporte {
	
	public static void main(String[] args) {
		
	     // Obtenemos la conexión a la base de datos
        Connection connection = conexionSQLMailMarketing.getConexionMailMarketing();

        // Verificamos si la conexión es exitosa o no
        if (connection != null) {
            try {
                // Llamamos a los metodos
            	
            	obtenerMaximoReporte(connection);
                
                
                
                // Se cierra la conexión a la base de datos
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//FIN DEL MAIN
	
	
	
									//OBTENEMOS EL REPORTE MÁXIMO DE IDREPORTE
	public static int obtenerMaximoReporte(Connection connection) throws SQLException{
		
		int maxIdReporte = 0;
		
		String queryObtenerMaxIdReporte = "SELECT MAX (idReporte) AS maxIdReporte FROM tblMailMarketingReporte";
		
		PreparedStatement statement = connection.prepareStatement(queryObtenerMaxIdReporte);
        ResultSet resultSet = statement.executeQuery();
        
        if (resultSet.next()) {
            maxIdReporte = resultSet.getInt("maxIdReporte");
        }
        
        resultSet.close();
        statement.close();
        
        System.out.println("maxIdReporte: " + maxIdReporte);
        
		return maxIdReporte;
		
	}
		
	}//FIN DE LA CLASE


