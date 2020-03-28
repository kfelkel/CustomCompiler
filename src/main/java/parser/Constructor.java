package parser;

import java.util.ArrayList;
import parser.statements.VariableDeclarationStmt;
import parser.statements.BlockStmt;

public class Constructor{

    final public ArrayList<VariableDeclarationStmt> parameters;
    final public BlockStmt body;

    public Constructor(final ArrayList<VariableDeclarationStmt> parameters, final BlockStmt body){
        this.parameters = parameters;
        this.body = body;
    }
}