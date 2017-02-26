package com.github.javaparser;

import java.io.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.BlockStmt;

//import java.util.List;
import java.util.*;


public class UMLParser {

    public static void main(String[] args) throws Exception {
    	
    	/* 
    	 * Specify the command usage of this Java Parser Tool.
    	 * #1: two arguments; first one is the java source code folder, must end with '/'; second one is the absolute path of the output UML graph
    	 * Example: UMLParser /javaFolder/ /outputFolder/outFile.jpg
    	 * */
    	if (args.length !=2 || !args[0].endsWith("/")) {
    		System.out.println("Invalid Usage !!!");
    		System.out.println("** Command format: UMLParser sourceFileFolder outputDiagramName");
    		System.out.println("** Require TWO arguments: sourceFileFolder must end with /");
    		System.out.println("** Example: UMLParser /javaFolder/ /outputFolder/outFile.jpg");
    		return;
    	}
    	    	
    	List<ClassOrInterfaceDeclaration> classList = new ArrayList<ClassOrInterfaceDeclaration>();
    	try {
        	File classFolder = new File(args[0]);
        	if (!classFolder.exists()) {
        		System.out.println("Invalid Usage !!! Java Source Code Folder doesn't exist!");
        		return;
        	}
        	File[] fileList = classFolder.listFiles();
        	    	
        	for (File file : fileList) {
        	    if (file.isFile() && file.getName().contains(".java")) {
        	    	// System.out.println("File Name: " + file.getName());

        	    	// Create an input stream for the file to be parsed
        	        FileInputStream in = new FileInputStream(file);

        	        try {
        	            // parse the file, add all the ClassOrInterface objects into the classList
        	        	CompilationUnit cu = JavaParser.parse(in);
        	            List<TypeDeclaration> types = cu.getTypes();        
        	            for (TypeDeclaration type : types) {
        	            	if (type instanceof ClassOrInterfaceDeclaration) {
        	             		ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) type;
        	             		classList.add(ci);
        	             	}
        	            }
        	        } finally {
        	            in.close();
        	        }
        	        
        	    }
        	}
    		
    	} catch (Exception e) {
			e.printStackTrace();
    	}
    	
    	String yumlStr = generateYumlString(classList);
    	writeStringToFile(yumlStr, args[0]);
    	//String cmd = "echo \"" + yumlStr + "\" | /Users/StarRUC/yuml/yuml -o " + args[1];
    	//String cmd = "/Users/StarRUC/yuml/yuml -i " + args[0] + "yuml.txt -o " + args[1];
    	String cmd = "yuml -s nofunky -i " + args[0] + "yuml.txt -o " + args[1];
    	//System.out.println("Test CMD: " + cmd);
    	Runtime.getRuntime().exec(cmd);
    	
    }



    /*
     * Generate the yUML String for all ClassDeclaration objects
     * */
    private static String generateYumlString(List<ClassOrInterfaceDeclaration> classList) {
    	
    	int length = classList.size();
    	int[][] assocRelation = new int[length][length];
    	for (int i=0; i<length; i++) {
    		for (int j=0; j<length; j++) {
    			assocRelation[i][j] = 0;    			
    		}
    	}

    	// Use SET to store the interface/use info to prevent duplicate items
    	Set<String> useInterfaceStrings = new HashSet<String>();
     	String resultString = "";    
    	String classString = "";   
    	String extendString = "";
    	String implementString = "";
   
     	for (int i=0; i<length; i++) {
     		ClassOrInterfaceDeclaration ci = classList.get(i);
            boolean isInterface = ci.isInterface();
            
         	//Add Implements info
         	List<ClassOrInterfaceType> implementList = ci.getImplements();
         	if (implementList.size()>0) {
         		for (ClassOrInterfaceType impl : implementList) {
         			String type = impl.getName();
         			// YUML Example of Implements: [<<interface>>;A2]^-.-[B2]
         			//String tmp = "[<<interface>>;" + type + "]^-.-[" + ci.getName() + "]\n";
         			String tmp = "[＜＜interface＞＞;" + type + "]^-.-[" + ci.getName() + "]\n";
         			implementString = implementString + tmp;
         		}
         	}

         	//Add Extends info
         	List<ClassOrInterfaceType> extendList = ci.getExtends();
         	if (extendList.size()>0) {
         		for (ClassOrInterfaceType extend : extendList) {
         			String type = extend.getName();
         			boolean isParentInterface = false;
            		if (searchInterface(classList, type) != -1) {
         				isParentInterface = true;
         			}
            		// YUML Example of Extends: [<<interface>>;P]^-[<<interface>>;B2],
            		String tmp = "[" + (isParentInterface?"＜＜interface＞＞;":"") + type 
        					+ "]^-[" + (isInterface?"＜＜interface＞＞;":"") + ci.getName() + "]\n";
            		//String tmp = "[" + (isParentInterface?"<<interface>>;":"") + type 
            		//			+ "]^-[" + (isInterface?"<<interface>>;":"") + ci.getName() + "]\n";
            		extendString = extendString + tmp;
         		}
         	}
         	

         	List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
         	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
         	List<ConstructorDeclaration> constructors = new ArrayList<ConstructorDeclaration>();

         	List<BodyDeclaration> members = ci.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof FieldDeclaration) {
            		FieldDeclaration field = (FieldDeclaration) member;
                	fields.add(field);
                }
                else if (member instanceof MethodDeclaration) {
                	MethodDeclaration method = (MethodDeclaration) member;
                	methods.add(method);
                }
                else if (member instanceof ConstructorDeclaration) {
                	ConstructorDeclaration constructor = (ConstructorDeclaration) member;
                	constructors.add(constructor);
                }

            }

         	String fieldStr = "";
         	String methodStr = "";
         	
            for (FieldDeclaration field : fields) {
         		// Check if the field type is multiple
         		boolean isMultiple = false;
         		String typeStr = field.getType().toString();
         		if (typeStr.contains("[]")) {
         			isMultiple = true;
         			int index = typeStr.indexOf("[]");
         			typeStr = typeStr.substring(0, index);
            		//System.out.println("[] Case: " + typeStr);
         		}
         		else {
         			int index1 = typeStr.indexOf('<');
         			int index2 = typeStr.indexOf('>');
         			if (index1>=1 && (index2-index1)>=2 ) {
             			isMultiple = true;
             			typeStr = typeStr.substring(index1+1, index2);
	            		//System.out.println("<> Case: " + typeStr);
         			}
         		}
         		
         		// Check if the field type is a declared class/interface
         		int index = searchClass(classList, typeStr);
         		if (index != -1) {
         			// Association Case: Found the class definition for field type
         			
         			// Don't keep the Association if both the two parties are interfaces
         			boolean isFieldInterface = classList.get(index).isInterface();
         			if (isInterface && isFieldInterface) {
         				continue;
         			}
         			
         			if (isMultiple)
         				assocRelation[i][index] = 2;
         			else
         				assocRelation[i][index] = 1;
            		//System.out.println("Found Specified Class: " + typeStr);
         		}
         		else {
         			// Attribute Compartment Case: Then generate the YUML string for this field
            		String modifierStr = getModifierString(field.getModifiers());
            		//System.out.println("Field Modifier: " + modifierStr);

            		List<VariableDeclarator> vars = field.getVariables();
             		for (VariableDeclarator var : vars) {
             			String varName = var.getId().getName();
                		
             			MethodDeclaration getMethod = searchMethod(methods, "get"+varName.toLowerCase());
                		MethodDeclaration setMethod = searchMethod(methods, "set"+varName.toLowerCase());
                		if (getMethod!=null && setMethod!=null) {
                			// Getter/Setter Case: set the field modifier as public
                			methods.remove(getMethod);
                			methods.remove(setMethod);
                 			String varStr = "+" + varName + ":" + typeStr + (isMultiple?"(*)":"") + ";";
                        	fieldStr = fieldStr + varStr;
                 			//fieldStrList.add(varStr);
                		}
                		else if (modifierStr.equals("+") || modifierStr.equals("-")) {
                    		// Normal public/private case: YUML Example of Field: -forname:string;
                 			String varStr = modifierStr + varName + ":" + typeStr + (isMultiple?"(*)":"") + ";";
                        	fieldStr = fieldStr + varStr;
                			//fieldStrList.add(varStr);
                		}
             		}// end of FOR variables
         		}// end of Attribute Compartment Case
            }// end of FOR Field Iteration

            
            for (MethodDeclaration method : methods) {
            	int modifier = method.getModifiers();
            	String modifierStr = getModifierString(modifier);
        		//System.out.println("Method: " + method.getName() + "\nModifier: " + modifierStr);

        		// If the method is not public, ignore it.
        		if (!modifierStr.equals("+"))
        			continue;

        		// YUML Example of Method: +doShiz(a:int;b:int;c:String):int;
            	String typeStr = "";
            	String paraStr = "";
        		//methodStr = modifierStr + method.getName() + "(";
            	
            	boolean useFlag = false;
            	
            	List<Parameter> paraList = method.getParameters();
            	for (Parameter para : paraList) {
            		typeStr = para.getType().toString();
            		
	         		// Check if the argu type is multiple
	         		boolean isMultiple = false;
	         		if (typeStr.contains("[]")) {
	         			isMultiple = true;
	         			int index = typeStr.indexOf("[]");
	         			typeStr = typeStr.substring(0, index);
	         		}
	         		else {
	         			int index1 = typeStr.indexOf('<');
	         			int index2 = typeStr.indexOf('>');
	         			if (index1>=1 && (index2-index1)>=2 ) {
	             			isMultiple = true;
	             			typeStr = typeStr.substring(index1+1, index2);
	         			}
	         		}

            		paraStr = paraStr + para.getId().getName() + ":" + typeStr + (isMultiple?"(*)":"") + ";";
            		//System.out.println("Method Parameter: " + paraStr);
            		
            		/*
            		 * Define the requirements for interface/use case:
            		 * #1: the class is not interface
            		 * #2: the argument in the method is of Interface type
            		 */
            		if ( !isInterface && (searchInterface(classList, typeStr) != -1) ) {                			
            			// YUML Example of Use Interface: [C2]uses -.->[<<interface>>;A2]
            			//String useStr = "[" + ci.getName() + "]uses -.->[<<interface>>;" + typeStr + "]\n";
            			String useStr = "[" + ci.getName() + "]uses -.->[＜＜interface＞＞;" + typeStr + "]\n";
        				useInterfaceStrings.add(useStr);
                		//System.out.println("Use Interface: " + useStr);
        				useFlag = true;
                		//System.out.println("Use Flag: " + useFlag);
            		}
            	}
            	
            	// Check the local variables in the method body
            	BlockStmt blck = method.getBody();
            	if (blck!=null) {
	            	for (Statement stmt : blck.getStmts()) {
	            		//System.out.println("Statement: " + stmt.toString());
	            		//System.out.println("Statement:");
	            		String[] stmtLines = stmt.toString().split("\n");
	            		for (int j=0; j<stmtLines.length; j++) {
	            			//System.out.println("\tLine: " + stmtLines[j]);
	            			//System.out.println("\tLine: ");
	            			String[] words = stmtLines[j].split("[\\.\\s\\(\\)\\{\\}\\\"\\+\\;\\:=]+");
	            			String type = "";
	            			if (words.length>0) {
		            			if ( (words[0].equals("for") || words[0].equals("if") || words[0].equals("else")) && words.length>1)
		            				type = words[1];
		            			else
		            				type = words[0];
	            			}
            				int tmp = searchInterface(classList, type);
            				if (tmp!=-1) {
                    			String useStr = "[" + ci.getName() + "]uses -.->[＜＜interface＞＞;" + type + "]\n";
                				useInterfaceStrings.add(useStr);
                        		//System.out.println("Use Interface: " + useStr);
                				useFlag = true;
            				}

	            		}
	            	}
            	}
            	
            	// If useFlag is false, not Use Interface Case, print this method; otherwise omit this print
            	//if (useFlag==false) {
            		//System.out.println("Print this method: " + method.getName());
                	paraStr = trimSemicolon(paraStr);
                	methodStr = methodStr + modifierStr + method.getName() + "(" + paraStr +  "):" + method.getType() + ";";
            	//}
            }

            for (ConstructorDeclaration constructor : constructors) {
            	int modifier = constructor.getModifiers();
            	String modifierStr = getModifierString(modifier);
        		//System.out.println("Method Modifier: " + modifierStr);
        		// If the method is not public, ignore it.
        		if (!modifierStr.equals("+"))
        			continue;

        		// YUML Example of Method: +doShiz(a:int;b:int;c:String):int;
            	String typeStr = "";
            	String paraStr = "";
        		//methodStr = modifierStr + method.getName() + "(";
            	
            	boolean useFlag = false;
            	List<Parameter> paraList = constructor.getParameters();
            	for (Parameter para : paraList) {
            		typeStr = para.getType().toString();
	         		
            		// Check if the argu type is multiple
	         		boolean isMultiple = false;
	         		if (typeStr.contains("[]")) {
	         			isMultiple = true;
	         			int index = typeStr.indexOf("[]");
	         			typeStr = typeStr.substring(0, index);
	         		}
	         		else {
	         			int index1 = typeStr.indexOf('<');
	         			int index2 = typeStr.indexOf('>');
	         			if (index1>=1 && (index2-index1)>=2 ) {
	             			isMultiple = true;
	             			typeStr = typeStr.substring(index1+1, index2);
	         			}
	         		}
           		
            		paraStr = paraStr + para.getId().getName() + ":" + typeStr + (isMultiple?"(*)":"") + ";";
            		//System.out.println("Method Parameter: " + paraStr);
            		
            		/*
            		 * Define the requirements for interface/use case:
            		 * #1: the class is not interface
            		 * #2: the argument in the method is of Interface type
            		 */
            		if ( !isInterface && (searchInterface(classList, typeStr) != -1) ) {                			
            			// YUML Example of Use Interface: [C2]uses -.->[<<interface>>;A2]
            			//String useStr = "[" + ci.getName() + "]uses -.->[<<interface>>;" + typeStr + "]\n";
            			String useStr = "[" + ci.getName() + "]uses -.->[＜＜interface＞＞;" + typeStr + "]\n";
        				useInterfaceStrings.add(useStr);
                		//System.out.println("Use Interface: " + useStr);
        				useFlag = true;
                		//System.out.println("Use Flag: " + useFlag);
            		}
            	}
            	// If useFlag is false, not Use Interface Case, print this method; otherwise omit this print
            	//if (useFlag==false) {
            		//System.out.println("Print this method: " + constructor.getName());
                	paraStr = trimSemicolon(paraStr);
                	methodStr = methodStr + modifierStr + constructor.getName() + "(" + paraStr +  ");";
            	//}
            }// End of for 

            fieldStr = trimSemicolon(fieldStr);
            methodStr = trimSemicolon(methodStr);
            if (fieldStr.equals("") && methodStr.equals("")) 
            	classString = classString + "[" + (isInterface?"＜＜interface＞＞;":"") + ci.getName() + "]\n";
            else 
            	classString = classString + "[" + (isInterface?"＜＜interface＞＞;":"") + ci.getName() + "|" + fieldStr + "|" + methodStr + "]\n";
        }// End of iteration of all ClassOrInterfaceDeclaration
     	
     	
     	// Add Association Info
     	for (int i=0; i<length; i++) {
     		for (int j=i+1; j<length; j++) {
     			if ( (assocRelation[i][j]+assocRelation[j][i]) > 0 ) {
     				String rightRel = mapRelationToString(assocRelation[i][j]);
     				String leftRel = mapRelationToString(assocRelation[j][i]);
     				String leftClass = classList.get(i).getName();
     				String rightClass = classList.get(j).getName();
     				boolean leftFlag = classList.get(i).isInterface();
     				boolean rightFlag = classList.get(j).isInterface();
     				
     				// YUML Example of Association Relation : [A]-*[D]
     				String tmp = "[" + (leftFlag?"＜＜interface＞＞;":"") + leftClass + "]"
     							+ leftRel + "-" + rightRel
     							+ "[" + (rightFlag?"＜＜interface＞＞;":"") + rightClass + "]\n";
     				
     				classString = classString + tmp;
     			}
     		}
     	}
     	
     	// FINAL Concatenate
     	resultString = classString + extendString + implementString;
     	for (String useStr : useInterfaceStrings) {
     		resultString = resultString + useStr;
     	}
        //System.out.println(resultString);
   	
        return resultString;
    }

 
    // searchMethod: get the getter/setter method
    private static MethodDeclaration searchMethod(List<MethodDeclaration> methods, String name) {
    	for (MethodDeclaration method : methods) {
    		String methodName = method.getName().toLowerCase();
    		if (methodName.equals(name))
    			return method;
    	}
    	return null;
    }
    
    
    private static String trimSemicolon(String str) {
    	String result = str;
    	if (str.endsWith(";")) {
    		int length = str.length();
    		result = str.substring(0, length-1);
    	}
    	return result;
    }
    
    
    private static String mapRelationToString(int rel) {
    	String str = "";
    	
		switch (rel) {
		case 1:
			str = "1";
			break;
		case 2:
			str = "*";
			break;
		default:
			str = "";
		}

		return str;
    }
    
    /*
     * Generate access modifier string according to modifier
     */
    private static String getModifierString(int modifier) {
    	String modifierStr = "";

    	//Define Bitmask for Access Modifiers, we only need to check the last 3 bits of Modifier
        int bitmask = 0x0007;
        int intModifier = modifier & bitmask;
		switch (intModifier) {
		case 0:
			// default
			modifierStr = "0";
			break;
		case 1:
			// public
			modifierStr = "+";
			break;
		case 2:
			// private
			modifierStr = "-";
			break;
		default:
			modifierStr = "";
			break;
		}
		
		return modifierStr;
    }
    
    
    /* Look for Class with specified name. 
     * If found, return index; otherwise, return -1.
     * */
    private static int searchClass(List<ClassOrInterfaceDeclaration> classList, String type) {
    	for (int i=0; i<classList.size(); i++) {
    		String className = classList.get(i).getName();
    		if (type.equals(className))
    			return i;
    	}
    	
    	return -1;
    }

    /* Look for Interface with specified name. 
     * If found, return index; otherwise, return -1.
     * */
    private static int searchInterface(List<ClassOrInterfaceDeclaration> classList, String type) {
    	for (int i=0; i<classList.size(); i++) {
    		String className = classList.get(i).getName();
    		if (type.equals(className) && classList.get(i).isInterface())
    			return i;
    	}
    	
    	return -1;
    }

    /*
     * Write generated UML string into local file for YUML tool input
     */
    private static void writeStringToFile(String str, String folder) {
    	String path = folder + "yuml.txt";
    	BufferedWriter writer = null;
    	try
    	{
    	    writer = new BufferedWriter( new FileWriter(path));
    	    writer.write(str);
    	}
    	catch ( IOException e)
    	{
    	}
    	finally
    	{
    	    try
    	    {
    	        if ( writer != null)
    	        writer.close( );
    	    }
    	    catch ( IOException e)
    	    {
    	    }
    	}
    }

    

}
