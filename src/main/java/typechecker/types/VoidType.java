package typechecker.types;

public class VoidType implements Type{

    public boolean equals(final Object other) {
        return other instanceof VoidType;
    }


    @Override
    public String toString() {
        return "Void";
    }

}