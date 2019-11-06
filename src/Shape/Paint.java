package Shape;

import java.awt.BasicStroke;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.Serializable;

import Object.GraphicAdapter;
import Object.InfoPaint;

public class Paint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5827467158791659131L;
	public InfoPaint infoPaint = null;
	private String user;
	public String getUser() {
		return this.user;
	}
	public void setUser(String user) {
		 if(this.user == null) this.user = user;
	}
	public void draw(GraphicAdapter g) { //Chuc nang chinh: Draw - ve doi tuong
		
	}; 
	public boolean contains(Point p) {//Kiem tra diem co thuoc doi tuong
		return false;
	};	
	public void move(Point startDrag, Point endDrag) {//Ham di chuyen doi tuong
		
	};	
	public void writetoFile(BufferedWriter b) {//Ghi cac gia tri doi tuong ra file text
		
	};	
	public void makeObject(Point startDrag, Point endDrag) {//Tao doi tuong
		
	};	
}
