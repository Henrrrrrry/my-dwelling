package helper_classes_and_methods.parser;


public enum TokenType {
    AND,
    OR,
    NOT,
    LEFT_BRA,
    RIGHT_BRA,
    COLON,
    COMMA,
    CONDITION;

    public static TokenType convertString(String type) {
        switch (type) {
            case "and":
                return AND;
            case "or":
                return OR;
            case "not":
                return NOT;
            case ",":
                return COMMA;
            case ":":
                return COLON;
            case "(":
                return LEFT_BRA;
            case ")":
                return RIGHT_BRA;
            default:
                return CONDITION;
        }

    }
}
