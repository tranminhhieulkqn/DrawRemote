package Object;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

/**
 * @author MinhHieu
 * Class used to extend application
 * 
 */
public class InfoPaint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4390679039754063371L;
	public Point startDrag = new Point(0, 0);// Điểm vẽ bắt đầu
	public Point endDrag = new Point(0, 0);//Điểm vẽ kết thúc
	public DrawType drawType = null;//Chọn cách vẽ ban đầu là Pen
	public Color color = Color.BLACK;//Chọn màu vẽ
	public BasicStroke stroke = new BasicStroke(1);
	
	//Data encapsulation
	public Point getStartDrag() {
		return startDrag;
	}

	public void setStartDrag(Point startDrag) {
		this.startDrag = startDrag;
	}

	public Point getEndDrag() {
		return endDrag;
	}

	public void setEndDrag(Point endDrag) {
		this.endDrag = endDrag;
	}

	public DrawType getDrawType() {
		return drawType;
	}

	public void setDrawType(DrawType drawType) {
		this.drawType = drawType;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BasicStroke getStroke() {
		return stroke;
	}

	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public InfoPaint() {
		// TODO Auto-generated constructor stub
	}
	
	public InfoPaint(DrawType drawType, int sizeStroke) {
		// TODO Auto-generated constructor stub
		this.drawType = drawType;
		this.stroke = new BasicStroke(sizeStroke);
	}
	
	
	//Constructor
	public InfoPaint(InfoPaint infoPaint) {
		// TODO Auto-generated constructor stub
		this.startDrag 	= infoPaint.startDrag;
		this.endDrag 	= infoPaint.endDrag;
		this.drawType 	= infoPaint.drawType;
		this.color 		= infoPaint.color;
		this.stroke 	= infoPaint.stroke;
	}
}
