package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;    
import java.sql.Timestamp;

public class BDMailMarketing {
	
    public static void main(String[] args) {
        // Obtenemos la conexión a la base de datos
    	
    	
    	int xIdLocal = 100;
    	
    	
        Connection connection = conexionSQLMailMarketing.getConexionMailMarketing();

        // Verificamos si la conexión es exitosa o no
        if (connection != null) {
            try {
                // Llamamos al método consultarTextoSMS
            	
                String xTextoSMS = consultarTextoSMS(connection, xIdLocal);
                
                String xFechaHora  = consultarFechayHora(connection, xIdLocal);
                
                
                // crear array numeros celulares
                // for (   LOOP 
                
                //*** enviar mensaje "1-UN" mensaje 
                
                int xIdMaximoReporte = obtenerMaximoReporte(connection);
                
                
                //ingresaReporte(Connection connection, int xIdLocal, int xIdMaximoReporte)
                
                // )
                
                ingresaReporte(connection, xIdLocal, xIdMaximoReporte);


                
                
                // Se cierra la conexión a la base de datos
                connection.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

                                             // EXTRAEMOS EL MENSAJE DE TEXTO 
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
        //connection.close();

        // Retornamos el texto del SMS
        
		return fechayHora;
    }
    
    
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
    
    public static boolean ingresaReporte(Connection connection, int xIdLocal, int xIdMaximoReporte)throws SQLException  {
    	
    	String insertStr =  " INSERT INTO tblMailMarketingReporte "
    			+ "            (IDLOCAL               "// 1
    			+ "            ,sistema               "// 2  			
    			+ "            ,idCampaign            "// 3
    			+ "            ,idPlantilla           "// 4
    			+ "            ,idDcto                "// 5
    			+ "            ,idRequerimiento       "// 6
    			+ "            ,documentoTercero      "// 7
    			+ "            ,estado                "// 8
    			+ "            ,descripcion           "// 9
    			+ "            ,fechaHoraEvento       "// 10
    			+ "            ,exception             "// 11
    			+ "            ,email                 "// 12
    			+ "            ,celular)              "// 13
    			+ "  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    	
    	                       
    	String xSistema = "aquamovil"; 	
    	int xidCampaign = 0;
    	int xidPlantilla = 0;
    	int xidDcto = 0;									
    	int xidRequerimiento = 1;						
    	int xestado = 1;
    	String xdescripcion = "Envio SMS";
    	Timestamp xfechaHoraEvento = new Timestamp(System.currentTimeMillis()); 
    	String xexception = "";
    	String xemail = "";
    	String xcelular = "";
    	

        
         PreparedStatement statement = connection.prepareStatement(insertStr);
        	
         statement.setInt(1, xIdLocal);
         statement.setString(2, xSistema);
         statement.setInt(3, xidCampaign);
         statement.setInt(4, xidPlantilla);
         statement.setInt(5, xidDcto);
         statement.setInt(6, xidRequerimiento);
         statement.setString(7, xcelular);
         statement.setInt(8, xestado);
         statement.setString(9, xdescripcion);
         statement.setTimestamp(10, xfechaHoraEvento);
         statement.setString(11, xexception);
         statement.setString(12, xemail);
         statement.setString(13, xcelular);
            
            
         statement.executeUpdate();
            

            return true;
    
}

}






// 1
// 2
// 3
// 4
// 5
// 6
// 7
// 8
// 9
// 10
// 11
// 12
// 13
// 14







