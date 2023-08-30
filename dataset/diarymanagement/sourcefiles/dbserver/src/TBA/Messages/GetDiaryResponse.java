/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;
import TBA.Exceptions.MessagesException;
import java.util.ArrayList;

/**
 * This class is for the GetDiaryResponse message, it is a response to a GetDiaryQuery
 *
 * @author cs321tx2
 */
public class GetDiaryResponse extends ResponseMessage
{
   private String DiaryName = null;
   private char ownerFlag = '\0';
   private int permissions = -1;
   private int revisionNo = -1;
   private int NumberOfEntries = -1;

   ArrayList<DiaryEntry> entryRecords = new ArrayList<DiaryEntry>();

   private final int DiaryNameLen = 32;
   private final int ownerFlagLen = 1;
   private final int permissionsLen = 3;
   private final int revisionNoLen = 8;
   private final int NumberOfEntriesLen = 8;
   //TODO: use function of size of entries maybe?

   //Dont think we can have a final GetUpdResponseLen becuase of differing body sizes? will use function
   //private final int GetUpdResponseLen = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();

    /**
    * This method returns the length of the total diary response string including
    * all the entries
    * @return length - total length of string
    */

   public int GetDiaryResponseLen()
   {
       int length;

       length = DiaryNameLen + ownerFlagLen + permissions + revisionNo + totalEntrySize();

       return length;
   }

   /**
    * constructor does nothing
    */
   public GetDiaryResponse(){}

   /**
    * This method returns the total size of all entries in vector
    * @return total size of all entries in vector
    */
   private int totalEntrySize()
   {
       //TODO: get entry sizes

       int size = 0;
       for(int i=0; i < NumberOfEntries; i++)
       {
             size += entryRecords.get(i).GetEntrySize();
       }
      return size;
   }

   /**
    * This method creates a string made up of the message and response header to send,
    * GetDiaryResponse fields cant be null
    * @param dummy - message to send, not including 'Message Header'
    * @return string to send
    * @throws MessagesException
    */
    @Override
    public String Get(String dummy) throws MessagesException
    {
        String message;

        if (DiaryName == null)
        {
            throw new MessagesException("Diary Name is null");
        }
        if (ownerFlag == '\0')
        {
            throw new MessagesException("owner flag is not set");
        }
        if (permissions == -1)
        {
            throw new MessagesException("permisions are not set");
        }
        if (revisionNo == -1)
        {
            throw new MessagesException("revision number is not set");
        }
        if (NumberOfEntries == -1)
        {
            throw new MessagesException("invalid number of entries");
        }

        message = String.format("%1$-" + Integer.toString(DiaryNameLen) + "s", DiaryName);
        message += ownerFlag;
        message += String.format("%0" + Integer.toString(permissionsLen) + "o", permissions);
        message += String.format("%0" + Integer.toString(revisionNoLen) + "x", revisionNo);
        message += String.format("%0" + Integer.toString(NumberOfEntriesLen) + "x", NumberOfEntries);

        for(int i=0; i < NumberOfEntries; i++)
        {
             message += entryRecords.get(i).Get();
        }

        super.SetErrorCode(0);
        return super.Get(message);

    }

    /**
     * Sets the fields of a GetDiaryResponse class from string made from Get()
     * @param message actual message-string to set fields from
     * @return remainder of message
     * @throws MessagesException
     */
    @Override
   public String Set(String message) throws MessagesException
   {
       message = super.Set(message);

       DiaryEntry tempRecord;

       if(GetErrorCode() > 0)
       {
          return "";
       }

       DiaryName = message.substring(0,DiaryNameLen).trim();
       message = message.substring(DiaryNameLen, message.length());

       ownerFlag = message.charAt(0);
       message = message.substring(ownerFlagLen, message.length());

       if(ownerFlag != 'N' && ownerFlag != 'Y')
       {
           throw new MessagesException("Invalid ownership flag");
       }

       try
       {
           permissions = Integer.parseInt(message.substring(0,permissionsLen),8);
           message = message.substring(permissionsLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("The permissions are invalid");
       }
       try
       {
          revisionNo = Integer.parseInt(message.substring(0,revisionNoLen),16); //16 coz hexidecimal
          message = message.substring(revisionNoLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("revision number is invalid");
       }

       try
       {
           NumberOfEntries = Integer.parseInt(message.substring(0,NumberOfEntriesLen),16); //16 coz hexidecimal
           message = message.substring(NumberOfEntriesLen, message.length());
       }
       catch (NumberFormatException ex)
       {
          throw new MessagesException("Number of entries are invalid");
       }

       for(int i=0; i < NumberOfEntries; i++)
       {
         // The following line can throw a MessagesException if the entry record is not valid.
         tempRecord = new DiaryEntry();
         message = tempRecord.Set(message);
         entryRecords.add(tempRecord);
       }

       //message = super.Set(message);

       return message;
   }

    /**
     * This method returns the DiaryName field
     * @return DiaryName field
     */
    public String getDiaryName() {
        return DiaryName;
    }

    /**
     * Sets the Diary Name field
     * @param newDiaryName new name to set to
     * @throws MessagesException
     */
    public void setDiaryName(String newDiaryName) throws MessagesException {
        if(newDiaryName == null)
        {
            throw new MessagesException("Diary Name is invalid");
        } else {
            DiaryName = newDiaryName;
        }
    }

    /**
     * This method returns the number of entries
     * @return number of entries
     */
    public int getNumberOfEntries() {
        return NumberOfEntries;
    }

    /**
     * Sets Number of Entries, cannot be < 0
     * @param newNumberOfEntries new number of entries to set to
     * @throws MessagesException
     */
    public void setNumberOfEntries(int newNumberOfEntries) throws MessagesException {
        if(newNumberOfEntries < 0)
        {
            throw new MessagesException("Number of entries is invalid");
        } else {
            NumberOfEntries = newNumberOfEntries;
        }
    }

    /**
     * This method returns the ownerFlag field, will only be 'Y' or 'N'
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
     * This method returns the permissions field using unix format
     * @return permissions
     * @throws MessagesException
     */
    public int getPermissions() throws MessagesException {
        return permissions;
    }

    /**
     * Sets permissions field using unix format, cannot be <0 or >511
     * @param newPermissions
     * @throws MessagesException
     */
    public void setPermissions(int newPermissions) throws MessagesException {
        if(newPermissions < 0 || newPermissions > 511 )
        {
            throw new MessagesException("Permissions is invalid");
        } else {
            permissions = newPermissions;
        }
    }

    /**
     * This method returns the revision number
     * @return revisionNo
     */
    public int getRevisionNo() {
        return revisionNo;
    }

    /**
     * Sets revisionNo field, cannot be < 0
     * @param newRevNo
     * @throws MessagesException
     */
    public void setRevisionNo(int newRevNo)throws MessagesException {
        if(newRevNo < 0)
        {
            throw new MessagesException("Revision No is invalid");
        } else {
            revisionNo = newRevNo;
        }
    }

    /**
     * Sets entryRecord field which is a vector of {@link DiaryEntry}
     * @param temps new DiaryEntries to send
     * @throws MessagesException
     */
    public void setEntries(ArrayList<DiaryEntry> temps) throws MessagesException
    {
      if(temps == null)
      {
         entryRecords = new ArrayList<DiaryEntry>();
         throw new MessagesException("The Diary Entry records are null");
      }
      else
      {
         entryRecords = (ArrayList<DiaryEntry>)temps.clone();
         NumberOfEntries = entryRecords.size();
      }
    }

    /**
     * This method returns the entryRecord field which is a vector of {@link DiaryEntry}
     * @return vector of {@link DiaryEntry}
     */
   public ArrayList<DiaryEntry> getEntries()
   {
      return (ArrayList<DiaryEntry>)entryRecords.clone();
   }

}
