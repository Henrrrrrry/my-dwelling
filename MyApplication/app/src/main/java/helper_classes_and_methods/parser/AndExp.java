package helper_classes_and_methods.parser;

public class AndExp extends Expression {

    private Expression left;
    private Expression right;

    public AndExp() {
    }

    public AndExp(Expression left, Expression right) {
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
