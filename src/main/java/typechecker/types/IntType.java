package typechecker.types;

public class IntType implements Type{
    @Override
    public boolean equals(final Object other) {
        return other instanceof IntType;
    }

public String toString(){
    return "Int";
}
}