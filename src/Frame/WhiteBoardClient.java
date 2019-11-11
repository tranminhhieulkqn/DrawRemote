package Frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import Object.ChatMessage;
import Object.ColorChoose;
import Object.DrawType;
import Object.OpenFile;
import Object.SaveFile;
import Paint.PaintApp;
import Socket.Client;

public class WhiteBoardClient extends JFrame implements WindowListener {

	private static final long serialVersionUID = -7985076036116513127L;
	private JPanel contentPane;
	int numberSizes = 20;
	public JTextArea textAreaChat;
	public PaintApp paintApp = new PaintApp();


	// the Client object
	private Client client;
	//Phần vẽ
	public static WhiteBoardClient frame;
	public static String selectShap = "";
	public static DrawType drawType = null;
	public static Color selectColor = Color.black;
	public JTextField textFieldServerAddress;
	public JTextField textFieldUsername;
	public JTextField textFieldPortNumber;
	public JTextField textFieldMessage;
	public JButton btnLogin;
	public JButton btnLogout;
	public JLabel lblNetVe;
	
	
	
	public static void main(String[] args) 
    {
    	try{
    		
    		frame = new WhiteBoardClient();
    		frame.setVisible(true);
        }catch (Exception e) {
			e.printStackTrace();
		}
    }

	// called by the Client to append text in the TextArea 
	public void append(String str) {
		textAreaChat.append(str);
		textAreaChat.setCaretPosition(textAreaChat.getText().length() - 1);
	}
	// called by the WhiteBoardClient is the connection failed
	// we reset our buttons, label, textfield
	public void connectionFailed() {
		btnLogin.setEnabled(true);
		btnLogout.setEnabled(false);
		// let the user change them
		textFieldServerAddress.setEditable(true);
		textFieldPortNumber.setEditable(true);
	}
	
	public WhiteBoardClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Draw Remote - Client");
		setSize(1300, 700);
		setMinimumSize(getSize());
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width  - getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
		
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
//				selectShap = "";
				drawType = DrawType.Pen;
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
//                selectShap="Open";
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
		JMenuItem mntmNew_1 = new JMenuItem("Save");
		mntmNew_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				selectShap = "Save";
				drawType = DrawType.Save;
				new SaveFile(paintApp.listPaint, paintApp.getWidth(), paintApp.getHeight());
			}
		});
		mnMenu.add(mntmNew_1);
		
		/*
		 * Menu exit this application
		 */
		JMenuItem mntmNew_2 = new JMenuItem("Exit");
		mntmNew_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				selectShap = "Exit";
				drawType = DrawType.Exit;
				System.exit(0);
			}
		});
		mnMenu.add(mntmNew_2);
		
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
//				selectShap = "Line";
				paintApp.setDrawType(DrawType.Line);
			}
		});
		
		/*
		 * Button selected draw Rectangle
		 */
		JButton btnRect = new JButton("Rectangle");
		btnRect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				selectShap = "Rectangle";
				paintApp.setDrawType(DrawType.Rectangle);
				
			}
		});
		
		/*
		 * Button selected draw Oval
		 */
		JButton btnOval = new JButton("Oval");
		btnOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				selectShap = "Oval";
				paintApp.setDrawType(DrawType.Oval);
			}
		});
		
		/*
		 * Button selected draw Square
		 */
		JButton btnSquare = new JButton("Square");
		btnSquare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				selectShap = "Square";
				paintApp.setDrawType(DrawType.Square);
			}
		});
		
		/*
		 * Button selected draw Triangle
		 */
		JButton btnTri = new JButton("Triangle");
		btnTri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				selectShap = "Triangle";
				paintApp.setDrawType(DrawType.Triangle);
			}
		});
		
		/*
		 * Button selected draw Circle
		 */
		JButton btnCir = new JButton("Circle");
		btnCir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				selectShap = "Circle";
				paintApp.setDrawType(DrawType.Circle);
			}
		});

		/*
		 * Button selected fill color to shape
		 */
		JButton btnFill = new JButton("Fillcolor");
		btnFill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				selectShap = "fill";
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
//				selectShap = "move";
				paintApp.setDrawType(DrawType.Move);
			}
		});
		
		/*
		 * Button delete shape by mouse click
		 */
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				selectShap = "Delete";
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
					.addGap(744))
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
							.addComponent(lblNetVe)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlColor.setLayout(gl_pnlColor);
		
		/*
		 * Create new Component paint to paint with mouse
		 */
		
		contentPane.add(paintApp, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel.setBackground(Color.darkGray);
		contentPane.add(panel_1, BorderLayout.EAST);
		
		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// ok it is a connection request
				String username = textFieldUsername.getText().trim();
				// empty username ignore it
				if(username.length() == 0)
					return;
				// empty serverAddress ignore it
				String server = textFieldServerAddress.getText().trim();
				if(server.length() == 0)
					return;
				// empty or invalid port numer, ignore it
				String portNumber = textFieldPortNumber.getText().trim();
				if(portNumber.length() == 0)
					return;
				int port = 0;
				try {
					port = Integer.parseInt(portNumber);
				}
				catch(Exception en) {
					return;   // nothing I can do if port number is not valid
				}

				// try creating a new Client with WhiteBoardClient
				client = new Client(server, port, username, frame);
				// test if we can start the Client
				if(!client.start())
					return;
				paintApp.setClient(client);
				textFieldMessage.setText("");

				
				// disable login button
				btnLogin.setEnabled(false);
				// enable the 2 buttons
				btnLogout.setEnabled(true);
				// disable the Server and Port JTextField
				textFieldServerAddress.setEditable(false);
				textFieldPortNumber.setEditable(false);
				// Action listener for when the user enter a message
				//textFieldMessage.addMouseListener(this);
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		btnLogout = new JButton("Logout");
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				client.messageLogout();
				client.disconnect();
			}
		});
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel label = new JLabel("Server Address:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel label_1 = new JLabel("Port Number:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel label_2 = new JLabel("Username:");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFieldServerAddress = new JTextField();
		textFieldServerAddress.setText("localhost");
		textFieldServerAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldServerAddress.setColumns(10);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldUsername.setColumns(10);
		
		textFieldPortNumber = new JTextField();
		textFieldPortNumber.setText("1024");
		textFieldPortNumber.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldPortNumber.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Chat Room", TitledBorder.LEFT, TitledBorder.TOP));
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 312, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGap(0, 200, Short.MAX_VALUE)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
		);
		
		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		scrollPane.setViewportView(textAreaChat);
		panel_2.setLayout(gl_panel_2);
		
		JLabel label_3 = new JLabel("Message:");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFieldMessage = new JTextField();
		textFieldMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(client != null) {
					client.sendMessageChat(new ChatMessage(client.getUsername() +" >> "+ textFieldMessage.getText()));
				}else JOptionPane.showConfirmDialog(null, "You are not connected to the server to message", "Notification", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
				textFieldMessage.setText("");
			}
		});
		
		textFieldMessage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textFieldMessage.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(textFieldServerAddress, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(textFieldUsername, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
								.addComponent(textFieldPortNumber, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
									.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(3))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(1)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textFieldMessage, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
					.addGap(2))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldServerAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldPortNumber, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldUsername, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnLogin))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldMessage, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
					.addGap(194))
		);
		panel_1.setLayout(gl_panel_1);
		validate();
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
		client.messageLogout();
		client.disconnect();
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

