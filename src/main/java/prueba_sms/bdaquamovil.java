package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

public class bdaquamovil {

    public static void main(String[] args) {
        // Obtenemos la conexión a la base de datos
        Connection connection = conexionSQLaquamovil.getConexionAquamovil();
        
        
        int xIdLocal = 100;
        int xIdPeriodo=202304;
               

        // Verificamos si la conexión es exitosa o no
        if (connection != null) {
            try {
            	
            	//Llamamos a los metodos
            	consultarRazonSocial(connection, xIdLocal);
                
            	consultarNombrePeriodo(connection, xIdLocal, xIdPeriodo);
            	
            	consultarFechaConRecargo(connection, xIdLocal, xIdPeriodo);
                
                consultarTelefonoCelular(connection, xIdLocal);
                
                

                // Se cierra la conexión a la base de datos
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }// FIN MAIN

                                             // EXTRAER EL ID DEL LOCAL Y SU RAZÓN SOCIAL 
    public static String consultarRazonSocial(Connection connection, int idLocal) throws SQLException {
    	
    	String razonSocial = "";
    	
        // Ejecutamos una consulta parametrizada. Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
        String queryRazonSocial = "SELECT * FROM tblLocales WHERE idLocal = ?";
        PreparedStatement statement = connection.prepareStatement(queryRazonSocial);

        // Asignamos los valores de los parámetros en la consulta
        statement.setInt(1, idLocal);

        // Se ejecuta la consulta utilizando el método executeQuery() del objeto PreparedStatement y se guarda el resultado en el objeto ResultSet.
        ResultSet resultSet = statement.executeQuery();

        // Recorremos los resultados de la consulta usando el método next() para recorrer fila por fila los registros obtenidos en la consulta
        while (resultSet.next()) {
        	
            // Utilizamos los métodos getInt() y getString() del ResultSet para obtener los valores de cada columna en el registro actual
            int idLocales = resultSet.getInt("IDLOCAL");
            razonSocial = resultSet.getString("razonSocial");

            // Mostramos los valores por consola
            System.out.println("Query 1 - IDlocal: " + idLocales + "  razonSocial: " + razonSocial);
        }

        // Cerramos los recursos utilizados para la consulta
        resultSet.close();
        statement.close();
        
		return razonSocial;
    }
    
    
    
    
    
    
                                        // EXTRAER NOMBRE PERIODO
    	public static String consultarNombrePeriodo(Connection connection, int idLocal, int idPeriodo ) throws SQLException{
    		
    		
    		String nombrePeriodo = "";
    		
    		
    		// Ejecutamos una consulta parametrizada. Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
            String queryPeriodoFechaPago = "SELECT * FROM tblDctosPeriodo WHERE idLocal = ? AND idPeriodo = ?";
            PreparedStatement statement = connection.prepareStatement(queryPeriodoFechaPago);
            
         // Asignamos los valores de los parámetros en la consulta
            statement.setInt(1, idLocal);
            statement.setInt(2, idPeriodo);
            
         // Se ejecuta la consulta utilizando el método executeQuery() del objeto PreparedStatement y se guarda el resultado en el objeto ResultSet.
            ResultSet resultSet = statement.executeQuery();
            
         // Recorremos los resultados de la consulta usando el método next() para recorrer fila por fila los registros obtenidos en la consulta
            while (resultSet.next()) {
            	
                // Utilizamos los métodos getInt() y getString() del ResultSet para obtener los valores de cada columna en el registro actual
                int idLocales = resultSet.getInt("IDLOCAL");
                int idPeriodos = resultSet.getInt("idPeriodo");
                nombrePeriodo = resultSet.getString("nombrePeriodo");
                

                // Mostramos los valores por consola
                System.out.println("Query 2 - IDlocal: " + idLocales + "  idPeriodo: " + idPeriodos + " nombrePeriodo: " + nombrePeriodo );
            }

            // Cerramos los recursos utilizados para la consulta
            resultSet.close();
            statement.close();
            
            return nombrePeriodo;
            
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    												//EXTRER FECHA CON RECARGO
    		public static String consultarFechaConRecargo(Connection connection, int idLocal, int idPeriodo ) throws SQLException{
    		
    		
    		String fechaConRecargo = "";
    		
    		
    		// Ejecutamos una consulta parametrizada. Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
            String queryPeriodoFechaPago = "SELECT * FROM tblDctosPeriodo WHERE idLocal = ? AND idPeriodo = ?";
            PreparedStatement statement = connection.prepareStatement(queryPeriodoFechaPago);
            
         // Asignamos los valores de los parámetros en la consulta
            statement.setInt(1, idLocal);
            statement.setInt(2, idPeriodo);
            
         // Se ejecuta la consulta utilizando el método executeQuery() del objeto PreparedStatement y se guarda el resultado en el objeto ResultSet.
            ResultSet resultSet = statement.executeQuery();
            
         // Recorremos los resultados de la consulta usando el método next() para recorrer fila por fila los registros obtenidos en la consulta
            while (resultSet.next()) {
            	
                // Utilizamos los métodos getInt() y getString() del ResultSet para obtener los valores de cada columna en el registro actual
                int idLocales = resultSet.getInt("IDLOCAL");
                int idPeriodos = resultSet.getInt("idPeriodo");
                
                // Obtener la fecha de la columna "fechaConRecargo" como objeto java.sql.Timestamp
                java.sql.Timestamp fechaRecargo = resultSet.getTimestamp("fechaConRecargo");
                
                // Convertir java.sql.Timestamp a java.util.Date
                Date fechaRecargoUtil = new Date(fechaRecargo.getTime());
                
                // Formatear la fecha al formato "dd/MM/yyyy"
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                fechaConRecargo = dateFormat.format(fechaRecargoUtil);
                            
                // Mostramos los valores por consola
                System.out.println("Query 3 - IDlocal: " + idLocales + "  idPeriodo: " + idPeriodos +  " fechaConRecargo: " + fechaConRecargo);
            }

            // Cerramos los recursos utilizados para la consulta
            resultSet.close();
            statement.close();
            
            return fechaConRecargo;
            
    	}
    		
    		
    		
    		
    	
    	
    	
    	                                            // EXTRAER NÚMEROS DE TELEFONO CELULAR
    	
    	// Creamos el metodo consultarTelefonoCelular se tipo String[] donde guardaremos en un array los números telegfonicos obtenidos 
    	public static String[] consultarTelefonoCelular(Connection connection, int idLocal) throws SQLException{
    		
    		// Ejecutamos una consulta parametrizada. Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
            String queryTelefonoCelular = "SELECT * FROM tblTerceros WHERE idLocal = ? AND ISNUMERIC([telefonoCelular]) = 1 AND LEN([telefonoCelular]) = 10 AND [telefonoCelular] NOT LIKE '%.%' ";
            PreparedStatement statement = connection.prepareStatement(queryTelefonoCelular);
            
         // Asignamos los valores de los parámetros en la consulta
            statement.setInt(1, idLocal);
            
         // Se ejecuta la consulta utilizando el método executeQuery() del objeto PreparedStatement y se guarda el resultado en el objeto ResultSet.
            ResultSet resultSet = statement.executeQuery();
            
            List<String> numerosCelular = new ArrayList<String>();
            
            // Recorremos los resultados de la consulta usando el método next() para recorrer fila por fila los registros obtenidos en la consulta
            while (resultSet.next()) {
            	
                // Utilizamos los métodos getInt() y getString() del ResultSet para obtener los valores de cada columna en el registro actual
                int idLocales = resultSet.getInt("IDLOCAL");
                long telefonoCelular = resultSet.getLong("telefonoCelular");
                
                //Le asignamos el prefijo 57 a cada numero para que posteriormente pueda se usado en el IF de validacion de formato de numero celular (12 números)
                String numeroCelularConPrefijo = "57" + telefonoCelular;
                // Mostramos los valores por consola
                System.out.println("Query 4 - IDlocal: " + idLocales + "  telefonoCelular: " + numeroCelularConPrefijo );
                
                numerosCelular.add(String.valueOf(numeroCelularConPrefijo));
            }

            // Cerramos los recursos utilizados para la consulta
            resultSet.close();
            statement.close();
            
            System.out.println("numerosCelular :" + numerosCelular );
            
            return numerosCelular.toArray(new String[0]);
    	}
}// FIN DBAQUAMOVIL























