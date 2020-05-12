package codeGenerator;

import typechecker.types.*;
import parser.*;
import parser.statements.*;
import parser.expressions.*;

import java.util.*;

public class CodeGenerator {
    // private static ArrayList<String> INCLUDE = new ArrayList<String>(); //holds
    private ArrayList<String> FunctionHeaders = new ArrayList<String>();
    private ArrayList<String> Classes = new ArrayList<String>();
    private ArrayList<String> Main = new ArrayList<String>();
    private Program myProgram;

    CodeGenerator(Program myProgram) {
        this.myProgram = myProgram;
    }

    public String getCode() throws CodeGeneratorException {
        generateProgramCode();
        String completeCode = "";
        // TO-DO
        return completeCode;
    }

    private void generateProgramCode() throws CodeGeneratorException {
        for (int i = 0; i < myProgram.classDefs.size(); i++) {
            generateClassCode(myProgram.classDefs.get(i));
        }
        generateMainCode();
    }

    private void generateClassCode(ClassDef myClass) throws CodeGeneratorException {
        Classes.add("struct");
        Classes.add(myClass.className);
        Classes.add("{");
        for (int i = 0; i < myClass.fields.size(); i++) {
            generateStatementCode(myClass.fields.get(i), Classes);
        }
        //TO-DO: Deal with parent class
        Classes.add("}");
        //TO-DO: Deal with constructor
        for (int i = 0; i < myClass.methods.size(); i++) {
            generateMethodDefCode(myClass.methods.get(i), myClass.className);
        }
    }

    private void generateMethodDefCode(MethodDef method, String classname) throws CodeGeneratorException {
        Classes.add(classname + method.name);
        Classes.add("(");
        Classes.add(classname);
        Classes.add("this");
        for (int i = 0; i < method.parameters.size(); i++) {
            Classes.add(",");
            generateStatementCode(method.parameters.get(i), Classes);
        }
        Classes.add(")");
        generateStatementCode(method.body, Classes);
        //handle returns?
    }

    private void generateMainCode() throws CodeGeneratorException {
        MethodDef mymain = myProgram.entryPoint;
        Main.add("int main()");
        generateStatementCode(mymain.body, Main);
        // handle returns?
    }

    public void generateStatementCode(Statement s, ArrayList<String> currentList) throws CodeGeneratorException {

        if (s instanceof BlockStmt) {
            BlockStmt stmt = (BlockStmt) s;
            currentList.add("{");
            List<Statement> bodyStmtList = stmt.body;
            for (int i = 0; i < bodyStmtList.size(); i++) {
                generateStatementCode(bodyStmtList.get(i), currentList);
            }
            currentList.add("}");
        } else if (s instanceof ForStmt) {
            ForStmt stmt = (ForStmt) s;
            currentList.add("for(");
            generateStatementCode(stmt.initializer, currentList);
            currentList.add(";");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(";");
            generateStatementCode(stmt.incrementor, currentList);
            currentList.add(")");
            generateStatementCode(stmt.body, currentList);
        } else if (s instanceof IfElseStmt) {
            IfElseStmt stmt = (IfElseStmt) s;
            currentList.add("if(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.trueBranch, currentList);
            currentList.add("else");
            generateStatementCode(stmt.falseBranch, currentList);
        } else if (s instanceof IfStmt) {
            IfStmt stmt = (IfStmt) s;
            currentList.add("if(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.trueBranch, currentList);
        } else if (s instanceof PrintlnStmt) {
            PrintlnStmt stmt = (PrintlnStmt) s;
            currentList.add(generatePrintf(stmt.output, currentList));
            currentList.add("printf(\"\\n\");");
        } else if (s instanceof PrintStmt) {
            PrintStmt stmt = (PrintStmt) s;
            currentList.add(generatePrintf(stmt.output, currentList));
        } else if (s instanceof ReturnStmt) {
            ReturnStmt stmt = (ReturnStmt) s;
            currentList.add("return");
            generateExpressionCode(stmt.value, currentList);
            currentList.add(";");
        } else if (s instanceof ReturnVoidStmt) {
            // Do nothing, no return void in C
        } else if (s instanceof WhileStmt) {
            WhileStmt stmt = (WhileStmt) s;
            currentList.add("while(");
            generateExpressionCode(stmt.condition, currentList);
            currentList.add(")");
            generateStatementCode(stmt.body, currentList);

        } else if (s instanceof VariableDeclarationStmt) {
            VariableDeclarationStmt stmt = (VariableDeclarationStmt) s;
            currentList.add(stmt.type);
            currentList.add(stmt.name);
            if (stmt.value != null) {
                currentList.add("=");
                generateExpressionCode(stmt.value, currentList);
                currentList.add(";");
            }
        } else if (s instanceof VariableAssignmentStmt) {
            VariableAssignmentStmt stmt = (VariableAssignmentStmt) s;
            currentList.add(stmt.name);
            currentList.add("=");
            generateExpressionCode(stmt.value, currentList);
            currentList.add(";");
        } else {
            throw new CodeGeneratorException("Unknown statement: " + s.toString());
        }
    }

    public String generatePrintf(Expression e, ArrayList<String> currentList) {
        String output = "printf(\"";
        // TO-DO
        output += "\"):";
        return output;
    }

    public void generateExpressionCode(Expression e, ArrayList<String> currentList) {

    }
}