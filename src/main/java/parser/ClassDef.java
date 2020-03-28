package parser;

import java.util.ArrayList;

public class ClassDef{

    String parent;
    ArrayList<VarDec> fields;
    Constructor constructor;
    ArrayList<MethodDef> methods;

    public ClassDef(final String parent, final ArrayList<VarDec> fields, 
           final Constructor constructor, final ArrayList<MethodDef> methods){
        this.parent = parent;
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

    public ClassDef(final ArrayList<VarDec> fields, 
           final Constructor constructor, final ArrayList<MethodDef> methods){
        this.fields = fields;
        this.constructor = constructor;
        this.methods = methods;
    }

}