package compiler;

import parser.*;
import parser.statements.*;
import parser.expressions.*;
import tokenizer.*;
import tokenizer.tokens.*;
import tokenizer.tokens.keywords.*;
import tokenizer.tokens.operatortokens.*;
import typechecker.*;
import typechecker.types.*;
import codeGenerator.*;

import java.io.*;
import java.util.*;

public class Compiler {

    public static String inputFilename;
    public static String outputFilename;

    public static String readFile() {
        Scanner console = new Scanner(System.in);

        File sourceFile = null;
        Scanner inputStream = null;
        String inputFileContents = "";
        while (inputFileContents.equals("")) {
            try {
                System.out.println("Enter filename: ");
                ;
                inputFilename = console.next();
                sourceFile = new File(inputFilename);
                inputStream = new Scanner(sourceFile);
                while (inputStream.hasNext()) {
                    inputFileContents += inputStream.next();
                }
            } catch (Exception e) {
                System.out.println("Error file not found.");
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                console.close();
            }
        }
        return inputFileContents;
    }

    public static void writeFile(String contents) throws IOException{
        //Get filename from input filename: find .cool, remove substring, add new .c
        FileWriter writer = null;
        try{
        int extensionStart = inputFilename.indexOf(".");
        outputFilename = inputFilename.substring(0, extensionStart);
        outputFilename += ".c";
        File outputFile = new File(outputFilename);
        writer = new FileWriter(outputFile);
        writer.write(contents);
        }
        catch(Exception e){
            System.out.println("Error writing to file.");
        }finally{
            if(writer != null){
                writer.close();
            }
        }
    }

    public static void main(String[] args){
        //src file->lexer->parser->typechecker->codegen->output file
        try{
            List<Token> tokens = Lexer.lex(readFile());
            Parser parser = new Parser((Token[])tokens.toArray());
            Program inputProgram = parser.parseProgram(); 
            new Typechecker(inputProgram); //type checking handled in constructor
            CodeGenerator codegen = new CodeGenerator(inputProgram);
            String generatedCode = codegen.getCode();
            writeFile(generatedCode);

            
            System.out.println("Successfully compiled to " + outputFilename);
        }
        catch (Exception e){
            System.out.println("Error compiling." + e.getMessage());//needs more debugging text
        }
        finally{
        }
    }

}