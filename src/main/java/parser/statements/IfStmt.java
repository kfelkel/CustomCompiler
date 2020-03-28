package parser.statements;

import parser.expressions.Expression;

public class IfStmt implements Statement {

    public final Expression condition;
    public final Statement trueBranch;

    public IfStmt(Expression condition, Statement trueBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
    }

    public String toString() {
        return "IfStmt(" + condition + ", " + trueBranch + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}
