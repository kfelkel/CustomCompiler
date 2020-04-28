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

        final Map<String, Type> finalGamma = typecheckStmts(gamma, false, function.body);
        final Type actualReturnType = typeof(finalGamma, function.returnExp);
        if (!actualReturnType.equals(function.type)) {// need to convert String type to Type
            throw new IllTypedException("return type mismatch");
        }

    }
    public Map<String, Type> typecheckStmts(Map<String, Type> gamma,
                                              final boolean breakAndContinueOk,
                                              final BlockStmt body)
        throws IllTypedException {
        for (final Statement s : body.body) {
            //                  result gamma
            // initial          []
            // int x = 7;       [x -> int]
            // int y = x + 3;   [x -> int, y -> int]
            // int z = y + x;   [x -> int, y -> int, z -> int]
            gamma = typecheckStmt(gamma, breakAndContinueOk, s);
        }
    
        return gamma;
    } // typecheckStmts
    public Map<String, Type> typecheckStmt(final Map<String, Type> gamma,
                                             final boolean breakAndContinueOk,
                                             final Statement s)
        throws IllTypedException {
        // x
        if (s instanceof BlockStmt) {
            final BlockStmt asFor = (BlockStmt)s;
            typecheckStmts(newGamma, true, asFor.body);
        } else if (s instanceof ForStmt) {
            // for(int x = 0; x < 10; x++) { s* }
            // gamma: []
            // newGamma: [x -> int]
            // for(int x = 0; x < 10; int y = 10) {
            //   int y = 0;
            //   int z = x + y;
            //   [x -> int, y -> int, z -> int]
            // }
            final ForStmt asFor = (ForStmt)s;
            final Map<String, Type> newGamma = typecheckStmt(gamma, breakAndContinueOk, asFor.initializer);
            final Type guardType = typeof(newGamma, asFor.condition);
            if (guardType instanceof BoolType) {
                typecheckStmt(newGamma, breakAndContinueOk, asFor.incrementor);
                typecheckStmts(newGamma, true, asFor.body);
            } else {
                throw new IllTypedException("Guard in for must be boolean");
            }
            return gamma;
        } else if (s instanceof IfElseStmt || s instanceof IfStmt) {
            final IfElseStmt asFor = (IfElseStmt)s;
            
        } else if (s instanceof PrintlnStmt) {
            // Check expression inside
        } else if (s instanceof ReturnStmt) {
            // Check expression inside
        } else if (s instanceof ReturnVoidStmt) {
            // Check expression inside
        } else if (s instanceof EmptyStmt) {
            return gamma;
        } else {
            assert(false);
            throw new IllTypedException("Unrecognized statement");
        }
    } // typecheckStmt
    public Type typeof(final Map<String, Type> gamma, final Expression e)
        throws IllTypedException {
        if (e instanceof IntegerExp) {
            return new IntType();
        } else if (e instanceof BooleanExp) {
            return new BoolType();
        } else if (e instanceof BinopExp) { // &&, +, or <
            final BinopExp asBinop = (BinopExp)e;
            if (asBinop.op instanceof AndBOP) {
                final Type leftType = typeof(gamma, asBinop.left);
                final Type rightType = typeof(gamma, asBinop.right);

                if (leftType instanceof BoolType &&
                    rightType instanceof BoolType) {
                    return new BoolType();
                } else {
                    throw new IllTypedException("left or right in && is not a boolean");
                }
            } else if (asBinop.op instanceof PlusBOP) {
                final Type leftType = typeof(gamma, asBinop.left);
                final Type rightType = typeof(gamma, asBinop.right);

                if (leftType instanceof IntType &&
                    rightType instanceof IntType) {
                    return new IntType();
                } else {
                    throw new IllTypedException("left or right in + is not an int");
                }
            } else if (asBinop.op instanceof LessThanBOP) {
                final Type leftType = typeof(gamma, asBinop.left);
                final Type rightType = typeof(gamma, asBinop.right);
                
                if (leftType instanceof IntType &&
                    rightType instanceof IntType) {
                    return new BoolType();
                } else {
                    throw new IllTypedException("left or right in < is not an int");
                }
            } else {
                assert(false);
                throw new IllTypedException("should be unreachable; unknown operator");
            }
        } else if (e instanceof VariableExp) {
            // final Map<Variable, Type> gamma
            final VariableExp asVar = (VariableExp)e;
            if (gamma.containsKey(asVar.x)) {
                final Type tau = gamma.get(asVar.x);
                return tau;
            } else {
                throw new IllTypedException("Not in scope: " + asVar.x);
            }
        } else if (e instanceof HigherOrderFunctionDef) {
            // (x: Int) => x + 1
            // Int => Int
            final HigherOrderFunctionDef asFunc = (HigherOrderFunctionDef)e;
            final Map<Variable, Type> copy = makeCopy(gamma);
            copy.put(asFunc.paramName, asFunc.paramType);
            final Type bodyType = typeof(copy, asFunc.body);
            return new FunctionType(asFunc.paramType, bodyType);
        } else if (e instanceof CallHigherOrderFunction) {
            // e1(e2)
            // e1: (x: Int) => x + 1 [Int => Int]
            // e2: 7 [Int]
            // e1(e2): [Int]
            final CallHigherOrderFunction asCall = (CallHigherOrderFunction)e;
            final Type hopefullyFunction = typeof(gamma, asCall.theFunction);
            final Type hopefullyParameter = typeof(gamma, asCall.theParameter);
            if (hopefullyFunction instanceof FunctionType) {
                final FunctionType asFunc = (FunctionType)hopefullyFunction;
                if (asFunc.paramType.equals(hopefullyParameter)) {
                    return asFunc.returnType;
                } else {
                    throw new IllTypedException("Parameter type mismatch");
                }
            } else {
                throw new IllTypedException("call of non-function");
            }
        } else if (e instanceof CallFirstOrderFunction) {
            final CallFirstOrderFunction asCall = (CallFirstOrderFunction)e;
            if (functionDefinitions.containsKey(asCall.functionName)) {
                final FirstOrderFunctionDefinition fdef = functionDefinitions.get(asCall.functionName);
                checkFormalParams(gamma, fdef.formalParams, asCall.actualParams);
                return fdef.returnType;
            } else {
                throw new IllTypedException("function does not exist: " + asCall.functionName);
            }
        } else {
            assert(false);
            throw new IllTypedException("unrecognized expression");
        }
    } // typeof
}
