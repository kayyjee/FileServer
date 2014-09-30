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

        String option;
        System.out.println("What is the IP Address of the Server?");
        Scanner input = new Scanner(System.in);
        String IP = input.nextLine().toString();
        System.out.println("\nWould you like to SEND or RECEIVE or QUIT");
        option = input.nextLine();

        do {
            System.out.println("\nPlease enter either SEND or RECEIVE or QUIT");
            option = input.nextLine();
            System.out.println(option);

            if ((option.equals("SEND")) || (option.equals("RECEIVE")) || (option.equals("QUIT")));
            {
                break;
            }
        } while (!option.equals("SEND") || !option.equals("RECEIVE")   || !option.equals("QUIT"));

        switch (option) {
            case "RECEIVE":

                try {
                    Socket sock = new Socket(IP, 7005);

                    String msg = "RECEIVE";

                    sock.getOutputStream().write(msg.getBytes());

                    byte[] myByteArray = new byte[10];

                    InputStream is = sock.getInputStream();
                    FileOutputStream fos = new FileOutputStream("d:/Client/Output/happy.mp3");

                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int bytesRead = is.read(myByteArray, 0, myByteArray.length);

                    int len = 0;
                    while ((len = is.read(myByteArray)) != -1) {

                        bos.write(myByteArray);

                    }
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

                    System.out.println("Please enter the path of the .txt file to upload: "
                            + "\n EX: d:/Client/FileToUpload.txt \n");

                    String userFile = input.nextLine();
                    File myFile = new File(userFile);
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    OutputStream os = sock.getOutputStream();
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                    bis.read(mybytearray, 0, mybytearray.length);
                    os.write(mybytearray, 0, mybytearray.length);

                    os.flush();
                    sock.close();

                } catch (Exception e) {
                }
                break;
             
        case "QUIT": 
            System.out.println("Goodbye");
            System.exit(0);
        }

    }
}
