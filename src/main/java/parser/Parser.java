package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import tokenizer.Lexer;
import tokenizer.TokenizationException;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;
import parser.expressions.*;
import parser.statements.*;

public class Parser {

    private Token[] tokens;
    private List<Token> subtokens;

    // tokens and subtokens are the same thing, just in Array vs List form. The
    // discrepancy is because
    // different people wrote different parts of the code, and we decided to just
    // leave it instead of
    // bothering to refactor
    public Parser(Token[] tokens) {
        this.tokens = tokens;
        this.subtokens = new ArrayList<Token>(Arrays.asList(tokens));
    }

    public class ParseResult<A> {
        public final A result;
        public final int nextPos;

        public ParseResult(final A result, final int nextPos) {
            this.result = result;
            this.nextPos = nextPos;
        }
    }

    public ParseResult<Expression> parseExp(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;

        int parenBalance = 0;
        while (!(subtokens.get(nextPos) instanceof SemicolonToken) && nextPos != subtokens.size()) {
            if (subtokens.get(nextPos) instanceof LeftParenToken) {
                parenBalance++;
            }
            // if we find a RightParenToken without a matching LeftParenToken, that means
            // we've gotten out
            // of the expression
            if (subtokens.get(nextPos) instanceof RightParenToken) {
                parenBalance--;
                if (parenBalance < 0) {
                    break;
                }
            }

            nextPos++;
        }
        return new ParseResult<Expression>(parseSubExp(getExp(startPos, nextPos, subtokens)), nextPos);
    }

    public ArrayList<Token> getExp(int left, int right, List<Token> array) {
        ArrayList<Token> subExp = new ArrayList<Token>();
        while (left != right) {
            subExp.add(array.get(left));
            left++;
        }
        return (subExp);
    }

    public Expression parseSubExp(ArrayList<Token> array) throws ParseException, TokenizationException {
        int right = array.size() - 1;
        int left = array.size() - 1;
        int paren = 0;
        int ops = 0;
        int neg = 0;
        int nums = 0;

        // Comparison Ops
        for (left = 0; left < array.size() - 1; left++) {
            if (array.get(left) instanceof LessThanToken) {
                return new LessThanExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof GreaterThanToken) {
                return new GreaterThanExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof LessThanOrEqualToken) {
                return new LessThanOrEqualExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof GreaterThanOrEqualToken) {
                return new GreaterThanOrEqualExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof EqualEqualToken) {
                return new EqualEqualExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
        }
        // find even amount of Parens
        for (int i = array.size() - 1; i >= 0; i--) {
            if (array.get(i) instanceof RightParenToken) {
                paren++;
            }
            if (array.get(i) instanceof LeftParenToken) {
                paren--;
            }
        }
        if (paren % 2 != 0) {
            // System.out.println(Arrays.toString(array.toArray()));
            throw new ParseException("Missing Parentheses");
        }
        // Check for missing ops or variables
        for (int i = array.size() - 1; i >= 0; i--) {
            if (array.get(i) instanceof IntegerToken || array.get(i) instanceof IdentifierToken) {
                nums++;
            }
            if ((array.get(i) instanceof MinusToken)) {
                if (i == 0) {
                    neg++;
                } else if (array.get(i - 1) instanceof OperatorToken) {
                    neg++;
                }

            }
            if (array.get(i) instanceof OperatorToken) {
                ops++;
            }

        }
        // ops=ops-neg;

        // if(ops>=nums){
        // throw new ParseException("Missing Int or Variable");
        // }else if(nums>(ops+1)){
        // throw new ParseException("Missing Operation");
        // }
        // Plus/Minus
        for (left = array.size() - 1; left >= 0; left--) {
            if (array.get(left) instanceof RightParenToken) {
                paren++;
            }
            if (array.get(left) instanceof LeftParenToken) {
                paren--;
            }
            if (array.get(left) instanceof PlusToken && paren == 0) {
                return new PlusExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof MinusToken && paren == 0 && left != 0) {
                if (array.get(left - 1) instanceof OperatorToken) {

                } else {
                    return new MinusExp(parseSubExp(getExp(0, left, array)),
                            parseSubExp(getExp(left + 1, right + 1, array)));
                }

            }
        }
        // Mult/Divide
        for (left = array.size() - 1; left >= 0; left--) {
            if (array.get(left) instanceof RightParenToken) {
                paren += 1;
            }
            if (array.get(left) instanceof LeftParenToken) {
                paren--;
            }
            if (array.get(left) instanceof MultiplicationToken && paren == 0) {
                return new MultiplicationExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof DivisionToken && paren == 0) {
                return new DivisionExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
            if (array.get(left) instanceof ModulusToken && paren == 0) {
                return new ModulusExp(parseSubExp(getExp(0, left, array)),
                        parseSubExp(getExp(left + 1, right + 1, array)));
            }
        }
        // New expression
        int parsenew = 0;
        if (array.get(parsenew) instanceof NewToken && array.size() - 1 > parsenew) {
            String classname;
            List<Expression> parameters = new ArrayList<Expression>();
            parsenew++;
            if (array.get(parsenew) instanceof IdentifierToken && array.size() - 1 > parsenew) {
                classname = ((IdentifierToken) array.get(parsenew)).name;
                parsenew++;
            } else {
                throw new ParseException("Missing Classname after new keyword");
            }
            if (array.get(parsenew) instanceof LeftParenToken && array.size() - 1 > parsenew) {
                parsenew++;
            } else {
                throw new ParseException("Missing LeftParen after classname");
            }
            if (array.get(parsenew) instanceof RightParenToken) {
                return new NewExp(classname, parameters);
            } else {
                for (int i = parsenew; i < array.size(); i++) {
                    if (array.get(i) instanceof CommaToken || array.get(i) instanceof RightParenToken) {
                        parameters.add(parseSubExp(getExp(parsenew, i, array)));
                        parsenew = i + 1;
                    }
                }
            }
            return new NewExp(classname, parameters);
        }
        // Methodcall()
        int parsemethod = 0;
        if (array.get(parsemethod) instanceof IdentifierToken && array.size() - 1 > parsemethod) {
            String objectName;
            String name;
            List<Expression> parameters = new ArrayList<Expression>();
            objectName = ((IdentifierToken) array.get(parsemethod)).name;
            parsemethod++;
            if (array.get(parsemethod) instanceof DotOperatorToken && array.size() - 1 > parsemethod) {
                parsemethod++;
                name = ((IdentifierToken) array.get(parsemethod)).name;
                parsemethod++;
            } else {
                throw new ParseException("Missing Identifier after dot operator");
            }
            if (array.get(parsemethod) instanceof LeftParenToken && array.size() - 1 > parsemethod) {
                parsemethod++;
            } else {
                throw new ParseException("Missing LeftParen after identifier");
            }
            if (array.get(parsemethod) instanceof RightParenToken && array.size() - 1 > parsemethod) {
                return new MethodCallExp(objectName, name, parameters);
            } else {
                for (int i = parsemethod; i < array.size(); i++) {

                    if (array.get(i) instanceof CommaToken || array.get(i) instanceof RightParenToken) {
                        parameters.add(parseSubExp(getExp(parsemethod, i, array)));
                        parsemethod = i + 1;
                    }
                }
            }
            return new MethodCallExp(objectName, name, parameters);
        }
        // Paren
        for (left = array.size() - 1; left > 0; left--) {
            if (array.get(left) instanceof RightParenToken) {
                return new ParenthesizedExp(parseSubExp(getExp(1, array.size() - 1, array)));
            }
        }
        // This
        if (array.get(0) instanceof ThisToken && array.size() - 1 > 0) {
            if (array.get(1) instanceof DotOperatorToken && array.size() - 1 > 1) {
                return new ThisExp(parseSubExp(getExp(2, array.size(), array)));
            }
        }
        // Negative Int
        if (array.size() == 2 && array.get(0) instanceof MinusToken) {
            IntegerToken numb = (IntegerToken) array.get(left + 1);
            return new IntegerExp(numb.value * -1);
        }
        // Single int
        if (array.size() == 1 && array.get(0) instanceof IntegerToken) {
            return new IntegerExp((IntegerToken) array.get(left));
        }
        if (array.size() == 1 && array.get(0) instanceof IdentifierToken) {
            return new VariableExp((IdentifierToken) array.get(left));
        }
        if (array.size() == 1 && array.get(0) instanceof StringLiteralToken) {
            return new StringExp(((StringLiteralToken) array.get(parsemethod)).string);
        }
        return null;
    }

    public ParseResult<Statement> parseStmt(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;
        Statement myStmt = null;

        if (tokens[nextPos] instanceof PrintToken) {
            nextPos++;
            if (tokens[nextPos] instanceof LeftParenToken) {
                nextPos++;
                ParseResult<Expression> expressionResult = parseExp(nextPos);
                myStmt = new PrintStmt(expressionResult.result);
                nextPos = expressionResult.nextPos;
            } else {
                throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
            }
            if (tokens[nextPos] instanceof RightParenToken) {
                nextPos++;
            } else {
                throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
            }
            if (tokens[nextPos] instanceof SemicolonToken) {
                nextPos++;
            } else {
                throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
            }
        } else if (tokens[nextPos] instanceof PrintlnToken) {
            nextPos++;
            if (tokens[nextPos] instanceof LeftParenToken) {
                nextPos++;
                ParseResult<Expression> expressionResult = parseExp(nextPos);
                myStmt = new PrintlnStmt(expressionResult.result);
                nextPos = expressionResult.nextPos;
            } else {
                throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
            }
            if (tokens[nextPos] instanceof RightParenToken) {
                nextPos++;
            } else {
                throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
            }
            if (tokens[nextPos] instanceof SemicolonToken) {
                nextPos++;
            } else {
                throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
            }
        } else if (tokens[nextPos] instanceof ForToken) {
            ParseResult<ForStmt> result = parseForStmt(nextPos);
            myStmt = result.result;
            nextPos = result.nextPos;
        } else if (tokens[nextPos] instanceof WhileToken) {
            ParseResult<WhileStmt> result = parseWhileStmt(nextPos);
            myStmt = result.result;
            nextPos = result.nextPos;
        } else if (tokens[nextPos] instanceof IfToken) {
            ParseResult<Statement> result = parseIfStmt(nextPos);
            myStmt = result.result;
            nextPos = result.nextPos;
        } else if (tokens[nextPos] instanceof LCurlyToken) {
            ParseResult<BlockStmt> result = parseBlockStmt(nextPos);
            myStmt = result.result;
            nextPos = result.nextPos;
        } else if (tokens[nextPos] instanceof IntKeywordToken || tokens[nextPos] instanceof BoolKeywordToken
                || tokens[nextPos] instanceof StringKeywordToken) {
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            myStmt = result.result;
            nextPos = result.nextPos;
            if (tokens[nextPos] instanceof SemicolonToken) {
                nextPos++;
            } else {
                throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
            }
        } else if (tokens[nextPos] instanceof IdentifierToken) {
            if (tokens[nextPos + 1] instanceof IdentifierToken) {
                ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
                myStmt = result.result;
                nextPos = result.nextPos;
                if (tokens[nextPos] instanceof SemicolonToken) {
                    nextPos++;
                } else {
                    throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
                }
            } else {
                ParseResult<VariableAssignmentStmt> result = parseVarAssignment(nextPos);
                myStmt = result.result;
                nextPos = result.nextPos;
                if (tokens[nextPos] instanceof SemicolonToken) {
                    nextPos++;
                } else {
                    throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
                }
            }
        } else if (tokens[nextPos] instanceof ReturnToken) {
            nextPos++;
            if (tokens[nextPos] instanceof SemicolonToken) {
                nextPos++;
                myStmt = new ReturnVoidStmt();
                // methodDef.returnlist.add(mystmt);
            } else {
                ParseResult<Expression> expressionResult = parseExp(nextPos);
                myStmt = new ReturnStmt(expressionResult.result);
                nextPos = expressionResult.nextPos;
                if (tokens[nextPos] instanceof SemicolonToken) {
                    nextPos++;
                } else {
                    throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
                }

            }
        } else {
            throw new ParseException(
                    "Unexpected Token " + tokens[nextPos].toString() + " received while parsing a statement.");
        }

        return new ParseResult<Statement>(myStmt, nextPos);
    }

    public ParseResult<ForStmt> parseForStmt(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;
        Statement initialization;
        Expression condition;
        Statement incrementation;
        Statement body;

        if (tokens[nextPos] instanceof ForToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected ForToken; received " + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof LeftParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
        }

        // parse the initializer
        ParseResult<Statement> stmtResult = parseStmt(nextPos);
        initialization = stmtResult.result;
        nextPos = stmtResult.nextPos;

        // parse the condition
        ParseResult<Expression> expResult = parseExp(nextPos);
        condition = expResult.result;
        nextPos = expResult.nextPos;
        if (tokens[nextPos] instanceof SemicolonToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
        }

        // parse the incrementor
        stmtResult = parseStmt(nextPos);
        incrementation = stmtResult.result;
        nextPos = stmtResult.nextPos;
        if (tokens[nextPos] instanceof RightParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
        }
        stmtResult = parseStmt(nextPos);
        body = stmtResult.result;
        nextPos = stmtResult.nextPos;

        return new ParseResult<ForStmt>(new ForStmt(initialization, condition, incrementation, (BlockStmt) body),
                nextPos);
    }

    public ParseResult<WhileStmt> parseWhileStmt(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;
        Expression condition;
        BlockStmt body;

        if (tokens[nextPos] instanceof WhileToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected ForToken; received " + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof LeftParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
        }
        ParseResult<Expression> expResult = parseExp(nextPos);
        condition = expResult.result;
        nextPos = expResult.nextPos;
        if (tokens[nextPos] instanceof RightParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
        }
        ParseResult<Statement> stmtResult = parseStmt(nextPos);
        body = (BlockStmt) stmtResult.result;
        nextPos = stmtResult.nextPos;

        return new ParseResult<WhileStmt>((new WhileStmt(condition, body)), nextPos);
    }

    public ParseResult<Statement> parseIfStmt(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;
        Expression condition;
        BlockStmt trueBranch;
        BlockStmt falseBranch;

        if (tokens[nextPos] instanceof IfToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected ForToken; received " + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof LeftParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
        }
        ParseResult<Expression> expResult = parseExp(nextPos);
        condition = expResult.result;
        nextPos = expResult.nextPos;
        if (tokens[nextPos] instanceof RightParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
        }
        ParseResult<Statement> stmtResult = parseStmt(nextPos);
        trueBranch = (BlockStmt) stmtResult.result;
        nextPos = stmtResult.nextPos;
        if (tokens[nextPos] instanceof ElseToken) {
            nextPos++;
            stmtResult = parseStmt(nextPos);
            falseBranch = (BlockStmt) stmtResult.result;
            nextPos = stmtResult.nextPos;
            return new ParseResult<Statement>(new IfElseStmt(condition, trueBranch, falseBranch), nextPos);
        }
        return new ParseResult<Statement>(new IfStmt(condition, trueBranch), nextPos);
    }

    public ParseResult<BlockStmt> parseBlockStmt(final int startPos) throws ParseException, TokenizationException {
        ArrayList<Statement> block = new ArrayList<Statement>();

        int nextPos = startPos;

        if (tokens[nextPos] instanceof LCurlyToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected LCurlyToken; received " + tokens[nextPos].toString());
        }

        while (!(tokens[nextPos] instanceof RCurlyToken)) {
            ParseResult<Statement> result = parseStmt(nextPos);
            System.out.println(result.result.toString());
            block.add(result.result);
            nextPos = result.nextPos;
        }
        if (tokens[nextPos] instanceof RCurlyToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected RCurlyToken; received " + tokens[nextPos].toString());
        }

        return new ParseResult<BlockStmt>(new BlockStmt(block), nextPos);
    }

    public ParseResult<VariableDeclarationStmt> parseVarDec(final int startPos)
            throws ParseException, TokenizationException {
        String type;
        String name;
        Expression value = null;

        int nextPos = startPos;

        if (tokens[nextPos] instanceof IntKeywordToken) {
            type = "int";
            nextPos++;
        } else if (tokens[nextPos] instanceof BoolKeywordToken) {
            type = "bool";
            nextPos++;
        } else if (tokens[nextPos] instanceof StringKeywordToken) {
            type = "String";
            nextPos++;
        } else if (tokens[nextPos] instanceof IdentifierToken) {
            type = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected token indicating variable type; received " + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof IdentifierToken) {
            name = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException(
                    "Expected IdentifierToken for variable name; received " + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof EqualToken) {
            nextPos++;
            ParseResult<Expression> result = parseExp(nextPos);
            value = result.result;
            nextPos = result.nextPos;
        }
        // Note: Does not need to look for a semi-colon; parseStmt() does that after
        // calling this function

        if (value != null) {
            return new ParseResult<VariableDeclarationStmt>(new VariableDeclarationStmt(type, name, value), nextPos);
        } else {
            return new ParseResult<VariableDeclarationStmt>(new VariableDeclarationStmt(type, name), nextPos);
        }
    }

    public ParseResult<VariableAssignmentStmt> parseVarAssignment(final int startPos)
            throws ParseException, TokenizationException {
        String name;
        Expression value = null;

        int nextPos = startPos;

        if (tokens[nextPos] instanceof IdentifierToken) {
            name = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException(
                    "Expected IdentifierToken for variable name; received " + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof EqualToken) {
            nextPos++;
            ParseResult<Expression> result = parseExp(nextPos);
            value = result.result;
            nextPos = result.nextPos;
        } else {
            throw new ParseException(
                    "Expected EqualToken; received " + tokens[nextPos].toString());
        }
        // Note: Does not need to look for a semi-colon; parseStmt() does that after
        // calling this function

        return new ParseResult<VariableAssignmentStmt>(new VariableAssignmentStmt(name, value), nextPos);
    }

    public ParseResult<Constructor> parseConstructor(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;
        ArrayList<VariableDeclarationStmt> parameters = new ArrayList<VariableDeclarationStmt>();
        BlockStmt body;

        if (tokens[nextPos] instanceof ConstructorKeywordToken) {
            nextPos++;
        } else {
            throw new ParseException(
                    "should not have started parsing a Constructor that doesn't start with a ConstructorKeywordToken, but somehow we have a "
                            + tokens[nextPos].toString());
        }
        if (tokens[nextPos] instanceof LeftParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected LeftParenToken; received " + tokens[nextPos].toString());
        }
        // get parameters
        if (!(tokens[nextPos] instanceof RightParenToken)) {
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            parameters.add(result.result);
            nextPos = result.nextPos;
            while (tokens[nextPos] instanceof CommaToken) {
                nextPos++;
                result = parseVarDec(nextPos);
                parameters.add(result.result);
                nextPos = result.nextPos;
            }
        }
        if (tokens[nextPos] instanceof RightParenToken) {
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

    public ParseResult<MethodDef> parseMethodDef(final int startPos) throws ParseException, TokenizationException {
        int nextPos = startPos;

        String type;
        String name;
        ArrayList<VariableDeclarationStmt> parameters = new ArrayList<VariableDeclarationStmt>();
        BlockStmt body;
        Expression returnExp = null;

        // get type

        if (tokens[nextPos] instanceof IntKeywordToken) {
            type = "int";
            nextPos++;
        } else if (tokens[nextPos] instanceof BoolKeywordToken) {
            type = "bool";
            nextPos++;
        } else if (tokens[nextPos] instanceof StringKeywordToken) {
            type = "String";
            nextPos++;
        } else if (tokens[nextPos] instanceof VoidToken) {
            type = "Void";
            nextPos++;
        } else if (tokens[nextPos] instanceof IdentifierToken) {
            type = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException(
                    "Expected token indicating method return type; received " + tokens[nextPos].toString());
        }
        // get method name

        if (tokens[nextPos] instanceof IdentifierToken) {
            name = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException(
                    "Expected IdentifierToken for method name; received " + tokens[nextPos].toString());
        }
        // get parameters
        if (tokens[nextPos] instanceof LeftParenToken) {
            nextPos++;
        } else {
            throw new ParseException(
                    "Expected LeftParenToken; received " + tokens[nextPos].toString() + " at position " + nextPos);
        }
        if (!(tokens[nextPos] instanceof RightParenToken)) {
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            parameters.add(result.result);
            nextPos = result.nextPos;
            while (tokens[nextPos] instanceof CommaToken) {
                nextPos++;
                result = parseVarDec(nextPos);
                parameters.add(result.result);
                nextPos = result.nextPos;
            }
        }
        if (tokens[nextPos] instanceof RightParenToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected RightParenToken; received " + tokens[nextPos].toString());
        }

        // get
        // body/////////////////////////////////////////////////////////////////////////
        ParseResult<BlockStmt> result = parseBlockStmt(nextPos);
        body = result.result;
        nextPos = result.nextPos;

        List<Statement> bodyStmts = body.body;
        if (bodyStmts.size() > 0) {
            Statement returnStmt = bodyStmts.get(bodyStmts.size() - 1);
            if (returnStmt instanceof ReturnStmt) {
                returnExp = ((ReturnStmt) returnStmt).value;
                bodyStmts.remove(bodyStmts.size() - 1);
            } else if (returnStmt instanceof ReturnVoidStmt) {
                bodyStmts.remove(bodyStmts.size() - 1);
            } else {
                throw new ParseException("Missing Return statement");
            }
        } else {
            throw new ParseException("Missing Return statement");
        }
        return new ParseResult<MethodDef>(new MethodDef(type, name, parameters, (BlockStmt) body, returnExp), nextPos);
    }

    public ParseResult<ClassDef> parseClassDefinition(final int startPos) throws ParseException, TokenizationException {
        String className;
        String parentClass = "";
        ArrayList<VariableDeclarationStmt> fields = new ArrayList<VariableDeclarationStmt>();
        Constructor constructor;
        ArrayList<MethodDef> methods = new ArrayList<MethodDef>();

        int nextPos = startPos;
        if (tokens[nextPos] instanceof ClassKeywordToken) {
            nextPos++;
        } else {
            throw new ParseException(
                    "should not have started parsing a ClassDefinition that doesn't start with a ClassKeywordToken, but somehow we did");
        }
        if (tokens[nextPos] instanceof IdentifierToken) {
            className = ((IdentifierToken) tokens[nextPos]).name;
            nextPos++;
        } else {
            throw new ParseException("Expected IdentifierToken for className; received " + tokens[nextPos].toString());
        }
        // check for parent class
        if (tokens[nextPos] instanceof ColonToken) {
            nextPos++;
            if (tokens[nextPos] instanceof IdentifierToken) {
                parentClass = ((IdentifierToken) tokens[nextPos]).name;
                nextPos++;
            } else {
                throw new ParseException(
                        "Expected IdentifierToken for parent class; received " + tokens[nextPos].toString());
            }
        }
        if (tokens[nextPos] instanceof LCurlyToken) {
            nextPos++;
        } else {
            throw new ParseException("Expected LCurlyToken; received " + tokens[nextPos].toString());
        }
        // get fields
        while (!(tokens[nextPos] instanceof ConstructorKeywordToken)) {
            ParseResult<VariableDeclarationStmt> result = parseVarDec(nextPos);
            fields.add(result.result);
            nextPos = result.nextPos;
            if (tokens[nextPos] instanceof SemicolonToken) {
                nextPos++;
            } else {
                throw new ParseException("Expected SemicolonToken; received " + tokens[nextPos].toString());
            }
        }
        // get constructor
        if (tokens[nextPos] instanceof ConstructorKeywordToken) {
            ParseResult<Constructor> result = parseConstructor(nextPos);
            constructor = result.result;
            nextPos = result.nextPos;
        } else {
            throw new ParseException(
                    "should not have reached here if next token wasn't a ConstructorKeywordToken, but somehow we did");
        }
        // get methods

        while (!(tokens[nextPos] instanceof RCurlyToken)) {
            ParseResult<MethodDef> result = parseMethodDef(nextPos);
            methods.add(result.result);
            nextPos = result.nextPos;
        }

        if (tokens[nextPos] instanceof RCurlyToken) {
            nextPos++;
        } else {
            throw new ParseException(
                    "should not have reached here if next token wasn't a RCurlyToken, but somehow we did");
        }

        if (parentClass.isEmpty()) {
            return new ParseResult<ClassDef>(new ClassDef(className, fields, constructor, methods), nextPos);
        } else {
            return new ParseResult<ClassDef>(new ClassDef(className, parentClass, fields, constructor, methods),
                    nextPos);
        }
    }

    public Program parseProgram() throws ParseException, TokenizationException {
        int nextPos = 0;
        ArrayList<ClassDef> classDefs = new ArrayList<ClassDef>();
        while (tokens[nextPos] instanceof ClassKeywordToken) {
            ParseResult<ClassDef> result = parseClassDefinition(nextPos);
            classDefs.add(result.result);
            nextPos = result.nextPos;
        }

        // final ParseResult<Expression> result = parseExp(nextPos);
        final ParseResult<MethodDef> result = parseMethodDef(nextPos);

        if (result.nextPos == tokens.length) {
            return new Program(classDefs, result.result);
        } else {
            throw new ParseException("extra tokens at end");
        }

    }

}