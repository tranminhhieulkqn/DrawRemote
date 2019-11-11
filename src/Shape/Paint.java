package Shape;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.Serializable;

import Object.GraphicAdapter;
import Object.InfoPaint;

/**
 * @author MinhHieu, VanAnh
 *
 */
public class Paint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5827467158791659131L;
	public InfoPaint infoPaint = null;//Used to save strokes and information related to strokes -- to expand
	private String user;//Save the username on the brush stroke
	
	
	//Data encapsulation
	public String getUser() {
		return this.user;
	}
	public void setUser(String user) {
		 if(this.user == null) this.user = user;//Khong cho set lai neu da co user truoc do
	}
	
	//Functions
	public void draw(GraphicAdapter g) { //The function is to draw objects on graphics
		
	}; 
	public boolean contains(Point p) {//Check if the point belongs to the object
		return false;
	};	
	public void move(Point startDrag, Point endDrag) {//Object moving function
		
	};	
	public void writetoFile(BufferedWriter b) {//Write to file to save as text file (txt)
		
	};	
	public void makeObject(Point startDrag, Point endDrag) {//Create object
		
	};	
}
