/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesend;

import java.io.BufferedInputStream;
import java.util.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public static void main(String[] argv) throws Exception {

        System.out.println("What is the IP Address of the Server?");
        Scanner input = new Scanner(System.in);
        String IP = input.nextLine().toString();
        System.out.println(IP);

        System.out.println("Would you like to SEND or RECEIVE");
        String option = input.nextLine().toString();

        switch (option) {
            case "RECEIVE":

                try {
                    Socket sock = new Socket(IP, 7005);

                    String msg = "RECEIVE";

                    sock.getOutputStream().write(msg.getBytes());

                    byte[] mybytearray = new byte[1024];
                    InputStream is = sock.getInputStream();
                    FileOutputStream fos = new FileOutputStream("d:/Client/Output/Received-file.txt");
                    System.out.println("File has been downloaded to: d:/Client/Output/");
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead = is.read(mybytearray, 0, mybytearray.length);
                    bos.write(mybytearray, 0, bytesRead);
                    bos.close();
                    sock.close();
                } catch (Exception e) {

                }
                break;

            case "SEND":

                try {
                    Socket sock = new Socket(IP, 7005);

                    String msg = "SEND   ";

                    sock.getOutputStream().write(msg.getBytes());

                    File myFile = new File("d:Justin.txt");

                    byte[] mybytearray = new byte[(int) myFile.length()];
                    OutputStream os = sock.getOutputStream();
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

                    bis.read(mybytearray, 0, mybytearray.length);

                    os.write(mybytearray, 0, mybytearray.length);
                    System.out.println("File has been uploaded to: d:/Server/UploadToHere");

                    os.flush();
                    sock.close();

                } catch (Exception e) {
                }
                break;
        }

    }
}
