package Shape;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;

import Frame.WhiteBoardClient;
import Object.GraphicAdapter;

public class MyTriangle implements Class2D, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5739858909496958747L;
	private Polygon Triangle;
	private Color lineColor = null;
	private Color color = null;
	public MyTriangle() {
		
	}
	public MyTriangle(Polygon pol) {
		this.Triangle = pol;
	}
	public MyTriangle(Polygon pol, Color lc) {
		this.Triangle = pol;
		this.lineColor = lc;
	}
	public MyTriangle(Polygon pol, Color lc, Color c) {
		this.Triangle = pol;
		this.lineColor = lc;
		this.color = c;
	}
	@Override
	public void makeObject(Point startDrag, Point endDrag){
		setLineColor(WhiteBoardClient.selectColor);
		int midx;
		int midy;
		if (startDrag.x > endDrag.x){
			midx = endDrag.x +(Math.abs(startDrag.x - endDrag.x));
			midy = endDrag.y;
		}
		else{
			midx = endDrag.x - (Math.abs(startDrag.x - endDrag.x));
			midy = endDrag.y;
		}
		int[] xs = {startDrag.x, endDrag.x, midx};
		int[] ys = { startDrag.y, endDrag.y, midy};
		Polygon pol = new Polygon(xs, ys, xs.length);
		this.setTriangle(pol);
	}
	
	public void makeTriangle(int x1, int y1, int x2, int y2){
		int midx;
		int midy;
		if (x1 > x2){
			midx = x2 +(Math.abs(x1 - x2));
			midy = y2;
		}
		else{
			midx = x2 -(Math.abs(x1 - x2));
			midy = y2;
		}
		int[] xs = { x1, x2, midx};
		int[] ys = { y1, y2, midy};
		Polygon pol = new Polygon(xs, ys, xs.length);
		this.setTriangle(pol);
	}
	
	@Override
	public void draw(GraphicAdapter g) {
		if(getColor() == null){
    		g.getGraphicAdapter().setColor(getLineColor());
    		g.getGraphicAdapter().drawPolygon(getTriangle());
    	}
		else{
			g.getGraphicAdapter().setColor(getColor());
			g.getGraphicAdapter().fillPolygon(getTriangle());
		}
	}
	@Override
    public void fill(Color c){
		this.setColor(c);
	}
	
	@Override
	public boolean contains(Point p) {
		return getTriangle().contains(p);
	}

	@Override
	public void move(Point startDrag, Point endDrag) {
		int[] xs = {0, 0, 0};
		int[] ys = {0, 0, 0};
		for(int k = 0; k < 3; k++ ){
			xs[k] = this.getTriangle().xpoints[k] + endDrag.x - startDrag.x;
			ys[k] = this.getTriangle().ypoints[k] + endDrag.y - startDrag.y;
		}
		this.setTriangle(new Polygon(xs,ys,3));
	}
	
	@Override
    public void writetoFile(BufferedWriter b){
    	try {
    		b.write(getClass().getSimpleName() + ";");
    		b.write(getTriangle().xpoints[0] + ";" + getTriangle().ypoints[0] + ";");
    		b.write(getTriangle().xpoints[1] + ";" + getTriangle().ypoints[1] + ";");
    		b.write(getLineColor().getRed() + ";" + getLineColor().getGreen() + ";" + getLineColor().getBlue() +";");
    		if(getColor()==null){
    			b.write("null" + ";" +"null" +";" + "null");
    		}else{
    			b.write(getColor().getRed() + ";" + getColor().getGreen() + ";" + getColor().getBlue());
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public Polygon getTriangle() {
		return Triangle;
	}
	public void setTriangle(Polygon triangle) {
		Triangle = triangle;
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
