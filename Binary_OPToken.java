public class Binary_OPToken implements Token {
    public final String value;

    public Binary_OPToken(final String value) {
        this.value = value;
    }
    public String toString(){
        return "Binary_OPToken(" + value + ")";
    }
}