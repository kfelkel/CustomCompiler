package parser.expressions;

public class VariableExp implements Expression {
    public final String type;
    public final String name;

    public VariableExp(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String toString() {
        return "VariableExp(" + type + ", " + name + ")";     
    }

    public boolean equals(Expression exp) {
        if(exp instanceof VariableExp){
            VariableExp castExp = (VariableExp) exp;
            return (castExp.type == type && castExp.name == name);
        }
        else
            return false;
    }
}