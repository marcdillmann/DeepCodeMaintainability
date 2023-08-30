/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is the root of all query messages between the client and
 * the server. It handles the 'query Header' section of the message only.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 247           $ $Date:: 2009-10-23 #$
 */
public class QueryMessage extends Messages
{
   private String queryType = null;
   private final int queryTypeLen = 8;
   /**
    * Constructor does nothing
    */
   public QueryMessage() { }

   /**
    * The Get() Method adds the 'Query Header' to the beginning of the message
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
    */
   @Override
   protected String Get(String childMessage) throws MessagesException
   {
      String message = null;

      if(queryType == null)
      {
         throw new MessagesException("Query Type not set");
      }

      message = String.format("%1$-" + Integer.toString(queryTypeLen) + "s", queryType);

      if(childMessage == null)
      {
         childMessage = "";
      }

      super.SetMessageType('Q');
      return super.Get(message + childMessage);
   }

   /**
    * The Set() Method takes a Message and strips out the 'Query Header'
    * information, returning the rest of the message back so that it can then
    * be processed.
    *<p>s
    * @param message The message as recieved.
    *<p>
    * @return The message without the 'Query Header'
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      message = super.Set(message);

      if(message.length() < queryTypeLen)
      {
         throw new MessagesException("The Query Header is incomplete");
      }

      queryType = message.substring(0, queryTypeLen).trim();
      message = message.substring(queryTypeLen, message.length());

      return message;
   }

   /**
    * This method returns the 'Query Type' field. You must have called either
    * Set() or SetQueryType() first.
    *<p>
    * @return The Query Type
    *<p>
    * @see #SetQueryType(java.lang.String)
    */
   public String GetQueryType()
   {
      return queryType;
   }

   /**
    * This method allows you to set the Query Type field.
    *<p>
    * @param Type The Query to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetQueryType()
    */
   public void SetQueryType(String Type) throws MessagesException
   {
      if(Type == null)
      {
         queryType = null;
         throw new MessagesException("The Query Type is null");
      }
      else
      {
         queryType = Type;
      }
   }
}