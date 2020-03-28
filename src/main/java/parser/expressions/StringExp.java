package parser.expressions;
public class StringExp implements Expression{
    public final String value;

    public StringExp(String value) {
        this.value = value;
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }

    public boolean equals(Expression exp){
        if(exp instanceof StringExp){
            StringExp castExp = (StringExp) exp;
            return (castExp.value == value);
        }
        else
            return false;
    }
}