import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.xml.bind.DatatypeConverter;

public class ServerMaestroTCP {
	
	private static ServerSocket ss;
	static String FILE100 = "./data/100MiB.txt";
	static String FILE250 = "./data/250MiB.txt";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		int p = 29000;
		int numThreads = 25;
		ThreadPoolExecutor executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(numThreads);
		
		// Escoge el archivo que se va a enviar
		
		File file = new File(FILE100);
		
		String h = calcMD5(FILE100);
		
//		if(Math.random() > 0.5)
//			archivo = new File(FILE100);
//		else
//			archivo = new File(FILE250);
		
		// Crea el archivo de log
		
        DateFormat df = new SimpleDateFormat("dd-MM-yy_HH-mm-ss"); 
        long miliSec = System.currentTimeMillis();
        Date current = new Date(miliSec);
		File log = null;
		String ruta = "./data/prueba_"+ df.format(current) + ".txt";
		   
        log = new File(ruta);
        if (!log.exists()) {
            log.createNewFile();
        }
        FileWriter fw = new FileWriter(log);
        fw.close();

        ServerTCP.init(file, h, log);
        
		// Crea el socket que escucha en el puerto seleccionado
		ss = new ServerSocket(p);
		int i = 1;
		while(true){
			try {
				Socket sc = ss.accept();
				System.out.println("Cliente aceptado.");
				ServerTCP d = new ServerTCP(i, sc);
				executor.execute(d);
				i++;
			} catch (Exception e) {
				ss.close();
				System.out.println("Error creando el socket cliente.");
				e.printStackTrace();
			}
		}
		
	}
	
	public static String calcMD5(String path) throws Exception {
        byte[] buffer = new byte[8192];
        MessageDigest md = MessageDigest.getInstance("MD5");

        DigestInputStream dis = new DigestInputStream(new FileInputStream(new File(path)), md);
        try {
            while (dis.read(buffer) > 0);
        }finally{
            dis.close();
        }

        byte[] bytes = md.digest();

        return DatatypeConverter.printBase64Binary(bytes);
	}
}
