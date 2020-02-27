import java.util.List;
import java.util.Scanner;

import tokens.*;
import tokens.keywords.*;
import tokens.operatortokens.*;

import java.util.ArrayList;
import java.util.Arrays;


public class Lexer {
    static String[] reserved = { "Int", "String", "Bool", 
                                "Void", "this", "print", 
                                "println", "new", "for", 
                                "while", "if", "else", 
                                "return", "public", "private",
                                "protected", "class", "constructor" };
    public static String getAtom(String s, int i){
        int j = i;
        for (; j < s.length();){
            if (Character.isLetter(s.charAt(j))){
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
    public static String getString(String s, int i){
        int j = i;
        for(; j < s.length();){
            if(s.charAt(j)=='"'){
                return s.substring(i,j);
            }else{
                j++;
            }
        }
        return s.substring(i,j);
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
    public static String getDoubleOp(String s, int i){
        int j = i;
        j++;
        for (; j< s.length();){
            if(Character.isWhitespace(s.charAt(i))){
                return s.substring(i, j);
            }else if(s.charAt(i+1)=='='){
                j++;
                return s.substring(i, j);
            }else{
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
                if(input.charAt(i+1)=='='){
                    result.add(new LessThanOrEqualToken());
                    i++;
                    i++;
                }else{
                    result.add(new LessThanToken());
                    i++;
                }
            }else if(input.charAt(i)=='>'){
                if(input.charAt(i+1)=='='){
                    result.add(new GreaterThanOrEqualToken());
                    i++;
                    i++;
                }else{
                    result.add(new GreaterThanToken());
                    i++;
                }
            }else if(input.charAt(i)=='='){
                if(input.charAt(i+1)=='='){
                    result.add(new EqualEqualToken());
                    i++;
                    i++;
                }else{
                    result.add(new EqualToken());
                    i++;
                }
            }else{
                if(Character.isWhitespace(input.charAt(i))){
                    i++;
                }else{
                    String atom = getAtom(input, i);
                    i += atom.length();
                    if (Arrays.asList(reserved).contains(atom)){

                    }else{
                        result.add(new StringToken(atom));
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

