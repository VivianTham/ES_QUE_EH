package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import g53sqm.chat.server.Server;

class ServerTest{
	
	private Socket socket;
	private BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
	private String msg;
	
    
    public void startSocket() throws IOException{
		//creating socket
		try {
            socket = new Socket("localhost", 9000);

        } catch (Exception ec) {      // exception handler if it failed

            String msg = "Error connecting to server:" + ec;
            System.out.println(msg);
        }
		
		//getting input and output data streams
		try {
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException eIO) {
            msg = "Error creating new I/O Streams: " + eIO;
            System.out.println(msg);
        }	
	}
	
//	@BeforeAll
//	static void setUp(){
//		server = new Server(9002);
//		thread = new Thread();
//		thread.start();
//	}

	
	@Test
	void Should_ReturnFalse_When_UserDoesNotExist(){
		//given
//		String input = "Kimberly";
		//when
//		boolean existence  = doesUserExist(input);
		//then
//		assertFalse(existence);
		//fail("Not implemented yet");
	}
}
