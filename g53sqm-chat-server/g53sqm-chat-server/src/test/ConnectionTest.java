package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import g53sqm.chat.server.Server;

//Test class for Connection.java
class ConnectionTest {
	
	private static Socket socket;
	private static BufferedReader sInput;       // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
    
    private static Server server = new Server(9000); //initialise Server object
    
    static int timeout = 100; //used to put server thread to sleep between read-write to socket
	
	@BeforeAll
	public static void startServer() throws IOException{
		//start thread for server
		new Thread(() -> server.startServer()).start();
	}
	
	@BeforeEach
	public void initialiseClient() {
		//create mock client and initialise read & write from socket before each test
		try {
			Thread.sleep(100);
            socket = new Socket("localhost", 9000); 
            
            sInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sOutput = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception ec) {      // exception handler if it failed
            System.out.println("Error connecting to server:" + ec);
        }
	}
	
	@AfterEach
	public void quitClient() {
		//quit client connection to server after each test
		sOutput.println("QUIT");
	}
	
	@Test
	public void testFirstConnection() throws IOException{
		//store and compare expected server response with actual straight after connection to server
		String msg = sInput.readLine();
		assertEquals("OK Welcome to the chat server, there are currently 1 user(s) online", msg);
	}
	
	@Test
	public void should_NotifyError_When_InvalidMsg() throws IOException{
		sInput.readLine(); //skip first response from server given due to successful connection
		
		//send message of length < 4 to server
		String input = "MSG"; 
		sOutput.println(input);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD invalid command to server",msg);
	}
	
	@Test
	public void testListMethod_WithoutRegisteredUser() throws IOException{
		sInput.readLine();
		
		//send LIST command to server without logging in
		String input = "LIST";
		sOutput.println(input);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet",msg);
	}
	
	@Test
	public void testListMethod_WithRegisteredUser() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//send LIST command to server
		sOutput.println("LIST");
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("OK " + username + ", ", msg);
	}
	
	@Test
	public void testIdenMethod_NewRegistration() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("OK Welcome to the chat server " + username, msg);
	}
	
	@Test
	public void testIdenMethod_AlreadyRegistered() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//try logging in another username on same client
		String username1 = "testUser1";
		sOutput.println("IDEN " + username1);
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD you are already registerd with username " + username, msg);
	}
	
	@Test
	public void testStatMethod_UNREGISTERED() throws Exception{
		sInput.readLine();
		
		//send STAT command to server without logging in
		sOutput.println("STAT");
		Thread.sleep(timeout);
		
		//store and compare expected server responses with actual
		String msg = sInput.readLine();
		assertEquals("OK There are currently 1 user(s) on the server", msg);
		
		msg = sInput.readLine();
		assertEquals("You have not logged in yet", msg);
	}
	
	@Test
	public void testStatMethod_REGISTERED() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//send STAT command to server
		sOutput.println("STAT");
		Thread.sleep(timeout);
		
		//store and compare expected server responses with actual
		String msg = sInput.readLine();
		assertEquals("OK There are currently 1 user(s) on the server", msg);
		
		msg = sInput.readLine();
		assertEquals("You are logged in and have sent 0 message(s)", msg);
	}
	
	@Test
	public void testMesgMethod_UNREGISTERED() throws Exception{
		sInput.readLine();
		
		//send msg without logging in
		sOutput.println("MESG Omer Hello!");
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet", msg);
	}
	
	@Test
	public void testHailMethod_UNREGISTERED() throws Exception{
		sInput.readLine();
		
		//broadcast msg without logging in
		sOutput.println("HAIL Hellooo!");
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD You have not logged in yet", msg);
	}
	
	@Test
	public void testHailMethod_REGISTERED() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//broadcast msg from testUser
		String broadcast = "Hellooo!";
		sOutput.println("HAIL " + broadcast);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("Broadcast from " + username + ": " + broadcast, msg);
	}
	
	@Test
	public void testQuitMethod_UNREGISTERED() throws Exception {
		sInput.readLine();
		
		//quit client from server without logging in
		sOutput.println("QUIT");
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("OK goodbye",msg);
	}
	
	@Test
	public void testQuitMethod_REGISTERED() throws Exception {
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//quit client from server
		sOutput.println("QUIT");
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("OK thank you for sending 0 message(s) with the chat service, goodbye. ", msg);
	}
	
	@Test
	public void testUnrecognisableCommand() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//send unrecognisable message to server
		String cmd = "TEST";
		sOutput.println(cmd);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD command not recognised", msg);
	}
	
	
	@Test
	public void testUsernameTaken() throws Exception{
		sInput.readLine();
		
		//Login testUser on first client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//Second client initialisation
		Socket socket1 = new Socket("localhost", 9000);
        BufferedReader sInput1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        PrintWriter sOutput1 = new PrintWriter(socket1.getOutputStream(), true);
		
        sInput1.readLine();
        
        //try logging in testUser again on second client
		String username1 = "testUser";
		sOutput1.println("IDEN " + username1);
		Thread.sleep(timeout);
		
		String msg = sInput1.readLine();
		assertEquals("BAD username is already taken", msg); //compare expected message from server with actual
		
		sOutput1.println("QUIT"); //statement used to close the second client and socket
	}
	
	@Test
	public void testInvalidFormattedMsg() throws Exception {
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//Send message without specifying username 
		sOutput.println("MESG Hello");
		Thread.sleep(timeout);
		
		//store and compare expected server response with actual
		String msg = sInput.readLine();
		assertEquals("BAD Your message is badly formatted", msg);
	}
	
	@Test
	public void testMsgSentAndReceived() throws Exception {
		sInput.readLine();
		
		//Login testUser on first client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//Second client initialisation
		Socket socket1 = new Socket("localhost", 9000);
        BufferedReader sInput1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        PrintWriter sOutput1 = new PrintWriter(socket1.getOutputStream(), true);
        
        sInput1.readLine();
        
	    //Login testUser2 on second client
        String username1 = "testUser2";
        sOutput1.println("IDEN " + username1);
        Thread.sleep(timeout);
        sInput1.readLine();
        
        //Send message from testUser to testUser2
        String sentMsg = "Hello!";
        sOutput.println("MESG testUser2 " + sentMsg);
        Thread.sleep(timeout);
        
        String msg = sInput.readLine(); //store server response for first client
        
        String msg1 = sInput1.readLine(); //store server response for second client
        
        //compare expected responses with actual for both first and second client accordingly
        assertAll(
        	() -> assertEquals("OK your message has been sent", msg), 
        	() -> assertEquals("PM from " + username + ":" + sentMsg, msg1)
        		);
	
        sOutput1.println("QUIT"); //statement used to close the second client and socket
	}
	
	@Test
	public void testMesgSentToNonExistentUser() throws Exception{
		sInput.readLine();
		
		//Login testUser on client
		String username = "testUser";
		sOutput.println("IDEN " + username);
		Thread.sleep(timeout);
		sInput.readLine();
		
		//Send message from testUser to testUser2 that doesn't exist
		String sentMsg = "Hello!";
        sOutput.println("MESG testUser2 " + sentMsg);
        
        String msg = sInput.readLine(); //store server response to testUser client
        assertEquals("BAD the user does not exist", msg); //compare expected response with actual
	}
}