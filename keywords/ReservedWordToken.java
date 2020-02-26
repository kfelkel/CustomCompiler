import tokens.Token;

package tokens.keywords;

public interface ReservedWordToken implements Token{
    public final String reserved;
    public String toString(){
        return "ReservedToken(" + reserved + ")";
    }
}