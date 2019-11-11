package Object;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @author MinhHieu
 * Convert all Gracphics and Gracphics2D into Gracphics2D 
 *
 */
public abstract class GraphicAdapter{
	
	public Graphics2D GraphicAdapter;
	
	//Constructor
	public GraphicAdapter() {
		// TODO Auto-generated constructor stub
	}
	public GraphicAdapter(Graphics g){
		// TODO Auto-generated constructor stub
		this.GraphicAdapter = (Graphics2D) g;
	}
	public GraphicAdapter(Graphics2D g){
		// TODO Auto-generated constructor stub
		this.GraphicAdapter = g;
	}
	
	//Data encapsulation
	public Graphics2D getGraphicAdapter() {
		return GraphicAdapter;
	}
	public void setGraphicAdapter(Graphics2D graphicAdapter) {
		GraphicAdapter = graphicAdapter;
	}
	public void setGraphicAdapter(Graphics graphicAdapter) {
		GraphicAdapter = (Graphics2D)graphicAdapter;
	}
	
}