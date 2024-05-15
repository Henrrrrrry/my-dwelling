package helper_classes_and_methods.parser;

/**
 * Author: Xinrui Zhang u7728429:implemented tokenizer function
 */

public class Token {
    private String content;
    private TokenType type;



    public Token(String content, TokenType type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TokenType getType() {
        return type;
    }
}
