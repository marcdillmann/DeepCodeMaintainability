package TBA.Messages;

import TBA.Exceptions.MessagesException;
import java.util.ArrayList;

/**
 * This class is the response message to a Login Query
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 273           $ $Date:: 2011-10-25 #$
 */
public class LoginResponse extends ResponseMessage
{
   private String displayName = null;
   private int diaryCount = 0;
   private int defaultDiary = -1;
   ArrayList<DiaryHeader> diaryHeaderRecords = new ArrayList<DiaryHeader>();

   private final int displayNameLen = 32;
   private final int diaryCountLen = 2;
   private final int defaultDiaryLen = 4;
   private final int loginResponseLen = displayNameLen + diaryCountLen + defaultDiaryLen;

    /**
    * Constructor does nothing
    */
   public LoginResponse() { }

   /**
    * The Get() Method gets the LoginResponse message including the
    * reponse and message headers
    *<p>
    * @param dummy 
    * @return The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    */
   @Override
   public String Get(String dummy) throws MessagesException
   {
      String message = null;

      if(displayName == null)
      {
         throw new MessagesException("Display Name is not set");
      }
      if(defaultDiary == -1)
      {
         throw new MessagesException("Default Diary ID is not set");
      }

      message = String.format("%1$-" + Integer.toString(displayNameLen) + "s", displayName);
      message += String.format("%0" + Integer.toString(diaryCountLen) + "x", diaryCount);
      message += String.format("%0" + Integer.toString(defaultDiaryLen) + "x", defaultDiary);
   
      for(int i=0; i < diaryCount; i++)
      {
         // The following line can throw a MessagesException if the diary record is not valid.
         message += diaryHeaderRecords.get(i).Get();
      }
      super.SetErrorCode(0);
      return super.Get(message);
   }

   /**
    * The Set() Method strips out all the fields of a Login Query message. They
    * are then available by the Get methods of this class.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      String diaryCountStr;
      String defaultDiaryStr;
      DiaryHeader tempRecord;

      message = super.Set(message);

      if(GetErrorCode() > 0)
      {
         return "";
      }
      if(message.length() < loginResponseLen)
      {
         throw new MessagesException("The Login Response is incomplete");
      }
      displayName = message.substring(0, displayNameLen);
      message = message.substring(displayNameLen, message.length());

      try
      {
         diaryCountStr = message.substring(0,diaryCountLen);
         diaryCount = Integer.parseInt(diaryCountStr, 16);
         message = message.substring(diaryCountLen, message.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The Diary Count is invalid");
      }

      try
      {
         defaultDiaryStr = message.substring(0, defaultDiaryLen);
         defaultDiary = Integer.parseInt(defaultDiaryStr, 16);
         message = message.substring(defaultDiaryLen, message.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The Diary Count is invalid");
      }
      
      for(int i=0; i < diaryCount; i++)
      {
         // The following line can throw a MessagesException if the diary record is not valid.
         tempRecord = new DiaryHeader();
         message = tempRecord.Set(message);
         diaryHeaderRecords.add(tempRecord);
      }

      return null;
   }

   /**
    * This method returns the 'Display name' field. You must have called either
    * SetResponse() or SetDisplayName() first.
    *<p>
    * @return The Error Code:
    * 0 = Success
    * >0 = A Failure
    *<p>
    * @see #SetDisplayName(java.lang.String)
    */
   public String GetDisplayName()
   {
      return displayName;
   }

   /**
    * This method allows you to set the 'Display name' field.
    *<p>
    * @param name The display name of the user
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetDisplayName()
    */
   public void SetDisplayName(String name) throws MessagesException
   {
      if(name == null)
      {
         displayName = null;
         throw new MessagesException("The Display name is null");
      }
      else
      {
         displayName = name;
      }
   }

   /**
    * This method returns the 'Default Diary' field. You must have called either
    * SetResponse() or SetDefaultDiary() first.
    *<p>
    * @return The Default Diary's ID
    *<p>
    * @see #SetErrorMessage(java.lang.String)
    */
   public int GetDefaultDiary()
   {
      return defaultDiary;
   }

   /**
    * This method allows you to set the 'Default Diary' field.
    *<p>
    * @param diary The ID of the Diary that is set as the default
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetDefaultDiary()
    */
   public void SetDefaultDiary(int diary) throws MessagesException
   {
      if(diary < 0)
      {
         defaultDiary = -1;
         throw new MessagesException("The Default Diary is invalid");
      }
      else
      {
         defaultDiary = diary;
      }
   }

   /**
    * This method returns all the 'Diary Header' recprds. You must have called either
    * SetResponse() or SetDiaryHeaders() first.
    *<p>
    * @return A Vector containing all the Diary Header records.
    *<p>
    * @see #SetErrorMessage(java.lang.String)
    */
   @SuppressWarnings("unchecked")
   public ArrayList<DiaryHeader> GetDiaryHeaders()
   {
      return (ArrayList<DiaryHeader>)diaryHeaderRecords.clone();
   }

   /**
    * This method allows you to set all the 'Diary Header' records.
    *<p>
    * @param records A vector of all the Diary Header records to add to the response
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetDiaryHeaders() 
    */
   @SuppressWarnings("unchecked")
   public void SetDiaryHeaders(ArrayList<DiaryHeader> records) throws MessagesException
   {
      if(records == null)
      {
         diaryHeaderRecords = new ArrayList<DiaryHeader>();
         throw new MessagesException("The Diary header records are null");
      }
      else
      {
         diaryHeaderRecords = (ArrayList<DiaryHeader>)records.clone();
         diaryCount = diaryHeaderRecords.size();
      }
   }
}