package typechecker;

import java.util.HashMap;
import java.util.Map;
import typechecker.types.*;
import parser.*;
import parser.statements.*;
import parser.expressions.*;

public class Typechecker {
    private final Map<String, ClassDef> classDefinitions;

    public Typechecker(final Program program) throws IllTypedException {

        classDefinitions = new HashMap<String, ClassDef>();

        for (final ClassDef classdef : program.classDefs) {
            if (!classDefinitions.containsKey(classdef.className)) {
                classDefinitions.put(classdef.className, classdef);
            } else {
                throw new IllTypedException("Duplicate class name: " + classdef.className);
            }
        }

    } // Typechecker

    public Type convertStringToType(String stringtype) {
        if (stringtype.equals("Int"))
            return new IntType();
        if (stringtype.equals("Bool"))
            return new BoolType();
        if (stringtype.equals("String"))
            return new StringType();
        if (stringtype.equals("Void"))
            return new VoidType();
        else
            return null; // add objects
    }

    public void typecheckProgram(final Program program) throws IllTypedException {
        for (final ClassDef classdef : program.classDefs) {
            typecheckClass(classdef);
        }
        typecheckFunction(program.entryPoint);
    } // typecheckProgram

    public void typecheckClass(final ClassDef classdef) {
        // To-Do
    }

    public void typecheckFunction(MethodDef function) throws IllTypedException {

        final Map<String, Type> gamma = new HashMap<String, Type>();

        for (final VariableDeclarationStmt parameter : function.parameters) {
            if (!gamma.containsKey(parameter.name)) {
                Type paramType = convertStringToType(parameter.type);
                gamma.put(parameter.name, paramType);
            } else {
                throw new IllTypedException("Duplicate formal parameter name");
            }
        }

        final Map<VariableExp, Type> finalGamma = typecheckStmts(gamma, false, function.body);
        final Type actualReturnType = typeof(finalGamma, function.returnExp);
        if (!actualReturnType.equals(function.type)) {// need to convert String type to Type
            throw new IllTypedException("return type mismatch");
        }

    }
}
