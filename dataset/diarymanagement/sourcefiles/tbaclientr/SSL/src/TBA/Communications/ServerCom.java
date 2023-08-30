package TBA.Communications;

import TBA.Exceptions.*;
import TBA.Messages.*;
import TBA.Data.*;
import TBA.Security.PasswordHashing;
import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles the connection to the TBA server
 *<p>
 *
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev::               $ $Date::             $
 */

public class ServerCom implements Runnable
{
   private SSLSocket myConx;
   private DataOutputStream os;
   private DataInputStream is;
   public final static Logger LOGIT = Logger.getLogger(ServerCom.class.getName());


   public void run()
   {
      while(1==1)
      {
         
      }
   }
   /**
    * This constructor initialises the connection to the TBA Server
    *<p>
    *<p>
    * @throws ServerComException
    *
    * @param machineName This is the IP address/Hostname to connect to
    * @param portNumber The port number to connect on.
    *
    * @see java.net.Socket
    */
   public void Start(String machineName, int portNumber) throws ServerComException
   {
      try
      {
         SSLSocketFactory sslSockFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         myConx = (SSLSocket) sslSockFactory.createSocket(machineName, portNumber);
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket");
         throw new ServerComException(ex);
      }

      try
      {
         os = new DataOutputStream(myConx.getOutputStream());
         is = new DataInputStream(myConx.getInputStream());
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating stream for the socket");
         throw new ServerComException(ex);
      }
   }

   public User Login(String Username, String Password) throws ServerComException
   {
      LoginQuery lQ = new LoginQuery();
      try
      {
         PasswordHashing ph = new PasswordHashing();

         lQ.SetUserName(Username);
         lQ.SetPasswordHash(ph.getClientHash(Password));
         putData(lQ.Get(""));

         Messages header = new Messages();

         String headerData = getHeaderData();
         header.Set(headerData);

         String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);

         LoginResponse lR = new LoginResponse();
         lR.Set(headerData + bodyData);

         if(lR.GetErrorCode() != 0)
         {
            return null;
         }
         else
         {
            User currentUser = new User();

            currentUser.setDisplayName(lR.GetDisplayName());
            currentUser.setDefaultDiaryID(lR.GetDefaultDiary());
            currentUser.setSessionID(lR.GetSessionID());

            return currentUser;
         }
      }
      catch (Exception ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("Login error");
      }
   }

   /**
    * Receive the header data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   private String getHeaderData() throws NetworkDataException
   {
      byte[] s=new byte[Messages.messageHeaderLen];

      try
      {
         // Confirming size of message is correct is
         // handled later by the Messages.* classes themselves
         is.readFully(s, 0, Messages.messageHeaderLen);
      }
      catch(Exception ex)
      {
         LOGIT.severe("Unable to get data from client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, Messages.messageHeaderLen);
   }

   /**
    * Receive data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   private String getData(int size) throws NetworkDataException
   {
      byte[] s=new byte[size];
      try
      {
         // Confirming size of messages is correct is
         // handled later by the Messages.* classes themselves
         is.readFully(s, 0, size);
      }
      catch(Exception ex)
      {
         LOGIT.severe("Unable to get data from client");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, size);
   }

   /**
    * This sends data to the server
    *<p>
    *<p>
    * @throws ServerComException
    *
    * @param data Data to send to the server
    *
    * @see #getData()
    */
   private void putData(String data) throws ServerComException
   {
      try
      {
         os.writeBytes(data);
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Failed recieving data from the server");
         throw new ServerComException(ex);
      }
   }
}