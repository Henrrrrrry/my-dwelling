package helper_classes_and_methods.parser;

/**
 * Author: Xinrui Zhang u7728429:implemented parser function
 */
public class OrExp extends Expression {
    private Expression left;
    private Expression right;

    public OrExp() {
    }

    public OrExp(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }
}
