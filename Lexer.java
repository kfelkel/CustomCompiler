import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import Token_Classes.*;

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
            }else{
                switch (input.charAt(i)){
                    case '(':{
                        result.add(new LeftParenToken());
                        i++;
                        break;
                    }
                    case ')':{
                        result.add(new RightParenToken());
                        i++;
                        break;
                    }
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '%':{
                        result.add(new Binary_OPToken(String.valueOf(input.charAt(i))));
                        i++;
                        break;
                    }
                    case '<':
                    case '>':
                    case '=':{
                        if(Character.isWhitespace(input.charAt(i))){
                            i++;
                        }else{
                            String atom = getDoubleOp(input, i);
                            i += atom.length();
                            result.add(new Binary_OPToken(atom));
                        }
                        break;
                    }
                    case '"':{

                    }
                    default:
                        if(Character.isWhitespace(input.charAt(i))){
                            i++;
                        }else{
                            String atom = getAtom(input, i);
                            i += atom.length();
                            if (Arrays.asList(reserved).contains(atom)){
                                result.add(new ReservedToken(atom));
                            }else{
                                result.add(new StringToken(atom));
                            }
                        }
                        break;
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

