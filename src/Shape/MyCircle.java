package Shape;

import java.awt.geom.Ellipse2D;
import java.io.Serializable;

/**
 * @author VanAnh
 * Extends from Oval
 *
 */
public class MyCircle extends MyOval implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4013581204942922781L;

	//Functions override
	@Override
	public void setElip2d(Ellipse2D elip2d) {
		elip2d.setFrame(elip2d.getX(), elip2d.getY(), elip2d.getWidth(), elip2d.getWidth());
		super.setElip2d(elip2d);
	}	
}
