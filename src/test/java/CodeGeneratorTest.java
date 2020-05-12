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

import codeGenerator.CodeGenerator;
import codeGenerator.CodeGeneratorException;

import static org.junit.Assert.*;


public class CodeGeneratorTest {

    //Helpers
    public void expressionHelper(final Expression exp, final String expected) throws CodeGeneratorException{
        ArrayList<String> list = new ArrayList<String>();
        CodeGenerator.generateExpressionCode(exp, list);
        String actual = "";
        for(int i = 0; i<list.size(); i++){
            actual += list.get(i);
            if(i<list.size() - 1)
                actual += " ";
        }
        assertEquals(expected, actual);
    }
    public void statementHelper(final Statement stmt, final String expected) throws CodeGeneratorException{
        ArrayList<String> list = new ArrayList<String>();
        CodeGenerator.generateStatementCode(stmt, list);
        String actual = "";
        for(int i = 0; i<list.size(); i++){
            actual += list.get(i);
            if(i<list.size() - 1)
                actual += " ";
        }
       // System.out.println(expected);
       // System.out.println(actual);
        assertEquals(expected, actual);
    }

    //Expression Tests
    @Test
    public void testDivExp() throws CodeGeneratorException{
        expressionHelper(new DivisionExp(new IntegerExp(2), new IntegerExp(3)), "( ( 2 ) / ( 3 ) )");
    }
    @Test
    public void testEqualEqualExp() throws CodeGeneratorException{
        expressionHelper(new EqualEqualExp(new IntegerExp(2), new IntegerExp(3)), "( ( 2 ) == ( 3 ) )");
    }
    @Test
    public void testGreaterThanExp() throws CodeGeneratorException{
        Expression exp = new GreaterThanExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) > ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testGreaterThanOrEqualExp() throws CodeGeneratorException{
        Expression exp = new GreaterThanOrEqualExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) >= ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testLessThanExp() throws CodeGeneratorException{
        Expression exp = new LessThanExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) < ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testLessThanOrEqualExp() throws CodeGeneratorException{
        Expression exp = new LessThanOrEqualExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) <= ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void TestMethodCallExp() throws CodeGeneratorException{
        String objectName = "MathClass";
        String methodName = "min";
        List<Expression> parameters = new ArrayList<Expression>();
        parameters.add(new IntegerExp(5));
        parameters.add(new IntegerExp(8));
        Expression exp = new MethodCallExp(objectName, methodName, parameters);
        String expectedString = "MathClass_min ( ( 5 ) , ( 8 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testMinusExp() throws CodeGeneratorException{
        Expression exp = new MinusExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) - ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testModulusExp() throws CodeGeneratorException{
        Expression exp = new ModulusExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) % ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testMultiplicationExp() throws CodeGeneratorException{
        Expression exp = new MultiplicationExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) * ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testNewExp() throws CodeGeneratorException{
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
    public void testParenthesizedExp() throws CodeGeneratorException{
        Expression exp = new ParenthesizedExp(new MinusExp(new IntegerExp(2), new IntegerExp(3)));
        String expectedString = "( ( ( 2 ) - ( 3 ) ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testPlusExp() throws CodeGeneratorException{
        Expression exp = new PlusExp(new IntegerExp(2), new IntegerExp(3));
        String expectedString = "( ( 2 ) + ( 3 ) )";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testStringExp() throws CodeGeneratorException{
        Expression exp = new StringExp("hello world");
        String expectedString = "\"hello world\"";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testThisExp() throws CodeGeneratorException{
        Expression exp = new ThisExp();
        String expectedString = "this.";
        expressionHelper(exp, expectedString);
    }
    @Test
    public void testVariableExp() throws CodeGeneratorException{
        Expression exp = new VariableExp("myvarx");
        String expectedString = "( myvarx )";
        expressionHelper(exp, expectedString);
    }//End Expression Tests

    //Statement Tests
    @Test 
    public void testEmptyBlockStmt() throws CodeGeneratorException{
        Statement stmt = new BlockStmt(new ArrayList<Statement>());
        String expectedString = "{ }";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testBlockStmt() throws CodeGeneratorException{
        Statement vardec = new VariableDeclarationStmt("int", "a", new IntegerExp(7));
        ArrayList<Statement> body = new ArrayList<Statement>();
        body.add(vardec);
        Statement stmt = new BlockStmt(body);
        String expectedString = "{ int a = ( 7 ) ; }";
        statementHelper(stmt, expectedString);
    }
    @Test 
    public void testForStmt() throws CodeGeneratorException{
        Statement initializer = new VariableDeclarationStmt("int", "i", new IntegerExp(0));
        Expression condition = new LessThanExp(new VariableExp("i"), new IntegerExp(10));
        Statement incrementor = new VariableAssignmentStmt("i", new PlusExp(new VariableExp("i"), new IntegerExp(1)));
        BlockStmt body = new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new ForStmt(initializer, condition, incrementor, body);
        String expectedString = "for ( int i = ( 0 ) ; ( ( i ) < ( 10 ) ) ; i = ( ( i ) + ( 1 ) ) ) { }";
        statementHelper(stmt, expectedString);
    }
    @Test 
    public void testIfElseStmt() throws CodeGeneratorException{
        Expression condition = new GreaterThanExp(new VariableExp("x"), new IntegerExp(5));
        BlockStmt trueBranch =  new BlockStmt(new ArrayList<Statement>());
        BlockStmt falseBranch =  new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new IfElseStmt(condition, trueBranch, falseBranch);
        String expectedString = "if ( ( ( x ) > ( 5 ) ) ) { } else { }";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testIfStmt() throws CodeGeneratorException{
        Expression condition = new GreaterThanExp(new VariableExp("x"), new IntegerExp(5));
        BlockStmt trueBranch =  new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new IfStmt(condition, trueBranch);
        String expectedString = "if ( ( ( x ) > ( 5 ) ) ) { }";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testPrintlnStmt() throws CodeGeneratorException{
        Expression output = new StringExp("hello world");

        Statement stmt = new PrintlnStmt(output);
        String expectedString = "printf ( \"hello world\" ) ; printf(\"\\n\");";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testPrintStmt() throws CodeGeneratorException{
        Expression output = new StringExp("hello world");

        Statement stmt = new PrintStmt(output);
        String expectedString = "printf ( \"hello world\" ) ;";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testReturnStmt() throws CodeGeneratorException{
        Expression exp = new IntegerExp(0);

        Statement stmt = new ReturnStmt(exp);
        String expectedString = "return ( 0 ) ;";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testReturnVoidStmt() throws CodeGeneratorException{
        Statement stmt = new ReturnVoidStmt();
        String expectedString = "";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testWhileStnt() throws CodeGeneratorException{
        Expression condition = new LessThanExp(new VariableExp("x"), new IntegerExp(5));
        BlockStmt body =  new BlockStmt(new ArrayList<Statement>());

        Statement stmt = new WhileStmt(condition, body);
        String expectedString = "while ( ( ( x ) < ( 5 ) ) ) { }";
        statementHelper(stmt, expectedString);
    }

    @Test
    public void testVariableDeclarationStmt() throws CodeGeneratorException{
        Statement stmt = new VariableDeclarationStmt("int", "x", new IntegerExp(10));
        String expectedString = "int x = ( 10 ) ;";
        statementHelper(stmt, expectedString);
    }
    @Test
    public void testVariableAssignmentStmt() throws CodeGeneratorException{
        Statement stmt = new VariableAssignmentStmt("x", new IntegerExp(10));
        String expectedString = "x = ( 10 ) ;";
        statementHelper(stmt, expectedString);
    }//End Statement Tests


}