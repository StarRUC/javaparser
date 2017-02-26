package com.github.javaparser;

import java.io.*;
import com.github.javaparser.ast.*;

import java.util.List;



public class TestCuPrinter {

    public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(args[0]);

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        //XJU Test:
        System.out.println("Test: print the COMMENTS:");
        System.out.println(cu.getComments().toString());
        System.out.println("");
        
        System.out.println("Test: print the IMPORTS:");
        System.out.println(cu.getImports().toString());
        System.out.println("");
       
        System.out.println("Test: print the PACKAGE:");
        System.out.println(cu.getPackage().toString());
        System.out.println("");
        
        System.out.println("Test: print the TYPES:");
        System.out.println(cu.getTypes().toString());
        System.out.println("");
       
        // prints the resulting compilation unit to default system output
        //System.out.println("Print the WHOLE CU:");
        //System.out.println(cu.toString());
    }
}