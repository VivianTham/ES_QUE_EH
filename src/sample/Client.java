package sample;

import java.net.*;
import java.io.*;
import java.util.*;


public class Client {

    public static String display;
    private BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
    private Socket socket;                    // socket object
    private String server;
    private int port;
    String msg;
    public static StringBuilder allMsg = new StringBuilder("");


    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    Client(String server, int port) {
        this.server = server;
        this.port = port;
    }


    //need to connect to server, read input then rest server settle
    //user name set from iden function in connection

    //
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        }
        // exception handler if it failed
        catch (Exception ec) {

            msg = "Error connecting to server:" + ec;
            allMsg.append(msg);
            allMsg.append("\n");
            System.out.println(msg);
            return false;
        }

        msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        allMsg.append(msg);
        allMsg.append("\n");
        System.out.println(msg);

        /* Creating both Data Stream */
        try {
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException eIO) {
            msg = "Exception creating new I/O Streams: " + eIO;
            allMsg.append(msg);
            allMsg.append("\n");
            System.out.println(msg);

            return false;
        }

        //creates the Thread to listen from the server
        new ListenFromServer().start();

        return true;
    }


//    private void display(String msg) {      //display message sent at console
//
//        System.out.println(msg);
//    }
    
    class ListenFromServer extends Thread {
        // to read and print message from input datastream
        public void run() {
            while (true) {
                try {
                    msg = (String) sInput.readLine();
                    allMsg.append(msg);
                    allMsg.append("\n");
                    Main.mainController.messages_console.setText(allMsg.toString());
                    //System.out.println(allMsg.toString());
                } catch (IOException e) {
                    msg = "Server has closed the connection: " + e;
                    allMsg.append(msg);
                    allMsg.append("\n");
                    System.out.println(msg);
                    break;
                }

            }
        }
    }


    public static void sendOverConnection(String msg) {
        allMsg.append(msg);
        allMsg.append("\n");
        sOutput.println(msg);
    }

}

