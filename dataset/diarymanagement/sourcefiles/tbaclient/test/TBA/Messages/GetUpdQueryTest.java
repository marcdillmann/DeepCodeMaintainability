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
public class GetUpdQueryTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public GetUpdQueryTest(String testName) {
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
    * Test of Get method, of class GetUpdQuery.
    * @throws Exception
    */
   public void testGet() throws Exception
   {
      System.out.println("Get");
      String dummy = "";

      GetUpdQuery setInstance = new GetUpdQuery();
      GetUpdQuery getInstance = new GetUpdQuery();

      int expID = 20;
      int resID;

      int expRevNo = 1;
      int resRevNo;

      try
      {
         setInstance.SetDiaryID(expID);
         setInstance.SetRevisionNumber(expRevNo);

         getInstance.Set(setInstance.Get(dummy));

         resID = getInstance.GetDiaryID();
         resRevNo = getInstance.GetRevisionNumber();

         assertEquals(expID,resID);
         assertEquals(expRevNo,resRevNo);

      } catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         getInstance = new GetUpdQuery();

         String failResult = getInstance.Get(dummy);

         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetRevisionNumber method, of class GetUpdQuery.
    */
   public void testGetRevisionNumber()
   {
      System.out.println("GetRevisionNumber");
      GetUpdQuery instance = new GetUpdQuery();
      int expResult = 0;
      try
      {
         instance.SetRevisionNumber(expResult);
         int result = instance.GetRevisionNumber();

         assertEquals(expResult, result);

      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetRevisionNumber(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetDiaryID method, of class GetUpdQuery.
    */
   public void testGetDiaryID()
   {
      System.out.println("GetDiaryID");
      GetUpdQuery instance = new GetUpdQuery();
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
