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
    /**
     * Constructor does noting
     */
   public DBServerException() {}

   /**
    * This method passes message to exception
    * @param message
    */
   public DBServerException(String message)
   {
      super(message);
   }

   /**
    * This method passes cause to exception
    * @param cause
    */
   public DBServerException(Throwable cause)
   {
      super(cause);
   }

   /**
    * This method passes both message and cause to exception
    * @param message
    * @param cause
    */
   public DBServerException(String message, Throwable cause)
   {
      super(message, cause);
   }
}