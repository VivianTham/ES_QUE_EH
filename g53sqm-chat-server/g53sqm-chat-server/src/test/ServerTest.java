package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import g53sqm.chat.server.Server;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.*;

class ServerTest{
	
	static int timeout = 100;
	
	private static Socket socket;
	private static BufferedReader sInput;        // to read from the socket
    private static PrintWriter sOutput;        // to write on the socket
    private static Server server = new Server(9000);
    

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
	}
    
    
	@AfterEach
	public void stopClient() {
		sOutput.println("QUIT");
	}

//Test cases for getUserList() method:
	@Test
	void Should_ReturnEmptyList_When_NoUserLoggedIn() {
		//when
		ArrayList<String> userList = server.getUserList();
		//then
        assertThat(userList, IsEmptyCollection.empty());
	}
	
	@Test
	void Should_ReturnUserList_When_ListNotEmpty() throws Exception{
		//login user
		String userName = "Vivian";
		sOutput.println("IDEN " + userName);
		Thread.sleep(timeout);
		
		sOutput.println("LIST");
		Thread.sleep(timeout);
		//when
		ArrayList<String> userList = server.getUserList();
		//then
		assertThat(userList, hasItem("Vivian"));
	
	}

//Test cases for doesUserExist() method:
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
	}
	
//Test cases for broadcastMessage() method:
	@Test
	void testBroadcastMessage() throws Exception {
		sInput.readLine();
		//given
		String message = "Hello sexy :)";
		//when
		server.broadcastMessage(message);
		//then
		String broadcast = sInput.readLine();
		assertEquals(message, broadcast);
	}
	

	
//Test cases for sendPrivateMessage() method:
	@Test
	void Should_ReturnTrue_When_() throws Exception {
		//given
		//message sender
		String sender = "Anne Hathaway";
		sOutput.println("IDEN " + sender);
		Thread.sleep(timeout);
		
		//message receiver
		Socket socket1 = new Socket("localhost", 9000);
        
        BufferedReader sInput1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        PrintWriter sOutput1 = new PrintWriter(socket1.getOutputStream(), true);
        
		String message = "Wonderwoman is wonderful.";
		String receiver = "Nathan";
		sOutput1.println("IDEN " + receiver);
		Thread.sleep(timeout);
			
		//when
		boolean messageStatus = server.sendPrivateMessage(message, receiver);
		//then
		assertTrue(messageStatus);
		
		sOutput1.println("QUIT");
	}
	
//Test cases for getNumberOfUsers() method:
	@Test
	void Should_ReturnNumberOfUsers_When_ListNotEmpty() throws Exception {
		
		//given
		//login user
		String userName = "Omer";
		sOutput.println("IDEN " + userName);
		Thread.sleep(timeout);
		
		//when
		int userCount = server.getNumberOfUsers();
		
		//then
		assertEquals(1, userCount);
	}	
	
	
	@Test
	void Should_ReturnZero_When_ListEmpty() throws Exception {
		sInput.readLine();
		
		//when
		int userCount = server.getNumberOfUsers();
	
		//then
		assertEquals(1, userCount);
	}	
	
}
