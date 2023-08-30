package TBA.Server;

import TBA.Data.Diary;
import TBA.Data.Entry;
import TBA.Exceptions.DBServerException;
import TBA.Exceptions.PasswordHashingException;
import java.io.IOException;
import java.sql.*;
import TBA.Security.*;
import TBA.Data.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.UUID;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class handles the connection to SQLite
 *<p>
 * If you wish to talk to SQLite, use this class.
 * It is only thread-safe if you instantiation it as a static class
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 273           $ $Date:: 2011-10-25 #$
 *
 * @see dbserver.SQLite
 */
class DBServer extends SQLite
{
   private PreparedStatement prepCheckUser;
   private PreparedStatement prepSaltPassword;
   private PreparedStatement prepSaveSession;
   private PreparedStatement prepNewSession;
   private PreparedStatement prepGetSession;
   private PreparedStatement prepCheckSession;
   private PreparedStatement prepRemoveSession;
   private PreparedStatement prepGetUserDiaries;
   private PreparedStatement prepGetDiary;
   private PreparedStatement prepGetEntries;
   private PreparedStatement prepUpdateEntry;
   private PreparedStatement prepNewEntry;
   private int nextEntryID;
   private int nextDiaryID;
   private final static Object lock = new Object();
   private PreparedStatement prepCheckUserExiest;
   private PreparedStatement prepAddUsr;
   private PreparedStatement prepAddSalt;
   private PreparedStatement prepAddDiary;
   private PreparedStatement prepAddDiaries;
   private PreparedStatement prepAddEntries;
   private PreparedStatement getUsrDiaryID;
   private PreparedStatement rmvAllEntries;
   private PreparedStatement rmvAllDiaries;
   private PreparedStatement rmvAlldiary;
   private PreparedStatement rmvSalt;
   private PreparedStatement rmvUser;
   private PreparedStatement rmvpermissions;
   private PreparedStatement chkpermissions;
   private PreparedStatement rmvDiary;
   private PreparedStatement chkDairyID;
   private PreparedStatement rmvEntries;
   private PreparedStatement tmvDiaries;
   private PreparedStatement smartlistone;
   private PreparedStatement smartlisttwo;
   private TBA.Security.PasswordHashing secure;
   private TBA.Security.SaltGen securesalt;

   // Block the default constructor
   private DBServer()
   {
   }

   /**
    * This is the constructor for DBServer.
    *<p>
    * @throws DBServerException
    *<p>
    * @param user The username to log into the database as
    * @param pass The password to log into the database with
    */
   DBServer(String user, String pass) throws DBServerException
   {
      super(user, pass);
   }

   /**
    * This sets up all the prepared statements that will be possibly used by
    * DBServer
    *<p>
    * @throws DBServerException
    *<p>
    * @see java.sql.PreparedStatement
    */
   public void setup() throws DBServerException, IOException, PasswordHashingException
   {
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

      ResultSet res = executeQuery("select * from SQLite_Master;");

      try
      {
         if (!res.next())
         {
            RealDatabase();
            LOGIT.info("Database Created Successfully.");
         }
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Error when checking for databases existance");
      }

      LOGIT.fine("Setting up prepared statements");

      try
      {
         String sql = null;

         sql = "select DISPLAYNAME, DEFAULTDIARYID from users where USER = ? and PWDHASH = ?;";
         prepCheckUser = connex.prepareStatement(sql);
         LOGIT.finest("Setup user PreparedStatement");

         sql = "select SALT from passwordSalts where USER = ?;";
         prepSaltPassword = connex.prepareStatement(sql);
         LOGIT.finest("Setup password PreparedStatement");

         sql = "select SESSIONID from userSessions where USER = ?;";
         prepGetSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup get session PreparedStatement");

         sql = "select USER from userSessions where SESSIONID = ?;";
         prepCheckSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup check session PreparedStatement");

         sql = "insert INTO userSessions(SESSIONID, USER) values (?, ?);";
         prepNewSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup new session PreparedStatement");

         sql = "update userSessions SET SESSIONID=? WHERE USER=?;";
         prepSaveSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup save session PreparedStatement");

         sql = "update userSessions SET SESSIONID='' WHERE SESSIONID=?;";
         prepRemoveSession = connex.prepareStatement(sql);
         LOGIT.finest("Setup remove session PreparedStatement");

         sql = "select DIARYID, PERMISSIONS from diaries where USER = ?;";
         prepGetUserDiaries = connex.prepareStatement(sql);
         LOGIT.finest("Setup get user's diaries PreparedStatement");

         sql = "select NAME, OWNER, REVISION from diary where ID = ?;";
         prepGetDiary = connex.prepareStatement(sql);
         LOGIT.finest("Setup get diary PreparedStatement");

         sql = "select ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY from entries where DIARYID = ? and DELETED=0;";
         prepGetEntries = connex.prepareStatement(sql);
         LOGIT.finest("Setup get entries PreparedStatement");

         sql = "update entries SET START=?, END=?, USER=?, LOCKED=?, TITLE=?, BODY=?, DELETED=? WHERE ID=?;";
         prepUpdateEntry = connex.prepareStatement(sql);
         LOGIT.finest("Setup update entry PreparedStatement");

         sql = "insert INTO entries(START, END, USER, LOCKED, TITLE, BODY, DELETED, ID, DIARYID) values(?,?,?,?,?,?,?,?,?);";
         prepNewEntry = connex.prepareStatement(sql);
         LOGIT.finest("Setup update entry PreparedStatement");

         sql = "select ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY from entries where DIARYID = ? and DELETED=0;";
         prepGetEntries = connex.prepareStatement(sql);
         LOGIT.finest("Setup get entries PreparedStatement");

         sql = "SELECT COUNT(*) FROM USERS WHERE USER = ? ;";
         prepCheckUserExiest = connex.prepareStatement(sql);
         LOGIT.finest("Checks if a username already exists");

         sql = "INSERT INTO USERS(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH) VALUES (?,?,?,?);";
         prepAddUsr = connex.prepareStatement(sql);
         LOGIT.finest("User is being added.");

         sql = "INSERT INTO PASSWORDSALTS(USER, SALT) VALUES (?,?);";
         prepAddSalt = connex.prepareStatement(sql);
         LOGIT.finest("Salt added.");

         sql = "INSERT INTO DIARY(ID,NAME, OWNER, REVISION) VALUES (?,?,?,?);";
         prepAddDiary = connex.prepareStatement(sql);
         LOGIT.finest("Add Diary");

         sql = "INSERT INTO DIARIES(USER, DIARYID, PERMISSIONS) VALUES (?,?,?);";
         prepAddDiaries = connex.prepareStatement(sql);
         LOGIT.finest("Adds Premission");

         sql = "INSERT INTO ENTRIES(ID,DIARYID, START, END, USER,LOCKED, TITLE, BODY, DELETED) VALUES (?,?,?,?,?,?,?,?,?);";
         prepAddEntries = connex.prepareStatement(sql);
         LOGIT.finest("Adds creation date to the diary");

         sql = " SELECT ID FROM DIARY WHERE NAME = ? AND OWNER = ?;";
         getUsrDiaryID = connex.prepareStatement(sql);
         LOGIT.finest("Gets DiaryID");

         sql = " DELETE FROM ENTRIES WHERE USER=?;";
         rmvAllEntries = connex.prepareStatement(sql);
         LOGIT.finest("Setup removing all entries for a user.");

         sql = " DELETE FROM DIARIES WHERE USER = ?;";
         rmvAllDiaries = connex.prepareStatement(sql);
         LOGIT.finest("Setup removing all diary permissions for a user.");

         sql = " DELETE FROM DIARY WHERE OWNER = ?;";
         rmvAlldiary = connex.prepareStatement(sql);
         LOGIT.finest("Setup removing all diaries from a user.");


         sql = " DELETE FROM PASSWORDSALTS WHERE USER = ?;";
         rmvSalt = connex.prepareStatement(sql);
         LOGIT.finest("Setup removing salt for a user.");

         sql = " DELETE FROM USERS WHERE USER = ?;";
         rmvUser = connex.prepareStatement(sql);
         LOGIT.finest("Setup removing a user.");

         sql = " DELETE FROM DIARIES WHERE USER = ? AND DIARYID =?";
         rmvpermissions = connex.prepareStatement(sql);
         LOGIT.finest("Setup removing a user permissions.");

         sql = " SELECT DIARY.ID FROM DIARY JOIN DIARIES ON DIARY.ID = DIARIES.DIARYID WHERE DIARIES.USER = ? AND DIARY.NAME = ?; ";
         chkpermissions = connex.prepareStatement(sql);
         LOGIT.finest("Setup checking dairyid of ");

         sql = " SELECT DIARY.ID, DIARY.NAME FROM DIARY JOIN DIARIES ON DIARY.ID = DIARIES.DIARYID WHERE DIARIES.USER = ?; ";
         smartlistone = connex.prepareStatement(sql);
         LOGIT.finest("Setup checking dairyid of ");

         sql = " SELECT ID, NAME FROM DIARY WHERE OWNER = ?; ";
         smartlisttwo = connex.prepareStatement(sql);
         LOGIT.finest("Setup checking dairyid of ");


         sql = " SELECT ID FROM DIARY WHERE NAME = ? AND OWNER = ?;";
         chkDairyID = connex.prepareStatement(sql);
         LOGIT.finest("Setup checking dairyid for Diary ID ");

         sql = " SELECT DIARY.ID FROM DIARY JOIN DIARIES ON DIARY.ID = DIARIES.DIARYID WHERE DIARIES.USER = ? AND DIARY.NAME = ?; ";
         rmvDiary = connex.prepareStatement(sql);
         LOGIT.finest("Setup removes Diaries");


         sql = " DELETE FROM ENTRIES WHERE DIARYID = ?;";
         rmvEntries = connex.prepareStatement(sql);
         LOGIT.finest("Setup removes Entries");

         sql = " DELETE FROM DIARIES WHERE DIARYID = ? ;";
         tmvDiaries = connex.prepareStatement(sql);
         LOGIT.finest("Setup removes Diaries permissions");

         secure = new TBA.Security.PasswordHashing();
         securesalt = new TBA.Security.SaltGen();
         res = executeQuery("select Max(ID) from entries");

         if (res.next())
         {
            nextEntryID = res.getInt(1) + 1;
         }
         else
         {
            nextEntryID = 1;
         }

         res = executeQuery("select Max(DIARYID) from diaries");
         if (res.next())
         {
            nextDiaryID = res.getInt(1) + 1;
         }
         else
         {
            nextDiaryID = 1;
         }
      }
      catch (Exception ex)
      {
         LOGIT.severe(ex, "Unable to create an SQL prepared statement");
         throw new DBServerException(ex);
      }

   }

   /**
    * This checks a user's credentials and returns a User object which contains
    * the details of the said user.
    *<p>
    * @param username The user name
    * @param pwdhash The password hash sent from the client
    *<p>
    * @throws DBServerException
    *<p>
    * @return User object if the credentials match, null if they don't
    */
   public User CheckUser(String username, String pwdhash) throws DBServerException
   {
      User userData = new User();

      try
      {
         ResultSet res;
         String salt;

         // Get details needed for password hash matching
         prepSaltPassword.setString(1, username);
         res = executeQuery(prepSaltPassword);
         if (res.next())
         {
            salt = res.getString("SALT");
         }
         else
         {
            return null;
         }

         // Convert password hash from user to password hash stored on server.
         PasswordHashing pHash = new PasswordHashing();
         pwdhash = pHash.getServerHash(pwdhash, salt);

         // Retrieve the user, if the password hash matches
         prepCheckUser.setString(1, username);
         prepCheckUser.setString(2, pwdhash);
         res = executeQuery(prepCheckUser);

         if (res.next())
         {
            userData.setDisplayName(res.getString("DISPLAYNAME"));
            userData.setDefaultDiaryID(res.getInt("DEFAULTDIARYID"));
         }
         else
         {
            return null;
         }

         // Update or setup a session for the user
         PreparedStatement giveSession;
         prepGetSession.setString(1, username);
         res = executeQuery(prepGetSession);

         if (res.next())
         {
            giveSession = prepSaveSession;
         }
         else
         {
            giveSession = prepNewSession;
         }

         String sessionID = UUID.randomUUID().toString();

         giveSession.setString(1, sessionID);
         giveSession.setString(2, username);
         if (executeUpdate(giveSession) == -1)
         {
            throw new SQLException("Give the user a session failed!");
         }

         userData.setSessionID(sessionID);

         return userData;
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to set values in an SQL prepared statement");
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (NullPointerException ex)
      {
         LOGIT.severe("DB Instance not active...");
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (PasswordHashingException ex)
      {
         LOGIT.severe("Password hashing services unavailable");
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * This gets all the diary information for a particular user, excluding the
    * actual diary entries.
    *<p>
    * @param username The user name
    *<p>
    * @throws DBServerException
    *<p>
    * @return A Vector of Diaries, with just there headers
    */
   public ArrayList<Diary> GetDiaries(String username) throws DBServerException
   {
      ArrayList<Diary> diaries = new ArrayList<Diary>();
      Diary diaryData;

      try
      {
         ResultSet res;
         int diaryID = -1;

         // Get all the diary ID's for this user
         prepGetUserDiaries.setString(1, username);
         res = executeQuery(prepGetUserDiaries);
         while (res.next())
         {
            diaryID = res.getInt("DIARYID");

            diaryData = new Diary();
            diaryData.setID(diaryID);
            diaryData.setPermissions(res.getInt("PERMISSIONS"));

            diaries.add(diaryData);
         }
         if (diaryID == -1)
         {
            return null;
         }

         // Get the details (excluding entries) of all the diaries.
         for (Diary aDiary : diaries)
         {
            diaryID = aDiary.getID();

            prepGetDiary.setInt(1, diaryID);
            res = executeQuery(prepGetDiary);
            if (res.next())
            {
               // why?
               aDiary.setName(res.getString("NAME"));
               aDiary.setOwnerName(res.getString("OWNER"));
               aDiary.setRevision(1);
            }
         }

         return diaries;
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to set values in an SQL prepared statement");
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (NullPointerException ex)
      {
         LOGIT.severe("DB Instance not active...");
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * This gets all the entries for a particular diary, excluding the
    * deleted entries. It passes back the Diary, including it's header
    * info.
    *<p>
    * @param ID The Diary ID to retrieve the entries for.
    *<p>
    * @throws DBServerException
    *<p>
    * @return A Vector of Entries
    */
   public Diary GetDiary(int ID) throws DBServerException
   {
      ArrayList<Entry> entries = new ArrayList<Entry>();
      Diary diary = new Diary();
      Entry aEntry;

      try
      {
         prepGetDiary.setInt(1, ID);
         ResultSet res;
         res = executeQuery(prepGetDiary);
         if (res.next())
         {
            diary.setName(res.getString("NAME"));
            diary.setOwnerName(res.getString("OWNER"));
            diary.setRevision(1);
            //diary.setPermissions(res.getInt("PERMISSIONS"));
            }

         Calendar timedate = Calendar.getInstance();
         prepGetEntries.setInt(1, ID);
         res = executeQuery(prepGetEntries);
         while (res.next())
         {
            aEntry = new Entry();
            aEntry.setID(res.getInt("ID"));
            timedate.setTimeInMillis(res.getLong("START"));
            aEntry.setStart((Calendar) timedate.clone());
            timedate.setTimeInMillis(res.getLong("END"));
            aEntry.setEnd((Calendar) timedate.clone());
            aEntry.setSubject(res.getString("TITLE"));
            aEntry.setBody(res.getString("BODY"));
            aEntry.setLocked(res.getBoolean("LOCKED"));
            aEntry.setOwner(res.getString("USER"));

            entries.add(aEntry);
         }

         diary.setEntries(entries);
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "SQL Error when attempting to get a diary");
      }

      return diary;
   }

   /**
   /**
    * This returns the next available Diary ID
    *<p>
    * @throws DBServerException
    *<p>
    * @return The next available Diary ID
    */
   public int GetNextDiaryID()
   {
      int nextID;

      synchronized (lock)
      {
         nextID = nextDiaryID;
         nextDiaryID += 1;
      }

      return nextID;
   }

   /**
    * This returns the next available Entry ID
    *<p>
    * @throws DBServerException
    *<p>
    * @return The next available Diary ID
    */
   public int GetNextEntryID()
   {
      int nextID;

      synchronized (lock)
      {
         nextID = nextEntryID;
         nextEntryID += 1;
      }

      return nextID;
   }

   /**
    * This logs out a session
    *<p>
    * @param sessionID The sessionID to sign out
    *<p>
    * @throws DBServerException
    *<p>
    */
   public void Logout(String sessionID) throws DBServerException
   {
      try
      {
         prepGetUserDiaries.setString(1, sessionID);
         executeUpdate(prepGetUserDiaries);
      }
      catch (SQLException ex)
      {
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * This retrieves a session
    *<p>
    * @param sessionID The sessionID to retrieve
    *<p>
    * @throws DBServerException
    */
   public String GetSession(String sessionID) throws DBServerException
   {
      try
      {
         prepCheckSession.setString(1, sessionID);
         ResultSet res = executeQuery(prepCheckSession);
         if (res.next())
         {
            return res.getString("USER");
         }
      }
      catch (SQLException ex)
      {
         throw new DBServerException(ex.getLocalizedMessage());
      }

      return null;
   }

   /**
    * This updates an entry in the database. It does not update the DIARYID
    * field as this is not a valid client action.
    *<p>
    * @param updatedEntry The updated entry to write to the DB.
    */
   public void UpdateEntry(Entry updatedEntry, int diaryID) throws DBServerException
   {
      try
      {
         PreparedStatement prepSaveEntry;
         if (updatedEntry.getID() == 0)
         {
            prepSaveEntry = prepNewEntry;
            updatedEntry.setID(GetNextEntryID());
            LOGIT.finer("New entry: " + Integer.toString(updatedEntry.getID()));
            prepSaveEntry.setInt(9, diaryID);
         }
         else
         {
            prepSaveEntry = prepUpdateEntry;
         }
         prepSaveEntry.setLong(1, updatedEntry.getStart().getTimeInMillis());
         prepSaveEntry.setLong(2, updatedEntry.getEnd().getTimeInMillis());
         prepSaveEntry.setString(3, updatedEntry.getOwner());
         prepSaveEntry.setString(4, "N"); // TODO: Hard-coded locked to NO until it is developed
         prepSaveEntry.setString(5, updatedEntry.getSubject());
         prepSaveEntry.setString(6, updatedEntry.getBody());
         if (updatedEntry.isDeleted())
         {
            prepSaveEntry.setInt(7, 1);
         }
         else
         {
            prepSaveEntry.setInt(7, 0);
         }
         prepSaveEntry.setInt(8, updatedEntry.getID());
         int res = executeUpdate(prepSaveEntry);
      }
      catch (SQLException ex)
      {
         throw new DBServerException(ex.getLocalizedMessage());
      }

   }

   private void tmpDummyDatabase()
   {
      ///* Dummy tables + data!
      executeUpdate("drop table users");
      executeUpdate("drop table passwordSalts");
      executeUpdate("drop table userSessions");
      executeUpdate("drop table diaries");
      executeUpdate("drop table diary");
      executeUpdate("drop table entries");


      executeUpdate("Create table users(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH);");
      executeUpdate("Create table passwordSalts(USER, SALT);");
      executeUpdate("Create table userSessions(USER, SESSIONID);");
      executeUpdate("Create table diaries(ID, USER, DIARYID, PERMISSIONS);");
      executeUpdate("Create table diary(ID, NAME, OWNER, REVISION);");
      executeUpdate("Create table entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED);");
      executeUpdate("insert INTO users(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH) VALUES('dmcgra', 'Dan McG-Unit', 0, 'dd54024027f265410d2bc7a43e9eeff7ff1a4378c86e4f07d24b199926a5bbad');");
      executeUpdate("insert INTO passwordSalts(USER, SALT) values('dmcgra', 'pepper')");
      executeUpdate("insert INTO diary(ID, NAME, OWNER, REVISION) values(0, 'Dan Diary','dmcgra', 15)");
      executeUpdate("insert INTO diary(ID, NAME, OWNER, REVISION) values(1, 'Group Diary','dmcgra', 35)");
      executeUpdate("insert INTO diaries(ID, USER, DIARYID, PERMISSIONS) values(0, 'dmcgra',0, 511)");
      executeUpdate("insert INTO diaries(ID, USER, DIARYID, PERMISSIONS) values(1, 'dmcgra',1, 511)");
      executeUpdate("insert INTO users(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH) VALUES('kse', 'Kyle Seton', 0, ' dedf49781653050d6b89f7484d4c1d5b18f896b3871d2f2b0dcb0d9a3980dd7');");
      executeUpdate("insert INTO passwordSalts(USER, SALT) values('kse', 'pepper')");
      executeUpdate("insert INTO diary(ID, NAME, OWNER, REVISION) values(2, 'Kyle Diary','kse', 15)");
      executeUpdate("insert INTO diaries(ID, USER, DIARYID, PERMISSIONS) values(3, 'kse',2, 511)");
      executeUpdate("insert INTO diaries(ID, USER, DIARYID, PERMISSIONS) values(4, 'kse',1, 511)");
      Calendar now = Calendar.getInstance();
      Calendar soon = Calendar.getInstance();
      soon.add(Calendar.MINUTE, 2);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(1, 0, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'dmcgra', 0, " + "'Meeting', 'I am having a meeting about project TX-02', 0)");
      now.add(Calendar.MINUTE, 5);
      soon.add(Calendar.MINUTE, 5);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(2, 0, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'john', 0, " + "'Group Meeting', 'We are having a meeting about project TX-02', 0)");
      now.add(Calendar.MINUTE, 5);
      soon.add(Calendar.MINUTE, 30);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(3, 0, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'dmcgra', 0, " + "'Group Meeting(2)', 'We are having a meeting about project TX-01', 0)");
      now.add(Calendar.MINUTE, 30);
      soon.add(Calendar.MINUTE, 30);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(4, 1, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'dmcgra', 0, " + "'Group Meeting(3)', 'We are having a meeting about project Fail', 0)");
      now.add(Calendar.MINUTE, 30);
      soon.add(Calendar.MINUTE, 30);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(5, 0, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'dmcgra', 0, " + "'Group Meeting(4)', 'We are having a meeting about project TX-OneMillion', 0)");
      now.add(Calendar.DAY_OF_YEAR, -2);
      soon.add(Calendar.DAY_OF_YEAR, -2);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(6, 1, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'dmcgra', 0, " + "'Group Meating', 'We are having a meeting about project Win', 0)");
      now.add(Calendar.DAY_OF_YEAR, 4);
      soon.add(Calendar.DAY_OF_YEAR, 4);
      executeUpdate("insert INTO entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED) " + "values(7, 0, " + Long.toString(now.getTimeInMillis()) + ", " + Long.toString(soon.getTimeInMillis()) + ", 'dmcgra', 0, " + "'Group Vegetabling', 'We are having a meeting about project TX-OneBillion', 0)");
   }

   /**
    * This is the constructor for DBServer.
    *
    */
   public void RealDatabase()
   {
      // Creates All the tables
      CreateTables();
      // Creates The Triggers to Enforce Foreign Keys
//        CreateTriggers();
   }

   //generates the list of diaries for the task need
   //returns the diary
   /**
    * This generates as list of both permissions and diary owned by the inputted user
    * @param usr  = the owners of the diary
    * @param type = type is the type of search: 1 = permisions and 2 = diaries
    * @return returns the dairy id of the item wanted
    */
   public int SmartList(String usr, int type)
   {
      //type 1 = permisions
      //type 2 = diaries
      String table, display;
      try
      {
         ResultSet rset = null;
         if (type == 1)
         {
            table = "Diary";
            display = "dairy";
            smartlistone.setString(1, usr);
            rset = smartlistone.executeQuery();
         }
         else if (type == 2)
         {
            table = "Diaries";
            display = "premission";
            smartlisttwo.setString(1, usr);
            rset = smartlisttwo.executeQuery();
         }
         else
         {
            return -1;
         }

         int list[] = null;
         int i = 0;
         System.out.println("Select a " + display + " owned by " + usr + " :");
         // If A Result Is Returned
         while (rset.next())
         {
            list[i] = rset.getInt(1);
            i++;
         }

         System.out.println("[-1] : None of the above (Cancel transaction)");
         System.out.println("$");
         BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
         int result;
         result = stdin.read();


         if (0 >= result || result > i)
         {
            result = -1;
         }
         else
         {
            result = list[result];
         }

         return result;
      }
      catch (SQLException ex)
      {
         Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (IOException ex)
      {
         Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      return -1;
   }

   /**
    * This Allows A New User To Be Added In The Server Command Line
    * @param usr = username to be inputted into the system
    * @param pws = password that the user is to use
    * @param DisplayName = name that the users wishes to have displayed
    * @param DiaryName = name of the diary the user will owen
    * @return A string letting the user know if the operation was successful or unsuccessful
    * @throws DBServerException
    */
   public String Addusr(String usr, String pws, String DisplayName, String DiaryName) throws DBServerException
   {

      //Convert Usrname To Lowercase
      usr = usr.toLowerCase();
      //check information is passed in
      if (usr == null || usr.length() == 0)
      {
         return "No username entered.";
      }

      // Checks Password Length
      if (pws == null || pws.length() == 0)
      {
         return "No password entered.";
      }

      //Check DisplayName is Present
      if (DisplayName == null || DisplayName.length() == 0)
      {
         return "No display name entered.";
      }

      //Checks DiaryName Is Present
      if (DiaryName == null || DiaryName.length() == 0)
      {
         return "No diary name entered.";
      }

      // Check Username Only Has Characters And Numbers
      // Adds An Extra Level Of Security
      for (int i = 0; i < usr.length(); i++)
      {
         if (!Character.isLetterOrDigit(usr.charAt(i)))
         {
            return "Character \"" + usr.charAt(i) + "\" is a violation to naming rules";
         }
      }

      try
      {

         // Check Username Doesn't Already Exiest
         prepCheckUserExiest.setString(1, usr);
         ResultSet rset = prepCheckUserExiest.executeQuery();
         // If A Result Is Returned Then Username Already Exiest
         if (rset.next())
         {
            if (rset.getInt(1) != 0)
            {
               return "Another user has the user name " + usr;
            }
         }

         // Generate A Random Salt
         String salt = securesalt.SaltGen(pws.length() / 2);

         // Gets Hash From Salt and Password
         String hash = secure.getClientHash(pws);
         hash = secure.getServerHash(hash, salt);

         //Creates Calendar For Entry
         Calendar thisday = Calendar.getInstance();

         Integer DairyId = GetNextDiaryID();
         //Add Information To The USERS Table
         prepAddUsr.setString(1, usr);
         prepAddUsr.setString(2, DisplayName);
         prepAddUsr.setInt(3, DairyId);
         prepAddUsr.setString(4, hash);
         prepAddUsr.executeUpdate();

         //Add AutoGenerated Salt and User to PASSWORDSALTS Table
         prepAddSalt.setString(1, usr);
         prepAddSalt.setString(2, salt);
         prepAddSalt.executeUpdate();

         //Creates String TimeStamp
         addDiary(DairyId, DiaryName, usr, "1");

         //Add permissions For The User To DIARIES
         addDiaries(usr, DairyId, "7");

         //Add An Entry To ENTRIES To Give A Second Log For The System
         prepAddEntries.setInt(1, GetNextEntryID());
         prepAddEntries.setInt(2, DairyId);
         prepAddEntries.setString(3, Long.toString(thisday.getTimeInMillis()));
         prepAddEntries.setString(4, Long.toString(thisday.getTimeInMillis()));
         prepAddEntries.setString(5, usr);
         prepAddEntries.setString(6, "N");
         prepAddEntries.setString(7, "Diary Created");
         prepAddEntries.setString(8, "Diary Created");
         prepAddEntries.setString(9, "0");
         prepAddEntries.executeUpdate();

         return "Succesfully Added.";
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to set values in an SQL prepared statement");
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (NullPointerException ex)
      {
         LOGIT.severe("DB Instance not active...");
         throw new DBServerException(ex.getLocalizedMessage());
      }
      catch (PasswordHashingException ex)
      {
         LOGIT.severe("Password hashing services unavailable");
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    * Adds A Dairy For A User
    * @param DiaryId = id of the diary to be entered
    * @param Diaryname = name of the diary to be entered
    * @param owner = user that will own the diary
    * @param time = time for the revision number
    * @return message for the users successfull or unsuccessfull
    * @throws DBServerException
    */
   private String addDiary(Integer DiaryId, String Diaryname, String owner, String time) throws DBServerException
   {
      try
      {
         //Adds A New Diary
         prepAddDiary.setInt(1, DiaryId);
         prepAddDiary.setString(2, Diaryname);
         prepAddDiary.setString(3, owner);
         prepAddDiary.setString(4, time);
         prepAddDiary.executeUpdate();

         return "Successfully added Diary" + DiaryId;
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to set values in an SQL prepared statement");
         throw new DBServerException(ex.getLocalizedMessage());
      }

   }

   /**
    * This Is Used To Add permissions In The Server Command Line
    * @param usr = user that has the permission
    * @param diaryid = diary id of the dairy getting permissions
    * @param permissions = the permissions the user is getting
    * @return string for users acknolegdement of success or failure
    * @throws DBServerException
    */
   private String addDiaries(String usr, Integer diaryid, String permissions) throws DBServerException
   {

      try
      {
         //Adds permissions To A User On A Diary
         prepAddDiaries.setString(1, usr);
         prepAddDiaries.setInt(2, diaryid);
         prepAddDiaries.setString(3, permissions);
         prepAddDiaries.executeUpdate();
         return "Successfully Added ";
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to set values in an SQL prepared statement");
         throw new DBServerException(ex.getLocalizedMessage());
      }
   }

   /**
    *  This Is Used To Check DairyID BeforeAdding permissions In The Server Command Line
    * @param usr = user to be added
    * @param diaryid = dairy id of the diary getting permissions added to
    * @param permissions = premission string
    * @return the outcome of the function : successful or un-successful
    * @throws DBServerException
    */
   public String addPremission(String usr, Integer diaryid, String permissions) throws DBServerException
   {
      String outcome = "";
      /////////////////////// Check format of the permissions string could be number or letter
      outcome = addDiaries(usr, diaryid, permissions);
      return outcome;
   }

   /**
    * This Is Used To Add Diary In The Server Command Line
    * @param usr = user to be added as the owner of the diary
    * @param DiaryName = Diary name to be assignened
    * @return = outcome of the internal functions
    * @throws DBServerException
    */
   public String Adddry(String usr, String DiaryName) throws DBServerException
   {
      //Create Date For The Revision Number Of The Diary
      Calendar thisday = Calendar.getInstance();
      Integer DairyId = GetNextDiaryID();
      //Adds The Diary To The DIARY Table
      String outcome = addDiary(DairyId, DiaryName, usr, Long.toString(thisday.getTimeInMillis()));
      //Adds The permissions To The DIARIES Tabel
      outcome = addDiaries(usr, DairyId, "7");
      return outcome;
   }

   /**
    * gets a diary id from diary owner and the diaryname
    * @param diaryowner = user that owns the diary and hence the dairy id we are searching for
    * @param diaryname = name of the users diary that we are searching for
    * @return the dairy id
    */
   public Integer getDairyId(String diaryowner, String diaryname)
   {
      Integer DairyId = -1;
      try
      {
         chkDairyID.setString(1, diaryowner);
         chkDairyID.setString(2, diaryname);
         ResultSet rset = chkDairyID.executeQuery();
         if (rset.next())
         {
            DairyId = Integer.valueOf(rset.getInt(1));
         }

         return DairyId;

      }
      catch (SQLException ex)
      {
         Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      return DairyId;
   }

   /**
    * This Is Used To Remove A User From The USERS Table
    * @param usr = user being removed
    * @return acknowledgement of completion or failure
    */
   private String dumRmUsr(String usr)
   {
      try
      {
         //Removes All Entries Owned By The User
         rmvAllEntries.setString(1, usr);
         rmvAllEntries.executeUpdate();

         //Removes All permissions That The User Has
         rmvAllDiaries.setString(1, usr);
         rmvAllDiaries.executeUpdate();

         //Removes All Diarys Owned By The User
         rmvAlldiary.setString(1, usr);
         rmvAlldiary.executeUpdate();

         //Removes The Users Salt
         rmvSalt.setString(1, usr);
         rmvSalt.executeUpdate();

         //Removes The User
         rmvUser.setString(1, usr);
         rmvUser.executeUpdate();

         return usr + " Successfully Removed.";

      }
      catch (SQLException ex)
      {
         Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      return usr + " Unsuccessfully Removed.";
   }

   /**
    * This Is Used To Remove A Diary From The DIARY Table
    * @param usr = owner of the diary
    * @param Dairyid = the dairy id
    * @return acknowledgement of completion or failure
    */
   public String rmDry(String usr, String Dairyid)
   {
      try
      {
         // Gets The DAIRYID From Owners Name And Diary Name
         chkDairyID.setString(1, usr);
         chkDairyID.setString(1, Dairyid);
         ResultSet rset = chkDairyID.executeQuery();
         if (rset.next())
         {
            Dairyid = String.valueOf(rset.getInt(1));
         }

         //Removes The Entries That Belonged To The Dairy
         rmvEntries.setString(1, Dairyid);
         rmvEntries.executeUpdate();

         //Removes The Premission That Belonged To The Dairy
         tmvDiaries.setString(1, Dairyid);
         tmvDiaries.executeUpdate();

         //Removes The Dairy
         rmvDiary.setString(1, usr);
         rmvDiary.setString(2, Dairyid);
         rmvDiary.executeUpdate();
      }
      catch (SQLException ex)
      {
         Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      return Dairyid + " Removed";
   }

   /**
    * This Is Used To Remove A Users From The Database
    * @param usr = user to be removed
    * @return acknowledgement of completion or failure
    */
   public String rmUsr(String usr)
   {
      //Does A Dumb Remove (Removes Everything That Has The User Name)
      return dumRmUsr(usr);

   }

   /**
    * This Is Used To Remove A Users permissions From The Diary
    * @param usr = user to have permissions removed
    * @param diaryid = id the user has permission on
    * @return acknowledgement of completion or failure
    */
   public String rmPer(String usr, String diaryid)
   {
      // Checks on The Users DiaryID
      try
      {
         int temp = Integer.getInteger(diaryid);
         if (temp > 0)
         {
            chkpermissions.setString(1, usr);
            chkpermissions.setString(2, diaryid);
            ResultSet rset = chkpermissions.executeQuery();
            if (rset.next())
            {
               diaryid = String.valueOf(rset.getInt(1));
            }
         }
         //Removes The permissions From DIARIES
         rmvpermissions.setString(1, usr);
         rmvpermissions.setString(2, diaryid);
         rmvpermissions.executeUpdate();
      }
      catch (SQLException ex)
      {
         Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
      }
      return usr + " permissions have been revoked from diary " + diaryid + ".";
   }

   /**
    * Creates the Trigger for the Database
    */
   private void CreateTriggers()
   {
      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_passwordSalts_USER_users_USER " + "BEFORE INSERT ON [passwordSalts] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"passwordSalts\" violates foreign key constraint \"fki_passwordSalts_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;" + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_passwordSalts_USER_users_USER " + "BEFORE UPDATE ON [passwordSalts] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"passwordSalts\" violates foreign key \"constraint \"fku_passwordSalts_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;" + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_passwordSalts_USER_users_USER " + "BEFORE DELETE ON users " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_passwordSalts_USER_users_USER\"') " + "WHERE (SELECT USER FROM passwordSalts WHERE USER = OLD.USER) IS NOT NULL;" + "END;");

      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_userSessions_USER_users_USER " + "BEFORE INSERT ON [userSessions] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"userSessions\" violates foreign key constraint \"fki_userSessions_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;" + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_userSessions_USER_users_USER " + "BEFORE UPDATE ON [userSessions] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"userSessions\" violates foreign key \"constraint \"fku_userSessions_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;" + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_userSessions_USER_users_USER " + "BEFORE DELETE ON users " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_userSessions_USER_users_USER\"') " + "WHERE (SELECT USER FROM userSessions WHERE USER = OLD.USER) IS NOT NULL;" + "END;");

      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_diary_OWNER_users_USER " + "BEFORE INSERT ON [diary] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"diary\" violates foreign key constraint \"fki_diary_OWNER_users_USER\"')" + "WHERE NEW.OWNER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.OWNER) IS NULL;" + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_diary_OWNER_users_USER " + "BEFORE UPDATE ON [diary] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"diary\" violates foreign key constraint \"fku_diary_OWNER_users_USER\"')" + "WHERE NEW.OWNER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.OWNER) IS NULL;" + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_diary_OWNER_users_USER " + "BEFORE DELETE ON users " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_diary_OWNER_users_USER\"')" + "WHERE (SELECT OWNER FROM diary WHERE OWNER = OLD.USER) IS NOT NULL;" + "END;");

      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_diaries_USER_users_USER " + "BEFORE INSERT ON [diaries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"diaries\" violates foreign key constraint \"fki_diaries_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;" + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_diaries_USER_users_USER " + "BEFORE UPDATE ON [diaries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"diaries\" violates foreign key constraint \"fku_diaries_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL;" + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_diaries_USER_users_USER " + "BEFORE DELETE ON users " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_diaries_USER_users_USER\"')" + "WHERE (SELECT USER FROM diaries WHERE USER = OLD.USER) IS NOT NULL;" + "END;");

      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_diaries_DIARYID_diary_ID " + "BEFORE INSERT ON [diaries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"diaries\" violates foreign key constraint \"fki_diaries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL; " + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_diaries_DIARYID_diary_ID " + "BEFORE UPDATE ON [diaries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"diaries\" violates foreign key constraint \"fku_diaries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL; " + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_diaries_DIARYID_diary_ID " + "BEFORE DELETE ON diary " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"diary\" violates foreign key constraint \"fkd_diaries_DIARYID_diary_ID\"') " + "WHERE (SELECT DIARYID FROM diaries WHERE DIARYID = OLD.ID) IS NOT NULL; " + "END;");

      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_entries_DIARYID_diary_ID " + "BEFORE INSERT ON [entries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"entries\" violates foreign key constraint \"fki_entries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL; " + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_entries_DIARYID_diary_ID " + "BEFORE UPDATE ON [entries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"entries\" violates foreign key constraint \"fku_entries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = NEW.DIARYID) IS NULL; " + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_entries_DIARYID_diary_ID " + "BEFORE DELETE ON diary " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"diary\" violates foreign key constraint \"fkd_entries_DIARYID_diary_ID\"') " + "WHERE (SELECT DIARYID FROM entries WHERE DIARYID = OLD.ID) IS NOT NULL; " + "END;");

      // Foreign Key Preventing insert
      executeUpdate("CREATE TRIGGER fki_entries_USER_users_USER " + "BEFORE INSERT ON [entries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'insert on table \"entries\" violates foreign key constraint \"fki_entries_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL; " + "END;");

      // Foreign key preventing update
      executeUpdate("CREATE TRIGGER fku_entries_USER_users_USER " + "BEFORE UPDATE ON [entries] " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'update on table \"entries\" violates foreign key constraint \"fku_entries_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = NEW.USER) IS NULL; " + "END;");

      // Foreign key preventing delete
      executeUpdate("CREATE TRIGGER fkd_entries_USER_users_USER " + "BEFORE DELETE ON users " + "FOR EACH ROW BEGIN " + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_entries_USER_users_USER\"') " + "WHERE (SELECT USER FROM entries WHERE USER = OLD.USER) IS NOT NULL; " + "END;");

   }

   /**
    * Drops the tables in the database
    */
   private void DropTables()
   {
      executeUpdate("drop table users");
      executeUpdate("drop table passwordSalts");
      executeUpdate("drop table userSessions");
      executeUpdate("drop table diaries");
      executeUpdate("drop table diary");
      executeUpdate("drop table entries");

   }

   /**
    * Drops all trigger in the Database
    */
   private void DropTriggers()
   {

      executeUpdate("DROP TRIGGER fki_passwordSalts_USER_users_USER");
      executeUpdate("DROP TRIGGER fku_passwordSalts_USER_users_USER");
      executeUpdate("DROP TRIGGER fkd_passwordSalts_USER_users_USER");
      executeUpdate("DROP TRIGGER fki_userSessions_USER_users_USER");
      executeUpdate("DROP TRIGGER fku_userSessions_USER_users_USER");
      executeUpdate("DROP TRIGGER fkd_userSessions_USER_users_USER");
      executeUpdate("DROP TRIGGER fki_diary_OWNER_users_USER");
      executeUpdate("DROP TRIGGER fku_diary_OWNER_users_USER");
      executeUpdate("DROP TRIGGER fkd_diary_OWNER_users_USER");
      executeUpdate("DROP TRIGGER fki_diaries_USER_users_USER");
      executeUpdate("DROP TRIGGER fku_diaries_USER_users_USER");
      executeUpdate("DROP TRIGGER fkd_diaries_USER_users_USER");
      executeUpdate("DROP TRIGGER fki_diaries_DIARYID_diary_ID");
      executeUpdate("DROP TRIGGER fku_diaries_DIARYID_diary_ID");
      executeUpdate("DROP TRIGGER fkd_diaries_DIARYID_diary_ID");
      executeUpdate("DROP TRIGGER fki_entries_DIARYID_diary_ID");
      executeUpdate("DROP TRIGGER fku_entries_DIARYID_diary_ID");
      executeUpdate("DROP TRIGGER fkd_entries_DIARYID_diary_ID");
      executeUpdate("DROP TRIGGER fki_entries_USER_users_USER");
      executeUpdate("DROP TRIGGER fku_entries_USER_users_USER");
      executeUpdate("DROP TRIGGER fkd_entries_USER_users_USER");

   }

   /**
    * Creates the Tables for the database
    */
   private void CreateTables()
   {
      executeUpdate(
              "CREATE TABLE users(" + "        USER TEXT PRIMARY KEY ON CONFLICT FAIL," + "        DISPLAYNAME NOT NULL," + "        DEFAULTDIARYID," + "        PWDHASH NOT NULL" + ")");

      executeUpdate(
              "CREATE TABLE passwordSalts(" + "        USER TEXT CONSTRAINT FK_passwordSalts_USER REFERENCES users(USER)," + "        SALT NOT NULL	" + ")");

      executeUpdate(
              "CREATE TABLE userSessions(" + "        USER TEXT CONSTRAINT FK_userSessions_USER REFERENCES users(USER)," + "        SESSIONID" + ")");

      executeUpdate(
              "CREATE TABLE diary(" + "        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "        NAME NOT NULL," + "        OWNER TEXT CONSTRAINT FK_diary_USER REFERENCES users(USER)," + "        REVISION NOT NULL" + ")");

      executeUpdate(
              "CREATE TABLE diaries(" + "        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "        USER TEXT CONSTRAINT FK_diaries_USER REFERENCES users(USER)," + "        DIARYID INTEGER CONSTRAINT FK_diaries_DIARYID REFERENCES diary(ID)," + "        PERMISSIONS NOT NULL" + ")");

      executeUpdate(
              "CREATE TABLE entries(" + "        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "        DIARYID INTEGER CONSTRAINT FK_entries_DIARYID REFERENCES diary(ID)," + "        START NOT NULL," + "        END NOT NULL," + "        USER TEXT CONSTRAINT FK_entries_USER REFERENCES users(USER)," + "        LOCKED NOT NULL," + "        TITLE NOT NULL," + "        BODY," + "        DELETED NOT NULL" + ")");
   }
}
