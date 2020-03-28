package parser;

import java.util.ArrayList;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.ClassKeywordToken;
import parser.expressions.*;
import parser.statements.*;

public class Parser{

    private final Token[] tokens;
    
    public Parser(final Token[] tokens) {
        this.tokens = tokens;
    }

    private class ParseResult<A> {
		public final A result;
		public final int nextPos;
		public ParseResult(final A result, final int nextPos){
			this.result = result;
			this.nextPos = nextPos;
		}
    }

    public ParseResult<Expression> parseExp(final int startPos) throws ParseException {
        return null;
    }

    public ParseResult<Statement> parseStmt(final int startPos) throws ParseException{
		return null;
	}
    
    public ParseResult<ClassDef> parseClassDefinition(final int startPos) throws ParseException{
        String className;
        String parentClass;
        ArrayList<VarDec> fields = new ArrayList<VarDec>();

        int currPos = startPos;
        if(tokens[currPos] instanceof ClassKeywordToken){
            currPos++;
        } else {
            throw new ParseException("should not have started parsing a ClassDefinition that doesn't start with a ClassKeywordToken, but somehow we did");
        }
        if(tokens[currPos] instanceof IdentifierToken){
            className = ((IdentifierToken) tokens[startPos + 1]).name;
            currPos++;
        } else {
            throw new ParseException("Expected IdentifierToken for className; received " + tokens[startPos + 1].toString());
        }
        if(tokens[currPos] instanceof ColonToken){
            currPos++;
            if(tokens[currPos] instanceof IdentifierToken){
                parentClass = ((IdentifierToken) tokens[startPos + 1]).name;
                currPos++;
            } else {
                throw new ParseException("Expected IdentifierToken for parent class; received " + tokens[startPos + 1].toString());
            }
        }
        if(tokens[currPos] instanceof LCurlyToken){
            currPos++;
        } else {
            throw new ParseException("Expected LCurlyToken; received " + tokens[startPos + 1].toString());
        }


		return null;
	}

    public Program parseProgram() throws ParseException{
        int nextPos = 0;
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        while(tokens[nextPos] instanceof ClassKeywordToken){
            ParseResult<ClassDef> result = parseClassDefinition(nextPos);
            classDefs.add(result.result);
            nextPos = result.nextPos;
        }

        final ParseResult<Expression> result = parseExp(nextPos);

        if (result.nextPos == tokens.length) {
            return new Program(classDefs, result.result);
        } else {
            throw new ParseException("extra tokens at end");
        }

    }

}