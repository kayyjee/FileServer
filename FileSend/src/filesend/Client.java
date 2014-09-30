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

        do {
            System.out.println("\nWould you like to SEND or RECEIVE or QUIT");
            option = input.nextLine();
            System.out.println(option);

            if ((option.equals("SEND")) || (option.equals("RECEIVE")) || (option.equals("QUIT")));
            {
                break;
            }
        } while (!option.equals("SEND") || !option.equals("RECEIVE")   || !option.equals("QUIT"));

        switch (option) {
            case "RECEIVE":
                
                File folder = new File("./ServerFiles/");
                File[] listOfFiles = folder.listFiles();
                
                System.out.println("\nList of all the current server files");
                
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        System.out.println("File: " + listOfFile.getName());
                    }
                }
                
                System.out.println("\nWhich file would you like to receive? (filename and extention)");
                String getFile = input.nextLine().toString();
                
                try {
                    Socket sock = new Socket(IP, 7005);
                    Socket sock2 = new Socket (IP, 7006);

                    String msg = "RECEIVE";
                    String name = getFile;

                    sock.getOutputStream().write(msg.getBytes());
                    sock2.getOutputStream().write(name.getBytes());

                    byte[] myByteArray = new byte[1];

                    InputStream is = sock.getInputStream();
                    FileOutputStream fos = new FileOutputStream("./ClientFiles/" + getFile.trim());
                    System.out.println(getFile + " has been successfully received to the client files directory.");
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


                    System.out.println("Please enter the path of the file to send: "
                            + "\n EX: D:/Client/FileToUpload.txt \n");

                    String userFile = input.nextLine();
                    
                    Socket sock2 = new Socket (IP, 7006);
                    String name = userFile;
                    //If the file path is used with back slashes
                    if(name.contains("\\")){
                        name = name.replaceAll(".*\\\\", "");
                    } else { 
                        //If the file path is used with forward slashes
                        name = name.replaceAll(".*/", "");
                    }
                    //Send the file name to the Server so it can store it as such.
                    System.out.println("File " + name + " has been sent.");
                    sock2.getOutputStream().write(name.getBytes());
                    
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
