import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

public class ServerTCP {
	
	
	static List<Socket> clients = new ArrayList<Socket>();  
	static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	static int x = 0;

	
	public static void main(String[] args) throws IOException {
	 	
	        	ServerSocket serverSocket = new ServerSocket(8080);
	            System.out.println("Server is listening on port " + 8080);
	            String h="";
	            
	            while(true)
	            {
	            	try {
                    	while (x<4)
                    	{
                    		Socket socket = serverSocket.accept(); 
                    		clients.add(socket);		
                    		System.out.println("new CLient" + clients.size());
                    		
                    			DataInputStream entrada = new DataInputStream(socket.getInputStream());
	        	                OutputStream output = socket.getOutputStream(); 
	        	                DataOutputStream salida = new DataOutputStream(output);
	        	                
	        	                String mensajeRecibido = entrada.readUTF();
	        	                if(mensajeRecibido.equals("Hola"))
	        	                {
	        	      		   
	        	      		    salida.writeUTF("Hola");
	        	      		    
	        	      		    salida.writeUTF(""+clients.size());
	        	      		    socket.setSoTimeout(4000);
	        	                }
                    		x++; 
                    	}
                    	if(x != 0)
    	            	{
                    		DataInputStream entrada = new DataInputStream(clients.get(0).getInputStream());
                    		h=entrada.readUTF();
    	            		String[] w = h.split(",");
    						String opcion = w[1];
    		      		    String numClientesAMandar =  w[0];
    		      		    System.out.println("se va a enviar");
    		      		    envio(opcion,numClientesAMandar);
    		      		    x = 0;
    	            	}
                    }
                    catch (IOException e) {
                    }
	            }
	                    
	                
	}
	public static void enviarMensaje(String solicitud) {
		
	}
	  
	  public static void envio(String fileSelection,String numClientes) throws IOException
	  {
		  System.out.println("holi");
          for(int x = 0; x < Integer.parseInt(numClientes);x++)
          {
        	  OutputStream output = clients.get(x).getOutputStream(); 
        	  DataInputStream entrada = new DataInputStream(clients.get(x).getInputStream());
        	  new Thread(new Runnable() {
	                public void run() {
	                    try {
	                    	
	                    	if(Integer.parseInt(numClientes) > clients.size())
	                  		{
	                  			System.out.println("Numero de clientes menor al especificado.");
	                  		}
	                  		else if(Integer.parseInt(fileSelection) == 1)
	                      	{

	                      		FileInputStream fr = new FileInputStream("./data/100MiB.txt");
	                      		byte b[] = new 	byte[8192];
	                      		fr.read(b, 0, b.length);
	                      		long startTime = System.currentTimeMillis();
	                      		output.write(b, 0, b.length);
	                      		output.write(hasher(b));
	                      		
	                      		String mensajeRecibido = entrada.readUTF();
	                      		System.out.println(mensajeRecibido);
	                      		long endTime = System.currentTimeMillis();
	                      		System.out.println("Tiempo transcurrido: " + (endTime - startTime) + " ms.");

	                      	}
	                      	else if(Integer.parseInt(fileSelection) == 2 )
	                      	{
	                      		//InputStream input = clients.get(x).getInputStream();  	                   
	           	                
	                      		FileInputStream fr = new FileInputStream("./data/250MiB.txt");
	                      		byte b[] = new byte[8192];
	                      		fr.read(b, 0, b.length);
	                      		output.write(b, 0, b.length);
	                      		output.write(hasher(b));
	                      		//En vez de esto que el cliente mande el mensaje
	                      		//System.out.println("Archivo enviado exitosamente.");
	                      		String mensajeRecibido = entrada.readUTF();
	                      		System.out.println(mensajeRecibido);
	                      	}
	                      	else
	                      	{
	                      		System.out.println("Opcion incorrecta");
	                      	}
	                    	
	                    }
	                    catch (IOException | NoSuchAlgorithmException e) {
	                    }
	                }
	            }).start();
          }
          
          
		
	  }
	  
	  static byte[] hasher(byte[] b) throws NoSuchAlgorithmException
	    {


	        MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(b);
		    byte[] digest = md.digest();
		    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
	        
		    byte[] z = myHash.getBytes();
		    return z;
	    }
}

