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
    
  
	@BeforeAll
	public static void startServer() throws IOException{
		new Thread(() -> server.startServer()).start();
	}
	
	@AfterEach
	public void stopClient() {
		sOutput.println("QUIT");
	}
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
	

	@Test
	void Should_ReturnNumberOfUsers_When_ListNotEmpty() throws Exception {
		sInput.readLine();
		//given
		//login user
		String userName = "Omer";
		sOutput.println("IDEN " + userName);
		Thread.sleep(timeout);
		
		//when
		int userCount = server.getNumberOfUsers();
		
		//then
		assertEquals(1, userCount);
		sOutput.println("QUIT");
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
