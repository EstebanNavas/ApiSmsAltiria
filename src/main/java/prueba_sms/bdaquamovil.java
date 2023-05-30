package prueba_sms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class bdaquamovil {

    public static void main(String[] args) {
        // Obtenemos la conexión a la base de datos
        Connection connection = conexionSQLaquamovil.getConexionAquamovil();

        // Verificamos si la conexión es exitosa o no
        if (connection != null) {
            try {
            	
            	//Llamamos a los metodos
                consultarLocalesPorId(connection, 100);
                
                consultarPeriodoFechaPago(connection, 100, 202304);
                
                consultarTelefonoCelular(connection, 100);

                // Se cierra la conexión a la base de datos
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }// FIN MAIN

                                             // EXTRAER EL ID DEL LOCAL Y SU RAZÓN SOCIAL 
    private static void consultarLocalesPorId(Connection connection, int idLocal) throws SQLException {
    	
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
            String razonSocial = resultSet.getString("razonSocial");

            // Mostramos los valores por consola
            System.out.println("Query 1 - IDlocal: " + idLocales + "  razonSocial: " + razonSocial);
        }

        // Cerramos los recursos utilizados para la consulta
        resultSet.close();
        statement.close();
    }
    
                                        // EXTRAER ULTIMO PERIODO Y FECHA DE PAGO DEL LOCAL 
    	private static void consultarPeriodoFechaPago(Connection connection, int idLocal, int idPeriodo ) throws SQLException{
    		
    		// Ejecutamos una consulta parametrizada. Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
            String queryPeriodoFechaPago = "SELECT * FROM bdaquamovil.dbo.tblDctosPeriodo WHERE idLocal = ? AND idPeriodo = ?";
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
                String nombrePeriodo = resultSet.getString("nombrePeriodo");
                String fechaConRecargo = resultSet.getString("fechaConRecargo");

                // Mostramos los valores por consola
                System.out.println("Query 2 - IDlocal: " + idLocales + "  idPeriodo: " + idPeriodos + " nombrePeriodo: " + nombrePeriodo + " fechaConRecargo: " + fechaConRecargo);
            }

            // Cerramos los recursos utilizados para la consulta
            resultSet.close();
            statement.close();
    	}
    	
    	                                            // EXTRAER NÚMEROS DE TELEFONO CELULAR
    	
    	// Creamos el metodo consultarTelefonoCelular se tipo String[] donde guardaremos en un array los números telegfonicos obtenidos 
    	public static String[] consultarTelefonoCelular(Connection connection, int idLocal) throws SQLException{
    		
    		// Ejecutamos una consulta parametrizada. Los signos de interrogación (?) se utilizan como marcadores de posición para los parámetros.
            String queryTelefonoCelular = "SELECT * FROM bdaquamovil.dbo.tblTerceros WHERE idLocal = ? AND ISNUMERIC([telefonoCelular]) = 1 AND LEN([telefonoCelular]) = 10";
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
                System.out.println("Query 3 - IDlocal: " + idLocales + "  telefonoCelular: " + numeroCelularConPrefijo );
                
                numerosCelular.add(String.valueOf(numeroCelularConPrefijo));
            }

            // Cerramos los recursos utilizados para la consulta
            resultSet.close();
            statement.close();
            
            System.out.println("numerosCelular :" + numerosCelular );
            
            return numerosCelular.toArray(new String[0]);
    	}
}// FIN DBAQUAMOVIL























