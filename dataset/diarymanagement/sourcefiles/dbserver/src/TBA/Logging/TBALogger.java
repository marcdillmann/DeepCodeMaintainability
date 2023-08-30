package TBA.Logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

/**
 * This class initialises the parent logger
 *<p> 
 * If any loggers have been instantiated before the settings have
 * been set by this class, you must manually apply the settings to
 * those loggers as well.
 * <p>
 * Any loggers created after calling this class, will inherit the
 * settings you set here.
 * <p>
 * @author Dan McGrath
 * 
 * @version $Rev:: 97            $ $Date:: 2009-09-06 #$
 * 
 * @see java.util.logging.Logger
 */
public class TBALogger
{
   /**
    * This constructor initialises the parent logger specifying the log filename
    *<p> 
    * @param logfile Filename to write log entries to
    * @throws IOException
    * 
    * @see #setup() 
    */
	static public void setup(String logfile) throws IOException
   {
   	FileHandler fileTxt;

		// Get the parent Logger and set default values
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.INFO);

      if(logfile.compareTo("") == 0 || logfile != null)
      {
         // Setup the logger to write to the filename passed in.
         fileTxt = new FileHandler(logfile);
         fileTxt.setFormatter(new SimpleFormatter());
         logger.addHandler(fileTxt);
      }
   }

   /**
    * This constructor initialises the parent logger. A default log filename of
    * TBALog is set
    *<p> 
    * @throws IOException
    * 
    * @see #setup(java.lang.String) 
    */
   static public void setup() throws IOException
   {
   	FileHandler fileTxt;

		// Create Logger
		Logger logger = Logger.getLogger("");
		logger.setLevel(Level.INFO);

      // Setup the logger to write to file.
      fileTxt = new FileHandler("TBALog");
      fileTxt.setFormatter(new SimpleFormatter());
      logger.addHandler(fileTxt);

      // Setup the logger to write to the console
      logger.addHandler(new ConsoleHandler());
	}

   /**
    * This method sets the Level of detail to log of the parent logger and all
    * it's handlers
    *<p>
    * @param loglevel The level of detail to set the logger to.
    *<p>
    * @throws IOException
    *<p>
    * @see java.util.logging.Logger#setLevel(java.util.logging.Level)
    * @see java.util.logging.Handler#setLevel(java.util.logging.Level)
    */
   static public void setLevel(Level loglevel) throws IOException
   {
		// Create Logger
		Logger logger = Logger.getLogger("");

      logger.setLevel(loglevel);
      // Setup the logger to write to the console
      for(Handler logHandler : logger.getHandlers())
      {
         logHandler.setLevel(loglevel);
      }
   }
   
   /**
    * This method removes the first handler from the parent log. The first time
    * you call this it should be the ConsoleHandler
    *<p> 
    * @see java.util.logging.ConsoleHandler
    * @see java.util.logging.Logger#removeHandler(java.util.logging.Handler) 
    */
   static public void removeConsole()
   {
      Logger logger = Logger.getLogger("");
      
      logger.removeHandler(logger.getHandlers()[0]);
   }
}