public class ReservedToken implements Token{
    public final String reserved;

    public ReservedToken(final String reserved){
        this.reserved = reserved;
    }
    public String toString(){
        return "ReservedToken(" + reserved + ")";
    }
}