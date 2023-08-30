package tbaclient;

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
   public ServerComException() {}

   public ServerComException(String message)
   {
      super(message);
   }

   public ServerComException(Throwable cause)
   {
      super(cause);
   }

   public ServerComException(String message, Throwable cause)
   {
      super(message, cause);
   }
}
