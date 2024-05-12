package helper_classes_and_methods.parser;

/**
 * Author: Xinrui Zhang:implemented parser function
 */

public class Condition extends Expression {
    private String key;
    private String value;

    public Condition() {
    }

    public Condition(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
