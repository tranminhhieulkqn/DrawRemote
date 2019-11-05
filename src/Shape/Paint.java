package Shape;

import java.awt.BasicStroke;
import java.awt.Point;
import java.io.BufferedWriter;

import Object.GraphicAdapter;
import Object.InfoPaint;
import Object.User;

public interface Paint{
	public InfoPaint infoPaint = null;
	public User user = null;
	public BasicStroke basicStroke = new BasicStroke(1);
	public void draw(GraphicAdapter g); //Chuc nang chinh: Draw - ve doi tuong
	public boolean contains(Point p);	//Kiem tra diem co thuoc doi tuong
	public void move(Point startDrag, Point endDrag);	//Ham di chuyen doi tuong
	public void writetoFile(BufferedWriter b);	//Ghi cac gia tri doi tuong ra file text
	public void makeObject(Point startDrag, Point endDrag);	//Tao doi tuong
}
