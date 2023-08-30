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
   public NetworkDataException() {}

   public NetworkDataException(String message)
   {
      super(message);
   }

   public NetworkDataException(Throwable cause)
   {
      super(cause);
   }

   public NetworkDataException(String message, Throwable cause)
   {
      super(message, cause);
   }
}