/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Server;

import TBA.Exceptions.DBServerException;
import TBA.Logging.WrappedLogger;
import java.sql.*;
import java.util.logging.Logger;

/**
 * This abstract class handles the connection to SQLite
 *<p>
 * If you wish to talk to SQLite, use this class.
 * It is only thread-safe if you instantiate it as a static class
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 272           $ $Date:: 2009-12-31 #$
 *
 * @see java.sql
 */
abstract public class SQLite
{
   private String dbAddress = null;
   private String username = null;
   private String password = null;
   private String dbDriver = null;
   /**
    * connex is connection to db
    */
   protected Connection connex = null;
   /**
    * stmt is sql statement to be executed
    */
   protected Statement stmt = null;
   private final static Object lock = new Object();
   /**
    * logger provides difference levels feedback to the user
    */
   public final static WrappedLogger LOGIT = new WrappedLogger(Logger.getLogger(Main.class.getName()));

   /**
    * This constructor initialises the DB address and db driver name
    *<p>
    * If you use it, then you must call Initialise(...) before using it.
    *<p>
    * @see #Initialise(java.lang.String, java.lang.String)
    */
   SQLite()
   {
      dbAddress = "jdbc:sqlite:TODO.db";
      dbDriver = "org.sqlite.JDBC";
   }

   /**
    * This constructor initialises the SQLite connection. It sets it the
    * database address and the driver name.
    *<p>
    * @param user The username to log into the database as
    * @param pass The password to log into the database with
    *<p>
    * @throws DBServerException
    */
   SQLite(String user, String pass) throws DBServerException
   {
      dbAddress = "jdbc:sqlite:TODO.db";
      dbDriver = "org.sqlite.JDBC";
      Initialise(user, pass);
      LOGIT.finest("Initialised SQLite");
   }

   /**
    * This is to be called in you instantiate with the default constructor.
    * You must call it before using the database if you. It logs into the database.
    *<p>
    * @param user The username to log into the database as
    * @param pass The password to log into the database with
    *<p>
    * @throws DBServerException
    */
   public void Initialise(String user, String pass) throws DBServerException
   {
      username = user;
      password = pass;

       // Boot up time.
      setConnection();
      setStatement();

      LOGIT.finest("SQL initialised");
   }

   /**
    * This creates a connection to the database
    *<p>
    * @throws DBServerException
    */
   private void setConnection() throws DBServerException
   {

      try
      {
         Class.forName(dbDriver);
         connex = DriverManager.getConnection(dbAddress, username, password);
      }
      catch (ClassNotFoundException ex)
      {
         LOGIT.severe(ex,"Unable to find class:" + dbDriver);
         throw new DBServerException(ex);
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to get the SQL connection for: " + dbAddress);
         throw new DBServerException(ex);
      }
   }

   /**
    * This creates a statement object for the database
    *<p>
    * @throws DBServerException
    */
   private void setStatement() throws DBServerException
   {
      try
      {
         stmt = connex.createStatement();
         LOGIT.finest("Created SQL statement object");
      }
      catch (SQLException ex)
      {
         LOGIT.severe(ex, "Unable to create the SQL statement object");
         throw new DBServerException(ex);
      }
   }

   /**
    * This is to be called when you want to query the database. It handles
    * the database locking.
    *<p>
    * @param sql The sql query execute
    * @return The result set of the query or null if there was an error
    */
   public ResultSet executeQuery(String sql)
   {
      ResultSet res = null;

      try
      {
         synchronized(lock)
         {
            res = stmt.executeQuery(sql);
         }
      }
      catch (SQLException ex)
      {
         // We don't throw an exception here so that the server can continue to
         // function
         LOGIT.severe(ex, "Unable to execute an SQL query");
      }

      return res;
   }

   /**
    * This executes a prepared statement that queries the database. It handles
    * the database locking
    *<p>
    * @param prepSql The prepared statement to execute
    * @return The result set of the query or null if there was an error
    *<p>
    * @see java.sql.PreparedStatement#executeQuery()
    */
   public ResultSet executeQuery(PreparedStatement prepSql)
   {
      ResultSet res = null;

      try
      {
         synchronized(lock)
         {
            res = prepSql.executeQuery();
         }
      }
      catch (SQLException ex)
      {
         // We don't throw an exception here so that the server can continue to
         // function
         LOGIT.severe(ex, "Unable to execute a prepared SQL statement");
      }

      return res;
   }

   /**
    * This executes a sql query that updates the database. It handles the
    * database locking
    *<p>
    * @param sql The sql query to execute
    * @return The number of rows affected, or -1 if there was an error.
    *<p>
    * @see java.sql.Statement#executeUpdate(java.lang.String)
    */
   public int executeUpdate(String sql)
   {
      int ret = -1;

      try
      {
         synchronized(lock)
         {
            ret = stmt.executeUpdate(sql);
         }
      }
      catch (SQLException ex)
      {
         // We don't throw an exception here so that the server can continue to
         // function
         LOGIT.severe(ex, "Unable to execute an SQL update query");
      }

      return ret;
   }

   /**
    * This executes a prepared statement that updates the database. It handles
    * the database locking
    *<p>
    * @param prepSql The prepared statement to execute
    * @return The result set of the query or null if there was an error
    *<p>
    * @see java.sql.PreparedStatement#executeUpdate()
    */
   public int executeUpdate(PreparedStatement prepSql)
   {
      int ret = -1;

      try
      {
         synchronized(lock)
         {
            ret = prepSql.executeUpdate();
         }
      }
      catch (SQLException ex)
      {
         // We don't throw an exception here so that the server can continue to
         // function
         LOGIT.severe(ex, "Unable to execute a prepared SQL statement");
      }

      return ret;
   }


   /**
    * This executes a sql query that updates the database. It handles the
    * database locking
    *<p>
    * @param prep The sql query to execute
    * @return 1 if successful or -1 if it failed.
    *<p>
    * @see java.sql.Statement#executeBatch()
    */
   // Execute an SQL Batch
   public int executeBatch(PreparedStatement prep)
   {
      int ret = -1;

      try
      {
         synchronized(lock)
         {
            connex.setAutoCommit(false);
            prep.executeBatch();
            connex.setAutoCommit(true);
         }
         ret = 1;
      }
      catch (SQLException ex)
      {
         // We don't throw an exception here so that the server can continue to
         // function
         LOGIT.severe(ex, "Unable to execute an SQL batch");
      }

      return ret;
   }
}