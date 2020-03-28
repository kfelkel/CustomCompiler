package parser;

import java.util.ArrayList;

public class ClassDef{

    String className;
    String parent;
    ArrayList<VarDec> fields;
    Constructor constructor;
    ArrayList<MethodDef> methods;

    public ClassDef(final String className, final String parent, final ArrayList<VarDec> fields, 
           final Constructor constructor, final ArrayList<MethodDef> methods){
        this.className = className;
        this.parent = parent;
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

    public ClassDef(final String className, final ArrayList<VarDec> fields, 
           final Constructor constructor, final ArrayList<MethodDef> methods){
        this.className = className;
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

}