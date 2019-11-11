package Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import Frame.WhiteBoardServer;
import Object.ChatMessage;
import Shape.Paint;

/**
 * @author MinhHieu, TuDuyen
 * The support layer for server sockets
 * WhiteBoardServer can use to manage Client
 * 
 */
public class Server {
	private int 					port = 1024;
	private static int 				uniqueId = 0;
	private ArrayList<ClientThread> listClient;
	private WhiteBoardServer 		board;
	private SimpleDateFormat 		simpleDateFormat;
	private boolean 				keepGoing;
	private String					msg;//To show message to WhileBoardServer

	//Constructor
	public Server() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param port
	 * @param board
	 */
	public Server(int port, WhiteBoardServer board) {
		// TODO Auto-generated constructor stub
		this.board 			= board;
		this.port 			= port;
		simpleDateFormat 	= new SimpleDateFormat("HH:mm");
		listClient 			= new ArrayList<ClientThread>();
	}
	
	/**
	 * 
	 */
	public void start() {
		keepGoing = true;
		//Create socket server and wait for connection requests
		try 
		{
			ServerSocket serverSocket = new ServerSocket(port);
			showMessage("Server waiting for Clients on port " + port + "...");
			
			//Infinite loop to wait for connections
			while(keepGoing) 
			{				
				Socket socket = serverSocket.accept();  	// Accept connection
				//If I was asked to stop
				if(!keepGoing)// Press Stop or close frame
					break;
				ClientThread clientThread = new ClientThread(socket);  //Make a thread of it
				listClient.add(clientThread);									//Save it in the ArrayList
				clientThread.start();
				broadcast(board.paintApp.listPaint);
				showListUser();
			}
			// I was asked to stop
			try {
				if(serverSocket != null) serverSocket.close();
				for (ClientThread clientThread : listClient) {
					clientThread.disconnect();
				}
			} catch (IOException e) {
				// TODO: handle exception
				msg = "Error closing the server and clients - Exception : " + e.getMessage();
				showMessage(msg);
			}
		}
		catch (IOException e) {
            msg = simpleDateFormat.format(new Date()) + " Exception on new ServerSocket: " + e.getMessage();
			showMessage(msg);
		}
	}		
    /*
     * For the WhiteBroadServer to stop the server
     */
	@SuppressWarnings("resource")
	public void stop() {
		keepGoing = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
			
		}
	}
	/**
	 * @param user
	 * Remove the user from the list, stopping his thread
	 */
	public void kickOutUser(String user) {
		for (ClientThread clientThread : listClient) {
			if(clientThread.username.trim().equals(user.trim())) {
				clientThread.stop_();
				clientThread.disconnect();
				break;
			}
		}
	}
	/**
	 * Display user list on JList
	 */
	public void showListUser() {
		board.model.clear();
		for (ClientThread clientThread : listClient) {
			board.model.addElement(clientThread.username);
		}
		
	}
	/**
	 * @param user
	 * Display user name when dragging the mouse
	 */
	public void showNetVe(String user) {
		String info = "Nét vẽ của " + user;
		board.lblNetVe.setText(info);
	}
	/**
	 * @param msg
	 * Display notification or event on the form
	 */
	private void showMessage(String msg) {
		board.appendEvent(simpleDateFormat.format(new Date()) + " " + msg + "\n");
	}
	/**
	 * @param chatMessage
	 * Display the message on the form
	 */
	private void showChat(ChatMessage chatMessage) {
		board.appendChatRoom(simpleDateFormat.format(new Date()) + " " + chatMessage.getMessage() + "\n");
	}
	/**
	 * @param listPaint
	 * Broadcast the brush list to all clients
	 */
	private synchronized void broadcast(ArrayList<Paint> listPaint) {
		for(int i = listClient.size(); --i >= 0;) {
			ClientThread ct = listClient.get(i);
			if(!ct.sendListPaint(listPaint)) {
				listClient.remove(i);
				showMessage("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}
	/**
	 * @param listPaint
	 * Support callback broadcast function
	 */
	public void sendListPaint(ArrayList<Paint> listPaint) {
		broadcast(listPaint);
	}
	/**
	 * @param chatMessage
	 * Broadcast a message to all clients
	 */
	@SuppressWarnings("unused")
	private synchronized void broadcastChatMessage(ChatMessage chatMessage) {
		for(int i = listClient.size(); --i >= 0;) {
			ClientThread ct = listClient.get(i);
			if(!ct.sendMessageChat(chatMessage)) {
				listClient.remove(i);
				showMessage("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}	
	/**
	 * @param id
	 * Delete the logged out client
	 */
	synchronized void remove(int id) {
		//Find in the array list until we found the Id
		for(int i = 0; i < listClient.size(); ++i) {
			ClientThread clientThread = listClient.get(i);
			//Found it
			if(clientThread.id == id) {
				listClient.remove(i);
				return;
			}
		}
	}
	
	/**
	 * @author MinhHieu
	 * One instance of this thread will run for each client
	 */
	class ClientThread extends Thread {
		private int 				id;
		private String 				username;
		private ArrayList<Paint> 	listPaint;
		private Socket 				socket;
		private ObjectInputStream 	sInput;
		private ObjectOutputStream 	sOutput;
		private boolean				keepGoing_ = true;

		// Constructore
		public ClientThread(Socket socket) {
			id = ++uniqueId;
			this.socket = socket;
				if(!loginNotification()) {
					remove(id);
				}
		}
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// to loop until LOGOUT
			String msgTemp;
			ChatMessage chatTemp;
			while(keepGoing_) {
				try {
					Object temp = sInput.readObject();
					if(sInput == null || sOutput == null || socket == null) break;
					//Get String "Logout" from Client to logout
					try {
						msgTemp = (String)temp;
						if(msgTemp.equals("Logout")) {
							stop_();
							msg = username + " has logged out of the session.";
							showMessage(msg);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					//Try casting to ChatMessage if possible then broadcast
					try {
						chatTemp = (ChatMessage)temp;
						showChat(chatTemp);
					} catch (Exception e) {
						// TODO: handle exception
					}
					//Try to cast listPaint to get value if possible then broadcast
					try {
						listPaint = (ArrayList<Paint>)temp;
						board.paintApp.listPaint = new ArrayList<Paint>(listPaint);
						broadcast(listPaint);
						board.paintApp.repaint();
						board.repaint();
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					if(sInput != null && sOutput != null && socket == null) msg = "Error readObject from server - Exception : " + e.getMessage();
					else msg = username + " has logged out of the session.";
					showMessage(msg);
					break;
				}
				
			}
			// Remove myself from the arrayList containing the list of the connected Clients
			remove(id);
			disconnect();
		}
		public void stop_() {
			// TODO Auto-generated method stub
			keepGoing_ = false;
		}
		/**
		 * Try to close everything
		 */
		public void disconnect() {
			try {
				if(sInput != null) sInput.close();
				if(sOutput != null) sOutput.close();
				if(socket != null) socket.close();
				showListUser();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				msg = username + " === Error closing all connect - IOException : " + e.getMessage();
				showMessage(msg);
			}
		}

		/**
		 * @return
		 * Ask when login request from Client
		 */
		public boolean loginNotification() {
			try {
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
	
		        username = (String)sInput.readObject();
				
		        int response = JOptionPane.showConfirmDialog(null, username +" want to join in?", "Login confirmation required", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	            if (response == JOptionPane.NO_OPTION) {
					msg = "Server does not allow user \""+ username +"\" to join";
					showMessage(msg);
					stop_();
					return false;
	            } else if (response == JOptionPane.YES_OPTION || response == JOptionPane.CLOSED_OPTION) {
	            	showMessage(username + " just connected.");
	            }
	        return true;
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	    }

		/**
		 * @param chatMessage
		 * @return
		 * Write a ChatMessage to the Client output stream
		 */
		private boolean sendMessageChat(ChatMessage chatMessage) {
			//If Client is still connected send the message to it
			if(!socket.isConnected()) {
				disconnect();
				return false;
			}
			try {
				sOutput.writeObject(chatMessage);
				sOutput.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				msg = "Error to send message - IOException : " + e.getMessage();
				showMessage(msg);
			}
			return true;
		}
		/**
		 * @param listPaint
		 * @return
		 * Write a ArrayList<Paint> to the Client output stream
		 */
		private boolean sendListPaint(ArrayList<Paint> listPaint) {
			//If Client is still connected send the listPaint to it
			if(!socket.isConnected()) {
				disconnect();
				return false;
			}
			try {
				sOutput.writeObject(listPaint);
				sOutput.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				msg = "Error to send list Paint - IOException : " + e.getMessage();
				showMessage(msg);
			}
			return true;
		}
	}
}

