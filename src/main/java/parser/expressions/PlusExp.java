package parser.expressions;

public class PlusExp implements Expression {

    public final Expression exp1;
    public final Expression exp2;

    public PlusExp(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public String toString() {
        return "PlusExp(" + exp1 + ", " + exp2 + ")";
    }

    public boolean equals(Expression exp) {
        if (exp instanceof PlusExp) {
            PlusExp castExp = (PlusExp) exp;
            return (castExp.exp1 == exp1 && castExp.exp2 == exp2);
        } else
            return false;
    }

}