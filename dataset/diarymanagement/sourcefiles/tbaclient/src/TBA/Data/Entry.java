package TBA.Data;

import java.util.Calendar;

/**
 * This class serves as the interface to the data of a single Entry.
 *<p>
 * @author Dan Mcgrath
 * @author Joe Neasy
 *
 * @version $Rev:: 197           $ $Date:: 2009-10-14 #$
 */
public class Entry implements Comparable<Entry>
{
   private int id;
   private boolean locked;
   private boolean deleted;
   private boolean notified;
   private boolean modified;
   private String owner;
   private String subject;
   private String body;
   private Calendar start = Calendar.getInstance();
   private Calendar end = Calendar.getInstance();

   /**
    * The constructor for Entry.
    */
   public Entry()
   {
      notified = false;
      modified = false;
   }

   /**
    * This method returns the id field of an Entry
    * @return id field
    */
   public int getID()
   {
      return id;
   }

   /**
    * Sets the id field of an Entry
    * @param id
    */
   public void setID(int id)
   {
      this.id = id;
   }

   /**
    * This method returns the un/locked state of an Entry
    * @return locked field - true if locked false if unlocked
    */
   public boolean isLocked()
   {
      return locked;
   }

   /**
    * Sets the un/locked state of an Entry
    * @param locked
    */
   public void setLocked(boolean locked)
   {
      this.locked = locked;
   }

   /**
    * This method returns the owner of an Entry
    * @return owner field
    */
   public String getOwner()
   {
      return owner;
   }

   /**
    * Sets the owner name of an Entry
    * @param owner
    */
   public void setOwner(String owner)
   {
      this.owner = owner;
   }

   /**
    * This method returns the end field of and Entry
    * @return end field
    */
   public Calendar getEnd()
   {
      return end;
   }

   /**
    * Sets the end field of an Entry
    * @param end
    */
   public void setEnd(Calendar end)
   {
      this.end = end;
   }

   /**
    * This method returns the start field of an Entry
    * @return start field
    */
   public Calendar getStart()
   {
      return start;
   }

   /**
    * Sets start field of an Entry
    * @param start
    */
   public void setStart(Calendar start)
   {
      this.start = start;
   }

   /**
    * This method returns the body of an Entry
    * @return body field
    */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of an Entry
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * This method returns the subject or title of an Entry
     * @return subject field
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of an Entry
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    //TODO: confirm during?
    /**
     * This method returns whether or not a date happens during a particular tim
     * @param panelDate
     * @return true if happens during a date
     */
    public boolean during(Calendar panelDate)
    {
       Calendar duringDate = (Calendar)panelDate.clone();
       boolean check1 = start.before(duringDate) && end.after(duringDate);
       boolean check2 = start.after(duringDate) || start.equals(duringDate);
       duringDate.add(Calendar.DAY_OF_YEAR, 1);
       boolean check3 = start.before(duringDate);

       return check1 || (check2 && check3);
    }

   /**
    * Used when sorting to sort entries by start date/time
    * @param other The Entry to compare to.
    * @return The result from {@link java.util.Calendar}'s compareTo
    */
   public int compareTo(Entry other)
   {
      return start.compareTo(other.getStart());
   }

   /**
    * Used to determine if an Entry hasn't already notified when it becomes current.
    * @return If the Entry should notify the client when it becomes current.
    */
   public boolean shouldNotify()
   {
      return !notified;
   }

   /**
    * Used to set if an Entry has notified that it become current.
    * @param notified 'true' if shouldNotify should return false and vice versa.
    */
   public void hasNotified(boolean notified)
   {
      this.notified = notified;
   }

   /**
    * This method flags if the entry is deleted.
    * @return If the entry is deleted.
    */
   public boolean isDeleted()
   {
      return deleted;
   }

   /**
    * Set if this entry has been deleted
    * @param deleted true if the entry is deleted, false if it is not.
    */
   public void setDeleted(boolean deleted)
   {
      this.deleted = deleted;
   }

   /**
    * Check if the entry has been modified
    * @return true if it has been modified
    */
   public boolean isModified()
   {
      return modified;
   }

   /**
    * This method flags if the entry has been modified
    * @param modified Whether or not the entry has been modified.
    */
   public void setModified(boolean modified)
   {
      this.modified = modified;
   }
}