package TBA.Data;

import java.util.ArrayList;

/**
 * This class provides an interface of a user
 * @author Dan McGrath
 */
public class User
{
   private String displayName;
   private String sessionID;
   private int defaultDiaryID;
   private int numberOfDiaries;
   ArrayList<Diary> Diaries;

  /**
   * This method returns the diaries of a user
   * @return A Vector of Diaries.
   */
   public ArrayList<Diary> getDiaries()
   {
      return Diaries;
   }

  /**
   * This method sets the diaries of a user
   * @param Diaries A Vector of Diaries.
   */
   public void setDiaries(ArrayList<Diary> Diaries)
   {
     this.Diaries = Diaries;
   }

   /**
    * This method returns the unique sessionID assigned to the user
    * @return sessionID field
    */
   public String getSessionID()
   {
      return sessionID;
   }

   /**
    * Sets the sessionID field of a user
    * @param sessionID
    */
   public void setSessionID(String sessionID)
   {
      this.sessionID = sessionID;
   }

   /**
    * Sets the default diary id a user has access to
    * @param defaultDiaryID
    */
   public void setDefaultDiaryID(int defaultDiaryID)
   {
      this.defaultDiaryID = defaultDiaryID;
   }

   /**
    * Sets name of the user
    * @param displayName
    */
   public void setDisplayName(String displayName)
   {
      this.displayName = displayName;
   }

   /**
    * Sets the number of diaries a user has
    * @param numberOfDiaries
    */
   public void setNumberOfDiaries(int numberOfDiaries)
   {
      this.numberOfDiaries = numberOfDiaries;
   }

   /**
    * This method returns the default diary id a user has access to
    * @return defaultDiaryID field
    */
   public int getDefaultDiaryID()
   {
      return defaultDiaryID;
   }

   /**
    * This method returns the name of the user
    * @return displayName field
    */
   public String getDisplayName()
   {
      return displayName;
   }

   /**
    * This method returns the number of Diaries a user has
    * @return numberOfDiaries field
    */
   public int getNumberOfDiaries()
   {
      return numberOfDiaries;
   }

   /**
    * Find the Diary with the same ID as the one passed in and replace all of
    * it's details with the new ones. This is used when a new version of the
    * diary has been recieved across the network.
    *<p>
    * @param newDiary The new Diary.
    */
   public void replaceDiary(Diary newDiary)
   {
      for(Diary nextDiary: Diaries)
      {
         if(nextDiary.getID() == newDiary.getID())
         {
            nextDiary.replaceDiary(newDiary);
            break;
         }
      }
   }
}
