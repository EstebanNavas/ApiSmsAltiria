package prueba_sms;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;


public class AppThread implements Runnable {
	
    private int xIdLocal;
    private int xIdPeriodo;
    
    
    // Creamos un cosntructor de la clase AppThread y le pasamos como parametros las varibales xIdLocal y xIdPeriodo
    public AppThread(int xIdLocal, int xIdPeriodo) {
        this.xIdLocal = xIdLocal;
        this.xIdPeriodo = xIdPeriodo;
    }
	

	public void run() {
		
		
		
		
		 // Declaramos e inicializamos las variables locales xIdLocal y xIdPeriodo
		
//			int xIdLocal = new Integer(args[0]).intValue();
//			int xIdPeriodo = new Integer(args[1]).intValue();	
		
//			 int xIdLocal = 101;
//			 int xIdPeriodo = 202304;
			
			//Obtenemos la conexion a la base de datos dbaquamovil
		     Connection connectionAquamovil =  conexionSQLaquamovil.getConexionAquamovil();
		     
		     String[] xNumerosCelularArr =null; // aquamovil
		     String razonSocial= ""; // aquamovil
		     String nombrePeriodo = ""; // aquamovil
		     String fechaConRecargo = "";// aquamovil
		     //String xTextoMensaje="";
		     
		        try {
		        	
			        // Obtenemos la conexión a la base de datos DBMailmarketing	  
			        Connection connectionMailMarketing = null;
			        
			        String textoSMS= "";  //Acá guardamos el texto obtenido en el método consultarTextoSMS de la clase BDMailMarketing

				    int xidCampaign = 0;
				    int xIdPlantilla = 0;
				    int xIdMaximoReporte = 0;

				    try {
			            
			            connectionMailMarketing = conexionSQLMailMarketing.getConexionMailMarketing();

			           
			            //Obtenemos la consultas a la base de datos BDMailMarketing con sus respectivos métodos 	
			            textoSMS = BDMailMarketing.consultarTextoSMS(connectionMailMarketing, xIdLocal);  // Se obtiene el texto del SMS de la base de datos
			            xidCampaign = BDMailMarketing.consultarIdCampaign(connectionMailMarketing, xIdLocal);
			            xIdPlantilla = BDMailMarketing.consultarIdPlantilla(connectionMailMarketing, xIdLocal);
			            xIdMaximoReporte = BDMailMarketing.obtenerMaximoReporte(connectionMailMarketing);

			        

			            if (!connectionMailMarketing.isClosed()) { // Acá verificamos si la conexión con la base de datos connectionMailMarketing NO está cerrada (osea está abierta)
			             
			            } else {
			                System.out.println("Error: La conexión a la base de datos está cerrada");// SI está cerrada generamos este mensaje
			            }
			        } catch (SQLException e) { // Se captura en el catch si se produjo una excepcion "SQLException"
			            System.out.println("Excepción al establecer la conexión con la base de datos"); 
			            e.printStackTrace();
			        } finally {
			            try {
			                if (connectionMailMarketing != null && !connectionMailMarketing.isClosed()) { //Acá verificamos si la conexion es diferente a null y si no está cerrada, procedemos a cerrar la coenxión
			                    connectionMailMarketing.close();
			                }
			            } catch (SQLException e) { // Capturamos en el catch si se produjo alguna excepcion al cerrar la conexión a la base de datos connectionMailMarketing
			                System.out.println("Excepción al cerrar la conexión con la base de datos");
			                e.printStackTrace();
			            }
			        }
				    
				    
				    
				    
				    //Obtenemos la consultas a la base de datos bdaquamovil con sus respectivos métodos 	        
		            razonSocial = bdaquamovil.consultarRazonSocial(connectionAquamovil, xIdLocal);
		            nombrePeriodo = bdaquamovil.consultarNombrePeriodo(connectionAquamovil, xIdLocal, xIdPeriodo);
		            fechaConRecargo =  bdaquamovil.consultarFechaConRecargo(connectionAquamovil, xIdLocal, xIdPeriodo);	    
		            
		            // Array celulares
		            xNumerosCelularArr = bdaquamovil.consultarTelefonoCelular(connectionAquamovil, xIdLocal);
		            
		            
		            // Reemplazamos los parámetros del texto anteriormente obtenidos en la variable textoSMS
		            textoSMS = textoSMS.replaceFirst("xxx", razonSocial)
		                               .replaceFirst("xxx", nombrePeriodo)
		                               .replaceFirst("xxx", fechaConRecargo);
		            
		            // Se cambian los "/" por "-" para que pueda tener un formato valido a la hora de verificar la fecha 
		            fechaConRecargo = fechaConRecargo.replace("/", "-");
		            
		            // Obtenemos la fecha actual
		            LocalDate fechaActual = LocalDate.now();
		            
		            // Obtenemos la fechaConRecargo como LocalDate
		            LocalDate fechaRecargo = LocalDate.parse(fechaConRecargo);
		            
		            // Validamos si la fechaRecargo es menor a la fecha actual 
		            if(fechaRecargo.isBefore(fechaActual)) {
		            	System.out.println("Fecha con recargo es menor a la fecha actual: " + fechaConRecargo);
		            	return;
		            }
		             
		            
		            
		 
		            // Recorre array celulares , los textos los arma antes
					for (int i = 0; i < xNumerosCelularArr.length; i++) { // Recorremos cada numero celular del array 
						

						//Por cada iteración creamos una instancia de la clase Main llamada obj1 y le pasamos como argumento xNumerosCelularArr[i] y  textoSMS
						Main obj1 = new Main(xNumerosCelularArr[i], textoSMS);

						//Llamamos al método EnviaSms del objeto obj1 y le pasamos como argumento xNumerosCelularArr[i] y  textoSMS
						obj1.EnviaSms(xNumerosCelularArr[i], textoSMS);
						
						//Despues del envio de cada SMS se guarda un registro en la tabla tblMailMarketingReporte 
						try {
						
						BDMailMarketing.ingresaReporte(connectionMailMarketing, xIdLocal, xIdMaximoReporte, xidCampaign,
								xIdPlantilla, xNumerosCelularArr[i], textoSMS)
						;
						
						}catch(SQLException e) {// Capturamos en el catch alguna excepción que pueda ocurrir al guardar el registro en la DB
							System.out.println("Excepción al guardar el registro en la DB");
							e.printStackTrace();
						}


						System.out.println("Registro guardado éxitosamente");
						System.out.println("El texto es : " + textoSMS);
						

					}     
		           
		           
		        } catch (SQLException e) {
		            e.printStackTrace();
		        } finally {
		            try {
		            	connectionAquamovil.close();
		            } catch (SQLException e) {
		                e.printStackTrace();
		            }
		        }


		}//FIN DEL main ******

		
	}
	
	


