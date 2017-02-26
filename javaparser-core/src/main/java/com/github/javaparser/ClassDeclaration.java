package com.github.javaparser;

import java.io.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.*;



public class ClassDeclaration {
	private String name;	
	private List<MyFieldDeclaration> myfields;
	private List<MethodDeclaration> methods;

	public ClassDeclaration() {
		myfields = new ArrayList<MyFieldDeclaration>();
		//hasClassFileFlags = new ArrayList<Boolean>();
		methods = new ArrayList<MethodDeclaration>();
	}
	
	public void setName(String inputString) {
		name = inputString;
	}
	
	public void addField(MyFieldDeclaration inputField) {
		myfields.add(inputField);
	}

	public void addMethod(MethodDeclaration inputMethod) {
		methods.add(inputMethod);
	}
	
	public String getName() {
		return name;
	}

	public List<MyFieldDeclaration> getFields() {
		return myfields;
	}

	public List<MethodDeclaration> getMethods() {
		return methods;
	}


}


class MyFieldDeclaration {
	private FieldDeclaration field;
	private String typeString;
	private boolean hasClassFileFlag;
	private boolean isMultipleFlag;
	
	public MyFieldDeclaration(FieldDeclaration inputField, String inputTypeString, boolean inputHasClassFileFlag, boolean inputIsMultipleFlag) {
		field = inputField;
		typeString = inputTypeString;
		hasClassFileFlag = inputHasClassFileFlag;
		isMultipleFlag = inputIsMultipleFlag;
	}
	
	public FieldDeclaration getField() {
		return field;
	}

	public String getTypeString() {
		return typeString;
	}

	public boolean getHasClassFileFlag() {
		return hasClassFileFlag;
	}

	public boolean getIsMultipleFlag() {
		return isMultipleFlag;
	}

}