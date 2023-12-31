package DatePicker;
import java.awt.Component;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * This an external class - it's more of an implementation of the datepicker
 * @author User1
 */
public class JPreviousIcon implements Icon
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

	private boolean doubleArrow = false;

   /**
    * 
    * @param width
    * @param height
    */
   public JPreviousIcon(int width, int height)
   {
		setDimension(width, height);
	}

   /**
    * 
    * @param width
    * @param height
    * @param doubleArrow
    */
   public JPreviousIcon(int width, int height, boolean doubleArrow)
   {
		setDimension(width, height);
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

   /**
    * 
    * @param doubleArrow
    */
   public void setDoubleArrow(boolean doubleArrow)
   {
		this.doubleArrow = doubleArrow;
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
			xPoints[0] = x;
			yPoints[0] = y + (height / 2);
			
			xPoints[1] = x + (width / 2);
			yPoints[1] = y - 1;
			
			xPoints[2] = x + (width / 2);
			yPoints[2] = y + height;
			
			g.setColor(Color.BLACK);
			g.fillPolygon(xPoints, yPoints, 3);

			xPoints[0] = x + (width / 2);
			yPoints[0] = y + (height / 2);
			
			xPoints[1] = x + width;
			yPoints[1] = y - 1;
			
			xPoints[2] = x + width;
			yPoints[2] = y + height;
			
			g.setColor(Color.BLACK);
			g.fillPolygon(xPoints, yPoints, 3);
		} else {
			xPoints[0] = x;
			yPoints[0] = y + (height / 2);
			
			xPoints[1] = x + width;
			yPoints[1] = y - 1;
			
			xPoints[2] = x + width;
			yPoints[2] = y + height;
			
			g.setColor(Color.BLACK);
			g.fillPolygon(xPoints, yPoints, 3);
		}
	}
}

