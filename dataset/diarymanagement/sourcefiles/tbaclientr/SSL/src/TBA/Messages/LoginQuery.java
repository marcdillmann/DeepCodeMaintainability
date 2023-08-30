package TBA.Messages;

import TBA.Exceptions.MessagesException;

/**
 * This class is for the LOGIN query message.
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 27            $ $Date:: 2009-07-31 #$
 */
public class LoginQuery extends QueryMessage
{
   private String userName = null;
   private String passwordHash = null;
   private final int userNameLen = 10;
   private final int passwordHashLen = 64;
   private final int loginQueryLen = userNameLen + passwordHashLen;
   private final String QueryType = "LOGIN";

   public LoginQuery() { }

   /**
    * The Get() Method gets the LOGIN message with the Message and Query header.
    *<p>
    * @returns The actual message string you can send
    *<p>
    * @throws MessagesException
    *<p>
    * @see #Set(java.lang.String)
    */
   @Override
   public String Get(String dummy) throws MessagesException
   {
      String message = null;

      if(userName == null)
      {
         throw new MessagesException("Username not set");
      }

      message = String.format("%1$-" + Integer.toString(userNameLen) + "s", userName);

      if(passwordHash.length() != passwordHashLen)
      {
         throw new MessagesException("Password Hash is not set or invalid");
      }

      message += passwordHash;
      
      super.SetQueryType(QueryType);
      
      return super.Get(message);
   }

   /**
    * The SetQuery() Method strips out all the fields of a Login Query message. They
    * are then available by the Get methods of this class.
    *<p>
    * @param message The message as recieved.
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetQuery(java.lang.String)
    */
   @Override
   public String Set(String message) throws MessagesException
   {
      message = super.Set(message);

      if(message.length() != loginQueryLen)
      {
         throw new MessagesException("The Login Query is invalid");
      }

      userName = message.substring(0, userNameLen).trim();
      passwordHash = message.substring(userNameLen, loginQueryLen);

      return null;
   }

   /**
    * This method returns the 'Username' field. You must have called either
    * SetQuery() or SetUserName() first.
    *<p>
    * @returns The Username
    *<p>
    * @see #SetUserName(java.lang.String)
    */
   public String GetUserName()
   {
      return userName;
   }

   /**
    * This method allows you to set the 'Username' field.
    *<p>
    * @param User The Username to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetUserName()
    */
   public void SetUserName(String User) throws MessagesException
   {
      if(User == null)
      {
         User = null;
         throw new MessagesException("The Username is null");
      }
      else
      {
         userName = User;
      }
   }

   /**
    * This method returns the 'Password hash' field. You must have called either
    * SetQuery() or SetPasswordHash() first.
    *<p>
    * @returns The Password Hash
    *<p>
    * @see #SetPasswordHash(java.lang.String)
    *
    */
   public String GetPasswordHash()
   {
      return passwordHash;
   }

   /**
    * This method allows you to set the 'Password hash' field.
    *<p>
    * @param Hash The Password Hash to set
    *<p>
    * @throws MessagesException
    *<p>
    * @see #GetPasswordHash()
    */
   public void SetPasswordHash(String Hash) throws MessagesException
   {
      if(Hash == null)
      {
         Hash = null;
         throw new MessagesException("The Password hash is null");
      }
      else
      {
         passwordHash = Hash;
      }
   }
}