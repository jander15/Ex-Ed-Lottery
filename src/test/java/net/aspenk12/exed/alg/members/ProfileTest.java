package net.aspenk12.exed.alg.members;

import net.aspenk12.exed.alg.containers.Gender;
import net.aspenk12.exed.alg.containers.Grade;
import net.aspenk12.exed.util.CSV;
import net.aspenk12.exed.util.Warnings;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProfileTest {

    @Test
    public void testDuplicateLottoNumber() {
        Profile alex = new Profile(11325, "Alex", "Appleby", Gender.MALE, Grade.SENIOR, 10, 72, new ArrayList<>());

        assertEquals(0, Warnings.count());

        Profile jeremy = new Profile(12345, "Jeremy", "Martin", Gender.MALE, Grade.SENIOR, 9, 72, new ArrayList<>());

        assertEquals(1, Warnings.count());
    }

    /**
     * Creates example instances of Profile using test/resources/validatedtest.csv
     */
    public static void makeExampleProfiles(){
        File file = new File(StudentTest.class.getResource("/validatedtest.csv").getFile());
        Profile.createProfiles(new CSV(file));
    }
}