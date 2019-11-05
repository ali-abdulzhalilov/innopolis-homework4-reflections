package stc;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        cleanup("Practically, anything", new HashSet<String>(), new HashSet<String>());
    }

    /**
     * - если в списках fieldsToCleanup или fieldsToOutput содержится поле, которого нет в объекте, падать с IllegalArgumentException, оставив объект неизменным
     * TODO - изменять значения в полях по списку fieldsToCleanup
     * TODO  - изменять значения полей примитивных типов на значения по умолчанию
     * TODO  - изменять на null значения полей других типов
     * TODO - выводить значения в полях по списку fieldsToOutput
     * TODO  - выводить String.valueOf для полей примитивных типов
     * TODO  - выводить toString для полей других типов
     * TODO - если объект является реализацией интерфейса Map, то проделать аналогичные операции - для списка fieldsToCleanup удалить ключи из мапы, для fieldsToOutput вывести в консоль значения, хранящиеся в мапе.
     */
    static void cleanup(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalArgumentException {
        checkIfObjectHasAllFields(object, fieldsToCleanup);
        checkIfObjectHasAllFields(object, fieldsToOutput);

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
}
