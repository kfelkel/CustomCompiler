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
    public void HelloWorldTest() throws TokenizationException, ParseException {
        String programString = "int main(){" + "String mystring= \"Hello World!\";" + "println(mystring);" + "return 0;"
                + ")";
        ArrayList<Statement> stmtList = new ArrayList<Statement>();
        stmtList.add(new VariableInitializerStmt("String", "mystring", new StringExp("Hello World!")));
        stmtList.add(new PrintlnStmt(new VariableExp("mystring")));
        stmtList.add(new ReturnStmt(new IntegerExp(0)));
        BlockStmt mainBody = new BlockStmt(stmtList);
        Program expected = new Program(new ArrayList<ClassDef>(), new MethodDef("int", "main", new ArrayList<Statement>(), mainBody));

        List<Token> tokens = Lexer.lex(programString);

        // I know Token[0] is weird, but, from the documentation,
        // "If the list fits in the specified array, it is returned therein."
        // "Otherwise, a new array is allocated with the runtime type of the specified array and the size of this list."
        // So, as far as I can tell, toArray(new Token[0]) will return an array of the proper size
        Parser myparser = new Parser(tokens.toArray(new Token[0]));
        Program actual = myparser.parseProgram();
        assertEquals(expected, actual);
    }

}