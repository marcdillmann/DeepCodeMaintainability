package TBA.Server;

import TBA.Exceptions.*;
import TBA.Logging.TBALogger;
import TBA.Logging.WrappedLogger;
import TBA.Security.PasswordHashing;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main class of the DBServer
 *<p>
 * @author Dan McGrath
 *
 * @version $Rev:: 273  $ $Date:: 2011-10-25 #$
 */
public class Main
{
   private static int port = 9999;
   private static boolean console = false;
   private final static WrappedLogger LOGIT = new WrappedLogger(Logger.getLogger(Main.class.getName()));
   private NetServer netServ;
   private Thread netThread;
   private DBServer dbInstance;
   private PasswordHashing PassHash;

   //  public void Main()
   //  {
//   }
   /**
    * Entry point
    *<p>
    * @param args Command line arguments. Currently it supports a handful of
    * switches.
    * <p>
    * Firstly, '-p x' or '--port x', where x is the port number you
    * wish the server to listen on.
    * <p>
    * Next is '-l level' or '--log-level level'
    * where 'level' is one of SEVERE, WARNING, INFO, FINE, FINER or FINEST
    * <p>
    * Lastly is '-c' or '--console' which enables logging to the console
    * @throws PasswordHashingException
    * @throws DBServerException
    */
   public static void main(String[] args) throws PasswordHashingException, DBServerException, SQLException
   {
      // TODO: Parameterize and secure this.
      
      System.setProperty("javax.net.ssl.keyStore", "tbaKeyStore");
      System.setProperty("javax.net.ssl.keyStorePassword", "Rgr4j9");

      LOGIT.info("Starting Server");
      Main server = new Main();
      try
      {
         TBALogger.setup("TBAServer.log");
         parseArgs(args);
         server.startNet();
         LOGIT.info("Starting Local mode");
         server.startLocal();
      }
      catch (IOException ex)
      {
         // Don't let logging stop our server...
         // Just log it to console if possible.
         LOGIT.warning(ex, "Error: Could not create the log file");
      }

   }

   /**
    * Parse the command line arguments
    *<p>
    * @param args Command line arguments. Currently it supports a handful of
    * switches.
    * <p>
    * Firstly, '-p x' or '--port x', where x is the port number you
    * wish the server to listen on.
    * <p>
    * Next is '-l level' or '--log-level level'
    * where 'level' is one of SEVERE, WARNING, INFO, FINE, FINER or FINEST
    * <p>
    * Lastly is '-c' or '--console' which enables logging to the console
    */
   private static void parseArgs(String args[])
   {
      String arg = null;
      
      // TODO: Refactor this section
      for (int argnum = 0; argnum < args.length; argnum++)
      {
         arg = args[argnum];
         if (arg.compareTo("-l") == 0 || arg.compareTo("--log-level") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               arg = args[argnum];
               try
               {
                  Level loglevel = Level.parse(arg);
                  TBALogger.setLevel(loglevel);
                  LOGIT.setLevel(loglevel);

                  // Setup this classes logger to write to the console
                  for (Handler logHandler : LOGIT.getHandlers())
                  {
                     logHandler.setLevel(loglevel);
                  }

                  LOGIT.info("Change Log level to: " + arg);
               }
               catch (IllegalArgumentException ex)
               {
                  LOGIT.warning("Invalid Log Level set via parameter: " + arg);
               }
               catch (IOException ex)
               {
                  LOGIT.warning(ex, "Error setting log level");
               }
            }
            else
            {
               LOGIT.warning("Missing detail for Log Level argument");
            }
         }
         else if (arg.compareTo("-p") == 0 || arg.compareTo("--port") == 0)
         {
            argnum++;
            if (argnum < args.length)
            {
               arg = args[argnum];
               try
               {
                  int portnum = Integer.parseInt(arg);
                  if (portnum > 1024 && portnum < 65536)
                  {
                     port = portnum;
                     LOGIT.info("Change Port to: " + arg);
                  }
                  else
                  {
                     LOGIT.warning("Out of range Port number set via parameter: " + arg);
                  }
               }
               catch (NumberFormatException ex)
               {
                  LOGIT.warning("Invalid Port number set via parameter: " + arg);
               }
            }
            else
            {
               LOGIT.warning("Missing detail for Port Number argument");
            }
         }
         else if (arg.compareTo("-c") == 0 || arg.compareTo("--console") == 0)
         {
            console = true;
            LOGIT.info("Console output enabled");
         }
      }

      // TODO: This is a hack. The first handler *should* be a console, at least if
      // only call this once. This code should be changed to look through the
      // handles and determine if they are a console handler, then remove it
      // when found.
      if (!console)
      {
         TBALogger.removeConsole();
      }
   }

   /**
    * Starts the Networking thread that deals with clients
    */
   public void startNet() throws IOException, SQLException, PasswordHashingException
   {
      try
      {  
         LOGIT.fine("Creating Database connection");
         dbInstance = new DBServer("", "");
         dbInstance.setup();

         LOGIT.fine("Starting network connection");
         netServ = new NetServer(port, dbInstance);
         netThread = new Thread(netServ);
         netThread.start();
      }
      catch (DBServerException ex)
      {
         LOGIT.severe(ex, "Unable to setup Database");
         System.exit(1);
      }
      catch (NetServerException ex)
      {
         LOGIT.severe(ex,"Unable to setup Network. Continue in local mode only");
      }
   }

   /**
    * Handles keyboard entry directly into the server.
    * @throws DBServerException
    * @throws PasswordHashingException
    */
   public void startLocal() throws IOException
   {
      boolean finished = false;
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

      char creationchar = ' ';


      try
      {
         System.out.println("\n : TBA Server Cmd :\n");
         System.out.println("Type \"help\" for list of commands.");

         String commandline;
         String[] command = new String[5];

         while (!finished)
         {

            //clear the array
            for (int i = 0; i < command.length; i++)
            {
               command[i] = "";
            }

            System.out.print("$");
            commandline = stdin.readLine();
            command = commandline.split(" ");
            if (command[0] != null && (command[0].length() > 3))
            {
               commandline = command[0];
               if (command[0].compareToIgnoreCase("quit") == 0 || command[0].compareToIgnoreCase("exit") == 0)
               {
                  LOGIT.info("Exiting server");
                  finished = true;
                  netServ.setFinished(finished);
               }
               else if (command[0].compareToIgnoreCase("AddUser") == 0)
               {
                  String usr, pws, DisplayName, DairyName;
                  System.out.println("Insert username:");
                  usr = stdin.readLine();
                  System.out.println("Insert password:");
                  pws = stdin.readLine();
                  System.out.println("Insert DisplayName:");
                  DisplayName = stdin.readLine();
                  System.out.println("Insert DiaryName:");
                  DairyName = stdin.readLine();
                  System.out.println(dbInstance.Addusr(usr, pws, DisplayName, DairyName));
               }
               else if (command[0].compareToIgnoreCase("AddDiary") == 0)
               {
                  System.out.println("Insert User Name:");
                  String usr = stdin.readLine();
                  System.out.println("Insert Diary Name:");
                  String DairyName = stdin.readLine();
                  System.out.println(dbInstance.Adddry(usr, DairyName));
               }
               else if (command[0].compareToIgnoreCase("AddPerson") == 0)
               {
                  System.out.println("Insert User Name:");
                  String usr = stdin.readLine();
                  System.out.println("Insert Diary Owner:");
                  String diaryowner = stdin.readLine();
                  System.out.println("Insert Diary ID:");
                  String dairy = stdin.readLine();
                  int dairyid = Integer.valueOf(dairy);
                  System.out.println("Insert Permissions:");
                  String premissions = stdin.readLine();
                  System.out.println(dbInstance.addPremission(usr, dairyid, premissions));
               }
               else if (command[0].compareToIgnoreCase("RemoveUser") == 0)
               {
                  System.out.println("Insert User Name:");
                  String usr = stdin.readLine();
                  System.out.println(dbInstance.rmUsr(usr));
               }
               else if (command[0].compareToIgnoreCase("RemoveDiary") == 0)
               {
                  System.out.println("Insert Owners Name:");
                  String usr = stdin.readLine();
                  System.out.println("Insert DiaryID:");
                  String diaryid = stdin.readLine();
                  System.out.println(dbInstance.rmDry(usr, diaryid));
               }
               else if (command[0].compareToIgnoreCase("RemovePerson") == 0)
               {
                  System.out.println("Insert User Name:");
                  String usr = stdin.readLine();
                  System.out.println("Insert Diary Name:");
                  String diaryid = stdin.readLine();
                  System.out.println(dbInstance.rmPer(usr, diaryid));
               }
               else if (command[0].compareToIgnoreCase("help") == 0)
               {
                  System.out.println("AddUser          : Add A User.");
                  System.out.println("AddDiary         : Add A Diary. ");
                  System.out.println("AddPermission    : Add Premission.");
                  System.out.println("RemoveUser       : Remove A User.");
                  System.out.println("RemoveDiary      : Remove A Dairy.");
                  System.out.println("RemovePermission : Remove Premissions From A User.");
                  System.out.println("Reset            : Reset Database");
                  System.out.println("Exit  or Quit    : Quits Server.");
               }
               else if (command[0].compareToIgnoreCase("reset") == 0)
               {

                  System.out.println("Do you wish to recreate the Database? (Y/N)");
                  System.out.print("$");

                  creationchar = stdin.readLine().charAt(0);

                  creationchar = Character.toUpperCase(creationchar);
                  if (creationchar == 'Y')
                  {
                     System.out.println("Database is being constructed.\nPlease Be Patient");
                     dbInstance.RealDatabase();
                     System.out.println("Database Created Successully.");
                  }
                  else if (creationchar == 'N')
                  {
                     System.out.println("Continuing to use existing database.");
                  }
               }
            }
            else
            {
               // ensures arugmens greater then 3 are passed in and the rest are discaded
               // saves time on users incorrectly inputting arguments.
               System.out.println("Incorrect Input");
               System.out.println("Type \"help\" for list of commands.\n");
            }

         }
      }
      catch (IOException ex)
      {
         LOGIT.severe(ex, "Error with standard input, local mode.");
      }
      catch (DBServerException ex)
      {
         LOGIT.severe(ex, "Database error");
      }
   }
}