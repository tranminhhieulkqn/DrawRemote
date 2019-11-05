package Paint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JComponent;

import com.sun.javafx.geom.Rectangle;

import Frame.WhiteBoardClient;

import Object.DrawType;
import Object.GraphicAdapter;
import Object.InfoPaint;
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
import sun.security.util.ArrayUtil;

import javafx.scene.shape.Line;

@SuppressWarnings("serial")
public class PaintApp extends JComponent {
	public Point startDrag, endDrag;
	public Paint ptemp;
	public BasicStroke basicStroke;
	public ArrayList<Paint> listPaint = new ArrayList<Paint>();
	private DrawType drawType;
	private Client client = null;
	private Server server = null;
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}

	public DrawType getDrawType() {
		return drawType;
	}
	public void setDrawType(DrawType drawType) {
		this.drawType = drawType;
	}
	
	void supportSend(ArrayList<Paint> listPaint) {
		if(client != null) client.sendListPaint(listPaint);
		if(server != null) {
			System.out.print("=======================================================" + "\n");
			for (Paint paint : listPaint) {
				System.out.print(paint.toString() + "\n");
			}
			server.sendListPaint(listPaint);
		}
	}
	public PaintApp() {
		this.drawType = DrawType.Pen;
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				if(drawType == DrawType.Move){
					for (Paint item : listPaint) {
						if(item.contains(startDrag)){
		        			  ptemp = item;
		        			  listPaint.remove(item); break;
		        		  }
					}
				}
				else if(drawType == DrawType.Delete ){
					for (Paint item : listPaint) {
						if(item.contains(startDrag)){
							ptemp = null;
							listPaint.remove(item); break;
						}
					}
				}
				supportSend(listPaint);
				repaint();
			}
			public void mouseReleased(MouseEvent e) {
				Paint obj;
	        	Point p = new Point(e.getX(), e.getY());
	        	switch (drawType) {
				case Rectangle:
					obj = new MyRectangle();
	        		obj.makeObject(startDrag, p);
	    			listPaint.add(obj);
					break;
				case Square:
					obj = new MySquare();
	        		obj.makeObject(startDrag, p);
	    			listPaint.add(obj);
					break;
				case Triangle:
					obj = new MyTriangle();
	        		obj.makeObject(startDrag, p);
	    			listPaint.add(obj);
	    			break;
				case Circle:
					obj = new MyCircle();
	        		obj.makeObject(startDrag, p);
	    			listPaint.add(obj);
					break;
				case Oval:
					obj = new MyOval();
	        		obj.makeObject(startDrag, p);
	    			listPaint.add(obj);
					break;
				case Line:
					obj = new MyLine(startDrag, new Point(e.getX(),  e.getY()), WhiteBoardClient.selectColor);
	        		obj.makeObject(startDrag, p);
	    			listPaint.add(obj);
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
    					listPaint.add(ptemp);
        			}
					break;
				default:
					break;
				}
	        	//Kết thúc phiên rê chuột đặt lại đồng thời repaint
	        	startDrag = null;
	        	endDrag = null;
	        	supportSend(listPaint);
	        	repaint();
	        }
	      });

		this.addMouseMotionListener(new MouseMotionAdapter() {
	        public void mouseDragged(MouseEvent e) {
	        	endDrag = new Point(e.getX(), e.getY());
	        	Paint obj;
	        	if(drawType == DrawType.Pen) {
	        		obj = new MyPoint();
	        		obj.makeObject(endDrag, endDrag);
	    			listPaint.add(obj);
	        	}
	        	supportSend(listPaint);
	        	repaint();
	        }
	      });
	}
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
