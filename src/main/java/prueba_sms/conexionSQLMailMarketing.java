package prueba_sms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionSQLMailMarketing {
	
	public static Connection getConexionMailMarketing() {
		
		String conexionUrl = "jdbc:sqlserver://localhost:1434;"
				+ "database=BDMailMarketing;"
				+ "user=sa;"
				+ "password=0424;"
				+ "loginTimeout=30";
		
		try {
			Connection con = DriverManager.getConnection(conexionUrl);
			return con;
		} catch(SQLException ex){
			System.out.println(ex.toString());
			return null;
		}
	}

}
