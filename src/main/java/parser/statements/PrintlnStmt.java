package parser.statements;

public class PrintlnStmt implements Statement {
    public final String output;

    public PrintlnStmt(String output) {
        this.output = output;
    }

    public String toString() {
        return "PrintlnStmt(" + output + ")";
    }

    public boolean equals(Statement stmt) {
        if(stmt instanceof PrintlnStmt){
            PrintlnStmt castStmt = (PrintlnStmt) stmt;
            return castStmt.output == output;
        }
        else
            return false;
    }
}