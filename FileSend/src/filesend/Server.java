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
    public static File myFile = new File("d:/Server/happy.mp3");

    public static void main(String[] args) throws IOException {

        ServerSocket servsock = new ServerSocket(port);
        System.out.println("Listening on Port " + port);

        while (true) {
            Socket sock = servsock.accept();
            System.out.println("Connection Established");
            
            byte[] msgarray = new byte[7];

            sock.getInputStream().read(msgarray);
            String readableMsg = new String(msgarray, "UTF-8");

            System.out.println(readableMsg);
            if (readableMsg.equals("RECEIVE")) {
                SendToClient(sock);
            } else if (readableMsg.equals("SEND   ")) {
                ReceiveFromClient(sock);
            }
            break;
        }

    }

    public static void SendToClient(Socket sock) throws IOException {
        
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

        byte[] myByteArray = new byte[10];
        InputStream is = sock.getInputStream();
        FileOutputStream fos = new FileOutputStream("d:/Server/UploadToHere/Happy.mp3");
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
