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



/**
 * 
 * @author Kyle & Justin
 * This Class acts as a File Server. It opens a socket on port 7005 and waits until a client sends a message.
 * It will read the message and invoke the required method. SEND, RECEIVE, or QUIT. 
 * Based on message receives, it invokes RECEIVE method or SEND method respectively, or quits.
 * 
 * 
 * 
 */
public class Server {
    //Using desired port 7005.
    public static int port = 7005;
    static String fileName;

    public static void main(String[] args) throws IOException {

        ServerSocket servsock = new ServerSocket(port);//Open socket on port 7005.
        ServerSocket servsock2 = new ServerSocket(7006);//Open socket on port 7006. Used to send over filename.
        
        System.out.println("Listening on Port " + port);//Let Client know we are listening. 
        
        //Listen then accept both incomming connections on both ports and inform client.
        while (true) {
            Socket sock = servsock.accept();
            Socket sock2 = servsock2.accept();
            System.out.println("Connection Established");
            
            
            //Used to hold the initial client message. 
            byte[] msgArray = new byte[7];
            //Allows for a file name to be up to 50 characters (including extension)
            byte[] nameArray = new byte[50];
            
            //Accept Client message and store it.
            sock.getInputStream().read(msgArray);
            String readableMsg = new String(msgArray, "UTF-8");
            //Trim excess space in array.
            readableMsg = readableMsg.trim();
            
            
            sock2.getInputStream().read(nameArray);
            //Converts the input stream to an array using the UTF-8 format.
            fileName = new String(nameArray, "UTF-8");
            //Removes the extra spaces not being used of the set 50 bytes in the previously stated array.
            fileName = fileName.trim();
            
            //Takes User Message and determines which method to invoke.
            System.out.println(readableMsg);
            if (readableMsg.equals("RECEIVE")) {
                SendToClient(sock);
            } else if (readableMsg.equals("SEND")) {
                ReceiveFromClient(sock);//
            }
            else if (readableMsg.equals("QUIT")){
                System.exit(0);//Closes connection, Program Ends. 
            }
            break;
        }

    }
    
    /**
     * Called when Client sends a RECEIVE message String.
     * 
     * This method simulates  downloading a file from the server.
     * They will be given a list of files in the directory and depending 
     * on the user's next request, will write the determined file 
     * using the socket's output stream.
     * 
     * 
     * @param sock port 7005.
     * @throws IOException 
     */
    public static void SendToClient(Socket sock) throws IOException {
        //Pulls the specified file from the ServerFiles directory based on the specified file name.
        File myFile = new File("./ServerFiles/" + fileName);
        //Array lenghth is file size. Client will keep accepting bytes until the array is empty.
        byte[] mybytearray = new byte[(int) myFile.length()];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
        bis.read(mybytearray, 0, mybytearray.length);
        //Get output stream from client.
        OutputStream os = sock.getOutputStream();
        //Writing file to the Output Stream.
        os.write(mybytearray, 0, mybytearray.length);
        //Inform user.
        System.out.println("File Sent");
        //Flush then close the socket.
        os.flush();
        sock.close();
        System.exit(0);
    }
    /**
     * Called when Client sends a GET message String.
     * 
     * This method simulates a Client uploading a file to the server through the socket.
     * The client will choose a file to send and the server will take the filename,
     * and save it with the same name to a pre determined directory. (/ServerFiles).
     * 
     * @param sock open on port 7005.
     * @throws IOException 
     */
    public static void ReceiveFromClient(Socket sock) throws IOException {
        //Length 1, the amount of bytes we will be sending over at a time.
        byte[] myByteArray = new byte[1];
        InputStream is = sock.getInputStream();
        //The output directory is set to be the ServerFiles directory
        //The output file is set to be the exact same name as the input (name and extension)
        FileOutputStream fos = new FileOutputStream("./ServerFiles/" + fileName);
        //used to write bytes to underlying output stream
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        //used to depleate array. Server keeps reading bytes until it reaches 0.
        int bytesRead = is.read(myByteArray, 0, myByteArray.length);
        int len=0;
        //write the bytes until reached file length.
        while((len = is.read(myByteArray))!= -1){
        bos.write(myByteArray);
        }
        //notify the user. Close the socket, close program.
        System.out.println("File Received");
        bos.close();
        sock.close();
        System.exit(0);
        
    }

}
