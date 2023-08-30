/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Images;

import javax.swing.ImageIcon;

/**
 * This class loads all needed images, so we only need to load them once.
 *<p>
 * @author Dan Mcgrath
 *
 * @version $Rev:: 132           $ $Date:: 2009-09-17 #$
 */
public class ImageResources
{
   // Hourpanel cell backgrounds
   /** backCell field is light grey   */
   public ImageIcon backCell;
   /** selectCell is white */
   public ImageIcon selectCell;
   /** pastcell is dark grey   */
   public ImageIcon pastCell;
   /**  nowCell is red  */
   public ImageIcon nowCell;
   /** nextCell is yellow */
   public ImageIcon nextCell;
   /**  soon cell is green */
   public ImageIcon soonCell;
   /**  red icon shown in the status bar when gesture is cancelled  */
   public ImageIcon mouseGestureOff;
   /**  blue icon shown in the statusbar when waiting for gesture */
   public ImageIcon mouseGestureWait;
   /**  green icon shown in the statusbar when gesturing  */
   public ImageIcon mouseGestureOn;

   /**
    * Constructor loads in all the cell images - this only gets called once when
    * needed and boject of this class gets passed down so they don't need to be
    * re-initialised
    */
   public ImageResources()
   {
      soonCell = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/CellSoon.png"));
      backCell = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/CellBack.png"));
      nowCell = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/CellNow.png"));
      nextCell = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/CellNext.png"));
      selectCell = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/CellSelect.png"));
      pastCell = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/CellPast.png"));
      mouseGestureOn = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/MGOn.png"));
      mouseGestureWait = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/MGWait.png"));
      mouseGestureOff = new javax.swing.ImageIcon(getClass().getResource("/TBA/Images/MGOff.png"));
   }
}
