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
   public HandleThreadException() {}

   public HandleThreadException(String message)
   {
      super(message);
   }

   public HandleThreadException(Throwable cause)
   {
      super(cause);
   }

   public HandleThreadException(String message, Throwable cause)
   {
      super(message, cause);
   }
}