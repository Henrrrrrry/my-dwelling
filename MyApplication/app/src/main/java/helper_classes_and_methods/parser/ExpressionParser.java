package helper_classes_and_methods.parser;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: Xinrui Zhang:implemented parser function
 */

public class ExpressionParser {

    private Expression expression;

    public ExpressionParser(String expr) throws IllegalArgumentException {
        Tokenizer tokenizer = new Tokenizer(expr);
        this.expression = parseExpression(tokenizer.getTokenList());
    }

    public Expression getExpression() {
        return expression;
    }
    /**
     * Author: Xinrui Zhang
     * Description: parse expression from given token list
     * @param tokenList: the current token
     */
    private Expression parseExpression(List<Token> tokenList) throws IllegalArgumentException {
        if (tokenList == null || tokenList.isEmpty()) {
            return null;
        }
        if (tokenList.size() < 3) {
            throw new IllegalArgumentException("Invalid Expression");
        } else if (tokenList.size() == 3) {
            if (tokenList.get(0).getType() == TokenType.CONDITION
                    && tokenList.get(1).getType() == TokenType.COLON
                    && tokenList.get(2).getType() == TokenType.CONDITION) {
                return new Condition(tokenList.get(0).getContent(), tokenList.get(2).getContent());
            } else {
                throw new IllegalArgumentException("Invalid Expression");
            }
        } else {
            Token firstToken = tokenList.get(0);
            if ((firstToken.getType() != TokenType.AND
                    && firstToken.getType() != TokenType.OR
                    && firstToken.getType() != TokenType.NOT)
                    || tokenList.get(1).getType() != TokenType.LEFT_BRA
                    || tokenList.get(tokenList.size() - 1).getType() != TokenType.RIGHT_BRA) {
                throw new IllegalArgumentException("Invalid Expression");
            }
            if (firstToken.getType() == TokenType.NOT) {
                List<Token> notTokens = new ArrayList<>();
                for (int i = 2; i < tokenList.size() - 1; i++) {
                    notTokens.add(tokenList.get(i));
                }
                Expression notExpression = parseExpression(notTokens);
                return new NotExp(notExpression);
            } else {
                // left expression
                Expression leftExpr = null;
                List<Token> leftTokenList = new ArrayList<>();
                int idx = 2;
                int leftBra = 0;
                while (true) {
                    Token cur = tokenList.get(idx);
                    if (cur.getType() == TokenType.COMMA
                            && leftBra == 0) {
                        leftExpr = parseExpression(leftTokenList);
                        idx++;
                        break;
                    } else {
                        leftTokenList.add(cur);
                        if (cur.getType() == TokenType.LEFT_BRA) {
                            leftBra++;
                        }
                        if (cur.getType() == TokenType.RIGHT_BRA) {
                            leftBra--;
                        }
                        if (leftBra < 0) {
                            throw new IllegalArgumentException("Brackets do not match");
                        }
                        idx++;
                    }
                }
                // right expression
                Expression rightExpr = null;
                List<Token> rightTokenList = new ArrayList<>();
                for (int i = idx; i < tokenList.size() - 1; i++) {
                    rightTokenList.add(tokenList.get(i));
                }
                rightExpr = parseExpression(rightTokenList);
                if (firstToken.getType() == TokenType.AND) {
                    return new AndExp(leftExpr, rightExpr);
                } else {
                    return new OrExp(leftExpr, rightExpr);
                }
            }
        }
    }
}
