package Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

import Frame.WhiteBoardServer;
import Object.ChatMessage;
import Shape.Paint;

/*
 * The server that can be run both as a console application or a GUI
 */
public class Server {
	private int 					port = 1024;
	private static int 				uniqueId = 0;
	private ArrayList<ClientThread> listClient;
	private WhiteBoardServer 		board;
	private SimpleDateFormat 		simpleDateFormat;
	private boolean 				keepGoing;
	private String					msg;//To show message to WhileBoardServer
	private ObjectInputStream 		sInput;
	private ObjectOutputStream 		sOutput;

	public Server(int port, WhiteBoardServer board) {
		this.board 			= board;
		this.port 			= port;
		simpleDateFormat 	= new SimpleDateFormat("HH:mm");
		listClient 			= new ArrayList<ClientThread>();
	}
	
	public void start() {
		keepGoing = true;
		/* create socket server and wait for connection requests */
		try 
		{
			ServerSocket serverSocket = new ServerSocket(port);
			showMessage("Server waiting for Clients on port " + port + "...");
			
			// infinite loop to wait for connections
			while(keepGoing) 
			{				
				Socket socket = serverSocket.accept();  	// accept connection
				// if I was asked to stop
				if(!keepGoing)// Press Stop or close frame
					break;
				ClientThread clientThread = new ClientThread(socket);  // make a thread of it
				listClient.add(clientThread);									// save it in the ArrayList
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
     * For the GUI to stop the server
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
	public void kickOutUser(String user) {
		for (ClientThread clientThread : listClient) {
			if(clientThread.username.trim().equals(user.trim())) {
				clientThread.stop_();
				clientThread.disconnect();
				break;
			}
		}
	}
	public void showListUser() {
		board.model.clear();
		for (ClientThread clientThread : listClient) {
			board.model.addElement(clientThread.username);
		}
		
	}
	public void showNetVe(String user) {
		String info = "Nét vẽ của " + user;
		board.lblNetVe.setText(info);
	}
	/*
	 * Display an event (not a message) to the console or the GUI
	 */
	private void showMessage(String msg) {
		board.appendEvent(simpleDateFormat.format(new Date()) + " " + msg + "\n");
	}
	private void showChat(ChatMessage chatMessage) {
		board.appendChatRoom(simpleDateFormat.format(new Date()) + " " + chatMessage.getMessage() + "\n");
	}
	/*
	 *  to broadcast a message to all Clients
	 */
	public void sendListPaint(ArrayList<Paint> listPaint) {
		broadcast(listPaint);
	}
	private synchronized void broadcast(ArrayList<Paint> listPaint) {
		for(int i = listClient.size(); --i >= 0;) {
			ClientThread ct = listClient.get(i);
			//ct.disconnect();
			// try to write to the Client if it fails remove it from the list
			if(!ct.sendListPaint(listPaint)) {
//				System.out.print("=======================================================" + "\n");
//				for (Paint paint : listPaint) {
//					System.out.print(paint.toString() + "\n");
//				}
				listClient.remove(i);
				showMessage("Disconnected Client " + ct.username + " removed from list.");
			}
		}
		
//		for (Paint paint : listPaint) {
//			System.out.print("Server : " + paint.toString()+ "\n");
//		}
//		for (ClientThread clientThread : listClient) {
//			clientThread.sendListPaint(listPaint);
//		}
//		showMessage("Có broadcast rồi mà!");
	}
	private synchronized void broadcastChatMessage(ChatMessage chatMessage) {
		for(int i = listClient.size(); --i >= 0;) {
			ClientThread ct = listClient.get(i);
			if(!ct.sendMessageChat(chatMessage)) {
				listClient.remove(i);
				showMessage("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}

	// for a client who logoff using the LOGOUT message
	synchronized void remove(int id) {
		// scan the array list until we found the Id
		for(int i = 0; i < listClient.size(); ++i) {
			ClientThread clientThread = listClient.get(i);
			// found it
			if(clientThread.id == id) {
				listClient.remove(i);
				return;
			}
		}
	}
	

	/** One instance of this thread will run for each client */
	class ClientThread extends Thread {
		private int 				id;
		private String 				username;
		private ArrayList<Paint> 	listPaint;
		private String 				date;
		private Socket 				socket;
		private ObjectInputStream 	sInput;
		private ObjectOutputStream 	sOutput;
		private boolean				keepGoing_ = true;

		// Constructore
		public ClientThread(Socket socket) {
			id = ++uniqueId;
			this.socket = socket;
			//showMessage("Thread trying to create Object Input/Output Streams");
//			try
//			{
//				// create output first
//				sOutput = new ObjectOutputStream(socket.getOutputStream());
//				sInput  = new ObjectInputStream(socket.getInputStream());
//				// read the username
//				username = (String)sInput.readObject();
//				showMessage(username + " just connected.");
				if(!loginNotification()) {
					remove(id);
//					disconnect();
				}
//			}
//			catch (IOException e) {
//				showMessage("Exception creating new Input/output Streams: " + e);
//				return;
//			}
//			// have to catch ClassNotFoundException
//			// but I read a String, I am sure it will work
//			catch (ClassNotFoundException e) {
//			}
            date = new Date().toString() + "\n";
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// to loop until LOGOUT
//			boolean keepGoing = true;
			String msgTemp;
			ChatMessage chatTemp;
			while(keepGoing_) {
				try {
					Object temp = sInput.readObject();
					if(sInput == null || sOutput == null || socket == null) break;
					// Nhận String "Logout" từ Client để logout
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
					// Thử ép kiểu sang ChatMessage nếu được broadcast
					try {
						chatTemp = (ChatMessage)temp;
						showChat(chatTemp);
					} catch (Exception e) {
						// TODO: handle exception
					}
					// Thử ép kiểu listPaint nhận giá trị
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
			// remove myself from the arrayList containing the list of the
			// connected Clients
			remove(id);
			disconnect();
		}
		public void stop_() {
			// TODO Auto-generated method stub
			keepGoing_ = false;
		}
		// try to close everything
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

		public boolean loginNotification() {
			try {
				// create output first
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
	
		        username = (String)sInput.readObject();
				
		        int response = JOptionPane.showConfirmDialog(null, username +" want to join in?", "Login confirmation required", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	            if (response == JOptionPane.NO_OPTION) {
//					System.out.println(username +" kicked out");
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
		/*
		 * Write a String to the Client output stream
		 */
		private boolean sendMessageChat(ChatMessage chatMessage) {
			// Chỉ gửi tin nhắn khi kết nối đã được thiết lập
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
		private boolean sendListPaint(ArrayList<Paint> listPaint) {
			// if Client is still connected send the message to it
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

