package Token_Classes;
public interface ReservedWordToken implements Token{
    public final String reserved;
    public String toString(){
        return "ReservedToken(" + reserved + ")";
    }
}

public class 	IntegerToken	implements ReservedWordToken	{}
public class 	StringToken	implements ReservedWordToken	{}
public class 	BoolToken	implements ReservedWordToken	{}
public class 	VoidToken	implements ReservedWordToken	{}
public class 	ThisToken	implements ReservedWordToken	{}
public class 	PrintToken	implements ReservedWordToken	{}
public class 	PrintlnToken	implements ReservedWordToken	{}
public class 	NewToken	implements ReservedWordToken	{}
public class 	ForToken	implements ReservedWordToken	{}
public class 	WhileToken	implements ReservedWordToken	{}
public class 	IfToken	implements ReservedWordToken	{}
public class 	ElseToken	implements ReservedWordToken	{}
public class 	ReturnToken	implements ReservedWordToken	{}
public class 	PublicToken	implements ReservedWordToken	{}
public class 	PrivateToken	implements ReservedWordToken	{}
public class 	ProtectedToken	implements ReservedWordToken	{}
public class 	ClassToken	implements ReservedWordToken	{}
public class 	ConstructorToken	implements ReservedWordToken	{}


		


		

public class 	SemicolonToken	extends Token	{}
public class 	PeriodToken	extends Token	{}
public class 	CommaToken	extends Token	{}
public class 	QuotationToken	extends Token	{}

