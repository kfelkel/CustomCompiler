package parser;

import java.util.ArrayList;
import parser.statements.VariableDeclarationStmt;
import parser.statements.BlockStmt;

public class MethodDef {

    public final String type;
    public final String name;
    public final ArrayList<VariableDeclarationStmt> parameters;
    public final BlockStmt body;
    public final List<Exp> returnExp;

    public MethodDef(final String type, final String name, 
            final ArrayList<VariableDeclarationStmt> parameters, final BlockStmt body) {
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
            ret += ", " + parameters.get(i).toString();
        }
        return ret;
    }
    
    public boolean equals(MethodDef def) {
        return (toString()).equals(def.toString());
    }

}