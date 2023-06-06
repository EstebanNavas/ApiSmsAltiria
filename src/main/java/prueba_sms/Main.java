package prueba_sms;
//Copyright (c) 2018, Altiria TIC SL

//All rights reserved.
//El uso de este código de ejemplo es solamente para mostrar el uso de la pasarela de envío de SMS de Altiria
//Para un uso personalizado del código, es necesario consultar la API de especificaciones técnicas, donde también podrás encontrar
//más ejemplos de programación en otros lenguajes de programación y otros protocolos (http, REST, web services)
//https://www.altiria.com/api-envio-sms/

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Main {
	
	//Declaramos las variiables de instancia 
	String numeroCel;
	String textoSms;

	
	// En el constructor de la clase Main inicializamos las variables de instancia numeroCel y textoSms
	 Main(String numeroCel, String textoSms){  
			this.numeroCel = numeroCel; // Utilizamos la palabra clave "this"  para hacer referencia a las variables de instancia de la clase
			this.textoSms=textoSms;
	   } 	 


	public static void main(String[] args) {
		
		 // Declaramos e inicializamos las variables locales xIdLocal y xIdPeriodo
		
		int xIdLocal = new Integer(args[0]).intValue();
		int xIdPeriodo = new Integer(args[1]).intValue();	
		
//		 int xIdLocal = 101;
//		 int xIdPeriodo = 202304;
		
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




	
	    // MÉTODO PARA ENVIAR EL SMS 
	public  void EnviaSms(String xNumeroCelular, String xTextoSMS) {
		
		// Se fija el tiempo máximo de espera para conectar con el servidor (5000)
		// Se fija el tiempo máximo de espera de la respuesta del servidor (60000)
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(60000).build();

		// Se inicia el objeto HTTP
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(config);
		CloseableHttpClient httpClient = builder.build();

		// Se fija la URL sobre la que enviar la petición POST
		HttpPost post = new HttpPost("https://www.altiria.net:8443/api/http");

		// Se crea la lista de parámetros a enviar en la petición POST
		List<NameValuePair> parametersList = new ArrayList<NameValuePair>();
        
		// Consulta de crédito disponible
		parametersList.clear();
		parametersList.add(new BasicNameValuePair("cmd", "getcredit")); // Comando para validar crédito disponible
		parametersList.add(new BasicNameValuePair("login", "noe.herrera@mobile-tic.com"));
		parametersList.add(new BasicNameValuePair("passwd", "M7Tc9pXbZh3d"));

		try {
			post.setEntity(new UrlEncodedFormEntity(parametersList, "UTF-8"));
		} catch (UnsupportedEncodingException uex) {
			System.out.println("ERROR: Codificación de caracteres no soportada");
			return;
		}

		CloseableHttpResponse creditResponse = null;
		double creditoDisponible = 0.0;

		try {
			System.out.println("Enviando petición de consulta de crédito");
			creditResponse = httpClient.execute(post);
			String resp = EntityUtils.toString(creditResponse.getEntity()); // Se utiliza la clase EntityUtils para convertir la entidad de la respuesta en  una cadena de texto.
																	

			if (creditResponse.getStatusLine().getStatusCode() != 200) {
				System.out.println("ERROR: Código de error HTTP: " + creditResponse.getStatusLine().getStatusCode());
				System.out.println(
						"Compruebe que ha configurado correctamente la dirección/URL suministrada por Altiria");
				return;
			} else {
				// Analizar la respuesta para obtener el crédito disponible
//                
				String regex = "credit\\(\\d+\\):(\\d+\\.\\d+)";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(resp);
				
				if (matcher.find()) {
					String creditString = matcher.group(1);
					System.out.println("El crédito es : OK credit(" + creditString + "):");
					creditoDisponible = Double.parseDouble(creditString);
				} else {
					System.out.println("ERROR: No se pudo obtener el crédito disponible de la respuesta");
					return;
				}
			}
		} catch (IOException ex) {
			System.out.println("ERROR al enviar la petición: " + ex.getMessage());
		} finally {
			if (creditResponse != null) {
				try {
					creditResponse.close();
				} catch (IOException ex) {
					System.out.println("ERROR al cerrar la respuesta: " + ex.getMessage());
				}
			}
		}

		int totalMensajes = 1; // Cantidad total de mensajes a enviar
		int costoPorMensaje = 1; // Costo en créditos de cada mensaje
		double creditosNecesarios = totalMensajes * costoPorMensaje;

		// Verificar si hay crédito suficiente para enviar todos los mensajes
		if (creditoDisponible < creditosNecesarios) {
			System.out.println("Error: No hay suficiente crédito disponible para enviar todos los mensajes.");
			return;

		}
        

        // Recorre el array de numerosTelefonicos
 
        	String numero = xNumeroCelular; // En la variable numero guardamos cada número del array para validar posteriormente con el if.
            String regex = "^[0-9]{12}$";  // en la variable regex le especificamos el formato que debe de tener.
            
            // CONSULTAR EL MAXIMOREPORTE
            
            // Validamos si el formato de cada número cumple o no con el requisito de (12 numeros continuos sin letras).
            if (!Pattern.matches(regex, numero)) {
                System.out.println("Error: El número telefónico '" + numero + "' no cumple con el formato adecuado.");
 
            }
            
            parametersList.clear();// Limpiamos la lista de parámetros antes de agregar los nuevos
        	
            parametersList.add(new BasicNameValuePair("cmd", "sendsms"));
    		parametersList.add(new BasicNameValuePair("login", "noe.herrera@mobile-tic.com"));
    		parametersList.add(new BasicNameValuePair("passwd", "M7Tc9pXbZh3d"));
            parametersList.add(new BasicNameValuePair("dest", numero));
//            parametersList.add(new BasicNameValuePair("msg", "Mensaje de prueba DB" + razonSocial));
            parametersList.add(new BasicNameValuePair("msg", xTextoSMS));
            
            try {
    			// Se fija la codificacion de caracteres de la peticion POST
    			
    			post.setEntity(new UrlEncodedFormEntity(parametersList, "UTF-8"));
    		} catch (UnsupportedEncodingException uex) {
    			System.out.println("ERROR: codificación de caracteres no soportada");
    		}

    		CloseableHttpResponse response = null;

    		try {
    			System.out.println("Enviando petición");
    			// Se envía la petición
    			response = httpClient.execute(post);
    			// Se consigue la respuesta
    			String resp = EntityUtils.toString(response.getEntity());

    			// Error en la respuesta del servidor
    			if (response.getStatusLine().getStatusCode() != 200) {
    				System.out.println("ERROR: Código de error HTTP:  " + response.getStatusLine().getStatusCode());
    				System.out.println("Compruebe que ha configurado correctamente la direccion/url ");
    				System.out.println("suministrada por Altiria");
    				return;
    			} else {
    				// Se procesa la respuesta capturada en la cadena 'response'
    				if (resp.startsWith("ERROR")) {
    					System.out.println(resp);
    					System.out.println("Código de error de Altiria. Compruebe las especificaciones");
    				} else
    					// Se muestra por consolola el envío éxitoso del SMS
    					System.out.println("Mensaje enviado exitosamente " + resp);

    			}
    		} catch (Exception e) {
    			System.out.println("Excepción");
    			e.printStackTrace();
    			return;
    		} finally {
    			// En cualquier caso se cierra la conexión
    			post.releaseConnection();
    			if (response != null) {
    				try {
    					response.close();
    				} catch (IOException ioe) {
    					System.out.println("ERROR cerrando recursos");
    				}
    			}
    		}
 
		
	} 
}// FIN DE LA CLASE MAIN
