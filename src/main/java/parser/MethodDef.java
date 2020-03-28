package parser;

import parser.statements.Statement;
import parser.statements.VariableDeclarationStmt;

public class MethodDef {

    public final String type;
    public final String name;
    public final VariableDeclarationStmt parameters;
    public final Statement body;

    public MethodDef(String type, String name, VariableDeclarationStmt parameters, Statement body) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
    
    public String toString() {
        return "MethodDef(" + type + ", " + name + ", " + parameters + ", " + body + ")";
    }
    
    public boolean equals(MethodDef def) {
        return (toString()).equals(def.toString());
    }

}