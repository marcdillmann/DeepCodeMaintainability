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
public class QueryMessageTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public QueryMessageTest(String testName) {
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
    * Test of Get method, of class QueryMessage.
    * @throws Exception 
    */
   public void testGet() throws Exception
   {
      System.out.println("Get/Set");
      QueryMessage getInstance = new QueryMessage();
      QueryMessage setInstance = new QueryMessage();

      String expResult = "GETDIARY";

      getInstance.SetQueryType(expResult);

      setInstance.Set(getInstance.Get(""));
      String result = setInstance.GetQueryType();

      assertEquals(expResult, result);
   }

   /**
    * Test of GetQueryType method, of class QueryMessage.
    */
   public void testGetQueryType()
   {
      System.out.println("GetQueryType/SetQueryType");
      
      QueryMessage instance = new QueryMessage();
      String expResult = "LOGIN";

      String result;
      try
      {
         instance.SetQueryType(expResult);
         result = instance.GetQueryType();
         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }
      
      try
      {
         instance.SetQueryType(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }
}
