package parser;

import java.util.ArrayList;
import parser.statements.Statement;

public class ClassDef{

    public final String className;
    public final String parent;
    public final ArrayList<Statement> fields;
    public final Constructor constructor;
    public final ArrayList<MethodDef> methods;

    public ClassDef(final String className, final String parent, 
            final ArrayList<Statement> fields, 
            final Constructor constructor, final ArrayList<MethodDef> methods){
        this.className = className;
        this.parent = parent;
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

    public ClassDef(final String className, final ArrayList<Statement> fields, 
           final Constructor constructor, final ArrayList<MethodDef> methods){
        this.className = className;
        this.parent = null;
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

}