package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import g53sqm.chat.server.Connection;
import g53sqm.chat.server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConnectionTest {
	
	private static Socket socket;
	private static BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
    private static Server server = new Server(9000);
    private static Connection c;
    
    static int timeout = 100;
	
	@BeforeAll
	public static void startServer() throws IOException{
		new Thread(() -> server.startServer()).start();
	}
	
	@BeforeEach
	public void initialiseClient() {
		try {
			Thread.sleep(100);
            socket = new Socket("localhost", 9000);
            
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception ec) {      // exception handler if it failed
            System.out.println("Error connecting to server:" + ec);
        }
		c = new Connection(socket,server);
	}
	
	@AfterEach
	public void quitClient() {
		sOutput.println("QUIT");
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
		sInput.readLine();
		String input = "MSG"; //command length < 4
		sOutput.println(input);
		String msg = sInput.readLine();
		assertEquals("BAD invalid command to server",msg);
	}
	
	@Test
	@Order(3)
	public void testListMethod_WithoutRegisteredUser() throws IOException{
		sInput.readLine();
		String input = "LIST";
		sOutput.println(input);
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet",msg);
	}
	
	@Test
	@Order(4)
	public void testListMethod_WithRegisteredUser() throws Exception{
		sInput.readLine();
		String username = "testUser";
		sOutput.println("IDEN " + username);
		sInput.readLine();
		Thread.sleep(timeout);
		
		sOutput.println("LIST");
		Thread.sleep(timeout);
		
		
		String msg = sInput.readLine();
		assertEquals("OK " + username + ", ", msg);
		
		sOutput.println("QUIT");
	}
	
	@Test
	@Order(5)
	public void testIdenMethod_NewRegistration() throws Exception{
		//initialiseClient();
		sInput.readLine();
		
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("OK Welcome to the chat server " + username, msg);
		
		sOutput.println("QUIT");
	}
	
	@Test
	@Order(6)
	public void testIdenMethod_AlreadyRegistered() throws Exception{
		//initialiseClient();
		sInput.readLine();
		
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		String username1 = "testUser1";
		sOutput.println("IDEN " + username1);
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("BAD you are already registerd with username " + username, msg);
		
		sOutput.println("QUIT");
	}
	
	@Test
	@Order(7)
	public void testStatMethod_UNREGISTERED() throws Exception{
		//initialiseClient();
		sInput.readLine();
		
		sOutput.println("STAT");
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("OK You have not logged in yet", msg);
	}
	
	@Test
	@Order(8)
	public void testStatMethod_REGISTERED() throws Exception{
		sInput.readLine();
		
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		sOutput.println("STAT");
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("You are logged im and have sent 0 message(s)", msg);
		
		sOutput.println("QUIT");
	}
	
	@Test
	@Order(9)
	public void testMesgMethod_UNREGISTERED() throws Exception{
		//initialiseClient();
		sInput.readLine();
		
		sOutput.println("MESG Omer Hello!");
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet", msg);
	}
	
	@Test
	@Order(10)
	public void testHailMethod_UNREGISTERED() throws Exception{
		sInput.readLine();
		
		sOutput.println("HAIL Hellooo!");
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet", msg);
	}
	
	@Test
	@Order(11)
	public void testHailMethod_REGISTERED() throws Exception{
		sInput.readLine();
		
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		String broadcast = "Hellooo!";
		sOutput.println("HAIL " + broadcast);
		
		String msg = sInput.readLine();
		assertEquals("Broadcast from " + username + ": " + broadcast, msg);
		
		sOutput.println("QUIT");
		
	}
	
	@Test
	@Order(12)
	public void testQuitMethod_UNREGISTERED() throws Exception {
		sInput.readLine();
		
		sOutput.println("QUIT");
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("OK goodbye",msg);
	}
	
	@Test
	@Order(13)
	public void testQuitMethod_REGISTERED() throws Exception {
		sInput.readLine();
		
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		sOutput.println("QUIT");
		Thread.sleep(timeout);
		
		String msg = sInput.readLine();
		assertEquals("OK thank you for sending 0 message(s) with the chat service, goodbye. ", msg);
	}
	
	@Test
	@Order(14)
	public void testGetState_UNREGISTERED() throws Exception{
		int state = c.getState();
		
		assertTrue(state == 0);
	}
	
	@Test
	@Order(15)
	public void testGetState_REGISTERED() throws Exception {
		sOutput.println("IDEN Omer");

		int state = c.getState();
		
		assertEquals(1,state);
	}
}
