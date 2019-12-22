package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import sample.*;

class ClientTest {

	public static Client c;
	private String serverAdd = "localhost";
	private int port = 9000;
	
	@Test
	public void testInitialisationOfStringBuilder() {
		c = new Client(serverAdd,port);
		
		String msgFromClient = c.allMsg.toString();	
		assertEquals("", msgFromClient);
	}
	
	@Test
	public void testConnectionToServer_SUCCESS() throws Exception {
		
		c = new Client(serverAdd,port);
		
		assertTrue(c.start());
	}
	
	@Test
	public void testConnectionToServer_FAILED() throws Exception {
		//if server not initialized
		c = new Client(serverAdd,port);
		
		assertFalse(c.start());
	}
	
	@Test
	public void testConnectionAcceptanceResponseFromClient() throws Exception {
		c = new Client(serverAdd,port);
		
		if(c.start()) {
			String msgFromClient = c.allMsg.toString();
			assertEquals("Connection accepted localhost/127.0.0.1:9000\n", msgFromClient);
		}
		else {
			fail("Client connection to server failed");
		}
	}
	
	@Test
	public void testSendOverConnection() throws Exception {
		c = new Client(serverAdd,port);
		
		if(c.start()) {
			String msgToServer = "IDEN testUser";
			c.sendOverConnection(msgToServer);
			String initialMsg = "Connection accepted localhost/127.0.0.1:9000\\n";
			String msgFromUser = c.allMsg.toString();
			assertEquals(initialMsg + msgToServer + "\n", msgFromUser);
		}
		else {
			fail("Client connection to server failed");
		}
	}
}
