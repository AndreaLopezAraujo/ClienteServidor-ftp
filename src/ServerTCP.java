import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServerTCP extends Thread{
	
	public static final String OK = "OK";
	public static final String HOLA = "HOLA";
	public static final String READY = "READY";
	public static final String ERROR = "ERROR";
	public static final String REC = "recibio-";
	public static final String ENVIO = "envio-";
	
	static List<Socket> clients = new ArrayList<Socket>();  
	static BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
	static File log;
	static int x = 0;
	private int id;
	private String dlg;
	private static File file;
	private static String hash;
	private Socket sc = null;
	
	public static void init(File pFile, String pHash, File pLog) {
		file = pFile;
		hash = pHash;
		log = pLog;
	}
	
	public ServerTCP(int pId, Socket pSocket) {
		id = pId;
		sc = pSocket;
		dlg = new String("delegado " + pId + ": ");
	}

	public synchronized void escribirMensaje(String pCadena) {

		try {
			FileWriter fw = new FileWriter(log,true);
			fw.write(pCadena + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run() {
		String feedback;
		String linea;
		int count;
		byte[] buffer = new byte[16384];
		try {

			BufferedReader dc = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			FileInputStream fis = new FileInputStream("./data/" + file.getName());
		    BufferedInputStream bis = new BufferedInputStream(fis);
		    DataInputStream dis = new DataInputStream(bis);
		    OutputStream os = sc.getOutputStream();
	        DataOutputStream dos = new DataOutputStream(os);

			/***** Fase 1: Saludo *****/
			linea = dc.readLine();
			if (!linea.equals(HOLA)) {
				dos.writeUTF(ERROR);
				dis.close();
				sc.close();
				throw new Exception(dlg + ERROR + REC + linea +"-terminando.");
			} else {
				dos.writeUTF(OK);
				feedback = dlg + REC + linea + "-continuando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			}

			/***** Fase 2: Envio de el nombre del archivo *****/
			linea = dc.readLine();
			if (!linea.equals(READY)) {
				dos.writeUTF(ERROR);
				dis.close();
				sc.close();
				throw new Exception(dlg + ERROR + REC + linea +"-terminando.");
			} else {
				feedback = dlg + REC + linea + "-continuando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			}
			
			dos.writeUTF(file.getName());
			
			linea = dc.readLine();
			if (!linea.equals(OK)) {
				dos.writeUTF(ERROR);
				dis.close();
				sc.close();
				throw new Exception(dlg + ERROR + REC + linea +"-terminando.");
			} else {
				feedback = dlg + REC + linea + "-continuando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			}
			
			dos.writeLong(file.length());
			
			/***** Fase 3: Envio de el archivo *****/
			linea = dc.readLine();
			if (!linea.equals(OK)) {
				dos.writeUTF(ERROR);
				dis.close();
				sc.close();
				throw new Exception(dlg + ERROR + REC + linea +"-terminando.");
			} else {
				feedback = dlg + REC + linea + "-continuando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			}

			long tiempoInicial = System.currentTimeMillis();
	        while((count = dis.read(buffer)) > 0){
	            dos.write(buffer, 0, count);
	            System.out.println("Enviando datos" + count);
	        }
	        dos.flush();
			long tiempoFinal = System.currentTimeMillis();
			
			/***** Fase 4: Calculo del tiempo de transferencia *****/
			escribirMensaje("Tiempo de transferencia desde el servidor " + id + ": " + (tiempoFinal-tiempoInicial) + "ms.");
			System.out.println("Tiempo de transferencia desde el servidor " + id + ": " + (tiempoFinal-tiempoInicial) + "ms.");
			
			/***** Fase 5: Envio del hash *****/
			linea = dc.readLine();
			if (!linea.equals(OK)) {
				dos.writeUTF(ERROR);
				dis.close();
				sc.close();
				throw new Exception(dlg + ERROR + REC + linea +"-terminando.");
			} else {
				feedback = dlg + REC + linea + "-5continuando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			}
			
			dos.writeUTF(hash);
			dos.flush();
			
			/***** Fase 6: Validacion de la transferencia correcta *****/
			
			linea = dc.readLine();
			if (!linea.equals(OK)) {
				feedback = dlg + REC + linea + " en la transmicion -terminando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			} else {
				feedback = dlg + REC + "transmicion " + linea + "-terminando.";
				escribirMensaje(feedback);
				System.out.println(feedback);
			}
			dis.close();
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
