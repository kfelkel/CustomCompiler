
package parser.expressions;
public class MethodCallExp implements Expression{
    public final String type;
    public final String name;

    public MethodCallExp(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String toString() {
        return "MethodCallExp(" + type + ", " + name + ")";     
    }

    public boolean equals(Expression exp) {
        if(exp instanceof MethodCallExp){
            MethodCallExp castExp = (MethodCallExp) exp;
            return (castExp.type == type && castExp.name == name);
        }
        else
            return false;
    }
}