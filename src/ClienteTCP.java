import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import java.io.*;

class ClienteTCP{
	
	
	static String m = "0";
	public Socket funcConect(String string) throws UnknownHostException, IOException
	{
		
		Socket socket = new Socket(string, 8080);
		try {
			

			InputStream is = socket.getInputStream();
			DataInputStream entrada = new DataInputStream(is);
			OutputStream output = socket.getOutputStream(); 
			DataOutputStream salida = new DataOutputStream(output);

			salida.writeUTF("Hola");
			String mensajeRecibido = entrada.readUTF();

			if(mensajeRecibido.equals("Hola"))
			{  	 
				salida.writeUTF("Listo para recibir datos");
			}

			mensajeRecibido = entrada.readUTF();
			System.out.println("Cliente nmo: "+mensajeRecibido);
			m = mensajeRecibido;
			
			
		}
			catch (Exception e) {
				e.printStackTrace();
			}
		
		return socket;
		
	}
	

	public void funcMandar( int i, String string2, Socket socket) throws IOException, NoSuchAlgorithmException {
		
		InputStream is = socket.getInputStream();
		DataInputStream entrada = new DataInputStream(is);
		OutputStream output = socket.getOutputStream(); 
		DataOutputStream salida = new DataOutputStream(output);
		
		
		if(Integer.parseInt(m) == 1)
		{
			String ss = "250MB".equals(string2) ? "2" : "1";
			salida.writeUTF(i+","+ss);   
		}

		
		
		
	}
	
	
	
	public void funcRecibir( Socket socket) throws IOException, NoSuchAlgorithmException
	{
		InputStream is = socket.getInputStream();
		DataInputStream entrada = new DataInputStream(is);
		OutputStream output = socket.getOutputStream(); 
		DataOutputStream salida = new DataOutputStream(output);

		byte[] b = new byte[8192];
		FileOutputStream fr = new FileOutputStream("./data/ArchivoCliente" + (int)(Math.random()*100) +".txt");

		is.read(b, 0, b.length);
		fr.write(b, 0, b.length);


		byte[] hash = new byte[32];
		is.read(hash, 0, hash.length);

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(b);
		byte[] digest = md.digest();
		String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		byte[] h = myHash.getBytes();

		if( Arrays.equals(h, hash))
		{

			salida.writeUTF("Archivo entregado e integridad del archivo verificada");
		}
	}
}