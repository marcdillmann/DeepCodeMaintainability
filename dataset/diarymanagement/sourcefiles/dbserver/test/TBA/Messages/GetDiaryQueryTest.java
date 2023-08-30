/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import junit.framework.TestCase;
import TBA.Exceptions.MessagesException;

/**
 *
 * @author cs321tx2
 */
public class GetDiaryQueryTest extends TestCase {
    
    /**
     * 
     * @param testName
     */
    public GetDiaryQueryTest(String testName) {
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
    * Test of Get method, of class GetDiaryQuery.
    * @throws Exception
    */
   public void testGet() throws Exception
   {
      //TODO: make sure this is right
      System.out.println("Get");
      String dummy = "";

      GetDiaryQuery setInstance = new GetDiaryQuery();
      GetDiaryQuery getInstance = new GetDiaryQuery();
      
      int expID = 20;
      int resID;

      try
      {
         setInstance.SetDiaryID(expID);

         getInstance.Set(setInstance.Get(dummy));

         resID = getInstance.GetDiaryID();

         assertEquals(expID,resID);

      } catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         getInstance = new GetDiaryQuery();

         String failResult = getInstance.Get(dummy);

         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }


   }

   /**
    * Test of GetDiaryID method, of class GetDiaryQuery.
    */
   public void testGetDiaryID()
   {
      System.out.println("GetDiaryID");
      GetDiaryQuery instance = new GetDiaryQuery();
      int expResult = 20;

      try
      {
         instance.SetDiaryID(expResult);
         int result = instance.GetDiaryID();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetDiaryID(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }
}
