package Object;

import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class GraphicAdapter{
	public Graphics2D GraphicAdapter;
	public GraphicAdapter(){
	}
	public GraphicAdapter(Graphics g){
		this.GraphicAdapter = (Graphics2D) g;
	}
	public GraphicAdapter(Graphics2D g){
		this.GraphicAdapter = g;
	}
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