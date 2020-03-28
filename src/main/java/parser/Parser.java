package parser;

import java.util.ArrayList;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;
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

    public ParseResult<BlockStmt> parseBlockStmt(final int startPos) throws ParseException{
        return null;
    }
    
    public ParseResult<VariableDeclarationStmt> parseVarDec(final int startPos) throws ParseException{
        String type;
        String name;
        Expression value = null;

        int nextPos = startPos;

        if(tokens[nextPos] instanceof IntKeywordToken){
            type = "Int";
            nextPos++;
        } else if(tokens[nextPos] instanceof BoolKeywordToken){
            type = "Bool";
            nextPos++;
        } else if(tokens[nextPos] instanceof StringKeywordToken){
            type = "String";
            nextPos++;
        } else if(tokens[nextPos] instanceof IdentifierToken){
            type = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected token indicating variable type; received " + tokens[nextPos].toString());
        }
        if(tokens[nextPos] instanceof IdentifierToken){
            name = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected IdentifierToken for variable name; received " + tokens[nextPos].toString());
        }
        if(tokens[nextPos] instanceof EqualToken){
            nextPos++;
            ParseResult<Expression> result = parseExp(nextPos);
            value = result.result;
            nextPos = result.nextPos;
        }

        if(value != null){
            return new ParseResult<VariableDeclarationStmt>(new VariableDeclarationStmt(type, name, value), nextPos);
        } else{
            return new ParseResult<VariableDeclarationStmt>(new VariableDeclarationStmt(type, name), nextPos);
        }
    }

    public ParseResult<Constructor> parseConstructor(final int startPos) throws ParseException{
        int nextPos = startPos;
        ArrayList<VariableDeclarationStmt> parameters = new ArrayList<VariableDeclarationStmt>();
        BlockStmt body;

        if(tokens[nextPos] instanceof ClassKeywordToken){
            nextPos++;
        } else {
            throw new ParseException("should not have started parsing a Constructor that doesn't start with a ConstructorKeywordToken, but somehow we did");
        }
        if(tokens[nextPos] instanceof LeftParenToken){
            nextPos++;
        } else {
            throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
        }
        // get parameters
        if(!(tokens[nextPos] instanceof RightParenToken)){
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            parameters.add(result.result);
            nextPos = result.nextPos;
            while(tokens[nextPos] instanceof CommaToken){
                nextPos++;
                result = parseVarDec(nextPos);
                parameters.add(result.result);
                nextPos = result.nextPos;
            }
        }
        if(!(tokens[nextPos] instanceof RightParenToken)){
            nextPos++;
        } else {
            throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
        }
        // get body
        ParseResult<BlockStmt> result = parseBlockStmt(nextPos);
        body = result.result;
        nextPos = result.nextPos;

        return new ParseResult<Constructor>(new Constructor(parameters, body), nextPos);
    }

    public ParseResult<MethodDef> parseMethodDef(final int startPos) throws ParseException{
        int nextPos = startPos;

        String type;
        String name;
        ArrayList<VariableDeclarationStmt> parameters = new ArrayList<VariableDeclarationStmt>();
        BlockStmt body;

        // get type
        if(tokens[nextPos] instanceof IntKeywordToken){
            type = "Int";
            nextPos++;
        } else if(tokens[nextPos] instanceof BoolKeywordToken){
            type = "Bool";
            nextPos++;
        } else if(tokens[nextPos] instanceof StringKeywordToken){
            type = "String";
            nextPos++;
        } else if(tokens[nextPos] instanceof VoidToken){
            type = "Void";
            nextPos++;
        } else if(tokens[nextPos] instanceof IdentifierToken){
            type = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected token indicating method return type; received " + tokens[nextPos].toString());
        }
        // get method name
        if(tokens[nextPos] instanceof IdentifierToken){
            name = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected IdentifierToken for method name; received " + tokens[nextPos].toString());
        }
        // get parameters
        if(tokens[nextPos] instanceof LeftParenToken){
            nextPos++;
        } else {
            throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
        }
        if(!(tokens[nextPos] instanceof RightParenToken)){
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            parameters.add(result.result);
            nextPos = result.nextPos;
            while(tokens[nextPos] instanceof CommaToken){
                nextPos++;
                result = parseVarDec(nextPos);
                parameters.add(result.result);
                nextPos = result.nextPos;
            }
        }
        if(!(tokens[nextPos] instanceof RightParenToken)){
            nextPos++;
        } else {
            throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
        }
        // get body
        ParseResult<BlockStmt> result = parseBlockStmt(nextPos);
        body = result.result;
        nextPos = result.nextPos;

        return new ParseResult<MethodDef>(new MethodDef(type, name, parameters, body), nextPos);
    }
    
    public ParseResult<ClassDef> parseClassDefinition(final int startPos) throws ParseException{
        String className;
        String parentClass = "";
        ArrayList<VariableDeclarationStmt> fields = new ArrayList<VariableDeclarationStmt>();
        Constructor constructor;
        ArrayList<MethodDef> methods = new ArrayList<MethodDef>();

        int nextPos = startPos;
        if(tokens[nextPos] instanceof ClassKeywordToken){
            nextPos++;
        } else {
            throw new ParseException("should not have started parsing a ClassDefinition that doesn't start with a ClassKeywordToken, but somehow we did");
        }
        if(tokens[nextPos] instanceof IdentifierToken){
            className = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected IdentifierToken for className; received " + tokens[nextPos].toString());
        }
        // check for parent class
        if(tokens[nextPos] instanceof ColonToken){
            nextPos++;
            if(tokens[nextPos] instanceof IdentifierToken){
                parentClass = ((IdentifierToken) tokens[nextPos]).name;
                nextPos++;
            } else {
                throw new ParseException("Expected IdentifierToken for parent class; received " + tokens[nextPos].toString());
            }
        }
        if(tokens[nextPos] instanceof LCurlyToken){
            nextPos++;
        } else {
            throw new ParseException("Expected LCurlyToken; received " + tokens[nextPos].toString());
        }
        // get fields
        while(!(tokens[nextPos] instanceof ConstructorKeywordToken)){
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            fields.add(result.result);
            nextPos = result.nextPos;
            if(tokens[nextPos] instanceof SemicolonToken){
                nextPos++;
            } else {
                throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
            }
        }
        // get constructor
        if(tokens[nextPos] instanceof ConstructorKeywordToken){
            ParseResult<Constructor> result = parseConstructor(nextPos);
            constructor = result.result;
            nextPos = result.nextPos;
        } else {
            throw new ParseException("should not have reached here if next token wasn't a ConstructorKeywordToken, but somehow we did");
        }
        // get methods
        while(!(tokens[nextPos] instanceof RCurlyToken)){
            ParseResult<MethodDef> result = parseMethodDef(nextPos);
            methods.add(result.result);
            nextPos = result.nextPos;
        }
        if(tokens[nextPos] instanceof RCurlyToken){
            nextPos++;
        } else {
            throw new ParseException("should not have reached here if next token wasn't a RCurlyToken, but somehow we did");
        }

		if(parentClass.isEmpty()){
            return new ParseResult<ClassDef>(new ClassDef(className, fields, constructor, methods), nextPos);
        } else {
            return new ParseResult<ClassDef>(new ClassDef(className, parentClass, fields, constructor, methods), nextPos);
        }
	}

    public Program parseProgram() throws ParseException{
        int nextPos = 0;
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        while(tokens[nextPos] instanceof ClassKeywordToken){
            ParseResult<ClassDef> result = parseClassDefinition(nextPos);
            classDefs.add(result.result);
            nextPos = result.nextPos;
        }

        //final ParseResult<Expression> result = parseExp(nextPos);
        final ParseResult<MethodDef> result = parseMethodDef(nextPos);

        if (result.nextPos == tokens.length) {
            return new Program(classDefs, result.result);
        } else {
            throw new ParseException("extra tokens at end");
        }

    }

}