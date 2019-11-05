package stc;

import java.util.HashSet;
import java.util.Set;

public class App
{
    public static void main( String[] args )
    {
        cleanup("Practically, anything", new HashSet<String>(), new HashSet<String>());
    }

    static void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) {
        System.out.println("Hello, World!");
    }
}
