
package parser.statements;

import parser.expressions.Expression;

public class ForStmt implements Statement {
    public final Statement initializer;
    public final Expression condition;
    public final Statement incrementor;
    public final BlockStmt body;
    
    public ForStmt(Statement initializer, Expression condition, Statement incrementor, BlockStmt body) {
        this.initializer = initializer;
        this.condition = condition;
        this.incrementor = incrementor;
        this.body = body;
    }
    
    public String toString() {
        return "ForStmt(" + initializer + ", " + condition + ", " + incrementor + ", " + body + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}