package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    
    public void sendCommand(String cmd, PrintWriter i) {
		i.println(cmd);
		i.flush();
	}
	
	public PrintWriter getPrintWriter() throws IOException{
		OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        PrintWriter writer = new PrintWriter(osw);

        return writer;
	}
	
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
	
	//test validateMessage() method
	@Test
	@Order(2)
	public void should_NotifyError_When_InvalidCommand() throws IOException{
		String input = "MSG"; //command length < 4
		PrintWriter i = getPrintWriter();
		sendCommand(input, i);
		String msg = sInput.readLine();
		assertEquals("BAD invalid command to server",msg);
	}
	
	
}
