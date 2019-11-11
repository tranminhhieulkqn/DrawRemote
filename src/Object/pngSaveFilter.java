package Object;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author MinhHieu, TuDuyen
 * Support save file to PNG
 */
public class pngSaveFilter extends FileFilter
{ 
	public boolean accept(File f)
	{
		if (f.isDirectory())
        {
           return false;
        }
		String s = f.getName();
		return s.endsWith(".png")||s.endsWith(".PNG");
  }
  public String getDescription() 
  {
      return "*.png,*.PNG";
  }
}
