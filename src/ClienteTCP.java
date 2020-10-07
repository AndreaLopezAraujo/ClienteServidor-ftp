import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;

class ClienteTCP {

	public final static String ERROR = "ERROR";
	public static final String READY = "READY";
	public final static String OK = "OK";
	public final static String HOLA = "HOLA";
	public static int puerto = 29000;
	public final static String IP = "localhost";
	static int id=0;
	
	public static String calcMD5(String path) throws Exception{
        byte[] buffer = new byte[8192];
        MessageDigest md = MessageDigest.getInstance("MD5");

        DigestInputStream dis = new DigestInputStream(new FileInputStream(new File(path)), md);
        try {
            while (dis.read(buffer) > 0);
        }finally{
            dis.close();
        }
        id=(int) Math.random()*25;
        byte[] bytes = md.digest();

        return DatatypeConverter.printBase64Binary(bytes);
	}

	public static void main(String[] args){
		id=(int) (Math.random()*25+1);
	    InputStream in;
	    int bufferSize=0;
	    String linea;
		
		try {
		
			Socket socket = new Socket(IP, puerto);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			bufferSize = socket.getReceiveBufferSize();
	        in = socket.getInputStream();
	        DataInputStream recibido = new DataInputStream(in);
	        
	        // Salido inicial
	        System.out.println("Enviando " + HOLA + "...");
			pw.println(HOLA);
			linea = recibido.readUTF();
			System.out.println("Respuesta del servidor: " + linea);
			if(!linea.equals(OK)){
				socket.close();
				throw new Exception("No hubo respuesta del servidor.");
			}
			
			// Se informa que se esta listo para recibir el archivo
			pw.println(READY);
			System.out.println("Listo para la recepcion del archivo");
			
	        // Recibe el nombre del archivo que se va a recibir y recibe el archivo
	        String name = recibido.readUTF();
	        String ar="./data/recibido_del_cliente_"+ id+"_el_archivo_"+ name;
	        OutputStream output = new FileOutputStream(ar);
	        System.out.println("El nombre del archivo es " + name);
	        pw.println(OK);
	        long size = recibido.readLong();
	        pw.println(OK);
	        byte[] buffer = new byte[bufferSize];
	        int read;
	        System.out.println("Se inicia con la recepcion del archivo");
	        while(size > 0 && (read = recibido.read(buffer)) > 0){
	            output.write(buffer, 0, read);
	            size-=read;
	            System.out.println("Recibiendo el archivo" + read);
	        }
	        output.close();
	        System.out.println("Se termino de recibir el archivo al cliente: "+id);
	        
	        pw.println(OK);
	        // Recibe el hash del archivo y lo compara con el hash
	        linea = recibido.readUTF();
	        System.out.println("El hash MD5 recibido es: " + linea);
	        if(linea.equals(calcMD5(ar))) {
	        	pw.println(OK);
	        	System.out.println("Los hash coinciden");
	        }
	        else {
	        	pw.println(ERROR);
	        	System.out.println("Los hash no coinciden");
	        }
	        socket.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}