package com.github.javaparser;

import java.io.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.List;



public class TestElements {

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

        // change the methods names and parameters
        iterateElements(cu);

        // prints the changed compilation unit
        //System.out.println(cu.toString());
    }

    private static void iterateElements(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
    		System.out.println("Each Type: " + type.getName());
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
        		//System.out.println("\tEach Member: " + member.toString());
            	
            	/*
            	List<AnnotationExpr> annos = member.getAnnotations();
        		System.out.println("\t\tEach Annotation:");
            	for (AnnotationExpr anno : annos){
            		System.out.println(anno.toString());
            	}
            	*/
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    System.out.println("\t\t\tMethod Name: " + method.getName());
                    System.out.println("\t\t\tMethod Modifiers: " + method.getModifiers());
                    System.out.println("\t\t\tMethod TypeParameters: " + method.getTypeParameters());
                    System.out.println("\t\t\tMethod Type: " + method.getType());
                    System.out.println("\t\t\tMethod Parameters: " + method.getParameters());
                    System.out.println("\t\t\tMethod ArrayCount: " + method.getArrayCount());
                    System.out.println("\t\t\tMethod NameExpr: " + method.getNameExpr());
                    System.out.println("\t\t\tMethod Body: " + method.getBody());

                    //changeMethod(method);
                }
                else {
                	if (member instanceof FieldDeclaration){
                		FieldDeclaration field = (FieldDeclaration) member;
                        //System.out.println("\t\t\tField Name: " + field.getName());
                        System.out.println("\t\t\tField Modifier: " + field.getModifiers());
                        System.out.println("\t\t\tField Type: " + field.getType());
                        System.out.println("\t\t\tField Type Annotations: " + field.getType().getAnnotations());
                        System.out.println("\t\t\tField Variables: " + field.getVariables());
                	}
                }
            }
        }
    }

    private static void changeMethod(MethodDeclaration n) {
        // change the name of the method to upper case
        n.setName(n.getName().toUpperCase());

        // create the new parameter
        Parameter newArg = ASTHelper.createParameter(ASTHelper.INT_TYPE, "value");

        // add the parameter to the method
        ASTHelper.addParameter(n, newArg);
    }
}