package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import g53sqm.chat.server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConnectionTest {
	
	private static Socket socket;
	private static BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
	
	@BeforeAll
	public static void startSocket() throws IOException{
		new Thread(() -> new Server(9000).startServer()).start();
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
	@Order(1)
	public void testFirstConnection() throws IOException{
		String msg = sInput.readLine();
		assertEquals("OK Welcome to the chat server, there are currently 1 user(s) online", msg);
	}
	
	@Test
	@Order(2)
	public void testListCommand_WhenNoLogIns() throws IOException{
		fail();
//		String msg = sInput.readLine();
//		assertEquals("BAD You have not logged in yet",msg);
	}
	
	public void sendCommand() {
		
	}
}
