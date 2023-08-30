/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class PasswordHashingException extends Exception
{
     /**
     * Constructor does nothing
     */
   public PasswordHashingException() {}

    /**
    * This method passes the message to super Exception
    * @param message
    */
   public PasswordHashingException(String message)
   {
      super(message);
   }

    /**
    * This method passes the message to super Exception
    * @param cause
    */
   public PasswordHashingException(Throwable cause)
   {
      super(cause);
   }

   /**
    * This method passes both message and cause to exception
    * @param message
    * @param cause
    */
   public PasswordHashingException(String message, Throwable cause)
   {
      super(message, cause);
   }
} 