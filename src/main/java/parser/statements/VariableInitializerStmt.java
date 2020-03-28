package parser.statements;

import parser.expressions.Expression;

public class VariableInitializerStmt implements Statement {
    public final String type;
    public final String name;
    public final Expression value;

    public VariableInitializerStmt(String type, String name, Expression value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String toString() {
        return "VariableInitializerStmt(" + type + ", " + name + ", " + value + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}