import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Lexer {
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
                case '%':
                case '<':
                case '>':
                case '=':{
                    result.add(new Binary_OPToken(String.valueOf(input.charAt(i))));
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