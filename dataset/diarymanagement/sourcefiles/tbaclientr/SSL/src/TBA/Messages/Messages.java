package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is the root of all messages between the client and
 * the server. It handles the 'Message Header' section of the message only.
 *<p> 
 * @author Dan McGrath
 * 
 * @version $Rev:: 27            $ $Date:: 2009-07-31 #$
 */
public class Messages
{
   private char messageType = '\0';
   private String sessionID = null;
   private int messageLen = 0;
   private final int messageLenSize = 6;
   private final int sessionIDLen = 36;
   public static final int messageHeaderLen =  43;//messageLenSize+sessionIDLen+1
   
   public Messages(){ }
   
   /**
    * This method converts the message length (int) to the message length (hex)
    * included the length of the message length field.
    *<p> 
    * @param len The length of the message, not included the message length field
    * @returns A zero-padded string of the message length
    */
   private String FormatLen(int len)
   {
      len += messageLenSize;
      return String.format("%0"+Integer.toString(messageLenSize)+"x", len);
   }
   
   /**
    * The Get() Method adds the 'Message Header' to the beginning of the message
    * passed into it. It automatically calculates the message length field
    * for you.
    *<p> 
    * @param childMessage The message to send, not including the 'Message Header'
    *<p>
    * @returns The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String) 
    */
   protected String Get(String childMessage) throws MessagesException
   {
      String message = null;
      
      if(messageType == '\0')
      {
         throw new MessagesException("Message Type not set");
      }
      if(sessionID == null)
      {
         sessionID = String.format("%0"+Integer.toString(sessionIDLen)+"d",0);
      } else if (sessionID.length() != sessionIDLen)
      {
         throw new MessagesException("Invalid Session ID");
      }
      if(childMessage == null)
      {
         childMessage = "";
      }
      
      message = Character.toString(messageType);
      message += sessionID;
      message += FormatLen(message.length()+childMessage.length());
      message += childMessage;
      
      return message;
   }
   
   /**
    * The Set() Method takes a Message and strips out the message header information
    * returning the rest of the message back so that it can then be processed.
    *<p> 
    * @param message The message as recieved.
    *<p>
    * @returns The message without the 'Message Header'
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String) 
    */
   public String Set(String message) throws MessagesException
   {
      int messageLenPos = sessionIDLen + messageLenSize + 1;
      String messageLenString;

      if(message.length() < messageLenPos)
      {
         throw new MessagesException("The Message Header is incomplete");
      }

      messageLenString = message.substring(sessionIDLen + 2, messageLenPos);
      try
      {
         messageLen = Integer.parseInt(messageLenString, 16);
      }
      catch(NumberFormatException ex)
      {
         throw new MessagesException("The Message length is formatted incorrectly!");
      }
  
      messageType = message.charAt(0);
      if(messageType != 'R' && messageType !='Q')
      {
         messageType = '\0';
         throw new MessagesException("The message is of an unknown type");
      }
      
      sessionID = message.substring(1, sessionIDLen+1);

      return message.substring(messageLenPos, message.length());
   }
   
   /**
    * This method returns the 'Message Type' field. You must have called either
    * Set() or SetMessageType() first.
    *<p> 
    * @returns The Message Type. Possible values are:
    * 'Q' for Query,
    * 'R' for Response, or
    * '\0' for Message not currently set.
    *<p>
    * @throws MessagesException
    *<p>
    * @see #SetMessageType(char) 
    */
   public char GetMessageType()
   {
      return messageType;
   }
   
   /**
    * This method allows you to set the Message Type in the 'Message Header'.
    *<p> 
    * @param Type Valid values are 'Q' for query or 'R' for Response. All others
    * will throw an exception
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetMessageType() 
    */
   protected void SetMessageType(char Type) throws MessagesException
   {
      if(Type != 'R' && Type !='Q')
      {
         messageType = '\0';
         throw new MessagesException("The message type is invalid");
      }
      else
      {
         messageType = Type;
      }
   }

   /**
    * This method returns the message length
    *<p>
    * @returns The Message length
    *<p>
    */
   public int GetMessageLen()
   {
      return messageLen;
   }
   
   /**
    * This method gets the Session ID from the 'Message Header'. You must have
    * called either Set() or SetSessionID() first.
    *<p> 
    * @returns The Session ID or null if one is not set.
    *<p>
    * @see #SetSessionID(java.lang.String) 
    */
   public String GetSessionID()
   {
      return sessionID;
   }
   
   /**
    * This method allows you to set the 'Session ID' field.
    *<p> 
    * @param ID The Session ID to set. Must be a 36-digit Hex number, as a string.
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetSessionID() 
    */
   public void SetSessionID(String ID) throws MessagesException
   {
      if(ID.length() != sessionIDLen)
      {
         sessionID = null;
         throw new MessagesException("The SessionID invalid");
      }
      else
      {
         // TODO: Confirm the Session ID only contains [0-9a-f]
         sessionID = ID;
      }
   }
}