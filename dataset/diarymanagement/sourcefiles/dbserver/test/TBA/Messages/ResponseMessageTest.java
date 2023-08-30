/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import TBA.Exceptions.MessagesException;
import junit.framework.TestCase;

/**
 *
 * @author Dan McGrath
 *
 * @version $Rev:: 97   $ $Date:: 2009-09-06 #$
 */
public class ResponseMessageTest extends TestCase {
    
    /**
     * 
     * @param testName
     */
    public ResponseMessageTest(String testName) {
        super(testName);
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     *
     * @throws java.lang.Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

   /**
    * Test of Get method, of class ResponseMessage.
    * @throws Exception
    */
   public void testGet() throws Exception
   {
      System.out.println("Get");

      ResponseMessage getInstance = new ResponseMessage();
      ResponseMessage setInstance = new ResponseMessage();

      int expCode = 3;
      int codeResult;
      String expError = "Expected error message!";
      String errorResult;

      getInstance.SetErrorCode(expCode);
      getInstance.SetErrorMessage(expError);

      setInstance.Set(getInstance.Get(""));

      codeResult = setInstance.GetErrorCode();
      errorResult = setInstance.GetErrorMessage();

      assertEquals(expCode, codeResult);
      assertEquals(expError, errorResult);
   }


   /**
    * Test of GetErrorCode method, of class ResponseMessage.
    */
   public void testGetErrorCode()
   {
      System.out.println("GetErrorCode/SetErrorCode");

      ResponseMessage instance = new ResponseMessage();
      int expResult = 1;
      int result;

      try
      {
         instance.SetErrorCode(expResult);
         result = instance.GetErrorCode();
         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetErrorCode(16);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }


   /**
    * Test of GetErrorMessage method, of class ResponseMessage.
    */
   public void testGetErrorMessage()
   {
      System.out.println("GetErrorMessage/SetErrorMessage");

      ResponseMessage instance = new ResponseMessage();
      String expResult = "My test error message!";

      String result;
      try
      {
         instance.SetErrorMessage(expResult);
         result = instance.GetErrorMessage();
         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetErrorMessage(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }
}
