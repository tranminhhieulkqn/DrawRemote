package Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Frame.WhiteBoardClient;
import Object.ChatMessage;
import Shape.Paint;

/**
 * @author MinhHieu, TuDuyen
 * The support layer for client sockets
 * WhiteBoardClient can use
 * 
 */
public class Client{
	private String 					serverHost;
	private int 					port;
	private String 					username;	//Username Client
	private SimpleDateFormat 		simpleDateFormat = new SimpleDateFormat("HH:mm");
	private ObjectInputStream 		sInput;		// to read from the socket
	private ObjectOutputStream 		sOutput;	// to write on the socket
	private Socket 					socket;
	private ArrayList<Paint>		listPaint;
	private String 					msg;		//Show message status
	private WhiteBoardClient 		board = null;
	
	//Data encapsulation
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	//Constructor
	public Client() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param serverHost
	 * @param port
	 * @param username
	 * @param board
	 */
	public Client(String serverHost, int port, String username, WhiteBoardClient board) {
		// TODO Auto-generated constructor stub
		this.serverHost = serverHost;
		this.port 		= port;
		this.username	= username;
		this.board 		= board;
	}
	
	
	//Self create functions
	/**
	 * @return
	 */
	public boolean start() {
		//Create connection to server.
		try {
			socket = new Socket(serverHost, port);
			msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
			showMessage(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "Error connecting to server - IOException : " + e.getMessage();
			showMessage(msg);
			return false;
		}
		//Create Data Stream
		try {
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "Error create new Data Stream - IOException : " + e.getMessage();
			showMessage(msg);
			return false;
		}
		
		// Create thread to listen from the server 
		new ListenFromServer().start();
		try {
			sOutput.writeObject(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "Error login - IOException : " + e.getMessage();
			showMessage(msg);
			disconnect();
			return false;
		}
		return true;
	}

	/**
	 * @param msg
	 * Display messages or notifications on the Client form
	 */
	private void showMessage(String msg) {
		board.append(simpleDateFormat.format(new Date()) + " "+ msg + "\n");
	}
	/**
	 * @param user
	 * Displays the owner of a brush stroke on the form
	 */
	public void showNetVe(String user) {
		String info = "Nét vẽ của " + user;
		board.lblNetVe.setText(info);
	}
	/**
	 * Send string "Logout" to log out of server
	 */
	public void messageLogout() {
		try {
			if(sInput == null || sOutput == null || socket == null) disconnect(); 
			else sOutput.writeObject("Logout");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "Error to write to Server - IOException : " + e.getMessage();
			showMessage(msg);
		}
	}
	/**
	 * @param chatMessage
	 * Send a message from the Client
	 */
	public void sendMessageChat(ChatMessage chatMessage) {
		try {
			if(sInput == null || sOutput == null || socket == null) {
				disconnect();
			}
			else {
				sOutput.writeObject(chatMessage);
				showMessage(chatMessage.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	/**
	 * @param listPaint
	 * Send the drawing list to the server
	 */
	public void sendListPaint(ArrayList<Paint> listPaint) {
		try {
			if(sInput == null || sOutput == null || socket == null) {
				disconnect();
			}
			else sOutput.writeObject(listPaint); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	

	/**
	 * When something goes wrong
	 * Close the Input/Output streams and disconnect not much to do in the catch clause
	 */
	public void disconnect() {
        try {
        	if(sInput != null) 	sInput.close();
			if(sOutput != null) sOutput.close();
			if(socket != null) 	socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "Error closing Data Stream and Socket - IOException ; " + e.getMessage();
			showMessage(msg);
		}
		board.connectionFailed();
	}

	/**
	 * Class use to waits for: 
	 * The list drawing paint from server and repaint them to the paintApp
	 * The message from the server and append them to the JTextArea
	 */
	class ListenFromServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			ChatMessage chatTemp;
			while(true) {
				
				if (sInput == null || sOutput == null) {
					break;
				}
						
				try {
					Object temp = sInput.readObject();
					
					//Try casting style to ChatMessage
					try {
						chatTemp = (ChatMessage)temp;
						showMessage(chatTemp.getMessage());
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					//Try casting style to ArratList<Paint>
					try {
						//Get list of brush strokes and repaint
						listPaint = (ArrayList<Paint>)temp;
						board.paintApp.listPaint = listPaint;
						board.paintApp.repaint();
						board.repaint();
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					if(socket == null) msg = "The server has been closed by - Exception ; " + e.getMessage();
					else msg = "Logged out successfully!";
					showMessage(msg);
					board.connectionFailed();
					break;
				}
					
			}
		}
	}
}
