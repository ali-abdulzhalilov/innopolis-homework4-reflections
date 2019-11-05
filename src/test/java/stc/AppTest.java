package stc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;

public class AppTest 
{
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testHelloWorld() {
        App.cleanup("Practically, anything", new HashSet<String>(), new HashSet<String>());
        assertEquals("Hello, World!\r\n", outContent.toString()); // don't forget about windows line ending!
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }
}
