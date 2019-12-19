package test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import g53sqm.chat.server.Server;

class ServerTest{
	private static Server server;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private ServerRunner runner;
	private String sep = System.getProperty("line.separator");
    private String initial = "Server has been initialised on port 9000" + sep;
	Thread t;
	//private static Server server = new Server(9015);
	
	@BeforeEach
    public void initialize() throws IOException {
        
		//runner = new ServerRunner(9003);
    }
	
	@AfterEach
	public void end() throws IOException {
       //runner.quitServer();
    }

	@Test
	public void should_NumberOfUsers_When_ListEmpty() throws IOException {
		//server = new Server(9000);
		//System.out.println("a");
		//System.out.println(server.getNumberOfUsers());
		// given
		initialiseClient();
		//initialiseAndSendMessage("IDEN " + input);
		// when
		//int userNumber = server.getNumberOfUsers();
		// then
		assertEquals(initial + "OK Welcome to the chat server, there are currelty 1 user(s) online", outContent.toString());
		System.out.println("b");
	}
	
	private void initialiseClient() throws IOException {
    String host = "localhost";
    int port = 9000;
    InetAddress address = InetAddress.getByName(host);
    Socket socket = new Socket(address, port);

}


class ServerRunner {

    private final int port;

    ServerRunner(int port) {
        this.port = port;
        server = new Server(port);
    }

//    @Override
//    public void run() {
//        System.setOut(new PrintStream(outContent));
//        server = new Server(port);
//    }

//    public void quitServer() throws IOException {
//        server.finalize();
//    }

    public Server getServer() {
        return server;
    }
}
}
