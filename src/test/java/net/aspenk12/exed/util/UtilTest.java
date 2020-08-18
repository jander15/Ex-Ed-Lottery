package net.aspenk12.exed.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testGetIDFromEmail() {
        int id = Util.getIDFromEmail("alexandera11325@aspenk12.net");
        assertEquals(11325, id);
    }

    @Test
    public void testFailedFindID() {
        try {
            Util.getIDFromEmail("janderson@aspenk12.net");
            fail("expected a BadDataException");
        } catch (BadDataException e){
            //an exception was found, yay!
        }
    }

    @Test
    public void testExtractCourseID() {
        String id = Util.extractCourseID("Soul <SS> Surfers");

        assertEquals("SS", id);

    }

    @Test
    public void testFailedExtractCourseID() {
        try {
            Util.extractCourseID("Soul Surfers");
            fail("expected a BadDataException");
        } catch (BadDataException e){
            //an exception was found, yay!
        }
    }

}