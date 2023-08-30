/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class HandleThreadException extends Exception
{
    /**
     * Constructor does nothing
     */
   public HandleThreadException() {}

   /**
    * This method passes the message to super Exception
    * @param message
    */
   public HandleThreadException(String message)
   {
      super(message);
   }

   /**
    * This method passes the cause to the super Exception
    * @param cause
    */
   public HandleThreadException(Throwable cause)
   {
      super(cause);
   }

   /**
    * This method passes message and cause to super Exception
    * @param message
    * @param cause
    */
   public HandleThreadException(String message, Throwable cause)
   {
      super(message, cause);
   }
}