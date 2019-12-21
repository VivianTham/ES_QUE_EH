package g53sqm.chat.server;

import java.io.IOException;

public class Runner
{
	static Server server;
	final static int PORT = 9000;
	
	public static void main(String[] args) {
		server = new Server(PORT);
		server.startServer();
	}
	
	
}
