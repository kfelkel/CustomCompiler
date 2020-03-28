package parser.statements;

import parser.expressions.Expression;

public class VariableDeclarationStmt implements Statement {
    public final String type;
    public final String name;
    public final Expression value;

    public VariableDeclarationStmt(String type, String name) {
        this.type = type;
        this.name = name;
        this.value = null;
    }

    public VariableDeclarationStmt(String type, String name, Expression value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String toString() {
        return "VariableDeclarationStmt(" + type + ", " + name + ")";
    }
    
    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}