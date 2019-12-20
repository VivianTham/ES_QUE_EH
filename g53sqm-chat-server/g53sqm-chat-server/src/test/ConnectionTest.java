package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionTest {
	
	private Socket socket;
	private BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
	private String msg;
	
	@BeforeEach
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
	
	
	@Test
	public void testFirstConnection() throws IOException{
		String msg = sInput.readLine();
		assertEquals("OK Welcome to the chat server, there are currently 1 user(s) online", msg);
	}
	
	@Test
	public void testListCommand_WhenNoLogIns() throws IOException{
		
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet",msg);
	}
	
	public void sendCommand() {
		
	}
}
