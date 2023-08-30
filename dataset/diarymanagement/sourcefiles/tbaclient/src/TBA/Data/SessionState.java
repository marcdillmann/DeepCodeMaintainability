package TBA.Data;

/**
 * This holds everything about the current user's session state, include whether
 * or not the server and display need to update anything.
 *<p>
 * @author Dan McGrath
 */
public class SessionState
{
   private User activeUser;
   private Diary activeDiary;
   private volatile boolean serverNeedsUpdating;
   private volatile boolean displayNeedsUpdating;

   /**
    * This method returns the currently logged in user
    * @return the activeUser
    */
   public User getActiveUser()
   {
      return activeUser;
   }

   /**
    * This method sets the currently logged in user
    * @param activeUser the activeUser to set
    */
   public void setActiveUser(User activeUser)
   {
      this.activeUser = activeUser;
   }

   /**
    * This method returns the currently selected diary
    * @return the activeDiary
    */
   public Diary getActiveDiary()
   {
      return activeDiary;
   }

   /**
    * This method sets the currently selected diary
    * @param activeDiary the activeDiary to set
    */
   public void setActiveDiary(Diary activeDiary)
   {
      this.activeDiary = activeDiary;
   }

   /**
    * Return whether or not the client potentially needs to send updates to
    * the server. If the server is updated, you must call {@link #serverUpdated()}
    * to flag that it has been done.
    *<p>
    * @return If updates may need to be sent to the server.
    *<p>
    * @see #serverUpdated()
    * @see #setUpdate()
    */
   public boolean shouldServerUpdate()
   {
      return serverNeedsUpdating;
   }

   /**
    * Set {@link #shouldServerUpdate()} to false.
    *<p>
    * @see #shouldServerUpdate()
    * @see #setUpdate()
    */
   public void serverUpdated()
   {
      this.serverNeedsUpdating = false;
   }

   /**
    * Return whether or not the display potentially needs to be updated. If the
    * display is updated, you must call {@link #displayUpdated()} to flag
    * that it has been done.
    *<p>
    * @return If the display may need to be updated.
    *<p>
    * @see #displayUpdated()
    * @see #setUpdate()
    */
   public boolean shouldDisplayUpdate()
   {
      return displayNeedsUpdating;
   }

   /**
    * Set {@link #shouldDisplayUpdate()} to false.
    *<p>
    * @see #shouldDisplayUpdate()
    * @see #setUpdate()
    */
   public void displayUpdated()
   {
      this.displayNeedsUpdating = false;
   }

   /**
    * Sets the flags to notify that both the server and display may need to update.
    *<p>
    * @see #shouldDisplayUpdate()
    * @see #shouldServerUpdate()
    * @see #setUpdate()
    */
   public void setUpdate()
   {
      this.displayNeedsUpdating = true;
      this.serverNeedsUpdating = true;
   }
}
