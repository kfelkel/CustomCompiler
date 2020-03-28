package parser.expressions;

public class NewExp implements Expression {
    
    public String toString() {
        return "NewExp";
    }

    public boolean equals(Expression exp) {
        return exp instanceof NewExp;
    }
}