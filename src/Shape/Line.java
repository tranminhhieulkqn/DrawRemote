package Shape;

import java.awt.Point;
import java.io.Serializable;

/**
 * @author MinhHieu
 * Because the class Line in javafx.scene.shape does not support Serialization Object
 * Create class Line to suport class MyLine
 * 
 */
public class Line implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Point startX;//
	Point startY;
	
	//Data encapsulation
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
	
	//Constructor
	public Line(Point startX, Point startY) {
		// TODO Auto-generated constructor stub
		this.startX = startX;
		this.startY = startY;
	}
}