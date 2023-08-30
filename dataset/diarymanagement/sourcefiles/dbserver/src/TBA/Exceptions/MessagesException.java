/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class MessagesException extends Exception
{
    /**
     * Constructor does nothing
     */
   public MessagesException() {}

   /**
    * This method passes message to super Exception
    * @param message
    */
   public MessagesException(String message)
   {
      super(message);
   }

   /**
    * This method passes cause to super Exception
    * @param cause
    */
   public MessagesException(Throwable cause)
   {
      super(cause);
   }

   /**
    * This method passes message and cause to super Exception
    * @param message
    * @param cause
    */
   public MessagesException(String message, Throwable cause)
   {
      super(message, cause);
   }
} 