package parser.expressions;

public class LessThanOrEqualExp implements Expression {

    public final Expression exp1;
    public final Expression exp2;

    public LessThanOrEqualExp(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public String toString() {
        return "LessThanOrEqualExp(" + exp1 + ", " + exp2 + ")";
    }

    public boolean equals(Expression exp) {
        if (exp instanceof LessThanOrEqualExp) {
            LessThanOrEqualExp castExp = (LessThanOrEqualExp) exp;
            return (castExp.exp1 == exp1 && castExp.exp2 == exp2);
        } else
            return false;
    }

}