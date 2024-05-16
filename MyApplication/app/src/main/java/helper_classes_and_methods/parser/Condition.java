package helper_classes_and_methods.parser;

/**
 * Author: Xinrui Zhang u7728429:implemented parser function
 */

public class Condition extends Expression {
    private String key;
    private String value;


    public Condition(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }


    public String getValue() {
        return value;
    }


}
