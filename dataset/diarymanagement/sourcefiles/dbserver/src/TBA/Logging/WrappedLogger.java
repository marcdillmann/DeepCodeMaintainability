package TBA.Logging;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;

/**
 * This class wraps the Logger
 *<p> 
 * <p>
 * @author Dan McGrath
 * 
 * @version $Rev:: 97            $ $Date:: 2009-09-06 #$
 * 
 * @see java.util.logging.Logger
 */
public class WrappedLogger
{
   private Logger actualLogger;

   public WrappedLogger(Logger newLogger)
   {
      actualLogger = newLogger;
   }

   public void severe(Exception ex, String msg)
   {
      actualLogger.severe(msg);
      actualLogger.info(ex.getLocalizedMessage());
   }

   public void severe(String msg)
   {
      actualLogger.severe(msg);
   }

   public void warning(Exception ex, String msg)
   {
      actualLogger.warning(msg);
      actualLogger.info(ex.getLocalizedMessage());
   }

   public void warning(String msg)
   {
      actualLogger.warning(msg);
   }

   public void info(String msg)
   {
      actualLogger.info(msg);
   }

   public void fine(String msg)
   {
      actualLogger.fine(msg);
   }

   public void finer(String msg)
   {
      actualLogger.finer(msg);
   }

   public void finest(String msg)
   {
      actualLogger.finest(msg);
   }

   public void setLevel(Level newLevel)
   {
      actualLogger.setLevel(newLevel);
   }

   public Handler[] getHandlers()
   {
      return actualLogger.getHandlers();
   }
}