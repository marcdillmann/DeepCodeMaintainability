package TBA.MouseGestures;

import java.util.Vector;
import javax.swing.ImageIcon;

/**
 * This class represents a single mouse gesture.
 * @author Dan McGrath
 */
public class Gesture
{
   private String gestureName;
   private ImageIcon gestureImage;
   private Vector<String> patterns;

   /**
    * @return the gestureName
    */
   public String getGestureName()
   {
      return gestureName;
   }

   /**
    * @param gestureName the gestureName to set
    */
   public void setGestureName(String gestureName)
   {
      this.gestureName = gestureName;
   }

   /**
    * @return the gestureImage
    */
   public ImageIcon getGestureImage()
   {
      return gestureImage;
   }

   /**
    * @param gestureImage the gestureImage to set
    */
   public void setGestureImage(ImageIcon gestureImage)
   {
      this.gestureImage = gestureImage;
   }

   /**
    * @return the patterns
    */
   public Vector<String> getPatterns()
   {
      return patterns;
   }

   /**
    * @param patterns the patterns to set
    */
   public void setPatterns(Vector<String> patterns)
   {
      this.patterns = patterns;
   }

   /**
    * @param pattern Add a pattern
    */
   public void addPattern(String pattern)
   {
      if(patterns == null)
      {
         patterns = new Vector<String>();
      }

      patterns.add(new String(pattern));
   }

   /**
    * Returns whether or not this gesture has been executed.
    * @param currentPattern
    * @return true if active false if not
    */
   public boolean isActive(String currentPattern)
   {
      if(patterns != null)
      {
         for(String pattern : patterns)
         {
            if(currentPattern.startsWith(pattern))
            {
               return true;
            }
         }
      }

      return false;
   }

   /**
    * Returns whether or not this gesture has been cancelled.
    * @param currentPattern
    * @return true if cancelled false if not
    */
   public boolean isCancelled(String currentPattern)
   {
      if(patterns != null)
      {
         for(String pattern : patterns)
         {
            if(currentPattern.endsWith(pattern))
            {
               return true;
            }
         }
      }

      return false;
   }
}
