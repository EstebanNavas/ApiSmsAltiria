package prueba_sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class sms_conURL {
	
	

	    
	public static void main(String[] args) {
		
		
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
		

		
		
		// Array de números telefonicos.
		String[] numerosTelefonicos = new String[1];
		
		numerosTelefonicos[0] = "573103593237";
//		numerosTelefonicos[1] = "573217048602";
//		numerosTelefonicos[2] = "573187945852";
		
	
	

        
		// Consulta de crédito disponible
		parametersList.clear();
		parametersList.add(new BasicNameValuePair("cmd", "getcredit")); // Comando para validar crédito disponible
		parametersList.add(new BasicNameValuePair("login", "noe.herrera@mobile-tic.com"));
		parametersList.add(new BasicNameValuePair("passwd", "M7Tc9pXbZh3d"));

		try {
			//Se configura la entidad de la solicitud POST utilizando el método setEntity(). Se crea una instancia de UrlEncodedFormEntity pasando la lista de parámetros parametersList
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
//                creditoDisponible = Double.parseDouble(resp);
				String regex = "credit\\(\\d+\\):(\\d+\\.\\d+)";
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher1 = pattern.matcher(resp);
				
				if (matcher1.find()) {
					String creditString = matcher1.group(1);
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

		int totalMensajes = numerosTelefonicos.length; // Cantidad total de mensajes a enviar
		int costoPorMensaje = 1; // Costo en créditos de cada mensaje
		double creditosNecesarios = totalMensajes * costoPorMensaje;

		// Verificar si hay crédito suficiente para enviar todos los mensajes
		if (creditoDisponible < creditosNecesarios) {
			System.out.println("Error: No hay suficiente crédito disponible para enviar todos los mensajes.");
			return;

		}
		
		

        // Recorre el array de numerosTelefonicos
        for (int i = 0; i < numerosTelefonicos.length; i++) {
        	String numero = numerosTelefonicos[i]; // En la variable numero guardamos cada número del array para validar posteriormente con el if.
            String regex = "^[0-9]{12}$";  // en la variable regex le especificamos el formato que debe de tener.
            
            // Validamos si el formato de cada número cumple o no con el requisito de (12 numeros continuos sin letras).
            if (!Pattern.matches(regex, numero)) {
                System.out.println("Error: El número telefónico '" + numero + "' no cumple con el formato adecuado.");
                continue;
            }
            
            parametersList.clear();// Limpiamos la lista de parámetros antes de agregar los nuevos
        	
            parametersList.add(new BasicNameValuePair("cmd", "sendsms"));
    		parametersList.add(new BasicNameValuePair("login", "noe.herrera@mobile-tic.com"));
    		parametersList.add(new BasicNameValuePair("passwd", "M7Tc9pXbZh3d"));
            parametersList.add(new BasicNameValuePair("dest", numerosTelefonicos[i]));
            parametersList.add(new BasicNameValuePair("msg", "Estimado cliente acceda a la promoción en el siguiente link: {$9355$} "));
            parametersList.add(new BasicNameValuePair("landing", "1"));
            parametersList.add(new BasicNameValuePair("params", "0"));
            
           
            
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
    					System.out.println(resp);
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
            
        }  //Fin del FOR
	

		
	}
	
}
