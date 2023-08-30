package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the LOGOUT query message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 204           $ $Date:: 2009-10-15 #$
 */
public class LogoutQuery extends QueryMessage
{
   private final String QueryType = "LOGOUT";

    /**
    * Constructor does nothing
    */
   public LogoutQuery() { }

   /**
    * The Get() Method gets the LOGOUT message with the Message and Query header.
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

      super.SetQueryType(QueryType);
      
      return super.Get(message);
   }

   /**
    * The Set() Method strips out all the fields of a Logout Query message. They
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

      if(message.length() != 0)
      {
         throw new MessagesException("The Login Query is invalid");
      }

      return null;
   }
}