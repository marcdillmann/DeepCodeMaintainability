package TBA.Communications;

import TBA.Exceptions.*;
import TBA.Messages.*;
import TBA.Data.*;
import TBA.Security.PasswordHashing;
import java.io.*;
import java.util.Calendar;
import java.util.Vector;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.util.logging.Logger;

/**
 * This class handles the connection to the TBA server
 *<p>
 *
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 244           $ $Date:: 2009-10-22 #$
 */

public class ServerCom
{
   private SSLSocket myConx = null;
   private int socketPort = 0;
   private String socketAddress = null;
   private DataOutputStream os;
   private DataInputStream is;
   private boolean isConnected = false;
   private DBServer DBCom;
   private String lastError = null;

   /** LOGIT field provides 4 levels of feedback to user info, fine, finer , severe*/
   public final static Logger LOGIT = Logger.getLogger(ServerCom.class.getName());

   /**
    * This returns the error message response from the last server command,
    * if there was one, or null if there was not. It is cleared every time a
    * server command is called, including when calling this method.
    * @return ret - error message or null if none
    */
   public String getLastError()
   {
      String ret = null;
      if(lastError != null)
      {
         ret = new String(lastError);
      }
      lastError = null;

      return ret;
   }

   /**
    * This static member is used to extract the SSL key store from within the
    * JAR file.
    */
   public static void extractKeyStore()
   {
      InputStream keyfile = ServerCom.class.getResourceAsStream("tbaKeyStore");
      
      File outFile = new File("tbaKeyStore");
      OutputStream out;
      try
      {
         out = new FileOutputStream(outFile);
         // Transfer bytes from in to out
         byte[] buf = new byte[1024];
         int len;
         while ((len = keyfile.read(buf)) > 0)
         {
            out.write(buf, 0, len);
         }
         keyfile.close();
         out.close();
      }
      catch (FileNotFoundException ex)
      { }
      catch (IOException ex)
      { }
   }

   /**
    * This constructor initialises the connection to the TBA Server
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
         if(DBCom == null)
         {
            DBCom = new DBServer("", "");
            DBCom.setup();
         }
      }
      catch (DBServerException ex)
      {
         DBCom = null;
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket");
         throw new ServerComException(ex);
      }

      try
      {
         socketAddress = machineName;
         socketPort = portNumber;
         SSLSocketFactory sslSockFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
         myConx = (SSLSocket) sslSockFactory.createSocket(machineName, portNumber);
         isConnected = true;
      }
      catch (IOException ex)
      {
         isConnected = false;
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
         isConnected = false;
         throw new ServerComException(ex);
      }
   }

   /**
    * This method attempts to authenticate and log in the user. It will first
    * attempt via the network to the remote server. If that is not connected it
    * will attempt via the local database.
    * @param Username
    * @param Password
    * @return The User Object if the user successfully logs in, otherwise null
    */
   public User Login(String Username, String Password)
   {
      lastError = null;
      User loginUser = null;

      try
      {
         if(!isConnected)
         {
            Start(socketAddress, socketPort);
         }

         if(isConnected)
         {
            loginUser = NetLogin(Username, Password);
            if(loginUser != null)
            {
               try
               {
                  if(DBCom != null)
                  {
                     DBCom.addUser(Username, Password, loginUser);
                  }
               }
               catch (DBServerException ex)
               {
                  DBCom = null;
                  LOGIT.severe("Local database error. Local cache disabled");
                  LOGIT.info(ex.getLocalizedMessage());
               }
            }
         }
      }
      catch(ServerComException ex)
      {
         if(isConnected)
         {
            isConnected = false;
            LOGIT.severe("Connection lost during attempted login");
            LOGIT.info(ex.getLocalizedMessage());
         }
      }
      finally
      {
         if(!isConnected)
         {
            LOGIT.fine("No network connection, attempting DB");
            loginUser = DBLogin(Username, Password);
         }
      }
      
      return loginUser;
   }

   /**
    * This method attempts to authenticate and log in the user via the network
    * to the remote server.
    * @param Username
    * @param Password
    * @return The User Object if the user successfully logs in, otherwise null
    * @throws ServerComException
    */
   public User NetLogin(String Username, String Password) throws ServerComException
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
            lastError = lR.GetErrorMessage();
            return null;
         }
         else
         {
            User currentUser = new User();
            Vector<Diary> diaries = new Vector<Diary>();
            Diary aDiary ;

            currentUser.setDisplayName(lR.GetDisplayName().trim());
            currentUser.setDefaultDiaryID(lR.GetDefaultDiary());
            currentUser.setSessionID(lR.GetSessionID());


            for(DiaryHeader aDiaryHeader : lR.GetDiaryHeaders())
            {
               aDiary = new Diary();
               aDiary.setID(aDiaryHeader.GetDiaryID());
               aDiary.setName(aDiaryHeader.GetDiaryName());
               aDiary.setPermissions(aDiaryHeader.GetPermissions());
               aDiary.setOwnerFlag(aDiaryHeader.GetOwnerFlag());
               aDiary.setRevision(aDiaryHeader.GetRevNum());
               diaries.add(aDiary);
            }

            currentUser.setDiaries(diaries);

            return currentUser;
         }
      }
      catch (NetworkDataException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         isConnected = false;
         throw new ServerComException("Login error");
      }
      catch (MessagesException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("Login error");
      }
      catch (PasswordHashingException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("Login error");
      }
   }

   /**
    * This method attempts to authenticate and log in the user via the local
    * database cache
    * @param Username
    * @param Password
    * @return The User Object if the user successfully logs in, otherwise null
    */
   public User DBLogin(String Username, String Password)
   {
      User ret = null;
      try
      {
         if(DBCom != null)
         {
            PasswordHashing ph = new PasswordHashing();

            ret = DBCom.CheckUser(Username, ph.getClientHash(Password));
         }
      }
      catch (DBServerException ex)
      {
         DBCom = null;
         LOGIT.severe("Local database error. Local cache disabled");
         LOGIT.info(ex.getLocalizedMessage());
      }
      catch (PasswordHashingException ex)
      {
         DBCom = null;
         LOGIT.severe("Password hashing error. Local cache disabled");
         LOGIT.info(ex.getLocalizedMessage());
      }
      
      return ret;
   }

   /**
    * This method attempts to log out the user. It will attempt via the
    * network to the remote server. If that is not connected it
    * will attempt via the local database.
    * @param sessionID
    * @throws ServerComException
    */
   public void Logout(String sessionID) throws ServerComException
   {
      lastError = null;
      if(!isConnected)
      {
         Start(socketAddress, socketPort);
      }
      try
      {
         if(isConnected)
         {
            NetLogout(sessionID);
         }
      }
      catch(ServerComException ex)
      {
         if(isConnected)
         {
            isConnected = false;
            LOGIT.severe("Unable to logout via the network");
            LOGIT.info(ex.getLocalizedMessage());
         }
      }
      finally
      {
         if(!isConnected)
         {
            LOGIT.info("No network connection, attempting DB");
            //DBLogin(sessionID);
         }
      }
   }

   /**
    * This method attempts to log out the user. It will attempt via the
    * network to the remote server.
    * @param sessionID
    * @throws ServerComException
    */
   public void NetLogout(String sessionID) throws ServerComException
   {
      LogoutQuery lQ = new LogoutQuery();
      try
      {
         lQ.SetSessionID(sessionID);

         putData(lQ.Get(""));

         // Pretty much for show, since we can't actually do anything about a
         // failed logout attempt...
         Messages header = new Messages();
         String headerData = getHeaderData();
         header.Set(headerData);
         String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);
         LogoutResponse lR = new LogoutResponse();
         lR.Set(headerData + bodyData);
         lastError = lR.GetErrorMessage();
      }
      catch (MessagesException ex)
      {
         throw new ServerComException(ex.getLocalizedMessage());
      }
      catch (NetworkDataException ex)
      {
         throw new ServerComException(ex.getLocalizedMessage());
      }
   }

   /**
    * Attempt to get a diary. If it can not do it via the network interface,
    * it will attempt to do so via the local database. If retrieved from the
    * network interface, it will still check the local database for any modified
    * or new entries.
    *<p>
    * @param diaryID The ID of the diary to retrieve
    * @param sessionID The current session ID with the network server
    *<p>
    * @return The Diary
    *<p>
    * @throws ServerComException 
    */
   public Diary GetDiary(int diaryID, String sessionID) throws ServerComException
   {
      Diary ret = null;
      lastError = null;

      try
      {
         if(!isConnected)
         {
            Start(socketAddress, socketPort);
         }

         if(isConnected)
         {
            ret = NetGetDiary(diaryID, sessionID);
            if(ret != null && DBCom != null)
            {
               // Check if any new/modified entries
               // are stored locally.
               if(DBCom.getLocalEntries(ret))
               {
                  //If there are, add them back to the diary then update the
                  // server
                  UpdateDiary(ret, sessionID);
                  ret = NetGetDiary(diaryID, sessionID);
               }
               DBCom.addDiary(ret);
            }
         }
      }
      catch(DBServerException ex)
      {
         LOGIT.severe("Unable to save diary to the local cache");
         LOGIT.info(ex.getLocalizedMessage());
         DBCom = null;
      }
      catch(ServerComException ex)
      {
         if(isConnected)
         {
            isConnected = false;
            LOGIT.severe("Unable to retrieve diary via the network");
            LOGIT.info(ex.getLocalizedMessage());
         }
      }
      finally
      {
         if(!isConnected)
         {
            LOGIT.fine("No network connection, attempting DB");
            ret = DBGetDiary(diaryID, sessionID);
         }
      }

      return ret;
   }

   /**
    * Using the network interface, attempt to retrieve a diary from the server.
    *<p>
    * @param diaryID The ID for the diary to retrieve.
    *<p>
    * @param sessionID The ID for for the current logged in session
    * @return The Diary
    *<p>
    * @throws ServerComException
    */
   public Diary NetGetDiary(int diaryID, String sessionID) throws ServerComException
   {
      GetDiaryQuery diaryQuery = new GetDiaryQuery();
      try
      {
         diaryQuery.SetDiaryID(diaryID);
         diaryQuery.SetSessionID(sessionID);

         putData(diaryQuery.Get(""));

         Messages header = new Messages();

         String headerData = getHeaderData();
         header.Set(headerData);

         String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);

         GetDiaryResponse diaryResponse = new GetDiaryResponse();
         diaryResponse.Set(headerData + bodyData);

         if(diaryResponse.GetErrorCode() != 0)
         {
            diaryResponse.GetErrorMessage();
            return null;
         }
         else
         {
            Diary returnedDiary = new Diary();
            Vector<Entry> entries = new Vector<Entry>();
            Entry anEntry ;

            returnedDiary.setID(diaryID);
            returnedDiary.setName(diaryResponse.getDiaryName());
            returnedDiary.setRevision(diaryResponse.getPermissions());
            returnedDiary.setOwnerFlag(diaryResponse.getOwnerFlag());
            returnedDiary.setPermissions(diaryResponse.getPermissions());

            Calendar start;
            Calendar end;
            for(DiaryEntry aDiaryEntry : diaryResponse.getEntries())
            {
               anEntry = new Entry();
               anEntry.setID(aDiaryEntry.getEntryID());
               anEntry.setSubject(aDiaryEntry.getTitle());
               anEntry.setBody(aDiaryEntry.getBody());
               anEntry.setOwner(aDiaryEntry.getCreatingUser());

               start = Calendar.getInstance();
               end = Calendar.getInstance();

               start.setTimeInMillis(aDiaryEntry.getStart());
               end.setTimeInMillis(aDiaryEntry.getEnd());

               anEntry.setStart(start);
               anEntry.setEnd(end);
               
               entries.add(anEntry);
            }

            returnedDiary.setEntries(entries);

            return returnedDiary;
         }
      }
      catch (NetworkDataException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("GetDiary error");
         isConnected = false;
         throw new ServerComException("GetDiary error");
      }
      catch (MessagesException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Login error");
         throw new ServerComException("GetDiary error");
      }
   }


   /**
    * Using the local database cache, attempt to retrieve a diary.
    *<p>
    * @param diaryID The ID for the diary to retrieve.
    *<p>
    * @param sessionID The ID for for the current logged in session
    * @return The Diary
    *<p>
    * @throws ServerComException
    */
   public Diary DBGetDiary(int diaryID, String sessionID) throws ServerComException
   {
      Diary ret = null;
      try
      {
//         if (DBCom.GetSession(sessionID) == null)
//         {
//            return null;
//         }
         ret = DBCom.GetDiary(diaryID);
      }
      catch (DBServerException ex)
      {
         DBCom = null;
         LOGIT.severe("Local database error. Local cache disabled");
         LOGIT.info(ex.getLocalizedMessage());
      }

      return ret;
   }

   /**
    * This will attempt to send/store the updated information
    * about a diary and its entries.
    *<p>
    * @param updatedDiary The diary that has been updated
    * @param sessionID The current session ID with the network server
    *<p>
    * @throws ServerComException
    */
   public void UpdateDiary(Diary updatedDiary, String sessionID) throws ServerComException
   {
      lastError = null;
      try
      {
         if(!isConnected)
         {
            Start(socketAddress, socketPort);
         }
         if(isConnected)
         {
            netUpdateDiary(updatedDiary, sessionID);
            DBCom.deleteLocalEntries(updatedDiary.getID());
         }
      }
      catch(ServerComException ex)
      {
         if(isConnected)
         {
            isConnected = false;
            LOGIT.severe("Unable to update diary via the network");
            LOGIT.info(ex.getLocalizedMessage());
            throw ex;
         }
      }
      finally
      {
         if(!isConnected)
         {
            LOGIT.fine("No network connection, attempting DB");
            DBUpdateDiary(updatedDiary, sessionID);
         }
      }
   }

   /**
    * Using the network interface, attempt to update a diary to the server.
    *<p>
    * @param updatedDiary The updated diary
    * @param sessionID The current session ID with the network server
    *<p>
    * @throws ServerComException
    */
   public void netUpdateDiary(Diary updatedDiary, String sessionID) throws ServerComException
   {
      RetUpdQuery updateQuery = new RetUpdQuery();
      try
      {
         updateQuery.SetDiaryID(updatedDiary.getID());
         updateQuery.SetSessionID(sessionID);
         updateQuery.setDiaryName(updatedDiary.getName());
         updateQuery.setPermissions(updatedDiary.getPermissions());
         updateQuery.setRevisionNo(updatedDiary.getRevision());

         Vector<DiaryEntry> diaryEntries = new Vector<DiaryEntry>();
         DiaryEntry aDiaryEntry;
         for(Entry nextEntry : updatedDiary.getModifiedEntries())
         {
            aDiaryEntry = new DiaryEntry();
            aDiaryEntry.setBody(nextEntry.getBody());
            aDiaryEntry.setCreatingUser("");

            if(nextEntry.isLocked())
            {
               aDiaryEntry.setLockedFlag('Y');
            }
            else
            {
               aDiaryEntry.setLockedFlag('N');
            }

            if(nextEntry.isDeleted())
            {
               aDiaryEntry.setDeletedFlag('Y');
            }
            else
            {
               aDiaryEntry.setDeletedFlag('N');
            }

            aDiaryEntry.setTitle(nextEntry.getSubject());
            aDiaryEntry.setEntryID(nextEntry.getID());
//TODO: Fix up entry ownership
//            if(nextEntry.getOwner().compareTo(username) == 0)
//            {
               aDiaryEntry.setOwnerFlag('Y');
//            }
//            else
//            {
//               aDiaryEntry.setOwnerFlag('N');
//            }
            aDiaryEntry.setStart(nextEntry.getStart().getTimeInMillis());
            aDiaryEntry.setEnd(nextEntry.getEnd().getTimeInMillis());

            diaryEntries.add(aDiaryEntry);
         }

         updateQuery.setEntries(diaryEntries);

         putData(updateQuery.Get(""));

         Messages header = new Messages();

         String headerData = getHeaderData();
         header.Set(headerData);

         String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);

         RetUpdResponse updateResponse = new RetUpdResponse();
         updateResponse.Set(headerData + bodyData);

         if(updateResponse.GetErrorCode() != 0)
         {
            //TODO: Work out how to handle errors!
            updatedDiary.clearModified(); // TODO: Remove this when RetUpd works fully
            LOGIT.warning(updateResponse.GetErrorMessage());
            lastError = updateResponse.GetErrorMessage();
         }
         else
         {
            updatedDiary.clearModified();
            //TODO: Implement Return Update response handling
         }
      }
      catch (NetworkDataException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Return Update error");
         isConnected = false;
         throw new ServerComException("Return Update error");
      }
      catch (MessagesException ex)
      {
         LOGIT.fine(ex.getLocalizedMessage());
         LOGIT.severe("Return Update error");
         throw new ServerComException("Return Update error");
      }
   }

   /**
    * Using the network interface, attempt to update a diary to the server.
    *<p>
    * @param updatedDiary The updated diary
    * @param sessionID The current session ID with the network server
    *<p>
    */
   public void DBUpdateDiary(Diary updatedDiary, String sessionID)
   {
      // Check Session here.
      try
      {
         DBCom.UpdateEntries(updatedDiary);
      }
      catch (DBServerException ex)
      {
         LOGIT.severe("Error updating server");
         // TODO: Need to handle this
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
         os.flush();
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Failed recieving data from the server");
         throw new ServerComException(ex);
      }
   }
}