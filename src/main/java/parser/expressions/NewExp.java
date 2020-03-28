package parser.expressions;

public class NewExp implements Expression {

    public final Expression exp;

    public NewExp(Expression exp) {
        this.exp = exp;
    }
    
    public String toString() {
        return "NewExp(" + exp + ")";
    }

    public boolean equals(Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}