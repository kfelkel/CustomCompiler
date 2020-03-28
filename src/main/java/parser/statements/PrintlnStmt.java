package parser.statements;

import parser.expressions.Expression;

public class PrintlnStmt implements Statement {
    public final Expression output;

    public PrintlnStmt(Expression output) {
        this.output = output;
    }

    public String toString() {
        return "PrintlnStmt(" + output + ")";
    }

    public boolean equals(Statement stmt) {
        return toString().equals(stmt.toString());
    }
}