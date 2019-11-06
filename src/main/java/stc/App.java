package stc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        String nodeKey = "some_key";
        int nodeValue = 42;
        Node nextNode = new Node("irrelevant_key", 427, null);
        Node testSubject = new Node(nodeKey, nodeValue, nextNode);
        Set<String> hOutput = new HashSet<>();
        hOutput.add("value");
        hOutput.add("next");

        cleanup(testSubject, new HashSet<String>(), hOutput);
    }

    /**
     * - если в списках fieldsToCleanup или fieldsToOutput содержится поле, которого нет в объекте, падать с IllegalArgumentException, оставив объект неизменным
     * - изменять значения в полях по списку fieldsToCleanup
     *   - изменять значения полей примитивных типов на значения по умолчанию
     *   - изменять на null значения полей других типов
     * - выводить значения в полях по списку fieldsToOutput
     *   - выводить String.valueOf для полей примитивных типов
     *   - выводить toString для полей других типов
     * - если объект является реализацией интерфейса Map, то проделать аналогичные операции
     *   - для списка fieldsToCleanup удалить ключи из мапы
     *   - для fieldsToOutput вывести в консоль значения, хранящиеся в мапе
     */
    static void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalArgumentException {
        checkIfObjectHasAllFields(object, fieldsToCleanup);
        checkIfObjectHasAllFields(object, fieldsToOutput);

        try {
            cleanupObject(object, fieldsToCleanup);
            outputFields(object, fieldsToOutput);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void checkIfObjectHasAllFields(Object object, Set<String> fieldSet) throws IllegalArgumentException {
        Class clazz = object.getClass();

        try {
            if (object instanceof Map) {
                Map objectAsMap = (Map) object;
                for (String potentialFieldName : fieldSet)
                    if (!objectAsMap.containsKey(potentialFieldName))
                        throw new NoSuchFieldException();
            } else
                for (String potentialFieldName : fieldSet)
                    clazz.getDeclaredField(potentialFieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }

    private static void cleanupObject(Object object, Set<String> fieldsToClean) throws IllegalAccessException {
        if (object instanceof Map) {
            Map objectAsMap = (Map) object;
            for (String fieldName : fieldsToClean)
                objectAsMap.remove(fieldName);
        } else {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (fieldsToClean.contains(field.getName())) {
                    if (Modifier.isPrivate(field.getModifiers()))
                        field.setAccessible(true);

                    cleanupField(object, field);
                }
            }
        }
    }

    private static void cleanupField(Object object, Field field) throws IllegalAccessException {
        switch (field.getType().getName()) { // im deeply unsatisfied with this solution, but it works and that's all that matters
            case "boolean":
                field.setBoolean(object, false);
                break;
            case "char":
                field.setChar(object, '\u0000');
                break;
            case "byte":
                field.setByte(object, (byte) 0);
                break;
            case "short":
                field.setShort(object, (short) 0);
                break;
            case "int":
                field.setInt(object, 0);
                System.out.println("int");
                break;
            case "long":
                field.setLong(object, 0L);
                break;
            case "float":
                field.setFloat(object, 0f);
                break;
            case "double":
                field.setDouble(object, 0d);
                break;
            default:
                field.set(object, null);
                break;
        }
    }

    /**
     * why should i use .toString() for non-primitive types
     * when function String.valueOf() itself calls .toString()
     * if argument is of non-primitive type?
     *
     * @see java.lang.String#valueOf(Object obj)
     */
    private static void outputFields(Object object, Set<String> fieldsToOutput) throws IllegalAccessException {
        if (object instanceof Map) {
            Map objectAsMap = (Map) object;
            for (String fieldName : fieldsToOutput)
                System.out.println(objectAsMap.get(fieldName));
        } else {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (fieldsToOutput.contains(field.getName())) {
                    if (Modifier.isPrivate(field.getModifiers()))
                        field.setAccessible(true);

                    System.out.println(field.get(object));
                }
            }
        }
    }
}
