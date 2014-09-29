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
    public static File myFile = new File("d:/Server/ServerFile.txt");

    public static void main(String[] args) throws IOException {

        ServerSocket servsock = new ServerSocket(port);

        while (true) {
            Socket sock = servsock.accept();

            byte[] msgarray = new byte[7];

            sock.getInputStream().read(msgarray);

            String readableMsg = new String(msgarray, "UTF-8");

            System.out.println(readableMsg);

            if (readableMsg.equalsIgnoreCase("RECEIVE")) {
                System.out.println("made it here1");
                SendToClient(sock);
            } else if (readableMsg.equals("SEND   ")) {

                System.out.println("NO");
                ReceiveFromClient(sock);
            }
        }

    }

    public static void SendToClient(Socket sock) throws IOException {

        System.out.println("Made it here");
        byte[] mybytearray = new byte[(int) myFile.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(mybytearray, 0, mybytearray.length);
        OutputStream os = sock.getOutputStream();
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
        sock.close();
    }

    public static void ReceiveFromClient(Socket sock) throws IOException {

        byte[] mybytearray = new byte[1024];
        InputStream is = sock.getInputStream();
        FileOutputStream fos = new FileOutputStream("d:/Server/UploadToHere/Received-file.zip");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int bytesRead = is.read(mybytearray, 0, mybytearray.length);
        bos.write(mybytearray, 0, bytesRead);
        bos.close();
        sock.close();

    }

}