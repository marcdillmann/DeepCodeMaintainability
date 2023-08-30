package TBA.Data;

import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

/**
 * This class serves as the interface to the data of a single Diary.
 *<p>
 * @author Dan Mcgrath
 *
 * @version $Rev:: 197           $ $Date:: 2009-10-14 #$
 */
public class Diary
{
   private int ID;
   private String Name;
   private String ownerName; // TODO: Maybe int userID?
   private char ownerFlag; // Y or N
   private int permissions; // TODO: Right data type?
   private int revision;
   private Vector<Entry> entries = null;

   /**
    * This method returns the ID field of a Diary
    * @return ID field
    */
   public int getID()
   {
      return ID;
   }

   /**
    * This method returns the Name field of a Diary
    * @return Name field
    */
   public String getName()
   {
      return Name;
   }

   @Override
   public String toString()
   {
      return Name;
   }

   /**
    * This method returns a Vector of entries.
    * @return entries for this diary
    */
   public Vector<Entry> getEntries()
   {
      return entries;
   }

   /**
    * This method returns a Vector of entries. It does not include those that
    * have been deleted.
    * @return Active entries for this diary
    */
   public Vector<Entry> getActiveEntries()
   {
      Vector<Entry> trimmedEntries = new Vector<Entry>();

      for(Entry aEntry : entries)
      {
         if(!aEntry.isDeleted())
         {
            trimmedEntries.add(aEntry);
         }
      }

      return trimmedEntries;
   }

   /**
    * This method returns a vector of entries during a given time
    * @param now
    * @return entries by date
    */
   public Vector<Entry> getEntriesByDate(Calendar now)
   {
      Vector<Entry> trimmedEntries = new Vector<Entry>();

      for(Entry aEntry : entries)
      {
         if(aEntry.during(now) && !aEntry.isDeleted())
         {
            trimmedEntries.add(aEntry);
         }
      }

      Collections.sort(trimmedEntries);
      return trimmedEntries;
   }

   /**
    * This method returns a vector of entries that have been modified
    *<p>
    * @return entries that have been modified
    */
   public Vector<Entry> getModifiedEntries()
   {
      Vector<Entry> trimmedEntries = new Vector<Entry>();

      for(Entry aEntry : entries)
      {
         if(aEntry.isModified())
         {
            trimmedEntries.add(aEntry);
         }
      }

      return trimmedEntries;
   }

   /**
    * This method returns the owner of the Diary
    * @return ownerName field
    */
   public String getOwnerName()
   {
      return ownerName;
   }

   /**
    * This method returns the permission of the Diary
    * @return permissions field - as per unix permissions
    */
   public int getPermissions()
   {
      return permissions;
   }

   /**
    * This method returns the revision number of the Diary
    * @return revision field
    */
   public int getRevision()
   {
      return revision;
   }

   /**
    * This method returns the ownerFlag field
    * @return ownerFlag field - 'Y' if owner 'N' if not
    */
   public char getOwnerFlag()
   {
      return ownerFlag;
   }

   /**
    * Sets the ownerFlag field of a Diary
    * @param ownerFlag - can only be 'Y' or 'N'
    */
   public void setOwnerFlag(char ownerFlag)
   {
      this.ownerFlag = ownerFlag;
   }

   /**
    * Sets the ID of a Diary
    * @param ID
    */
   public void setID(int ID)
   {
      this.ID = ID;
   }

   /**
    * Sets the name of a Diary
    * @param Name
    */
   public void setName(String Name)
   {
      this.Name = Name;
   }

   /**
    * Sets the entries of a Diary
    * @param entries - as a vector containing the entries
    */
   public void setEntries(Vector<Entry> entries)
   {
      this.entries = entries;
   }

   /**
    * Sets the Name of the owner of a Diary
    * @param ownerName
    */
   public void setOwnerName(String ownerName)
   {
      this.ownerName = ownerName;
   }

   /**
    * Sets the permissions of the Diary
    * @param permissions - as per unix permissions
    */
   public void setPermissions(int permissions)
   {
      this.permissions = permissions;
   }

   /**
    * Sets the revision number of a Diary
    * @param revision
    */
   public void setRevision(int revision)
   {
      this.revision = revision;
   }

   /**
    * This method adds a single entry to a Diary
    * @param newEntry - a single entry to be added
    */
   public void addEntry(Entry newEntry)
   {
       if (entries == null )
       {
           entries=new Vector<Entry>();
       }
       entries.add(newEntry);
   }

   /**
    * This method resets all entries to not be flagged as modified
    */
   public void clearModified()
   {
      for(Entry aEntry : entries)
      {
         aEntry.setModified(false);
      }
   }

   /**
    * Replaces the details of the diary with the one passed in.
    * @param newDiary The Diary containing the details to replace this ones with.
    */
   public void replaceDiary(Diary newDiary)
   {
      Name = newDiary.getName();
      ownerName = newDiary.getOwnerName();
      ownerFlag = newDiary.getOwnerFlag();
      permissions = newDiary.getPermissions();
      revision = newDiary.getRevision();
      entries = newDiary.getEntries();
   }
}
