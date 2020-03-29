package parser.expressions;

public class VariableExp implements Expression {

    public final String name;

    public VariableExp(final String name) {
        this.name = name;
    }

    public String toString() {
        return "VariableExp(" + name + ")";     
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}