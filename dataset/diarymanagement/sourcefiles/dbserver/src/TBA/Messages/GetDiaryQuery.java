package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the GetDiaryQuery message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 252           $ $Date:: 2009-10-23 #$
 */
public class GetDiaryQuery extends QueryMessage
{
   private final String QueryType = "GETDIARY";
   private int DiaryID = -1;
   
   private final int diaryIDLen = 4;

    /**
    * Constructor does nothing
    */
   public GetDiaryQuery() { }

   /**
    * The Get() Method gets the GetDiaryQuery message with the Message and Query header.
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

      if (DiaryID == -1)
      {
          throw new MessagesException("Diary ID not set");
      }

      message = String.format("%0" + Integer.toString(diaryIDLen) + "x", DiaryID);

      super.SetQueryType(QueryType);
      
      return super.Get(message);
   }

   /**
    * The Set() Method strips out all the fields of a GetDiaryQuery message. They
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
      message = super.Set(message);

      if(message.length() != diaryIDLen)
      {
         throw new MessagesException("Get Diary Query is invalid");
      }

      try
      {
         DiaryID = Integer.parseInt(message.substring(0,diaryIDLen),16); //16 coz hexidecimal
         message = message.substring(diaryIDLen, message.length());
         //duno if i might have to kill this top line
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("entry ID is invalid");
      }

      return null;
   }

   /**
    * This method returns the diaryID to be retrieved
    * @return diaryID field
    */
   public int GetDiaryID()
   {
       return DiaryID;
   }

   /**
    * Sets the diaryID to retrieve, cannot be <0
    * @param temp new diaryID to set to
    * @throws MessagesException
    */
   public void SetDiaryID(int temp) throws MessagesException
   {
      if(temp < 0)
      {
         DiaryID = -1;
         throw new MessagesException("The Diary ID is invalid");
      }
      else
      {
         DiaryID = temp;
      }
   }
}