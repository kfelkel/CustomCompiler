package parser;

import java.util.ArrayList;
import parser.expressions.Expression;

public class Program{

    ArrayList<ClassDef> classDefs;

    Expression entryPoint;

    public Program(final ArrayList<ClassDef> classDefs, final Expression entryPoint){
        this.classDefs = classDefs;
        this.entryPoint = entryPoint;
    }
}