/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Data;

import java.util.Vector;

/**
 *
 * @author User1
 */
public class Diary
{
   private int ID;
   private String Name;
   private String ownerName; // TODO: Maybe int userID?
   private int permissions; // TODO: Right data type?
   private int revision;
   private Vector<Entry> entries;

   public int getID()
   {
      return ID;
   }

   public String getName()
   {
      return Name;
   }

   public Vector<Entry> getEntries()
   {
      return entries;
   }

   public String getOwnerName()
   {
      return ownerName;
   }

   public int getPermissions()
   {
      return permissions;
   }

   public int getRevision()
   {
      return revision;
   }

   public void setID(int ID)
   {
      this.ID = ID;
   }

   public void setName(String Name)
   {
      this.Name = Name;
   }

   public void setEntries(Vector<Entry> entries)
   {
      this.entries = entries;
   }

   public void setOwnerName(String ownerName)
   {
      this.ownerName = ownerName;
   }

   public void setPermissions(int permissions)
   {
      this.permissions = permissions;
   }

   public void setRevision(int revision)
   {
      this.revision = revision;
   }

   public void addEntry(Entry newEntry)
   {
      entries.add(newEntry);
   }
}
