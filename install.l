[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO] 
[INFO] javaparser-parent
[INFO] javaparser-core
[INFO] javaparser-testing
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building javaparser-parent 2.2.3-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ javaparser-parent ---
[INFO] 
[INFO] --- maven-enforcer-plugin:1.0:enforce (enforce-maven) @ javaparser-parent ---
[INFO] 
[INFO] --- maven-install-plugin:2.4:install (default-install) @ javaparser-parent ---
[INFO] Installing /Users/StarRUC/git/javaparser/pom.xml to /Users/StarRUC/.m2/repository/com/github/javaparser/javaparser-parent/2.2.3-SNAPSHOT/javaparser-parent-2.2.3-SNAPSHOT.pom
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building javaparser-core 2.2.3-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ javaparser-core ---
[INFO] Deleting /Users/StarRUC/git/javaparser/javaparser-core/target
[INFO] 
[INFO] --- maven-enforcer-plugin:1.4:enforce (enforce-maven) @ javaparser-core ---
[INFO] 
[INFO] --- javacc-maven-plugin:2.6:javacc (javacc) @ javaparser-core ---
Java Compiler Compiler Version 5.0 (Parser Generator)
(type "javacc" with no arguments for help)
Reading from file /Users/StarRUC/git/javaparser/javaparser-core/src/main/javacc/java_1_8.jj . . .
File "TokenMgrError.java" does not exist.  Will create one.
File "ParseException.java" does not exist.  Will create one.
File "Token.java" does not exist.  Will create one.
File "JavaCharStream.java" does not exist.  Will create one.
Parser generated successfully.
[INFO] Processed 1 grammar
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ javaparser-core ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/StarRUC/git/javaparser/javaparser-core/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.0:compile (default-compile) @ javaparser-core ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 132 source files to /Users/StarRUC/git/javaparser/javaparser-core/target/classes
[INFO] -------------------------------------------------------------
[WARNING] COMPILATION WARNING : 
[INFO] -------------------------------------------------------------
[WARNING] /Users/StarRUC/git/javaparser/javaparser-core/src/main/java/com/github/javaparser/TestMethodChangeWithVisitor.java: Some input files use unchecked or unsafe operations.
[WARNING] /Users/StarRUC/git/javaparser/javaparser-core/src/main/java/com/github/javaparser/TestMethodChangeWithVisitor.java: Recompile with -Xlint:unchecked for details.
[INFO] 2 warnings 
[INFO] -------------------------------------------------------------
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /Users/StarRUC/git/javaparser/javaparser-core/src/main/java/com/github/javaparser/UMLParser.java:[202,73] cannot find symbol
  symbol:   method toLowerCaes()
  location: class java.lang.String
[ERROR] /Users/StarRUC/git/javaparser/javaparser-core/src/main/java/com/github/javaparser/UMLParser.java:[205,48] variable fieldStr is already defined in method generateYumlString(java.util.List<com.github.javaparser.ast.body.ClassOrInterfaceDeclaration>)
[INFO] 2 errors 
[INFO] -------------------------------------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] javaparser-parent .................................. SUCCESS [  0.754 s]
[INFO] javaparser-core .................................... FAILURE [  6.102 s]
[INFO] javaparser-testing ................................. SKIPPED
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 7.051 s
[INFO] Finished at: 2015-10-23T16:53:07-07:00
[INFO] Final Memory: 21M/247M
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.0:compile (default-compile) on project javaparser-core: Compilation failure: Compilation failure:
[ERROR] /Users/StarRUC/git/javaparser/javaparser-core/src/main/java/com/github/javaparser/UMLParser.java:[202,73] cannot find symbol
[ERROR] symbol:   method toLowerCaes()
[ERROR] location: class java.lang.String
[ERROR] /Users/StarRUC/git/javaparser/javaparser-core/src/main/java/com/github/javaparser/UMLParser.java:[205,48] variable fieldStr is already defined in method generateYumlString(java.util.List<com.github.javaparser.ast.body.ClassOrInterfaceDeclaration>)
[ERROR] -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
[ERROR] 
[ERROR] After correcting the problems, you can resume the build with the command
[ERROR]   mvn <goals> -rf :javaparser-core
