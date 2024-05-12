package helper_classes_and_methods.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Xinrui Zhang:implemented tokenizer function
 */

public class Tokenizer {

    private List<Token> tokenList;

    public Tokenizer(String expressionStr) {
        tokenList = new ArrayList<>();
        parseToken(expressionStr);
    }
    /**
     * Author: Xinrui Zhang
     * Description: parse token from given format string
     * @param input: the text string
     */
    private void parseToken(String input) {
        input = input.trim();
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if (c == '(' || c == ')' || c == ',' || c == ':') {
                if (sb.length() > 0) {
                    TokenType type = TokenType.convertString(sb.toString().trim());
                    tokenList.add(new Token(sb.toString().trim(), type));
                    sb = new StringBuilder();
                }
            }
            if (c == '(') {
                tokenList.add(new Token("(", TokenType.LEFT_BRA));
            } else if (c == ')') {
                tokenList.add(new Token(")", TokenType.RIGHT_BRA));
            } else if (c == ',') {
                tokenList.add(new Token(",", TokenType.COMMA));
            } else if (c == ':') {
                tokenList.add(new Token(":", TokenType.COLON));
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            TokenType type = TokenType.convertString(sb.toString().trim());
            tokenList.add(new Token(sb.toString().trim(), type));
        }
    }

    public List<Token> getTokenList() {
        return tokenList;
    }
}
