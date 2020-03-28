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
    @Test
    public void HelloWorldTest()throws TokenizationException, ParseException{
        String programString = 
            "int main(){" +
                "String mystring= \"Hello World!\";" +
                "println(mystring);"+
                "return 0;"+
            ")";
        ArrayList<Statement> stmtList = new ArrayList<Statement>();
        stmtList.add(new VariableInitializerStmt("String", 
                                            "mystring",
                                            new StringExp("Hello World!")));
        stmtList.add(new PrintlnStmt(new VariableExp("String", "mystring")));
        stmtList.add(new ReturnStmt(new IntegerExp(0)));
        BlockStmt mainBody = new BlockStmt(stmtList);
        Program expected = new Program(null, new MethodDef("int", "main", new ArrayList<VariableDeclarationStmt>(), mainBody));

        List<Token> tokens = Lexer.lex(programString);
        Parser myparser = new Parser((Token[]) tokens.toArray());
        Program actual = myparser.parseProgram();
assertEquals(expected, actual);

}

}