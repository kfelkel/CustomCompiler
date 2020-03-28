package parser.statements;

public class WhileStmt implements Statement {

    public final Statement body;

    public WhileStmt(Statement body) {
        this.body = body;
    }

    public String toString() {
        return "WhileStmt(" + body + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}