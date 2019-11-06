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
import Object.User;
import Shape.Paint;

/*
 * The Client that can be run both as a console or a WhiteBoardClient
 */
public class Client{
	private String 					serverHost;
	private int 					port;
	private String 					username;	//Username Client
	private SimpleDateFormat 		simpleDateFormat = new SimpleDateFormat("HH:mm");
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private ObjectInputStream 		sInput;		// to read from the socket
	private ObjectOutputStream 		sOutput;	// to write on the socket
	private Socket 					socket;
	private ArrayList<Paint>		listPaint;
	private String 					msg;		//Show message status

	private WhiteBoardClient board = null;
	
	public Client(String serverHost, int port, String username, WhiteBoardClient board) {
		this.serverHost = serverHost;
		this.port 		= port;
		this.username	= username;
		this.board 		= board;
	}
	
	/*
	 * To start the dialog
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
		// Send our username to the server this is the only message that we
		// will send as a String. All other messages will be ChatMessage objects
		try {
			sOutput.writeObject(username);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			msg = "Error login - IOException : " + e.getMessage();
			showMessage(msg);
			disconnect();
			return false;
		}
		//After success
		return true;
	}

	//To send message to the WhiteBoardClient
	private void showMessage(String msg) {
		board.append(simpleDateFormat.format(new Date()) + " "+ msg + "\n");
	}
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
//			msg = "Error to write to Server - IOException : " + e.getMessage();
//			showMessage(msg);
		}
	}
	//To send list Paint to the server
	public void sendListPaint(ArrayList<Paint> listPaint) {
		try {
			if(sInput == null || sOutput == null || socket == null) {
				disconnect();
			}
			else sOutput.writeObject(listPaint); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			msg = "Error to write to Server - IOException : " + e.getMessage();
//			showMessage(msg);
		}
	}
	
	public void showNetVe(String user) {
		String info = "Nét vẽ của " + user;
		board.lblNetVe.setText(info);
	}

	/*
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

	/*
	 * a class that waits for the message from the server and append them to the JTextArea
	 * if we have a WhiteBoardClient or simply System.out.println() it in console mode
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
					
					// Thử ép kiểu sang ChatMessage nếu được broadcast
					try {
						chatTemp = (ChatMessage)temp;
						showMessage(chatTemp.getMessage());
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					try {
							listPaint = (ArrayList<Paint>)temp;
							
		//					if(!board.paintApp.listPaint.equals(listPaint))
							board.paintApp.listPaint = listPaint;
		//					int k = 1;
//							showMessage("==================================Đã nhận từ Server");
//							System.out.print("=============================================" + "\n");
//							for (Paint paint : listPaint) {
//								
//								System.out.print("Client : " + paint.toString()+ "\n");
//							}
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
