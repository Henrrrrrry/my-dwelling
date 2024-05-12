package helper_classes_and_methods.parser;

/**
 * Author: Xinrui Zhang:implemented parser function
 */
public class NotExp extends Expression {
    private Expression expression;

    public NotExp() {
    }

    public NotExp(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
