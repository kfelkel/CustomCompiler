package parser.statements;

public class PrintStmt implements Statement {
    public final String output;

    public PrintStmt(String output) {
        this.output = output;
    }

    public String toString() {
        return "PrintStmt(" + output + ")";
    }

    public boolean equals(Statement stmt) {
        if(stmt instanceof PrintStmt){
            PrintStmt castStmt = (PrintStmt) stmt;
            return castStmt.output == output;
        }
        else
            return false;
    }
    
}