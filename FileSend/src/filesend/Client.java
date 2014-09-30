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

/**
 *
 * @author Kyle & Justin This Class acts as a Client machine. It attempts and
 * succeeds in establishing a connection on a Server Socket. Client will be
 * asked to input the IP Address of the Server. Client will then Send a message
 * String to determine which Server method to invoke. The options are GET,
 * RECEIVE & QUIT.
 *
 */
public class Client {

    public static void main(String[] argv) throws Exception {

        String option;//user's determined option. 
        System.out.println("What is the IP Address of the Server?");//Prompt user.
        Scanner input = new Scanner(System.in);
        String IP = input.nextLine().toString();//storing the IP Address of the server.

        //Will continue asking user to enter one of 3 commands until entered properly.
        do {
            System.out.println("\nWould you like to SEND or RECEIVE or QUIT");
            option = input.nextLine();
            

            //breaks if user enters it properly the first time.
            //if ((option.equals("SEND")) || (option.equals("RECEIVE")) || (option.equals("QUIT")));
           // {
           //     break;
          //  }
        
        
        //switch statement will handle the 3 users choices.
        switch (option) {

            //Simulates a download of a file from the server.
            case "RECEIVE":
                //folder holds one file at a time in ServerFiles directory.
                File folder = new File("./ServerFiles/");
                //array reads all files in directory and stores them.
                File[] listOfFiles = folder.listFiles();
                System.out.println("\nList of all the current server files");
                //displays all the files in the directory to the user.
                for (File listOfFile : listOfFiles) {
                    if (listOfFile.isFile()) {
                        System.out.println("File: " + listOfFile.getName());
                    }
                }
                //prompt user to choose from ServerFiles
                System.out.println("\nWhich file would you like to receive? (filename and extention)");
                String getFile = input.nextLine().toString();
                //try to establish connection on two sockets.
                try {
                    Socket sock = new Socket(IP, 7005);//used to transfer file.
                    //Creates a new socket on port 7006 in order to send the filename over to the server so it can GET the correct file from the Server files directory
                    Socket sock2 = new Socket(IP, 7006);

                    String msg = "RECEIVE";//send to server to invoke it's send method.
                    String name = getFile;

                    sock.getOutputStream().write(msg.getBytes());
                    //Send the file name to the Server so it can retrieve the right one.
                    sock2.getOutputStream().write(name.getBytes());

                    //array lenth determines how many bytes we send at a time.
                    byte[] myByteArray = new byte[1];
                    //get input stream from Server.
                    InputStream is = sock.getInputStream();
                    //Outputs the retrieved file to the Client Files directory under the specified name.
                    FileOutputStream fos = new FileOutputStream("./ClientFiles/" + getFile.trim());//trim off array space not used.
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    //used to deplete the array. Continue reading until myByteArray is empty.
                    int bytesRead = is.read(myByteArray, 0, myByteArray.length);
                    int len = 0;
                    //write the bytes until reached file length.
                    while ((len = is.read(myByteArray)) != -1) {
                        bos.write(myByteArray);
                    }
                    //notify user.
                    System.out.println(getFile + " has been successfully received to the client files directory.");
                    bos.close();
                    sock.close();
                } catch (Exception e) {
                }
                
                
                
                
                
                //simulates uploading a file to the server.
            case "SEND":

                try {
                    Socket sock = new Socket(IP, 7005);
                    String msg = "SEND   ";//initial message.
                    //send message to Server to invoke method.
                    sock.getOutputStream().write(msg.getBytes());
                    //prompt user to enter path of file to upload.
                    System.out.println("Please enter the path of the file to send: "
                            + "\n EX: D:/Client/FileToUpload.txt \n");
                    //store path of user's file.
                    String userFile = input.nextLine();

                    //Creates a new socket on port 7006 in order to send the filename over to the server.
                    Socket sock2 = new Socket(IP, 7006);
                    String name = userFile;

                    //If statement to extract the file and extension from the full path.
                    //If the file path is used with back slashes
                    if (name.contains("\\")) {
                        name = name.replaceAll(".*\\\\", "");
                    } else {
                        //If the file path is used with forward slashes
                        name = name.replaceAll(".*/", "");
                    }
                    //Send the file name to the Server so it can store it as such.
                    sock2.getOutputStream().write(name.getBytes());
                    //store as type File, the determined path of file to transfer.
                    File myFile = new File(userFile);
                    //array is set to length of file.
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    OutputStream os = sock.getOutputStream();
                    //used to write bytes to underlying output stream
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                    bis.read(mybytearray, 0, mybytearray.length);
                    os.write(mybytearray, 0, mybytearray.length);
                    //Notify user.
                    System.out.println("File " + name + " has been sent.");
                    //Flush and close socket. Close program. 
                    os.flush();
                    sock.close();
                    System.exit(0);

                } catch (Exception e) {
                }
                
                
            //Closes program if user enters quit.
            case "QUIT":
                System.out.println("Goodbye");
                System.exit(0);
        }
        //Exits loop if one of options below is entered.
        } while (!option.equals("SEND") || !option.equals("RECEIVE") || !option.equals("QUIT"));

    }
}
