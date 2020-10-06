import java.util.Date;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Log {
	Date dia;
	String archivo;
	int cliente;
	boolean exito;
	int tiempo;
	String hashCliente;
	String hashServidor;
	int paquetesEnviados;
	int paquetesRecividos;
	int bytesEnviados;
	int bytesResividos;

	Log(String archivo, int cliente, int tiempo, int paquetesEnviados, int bytesEnviados,
			String hashServidor) {
		dia = new Date();
		this.archivo = archivo;
		this.cliente = cliente;
		this.tiempo = tiempo;
		this.paquetesEnviados = paquetesEnviados;
		this.bytesEnviados = bytesEnviados;
		this.hashServidor = hashServidor;
		crearArchivo();

	}

	public void clienteLog(int paquetesRecividos, int bytesResividos, String hashCliente) {
		this.hashCliente = hashCliente;
		this.paquetesRecividos = paquetesRecividos;
		this.bytesResividos = bytesResividos;
		exito = comparar();
	}

	public boolean comparar() {
		if (hashCliente == hashServidor) {
			return true;
		}
		return false;
	}

	public void crearArchivo() {
		try {
			String ruta = "./data/logCliente" + cliente + ".txt";
			File file = new File(ruta);
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			String diax = "El archivo fue enviado: " + dia;
			String pas = "\n";
			String nombreArchivo = "El nombre del archivo enviado fue: " + this.archivo;
			String enCliente = "El archivo fue enviado al cliente: " + this.cliente;
			String envio = "El envio del archivo duro: " + this.tiempo;
			String inf = "-----Informacion Servidor-----";
			String paq = "El numero de paquetes enviados fueron: " + this.paquetesEnviados;
			String byt = "El numero de bytes enviados fueron: " + this.bytesEnviados;
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(diax + pas + nombreArchivo + pas + enCliente + pas + envio + pas + inf + pas + paq + pas + byt);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
