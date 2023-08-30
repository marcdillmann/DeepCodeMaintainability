/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import TBA.Exceptions.MessagesException;
import java.util.UUID;
import junit.framework.TestCase;

/**
 *
 * @author Dan McGrath
 *
 * @version $Rev:: 97   $ $Date:: 2009-09-06 #$
 */
public class MessagesTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public MessagesTest(String testName) {
        super(testName);
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /**
    * Test of Get/Set method, of class Messages.
    * @throws Exception
    */
   public void testGet() throws Exception
   {
      System.out.println("Get/Set");

      Messages setInstance = new Messages();
      Messages getInstance = new Messages();

      char expResult;
      char result;
      String expSession;
      String resultSession;

      try
      {
         expResult = 'Q';
         // Reflects current Session ID generating by server.
         expSession = UUID.randomUUID().toString();

         setInstance.SetMessageType(expResult);
         setInstance.SetSessionID(expSession);

         getInstance.Set(setInstance.Get(""));

         result = getInstance.GetMessageType();
         resultSession = getInstance.GetSessionID();
         assertEquals(expResult, result);
         assertEquals(expSession, resultSession);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         getInstance = new Messages();

         String failResult = getInstance.Get("");

         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetMessageType/GetMessageType method, of class Messages.
    */
   public void testGetMessageType()
   {
      System.out.println("GetMessageType/SetMessageType");
      Messages instance = new Messages();

      char expResult;
      char result;

      try
      {
         expResult = 'Q';
         instance.SetMessageType(expResult);
         result = instance.GetMessageType();

         assertEquals(expResult, result);
         
         expResult = 'R';
         instance.SetMessageType(expResult);
         result = instance.GetMessageType();
         
         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         expResult = 'P';
         instance.SetMessageType(expResult);
         result = instance.GetMessageType();

         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetSessionID/SetSessionID method, of class Messages.
    */
   public void testGetSessionID()
   {
      System.out.println("GetSessionID/SetSessionID");
      Messages instance = new Messages();
      String expResult;
      String result;
      int sessionIDLen = 36; // Bad, brittle test. Value from messages class!

      try
      {
         // Reflects current Session ID generating by server.
         expResult = UUID.randomUUID().toString();

         instance.SetSessionID(expResult);

         result = instance.GetSessionID();
         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

   try
      {
         expResult = "01234567"; // Wrong size

         instance.SetSessionID(expResult);
         result = instance.GetSessionID();
         assertEquals(expResult, result);

         fail("Expected thow, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }
}
