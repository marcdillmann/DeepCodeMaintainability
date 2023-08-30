/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Exceptions;

/**
 *
 * @author dmcgra
 */
public class MessagesException extends Exception
{
   public MessagesException() {}

   public MessagesException(String message)
   {
      super(message);
   }

   public MessagesException(Throwable cause)
   {
      super(cause);
   }

   public MessagesException(String message, Throwable cause)
   {
      super(message, cause);
   }
} 