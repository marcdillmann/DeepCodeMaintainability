package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the GetUpdQuery message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 159           $ $Date:: 2009-09-22 #$
 */
public class GetUpdQuery extends QueryMessage
{
   private final String QueryType = "GETUPD";
   private int DiaryID = -1;
   private int RevNo = -1;

   private final int diaryIDLen = 4;
   private final int revNumLen = 8;

   private final int GetUpdQueryLen = diaryIDLen + revNumLen;

    /**
    * Constructor does nothing
    */
   public GetUpdQuery() { }

   /**
    * The Get() Method gets the GetUpdQuery message with the Message and Query header.
    *<p>
    * @return The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    * @see TBA.Messages.QueryMessage#Get(java.lang.String)
    * @see TBA.Messages.Messages#Get(java.lang.String)
    * @param dummy - to override Super Get(String)
    */
   @Override
   public String Get(String dummy) throws MessagesException
   {
      String message = null;

       if (DiaryID == -1)
       {
           throw new MessagesException("Diary ID not set");
       }
      
       if (RevNo == -1)
       {
           throw new MessagesException("revision number is not set");
       }

      message = String.format("%0" + Integer.toString(diaryIDLen) + "x", DiaryID);
      message += String.format("%0" + Integer.toString(revNumLen) + "x", RevNo);

      super.SetQueryType(QueryType); 
      return super.Get(message);
   }

   /**
    * The Set() Method strips out all the fields of a GetUpdQuery message. They
    * are then available by the Get methods of this class.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String)
    * @see TBA.Messages.QueryMessage#Set(java.lang.String)
    * @see TBA.Messages.Messages#Set(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      message = super.Set(message);

      if(message.length() != GetUpdQueryLen)
      {
         throw new MessagesException("Get Update Query is invalid");
      }

      try
      {
         DiaryID = Integer.parseInt(message.substring(0,diaryIDLen),16); //16 coz hexidecimal
         message = message.substring(diaryIDLen, message.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("entry ID is invalid");
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


      return null;
   }

   /**
    * This method returns the revision number
    * @return RevNo field
    */
   public int GetRevisionNumber()
   {
       return RevNo;
   }

   /**
    * Sets RevNo field, cannot be < 0
    * @param newRevNo
    * @throws MessagesException
    */
   public void SetRevisionNumber(int newRevNo) throws MessagesException
   {
       if (newRevNo  < 0)
       {
           throw new MessagesException("Revision number is null");
       }
       else
       {
          RevNo = newRevNo;
       }
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
    * Sets DiaryID field to newID, cannot be <0
    * @param newID
    * @throws MessagesException
    */
   public void SetDiaryID(int newID) throws MessagesException
   {
       if (newID  < 0)
       {
           throw new MessagesException("New Revision ID is null");
       }
       else
       {
          DiaryID = newID;
       }
   }
}