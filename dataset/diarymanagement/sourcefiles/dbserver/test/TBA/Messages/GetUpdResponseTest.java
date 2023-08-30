/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package TBA.Messages;

import TBA.Exceptions.MessagesException;
import java.util.Calendar;
import junit.framework.TestCase;
import java.util.ArrayList;

/**
 *
 * @author cs321tx2
 */
public class GetUpdResponseTest extends TestCase {
    
    /**
     *
     * @param testName
     */
    public GetUpdResponseTest(String testName) {
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
     * Test of GetUpdResponseLen method, of class GetUpdResponse.
     */
/*    public void testGetUpdResponseLen() {
        System.out.println("GetUpdResponseLen");
        GetUpdResponse instance = new GetUpdResponse();
        int expResult = 0;
        int result = instance.GetUpdResponseLen();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of Get method, of class GetUpdResponse.
     * @throws Exception
     */
    public void testGet() throws Exception {

  //stupidest longest test case ever
      System.out.println("Get");
      String dummy = "";

      GetUpdResponse setInstance = new GetUpdResponse();
      GetUpdResponse getInstance = new GetUpdResponse();

      //getDiaryResponse vars
      String expDiaryName = "aDiaryName";
      String resDiaryName;

      char expOwnerFlag = 'Y';
      char resOwnerFlag;

      int expPermissions = 444;
      int resPermissions;

      int expRevNo = 1;
      int resRevNo;

      //int expTotalEntries = 3;
      int resTotalEntries;

      ArrayList<DiaryEntry> entryRecords = new ArrayList<DiaryEntry>();
      ArrayList<DiaryEntry> resEntryRecords = new ArrayList<DiaryEntry>();


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
         setInstance.setDiaryName(expDiaryName);
         setInstance.setOwnerFlag(expOwnerFlag);
         setInstance.setPermissions(expPermissions);
         setInstance.setRevisionNo(expRevNo);
         setInstance.setNumberOfEntries(entryRecords.size());
         setInstance.setEntries(entryRecords);

         getInstance.Set(setInstance.Get(""));

         resDiaryName = getInstance.getDiaryName();
         resOwnerFlag = getInstance.getOwnerFlag();
         resPermissions = getInstance.getPermissions();
         resRevNo = getInstance.getRevisionNo();
         resTotalEntries = getInstance.getNumberOfEntries();
         resEntryRecords = getInstance.getEntries();

         assertEquals(expDiaryName, resDiaryName);
         assertEquals(expOwnerFlag, resOwnerFlag);
         assertEquals(expPermissions,resPermissions);
         assertEquals(expRevNo,resRevNo);
         assertEquals(entryRecords.size(),resTotalEntries);

         /*boolean result = entryRecords.equals((ArrayList<DiaryEntry>)resEntryRecords);
         assertEquals(true,result);*/

      } catch (MessagesException ex) {
          fail("Unexpected throw!");
      }


        try
            {
               getInstance = new GetUpdResponse();

               String failResult = getInstance.Get("");

               fail("Expected throw, but did not receive!");
            }
        catch (MessagesException ex)
        {
        }
    }

 
    /**
     * Test of getDiaryName method, of class GetUpdResponse.
     */
    public void testGetDiaryName() {
        System.out.println("getDiaryName");
        GetUpdResponse instance = new GetUpdResponse();
        String expResult = "bob";

        try
        {
           instance.setDiaryName(expResult);
           String result = instance.getDiaryName();

           assertEquals(expResult, result);
        } catch (MessagesException ex)
        {
           fail("Unexpected throw!");
        }

        // TODO: go see what thsi try does
        try
        {
         instance.setDiaryName(null);
         fail("Expected throw, but did not receive!");
        }
        catch (MessagesException ex)
        { }

    }


    /**
     * Test of getNumberOfEntries method, of class GetUpdResponse.
     */
    public void testGetNumberOfEntries() {
         System.out.println("getNumberOfEntries");
         GetUpdResponse instance = new GetUpdResponse();
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

    /**
     * Test of getOwnerFlag method, of class GetUpdResponse.
     */
    public void testGetOwnerFlag() {
      System.out.println("getOwnerFlag");
      GetUpdResponse instance = new GetUpdResponse();
      char expResult = 'Y';

      try
      {
         instance.setOwnerFlag(expResult);
         char result = instance.getOwnerFlag();

         assertEquals(expResult, result);

         expResult = 'N';
         instance.setOwnerFlag(expResult);
         result = instance.getOwnerFlag();

         assertEquals(expResult, result);
      }
      catch (MessagesException ex)
      {
         fail("Unexpected throw!");
      }

      try
      {
         instance.setOwnerFlag('M');
         fail("Expected throw, but did not receive!");
      }
      catch (MessagesException ex)
      { }
    }



    /**
     * Test of getPermissions method, of class GetUpdResponse.
     */
    public void testGetPermissions() {
      System.out.println("getPermissions");
      GetUpdResponse instance = new GetUpdResponse();
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
     * Test of getRevisionNo method, of class GetUpdResponse.
     */
    public void testGetRevisionNo() {
      System.out.println("GetRevNum/SetRevNum");
      GetUpdResponse instance = new GetUpdResponse();

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
}