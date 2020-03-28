package parser.statements;

import java.util.List;

public class BlockStmt implements Statement {
    public final List<Statement> body;

    public BlockStmt(List<Statement> body) {
        this.body = body;
    }

    public String toString() {
        return "BlockStmt( " + body + ")";  
    }

    public boolean equals(Statement stmt) {
        if(stmt instanceof BlockStmt){
            BlockStmt castStmt = (BlockStmt) stmt;
            return castStmt.body == body;
        }
        else
            return false;
    }
}
