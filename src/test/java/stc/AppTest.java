package stc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

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

    @Test
    public void testCleanup() {
        class Node {
            private String key;
            private int value;
            private Node next;

            private Node(String key, int value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }

            private String getKey() {
                return this.key;
            }

            private int getValue() {
                return this.value;
            }
        }

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
        class someClass {
            private boolean booleanField = true;
            private char charField = '?';
            private byte byteField = (byte)231;
            private short shortField = (short)4416;
            private int intField = 6112019;
            private long longField = 3L;
            private float floatField = 1.1f;
            private double doubleField = 1.2d;
            private String stringField = "as good as any other non-primitive type";
        }

        someClass someObject = new someClass();

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
        assertEquals((byte)0, someObject.byteField);
        assertEquals((short)0, someObject.shortField);
        assertEquals(0, someObject.intField);
        assertEquals(0L, someObject.longField);
        assertEquals(0f, someObject.floatField, 0.0001f);
        assertEquals(0d, someObject.doubleField, 0.0001d);
        assertNull(someObject.stringField);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }
}
