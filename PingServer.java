package likeDislike;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PingServer extends Thread {

	int port;
	int noOfClients;

	public PingServer() {
		this.port = 0;
		this.noOfClients = 0;
	}

	//to create instance of ping server 
	public PingServer(int port, int noOfClients) {
		this.port = port;
		this.noOfClients = noOfClients;
	}

	public void run() {//to start the thread that listens for connections 
		System.out.println("PingServer thread started");
		Socket connection = null;
		ServerSocket server = null;
		//Create an instance of ServerSocket
		try {

			server = new ServerSocket(port);}

		// end try
		catch (IOException e) {
			System.err.println(e); } // end catch

		connection = null;
		System.out.println("Server socket created");
		for(int i= 0; i < noOfClients ; i++) {
			try {
				connection = server.accept( );//to accept connection from client 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Server got connected to client");
			if(connection==null||connection.isClosed())
			{
				System.out.println("Connection closed");
			}
			HandleConnection h = new HandleConnection(connection, i+1);//to begin interacting with client 

			h.start();//to start thread 

		}
	}
}

