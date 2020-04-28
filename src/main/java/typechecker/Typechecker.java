package typechecker;

import java.util.HashMap;
import java.util.Map;

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

    public void typecheckProgram(final Program program) throws IllTypedException {
        for (final ClassDef classdef : program.classDefs) {
            typecheckClass(classdef);
        }
        typecheckFunction(program.entryPoint);
    } // typecheckProgram

    public void typecheckClass(final ClassDef classdef) {
  //To-Do
    }

    public void typecheckFunction(MethodDef function) throws IllTypedException {
    // final Map<Variable, Type> gamma = new HashMap<Variable, Type>();
    // for (final FormalParameter formalParam : function.formalParams) {
    //     if (!gamma.containsKey(formalParam.theVariable)) {
    //         gamma.put(formalParam.theVariable, formalParam.theType);
    //     } else {
    //         throw new IllTypedException("Duplicate formal parameter name");
    //     }
    // }

    // final Map<Variable, Type> finalGamma = typecheckStmts(gamma, false, function.body);
    // final Type actualReturnType = typeof(finalGamma, function.returnExp);
    // if (!actualReturnType.equals(function.returnType)) {
    //     throw new IllTypedException("return type mismatch");
    }
} // typecheckFunction

