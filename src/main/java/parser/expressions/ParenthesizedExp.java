package parser.expressions;

import java.util.List;

public class ParenthesizedExp implements Expression {
    public final List<Expression> body;

    public ParenthesizedExp(List<Expression> body) {
        this.body = body;
    }

    public String toString() {
        return "ParenthesizedExp( " + body + ")";  
    }

    public boolean equals(Expression exp) {
        if(exp instanceof ParenthesizedExp){
            ParenthesizedExp castExp = (ParenthesizedExp) exp;
            return castExp.body == body;
        }
        else
            return false;
    }
}