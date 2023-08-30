package TBA.Server;

import TBA.Data.Diary;
import TBA.Data.Entry;
import TBA.Data.User;
import TBA.Exceptions.*;
import TBA.Messages.*;
import TBA.Logging.WrappedLogger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.util.*;
import java.util.logging.Logger;
import java.io.*;
import java.net.SocketTimeoutException;

/**
 * This class runs the network section of the server
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 97   $ $Date:: 2009-09-06 #$
 */
public class NetServer implements Runnable
{
   ArrayList<Thread> threads = new ArrayList<Thread>();
   ArrayList<HandleThread> handles = new ArrayList<HandleThread>();
   private SSLServerSocket serv;
   private DBServer dbInstance;
   private volatile boolean finished = false;
   private final static WrappedLogger LOGIT = new WrappedLogger(Logger.getLogger(NetServer.class.getName()));

   /**
    * This is the constructor, starts the DBServer and initialises the
    * Network Server section
    *<p>
    * @throws HandleThreadException
    *<p>s
    * @param port The port number to open the server on
    *<p>
    * @see Socket
    * @see DBServer
    */
   NetServer(int port, DBServer db) throws NetServerException
   {
      dbInstance = db;
      try
      {
         SSLServerSocketFactory sslSSockFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
         serv = (SSLServerSocket)sslSSockFactory.createServerSocket(port);
      }
      catch (Exception ex)
      {
         LOGIT.severe(ex, "Unable to open the ssl server socket");
         throw new NetServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * Run the thread. This will communicate with the client and service its
    * requests
    *<p>
    * @see #waitForClient()
    */
   public void run()
   {
      LOGIT.info("Waiting for client's requests...");
      waitForClient();
      LOGIT.finer("Shutting NetServer down cleanly");
   }

   /**
    * Wait for a client to connect, then send them to be handled by another thread
    *<p>
    * @see HandleThread
    * @see Thread
    * @see Socket
    */
   private void waitForClient()
   {
      while (!finished)
      {
         try
         {
            serv.setSoTimeout(5000);
            SSLSocket s = (SSLSocket)serv.accept();
            HandleThread ht = new HandleThread(s, dbInstance);
            handles.add(ht);
            Thread t = new Thread(ht);

            LOGIT.finest("Start new thread to handle client");
            t.start();
            threads.add(t);

            // Delete any dead threads & handles
            for(int threadNum = threads.size() - 1; threadNum >= 0; threadNum--)
            {
               if(!threads.get(threadNum).isAlive())
               {
                  threads.remove(threadNum);
                  handles.remove(threadNum);
               }
            }
         }
         catch (SocketTimeoutException ex)
         { /* We only timeout to allow main thread to close this */ }
         catch (IOException ex)
         {
            LOGIT.severe(ex,"Unable to accept connections on the server socket");
         }
         catch (HandleThreadException ex)
         {
            LOGIT.severe(ex, "Unable start new thread for client");
         }
      }
   }

   /**
    * This method informs all threads to finish or close
    * @param finished - flag relating to whether or not finished
    */
   public void setFinished(boolean finished)
   {
      this.finished = finished;
      if(finished)
      {
         for(HandleThread ht : handles)
         {
            ht.setFinished(finished);
         }
      }
      
      if (Thread.currentThread() != null)
      {
         Thread.currentThread().interrupt();
      }
   }
}
/**
 * This class is a that handles 1 client per instance and runs as a thread
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 97   $ $Date:: 2009-09-06 #$
 */
class HandleThread implements Runnable
{
   private SSLSocket con;
   private DataInputStream in;
   private DataOutputStream out;
   private static DBServer dbInstance = null;
   private volatile boolean finished = false;
   private final static WrappedLogger LOGIT = new WrappedLogger(Logger.getLogger(Main.class.getName()));
   private final static String MESSAGENOTUNDERSTOOD = "The Server does not understand your CLIENT. Please confirm with your System Administrator that you are using the correct version.";

   /**
    * This is the constructor, which accepts a socket to communicate with.
    *<p>
    * If the database has not been instantiated yet, it will do so
    *<p>
    * @throws HandleThreadException
    *<p>
    * @param s The socket from the client
    *<p>
    * @see Socket
    * @see DBServer
    */
   public HandleThread(SSLSocket s, DBServer db) throws HandleThreadException
   {
      con = s;
      dbInstance = db;
   }

   /**
    * Run the thread. This will communicate with the client and service its
    * requests
    *<p>
    */
   public void run()
   {
      try
      {
         in = new DataInputStream(con.getInputStream());
         out = new DataOutputStream(con.getOutputStream());
      }
      catch(Exception ex)
      {
         LOGIT.severe(ex, "No input/output stream can be created");
         return; // Kill this thread.
      }

      try
      {
         // Really unhappy with this bit. Not sure how to refactor Messages.*
         // to fix this however
         while(!finished)
         {
            Messages header = new Messages();

            String headerData = getHeaderData();
            header.Set(headerData);

            String bodyData = getData(header.GetMessageLen() - Messages.messageHeaderLen);

            HandleCommands(headerData + bodyData);
         }
      }
      catch (NetworkDataException ex)
      {
         LOGIT.severe(ex, "Thread ending because of network data read/write issues");
         
         return; // Kill this thread
      }
      catch (NetServerException ex)
      {
         LOGIT.severe(ex, "Error processing request. Connection Closed");
         
         return; // Kill this thread
      }
      catch (MessagesException ex)
      {
         LOGIT.severe(ex, "Unable to parse message from client");
         try
         {
            sendError(15, MESSAGENOTUNDERSTOOD, "");
         }
         catch (NetServerException ignored)
         { /* Let it continue as it is just finishing up the thread */ }
         
         return; // Kill this thread
      }

      try
      {
         out.close();
         in.close();
         con.close();
      }
      catch (IOException ex)
      {
         return;
      }
   }

   /**
    * Deal with requests sent across from the client
    *<p>
    * @throws NetworkDataException, MessagesException
    */
   private void HandleCommands(String queryData) throws NetServerException
   {
      QueryMessage query = new QueryMessage();

      try
      {
         LOGIT.finer("Received request");
         LOGIT.finest(queryData);
         query.Set(queryData);
      }
      catch (MessagesException ex)
      {
         // Some corrupt data
         throw new NetServerException(ex);
      }

      if(query.GetQueryType().equals("LOGIN"))
      {
         HandleLoginCommand(queryData);
      }
      else if(query.GetQueryType().equals("LOGOUT"))
      {
         HandleLogoutCommand(queryData);
      }
      else if(query.GetQueryType().equals("GETDIARY"))
      {
         HandleGetDiaryCommand(queryData);
      }
      else if(query.GetQueryType().equals("RETUPD"))
      {
         HandleRetUpdateCommand(queryData);
      }
   }

   /**
    * Specifically deals with the LOGIN request sent across from the client
    *<p>
    * @throws NetworkDataException, MessagesException
    */
   private void HandleLoginCommand(String queryData) throws NetServerException
   {
      LoginQuery login = new LoginQuery();
      User userData;

      try
      {
         // Authenticate user
         login.Set(queryData);
         String username = login.GetUserName();
         String pwdHash = login.GetPasswordHash();
         userData = dbInstance.CheckUser(username, pwdHash);

         if (userData != null)
         {
            // User successfully logged in, prepare the response
            ArrayList<Diary> diaries = new ArrayList<Diary>();
            ArrayList<DiaryHeader> diaryHeaders = new ArrayList<DiaryHeader>();
            DiaryHeader aDiaryHeader;
            LoginResponse loginResp = new LoginResponse();

            // Set the user information
            loginResp.SetDisplayName(userData.getDisplayName());
            loginResp.SetSessionID(userData.getSessionID());
            loginResp.SetDefaultDiary(userData.getDefaultDiaryID());

            // Get the diary header information from the db
            diaries = dbInstance.GetDiaries(username);
            for(Diary aDiary : diaries)
            {
               aDiaryHeader = new DiaryHeader();
               aDiaryHeader.SetDiaryID(aDiary.getID());
               aDiaryHeader.SetDiaryName(aDiary.getName());
               aDiaryHeader.SetPermissions(aDiary.getPermissions());
               if(aDiary.getOwnerName().compareTo(username) == 0)
               {
                  aDiaryHeader.SetOwnerFlag('Y');
               }
               else
               {
                  aDiaryHeader.SetOwnerFlag('N');
               }
               aDiaryHeader.SetRevNum(aDiary.getRevision());

               diaryHeaders.add(aDiaryHeader);
            }

            // Set the diary headers in the response
            loginResp.SetDiaryHeaders(diaryHeaders);

            // Tada!
            LOGIT.info("Login successful, sending response");
            sendData(loginResp.Get(""));
         }
         else
         {
            LOGIT.warning("Unsuccessful login attempt for user: ".concat(username));
            sendError(1, "Incorrect username or password", login.GetSessionID());
         }
      }
      catch (MessagesException ex)
      {
         // Some data appears to be corrupt.
         LOGIT.severe(ex, "Messaging error!");
         sendError(15, "Server error when attempting to log in", login.GetSessionID());
      }
      catch (NetworkDataException ex)
      {
         // Some sort of network error. Force this thread to die
         LOGIT.severe(ex, "Network error!");
         throw new NetServerException(ex);
      }
      catch (DBServerException ex)
      {
         LOGIT.severe(ex, "Error when checking user");
         sendError(15, "Server error when attempting to log in", login.GetSessionID());
      }
   }

   /**
    * Specifically deals with the LOGOUT request sent across from the client
    */
   public void HandleLogoutCommand(String queryData) throws NetServerException
   {
      LogoutQuery logout = new LogoutQuery();

      try
      {
         logout.Set(queryData);
         String sessionID = logout.GetSessionID();
         dbInstance.Logout(sessionID);
      }
      catch(MessagesException ex)
      { /* We don't care about this really*/ }
      catch(DBServerException ex)
      { /* We don't care about this really*/ }

      // Try to send a successful response anyway, despite any errors
      try
      {
         LogoutResponse lR = new LogoutResponse();
         lR.SetErrorCode(0);
         sendData(lR.Get(""));
      }
      catch (NetworkDataException ex)
      {
         // Force the thread to die
         LOGIT.severe(ex, "Network issues while attempting to send logout response");
         throw new NetServerException(ex);
      }
      catch (MessagesException ex)
      {
         // Force the thread to die
         LOGIT.severe(ex, "Errors with logout message, response not sent");
         throw new NetServerException(ex);
      }
   }

   /**
    * Specifically deals with the GETDIARY request sent across from the client
    */
   private void HandleGetDiaryCommand(String queryData) throws NetServerException
   {
      GetDiaryQuery diary = new GetDiaryQuery();
      Diary requestedDiary;
      ArrayList<DiaryEntry> diaryEntries = new ArrayList<DiaryEntry>();
      DiaryEntry aDiaryEntry;

      try
      {
         diary.Set(queryData);

         // Check if user is actually logged in
         String username = dbInstance.GetSession(diary.GetSessionID());
         if(username == null)
         {
            sendError(2, "Sorry, you are not currently logged in.", diary.GetSessionID());
            return;
         }

         // Attempt to retrieve the requested diary
         int diaryID = diary.GetDiaryID();
         requestedDiary = dbInstance.GetDiary(diaryID);
         if(requestedDiary == null)
         {
            sendError(2, "Sorry, your current diary was not found", diary.GetSessionID());
            return;
         }

         // Process the diary/entries so it can be sent back to the client
         GetDiaryResponse diaryResponse = new GetDiaryResponse();
         diaryResponse.SetErrorCode(0);
         diaryResponse.setDiaryName(requestedDiary.getName());
         diaryResponse.setPermissions(requestedDiary.getPermissions());
         diaryResponse.setRevisionNo(requestedDiary.getRevision());
         if(username.compareTo(requestedDiary.getOwnerName()) == 0)
         {
            diaryResponse.setOwnerFlag('Y');
         }
         else
         {
            diaryResponse.setOwnerFlag('N');
         }

         ArrayList<Entry> entries = requestedDiary.getEntries();
         if(entries != null)
         {
            for(Entry nextEntry : entries)
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
               aDiaryEntry.setTitle(nextEntry.getSubject());
               aDiaryEntry.setEntryID(nextEntry.getID());
               if(nextEntry.getOwner().compareTo(username) == 0)
               {
                  aDiaryEntry.setOwnerFlag('Y');
               }
               else
               {
                  aDiaryEntry.setOwnerFlag('N');
               }

               aDiaryEntry.setStart(nextEntry.getStart().getTimeInMillis());
               aDiaryEntry.setEnd(nextEntry.getEnd().getTimeInMillis());

               diaryEntries.add(aDiaryEntry);
            }
         }

         diaryResponse.setEntries(diaryEntries);

         LOGIT.finer("Get Diary successful, sending response");
         sendData(diaryResponse.Get(""));
      }
      catch (MessagesException ex)
      {
         // Some data appears to be corrupt.
         LOGIT.severe(ex, "Messaging error!");
         sendError(15, "Server error when attempting to get diary", diary.GetSessionID());
      }
      catch (DBServerException ex)
      {
         // Unexpected errors when using the database
         LOGIT.severe(ex, "Database error!");
         sendError(15, "Server error when attempting to get diary", diary.GetSessionID());
      }
      catch (NetworkDataException ex)
      {
         // Some sort of network error. Force this thread to die
         LOGIT.severe(ex, "Network error!");
         throw new NetServerException(ex);
      }
   }

   /**
    * Specifically deals with the RETUPD request sent across from the client
    */
   private void HandleRetUpdateCommand(String queryData) throws NetServerException
   {
      RetUpdQuery update = new RetUpdQuery();
      Diary updatedDiary;

      try
      {
         LOGIT.info(queryData);
         update.Set(queryData);

         // Check if user is actually logged in
         String username = dbInstance.GetSession(update.GetSessionID());
         if(username == null)
         {
            sendError(2, "Sorry, you are not currently logged in.", update.GetSessionID());
            return;
         }

         // Attempt to retrieve the diary to be updated
         int diaryID = update.GetDiaryID();
         updatedDiary = dbInstance.GetDiary(diaryID);
         if(updatedDiary == null)
         {
            sendError(2, "Sorry, the diary no longer exists.", update.GetSessionID());
            return;
         }
         // TODO: Need to check  diary permissions here
         // Maybe update diary display name? permissions?

         // Process the entries and update the server
         Entry anEntry = new Entry();
         Calendar cal;
         for(DiaryEntry nextDiaryEntry : update.getEntries())
         {
            anEntry.setID(nextDiaryEntry.getEntryID());
            anEntry.setSubject(nextDiaryEntry.getTitle());
            anEntry.setBody(nextDiaryEntry.getBody());
            anEntry.setOwner(nextDiaryEntry.getCreatingUser());
            anEntry.setLocked(nextDiaryEntry.getOwnerFlag() == 'Y');
            anEntry.setDeleted(nextDiaryEntry.getDeletedFlag() == 'Y');

            cal = Calendar.getInstance();
            cal.setTimeInMillis(nextDiaryEntry.getStart());
            anEntry.setStart(cal);
            cal = Calendar.getInstance();
            cal.setTimeInMillis(nextDiaryEntry.getEnd());
            anEntry.setEnd(cal);

            dbInstance.UpdateEntry(anEntry, update.GetDiaryID());
         }
 
         LOGIT.finer("Update Diary successful, sending response");
         sendError(1,"Response to Update not implemented", update.GetSessionID());
      }
      catch (MessagesException ex)
      {
         // Some data appears to be corrupt.
         LOGIT.severe(ex, "Messaging error!");
         sendError(15, "Server error when attempting to get diary", update.GetSessionID());
      }
      catch (DBServerException ex)
      {
         // Unexpected errors when using the database
         LOGIT.severe(ex, "Database error!");
         sendError(15, "Server error when attempting to get diary", update.GetSessionID());
      }
      /*catch (NetworkDataException ex)
      {
         // Some sort of network error. Force this thread to die
         LOGIT.severe("Network error!");
         LOGIT.info(ex.getLocalizedMessage());
         throw new NetServerException(ex);
      } //*/
   } 

   /**
    * Send data to the client
    *<p>
    * @param s The data to send to the client
    *<p>
    * @throws NetworkDataException
    */
   public void sendData(String s) throws NetworkDataException
   {
      LOGIT.finest(s);
      try
      {
         out.writeBytes(s);
      }
      catch(IOException ex)
      {
         LOGIT.severe(ex, "Unable to send data to client");
         throw new NetworkDataException(ex);
      }
   }

/**
    * Receive the header data from the client
    *<p>
    * @return The data received from the client
    *<p>
    * @throws NetworkDataException
    */
   public String getHeaderData() throws NetworkDataException
   {
      byte[] s=new byte[Messages.messageHeaderLen];

      try
      {
         // Confirming size of message is correct is
         // handled later by the Messages.* classes themselves
         in.readFully(s, 0, Messages.messageHeaderLen);
      }
      catch(Exception ex)
      {
         LOGIT.severe(ex, "Unable to get data from client");
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
   public String getData(int size) throws NetworkDataException
   {
      byte[] s=new byte[size];
      try
      {
         // Confirming size of messages is correct is
         // handled later by the Messages.* classes themselves
         in.readFully(s, 0, size);
      }
      catch(Exception ex)
      {
         LOGIT.severe(ex, "Unable to get data from client");
         throw new NetworkDataException(ex);
      }

      return new String(s, 0, size);
   }

   /**
    * Prepare the thread to finish
    */
   public void setFinished(boolean finished)
   {
      this.finished = finished;

      if (Thread.currentThread() != null)
      {
         Thread.currentThread().interrupt();
      }
   }

   private void sendError(int errNum, String errMsg, String sessionID) throws NetServerException
   {
      LOGIT.finest(errMsg);
      if (errNum != 0)
      {
         try
         {
            ResponseMessage error = new ResponseMessage();
            error.SetErrorCode(errNum);
            error.SetErrorMessage(errMsg);
            error.SetSessionID(sessionID);
            sendData(error.Get(""));
         }
         catch (MessagesException ex)
         {
            LOGIT.severe(ex, "Unable to send error response; Internal server error");
            throw new NetServerException(ex);
         }
         catch (NetworkDataException ex)
         {
            LOGIT.severe(ex, "Unable to send error response; Network error");
            throw new NetServerException(ex);
         }
      }
   }
}