package stc;

class Node {
    private String key;
    private int value;
    Node next;

    Node(String key, int value, Node next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    String getKey() {
        return this.key;
    }

    int getValue() {
        return this.value;
    }
}
