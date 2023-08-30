package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is the response message to a Logout Query
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 27            $ $Date:: 2009-07-31 #$
 */
public class LogoutResponse extends ResponseMessage
{
   public LogoutResponse() {}

   /**
    * The Get() Method gets the LOGOUT message
    *<p>
    * @returns The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #SetResponse(java.lang.String)
    */
   @Override
   public String Get(String dummy) throws MessagesException
   {
      return super.Get("");
   }

   /**
    * The Set() Method strips out all the fields of a Logout Query message. They
    * are then available by the Get methods of this class.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @returns The message without the 'Response Header'
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Get(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      message = super.Set(message);
      return null;
   }
}