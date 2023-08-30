/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class NetworkDataException extends Exception
{

    /**
     * Constructor does nothing
     */
   public NetworkDataException() {}

      /**
    * This method passes the message to super Exception
    * @param message
    */
   public NetworkDataException(String message)
   {
      super(message);
   }

   /**
    * This method passes the cause to the super Exception
    * @param cause
    */
   public NetworkDataException(Throwable cause)
   {
      super(cause);
   }

    /**
    * This method passes both message and cause to exception
    * @param message
    * @param cause
    */
   public NetworkDataException(String message, Throwable cause)
   {
      super(message, cause);
   }
}