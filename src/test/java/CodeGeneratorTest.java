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
        actual += list.get(0);
        for(int i = 1; i<list.size(); i++){
            actual += " ";
            actual += list.get(i);
        }
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
    }
}