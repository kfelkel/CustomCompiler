package tokens;
public class StringToken implements Token{
    public final String string;

    public StringToken(final String string){
        this.string = string;
    }
    public String toString(){
        return "StringToken(" + string + ")";
    }
}