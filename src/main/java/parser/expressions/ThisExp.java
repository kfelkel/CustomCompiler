package parser.expressions;

public class ThisExp implements Expression {
    
    public String toString() {
        return "ThisExp";
    }

    public boolean equals(final Expression exp) {
        return exp instanceof ThisExp;
    }
}
