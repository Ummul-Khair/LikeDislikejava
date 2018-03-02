package likeDislike;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

import org.bson.Document;


public class HandleConnection extends Thread {

	BufferedReader in, fin;
	Scanner scan = new Scanner (System.in);
	String t;
	PrintWriter p;
	int clientno;
	Socket socket;

	public HandleConnection() {
		this.socket = null;
		t = new String();
		clientno = 0;
	}//default constructor 

	public HandleConnection(Socket socket, int client) {
		this.socket = socket;
		t = new String();
		clientno = client;
	}//constructor with parameters

	public void run() {

		//to connect to database with url, name of database, and collection 
		Database db = new Database("mongodb://umm:like@ds121716.mlab.com:21716/likedislike", "likedislike", "rooms");
		OutputStreamWriter out = null;//output stream to write response message and data to client 
		try {
			out = new OutputStreamWriter(socket.getOutputStream( ));
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		try {
			Date d = new Date();
			db.connectToDatabase();//connects to database 
			//sends response message 
			out.write("HTTP/1.1" + " 200 OK\r\n");
			out.write("Date: " +d.toString()+ "\r\n");
			out.write("Server: " + "localhost\r\n");
			out.write("Last-Modified: " +d.toString()+ "\r\n");
			out.write("Context-Length:" + 30 + "\r\n");
			out.write("Content-Type: " + "text/plain" +"\r\n");
			out.write("Connection closed\r\n");
			out.write("\r\n");
			if(socket.getLocalPort() == 5095)//if port connected to is 5095, get and display stats 
			{
				Document stats = db.getStats();//calls database function to get statistics 
				String likes = stats.get("likes").toString();//gets number of likes in database
				String dislikes = stats.get("dislikes").toString();//gets number of dislikes in database 
				out.write("Likes: " + likes +" Dislikes: " + dislikes +"\r\n");//sends no of likes and dislikes to client 
			}
			else if (socket.getLocalPort() == 5088)//if port connected to is 5088, get, increment, and display likes
			{
				int likes = db.Like();//get likes from database
				out.write("Likes: " + likes +"\r\n");//send likes to user 
			}


			else if(socket.getLocalPort() == 5082)////if port connected to is 5082, get, increment  and display dislikes
			{
				int dislikes = db.DisLike();//get dislikes frokm database 
				out.write("Dislikes: " + dislikes +"\r\n");//send dislikes to user 
			}
			db.closeDb();///close database 

		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			out.write("\r\n");

			out.flush();
			out.close();//close connection 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
