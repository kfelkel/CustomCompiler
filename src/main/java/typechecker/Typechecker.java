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
        //typecheckMain(program.entryPoint);
    } // typecheckProgram

    public void typecheckClass(final ClassDef classdef) throws IllTypedException {
        // To-Do
        // typecheck fields (no duplicates)
        final Map<String, Type> variables = new HashMap<String, Type>();

        for (final VariableDeclarationStmt parameter : classdef.fields) {
            if (!variables.containsKey(parameter.name)) {
                Type paramType = convertStringToType(parameter.type);
                variables.put(parameter.name, paramType);
            } else {
                throw new IllTypedException("Duplicate parameter name");
            }
        }
        // check inheritance (child doesnt inherit itself/parent class defined
        if (classdef.parent != null) {
            if (classdef.className.equals(classdef.parent))
                throw new IllTypedException("Class inheriting itself");
            if (!classDefinitions.containsKey(classdef.className))
                throw new IllTypedException("Parent class not defined");
        }
        // check constructor parameters
        final Map<String, Type> constructorParameters = new HashMap<String, Type>();
        for (final VariableDeclarationStmt parameter : classdef.constructor.parameters) {
            if (!constructorParameters.containsKey(parameter.name)) {
                Type paramType = convertStringToType(parameter.type);
                constructorParameters.put(parameter.name, paramType);
            } else {
                throw new IllTypedException("Duplicate parameter name");
            }
        }
        //check constructor statements 
        //TO-DO

        //check methods for duplicates
        final Map<String, Type> methods = new HashMap<String, Type>()
        for (final MethodDef method : classdef.methods) {
            if (!methods.containsKey(method.name)) {
                Type paramType = convertStringToType(method.type);
                methods.put(method.name, paramType);
            } else {
                throw new IllTypedException("Duplicate function name");
            }
        }
        //check method statements 
        //TO-DO
        for (final MethodDef method : classdef.methods) {
        typecheckFunction(method, methods, variables);
        }
    }

    //add parameters for functions, variables
    public void typecheckFunction(MethodDef function, Map<String, Type> classMethods, Map<String, Type> classFields) throws IllTypedException {

        final Map<String, Type> gamma = new HashMap<String, Type>();//?

        for (final VariableDeclarationStmt parameter : function.parameters) {
            if (!gamma.containsKey(parameter.name)) {
                Type paramType = convertStringToType(parameter.type);
                gamma.put(parameter.name, paramType);
            } else {
                throw new IllTypedException("Duplicate parameter name");
            }
        }

        final Map<VariableExp, Type> finalGamma = typecheckStmts(gamma, false, function.body);
        final Type actualReturnType = typeof(finalGamma, function.returnExp);
        if (!actualReturnType.equals(function.type)) {// need to convert String type to Type
            throw new IllTypedException("return type mismatch");
        }

    }
}
