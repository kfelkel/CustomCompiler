package parser;

import java.util.ArrayList;
import parser.expressions.Expression;

public class Program{

    public final ArrayList<ClassDef> classDefs;

    MethodDef entryPoint;

    public Program(final ArrayList<ClassDef> classDefs, final MethodDef entryPoint){
        this.classDefs = classDefs;
        this.entryPoint = entryPoint;
    }
}