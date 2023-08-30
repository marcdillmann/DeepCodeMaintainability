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
public class DiaryHeaderTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public DiaryHeaderTest(String testName) {
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
    * Test of Get method, of class DiaryHeader.
    * @throws Exception 
    */
   public void testGet() throws Exception
   {
      System.out.println("Get");
      DiaryHeader setInstance = new DiaryHeader();
      DiaryHeader getInstance = new DiaryHeader();

      char expOwner = 'Y';
      char resultOwner;
      String expName = "Diary name";
      String resultName;
      int expID = 23;
      int resultID;
      int expPermissions = 444;
      int resultPermissions;
      int expRev = 2;
      int resultRev;

      try
      {
         setInstance.SetDiaryID(expID);
         setInstance.SetDiaryName(expName);
         setInstance.SetOwnerFlag(expOwner);
         setInstance.SetPermissions(expPermissions);
         setInstance.SetRevNum(expRev);

         getInstance.Set(setInstance.Get());

         resultID = getInstance.GetDiaryID();
         resultName = getInstance.GetDiaryName();
         resultOwner = getInstance.GetOwnerFlag();
         resultPermissions = getInstance.GetPermissions();
         resultRev = getInstance.GetRevNum();

         assertEquals(expID, resultID);
         assertEquals(expName, resultName);
         assertEquals(expOwner, resultOwner);
         assertEquals(expPermissions, resultPermissions);
         assertEquals(expRev, resultRev);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         getInstance = new DiaryHeader();

         String failResult = getInstance.Get();

         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetDiaryID method, of class DiaryHeader.
    */
   public void testGetDiaryID()
   {
      System.out.println("GetDiaryID");
      DiaryHeader instance = new DiaryHeader();
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

   /**
    * Test of GetDiaryName method, of class DiaryHeader.
    */
   public void testGetDiaryName()
   {
      System.out.println("GetDiaryName");
      DiaryHeader instance = new DiaryHeader();
      String expResult = "";

      try
      {
         instance.SetDiaryName(expResult);
         String result = instance.GetDiaryName();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetDiaryName(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetOwnerFlag method, of class DiaryHeader.
    */
   public void testGetOwnerFlag()
   {
      System.out.println("GetOwnerFlag");
      DiaryHeader instance = new DiaryHeader();
      char expResult = 'Y';

      try
      {
         instance.SetOwnerFlag(expResult);
         char result = instance.GetOwnerFlag();

         assertEquals(expResult, result);

         expResult = 'N';
         instance.SetOwnerFlag(expResult);
         result = instance.GetOwnerFlag();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetOwnerFlag('M');
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetPermissions method, of class DiaryHeader.
    */
   public void testGetPermissions()
   {
      System.out.println("GetPermissions");
      DiaryHeader instance = new DiaryHeader();
      int expResult = 511;

      try
      {
         instance.SetPermissions(expResult);
         int result = instance.GetPermissions();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetPermissions(777);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of GetRevNum method, of class DiaryHeader.
    */
   public void testGetRevNum()
   {
      System.out.println("GetRevNum/SetRevNum");
      DiaryHeader instance = new DiaryHeader();

      int expResult = 0;
      try
      {
         instance.SetRevNum(expResult);
         int result = instance.GetRevNum();

         assertEquals(expResult, result);

      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.SetRevNum(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }
}
