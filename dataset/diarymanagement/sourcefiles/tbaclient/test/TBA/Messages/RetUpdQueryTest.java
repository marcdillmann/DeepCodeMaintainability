/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import junit.framework.TestCase;
import TBA.Exceptions.MessagesException;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author cs321tx2
 */
public class RetUpdQueryTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public RetUpdQueryTest(String testName) {
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
    * Test of Get method, of class RetUpdQuery.
    * @throws Exception
    */
   public void testGet() throws Exception
   {
     //stupidest longest test case ever
      System.out.println("Get");
      String dummy = "";

      RetUpdQuery setInstance = new RetUpdQuery();
      RetUpdQuery getInstance = new RetUpdQuery();

      //getDiaryResponse vars
      int expDiaryID = 12;
      String expDiaryName = "aDiaryName";
      int expPermissions = 444;
      int expRevNo = 1;

      //int expTotalEntries = 3;
      int resTotalEntries;

      Vector<DiaryEntry> entryRecords = new Vector<DiaryEntry>();
      Vector<DiaryEntry> resEntryRecords = new Vector<DiaryEntry>();


      //diary vars
      int expEntryIDRec = 20;
      Calendar expStartRec = Calendar.getInstance();
      Calendar expEndRec = Calendar.getInstance();
      char expOwnerFlagRec = 'Y';
      char expLockedFlagRec = 'Y';
      String expUserRec = "bob";
      String expTitleRec = "title";
      String BodyRec = "superMeetinglify";

      DiaryEntry setDiaryEntry = new DiaryEntry();

      setDiaryEntry.setEntryID(expEntryIDRec);
      setDiaryEntry.setStart(expStartRec.getTimeInMillis());
      setDiaryEntry.setEnd(expEndRec.getTimeInMillis());
      setDiaryEntry.setOwnerFlag(expOwnerFlagRec);
      setDiaryEntry.setLockedFlag(expLockedFlagRec);
      setDiaryEntry.setCreatingUser(expUserRec);
      setDiaryEntry.setTitle(expTitleRec);
      setDiaryEntry.setBody(BodyRec);

      entryRecords.add(setDiaryEntry);

      try
      {
         setInstance.SetDiaryID(expDiaryID);
         setInstance.setDiaryName(expDiaryName);
         setInstance.setPermissions(expPermissions);
         setInstance.setRevisionNo(expRevNo);
         setInstance.setNumberOfEntries(entryRecords.size());
         setInstance.setEntries(entryRecords);

         getInstance.Set(setInstance.Get(""));
         //System.out.println("zxcvbnm,." + setInstance.Get(""));
         //System.out.println("fghjlkgfhjfgsdhjklgsfdhjklgsdfhjklfgdshkljgfdhjklfgdjhkgdfj");
    /*
         resDiaryID = getInstance.GetDiaryID();
         resDiaryName = getInstance.getDiaryName();
         resPermissions = getInstance.getPermissions();
         resRevNo = getInstance.getRevisionNo();
         resTotalEntries = getInstance.getNumberOfEntries();
         resEntryRecords = getInstance.getEntries();

         assertEquals(expDiaryID, resDiaryID);
         assertEquals(expDiaryName, resDiaryName);
         assertEquals(expPermissions,resPermissions);
         assertEquals(expRevNo,resRevNo);
         assertEquals(entryRecords.size(),resTotalEntries);
*/
         /*boolean result = entryRecords.equals((Vector<DiaryEntry>)resEntryRecords);
         assertEquals(true,result);*/

      } catch (MessagesException ex) {
          System.out.println(ex.getMessage());
          fail("Unexpected throw!");
      }


        try
            {
               getInstance = new RetUpdQuery();

               String failResult = getInstance.Get("");

               fail("Expected throw, but did not receive!");
            }
        catch (MessagesException ex)
        {
        }
   }

   /**
    * Test of GetDiaryID method, of class RetUpdQuery.
    */
   public void testGetDiaryID()
   {
      System.out.println("GetDiaryID");
      RetUpdQuery instance = new RetUpdQuery();
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
    * Test of getDiaryName method, of class RetUpdQuery.
    */
   public void testGetDiaryName()
   {
      System.out.println("getDiaryName");
      RetUpdQuery instance = new RetUpdQuery();
      String expResult = "";

      try
      {
         instance.setDiaryName(expResult);
         String result = instance.getDiaryName();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setDiaryName(null);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getPermissions method, of class RetUpdQuery.
    * @throws Exception
    */
   public void testGetPermissions() throws Exception
   {
      System.out.println("getPermissions");
      RetUpdQuery instance = new RetUpdQuery();

      int expResult = 511;

      try
      {
         instance.setPermissions(expResult);
         int result = instance.getPermissions();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setPermissions(777);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getRevisionNo method, of class RetUpdQuery.
    */
   public void testGetRevisionNo()
   {
      System.out.println("getRevisionNo");
      RetUpdQuery instance = new RetUpdQuery();
      int expResult = 0;
      try
      {
         instance.setRevisionNo(expResult);
         int result = instance.getRevisionNo();

         assertEquals(expResult, result);

      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setRevisionNo(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

   /**
    * Test of getNumberOfEntries method, of class RetUpdQuery.
    */
   public void testGetNumberOfEntries()
   {
      System.out.println("getNumberOfEntries");
      RetUpdQuery instance = new RetUpdQuery();
      int expResult = 20;

      try
      {
         instance.setNumberOfEntries(expResult);
         int result = instance.getNumberOfEntries();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setNumberOfEntries(-1);
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
   }

}
