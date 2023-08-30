package TBA.Client.GUI;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 * This class is basically a JLayeredPane with a basckground image.
 *<p>
 * @author Dan McGrath
 *<p>
 * @see javax.swing.JLayeredPane
 */
public class jImageLayeredPane extends JLayeredPane
{
   private ImageIcon backgroundImage;

   /**
    * This intialises the panel with the default background image.
    */
   public jImageLayeredPane()
   {
      backgroundImage = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/BackgroundMain.png"));
   }

   /**
    * This overides the paintComponent method to redraw the background image
    * when repainting.
    * @param g See {@link javax.swing.JLayeredPane#paintComponent(java.awt.Graphics)}
    */
   @Override
   public void paintComponent(Graphics g)
   {
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
