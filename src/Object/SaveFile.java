package Object;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Shape.Paint;

/**
 * @author MinhHieu
 * Support save file
 */
@SuppressWarnings("serial")
public class SaveFile extends JFrame {
	public SaveFile(ArrayList<Paint> listPaint, int width, int height){
		JFileChooser saveFile = new JFileChooser();
		saveFile.addChoosableFileFilter(new pngSaveFilter());
		saveFile.addChoosableFileFilter(new txtSaveFilter());
        int sf = saveFile.showSaveDialog(saveFile);
        if(sf == JFileChooser.APPROVE_OPTION){
        	String ext = "";
        	String extension = saveFile.getFileFilter().getDescription();
           if(extension.equals("*.png,*.PNG"))
           { 
        	   ext = ".png";
        	   saveFile.setSelectedFile(new File(saveFile.getSelectedFile().toString() + ext));
           		try {
           			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
           			GraphicAdapter ig2 = new GraphicAdapter() {
           			};
           			ig2.setGraphicAdapter(bi.createGraphics());
           			ig2.getGraphicAdapter().setColor(Color.white);
    				ig2.getGraphicAdapter().fillRect(0, 0, width, height);
           			if(OpenFile.image != null){
           				ig2.getGraphicAdapter().drawImage(OpenFile.image, 0, 0, null);
           				repaint();
           			}
       			BasicStroke basicStroke = new BasicStroke(7);
       			ig2.getGraphicAdapter().setStroke(basicStroke);
           			for (Paint pt : listPaint){	
           				pt.draw(ig2);
           			}
           			File outputfile = saveFile.getSelectedFile();
           			ImageIO.write(bi, "png", outputfile);
           		}catch (IOException e) {
           			e.printStackTrace();
           		}
           }else if(extension.equals("*.txt,*.TXT")){
        	   ext = ".txt";
        	   try{
           			FileOutputStream fi = new FileOutputStream(saveFile.getSelectedFile() + ext); 
           			OutputStreamWriter out = new OutputStreamWriter(fi); 
           			BufferedWriter b = new BufferedWriter(out);
                   for(int i = 0; i<listPaint.size(); i++)
                   {
                   		Paint pt = listPaint.get(i);
                   		pt.writetoFile(b);
                   		b.newLine();
                   } 
                   b.close(); 
           		}catch(Exception e){
           			e.printStackTrace();
           		}
           }
        }
	}
}

