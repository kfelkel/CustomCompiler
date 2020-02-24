import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * Lexical analyzer for Scheme-like minilanguage:
 * (define (foo x) (bar (baz x)))
 */
public class Lexer {
    public static enum Type {
        // This Scheme-like language has three token types:
        // open parens, close parens, and an "atom" type
        PAREN, ATOM, BINARY_OP, INT;
    }
    public static class Token {
        public final Type t;
        public final String c; // contents mainly for atom tokens
        
        // could have column and line number fields too, for reporting errors later
        public Token(Type t, String c) {
            this.t = t;
            this.c = c;
            
        }
        public String toString() {
            if(t == Type.ATOM) {
                return "ATOM(" + c + ")";
            }
            if(t == Type.BINARY_OP){
                return "Binary_op(" + c + ")";
            }
            if(t == Type.PAREN){
                return "PAREN(" + c + ")";
            }
            return t.toString();
        }
    }

    /*
     * Given a String, and an index, get the atom starting at that index
     */
    public static String getAtom(String s, int i) {
        int j = i;
        for( ; j < s.length(); ) {
            if(Character.isLetter(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }

    public static List<Token> lex(String input) {

        List<Token> result = new ArrayList<Token>();
        for(int i = 0; i < input.length(); ) {
            switch(input.charAt(i)) {
                case '(':
                case ')':{
                    result.add(new Token(Type.PAREN, String.valueOf(input.charAt(i))));
                    i++;
                    break;
                }
                case '+':
                case '-':    
                case '*':
                case '/':
                case '%':
                case '<':
                case '>':{
                    result.add(new Token(Type.BINARY_OP, String.valueOf(input.charAt(i))));
                    i++;
                    break;
                }  
                default:
                    if(Character.isWhitespace(input.charAt(i))) {
                        i++;
                    } else {
                        String atom = getAtom(input, i);
                        i += atom.length();
                        result.add(new Token(Type.ATOM, atom));
                    }
                    break;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String name = in.nextLine();

        List<Token> tokens = lex(name);
        for(Token t : tokens) {
            System.out.println(t);
        }
    }
}