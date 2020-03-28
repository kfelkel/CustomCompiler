package parser.expressions;

public class MultiplicationExp implements Expression {

    public final Expression exp1;
    public final Expression exp2;

    public MultiplicationExp(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public String toString() {
        return "MultiplicationExp(" + exp1 + ", " + exp2 + ")";
    }

    public boolean equals(Expression exp) {
        if(exp instanceof MultiplicationExp){
            MultiplicationExp castExp = (MultiplicationExp) exp;
            return (castExp.exp1 == exp1 && castExp.exp2 == exp2);
        }
        else
            return false;
    }
}
