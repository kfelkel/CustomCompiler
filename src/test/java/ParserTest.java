import java.util.*;

import parser.*;
import parser.statements.*;
import parser.expressions.*;
import tokenizer.*;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    public static void testParse(String programString, Program expected) throws TokenizationException, ParseException {
        List<Token> tokens = Lexer.lex(programString);
        Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
        //System.out.println(Arrays.toString(tokenArray));
        Parser myparser = new Parser(tokenArray);
        Program actual = myparser.parseProgram();
        assertEquals(expected.toString(), actual.toString());
    }

    public void testStatementParse(String stmtString, Statement expected) throws TokenizationException, ParseException {
        String programString = "int main(){" + stmtString + " return 0; }";
        List<Token> tokens = Lexer.lex(programString);
        Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
        //System.out.println(Arrays.toString(tokenArray));
        Parser myparser = new Parser(tokenArray);
        Program program = myparser.parseProgram();
        Statement actual = program.entryPoint.body.body.get(0);
        assertEquals(expected.toString(), actual.toString());
    }

    public void testExpressionParse(String expString, Expression expected) throws TokenizationException, ParseException {
        String programString = "int main(){ return " + expString + "; }";
        List<Token> tokens = Lexer.lex(programString);
        Token[] tokenArray = tokens.toArray(new Token[tokens.size()]);
        //System.out.println(Arrays.toString(tokenArray));
        Parser myparser = new Parser(tokenArray);
        Program program = myparser.parseProgram();
        Expression actual = program.entryPoint.returnExp;
        assertEquals(expected.toString(), actual.toString());
    }


    @Test
    public void testMainOnly()throws TokenizationException, ParseException {
            // no class defs
            String programString = "int main(){ return 0;}";
            Program expected = new Program(new ArrayList<ClassDef>(), new MethodDef("int", "main",
                    new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
            testParse(programString, expected);
    }

    @Test
    public void ClassDefTest() throws TokenizationException, ParseException {
        String programString;
            Program expected;

        // one class def
        programString = "class TestClass{constructor(){}}" + "int main(){return 0;}";
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        classDefs.add(new ClassDef("TestClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        testParse(programString, expected);

        // two class defs
        programString = "class TestClassA{constructor(){}} class TestClassB{constructor(){}} " + "int main(){return 0;}";
        classDefs = new ArrayList<ClassDef>();
        classDefs.add(new ClassDef("TestClassA", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        classDefs.add(new ClassDef("TestClassB", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void InheritanceTest() throws TokenizationException, ParseException {
        String programString = "class TestClass:ParentClass{constructor(){}} " + "int main(){ return 0; }";
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        classDefs.add(new ClassDef("TestClass", "ParentClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                new ArrayList<MethodDef>()));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void MethodDefTest() throws TokenizationException, ParseException {
        // one method
        String programString = "class TestClass{constructor(){} int methodTest(){ return 0;}} " + "int main(){ return 0;}";
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        ArrayList<MethodDef> methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(new MethodDef("int", "methodTest", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        classDefs.add(new ClassDef("TestClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                methodDefs));
        Program expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        testParse(programString, expected);
        // two methods
        programString = "class TestClass{constructor(){} int methodA(){return 0;} TestClass methodB(){return 0;}} " + "int main(){return 0;}";
        classDefs = new ArrayList<ClassDef>();
        methodDefs = new ArrayList<MethodDef>();
        methodDefs.add(new MethodDef("int", "methodA", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        methodDefs.add(new MethodDef("TestClass", "methodB", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        classDefs.add(new ClassDef("TestClass", new ArrayList<VariableDeclarationStmt>(),
                new Constructor(new ArrayList<VariableDeclarationStmt>(), new BlockStmt(new ArrayList<Statement>())),
                methodDefs));
        expected = new Program(classDefs, new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(),
                new BlockStmt(new ArrayList<Statement>()), new IntegerExp(0)));
        testParse(programString, expected);
    }

    @Test
    public void HelloWorldTest() throws TokenizationException, ParseException {
        String programString = "int main(){" + "String mystring = \"Hello World!\";" + "println(mystring);" + "return 0;"
                + "}";
        ArrayList<Statement> stmtList = new ArrayList<Statement>();
        stmtList.add(new VariableDeclarationStmt("String", "mystring", new StringExp("Hello World!")));
        stmtList.add(new PrintlnStmt(new VariableExp("mystring")));
        BlockStmt mainBody = new BlockStmt(stmtList);
        Program expected = new Program(new ArrayList<ClassDef>(),
                new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(), mainBody, new IntegerExp(0)));

        testParse(programString, expected);
    }

    @Test
    public void testParseExpression() throws TokenizationException, ParseException{
        // Token[] tokens = new Token[]{new IntegerToken(9),new MultiplicationToken(),new IntegerToken(8),new PlusToken(),
        // new IntegerToken(1), new DivisionToken(), new IntegerToken(2),
        // new MultiplicationToken(), new IntegerToken(3), new DivisionToken(), new IntegerToken(4),
        // new MultiplicationToken(), new IntegerToken(5),new PlusToken(), new IntegerToken(6),new MinusToken(), 
        //                 new IntegerToken(7) };
        // // String mystring = "int main(){int x = 9*8+1/2*3/4*5+6-7;}";

        //String mystring = "9+8;";
        //String mystring = "9*8+1/2*3/4*5+6-7;";
        String mystring;
        Expression expected;
        Expression actual;
        Token[] tokens;
        Parser myparser;

        mystring = "1/2;";
        expected = new DivisionExp(new IntegerExp(1), new IntegerExp(2));
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));
        
        mystring = "1/2*3;";
        expected = new MultiplicationExp(expected, new IntegerExp(3));
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));

        mystring = "1/2*3/4;";
        expected = new DivisionExp(expected, new IntegerExp(4));
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));
        
        mystring = "1/2*3/4*5;";
        expected = new MultiplicationExp(expected, new IntegerExp(5));
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));
        
        mystring = "9*8+1/2*3/4*5;";
        expected = new PlusExp(new MultiplicationExp(new IntegerExp(9), new IntegerExp(8)), expected);
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));
        
        mystring = "9*8+1/2*3/4*5+6;";
        expected = new PlusExp(expected, new IntegerExp(6));
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));
        
        mystring = "9*8+1/2*3/4*5+6-7;";
        expected = new MinusExp(expected, new IntegerExp(7));
        tokens = Lexer.lex(mystring).toArray(new Token[0]);
        myparser = new Parser(tokens);
        actual = myparser.parseExp(0).result;
        assert (actual.equals(expected));
        
    }

    @Test(expected = ParseException.class)
    public void testUnevenParensException()throws TokenizationException, ParseException{
        testExpressionParse("((5)", null);
    }
    @Test
    public void testLessThanExp()throws TokenizationException, ParseException{
        testExpressionParse("5<3", new LessThanExp(new IntegerExp(5), new IntegerExp(3)));
    }
    @Test
    public void testLessThanOrEqualExp()throws TokenizationException, ParseException{
        testExpressionParse("5<=3", new LessThanOrEqualExp(new IntegerExp(5), new IntegerExp(3)));
    }
    @Test
    public void testGreaterThanExp()throws TokenizationException, ParseException{
        testExpressionParse("5>3", new GreaterThanExp(new IntegerExp(5), new IntegerExp(3)));
    }
    @Test
    public void testGreaterThanOrEqualExp()throws TokenizationException, ParseException{
        testExpressionParse("5>=3", new GreaterThanOrEqualExp(new IntegerExp(5), new IntegerExp(3)));
    }
    @Test
    public void testEqualEqualExp()throws TokenizationException, ParseException{
        testExpressionParse("5==3", new EqualEqualExp(new IntegerExp(5), new IntegerExp(3)));
    }
    @Test
    public void testModulusExp()throws TokenizationException, ParseException{
        testExpressionParse("5%3", new ModulusExp(new IntegerExp(5), new IntegerExp(3)));
    }
    @Test
    public void testNewExp()throws TokenizationException, ParseException{
        List<Expression> params = new ArrayList<Expression>();
        params.add(new IntegerExp(5));
        params.add(new IntegerExp(3));
        testExpressionParse("new Class(5,3)", new NewExp("Class", params));
    }
    @Test(expected = ParseException.class)
    public void testNewExpExceptionMissingName()throws TokenizationException, ParseException{
        List<Expression> params = new ArrayList<Expression>();
        testExpressionParse("new()", new NewExp("Class", params));
    }
    @Test(expected = ParseException.class)
    public void testNewExpExceptionMissingParens() throws TokenizationException, ParseException{
        testExpressionParse("new Class 5", new NewExp("Class", new ArrayList<Expression>()));
    }
    @Test
    public void testMethodCallExp()throws TokenizationException, ParseException{
        List<Expression> params = new ArrayList<Expression>();
        params.add(new IntegerExp(5));
        MethodCallExp callExp = new MethodCallExp("obj", "method", params);
        testExpressionParse("obj.method(5)", callExp);
    }
    @Test(expected = ParseException.class)
    public void testNoParenMethodCallExp()throws TokenizationException, ParseException{
        List<Expression> params = new ArrayList<Expression>();
        params.add(new IntegerExp(5));
        MethodCallExp callExp = new MethodCallExp("obj", "method", params);
        testExpressionParse("obj.method 5)", callExp);
    }
    @Test
    public void testThisMethodCallExp()throws TokenizationException, ParseException{
        ThisExp callExp = new ThisExp(new VariableExp("name"));
        testExpressionParse("this.name", callExp);
    }
    @Test
    public void testStringExp()throws TokenizationException, ParseException{
        testExpressionParse("\"hello\"", new StringExp("hello"));
    }
    @Test
    public void testNegativeNumberExp()throws TokenizationException, ParseException{
        testExpressionParse("-3", new IntegerExp(-3));
    }

}