package typechecker.types;

public class BoolType implements Type{
    @Override
    public boolean equals(final Object other) {
        return other instanceof BoolType;
    }

    public String toString(){
        return "Bool";
    }
}