package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
	
	static int timeout = 100;
	
	private static Socket socket;
	private static BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
    private static Server server = new Server(9000);
    
  
	@BeforeAll
	public static void startSocket() throws IOException{
		
		new Thread(() -> server.startServer()).start();
		//creating socket
		try {
			Thread.sleep(100);
            socket = new Socket("localhost", 9000);
            
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception ec) {      // exception handler if it failed
            System.out.println("Error connecting to server:" + ec);
        }	
	}
	

	@Test
	void Should_ReturnFalse_When_UserDoesNotExist(){
		//given
		String input = "Kimberly";
		//when
		boolean existence  = server.doesUserExist(input);
		//then
		assertFalse(existence);
	}
	
	@Test
	void Should_ReturnTrue_When_UserDoesExist() throws Exception {
		
		assertFalse(server.doesUserExist("Omer"));
		//given
		//login user
		String userName = "Omer";
		sOutput.println("IDEN " + userName);
		Thread.sleep(timeout);
		
		//when
		boolean existence  = server.doesUserExist(userName);
		//then
		assertTrue(existence);
		
		sOutput.println("QUIT");
		
	}
}
