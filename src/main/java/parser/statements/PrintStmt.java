package parser.statements;
import parser.expressions.Expression;
public class PrintStmt implements Statement {
    public final Expression output;

    public PrintStmt(Expression output) {
        this.output = output;
    }

    public String toString() {
        return "PrintStmt(" + output + ")";
    }

    public boolean equals(Statement stmt) {
        return toString().equals(stmt.toString());
    }
}