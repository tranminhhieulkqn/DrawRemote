package Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import Object.ColorChoose;
import Object.DrawType;
import Object.OpenFile;
import Object.SaveFile;
import Paint.PaintApp;
import Socket.Server;
import javax.swing.DefaultListModel;

public class WhiteBoardServer extends JFrame implements WindowListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7985076036116513127L;
	private JPanel contentPane;
	int numberSizes = 20;
	
	public static String selectShap = "";
	public static Color selectColor = Color.black;
	public PaintApp paintApp = new PaintApp();


	JButton buttonStartStop;
	private JTextField textFieldPortNumber;
	JTextArea textAreaChat;
	JTextArea textAreaStatus;
	public DefaultListModel<String> model = new DefaultListModel<String>();
	public JList<String> listUser;
	public JLabel lblNetVe;
	// my server
	public Server server;
	public static WhiteBoardServer frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new WhiteBoardServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public WhiteBoardServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Draw Remote - Server");
		setSize(1300, 700);
		setMinimumSize(getSize());
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
		
		server = null;
		
		/*
		 * Create Menu bar
		 */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		/*
		 * Menu Create News paint
		 */
		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectShap = "";
				OpenFile.image = null;
				paintApp.listPaint.clear();
				repaint();
			}
		});
		mnMenu.add(mntmNew);
		
		/*
		 * Menu open this paint 
		 */
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //selectShap = "Open";
            	try {
            		paintApp.listPaint.clear();
            		OpenFile open = new OpenFile();
					paintApp.listPaint = open.getListPaint();
					paintApp.repaint();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
            }
        });
		mnMenu.add(mntmOpen);
		
		/*
		 * Menu Save this paint 
		 */
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectShap = "Save";
				new SaveFile(paintApp.listPaint, paintApp.getWidth(), paintApp.getHeight());
			}
		});
		mnMenu.add(mntmSave);
		
		/*
		 * Menu exit this application
		 */
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				selectShap = "Exit";
				System.exit(0);
			}
		});
		mnMenu.add(mntmExit);
		
		/*
		 * Menu About
		 */
		
		Panel pnlabout = new Panel();
		pnlabout.setVisible(false);
		
		/*
		 * Panel main (painting on this panel)
		 */
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setBackground(Color.white);
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.darkGray);
		contentPane.add(panel, BorderLayout.NORTH);
		
		/*
		 * Button selected draw Point
		 */
		JButton btnPoint = new JButton("Point");
		btnPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paintApp.setDrawType(DrawType.Pen);
			}
		});
		
		/*
		 * Button selected draw Line
		 */
		JButton btnLine = new JButton("Line");
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintApp.setDrawType(DrawType.Line);
			}
		});
		
		/*
		 * Button selected draw Rectangle
		 */
		JButton btnRect = new JButton("Rectangle");
		btnRect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paintApp.setDrawType(DrawType.Rectangle);
			}
		});
		
		/*
		 * Button selected draw Oval
		 */
		JButton btnOval = new JButton("Oval");
		btnOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintApp.setDrawType(DrawType.Oval);
			}
		});
		
		/*
		 * Button selected draw Square
		 */
		JButton btnSquare = new JButton("Square");
		btnSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paintApp.setDrawType(DrawType.Square);
			}
		});
		
		/*
		 * Button selected draw Triangle
		 */
		JButton btnTri = new JButton("Triangle");
		btnTri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintApp.setDrawType(DrawType.Triangle);
			}
		});
		
		/*
		 * Button selected draw Circle
		 */
		JButton btnCir = new JButton("Circle");
		btnCir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paintApp.setDrawType(DrawType.Circle);
			}
		});

		/*
		 * Button selected fill color to shape
		 */
		JButton btnFill = new JButton("Fillcolor");
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintApp.setDrawType(DrawType.Fill);
			}
		});
		
		/*
		 * Button choose color
		 */
		JButton btnChooseColor = new JButton("Chose Color");
		btnChooseColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//selectShap = "ChooseColor";
				new ColorChoose();
			}
		});
		
		/*
		 * Button move shape by mouse
		 */
		JButton btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paintApp.setDrawType(DrawType.Move);
			}
		});
		
		/*
		 * Button delete shape by mouse click
		 */
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				paintApp.setDrawType(DrawType.Delete);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(4)
					.addComponent(btnPoint, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnLine, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRect, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOval, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSquare, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTri, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCir, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnFill, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnChooseColor, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnMove, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(210))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPoint, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLine, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRect, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOval, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSquare, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnTri, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCir, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnFill, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnChooseColor, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMove, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		/*
		 * Create Panel Color to select color
		 */
		JPanel pnlColor = new JPanel();
		pnlColor.setBackground(Color.darkGray);
		pnlColor.setSize(20, getHeight());
		contentPane.add(pnlColor, BorderLayout.SOUTH);
		
		
		/*
		 * Add another button color
		 */
		Dimension SizeColorBox = new Dimension(33, 33);
		
		JButton btnC_Red = new JButton();
		btnC_Red.setPreferredSize(SizeColorBox);
		btnC_Red.setBackground(Color.red);
		btnC_Red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.red;
			}
		});
		
		
		/*
		 * Add another button color
		 */
		JButton btnC_Blue = new JButton();
		btnC_Blue.setPreferredSize(SizeColorBox);
		btnC_Blue.setBackground(Color.blue);
		btnC_Blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.blue;
			}
		});
		
		/*
		 * Add another button color
		 */
		JButton btnC_Green = new JButton();
		btnC_Green.setPreferredSize(SizeColorBox);
		btnC_Green.setBackground(Color.green);
		btnC_Green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.green;
			}
		});
		
		/*
		 * Add another button color
		 */
		JButton btnC_Cyan = new JButton();
		btnC_Cyan.setPreferredSize(SizeColorBox);
		btnC_Cyan.setBackground(Color.cyan);
		btnC_Cyan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.cyan;
			}
		});
		
		/*
		 * Add another button color
		 */
		JButton btnC_DarkGray = new JButton();
		btnC_DarkGray.setPreferredSize(SizeColorBox);
		btnC_DarkGray.setBackground(Color.darkGray);
		btnC_DarkGray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.darkGray;
			}
		});
		
		/*
		 * Add another button color
		 */
		JButton btnC_Orange = new JButton();
		btnC_Orange.setPreferredSize(SizeColorBox);
		btnC_Orange.setBackground(Color.orange);
		btnC_Orange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.orange;
			}
		});
		
		/*
		 * Add another button color
		 */
		JButton btnC_Pink = new JButton();
		btnC_Pink.setPreferredSize(SizeColorBox);
		btnC_Pink.setBackground(Color.pink);
		btnC_Pink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.pink;
			}
		});
		
		/*
		 * Add another button color
		 */
		JButton btnC_LightGray = new JButton();
		btnC_LightGray.setPreferredSize(SizeColorBox);
		btnC_LightGray.setBackground(Color.lightGray);
		btnC_LightGray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectColor = Color.lightGray;
			}
		});
		
		lblNetVe = new JLabel("Nét vẽ");
		lblNetVe.setForeground(Color.WHITE);
		lblNetVe.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_pnlColor = new GroupLayout(pnlColor);
		gl_pnlColor.setHorizontalGroup(
			gl_pnlColor.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlColor.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnC_Red, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_Blue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_Green, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_Cyan, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_DarkGray, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_Orange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_Pink, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnC_LightGray, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(lblNetVe, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
					.addGap(908))
		);
		gl_pnlColor.setVerticalGroup(
			gl_pnlColor.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlColor.createSequentialGroup()
					.addGroup(gl_pnlColor.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlColor.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlColor.createParallelGroup(Alignment.LEADING)
								.addComponent(btnC_LightGray, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_Blue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_Red, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_Green, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_Cyan, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_DarkGray, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_Orange, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnC_Pink, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_pnlColor.createSequentialGroup()
							.addGap(19)
							.addComponent(lblNetVe, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlColor.setLayout(gl_pnlColor);
		
		/*
		 * Create new Component paint to paint with mouse
		 */
		
		contentPane.add(paintApp , BorderLayout.CENTER);
		panel.setBackground(Color.darkGray);
		
		JPanel panelManagement = new JPanel();
		contentPane.add(panelManagement, BorderLayout.EAST);
		
		JLabel lblPortNumber = new JLabel("Port Number:");
		lblPortNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFieldPortNumber = new JTextField();
		textFieldPortNumber.setText("1024");
		textFieldPortNumber.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldPortNumber.setColumns(10);
		
		buttonStartStop = new JButton("Start");
		buttonStartStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// if running we have to stop
				if(server != null) {
					server.stop();
					server = null;
					textFieldPortNumber.setEditable(true);
					buttonStartStop.setText("Start");
					return;
				}
		      	// OK start the server	
				int port;
				try {
					port = Integer.parseInt(textFieldPortNumber.getText().trim());
				}
				catch(Exception er) {
					appendEvent("Invalid port number");
					return;
				}
				// ceate a new Server
				server = new Server(port, frame);
				paintApp.setServer(server);
				// and start it as a thread
				
				new ServerThread().start();;
				buttonStartStop.setText("Stop");
				textFieldPortNumber.setEditable(false);
			}
		});
		buttonStartStop.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JPanel panelChatRoom = new JPanel();
		panelChatRoom.setBorder(new TitledBorder(null, "Chat Room", TitledBorder.LEFT, TitledBorder.TOP));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_panelChatRoom = new GroupLayout(panelChatRoom);
		gl_panelChatRoom.setHorizontalGroup(
			gl_panelChatRoom.createParallelGroup(Alignment.LEADING)
				.addGap(0, 310, Short.MAX_VALUE)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
		);
		gl_panelChatRoom.setVerticalGroup(
			gl_panelChatRoom.createParallelGroup(Alignment.LEADING)
				.addGap(0, 200, Short.MAX_VALUE)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
		);
		
		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		scrollPane_2.setViewportView(textAreaChat);
		panelChatRoom.setLayout(gl_panelChatRoom);
		
		JPanel panelStatus = new JPanel();
		panelStatus.setBorder(new TitledBorder(null, "Status", TitledBorder.LEFT, TitledBorder.TOP));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panelStatus = new GroupLayout(panelStatus);
		gl_panelStatus.setHorizontalGroup(
			gl_panelStatus.createParallelGroup(Alignment.LEADING)
				.addGap(0, 310, Short.MAX_VALUE)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
		);
		gl_panelStatus.setVerticalGroup(
			gl_panelStatus.createParallelGroup(Alignment.LEADING)
				.addGap(0, 200, Short.MAX_VALUE)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
		);
		
		textAreaStatus = new JTextArea();
		textAreaStatus.setEditable(false);
		scrollPane_1.setViewportView(textAreaStatus);
		panelStatus.setLayout(gl_panelStatus);
		
		JPanel panelListUser = new JPanel();
		panelListUser.setBorder(new TitledBorder(null, "List User", TitledBorder.LEFT, TitledBorder.TOP));
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panelListUser = new GroupLayout(panelListUser);
		gl_panelListUser.setHorizontalGroup(
			gl_panelListUser.createParallelGroup(Alignment.LEADING)
				.addGap(0, 274, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
		);
		gl_panelListUser.setVerticalGroup(
			gl_panelListUser.createParallelGroup(Alignment.LEADING)
				.addGap(0, 123, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
		);
		
		listUser = new JList<String>(model);
		listUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
		        if (arg0.getClickCount() == 2) {
		        	String userSelect = listUser.getSelectedValue();
		        	int response = JOptionPane.showConfirmDialog(null,
                            "Do you want to keep kicking out \""+ userSelect +"\"?", "Confirm",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    
					if(response == JOptionPane.YES_OPTION) {
//    		        	System.out.print(listUser.getSelectedValue());
    		        	server.kickOutUser(userSelect);
    		        	server.showListUser();
		        	}
		        }
			}
		});
		scrollPane.setViewportView(listUser);
		panelListUser.setLayout(gl_panelListUser);
		GroupLayout gl_panelManagement = new GroupLayout(panelManagement);
		gl_panelManagement.setHorizontalGroup(
			gl_panelManagement.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelManagement.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPortNumber)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textFieldPortNumber, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonStartStop, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_panelManagement.createSequentialGroup()
					.addGroup(gl_panelManagement.createParallelGroup(Alignment.LEADING)
						.addComponent(panelListUser, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
						.addComponent(panelStatus, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
						.addComponent(panelChatRoom, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
					.addGap(3))
		);
		gl_panelManagement.setVerticalGroup(
			gl_panelManagement.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelManagement.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelManagement.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPortNumber)
						.addComponent(textFieldPortNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonStartStop))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelStatus, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelListUser, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelChatRoom, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
					.addContainerGap())
		);
		panelManagement.setLayout(gl_panelManagement);
		validate();
	}
	
	// append message to the two JTextArea
	// position at the end
	public void appendChatRoom(String str) {
		textAreaChat.append(str);
		textAreaChat.setCaretPosition(textAreaChat.getText().length() - 1);
	}
	public void appendEvent(String str) {
		textAreaStatus.append(str);
		textAreaStatus.setCaretPosition(textAreaStatus.getText().length() - 1);
	}
	
	/*
	 * A thread to run the Server
	 */
	class ServerThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			server.start();         // should execute until if fails
			// the server failed
			buttonStartStop.setText("Start");
			textFieldPortNumber.setEditable(true);
			appendEvent("Successfully disconnected servers\n");
			server = null;
		}
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		// if my Server exist
		if(server != null) {
			try {
				server.stop();			// ask the server to close the conection
			}
			catch(Exception eClose) {
			}
			server = null;
		}
		// dispose the frame
		dispose();
		System.exit(0);
	}


	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
