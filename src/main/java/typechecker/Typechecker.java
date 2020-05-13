package typechecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Iterator;

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
        typecheckProgram(program);
    } // Typechecker

    public Type convertStringToType(String stringtype) throws IllTypedException {
        if (stringtype.equals("Int"))
            return new IntType();
        else if (stringtype.equals("Bool"))
            return new BoolType();
        else if (stringtype.equals("String"))
            return new StringType();
        else if (stringtype.equals("Void"))
            return new VoidType();
        else if (classDefinitions.containsKey(stringtype))
            return new ObjectType(stringtype); // add objects
        else
            throw new IllTypedException("Unrecognized type: " + stringtype);
    }

    public static Map<String, Type> makeCopy(final Map<String, Type> gamma) {
        final Map<String, Type> copy = new HashMap<String, Type>();
        copy.putAll(gamma);
        return copy;
    }

    public void typecheckProgram(final Program program) throws IllTypedException {
        for (final ClassDef classdef : program.classDefs) {
            typecheckClass(classdef);
        }
        typecheckFunction(program.entryPoint, new HashMap<String, MethodDef>(), new HashMap<String, Type>());
    } // typecheckProgram

    public void typecheckClass(final ClassDef classdef) throws IllTypedException {
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
        // check methods for duplicates
        final Map<String, MethodDef> methods = new HashMap<String, MethodDef>();
        for (final MethodDef method : classdef.methods) {
            if (!methods.containsKey(method.name)) {
                methods.put(method.name, method);
            } else {
                throw new IllTypedException("Duplicate function name");
            }
        }

        // check constructor statements
        // TO-DO
        typecheckStmts(variables, methods, classdef.constructor.body);

        // check method statements

        for (final MethodDef method : classdef.methods) {
            typecheckFunction(method, methods, variables);
        }
    }

    // add parameters for functions, variables
    public void typecheckFunction(MethodDef function, Map<String, MethodDef> classMethods,
            Map<String, Type> classFields) throws IllTypedException {

        final Map<String, Type> variables = makeCopy(classFields);// ?

        for (final VariableDeclarationStmt parameter : function.parameters) {
            if (!variables.containsKey(parameter.name)) {
                Type paramType = convertStringToType(parameter.type);
                variables.put(parameter.name, paramType);
            } else {
                throw new IllTypedException("Duplicate parameter name");
            }
        } // add check for bad type declaration

        // TO-DO
        final Map<String, Type> finalGamma = typecheckStmts(variables, classMethods, function.body);
        final Type actualReturnType = typecheckExp(finalGamma, classMethods, function.returnExp);
        if (!actualReturnType.equals(convertStringToType(function.type))) {
            throw new IllTypedException("return type mismatch");
        }
    }

    public Map<String, Type> typecheckStmts(Map<String, Type> gamma, Map<String, MethodDef> classMethods,
            final BlockStmt body) throws IllTypedException {
        for (final Statement s : body.body) {
            // result gamma
            // initial []
            // int x = 7; [x -> int]
            // int y = x + 3; [x -> int, y -> int]
            // int z = y + x; [x -> int, y -> int, z -> int]
            gamma = typecheckStmt(gamma, classMethods, s);
        }
        return gamma;
    } // typecheckStmts

    public Map<String, Type> typecheckStmt(final Map<String, Type> gamma, Map<String, MethodDef> classMethods,
            final Statement s) throws IllTypedException {
        // x
        if (s instanceof BlockStmt) {
            final BlockStmt asBlock = (BlockStmt) s;
            typecheckStmts(gamma, classMethods, new BlockStmt(asBlock.body));
        } else if (s instanceof ForStmt) {
            // for(int x = 0; x < 10; x++) { s* }
            // gamma: []
            // newGamma: [x -> int]
            // for(int x = 0; x < 10; int y = 10) {
            // int y = 0;
            // int z = x + y;
            // [x -> int, y -> int, z -> int]
            // }
            final ForStmt asFor = (ForStmt) s;
            final Map<String, Type> newGamma = typecheckStmt(gamma, classMethods, asFor.initializer);
            final Type guardType = typecheckExp(newGamma, classMethods, asFor.condition);
            if (guardType instanceof BoolType) {
                typecheckStmt(newGamma, classMethods, asFor.incrementor);
                typecheckStmts(newGamma, classMethods, asFor.body);
            } else {
                throw new IllTypedException("Guard in for must be boolean");
            }
            return gamma;
        } else if (s instanceof IfElseStmt) {
            final IfElseStmt asIf = (IfElseStmt) s;
            final Type guardType = typecheckExp(gamma, classMethods, asIf.condition);
            if (guardType instanceof BoolType && asIf.falseBranch != null) {
                typecheckStmts(gamma, classMethods, asIf.trueBranch);
                typecheckStmts(gamma, classMethods, asIf.falseBranch);
            } else if (guardType instanceof BoolType) {
                typecheckStmts(gamma, classMethods, asIf.trueBranch);
            } else {
                throw new IllTypedException("Guard in If must be boolean");
            }
            return gamma;
        } else if (s instanceof IfStmt) {
            final IfStmt asIf = (IfStmt) s;
            final Type guardType = typecheckExp(gamma, classMethods, asIf.condition);
            if (guardType instanceof BoolType) {
                typecheckStmts(gamma, classMethods, asIf.trueBranch);
            } else {
                throw new IllTypedException("Guard in If must be boolean");
            }
            return gamma;
        } else if (s instanceof PrintlnStmt) {
            final PrintlnStmt asPrintln = (PrintlnStmt) s;
            final Type guardType = typecheckExp(gamma, classMethods, asPrintln.output);
            if (!(guardType instanceof Type)) {
                throw new IllTypedException("Println must have Expression");
            }
            return gamma;
        } else if (s instanceof PrintStmt) {
            final PrintStmt asPrint = (PrintStmt) s;
            final Type guardType = typecheckExp(gamma, classMethods, asPrint.output);
            if (!(guardType instanceof Type)) {
                throw new IllTypedException("Print must have Expression");
            }
            return gamma;
        } else if (s instanceof ReturnStmt) {
            final ReturnStmt asReturn = (ReturnStmt) s;
            final Type guardType = typecheckExp(gamma, classMethods, asReturn.value);
            // xxxxxxget type of function return
            if (!(guardType instanceof Type)) {
                throw new IllTypedException("Function must have return value of " + guardType);
            }
            return gamma;
        } else if (s instanceof ReturnVoidStmt) {
            final VoidType guardType = new VoidType();
            // xxxxxxget type of function return
            if (!(guardType instanceof Type)) {
                throw new IllTypedException("Function must have return value of " + guardType);
            }
            return gamma;
        } else if (s instanceof WhileStmt) {
            final WhileStmt asWhile = (WhileStmt) s;
            final Type guardType = typecheckExp(gamma, classMethods, asWhile.condition);
            if (guardType instanceof Type) {
                typecheckStmts(gamma, classMethods, asWhile.body);
            } else {
                throw new IllTypedException("Guard in While must be boolean");
            }
            return gamma;
        } else if (s instanceof VariableDeclarationStmt) {
            VariableDeclarationStmt asVarDec = (VariableDeclarationStmt) s;
            final Map<String, Type> newgamma = makeCopy(gamma);
            Type varType = convertStringToType(asVarDec.type);
            if (!newgamma.containsKey(asVarDec.name)) {
                if (asVarDec.value == null) {
                    newgamma.put(asVarDec.name, convertStringToType(asVarDec.type));
                    return newgamma;
                } else if (varType.equals(typecheckExp(gamma, classMethods, asVarDec.value))) {

                    newgamma.put(asVarDec.name, convertStringToType(asVarDec.type));
                    return newgamma;
                } else {
                    throw new IllTypedException("Assigning invalid type to variable");
                }
            } else {
                throw new IllTypedException("Declaring a variable twice");
            }

        } else if (s instanceof VariableAssignmentStmt) {
            VariableAssignmentStmt asVarDec = (VariableAssignmentStmt) s;
            Type varType = gamma.get(asVarDec.name);
            Type valueType = typecheckExp(gamma, classMethods, asVarDec.value);
            if (gamma.containsKey(asVarDec.name)) {
                if (varType.equals(valueType)) {
                    return gamma;
                } else if (valueType instanceof ObjectType) {
                    String childName = ((ObjectType) valueType).name;

                    if (classDefinitions.containsKey(childName)) {
                        String childinheritance = classDefinitions.get(childName).parent;
                        if (childinheritance.length() > 0) {
                            Type parentType = convertStringToType(childinheritance);
                            if (varType.equals(parentType)) {
                                return gamma;
                            } else
                                throw new IllTypedException("Assigning invalid type to variable");
                        } else
                            throw new IllTypedException("Assigning invalid type to variable");
                    } else
                        throw new IllTypedException("Assigning invalid type to variable");
                } else
                    throw new IllTypedException("Assigning invalid type to variable");

            } else {
                throw new IllTypedException("Assigning value to nonexistent variable");
            }
        }

        throw new IllTypedException("Unrecognized statement");

    } // typecheckStmt

    public Type typecheckExp(final Map<String, Type> gamma, final Map<String, MethodDef> classMethods,
            final Expression e) throws IllTypedException {
        // Check for integers
        if (e instanceof IntegerExp) {
            return new IntType();
        }
        if (e instanceof StringExp) {
            return new StringType();
        }
        if (e instanceof VariableExp) {
            VariableExp asVar = (VariableExp) e;
            return gamma.get(asVar.name);
        }
        // Check for Binary Operators
        else if (e instanceof PlusExp) {
            PlusExp asBinOpExp = (PlusExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new IllTypedException("left or right in Plus is not an int");
            }
        } else if (e instanceof MinusExp) {
            MinusExp asBinOpExp = (MinusExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new IllTypedException("left or right in minus is not an int");
            }
        } else if (e instanceof ModulusExp) {
            ModulusExp asBinOpExp = (ModulusExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new IllTypedException("left or right in Modulus is not an int");
            }
        } else if (e instanceof MultiplicationExp) {
            MultiplicationExp asBinOpExp = (MultiplicationExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new IllTypedException("left or right in Multiplication is not an int");
            }
        } else if (e instanceof DivisionExp) {
            DivisionExp asBinOpExp = (DivisionExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new IntType();
            } else {
                throw new IllTypedException("left or right in Division is not an int");
            }
        } else if (e instanceof GreaterThanExp) {
            GreaterThanExp asBinOpExp = (GreaterThanExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new IllTypedException("left or right in GreaterThan is not an int");
            }
        } else if (e instanceof GreaterThanOrEqualExp) {
            GreaterThanOrEqualExp asBinOpExp = (GreaterThanOrEqualExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new IllTypedException("left or right in GreaterThanOrEqual is not an int");
            }
        } else if (e instanceof LessThanExp) {
            LessThanExp asBinOpExp = (LessThanExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new IllTypedException("left or right in LessThan is not an int");
            }
        } else if (e instanceof LessThanOrEqualExp) {// <,<=
            LessThanOrEqualExp asBinOpExp = (LessThanOrEqualExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new IllTypedException("left or right in LessThanOrEqual is not an int");
            }
        } else if (e instanceof EqualEqualExp) {
            EqualEqualExp asBinOpExp = (EqualEqualExp) e;
            final Type leftType = typecheckExp(gamma, classMethods, asBinOpExp.exp1);
            final Type rightType = typecheckExp(gamma, classMethods, asBinOpExp.exp2);

            if (leftType instanceof IntType && rightType instanceof IntType) {
                return new BoolType();
            } else {
                throw new IllTypedException("left or right in EqualEqual is not an int");
            }
        }
        // Check for Variables
        else if (e instanceof VariableExp) {
            // final Map<Variable, Type> gamma
            final VariableExp asVar = (VariableExp) e;
            if (gamma.containsKey(asVar.name)) {
                final Type tau = gamma.get(asVar.name);
                return tau;
            } else {
                throw new IllTypedException("Not in scope: " + asVar.name);
            }
        }
        // Check for MethodCall
        else if (e instanceof MethodCallExp) {
            final MethodCallExp asCall = (MethodCallExp) e;
            String objectName = asCall.objectName;
            if (objectName == "this" && classMethods.containsKey(asCall.name)) {
                final MethodDef fdef = classMethods.get(objectName);
                checkParams(gamma, fdef.parameters, asCall.parameters, classMethods);
                return convertStringToType(fdef.type);
            } else if (classDefinitions.containsKey(objectName)) {
                // TO-DO
            } else {
                throw new IllTypedException("function does not exist: " + asCall.name);
            }
        } else if (e instanceof NewExp) {
            NewExp asNewExp = (NewExp) e;
            if (classDefinitions.containsKey(asNewExp.classname)) {
                return convertStringToType(asNewExp.classname);
            } else {
                throw new IllTypedException("unrecognized classname in 'new' expression");
            }

        }
        throw new IllTypedException("unrecognized expression" + e);

    } // typecheckExp

    private void checkParams(final Map<String, Type> gamma, final List<VariableDeclarationStmt> formalParams,
            final List<Expression> actualParams, final Map<String, MethodDef> classMethods) throws IllTypedException {
        if (formalParams.size() == actualParams.size()) {
            final Iterator<VariableDeclarationStmt> formalIterator = formalParams.iterator();
            final Iterator<Expression> actualIterator = actualParams.iterator();
            while (formalIterator.hasNext() && actualIterator.hasNext()) {
                final VariableDeclarationStmt formalParam = formalIterator.next();
                final Expression actualParam = actualIterator.next();
                final Type actualType = typecheckExp(gamma, classMethods, actualParam);
                if (!actualType.equals(convertStringToType(formalParam.type))) {
                    throw new IllTypedException("Parameter type mismatch");
                }
            }

            assert (!formalIterator.hasNext());
            assert (!actualIterator.hasNext());
        } else {
            throw new IllTypedException("wrong number of arguments");
        }
    }
}