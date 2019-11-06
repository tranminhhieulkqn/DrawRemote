package Shape;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import Frame.WhiteBoardClient;
import Object.GraphicAdapter;

public class MyLine extends Class1D implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7449909779562255476L;
	
	private Line line;
	
	private Color color;
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Line getLine() {
		return line;
	}
	public void setLine(Line line) {
		this.line = line;
	}
	
	public MyLine(){
		
	}
	public MyLine(Line ln){
		this.line = ln;
	}
	public MyLine(Point staPoint, Point enPoint){
		this.line = new Line(staPoint, enPoint);
	}
	public MyLine(Point staPoint, Point enPoint, Color c){
		this.line = new Line(staPoint, enPoint);
		this.color = c;
	}
	
	@Override
	public void draw(GraphicAdapter g){
		g.getGraphicAdapter().setColor(getColor());
		g.getGraphicAdapter().drawLine(getLine().startX.x, getLine().startX.y, getLine().startY.x, getLine().startY.y);
	}
	
    @Override
    public boolean contains(Point p) {
    	//return this.getLine().contains(p.x, p.y);
    	return true;
    }
    @Override
    public void move(Point startDrag, Point endDrag){
    	Point startPoint = new Point((int)getLine().startX.x + (endDrag.x - startDrag.x) , (int)getLine().startX.y + (endDrag.y - startDrag.y));
    	Point endPoint = new Point((int)getLine().startY.x + (endDrag.x - startDrag.x), (int)getLine().startY.y + (endDrag.y - startDrag.y));
    	Line ln = new Line(startPoint, endPoint);
    	this.setLine(ln);
    }
    
    @Override
    public void writetoFile(BufferedWriter b){
    	try {
    		b.write(getClass().getSimpleName() + ";");
    		b.write((int)getLine().getStartX().x + ";" + (int)getLine().getStartX().y + ";" + (int)getLine().getStartY().x + ";" + (int)getLine().getStartY().y + ";");
    		b.write(getColor().getRed() + ";" + getColor().getGreen() + ";" + getColor().getBlue());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @Override
    public void makeObject(Point startDrag, Point endDrag){
    	Line ln = new Line(startDrag, endDrag);
    	setLine(ln);
    	setColor(WhiteBoardClient.selectColor);
    }

	
}
