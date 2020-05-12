package codeGenerator;

import typechecker.types.*;
import parser.*;
import parser.statements.*;
import parser.expressions.*;

import java.util.*;

public class CodeGenerator {
    //private static ArrayList<String> INCLUDE = new ArrayList<String>(); //holds all #include files, worry later
    private ArrayList<String> FunctionHeaders = new ArrayList<String>();
    private ArrayList<String> Classes = new ArrayList<String>();
    private ArrayList<String> Main = new ArrayList<String>(); 
    private Program myProgram;

    CodeGenerator(Program myProgram){
        this.myProgram = myProgram;
        generateProgramCode();
    }

    public String getCode(){
        String completeCode = "";



        return completeCode;
    }

    private void generateProgramCode(){

    }

}