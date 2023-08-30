/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class DBServerException extends Exception
{
   public DBServerException() {}

   public DBServerException(String message)
   {
      super(message);
   }

   public DBServerException(Throwable cause)
   {
      super(cause);
   }

   public DBServerException(String message, Throwable cause)
   {
      super(message, cause);
   }
}