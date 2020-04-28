import java.util.*;

import parser.*;
import parser.statements.*;
import parser.expressions.*;
import tokenizer.*;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;

import typechecker.*;
import typechecker.types.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypecheckerTest {
    @Test
    public void testInherit() {
        Type mytype = new IntType();
        assertTrue(mytype instanceof IntType);

    }

    @Test
    public void testSimplestProgram() throws IllTypedException {
        // Note: Don't need an assert if the test is expected to pass

        // "Int main(){return 0;}"
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        Program myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        Typechecker myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testValidPrimitiveDeclaration() throws IllTypedException {
        /*
         * Int main(){ Int a; Int b = 1; return 0;}
         */
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("Int", "a"));
        mainStatements.add(new VariableDeclarationStmt("Int", "b", new IntegerExp(1)));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        Program myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        Typechecker myTypechecker = new Typechecker(myProgram);

        /*
         * Int main(){ String a; String b = "text"; return 0;}
         */
        mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("String", "a"));
        mainStatements.add(new VariableDeclarationStmt("String", "b", new StringExp("text")));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        myTypechecker = new Typechecker(myProgram);

        /*
         * Int main(){ Bool a; Bool b = 1 == 1; return 0;}
         */
        mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("Bool", "a"));
        mainStatements
                .add(new VariableDeclarationStmt("Bool", "b", new EqualEqualExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testInvalidPrimitiveDeclaration() throws IllTypedException {
        /*
         * Int main(){ Int a = "text"; return 0;}
         */
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("Int", "a", new StringExp("text")));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        Program myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));

        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("IllTypedException should have been thrown");
        } catch (IllTypedException e) {
        }

        /*
         * Int main(){ String a = 1; return 0;}
         */
        mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("String", "a", new IntegerExp(1)));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("IllTypedException should have been thrown");
        } catch (IllTypedException e) {
        }
    }

    @Test
    public void testValidPrimitiveAssignment() throws IllTypedException {
        /*
         * Int main(){ Int a; a = 1; return 0;}
         */
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("Int", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new IntegerExp(1)));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        Program myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        Typechecker myTypechecker = new Typechecker(myProgram);

        /*
         * Int main(){ String a; a = "text"; return 0;}
         */
        mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("String", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new StringExp("text")));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        myTypechecker = new Typechecker(myProgram);

        /*
         * Int main(){ Bool a; a = 1 == 1; return 0;}
         */
        mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("Bool", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new EqualEqualExp(new IntegerExp(1), new IntegerExp(1))));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        myTypechecker = new Typechecker(myProgram);
    }

    @Test
    public void testInvalidPrimitiveAssignment() throws IllTypedException {
        /*
         * Int main(){ Int a; a = "text"; return 0;}
         */
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("Int", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new StringExp("text")));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        Program myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("IllTypedException should have been thrown");
        } catch (IllTypedException e) {
        }

        /*
         * Int main(){ String a; a = 1; return 0;}
         */
        mainStatements = new ArrayList<Statement>();
        mainStatements.add(new VariableDeclarationStmt("String", "a"));
        mainStatements.add(new VariableAssignmentStmt("a", new IntegerExp(1)));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));
        try {
            Typechecker myTypechecker = new Typechecker(myProgram);
            fail("IllTypedException should have been thrown");
        } catch (IllTypedException e) {
        }
    }

    @Test
    public void testPrintStatements() throws IllTypedException {
        /*
         * Int main(){ print(1); print("text"); println(1); println("text"); return 0;}
         */
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(new PrintStmt(new IntegerExp(1)));
        mainStatements.add(new PrintStmt(new StringExp("text")));
        mainStatements.add(new PrintlnStmt(new IntegerExp(1)));
        mainStatements.add(new PrintlnStmt(new StringExp("text")));
        mainStatements.add(new ReturnStmt(new IntegerExp(0)));
        Program myProgram = new Program(new ArrayList<ClassDef>(),
                new MethodDef("Int", "main", new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements)));

        Typechecker myTypechecker = new Typechecker(myProgram);
    }
}