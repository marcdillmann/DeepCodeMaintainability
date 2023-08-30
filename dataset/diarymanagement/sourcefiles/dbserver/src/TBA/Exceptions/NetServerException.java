/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class NetServerException extends Exception
{
   /**
   * Constructor does nothing
   */
    public NetServerException() {}

    /**
     * This method passes the message to super Exception
     * @param message
     */
    public NetServerException(String message)
   {
      super(message);
   }

   /**
    * This method passes the cause to the super Exception
    * @param cause
    */
   public NetServerException(Throwable cause)
   {
      super(cause);
   }

   /**
    * This method passes both message and cause to exception
    * @param message
    * @param cause
    */
   public NetServerException(String message, Throwable cause)
   {
      super(message, cause);
   }
}