package parser;

import java.util.ArrayList;
import parser.expressions.Expression;

public class Program{

    public final ArrayList<ClassDef> classDefs;

    public final Expression entryPoint;

    public Program(final ArrayList<ClassDef> classDefs, final Expression entryPoint){
        this.classDefs = classDefs;
        this.entryPoint = entryPoint;
    }
}