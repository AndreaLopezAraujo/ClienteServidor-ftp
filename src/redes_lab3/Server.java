package redes_lab3;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

public class Server {

	public Server() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		
		final String FILE100 = "./src/datos/ilovepdf_merged.pdf";
		final String FILE250 = "./src/datos/file.bin";
		int PORT = 5555;
		Stack<ServerThread> threads = new Stack<>();
		
		try (ServerSocket serverSocket = new ServerSocket(PORT)){
			Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		    System.out.println("Pon tamano del archivo:");
		    int tamanoArchivo = Integer.parseInt(scanner.nextLine());  // Read user input
		    System.out.println("Pon numero Clientes:");
		    int numClientes = Integer.parseInt(scanner.nextLine());
		    //BufferedReader reader;
		    String file = null;
		    if(tamanoArchivo == 100) {
		    	//reader = new BufferedReader(new FileReader(FILE100));
		    	file = FILE100;
		    }
		    else {
		    	//reader = new BufferedReader(new FileReader(FILE250));
		    	file = FILE250;
		    }
		    
			
			System.out.println("Server listening on port " + PORT);
			
			while(true) {
				Socket socket = serverSocket.accept();
				threads.add(new ServerThread(socket, file));
				if(threads.size() >= numClientes) {
					while(threads.size() > 0) {
						threads.pop().start();
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
