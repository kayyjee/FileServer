package filesend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    
    public static int port = 7005;
    static String fileName;

    public static void main(String[] args) throws IOException {

        ServerSocket servsock = new ServerSocket(port);
        System.out.println("Listening on Port " + port);
        
        ServerSocket servsock2 = new ServerSocket(7006);

        while (true) {
            Socket sock = servsock.accept();
            System.out.println("Connection Established");
            
            Socket sock2 = servsock2.accept();
            
            byte[] msgArray = new byte[7];
            //Allows for a file name to be up to 50 characters (including extension)
            byte[] nameArray = new byte[50];

            sock.getInputStream().read(msgArray);
            String readableMsg = new String(msgArray, "UTF-8");
            readableMsg = readableMsg.trim();
            
            sock2.getInputStream().read(nameArray);
            fileName = new String(nameArray, "UTF-8");
            fileName = fileName.trim();

            System.out.println(readableMsg);
            if (readableMsg.equals("RECEIVE")) {
                SendToClient(sock);
            } else if (readableMsg.equals("SEND")) {
                ReceiveFromClient(sock);
            }
            break;
        }

    }

    public static void SendToClient(Socket sock) throws IOException {
        File myFile = new File("./ServerFiles/" + fileName);
        byte[] mybytearray = new byte[(int) myFile.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = sock.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        System.out.println("File Sent");
        os.flush();
        sock.close();
    }

    public static void ReceiveFromClient(Socket sock) throws IOException {
        byte[] myByteArray = new byte[1];
        InputStream is = sock.getInputStream();
        FileOutputStream fos = new FileOutputStream("./ServerFiles/" + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        
        
        int bytesRead = is.read(myByteArray, 0, myByteArray.length);
        
        int len=0;
        while((len = is.read(myByteArray))!= -1){
            
    
        bos.write(myByteArray);
    }
        
        System.out.println("File Received");
        bos.close();
        sock.close();

    }

}
