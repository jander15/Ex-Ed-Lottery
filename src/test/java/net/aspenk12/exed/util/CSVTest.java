package net.aspenk12.exed.util;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class CSVTest {
    @Test
    public void test4x4CSV() {
        //tests the file import on the 4x4 csv in the resources directory
        File file = new File(this.getClass().getResource("/4x4.csv").getFile());
        CSV csv = new CSV(file);

        assertEquals("1", csv.get(0,0));
        assertEquals("2", csv.get(0,1));
        assertEquals("3", csv.get(1,0));
        assertEquals("4", csv.get(1,1));
    }
}