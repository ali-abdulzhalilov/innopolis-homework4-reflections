package stc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class AppTest {
    private class superClass {
        private boolean booleanField = true;
        private char charField = '?';
        private byte byteField = (byte) 231;
        private short shortField = (short) 4416;
        private int intField = 6112019;
        private long longField = 3L;
        private float floatField = 1.1f;
        private double doubleField = 1.2d;
        private String stringField = "as good as any other non-primitive type";
    }

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    // testing intercepting output
    @Test
    public void testLookingAtSout() {
        App.cleanup("Practically, anything", new HashSet<String>(), new HashSet<String>());
        assertEquals("", outContent.toString());
    }
    /* -------------------- */

    // testing throwing exceptions
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
    /* -------------------- */

    // testing cleanup
    @Test
    public void testCleanup() {
        String nodeKey = "some_key";
        int nodeValue = 42;
        Node nextNode = new Node("irrelevant_key", 427, null);
        Node testSubject = new Node(nodeKey, nodeValue, nextNode);
        assertEquals(nodeKey, testSubject.getKey());
        assertEquals(nodeValue, testSubject.getValue());
        assertEquals(nextNode, testSubject.next);

        Set<String> hCleanup = new HashSet<>();
        hCleanup.add("value");
        hCleanup.add("next");
        App.cleanup(testSubject, hCleanup, new HashSet<String>());
        assertEquals(nodeKey, testSubject.getKey());
        assertEquals(0, testSubject.getValue());
        assertNull(testSubject.next);
    }

    @Test
    public void testCleanupForAllTypes() {
        superClass someObject = new superClass();

        HashSet<String> hCleanup = new HashSet<String>() {{
            add("booleanField");
            add("charField");
            add("byteField");
            add("shortField");
            add("intField");
            add("longField");
            add("floatField");
            add("doubleField");
            add("stringField");
        }};

        App.cleanup(someObject, hCleanup, new HashSet<String>());

        assertFalse(someObject.booleanField);
        assertEquals('\u0000', someObject.charField);
        assertEquals((byte) 0, someObject.byteField);
        assertEquals((short) 0, someObject.shortField);
        assertEquals(0, someObject.intField);
        assertEquals(0L, someObject.longField);
        assertEquals(0f, someObject.floatField, 0.0001f);
        assertEquals(0d, someObject.doubleField, 0.0001d);
        assertNull(someObject.stringField);
    }
    /* -------------------- */

    // testing output
    @Test
    public void testOutput() {
        String nodeKey = "some_key";
        int nodeValue = 42;
        Node nextNode = new Node("irrelevant_key", 427, null);
        Node testSubject = new Node(nodeKey, nodeValue, nextNode);
        assertEquals(nodeKey, testSubject.getKey());
        assertEquals(nodeValue, testSubject.getValue());
        assertEquals(nextNode, testSubject.next);

        Set<String> hOutput = new HashSet<>();
        hOutput.add("value");
        hOutput.add("next");
        App.cleanup(testSubject, new HashSet<String>(), hOutput);
        String output = outContent.toString();
        assertFalse(output.contains(nodeKey));
        assertTrue(output.contains(String.valueOf(42)));
        assertTrue(output.contains(nextNode.toString()));

        // values of an object shouldn't be changed
        assertEquals(nodeKey, testSubject.getKey());
        assertEquals(nodeValue, testSubject.getValue());
        assertEquals(nextNode, testSubject.next);
    }

    @Test
    public void testOutputForAllTypes() {
        superClass someObject = new superClass();

        HashSet<String> hOutput = new HashSet<String>() {{
            add("booleanField");
            add("charField");
            add("byteField");
            add("shortField");
            add("intField");
            add("longField");
            add("floatField");
            add("doubleField");
            add("stringField");
        }};

        App.cleanup(someObject, new HashSet<String>(), hOutput);

        String output = outContent.toString();
        assertTrue(output.contains(String.valueOf(someObject.booleanField)));
        assertTrue(output.contains(String.valueOf(someObject.charField)));
        assertTrue(output.contains(String.valueOf(someObject.byteField)));
        assertTrue(output.contains(String.valueOf(someObject.shortField)));
        assertTrue(output.contains(String.valueOf(someObject.intField)));
        assertTrue(output.contains(String.valueOf(someObject.longField)));
        assertTrue(output.contains(String.valueOf(someObject.floatField)));
        assertTrue(output.contains(String.valueOf(someObject.floatField)));
        assertTrue(output.contains(String.valueOf(someObject.stringField)));
    }
    /* -------------------- */
}