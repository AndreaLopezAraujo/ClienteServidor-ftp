package redes_lab3;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ServerThread extends Thread {

	private Socket socket;
	private String fileName;
	String line;
	String holder;
	String clientWord;
	int bytNumber;

	public ServerThread(Socket pSocket, String pFile) {
		socket = pSocket;
		fileName = pFile;
		line = null;
		holder = null;
		clientWord = null;
		bytNumber = 0;
	}

	public void run() {
		try {
			DataInputStream inFromClient = new DataInputStream(socket.getInputStream());
			// create output stream, attached to socket
			OutputStream outToClient = socket.getOutputStream();
			// read in line from socket
			clientWord = inFromClient.readUTF();
			System.out.println(clientWord);
			if (clientWord.equals("hello")) {
				
				
				try {
					MessageDigest md = MessageDigest.getInstance("MD5");
					File file = new File(fileName);
					FileInputStream in = new FileInputStream(file);
					DigestInputStream dis = new DigestInputStream(in, md);
					//MSG ES EL ID DEL CLIENTE
					String msg = "" + ((InetSocketAddress)socket.getRemoteSocketAddress()).getPort();;					
					outToClient.write(msg.getBytes());
					
					String fileSize = Long.toString(file.length());
					outToClient.write(fileSize.getBytes());
					
					byte[] outputByte = new byte[4096];
					int count;
					while ((count = dis.read(outputByte)) > 0) {
						outToClient.write(outputByte, 0, count);
					}
					
					
					clientWord = inFromClient.readUTF();
					if(clientWord.equals("OK")) {
						System.out.println("WOO");
						MessageDigest digest = dis.getMessageDigest();
						dis.close();
						byte[] md5 = digest.digest(); 
						outToClient.write(md5, 0, md5.length); //16 bytes
						
						clientWord = inFromClient.readUTF();
						if(clientWord.equals("GOOD")) {
							System.out.println("DONE");
						}
					}
					
				
					// Always close files.
					outToClient.close();
					in.close();
					dis.close();
				} catch (FileNotFoundException ex) {
					System.out.println("Unable to open file '" + fileName + "'");
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
