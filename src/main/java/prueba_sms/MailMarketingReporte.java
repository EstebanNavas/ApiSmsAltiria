package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Timestamp;

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
		
		//Se define la consulta SQL para obtener el máximo valor de idReporte de la tabla tblMailMarketingReporte
		String queryObtenerMaxIdReporte = "SELECT MAX (idReporte) AS maxIdReporte FROM tblMailMarketingReporte";
		
		PreparedStatement statement = connection.prepareStatement(queryObtenerMaxIdReporte);
        ResultSet resultSet = statement.executeQuery();
        
        //se obtiene el valor de maxIdReporte del resultado utilizando el método getInt() del objeto ResultSet
        if (resultSet.next()) {
            maxIdReporte = resultSet.getInt("maxIdReporte");
        }
        
        //Cerramos para librar los recursos
        resultSet.close();
        statement.close();
        
        System.out.println("maxIdReporte: " + maxIdReporte);
        
		return maxIdReporte;
		
	}
	
								// GENERAMOS LOS REGISTROS A INSERTAR
	public static ArrayList<Object[]> generarRegistros(int maxIdReporte){
		ArrayList<Object[]> registros = new ArrayList();
		
        
        // Creamos un objeto java.sql.Timestamp para obtener la fecha y hora del sistema
//        Timestamp fechaHoraEvento = new Timestamp(System.currentTimeMillis());
        
     // Obtenemos los valores correspondientes a cada campo
        int IDLOCAL = 0; 
        int sistema = 0; 
        int idCampaign = 0; 
        int idPlantilla = 0; 
        int idDcto = 0; 
        int idRequerimiento = 1; 
        String documentoTercero = "celular"; 
        int estado = 1; 
        String descripcion = "Envio SMS";
        Timestamp fechaHoraEvento = new Timestamp(System.currentTimeMillis()); 
        String exception = ""; 
        String email = ""; 
        String celular = "celular"; 


        // Agregamos un nuevo objeto ArrayList con los valores de cada campo a insertar
        Object[] registro = { IDLOCAL, sistema, maxIdReporte, idCampaign, idPlantilla, idDcto, idRequerimiento,
                documentoTercero, estado, descripcion, fechaHoraEvento, exception, email, celular };
        registros.add(registro);
		
		
		return registros;
	}
	
		
	}//FIN DE LA CLASE


