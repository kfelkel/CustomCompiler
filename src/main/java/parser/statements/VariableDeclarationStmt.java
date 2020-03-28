package parser.statements;

public class VariableDeclarationStmt implements Statement {
    public final String type;
    public final String name;

    public VariableDeclarationStmt(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String toString() {
        return "VariableDeclarationStmt(" + type + ", " + name + ")";
    }
    
    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}