package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is used to handle Diary header records.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 204           $ $Date:: 2009-10-15 #$
 */
public class DiaryHeader
{
   private int diaryID = -1;
   private String diaryName = null;
   private char ownerFlag = '\0';
   private int permissions = -1;
   private int revNum = -1;
   
   private final int diaryIDLen = 4;
   private final int diaryNameLen = 32;
   private final int ownerFlagLen = 1;
   private final int permissionsLen = 3;
   private final int revNumLen = 8;
   private final int recordLen = diaryIDLen + diaryNameLen + ownerFlagLen + permissionsLen + revNumLen;

   /**
    * Constructor does nothing
    */
   public DiaryHeader() { }
   
   /**
    * The Get() Method returns the Diary Header record
    *<p>
    *<p>
    * @return The actual Diary Header record 
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    */
   
   public String Get() throws MessagesException
   {
      String record = null;

      if(diaryID == -1)
      {
         throw new MessagesException("Diary ID not set");
      }
      if(diaryName == null)
      {
         throw new MessagesException("Diary Name not set");
      }
      if(ownerFlag == '\0')
      {
         throw new MessagesException("Owner Flag not set");
      }
      if(permissions == -1)
      {
         throw new MessagesException("Permissions not set");
      }
      if(revNum == -1)
      {
         throw new MessagesException("Rev number not set");
      }

      record = String.format("%0" + Integer.toString(diaryIDLen) + "x", diaryID);
      record += String.format("%1$-" + Integer.toString(diaryNameLen) + "s", diaryName);
      record += ownerFlag;
      record += String.format("%0" + Integer.toString(permissionsLen) + "o", permissions);
      record += String.format("%0" + Integer.toString(revNumLen) + "x", revNum);

      return record;
   }

   /**
    * The Set() Method takes a Diary Header record and strips out all the fields
    *<p>
    * @param record The record as recieved.
    *<p>
    * @return Any data left after the record
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get()
    */
   public String Set(String record) throws MessagesException
   {
      if(record.length() < recordLen)
      {
         throw new MessagesException("The Response Header is incomplete");
      }

      try
      {
         diaryID = Integer.parseInt(record.substring(0,diaryIDLen), 16);
         record = record.substring(diaryIDLen, record.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The Diary ID is invalid");
      }
      
      diaryName = record.substring(0, diaryNameLen).trim();
      record = record.substring(diaryNameLen, record.length());
      
      ownerFlag = record.charAt(0);
      if(ownerFlag != 'N' && ownerFlag != 'Y')
      {
         throw new MessagesException("Invalid ownership flag");
      }
      
      try
      {
         permissions = Integer.parseInt(record.substring(1, permissionsLen+1), 8);
         record = record.substring(permissionsLen + 1, record.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The permissions are invalid");
      }
      
      try
      {
         revNum = Integer.parseInt(record.substring(0, revNumLen), 16);
         record = record.substring(revNumLen, record.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The revision number is invalid");
      }
      
      return record;
   }

   /**
    * This method returns the 'Diary ID' field. You must have called either
    * Set() or SetDiaryID() first.
    *<p>
    * @return The Diary ID
    *<p>
    * @see #SetDiaryID(int)
    */
   public int GetDiaryID()
   {
      return diaryID;
   }

   /**
    * This method allows you to set the 'Diary ID' field.
    *<p>
    * @param diary - Code The 'Diary ID' to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetDiaryID()
    */
   public void SetDiaryID(int diary) throws MessagesException
   {
      if(diary < 0)
      {
         diaryID = -1;
         throw new MessagesException("The Diary ID is invalid");
      }
      else
      {
         diaryID = diary;
      }
   }

   /**
    * This method returns the 'Diary name' field. You must have called either
    * Set() or SetDiaryName() first.
    *<p>
    * @return The Diary name
    *<p>
    * @see #SetDiaryName(java.lang.String)
    */
   public String GetDiaryName()
   {
      return diaryName;
   }

   /**
    * This method allows you to set the 'Diary name' field. 
    *<p>
    * @param name - Message The Diary name to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetDiaryName()
    */
   public void SetDiaryName(String name) throws MessagesException
   {
      if(name == null)
      {
         diaryName = null;
         throw new MessagesException("The Diary name is null");
      }
      else
      {
         diaryName = name;
      }
   }
   
   /**
    * This method returns the 'Owner Flag' field. You must have called either
    * Set() or SetOwnerFlag() first.
    *<p>
    * @return The Owner Flag
    *<p>
    * @see #SetDiaryName(java.lang.String)
    */
   public char GetOwnerFlag()
   {
      return ownerFlag;
   }

   /**
    * This method allows you to set the 'Owner Flag' field. 
    *<p>
    * @param flag - Message The Owner flag to set. Either 'Y' or 'N'
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetOwnerFlag() 
    */
   public void SetOwnerFlag(char flag) throws MessagesException
   {
      if(flag != 'Y' && flag != 'N')
      {
         ownerFlag = '\0';
         throw new MessagesException("The Owner Flag is null");
      }
      else
      {
         ownerFlag = flag;
      }
   }
   
   /**
    * This method returns the 'Permissions' field. You must have called either
    * Set() or SetPermissions() first.
    *<p>
    * @return The Permissions
    *<p>
    * @see #SetPermissions(int)
    */
   public int GetPermissions()
   {
      return permissions;
   }

   /**
    * This method allows you to set the 'Permissions' field.
    *<p>
    * @param per The 'Permissions' to set. Covert to Octal to get them Unix Style
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetPermissions()
    */
   public void SetPermissions(int per) throws MessagesException
   {
      if(per < 0 || per > 511)
      {
         permissions = -1;
         throw new MessagesException("The Permissions are invalid");
      }
      else
      {
         permissions = per;
      }
   }
   
   /**
    * This method returns the 'Revision Number' field. You must have called either
    * Set() or SetRevNum() first.
    *<p>
    * @return The Revision Number
    *<p>
    * @see #SetRevNum(int)
    */
   public int GetRevNum()
   {
      return revNum;
   }

   /**
    * This method allows you to set the 'Revision number' field.
    *<p>
    * @param num The 'Revision number' to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetRevNum()
    */
   public void SetRevNum(int num) throws MessagesException
   {
      if(num < 0)
      {
         num = -1;
         throw new MessagesException("The Revision number is invalid");
      }
      else
      {
         revNum = num;
      }
   }
} 