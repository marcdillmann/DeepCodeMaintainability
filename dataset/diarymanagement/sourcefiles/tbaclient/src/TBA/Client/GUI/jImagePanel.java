package TBA.Client.GUI;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class is basically a JPanel with a background image.
 *<p>
 * @author Dan McGrath
 *<p>
 * @see javax.swing.JPanel
 */
public class jImagePanel extends JPanel
{
   private ImageIcon backgroundImage;

   /**
    * This gets the background image of this panel.
    * @return backgroundimage
    */
   public ImageIcon getBackgroundImage()
   {
      return backgroundImage;
   }

   /**
    * This sets the background image to use when repainting the panel.
    * @param backgroundImage The image used for the background
    */
   public void setBackgroundImage(ImageIcon backgroundImage)
   {
      this.backgroundImage = backgroundImage;
   }

   /**
    * This intialises the panel with the default background image.
    */
   public jImagePanel()
   {
      backgroundImage = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/BackgroundMain.png"));
   }

   /**
    * This overides the paintComponent method to redraw the background image
    * when repainting.
    * @param g See {@link javax.swing.JPanel#paintComponent(java.awt.Graphics)}
    */
   @Override
   public void paintComponent(Graphics g)
   {
      if(backgroundImage == null)
      {
         return;
      }

      int width = getWidth();
      int height = getHeight();
      int imageW = backgroundImage.getIconWidth();
      int imageH = backgroundImage.getIconHeight();

      // Tile the image to fill our area.
      for (int x = 0; x < width; x += imageW) {
        for (int y = 0; y < height; y += imageH) {
            g.drawImage(backgroundImage.getImage(), x, y, this);
        }
      }
   }
}
