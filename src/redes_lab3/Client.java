package redes_lab3;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class Client {

    public static void main(String[] args) {
        String temp;
        String displayBytes;
        String id;
       try
       {
       //create input stream
       //create client socket, connect to server
       Socket clientSocket = new Socket("localhost",5555);
       //create output stream attached to socket
       DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
       InputStream inFromServer = clientSocket.getInputStream();
       //create input stream attached to socket



       System.out.println("Send hello");
       //send line to server
       //outToServer.writeBytes(temp);
       outToServer.writeUTF("hello");
       outToServer.flush();
       
       byte[] bytes = new byte[4096];
       
       inFromServer.read(bytes, 0, 5);
       System.out.println(new String(bytes));
       
       id = new String(bytes).trim();

       File file = new File("testing" + id +  ".bin");
       OutputStream os = new FileOutputStream(file); 
       
       inFromServer.read(bytes, 0, 9);
       
       long size = Long.parseLong(new String(bytes).trim());
       
       MessageDigest md5Digest = MessageDigest.getInstance("MD5");
      //read line from server
       //displayBytes = inFromServer.readLine();
       String line = "";
       int count;
       while ((count = inFromServer.read(bytes)) > 0) {
           os.write(bytes, 0, count);
           size -= count;
           if(size <= 0) {
        	   
        	   break;
           }
       }
       os.close();
       
       
       System.out.println("Sending OK");
       outToServer.writeUTF("OK");
       outToServer.flush();
       
       inFromServer.read(bytes, 0, 16); //MD5
       //count = inFromServer.read(bytes);
       StringBuilder hash = new StringBuilder();
       for(int i=0; i< 16 ;i++)
       {
           hash.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
       }
       //Use MD5 algorithm
        
       //Get the checksum
       String checksum = getFileChecksum(md5Digest, file);
       if(checksum.equals(hash.toString())) {
    	   outToServer.writeUTF("GOOD");
       }
       else {
    	   outToServer.writeUTF("BAD");
       }
       outToServer.close();
       inFromServer.close();
       file.delete();
       clientSocket.close();
   }
   catch(Exception ex)
   {
	   ex.printStackTrace();
	   
   }
}
    
    private static String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);
        //Create byte array to read data in chunks
        byte[] byteArray = new byte[4096];
        int bytesCount = 0; 
          
        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };
         
        //close the stream; We don't need it now.
        fis.close();
         
        //Get the hash's bytes
        byte[] bytes = digest.digest();
         
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        //return complete hash
       return sb.toString();
    }
	
}
