package codeGenerator;

import typechecker.types.*;
import parser.*;
import parser.statements.*;
import parser.expressions.*;

import java.util.*;

public class CodeGenerator {
    // private static ArrayList<String> INCLUDE = new ArrayList<String>(); //holds
    // all #include files, worry later
    private ArrayList<String> FunctionHeaders = new ArrayList<String>();
    private ArrayList<String> Classes = new ArrayList<String>();
    private ArrayList<String> Main = new ArrayList<String>();
    private Program myProgram;

    CodeGenerator(Program myProgram) {
        this.myProgram = myProgram;
        generateProgramCode();
    }

    public String getCode() {
        String completeCode = "";
        return completeCode;
    }

    private void generateProgramCode() {

    }

    private void generateClassCode() {
        // generate structs, rename object.method to objectmethod,
    }

    private void generateMainCode() {

    }

    private void generateMethodDefCode() {

    }

    public void generateStatementCode(Statement s, ArrayList<String> currentList) throws CodeGeneratorException{

        if (s instanceof BlockStmt) {
            BlockStmt stmt = (BlockStmt) s;
            currentList.add("{");
            List<Statement> bodyStmtList = stmt.body;
            for (int i = 0; i < bodyStmtList.size(); i++) {
                generateStatementCode(bodyStmtList.get(i), currentList);
            }
            currentList.add("}");
        } 
        else if (s instanceof ForStmt) {
            ForStmt stmt = (ForStmt) s;
            currentList.add("for(");
            generateStatementCode(stmt.initializer, currentList);
            currentList.add(";");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(";");
            generateStatementCode(stmt.incrementor, currentList);
            currentList.add(")");
            generateStatementCode(stmt.body, currentList);
        } 
        else if (s instanceof IfElseStmt) {
            IfElseStmt stmt = (IfElseStmt) s;
            currentList.add("if(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.trueBranch, currentList);
            currentList.add("else");
            generateStatementCode(stmt.falseBranch, currentList);
        } 
        else if (s instanceof IfStmt) {
            IfStmt stmt = (IfStmt) s;
            currentList.add("if(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.trueBranch, currentList);
        } 
        else if (s instanceof PrintlnStmt) {
            PrintlnStmt stmt = (PrintlnStmt) s;
            currentList.add(generatePrintf(stmt.output, currentList));
            currentList.add("printf(\"\\n\");");
        } 
        else if (s instanceof PrintStmt) {
            PrintStmt stmt = (PrintStmt) s;
            currentList.add(generatePrintf(stmt.output, currentList));
        } 
        else if (s instanceof ReturnStmt) {
            ReturnStmt stmt = (ReturnStmt) s;
            currentList.add("return");
            generateExpressionCode(stmt.value, currentList);
            currentList.add(";");
        } 
        else if (s instanceof ReturnVoidStmt) {
            //Do nothing, no return void in C
        } 
        else if (s instanceof WhileStmt) {
            WhileStmt stmt = (WhileStmt) s;
            currentList.add("while(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.body, currentList);

        } 
        else if (s instanceof VariableDeclarationStmt) {
            VariableDeclarationStmt stmt = (VariableDeclarationStmt) s;
            currentList.add(stmt.type);
            currentList.add(stmt.name);
            if(stmt.value != null){
                currentList.add("=");
                generateExpressionCode(stmt.value, currentList);
                currentList.add(";");
        }
        } 
        else if (s instanceof VariableAssignmentStmt) {
            VariableAssignmentStmt stmt = (VariableAssignmentStmt) s;
            currentList.add(stmt.name);
            currentList.add("=");
            generateExpressionCode(stmt.value, currentList);
            currentList.add(";");
        } 
        else {
            throw new CodeGeneratorException("Unknown statement: " + s.toString());
        }
    }

    public String generatePrintf(Expression e, ArrayList<String> currentList){
        String output = "printf(\"";
        //TO-DO
        output += "\"):";
        return output;
    }
    public void generateExpressionCode(Expression e, ArrayList<String> currentList){

    }
}