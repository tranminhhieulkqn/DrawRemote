package Shape;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import Frame.WhiteBoardClient;
import Object.GraphicAdapter;

public class MyRectangle extends Class2D implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -52280554150878525L;
	private Rectangle rect;
	private Color lineColor;
	private Color color = null;
	
	public MyRectangle() {
		
	}
	public MyRectangle(Rectangle r) {
		this.rect = r;
	}
	public MyRectangle(Rectangle r, Color lc) {
		this.rect = r;
		this.lineColor = lc;
	}
	public MyRectangle(Rectangle r, Color lc, Color c) {
		this.rect = r;
		this.lineColor = lc;
		this.color = c;
	}
	
	public void makeObject(Point startDrag, Point endDrag) {
		setLineColor(WhiteBoardClient.selectColor);
		Rectangle r = new Rectangle(Math.min(startDrag.x, endDrag.x), Math.min(startDrag.y, endDrag.y), Math.abs(startDrag.x - endDrag.x), Math.abs(startDrag.y - endDrag.y));	
		this.setRect(r);
	}
	
	public void makeRectangle(int x, int y, int w, int h) {
		Rectangle r = new Rectangle(x, y, w, h);
	    this.setRect(r);
	}
	
	/*public void makeRectangle(Point startDrag, MouseEvent e) {
		Rectangle r = new Rectangle(Math.min(startDrag.x, e.getX()), Math.min(startDrag.y, e.getY()), Math.abs(startDrag.x - e.getX()), Math.abs(startDrag.y - e.getY()));
	    this.setRect(r);
	}*/
	
    @Override
	public void draw(GraphicAdapter g) {
    	if(getColor() == null){
    		g.getGraphicAdapter().setColor(this.getLineColor());
    		g.getGraphicAdapter().drawRect(this.getRect().x, this.getRect().y, this.getRect().width, this.getRect().height);
    	}
    	else{
    		g.getGraphicAdapter().setColor(this.getColor());
        	g.getGraphicAdapter().fillRect(this.getRect().x, this.getRect().y, this.getRect().width, this.getRect().height);
        	g.getGraphicAdapter().drawRect(this.getRect().x, this.getRect().y, this.getRect().width, this.getRect().height);
    	}
    }
    @Override
    public void fill(Color c){
		this.setColor(c);
	}
    @Override
    public boolean contains(Point p) {
    	return this.getRect().contains(p);
    }
    @Override
    public void move(Point startDrag, Point endDrag){
    	Rectangle r = new Rectangle(this.getRect().x + (endDrag.x - startDrag.x),this.getRect().y + (endDrag.y - startDrag.y), this.getRect().width, this.getRect().height);
    	this.setRect(r);
    }

    public void writetoFile(BufferedWriter b){
    	try {
			b.write(getClass().getSimpleName() + ";");
			b.write(getRect().x + ";" + getRect().y + ";" + getRect().width + ";" + getRect().height + ";");
			b.write(getLineColor().getRed() + ";" +getLineColor().getGreen() + ";" + getLineColor().getBlue() +";");
			if(getColor()==null){
				b.write("null" + ";" +"null" +";" + "null");
			}else{
				b.write(getColor().getRed() + ";" + getColor().getGreen() + ";" + getColor().getBlue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	public Rectangle getRect() {
		return rect;
	}
	public void setRect(Rectangle rect) {
		this.rect = rect;
	}
	public Color getLineColor() {
		return lineColor;
	}
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

}

