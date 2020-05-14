import java.util.*;

import parser.*;
import parser.statements.*;
import parser.expressions.*;
import codeGenerator.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class CodeGeneratorTest {

    // Helpers
    public void expressionHelper(final Expression exp, final String expected) throws CodeGeneratorException {
        ArrayList<String> list = new ArrayList<String>();
        CodeGenerator.generateExpressionCode(exp, list);
        String actual = "";
        for (int i = 0; i < list.size(); i++) {
            actual += list.get(i);
            if (i < list.size() - 1)
                actual += " ";
        }
        assertEquals(expected, actual);
    }

    public void statementHelper(final Statement stmt, final String expected) throws CodeGeneratorException {
        ArrayList<String> list = new ArrayList<String>();
        CodeGenerator.generateStatementCode(stmt, list);
        String actual = "";
        for (int i = 0; i < list.size(); i++) {
            actual += list.get(i);
            if (i < list.size() - 1)
                actual += " ";
        }
        // System.out.println(expected);
        // System.out.println(actual);
        assertEquals(expected, actual);
    }

    // Expression Tests
    @Test
    public void testDivExp() throws CodeGeneratorException {
        expressionHelper(new DivisionExp(new IntegerExp(2), new IntegerExp(3)), "( ( 2 ) / ( 3 ) )");
    }

    @Test
    public void testEqualEqualExp() throws CodeGeneratorException {
        expressionHelper(new EqualEqualExp(new IntegerExp(2), new IntegerExp(3)), "( ( 2 ) == ( 3 ) )");
    }

    @Test
    public void testGreaterThanExp() throws CodeGeneratorException {
        Expression exp = new GreaterThanExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) > ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testGreaterThanOrEqualExp() throws CodeGeneratorException {
        Expression exp = new GreaterThanOrEqualExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) >= ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testLessThanExp() throws CodeGeneratorException {
        Expression exp = new LessThanExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) < ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testLessThanOrEqualExp() throws CodeGeneratorException {
        Expression exp = new LessThanOrEqualExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) <= ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void TestMethodCallExp() throws CodeGeneratorException {
        String objectName = "MathObject";
        String methodName = "min";
        List<Expression> parameters = new ArrayList<Expression>();
        parameters.add(new IntegerExp(5));
        parameters.add(new IntegerExp(8));
        Expression exp = new MethodCallExp(objectName, methodName, parameters);
        String expectedString = "MathObject.null_min ( MathObject , ( 5 ) , ( 8 ) )";// null is weird but expected given
                                                                                     // interaction with static globals
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testMinusExp() throws CodeGeneratorException {
        Expression exp = new MinusExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) - ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testModulusExp() throws CodeGeneratorException {
        Expression exp = new ModulusExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) % ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testMultiplicationExp() throws CodeGeneratorException {
        Expression exp = new MultiplicationExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) * ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testNewExp() throws CodeGeneratorException {
        String objectName = "DateClass";
        List<Expression> parameters = new ArrayList<Expression>();
        parameters.add(new StringExp("May"));
        parameters.add(new IntegerExp(8));
        parameters.add(new IntegerExp(2020));
        Expression exp = new NewExp(objectName, parameters);
        String expectedString = "DateClass_Constructor ( \"May\" , ( 8 ) , ( 2020 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testParenthesizedExp() throws CodeGeneratorException {
        Expression exp = new ParenthesizedExp(new MinusExp(new IntegerExp(2), new IntegerExp(3)));
        String expectedString = "( ( ( 2 ) - ( 3 ) ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testPlusExp() throws CodeGeneratorException {
        Expression exp = new PlusExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) + ( 3 ) )";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testStringExp() throws CodeGeneratorException {
        Expression exp = new StringExp("hello world");
        String expectedString = "\"hello world\"";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testThisExp() throws CodeGeneratorException {
        Expression exp = new ThisExp(new VariableExp("x"));
        String expectedString = "this.";
        expressionHelper(exp, expectedString);
    }

    @Test
    public void testVariableExp() throws CodeGeneratorException {
        Expression exp = new VariableExp("myvarx");
        String expectedString = "( myvarx )";
        expressionHelper(exp, expectedString);
    }// End Expression Tests

    // Statement Tests
    @Test
    public void testEmptyBlockStmt() throws CodeGeneratorException {
        Statement stmt = new BlockStmt(new ArrayList<Statement>());
        String expectedString = "{ }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testBlockStmt() throws CodeGeneratorException {
        Statement vardec = new VariableDeclarationStmt("int", "a", new IntegerExp(7));
        ArrayList<Statement> body = new ArrayList<Statement>();
        body.add(vardec);
        Statement stmt = new BlockStmt(body);
        String expectedString = "{ int a = ( int ) ( 7 ) ; }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testForStmt() throws CodeGeneratorException {
        Statement initializer = new VariableDeclarationStmt("int", "i", new IntegerExp(0));
        Expression condition = new LessThanExp(new VariableExp("i"), new IntegerExp(10));
        Statement incrementor = new VariableAssignmentStmt("i", new PlusExp(new VariableExp("i"), new IntegerExp(1)));
        BlockStmt body = new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new ForStmt(initializer, condition, incrementor, body);
        String expectedString = "for ( int i = ( int ) ( 0 ) ; ( ( i ) < ( 10 ) ) ; i = ( int ) ( ( i ) + ( 1 ) ) ) { }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testIfElseStmt() throws CodeGeneratorException {
        Expression condition = new GreaterThanExp(new VariableExp("x"), new IntegerExp(5));
        BlockStmt trueBranch = new BlockStmt(new ArrayList<Statement>());
        BlockStmt falseBranch = new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new IfElseStmt(condition, trueBranch, falseBranch);
        String expectedString = "if ( ( ( x ) > ( 5 ) ) ) { } else { }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testIfStmt() throws CodeGeneratorException {
        Expression condition = new GreaterThanExp(new VariableExp("x"), new IntegerExp(5));
        BlockStmt trueBranch = new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new IfStmt(condition, trueBranch);
        String expectedString = "if ( ( ( x ) > ( 5 ) ) ) { }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testPrintlnStmt() throws CodeGeneratorException {
        Expression output = new StringExp("hello world");

        Statement stmt = new PrintlnStmt(output);
        String expectedString = "printf ( \"hello world\" ) ; printf(\"\\n\");";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testPrintStmt() throws CodeGeneratorException {
        Expression output = new StringExp("hello world");

        Statement stmt = new PrintStmt(output);
        String expectedString = "printf ( \"hello world\" ) ;";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testReturnStmt() throws CodeGeneratorException {
        Expression exp = new IntegerExp(0);

        Statement stmt = new ReturnStmt(exp);
        String expectedString = "return ( 0 ) ;";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testReturnVoidStmt() throws CodeGeneratorException {
        Statement stmt = new ReturnVoidStmt();
        String expectedString = "";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testWhileStnt() throws CodeGeneratorException {
        Expression condition = new LessThanExp(new VariableExp("x"), new IntegerExp(5));
        BlockStmt body = new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new WhileStmt(condition, body);
        String expectedString = "while ( ( ( x ) < ( 5 ) ) ) { }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testVariableDeclarationStmt() throws CodeGeneratorException {
        Statement stmt = new VariableDeclarationStmt("int", "x", new IntegerExp(10));
        String expectedString = "int x = ( int ) ( 10 ) ;";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testVariableAssignmentStmt() throws CodeGeneratorException {
        Statement stmt = new VariableAssignmentStmt("x", new IntegerExp(10));
        String expectedString = "x = ( null ) ( 10 ) ;"; // null is weird but expected given interaction with static
                                                         // globals
        statementHelper(stmt, expectedString);
    }// End Statement Tests

    @Test
    public void testPolymorphismMethodCall() throws CodeGeneratorException {
        /*
         * Int main(){ ParentClass a; a = new ChildClass(); return 0;}
         */
        List<Statement> mainStatements = new ArrayList<Statement>();
        mainStatements.add(
                new VariableDeclarationStmt("ParentClass", "a", new NewExp("ChildClass", new ArrayList<Expression>())));
        mainStatements.add(new VariableDeclarationStmt("int", "x",
                new MethodCallExp("a", "parentMethod", new ArrayList<Expression>())));

        ArrayList<VariableDeclarationStmt> fields = new ArrayList<VariableDeclarationStmt>();
        Constructor constructor = new Constructor(fields, new BlockStmt(new ArrayList<Statement>()));
        ArrayList<MethodDef> methods = new ArrayList<MethodDef>();
        ArrayList<ClassDef> classdefs = new ArrayList<ClassDef>();
        ClassDef child = new ClassDef("ChildClass", "ParentClass", fields, constructor, new ArrayList<MethodDef>());
        MethodDef parentMethod = new MethodDef("int", "parentMethod", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0));
        methods.add(parentMethod);
        ClassDef parent = new ClassDef("ParentClass", "", fields, constructor, methods);

        classdefs.add(parent);
        classdefs.add(child);

        Program myProgram = new Program(classdefs, new MethodDef("int", "main",
                new ArrayList<VariableDeclarationStmt>(), new BlockStmt(mainStatements), new IntegerExp(0)));
        System.out.println((new CodeGenerator(myProgram).getCode()));
        // Produces following code (manually formatted for readibility)
        /*
            typedef ParentClass;
            typedef ChildClass;
            ParentClass ParentClass_Constructor();
            int ParentClass_parentMethod(ParentClass this);
            ChildClass ChildClass_Constructor();
            int ChildClass_parentMethod(ChildClass this);
            typedef struct
            {
                int (*ParentClass_parentMethod)(ParentClass this);
            } ParentClass;
            ParentClass ParentClass_Constructor()
            {
                ParentClass this;
                return this;
            }
            int ParentClass_parentMethod(ParentClass this)
            {
            }
            typedef struct
            {
                int (*ChildClass_parentMethod)(ChildClass this);
            } ChildClass;
            ChildClass ChildClass_Constructor()
            {
                ChildClass this;
                return this;
            }
            int ChildClass_parentMethod(ChildClass this) {}
            int main()
            {
                ParentClass a = (ParentClass)ChildClass_Constructor();
                int x = (int)a.ParentClass_parentMethod(a);
                return (0);
            }
         */
    }
}