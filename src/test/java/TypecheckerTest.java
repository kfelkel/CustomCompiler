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
public void testInherit(){
    Type mytype = new IntType();
    assertTrue(mytype instanceof IntType);

}

}