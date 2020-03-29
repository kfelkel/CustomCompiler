package parser;

import java.util.List;
import parser.statements.Statement;
import parser.statements.BlockStmt;

public class Constructor{

    final public List<Statement> parameters;
    final public BlockStmt body;

    public Constructor(final List<Statement> parameters, final BlockStmt body){
        this.parameters = parameters;
        this.body = body;
    }
}