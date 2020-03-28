package parser.expressions;
public class IntegerExp implements Expression{
    public final int value;

    public IntegerExp(int value) {
        this.value = value;
    }

    public String toString() {
        return "IntegerExp(" + value + ")";
    }

    public boolean equals(Expression exp){
        if(exp instanceof IntegerExp){
            IntegerExp castExp = (IntegerExp) exp;
            return (castExp.value == value);
        }
        else
            return false;
    }
}