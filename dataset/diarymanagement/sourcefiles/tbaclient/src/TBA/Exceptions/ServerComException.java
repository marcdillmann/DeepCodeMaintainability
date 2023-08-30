package TBA.Exceptions;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author dmcgra
 */
public class ServerComException extends Exception
{
   /**
   * Constructor does nothing
   */
   public ServerComException() {}

    /**
    * This method passes the message to super Exception
    * @param message
    */
   public ServerComException(String message)
   {
      super(message);
   }

    /**
    * This method passes the cause to the super Exception
    * @param cause
    */
   public ServerComException(Throwable cause)
   {
      super(cause);
   }

    /**
    * This method passes both message and cause to exception
    * @param message
    * @param cause
    */
   public ServerComException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
