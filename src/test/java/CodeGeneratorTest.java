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

    public void expressionHelper(final Expression exp, final String expected) throws CodeGeneratorException{
        ArrayList<String> list = new ArrayList<String>();
        CodeGenerator.generateExpressionCode(exp, list);
        String actual = "";
        for(int i = 0; i<list.size(); i++){
            actual += list.get(i);
            actual += " ";
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testDivExp() throws CodeGeneratorException{
        expressionHelper(new DivisionExp(new IntegerExp(2), new IntegerExp(3)), " ( 2 ) / ( 3 ) ");
    }
    
}