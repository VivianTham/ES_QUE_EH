package sample;

import java.net.*;
import java.io.*;

public class Client {

    public static String display;
    private BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
    private static Socket socket;                    // socket object
    private String server;
    private int port;
    String msg;

    //to use append, we use stringBuilder instead of string
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

    public static Socket getSocket(){
        return socket;
    }

    public void setSocket(){
        Client.socket = socket;
    }

    Client(String server, int port) {
        this.server = server;
        this.port = port;
    }

    //connection to server starts here
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);

        } catch (Exception ec) {      // exception handler if it failed

            msg = "Error connecting to server:" + ec;
            Main.mainController.messages_console.setText(msg);
            System.out.println(msg);
            return false;
        }

        //reading input
        Main.mainController.userInput.requestFocus();

        msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        allMsg.append(msg);
        allMsg.append("\n");
        System.out.println(msg);

        //creating input and output data stream
        try {
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException eIO) {
            msg = "Error creating new I/O Streams: " + eIO;
            allMsg.append(msg);
            allMsg.append("\n");
            System.out.println(msg);

            return false;
        }

        //thread to listen from the server started here
        new ListenFromServer().start();

        return true;
    }

    //creating thread to connect with server
    class ListenFromServer extends Thread {
        // to read and print message from input data stream
        public void run() {
            while (true) {
                try {
                    msg = sInput.readLine();
                    allMsg.append(msg);
                    allMsg.append("\n");
                    Main.mainController.messages_console.setText(allMsg.toString());
                    //auto scrolling for new messages received
                    Main.mainController.messages_console.selectPositionCaret(Main.mainController.messages_console.getLength());
                    Main.mainController.messages_console.deselect();

                } catch (IOException e) {
                    msg = "Server closed: " + e;
                    allMsg.append(msg);
                    allMsg.append("\n");
                    System.out.println(msg);
                    break;
                }

            }
        }
    }

    //this method gets information from user, send to server and server will handle it
    public static void sendOverConnection(String msg) {
        //user's input is append to the end of previous one + a line break

        allMsg.append(msg);
        allMsg.append("\n");

        sOutput.println(msg);
    }

}