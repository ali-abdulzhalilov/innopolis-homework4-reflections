package stc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

public class AppTest 
{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testOutput() {
        App.cleanup("Practically, anything", new HashSet<String>(), new HashSet<String>());
        assertEquals("Hello, World!\r\n", outContent.toString()); // don't forget about windows line endings!
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNonexistentCleanupField() throws IllegalArgumentException {
        Set<String> hCleanup = new HashSet<>();
        hCleanup.add("width");
        App.cleanup("String, for example", hCleanup, new HashSet<String>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnNonexistentOutputField() throws IllegalArgumentException {
        Set<String> hOutput = new HashSet<>();
        hOutput.add("height");
        App.cleanup("String, for example", new HashSet<String>(), hOutput);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }
}
