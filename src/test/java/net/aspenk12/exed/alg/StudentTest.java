package net.aspenk12.exed.alg;

import net.aspenk12.exed.util.BadEmailException;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentTest {

    @Test
    public void testFindID() {
        int id = Student.findID("alexandera11325@aspenk12.net");

        assertEquals(11325, id);
    }

    @Test
    public void testFailedFindID() {
        try {
            Student.findID("janderson@aspenk12.net");
            fail("no email exception was thrown");
        } catch (BadEmailException e){
            //an exception was found, yay!
        }
    }
}