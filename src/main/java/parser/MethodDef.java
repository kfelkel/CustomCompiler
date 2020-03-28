package parser;

import java.util.ArrayList;
import parser.statements.Statement;
import parser.statements.VariableDeclarationStmt;

public class MethodDef {

    public final String type;
    public final String name;
    public final ArrayList<VariableDeclarationStmt> parameters;
    public final Statement body;

    public MethodDef(final String type, final String name, 
            final ArrayList<VariableDeclarationStmt> parameters, final Statement body) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }
    
    public String toString() {
        return "MethodDef(" + type + ", " + name + ", " + parametersString() + ", " + body + ")";
    }

    private String parametersString(){
        String ret = "";
        if(parameters.size() >= 1){
            ret += parameters.get(0).toString();
        }
        for(int i = 1; i < parameters.size(); i++){
            ret += ", " + parameters.get(0).toString();
        }
        return ret;
    }
    
    public boolean equals(MethodDef def) {
        return (toString()).equals(def.toString());
    }

}