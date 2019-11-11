package Paint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JComponent;


import Frame.WhiteBoardClient;
import Object.DrawType;
import Object.GraphicAdapter;
import Object.OpenFile;
import Shape.Class2D;
import Shape.MyCircle;
import Shape.MyLine;
import Shape.MyOval;
import Shape.MyPoint;
import Shape.MyRectangle;
import Shape.MySquare;
import Shape.MyTriangle;
import Shape.Paint;
import Socket.Client;
import Socket.Server;

/**
 * @author MinhHieu, VanAnh
 * Class used to draw
 * The class tried to add a mouse event
 * Because it is still limited to call the two client and server classes, it is necessary to distinguish to create suport functions
 * 
 */
@SuppressWarnings("serial")
public class PaintApp extends JComponent {
	public Point startDrag, endDrag; //Get the start and end points of the mouse
	public Paint ptemp;
	public BasicStroke basicStroke;
	public ArrayList<Paint> listPaint = new ArrayList<Paint>();
	public ArrayList<Paint> listPaintServer = new ArrayList<Paint>();;
	private DrawType drawType;
	private Client client = null;
	public String user = null;
	private Server server = null;
	
	//Data Encapsulation
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
		this.user = client.getUsername();
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.user = "Server";
		this.server = server;
	}
	public DrawType getDrawType() {
		return drawType;
	}
	public void setDrawType(DrawType drawType) {
		this.drawType = drawType;
	}
	
	//Self create functions
	/**
	 * @param paint
	 * Support for deleting in any list?
	 */
	void supportRemove(Paint paint) {
		if(server == null) listPaint.remove(paint);
		if(server != null) listPaintServer.remove(paint);
	}
	/**
	 * @param paint
	 * Support for adding in any list?
	 */
	void supportAdd(Paint paint) {
		if(server == null) listPaint.add(paint);
		if(server != null) listPaintServer.add(paint);
	}
	/**
	 * @param listPaint
	 * Determine whether the current form is a Server or a new Client submitted appropriately
	 */
	void supportSend(ArrayList<Paint> listPaint) {
		if(client != null) {
			client.sendListPaint(listPaint);
		}
		if(server != null) {
			server.sendListPaint(listPaint);
		}
	}
	
	/**
	 * @param user
	 * Display the username of the brush stroke on the form
	 */
	void showNetVe(String user) {
		if(client != null) client.showNetVe(user);
		if(server != null) server.showNetVe(user);
	}
	
	//Constructor
	public PaintApp() {
		this.drawType = DrawType.Pen;//Set ogrinal DrawType

		this.addMouseListener(new MouseAdapter() {
			//Attach mousePressed
			public void mousePressed(MouseEvent e) {
				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				ArrayList<Paint> temp;
				if(server == null) temp = listPaint;
				else temp = listPaintServer;
				if(drawType == DrawType.Move){
					for (Paint item : temp) {
						if(item.contains(startDrag)){
		        			  ptemp = item;
		        			  supportRemove(item); break;
		        		  }
					}
				}
				else if(drawType == DrawType.Delete ){
					for (Paint item : temp) {
						if(item.contains(startDrag)){
							ptemp = null;
							supportRemove(item); break;
						}
					}
				}
				supportSend(listPaint);
				repaint();
			}
			//Attach mouseReleased
			public void mouseReleased(MouseEvent e) {
				Paint obj;
	        	Point p = new Point(e.getX(), e.getY());
	        	switch (drawType) {
				case Rectangle:
					obj = new MyRectangle();
					obj.setUser(user);
	        		obj.makeObject(startDrag, p);
	    			supportAdd(obj);
					break;
				case Square:
					obj = new MySquare();
					obj.setUser(user);
	        		obj.makeObject(startDrag, p);
	    			supportAdd(obj);
					break;
				case Triangle:
					obj = new MyTriangle();
					obj.setUser(user);
	        		obj.makeObject(startDrag, p);
	    			supportAdd(obj);
	    			break;
				case Circle:
					obj = new MyCircle();
					obj.setUser(user);
	        		obj.makeObject(startDrag, p);
	    			supportAdd(obj);
					break;
				case Oval:
					obj = new MyOval();
					obj.setUser(user);
	        		obj.makeObject(startDrag, p);
	    			supportAdd(obj);
					break;
				case Line:
					obj = new MyLine(startDrag, new Point(e.getX(),  e.getY()), WhiteBoardClient.selectColor);
					obj.setUser(user);
	        		obj.makeObject(startDrag, p);
	    			supportAdd(obj);
					break;
				case Fill:
					for(int i = listPaint.size() - 1; i >= 0; i--){
						Paint item = listPaint.get(i);
	        			try {
	        				Class2D temp = (Class2D)item;
	        				if(temp.contains(startDrag)){
		        				try {
		        					temp.fill(WhiteBoardClient.selectColor);
								} catch (Exception ex) {
									// TODO: handle exception
								}
			        			break;
		        			}
						} catch (Exception ex) {
							// TODO: handle exception
						}
	        		}
					break;
				case Move:
					if(ptemp.contains(startDrag)){
        				ptemp.move(startDrag, p);
    					supportAdd(ptemp);
        			}
					break;
				default:
					break;
				}
	        	//End of the mouseover event then repaint
	        	startDrag = null;
	        	endDrag = null;
	        	supportSend(listPaint);
	        	repaint();
	        }
	      });

		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
	        public void mouseDragged(MouseEvent e) {
	        	endDrag = new Point(e.getX(), e.getY());
	        	Paint obj;
	        	if(drawType == DrawType.Pen) {
	        		obj = new MyPoint();
	        		obj.setUser(user);
	        		obj.makeObject(endDrag, endDrag);
	    			supportAdd(obj);
	        	}
	        	supportSend(listPaint);
	        	repaint();
	        }
	        @Override
	        public void mouseMoved(MouseEvent e) {
	        	// TODO Auto-generated method stub
	        	if(server != null) {
	        		for (Paint item : listPaintServer) {
						if(item.contains(new Point(e.getX(), e.getY()))){
							showNetVe(item.getUser());
							break;
						}
					}
	        	}
        		for (Paint item : listPaint) {
					if(item.contains(new Point(e.getX(), e.getY()))){
						showNetVe(item.getUser());
						break;
					}
				}
	        }
	      });
		
	}
	/**
	 * Main drawing function
	 */
	public void paint(Graphics g) {
		GraphicAdapter g2 = new GraphicAdapter() { };
		g2.setGraphicAdapter(g);
		basicStroke = new BasicStroke(7);
		g2.getGraphicAdapter().setStroke(basicStroke);
		if(OpenFile.image != null){
			g2.getGraphicAdapter().drawImage(OpenFile.image, 0, 0, null);
			repaint();
		}
		for (Paint pt : listPaint){
			pt.draw(g2);
		}
		if(server != null) {
			for (Paint pt : listPaintServer){
				pt.draw(g2);
			}
		}
		Paint obj;
		if (startDrag != null && endDrag != null) {
			switch (drawType) {
			case Rectangle:
				obj = new MyRectangle();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Square:
				obj = new MySquare();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Oval:
				obj = new MyOval();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Circle:
				obj = new MyCircle();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Pen:
				obj = new MyPoint();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Line:
				obj = new MyLine();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Triangle:
				obj = new MyTriangle();
				obj.makeObject(startDrag, endDrag);
				obj.draw(g2);
				break;
			case Move:
				if(ptemp instanceof MyRectangle){
    				MyRectangle r = (MyRectangle)ptemp;
    				if(r.contains(startDrag)){
    					if(r.getColor() == null){
    						g2.getGraphicAdapter().setColor(r.getLineColor());
    						g2.getGraphicAdapter().drawRect(r.getRect().x + endDrag.x - startDrag.x, r.getRect().y + endDrag.y - startDrag.y, r.getRect().width, r.getRect().height);
    					}else {
    						g2.getGraphicAdapter().setColor(r.getColor());
    						g2.getGraphicAdapter().fillRect(r.getRect().x + endDrag.x - startDrag.x, r.getRect().y + endDrag.y - startDrag.y, r.getRect().width, r.getRect().height);
    						g2.getGraphicAdapter().drawRect(r.getRect().x + endDrag.x - startDrag.x, r.getRect().y + endDrag.y - startDrag.y, r.getRect().width, r.getRect().height);
						}
    				}
    			}else if (ptemp instanceof MyOval) {
    				MyOval oval = (MyOval)ptemp;
    				if(oval.contains(startDrag)){
    					if(oval.getColor() == null){
    						g2.getGraphicAdapter().setColor(oval.getLineColor());
    						g2.getGraphicAdapter().drawOval((int)(oval.getElip2d().getX() + endDrag.x - startDrag.x),(int)(oval.getElip2d().getY() + endDrag.y - startDrag.y), (int)oval.getElip2d().getWidth(), (int)oval.getElip2d().getHeight());
    					}else{
    						g2.getGraphicAdapter().setColor(oval.getColor());
    						g2.getGraphicAdapter().fillOval((int)(oval.getElip2d().getX() + endDrag.x - startDrag.x),(int)(oval.getElip2d().getY() + endDrag.y - startDrag.y), (int)oval.getElip2d().getWidth(), (int)oval.getElip2d().getHeight());
    					}
    				}
				}else if (ptemp instanceof MyTriangle) {
    				MyTriangle tr = (MyTriangle)ptemp;
    				if(tr.contains(startDrag)){
    					int[]x = {tr.getTriangle().xpoints[0] + endDrag.x - startDrag.x,tr.getTriangle().xpoints[1] + endDrag.x - startDrag.x, tr.getTriangle().xpoints[2] + endDrag.x - startDrag.x};
    					int[]y = {tr.getTriangle().ypoints[0] + endDrag.y - startDrag.y,tr.getTriangle().ypoints[1] + endDrag.y - startDrag.y, tr.getTriangle().ypoints[2] + endDrag.y - startDrag.y};
    					if(tr.getColor()==null){
    						g2.getGraphicAdapter().setColor(tr.getLineColor());
    						g2.getGraphicAdapter().drawPolygon(x,y,3);
    					}else{
    						g2.getGraphicAdapter().setColor(tr.getColor());
    						g2.getGraphicAdapter().fillPolygon(x,y,3);
    						g2.getGraphicAdapter().drawPolygon(x,y,3);
    					}
    				}
				}else if (ptemp instanceof MyLine) {
    				MyLine tr = (MyLine)ptemp;
    				if(tr.contains(startDrag)){
    						g2.getGraphicAdapter().setColor(tr.getColor());
    						g2.getGraphicAdapter().drawLine(
    								(int)(tr.getLine().getStartX().x + endDrag.x - startDrag.x), 
    								(int)(tr.getLine().getStartX().y + endDrag.y - startDrag.y), 
    								(int)(tr.getLine().getStartY().x + endDrag.x - startDrag.x), 
    								(int)(tr.getLine().getStartY().y + endDrag.y - startDrag.y));
    				}
				}
				break;
			default:
				break;
			}	
		}
	//
	}
}
