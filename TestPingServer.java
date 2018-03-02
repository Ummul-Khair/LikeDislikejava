package likeDislike;

public class TestPingServer {

	public static void main(String []args)
	{
		//each port permits 8 clients 
		int PORT = 5095;//getstats port number
		int NO_OF_CLIENTS = 8; 
		PingServer ps = new PingServer(PORT, NO_OF_CLIENTS);
		ps.start();
		
		int PORT2 = 5088;//likes port number
		PingServer ps2 = new PingServer(PORT2, NO_OF_CLIENTS);
		ps2.start();
		
		int PORT3 = 5082;//dislikes port number
		PingServer ps3 = new PingServer(PORT3, NO_OF_CLIENTS);
		ps3.start();
		
	}
}
