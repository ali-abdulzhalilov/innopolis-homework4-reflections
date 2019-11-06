package stc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        class Node {
            private String key;
            private int value;
            private Node next;

            private Node(String key, int value, Node next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }

            public String getKey() {
                return this.key;
            }

            public int getValue() {
                return this.value;
            }
        }

        String nodeKey = "some_key";
        int nodeValue = 42;
        Node nextNode = new Node("irrelevant_key", 427, null);
        Node testSubject = new Node(nodeKey, nodeValue, nextNode);
        Set<String> hCleanup = new HashSet<>();
        hCleanup.add("value");
        hCleanup.add("next");

        cleanup(testSubject, hCleanup, new HashSet<String>());
    }

    /**
     * - если в списках fieldsToCleanup или fieldsToOutput содержится поле, которого нет в объекте, падать с IllegalArgumentException, оставив объект неизменным
     * - изменять значения в полях по списку fieldsToCleanup
     *   - изменять значения полей примитивных типов на значения по умолчанию
     *   - изменять на null значения полей других типов
     * TODO - выводить значения в полях по списку fieldsToOutput
     * TODO   - выводить String.valueOf для полей примитивных типов
     * TODO   - выводить toString для полей других типов
     * TODO - если объект является реализацией интерфейса Map, то проделать аналогичные операции
     * TODO   - для списка fieldsToCleanup удалить ключи из мапы
     * TODO   - для fieldsToOutput вывести в консоль значения, хранящиеся в мапе
     */
    static void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalArgumentException {
        checkIfObjectHasAllFields(object, fieldsToCleanup);
        checkIfObjectHasAllFields(object, fieldsToOutput);

        try {
            cleanupObject(object, fieldsToCleanup);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("Hello, World!");
    }

    private static void checkIfObjectHasAllFields(Object object, Set<String> fieldSet) throws IllegalArgumentException {
        Class clazz = object.getClass();

        try {
            for (String potentialFieldName : fieldSet)
                clazz.getDeclaredField(potentialFieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    private static void cleanupObject(Object object, Set<String> fieldToClean) throws IllegalAccessException {
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {

            if (fieldToClean.contains(field.getName())) {
                if (Modifier.isPrivate(field.getModifiers()))
                    field.setAccessible(true);

                cleanupField(object, field);
            }
        }
    }

    private static void cleanupField(Object object, Field field) throws IllegalAccessException {
        switch (field.getType().getName()) { // im deeply unsatisfied with this solution, but it works and that's all that matters
            case "boolean": field.setBoolean(object, false); break;
            case "char": field.setChar(object, '\u0000'); break;
            case "byte": field.setByte(object, (byte)0); break;
            case "short": field.setShort(object, (short)0); break;
            case "int": field.setInt(object, 0); System.out.println("int"); break;
            case "long": field.setLong(object, 0L); break;
            case "float": field.setFloat(object, 0f); break;
            case "double": field.setDouble(object, 0d); break;
            default: field.set(object, null); break;
        }
    }
}
