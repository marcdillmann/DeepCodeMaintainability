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
   public PasswordHashingException() {}

   public PasswordHashingException(String message)
   {
      super(message);
   }

   public PasswordHashingException(Throwable cause)
   {
      super(cause);
   }

   public PasswordHashingException(String message, Throwable cause)
   {
      super(message, cause);
   }
} 