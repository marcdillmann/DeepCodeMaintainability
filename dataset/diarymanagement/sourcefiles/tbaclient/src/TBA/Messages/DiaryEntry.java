package TBA.Messages;
import TBA.Exceptions.MessagesException;
/**
 *  This class is for containing diary entries to be sent
 * @author Charles Jap
 * @author Dan McGrath
 */
public class DiaryEntry
{
   private int entryID = -1;

   private long start = -1;
   private long end = -1;

   private char ownerFlag = '\0';  //Y if owner N if not
   private char lockedFlag = '\0'; //Y if locked N if not
   private char deletedFlag = 'N'; //Y if deleted N if not
   private String creatingUser = null; //Username who created it
   private String title = null;
   private String body = null;
   
   private final int entryIDLen = 8;
   private final int startLen = 16;
   private final int endLen = 16;
   private final int ownerFlagLen = 1;
   private final int lockedFlagLen = 1;
   private final int deletedFlagLen = 1;
   private final int creatingUserLen = 32;
   private final int titleLen = 32;
   
   //TODO: size of body, prolly wont final EntryLen because of changing body size maybe?

   //cant have this final int, coz body is changing... and you can only get after body's been set
   //private final int EntryLen = entryIDLen + startDateLen + endDateLen + startTimeLen + endTimeLen + ownerFlagLen + lockedFlagLen + creatingUserLen + titleLen + getBodyLen();//bodyLength;
   
   //delimter is only placed after message is made, it isnt stored as part of body
   /**
    * This method returns the length of the entry's body excluding the delimiter
    * @return length of the entry's body excluding the delimiter
    */
   private int getBodyLen()
   {
       int length = body.length();
       return length;
   } 

   //returns -1 if body is null?
   /**
    * This method returns the size of the whole entry exluding the delimter
    * @return -1 if nobody, otherwise size of entry
    */
   public int GetEntrySize()
   {
       if (body == null)
       {
           return -1;
       }else {
           return entryIDLen + startLen + endLen + ownerFlagLen + lockedFlagLen + deletedFlagLen + creatingUserLen + titleLen + body.length();
       }
   }
   /**
    * constructor does nothing
    */
   public DiaryEntry(){}

   //Get function makes the entry record string to send
   /**
    * This method compiles a string to send from the entry's fields. These fields must first be set.
    * @return compiled string representing a diary entry
    * @throws MessagesException
    */
   public String Get() throws MessagesException
   {
       String EntryRecord = null;
       
       if (entryID == -1)
       {
           throw new MessagesException("entry ID not set");
       }
       if (start == -1)
       {
           throw new MessagesException("Start Time/Date not set");
       }
       if (end == -1)
       {
           throw new MessagesException("End Time/Date not set");
       }
       if (ownerFlag == '\0')
       {
           throw new MessagesException("Owner Flag is not set");
       }
       if(lockedFlag == '\0')
       {
           throw new MessagesException("Locked Flag is not set");
       }
       if(creatingUser == null)
       {
           throw new MessagesException("Creating User is not set");
       }
       if(title == null)
       {
           throw new MessagesException("Title is not set");
       }
       if(body == null)
       {
           throw new MessagesException("Body is not set");
       }
      
       //x is hex, s is string, o is octal - padding

       EntryRecord = String.format("%0" + Integer.toString(entryIDLen) + "x", entryID);
       EntryRecord += String.format("%0" + Integer.toString(startLen) + "x", start);
       EntryRecord += String.format("%0" + Integer.toString(endLen) + "x", end);
       EntryRecord += ownerFlag;
       EntryRecord += lockedFlag;
       EntryRecord += getDeletedFlag();
       //not sure if im padding teh user and title, but i'l leae for now
       EntryRecord += String.format("%1$-" + Integer.toString(creatingUserLen) + "s", creatingUser);
       EntryRecord += String.format("%1$-" + Integer.toString(titleLen) + "s", title);
       
       EntryRecord += body + '|';

       return EntryRecord;
       
   }

   /**
    * This method sets the fields of a DiaryEntry from a string created by the Get function.
    * @param entryRecord String created by a Get()
    * @return null if successful
    * @throws MessagesException
    */
   public String Set(String entryRecord) throws MessagesException
   {
       try
       {
           entryID = Integer.parseInt(entryRecord.substring(0,entryIDLen),16); //16 coz hexidecimal
           entryRecord = entryRecord.substring(entryIDLen, entryRecord.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("entry ID is invalid");
       }

       start = Long.parseLong(entryRecord.substring(0,startLen), 16);
       entryRecord = entryRecord.substring(startLen, entryRecord.length());

       end = Long.parseLong(entryRecord.substring(0,endLen), 16);
       entryRecord = entryRecord.substring(endLen, entryRecord.length());

       ownerFlag = entryRecord.charAt(0);
       entryRecord = entryRecord.substring(ownerFlagLen, entryRecord.length());
       
       if(ownerFlag != 'N' && ownerFlag != 'Y')
       {
           throw new MessagesException("Invalid ownership flag in diary entry as: " + ownerFlag);
       }

       lockedFlag = entryRecord.charAt(0);
       entryRecord = entryRecord.substring(lockedFlagLen, entryRecord.length());

       if(lockedFlag != 'N' && lockedFlag != 'Y')
       {
           throw new MessagesException("Invalid locking flag");
       }

       setDeletedFlag(entryRecord.charAt(0));
       entryRecord = entryRecord.substring(deletedFlagLen, entryRecord.length());

       if(getDeletedFlag() != 'N' && getDeletedFlag() != 'Y')
       {
           throw new MessagesException("Invalid delete flag");
       }

       creatingUser = entryRecord.substring(0,creatingUserLen).trim();
       entryRecord = entryRecord.substring(creatingUserLen, entryRecord.length());

       title = entryRecord.substring(0,titleLen).trim();
       entryRecord = entryRecord.substring(titleLen, entryRecord.length());

       int token = entryRecord.indexOf("|");
       //if the token is the last char of string - ie no other entries
       if (token == entryRecord.length() - 1)
       {
          body = entryRecord.substring(0,entryRecord.length()-1);
          entryRecord = "";
       } else {
          //-1 for the delimiter
          body = entryRecord.substring(0,token).trim();
          //+1 to skill the token
          entryRecord = entryRecord.substring(token+1,entryRecord.length());
          //body = entryRecord;
          //since everythign else is the body
       }

       return entryRecord;
   }
   
   //member variables get/sets
   
   /**
    * This method returns the body field of diary entry excluding delimiter
    * @return body field of diary entry excluding delimiter
    * @throws MessagesException
    */
    public String getBody() throws MessagesException {
        if (body == null)
        {
            throw new MessagesException("body is null");
        } else {
            return body;
        }
    }

    /**
     * Sets the body field of DiaryEntry, cannot be null
     * @param newBody
     * @throws MessagesException
     */
    public void setBody(String newBody) throws MessagesException {
        if(newBody == null)
        {
           throw new MessagesException("body is null");
        } else {
           body = newBody;
        }
    }

    /**
     * This method returns a String containing the username of the creator
     * @return String containing the username of the creator
     */
    public String getCreatingUser() {
        return creatingUser;
    }

    /**
     * Sets the creatingUser field of DiaryEntry, cannot be null
     * @param newCreatingUser username of creator of diary entry
     * @throws MessagesException
     */
    public void setCreatingUser(String newCreatingUser) throws MessagesException {
        if (newCreatingUser == null)
        {
            throw new MessagesException("creating user must not be null");
        } else {
            creatingUser = newCreatingUser;
        }
    }

 /**
  * This method returns the end time/date for use in Calendar
  * @return The end time/date as used by Calendar
  */
   public long getEnd()
   {
      return end;
   }

/**
  * This method sets the end time/date for use in Calendar
  * @param end The end time/date as used by Calendar
  */
   public void setEnd(long end)
   {
      this.end = end;
   }

 /**
  * This method returns the start time/date for use in Calendar
  * @return The start time/date as used by Calendar
  */
   public long getStart()
   {
      return start;
   }

/**
  * This method sets the start time/date for use in Calendar
  * @param start The start time/date as used by Calendar
  */
   public void setStart(long start)
   {
      this.start = start;
   }

    /**
     * This method returns the unique entryID of DiaryEntry
     * @return unique entryID of DiaryEntry
     */
    public int getEntryID() {
        return entryID;
    }

    /**
     * Sets entryID field of DiaryEntry, cannot be <0
     * @param newID
     * @throws MessagesException
     */
    public void setEntryID(int newID) throws MessagesException {
        if (newID < 0)
        {
            throw new MessagesException("new entry ID is invalid");
        } else {
            entryID = newID;
        }
    }

    /**
     * This method returns the lockedFlag field, will only be'Y' or 'N'
     * @return lockedFlag field, will only be'Y' or 'N'
     */
    public char getLockedFlag() {
        return lockedFlag;
    }

    /**
     * Sets lockedFlag field, can only be 'Y' or 'N'
     * @param newLockedFlag
     * @throws MessagesException
     */
    public void setLockedFlag(char newLockedFlag) throws MessagesException {
        if((newLockedFlag == 'Y') || (newLockedFlag == 'N'))
        {
            lockedFlag = newLockedFlag;
        } else {
            throw new MessagesException("Invalid flag");
        }
    }

    /**
     * This method returns ownerFlag field, will only be 'Y' or 'N'
     * @return ownerFlag field, will only be 'Y' or 'N'
     */
    public char getOwnerFlag() {
        return ownerFlag;
    }

    /**
     * Sets ownerFlag field, can only be 'Y' or 'N'
     * @param newOwnerFlag
     * @throws MessagesException
     */
    public void setOwnerFlag(char newOwnerFlag) throws MessagesException {
        if((newOwnerFlag == 'Y') || (newOwnerFlag == 'N'))
        {
            ownerFlag = newOwnerFlag;
        } else {
            throw new MessagesException("Invalid flag");
        }
    }

    /**
     * This method returns the title field of DiaryEntry
     * @return title field of DiaryEntry
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title field, cannot be null
     * @param newTitle new title cannot be null
     * @throws MessagesException
     */
    public void setTitle(String newTitle) throws MessagesException {
        if(newTitle == null)
        {
           throw new MessagesException("Title must not be null");
        } else {
           title = newTitle;
        }
    }

   /**
    * This method is used to determine if the Entry is deleted or not.
    * @return 'Y' if the entry is deleted, 'N' if it is not
    */
   public char getDeletedFlag()
   {
      return deletedFlag;
   }

   /**
    * This method is used to set if an entry is deleted or not.
    * @param deletedFlag 'Y' if the entry is deleted, 'N' if it is not
    */
   public void setDeletedFlag(char deletedFlag)
   {
      this.deletedFlag = deletedFlag;
   }
 }