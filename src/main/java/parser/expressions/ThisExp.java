package parser.expressions;

public class ThisExp implements Expression {
    
    public String toString() {
        return "ThisExp";
    }

    public boolean equals(Expression exp) {
        return exp instanceof ThisExp;
    }
}
