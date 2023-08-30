package tbaclient;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

/**
 * This class handles the connection to the TBA server
 *<p>
 *
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 11            $ $Date:: 2009-07-21 #$
 */

public class ServerCom
{
   private Socket MyConx;
   private DataOutputStream os;
   private DataInputStream is;
   public final static Logger LOGIT = Logger.getLogger(ServerCom.class.getName());
    
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
   public ServerCom(String machineName, int portNumber) throws ServerComException
   {
      try
      {
         MyConx = new Socket(machineName, portNumber);
      }

      catch (UnknownHostException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket: Host name unknown");
         throw new ServerComException(ex);
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating socket");
         throw new ServerComException(ex);
      }

      try
      {
         os = new DataOutputStream(MyConx.getOutputStream());
         is = new DataInputStream(MyConx.getInputStream());
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Error creating stream for the socket");
         throw new ServerComException(ex);
      }
   }

   /**
    * This recieves data from the server
    *<p>
    *<p>
    * @throws ServerComException
    *
    * @return A string containing the data sent from the server
    *
    * @see #putData(java.lang.String)
    */
   public String getData() throws ServerComException
   {
      byte[] s=new byte[1024];
      int cnt = 0;
      try
      {
         while(true)
         {
            byte b = is.readByte();
            if(b == '\n')
            {
               break;
            }
            s[cnt] = b;
            cnt++;
            if(cnt >= 1024)
            {
               break;
            }
         }
      }
      catch(Exception ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Failed recieving data from the server");
         throw new ServerComException(ex);
      }
        
      return new String(s, 0, cnt);
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
   public void putData(String data) throws ServerComException
   {
      try
      {
         os.writeBytes(data + '\n');
      }
      catch (IOException ex)
      {
         LOGIT.info(ex.getLocalizedMessage());
         LOGIT.severe("Failed recieving data from the server");
         throw new ServerComException(ex);
      }
   }
}