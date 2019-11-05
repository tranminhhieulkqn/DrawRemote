package Shape;

import java.awt.Rectangle;
import java.io.Serializable;

public class MySquare extends MyRectangle implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2636124788206214756L;

	public void setRect(Rectangle rect) {
		rect.setBounds(rect.x, rect.y , rect.width, rect.width);
		super.setRect(rect);
	}
}
