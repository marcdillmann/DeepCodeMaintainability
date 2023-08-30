package TBA.MouseGestures;

import java.util.Vector;

/**
 *
 * @author Dan McGrath
 */
public class GesturesResource
{
   private Vector<Gesture> gestures = new Vector<Gesture>();

   /**
    * Initialises all the available mouse gesture types. Eventually this could
    * be read from a configuration file
    */
   public GesturesResource()
   {
      // Shake mouse horizontally
      Gesture newGesture = new Gesture();
      newGesture.setGestureName("Shake Sideways");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("LRLR");
      newGesture.addPattern("RLRL");
      gestures.add(newGesture);

      // Shake mouse vertically
      newGesture = new Gesture();
      newGesture.setGestureName("Shake Vertically");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("UDUD");
      newGesture.addPattern("DUDU");
      gestures.add(newGesture);

      // Clockwise circle
      newGesture = new Gesture();
      newGesture.setGestureName("Clockwise");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("LURD");
      newGesture.addPattern("DLUR");
      newGesture.addPattern("RDLU");
      newGesture.addPattern("URDL");
      gestures.add(newGesture);

      // Anti-Clockwise circle
      newGesture = new Gesture();
      newGesture.setGestureName("Anti-Clockwise");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("LDRU");
      newGesture.addPattern("ULDR");
      newGesture.addPattern("RULD");
      newGesture.addPattern("DRUL");
      gestures.add(newGesture);

      // T shape
      newGesture = new Gesture();
      newGesture.setGestureName("Up and Shake");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("ULR");
      newGesture.addPattern("URL");
      gestures.add(newGesture);

      // Upside-down T shape
      newGesture = new Gesture();
      newGesture.setGestureName("Down and Shake");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("DLR");
      newGesture.addPattern("DRL");
      gestures.add(newGesture);

      // Disabled
      newGesture = new Gesture();
      newGesture.setGestureName("Disabled");
      newGesture.setGestureImage(null); // TODO: Images for settings page?
      newGesture.addPattern("!");
      gestures.add(newGesture);
   }

   /**
    * @return the gestures
    */
   public Vector<Gesture> getGestures()
   {
      return gestures;
   }

   /**
    * @param gestures the gestures to set
    */
   public void setGestures(Vector<Gesture> gestures)
   {
      this.gestures = gestures;
   }
}
