package prueba_sms;

import java.sql.Connection;

public class pruebaConexionSQL {
    public static void main(String[] args) {
        Connection connection = conexionSQL.getConexion();

        if (connection != null) {
            System.out.println("Conexión exitosa a la base de datos");
            // Aquí puedes realizar operaciones con la conexión
        } else {
            System.out.println("Error al conectar a la base de datos");
        }
    }
}


