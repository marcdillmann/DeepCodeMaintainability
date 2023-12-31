package DatePicker;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * This an external class - it's more of an implementation of the datepicker
 * @author User1
 */
public class JNextIcon implements Icon
{
	
   /**
    *
    */
   protected int width;
   /**
    *
    */
   protected int height;

   /**
    *
    */
   protected int[] xPoints = new int[3];
   /**
    *
    */
   protected int[] yPoints = new int[3];
	
	private boolean doubleArrow;

   /**
    * 
    * @param width
    * @param height
    */
   public JNextIcon(int width, int height)
   {
		setDimension(width, height);
		doubleArrow = false;
	}

   /**
    * 
    * @param width
    * @param height
    * @param doubleArrow
    */
   public JNextIcon(int width, int height, boolean doubleArrow)
   {
		setDimension(width, height);
		this.doubleArrow = doubleArrow;
	}

   /**
    * 
    * @param doubleArrow
    */
   public void setDoubleArrow(boolean doubleArrow)
   {
		this.doubleArrow = doubleArrow;
	}

   /**
    * 
    * @return
    */
   public boolean getDoubleArrow()
   {
		return doubleArrow;
	}

	public int getIconWidth() {
		return width;
	}

	public int getIconHeight() {
		return height;
	}

   /**
    * 
    * @param width
    * @param height
    */
   public void setDimension(int width, int height)
   {
		this.width = width;
		this.height = height;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (doubleArrow) {
			xPoints[0] = x + (width / 2);
			yPoints[0] = y + (height / 2);

			xPoints[1] = x;
			yPoints[1] = y - 1;

			xPoints[2] = x;
			yPoints[2] = y + height;

			g.setColor(Color.BLACK);
			g.fillPolygon(xPoints, yPoints, 3);

			xPoints[0] = x + width;
			yPoints[0] = y + (height / 2);

			xPoints[1] = x + (width / 2);
			yPoints[1] = y - 1;

			xPoints[2] = x + (width / 2);
			yPoints[2] = y + height;

			g.setColor(Color.BLACK);
			g.fillPolygon(xPoints, yPoints, 3);
		} else {
			xPoints[0] = x + width;
			yPoints[0] = y + (height / 2);
			
			xPoints[1] = x;
			yPoints[1] = y - 1;
			
			xPoints[2] = x;
			yPoints[2] = y + height;
			
			g.setColor(Color.BLACK);
			g.fillPolygon(xPoints, yPoints, 3);
		}
	}
}

