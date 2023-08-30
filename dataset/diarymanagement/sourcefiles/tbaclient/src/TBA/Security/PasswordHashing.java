package TBA.Security;

import TBA.Exceptions.PasswordHashingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class handles the user password hashing.
 *<p> 
 * @author Dan McGrath
 * 
 * @version $Rev:: 164           $ $Date:: 2009-09-25 #$
 */
public class PasswordHashing
{
   
   /**
    * This method returns the SHA-256 hash of a string password.
    *<p>
    * @param password The client's raw password
    *<p>
    * @return A 64char Hex hash of the password
    *<p>
    * @throws PasswordHashingException
    *<p>
    * @see java.security.MessageDigest
    */
   public String getClientHash(String password) throws PasswordHashingException
   {
      byte[] hash;

      try
      {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         digest.reset();
         hash = digest.digest(password.getBytes("UTF-16"));
         
         return bytesToHex(hash);
      }
      catch (NoSuchAlgorithmException ex)
      {
         throw new PasswordHashingException("Current environment does not supply needed security algorithms. Please update Java");
      }
      catch (UnsupportedEncodingException ex)
      {
         throw new PasswordHashingException("Current environment does not supply needed character encoding. Please update Java");
      }
   }
   
   /**
    * This method returns the SHA-256 salted hash of a string password. As
    * stored in the TBA database
    *<p>
    * @param passwordHash The client's Hashed Password
    * @param PasswordSalt The user's salt, as stored on the TBA DB.
    *<p>
    * @return A 64char Hex salted hash of the password
    *<p>
    * @throws PasswordHashingException
    *<p>
    * @see java.security.MessageDigest
    */
   public String getServerHash(String passwordHash, String PasswordSalt) throws PasswordHashingException
   {
      byte[] hash;

      try
      {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         digest.reset();
         digest.update(PasswordSalt.getBytes("UTF-16"));
         hash = digest.digest(passwordHash.getBytes("UTF-16"));
         
         return bytesToHex(hash);
      }
      catch (NoSuchAlgorithmException ex)
      {
         throw new PasswordHashingException("Current environment does not supply needed security algorithms. Please update Java");
      }
      catch (UnsupportedEncodingException ex)
      {
         throw new PasswordHashingException("Current environment does not supply needed character encoding. Please update Java");
      }
   }
   
   /**
    * This method converts a byte array into a 64 char hex string
    *<p>
    * @param bytes The bytes to convert. as returned by MessageDigest
    *<p>
    * @returns A 64char Hex string
    *<p>
    */
   private String bytesToHex(byte[] bytes)
   {
      String hex;
      
      hex = new BigInteger(1,bytes).toString(16);
      
      return String.format("%64s", hex);
   }
}
