# CustomCompiler
## Contributors
* Kyle Felkel
* Tommy Avetisyan
* Matthew Estes

## Description
This is a compiler that coverts a custom grammar into C code. The grammar is Java/C++ like and supports basic programming features. The language includes support for simple Object-Oriented Programming (OOP) including encapsulation, inheritance, and polymorphism.
The grammar for the language, code snippets, limitations, and explanations are found in the 'Language Documentation.pdf' file.

## Tools
Compiler was written in Java using an OOP approach. 
Tests are written in Java using JUnit library.
Program compilation and test execution was handled using Maven and the 'pom.xml' configuration file.

## Implementation
The overall compiler takes an input string that denotes a file location containing source code. This source code is then read in, converted to tokens in the tokenizer, converted to an Abstract Syntax Tree in the parser, checked for validity in the typechecker, and finally converted to C code in the code generator. OOP is implemented in C using structs and pointers. 
