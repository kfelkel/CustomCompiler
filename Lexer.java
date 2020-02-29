import java.util.List;
import java.util.Scanner;

import tokens.*;
import tokens.keywords.*;
import tokens.operatortokens.*;

import java.util.ArrayList;


public class Lexer {
    public static String getString(String s, int i){
        int j = i;
        for (; j < s.length();){
            if (Character.isLetter(s.charAt(j)) || Character.isDigit(s.charAt(j))){
                j++;
            }else if(s.charAt(j)=='"'){
                return s.substring(i,j);
            }else{
                return s.substring(i,j);
            }
        }
        return s.substring(i, j);
    }
    public static String getInt(String s, int i){
        int j = i;
        for (; j < s.length();){
            if (Character.isDigit(s.charAt(j))){
                j++;
            } else {
                return s.substring(i, j);
            }          
        }
        return s.substring(i, j);
    }
    public static List<Token> lex(String input){
        List<Token> result = new ArrayList<Token>();
        for (int i = 0; i < input.length();){
            if(Character.isDigit((input.charAt(i)))){
                if(Character.isWhitespace(input.charAt(i))){
                    i++;
                }else{
                    String atom = getInt(input, i);
                    i += atom.length();
                    result.add(new IntegerToken(Integer.parseInt(atom)));
                }
            }else if(input.charAt(i)=='('){
                result.add(new LeftParenToken());
                i++;
            }else if(input.charAt(i)==')'){
                result.add(new RightParenToken());
                i++;
            }else if(input.charAt(i)=='{'){
                result.add(new LCurlyToken());
                i++;
            }else if(input.charAt(i)=='}'){
                result.add(new RCurlyToken());
                i++;
            }
            else if(input.charAt(i)==';'){
                result.add(new LeftParenToken());
                i++;
            }
            else if(input.charAt(i)=='.'){
                result.add(new PeriodToken());
                i++;
            }
            else if(input.charAt(i)==','){
                result.add(new CommaToken());
                i++;
            }else if(input.charAt(i)=='+'){
                result.add(new PlusToken());
                i++;
            }else if(input.charAt(i)=='-'){
                result.add(new MinusToken());
                i++;
            }else if(input.charAt(i)=='*'){
                result.add(new MultiplicationToken());
                i++;
            }else if(input.charAt(i)=='/'){
                result.add(new DivisionToken());
                i++;
            }else if(input.charAt(i)=='%'){
                result.add(new ModulusToken());
                i++;
            }else if(input.charAt(i)=='<'){
                if( i+1 < input.length()&&input.charAt(i+1)=='='){
                    result.add(new LessThanOrEqualToken());
                    i++;
                    i++;
                }else{
                    result.add(new LessThanToken());
                    i++;
                }
            }else if(input.charAt(i)=='>'){
                if(i+1 < input.length()&&input.charAt(i+1)=='='){
                    result.add(new GreaterThanOrEqualToken());
                    i++;
                    i++;
                }else{
                    result.add(new GreaterThanToken());
                    i++;
                }
            }else if(input.charAt(i)=='='){
                if(i+1 < input.length()&&input.charAt(i+1)=='='){
                    result.add(new EqualEqualToken());
                    i++;
                    i++;
                }else{
                    result.add(new EqualToken());
                    i++;
                }
            }else if(input.charAt(i)=='"'){
                i++;
                String atom = getString(input, i);
                i += atom.length();
                result.add(new StringLiteralToken(atom));
                i++;
            }else{
                if(Character.isWhitespace(input.charAt(i))){
                    i++;
                }else{
                    String atom = getString(input, i);
                    i += atom.length();
                    if(atom.equals("String")){
                        result.add(new StringKeywordToken());
                    }else if(atom.equals("class")){
                        result.add(new ClassKeywordToken());
                    }else if(atom.equals("constructor")){
                        result.add(new ConstructorKeywordToken());
                    }else if(atom.equals("else")){
                        result.add(new ElseToken());
                    }else if(atom.equals("for")){
                        result.add(new ForToken());
                    }else if(atom.equals("if")){
                        result.add(new IfToken());
                    }else if(atom.equals("Int")){
                        result.add(new IntKeywordToken());
                    }else if(atom.equals("new")){
                        result.add(new NewToken());
                    }else if(atom.equals("println")){
                        result.add(new PrintlnToken());
                    }else if(atom.equals("print")){
                        result.add(new PrintToken());
                    }else if(atom.equals("return")){
                        result.add(new ReturnToken());
                    }else if(atom.equals("this")){
                        result.add(new ThisToken());
                    }else if(atom.equals("Void")){
                        result.add(new VoidToken());
                    }else if(atom.equals("while")){
                        result.add(new WhileToken());
                    }else if(atom.equals("Bool")){
                        result.add(new BoolKeywordToken());
                    }else{
                        result.add(new IdentifierToken(atom));
                    }
                }

            }
        }
        return result;
    }
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String name = in.nextLine();

        List<Token> tokens = lex(name);
        for(Token t : tokens) {
            System.out.println(t);
        }
    }
}

