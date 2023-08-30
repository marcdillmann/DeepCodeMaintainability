/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is the root of all response messages between the client and
 * the server. It handles the 'Response Header' section of the message only.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 204           $ $Date:: 2009-10-15 #$
 */
public class ResponseMessage extends Messages
{
   private int errorCode = -1;
   private String errorMessage = null;
   private final int errorCodeLen = 1;

    /**
    * Constructor does nothing
    */
   public ResponseMessage() { }

   /**
    * The Get() Method adds the 'Response Header' to the beginning of the message
    * passed into it. It then passes it on to Messages to process the
    * 'Message Header'
    *<p>
    * @param childMessage The message to send, not including the 'Message Header'
    *<p>
    * @return The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    * @see TBA.Messages.Messages#Get(java.lang.String)
    */
   @Override
   public String Get(String childMessage) throws MessagesException
   {
      String message = null;

      if(errorCode == -1)
      {
         throw new MessagesException("Error/Success code not set");
      }

      if(errorCode > 0)
      {
         childMessage = errorMessage + '|';
      }

      if(childMessage == null)
      {
         childMessage = "";
      }

      message = String.format("%0" + Integer.toString(errorCodeLen) + "x", errorCode) + childMessage;

      super.SetMessageType('R');
      return super.Get(message);
   }

   /**
    * The Set() Method takes a Message and strips out the 'Response Header'
    * information, returning the rest of the message back so that it can then
    * be processed.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @return The message without the 'Response Header'
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String)
    * @see TBA.Messages.Messages#Set(java.lang.String) 
    */
   @Override
   public String Set(String message) throws MessagesException
   { 
      message = super.Set(message);

      if(message.length() < 1)
      {
         throw new MessagesException("The Response Header is incomplete");
      }

      try
      {
         errorCode = Integer.parseInt(message.substring(0,errorCodeLen), 16);
         message = message.substring(errorCodeLen, message.length());
      }
      catch (NumberFormatException ex)
      {
         throw new MessagesException("The Error Code is invalid");
      }

      if(errorCode > 0)
      {
         int delimPos = message.indexOf("|");
         errorMessage = message.substring(0,delimPos);
         message = message.substring(delimPos + 1, message.length());
      }

      return message;
   }

   /**
    * This method returns the 'Error Code' field. You must have called either
    * Set() or SetErrorCode() first.
    *<p>
    * @return The Error Code:
    * 0 = Success
    * >0 = A Failure
    *<p>
    * @see #SetErrorCode(int)
    */
   public int GetErrorCode()
   {
      return errorCode;
   }

   /**
    * This method allows you to set the Error Code field.
    *<p>
    * @param Code The 'Error Code' to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetErrorCode()
    */
   public void SetErrorCode(int Code) throws MessagesException
   {
      if(Code < 0 || Code > 15)
      {
         errorCode = -1;
         throw new MessagesException("The Error Code is out of range");
      }
      else
      {
         errorCode = Code;
      }
   }

   /**
    * This method returns the 'Error Message' field. You must have called either
    * Set() or SetErrorMessage() first.
    *<p>
    * @return The Error Message:
    *<p>
    * @see #SetErrorMessage(java.lang.String)
    */
   public String GetErrorMessage()
   {
      return errorMessage;
   }

   /**
    * This method allows you to set the 'Error Message' field. Note, this field
    * will only be sent if the Error Code field is > 0.
    *<p>
    * @param Message The Error Message to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetErrorMessage()
    */
   public void SetErrorMessage(String Message) throws MessagesException
   {
      if(Message == null)
      {
         errorMessage = null;
         throw new MessagesException("The Error Message is null");
      }
      else
      {
         errorMessage = Message;
      }
   }
}