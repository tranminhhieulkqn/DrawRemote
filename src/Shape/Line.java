package Shape;

import java.awt.Point;
import java.io.Serializable;

public class Line implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Point startX;
	Point startY;
	public Point getStartX() {
		return startX;
	}
	public void setStartX(Point startX) {
		this.startX = startX;
	}
	public Point getStartY() {
		return startY;
	}
	public void setStartY(Point startY) {
		this.startY = startY;
	}
	public Line(Point startX, Point startY) {
		// TODO Auto-generated constructor stub
		this.startX = startX;
		this.startY = startY;
	}
}