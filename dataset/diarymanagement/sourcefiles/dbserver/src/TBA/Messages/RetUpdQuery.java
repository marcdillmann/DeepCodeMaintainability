package TBA.Messages;
import TBA.Exceptions.MessagesException;
import java.util.ArrayList;

/**
 * This class is for the RetUpdQuery message.
 * @author Charles Jap
 * @author Dan McGrath
 */
public class RetUpdQuery extends QueryMessage
{

   private final String QueryType = "RETUPD";

   private int DiaryID = -1;
   private String DiaryName = null;
   private int permissions = -1;
   private int RevNo = -1; // revision number of local
   private int NumberOfEntries = -1; //entries that have changed
   private final int diaryIDLen = 4;
   private final int diaryNameLen = 32;
   private final int permissionsLen = 3;
   private final int revNumLen = 8;
   private final int NumberOfEntriesLen = 8;

   ArrayList<DiaryEntry> entryRecords = new ArrayList<DiaryEntry>();

   //TODO: some sort of min len? cant have definate one because of entry records

    /**
     * constructor does nothing
     */
    public RetUpdQuery(){}

    /**
     * The Get() Method gets the RetUpdQuery message with the Message and Query header.
     * @param dummy 
     * @return The actual message string you can send
     * @throws MessagesException
     * @see #Set(java.lang.String)
     * @see TBA.Messages.QueryMessage#Get(java.lang.String)
     * @see TBA.Messages.Messages#Get(java.lang.String)
     */
    @Override
    public String Get(String dummy) throws MessagesException
    {

       String message = null;

       if (DiaryID == -1)
       {
           throw new MessagesException("Diary ID not set");
       }
       if (DiaryName == null)
       {
           throw new MessagesException("Diary Name is null");
       }

       if (permissions == -1)
       {
           throw new MessagesException("permisions are not set");
       }
       if (RevNo == -1)
       {
           throw new MessagesException("revision number is not set");
       }
       if (NumberOfEntries == -1)
       {
           throw new MessagesException("invalid number of entries");
       }


       message = String.format("%0" + Integer.toString(diaryIDLen) + "x", DiaryID);
       message += String.format("%1$-" + Integer.toString(diaryNameLen) + "s", DiaryName);
       message += String.format("%0" + Integer.toString(permissionsLen) + "o", permissions);
       message += String.format("%0" + Integer.toString(revNumLen) + "x", RevNo);
       message += String.format("%0" + Integer.toString(NumberOfEntriesLen) + "x", NumberOfEntries);

       for(int i=0; i < NumberOfEntries; i++)
       {
            message += entryRecords.get(i).Get();
       }

       super.SetQueryType(QueryType);
       return super.Get(message);
    }

    /**
     * The Set() Method strips out all the fields of a RetUpdQuery message. They
     * are then available by the Get methods of this class.
     * @param message that fields will be set from
     * @return remainder of message
     * @throws MessagesException
     * @see #Get(java.lang.String)
     * @see TBA.Messages.QueryMessage#Set(java.lang.String)
     * @see TBA.Messages.Messages#Set(java.lang.String)
     */
    @Override
    public String Set(String message) throws MessagesException
    {
        message = super.Set(message);

        DiaryEntry tempRecord;

        try
        {
            DiaryID = Integer.parseInt(message.substring(0,diaryIDLen),16); //16 coz hexidecimal
            message = message.substring(diaryIDLen, message.length());
        }
        catch (NumberFormatException ex)
        {
           throw new MessagesException("entry ID is invalid");
        }

        DiaryName = message.substring(0,diaryNameLen).trim();
        message = message.substring(diaryNameLen, message.length());
        try
        {
           permissions = Integer.parseInt(message.substring(0, permissionsLen), 8);
           message = message.substring(permissionsLen, message.length());
        }
        catch (NumberFormatException ex)
        {
           throw new MessagesException("The permissions are invalid");
        }

        try
        {
           RevNo = Integer.parseInt(message.substring(0, revNumLen), 16);
           message = message.substring(revNumLen, message.length());
        }
        catch (NumberFormatException ex)
        {
           throw new MessagesException("The revision number is invalid");
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

        return null;
    }

   /**
    * This method returns the DiaryID field
    * @return DiaryID
    */
   public int GetDiaryID()
   {
      return DiaryID;
   }

    /**
     * Sets DiaryID field to update, cannot be <0
     * @param diary - new DiaryID you want to set to
     * @throws MessagesException
     */
   public void SetDiaryID(int diary) throws MessagesException
   {
      if(diary < 0)
      {
         DiaryID = -1;
         throw new MessagesException("The Diary ID is invalid");
      }
      else
      {
         DiaryID = diary;
      }
   }

    /**
     * This method returns the DiaryName field
     * @return DiaryName field
     */
    public String getDiaryName() {
        return DiaryName;
    }

    /**
     * Sets DiaryName field to newDiaryName, cannot be null
     * @param newDiaryName as the new DiaryName field
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
        if(newPermissions < 0  || newPermissions > 511 )
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
        return RevNo;
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
            RevNo = newRevNo;
        }
    }

    /**
     * This method returns the NumberOfEntries field
     * @return NumberOfEntries field
     */
    public int getNumberOfEntries() {
        return NumberOfEntries;
    }

    /**
     * NumberOfEntries field is set when entries are added,
     * Use this to manually set/change this
     * @param newNumberOfEntries - new NumberOfEntries to set to
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
     * Sets entryRecord field which is a vector of {@link DiaryEntry}
     * @param temps new DiaryEntries to send
     * @throws MessagesException
     */
    @SuppressWarnings("unchecked")
    //I think the cloning/casting is beacuse of the pointer property
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
   @SuppressWarnings("unchecked")
   //I think the cloning/casting is beacuse of the pointer property
   public ArrayList<DiaryEntry> getEntries()
   {
      return (ArrayList<DiaryEntry>)entryRecords.clone();
   }

}
