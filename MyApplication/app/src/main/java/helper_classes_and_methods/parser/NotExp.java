package helper_classes_and_methods.parser;

/**
 * Author: Xinrui Zhang u7728429:implemented parser function
 */
public class NotExp extends Expression {
    private Expression expression;


    public NotExp(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }


}
