package tokens;
public class 	QuotationToken	implements Token	{
    public final String value;

    public QuotationToken(final String value) {
        this.value = value;
    }

    public String toString(){
        return "QuotationToken(\"" + value + "\")";
    }
}