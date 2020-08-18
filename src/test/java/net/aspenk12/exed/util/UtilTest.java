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
            fail("no email exception was thrown");
        } catch (BadDataException e){
            //an exception was found, yay!
        }
    }

}