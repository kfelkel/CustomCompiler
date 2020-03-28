package parser.statements;

import parser.expressions.Expression;

public class IfElseStmt implements Statement {

    public final Expression condition;
    public final Statement trueBranch;
    public final Statement falseBranch;

    public IfElseStmt(Expression condition, Statement trueBranch, Statement falseBranch) {

        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;

    }

    public String toString() {
        return "IfElseStmt("+ condition + ", " + trueBranch + ", " + falseBranch + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
} 