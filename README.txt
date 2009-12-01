
To Build
-----------------

1. Follow instructions for Maven2's Antlr V3 plugin per this URL:
    http://antlr.org/antlr3-maven-plugin/index.html

2. To run Orange compiler upon sample data:
   shell> mvn clean
   shell> mvn package
   shell> java -cp target/orange-1.0.2-SNAPSHOT.jar:lib/antlr-3.1.1.jar OrangeCompiler -d -c ./test-data/SampleData2.txt

3. To get IDE working:
   shell> mvn eclipse:eclipse

4. To install into local maven repository:
  shell> mvn install:install-file -Dfile=/Users/brianmcginnis/personal/OrangeProject/Orange/target/orange-1.0.2-SNAPSHOT.jar -DgroupId=com.bitgenre -DartifactId=orange -Dversion=1.0.2-SNAPSHOT -Dpackaging=jar


** The Mac eclise "Bad Class Version" problem:
 This is caused by compiler settings in build. Change compiler settings to project-specific, select
 1.5 settings if Mac.
  
  
To use it
--------------

1. Inlcude orange's jar file in your project into your local repository per mvn install:install instructions
   above.
  
   Include this into your maven pom.xml:

   <dependency>
     <groupId>com.bitgenre</groupId>
     <artifactId>orange</artifactId>
     <version>1.0-SNAPSHOT</version>
   </dependency>
 
2. May need to Include the following added dependency into your pom:

    <dependency>
      <groupId>org.antlr</groupId>
      <artifactId>antlr</artifactId>
      <version>3.1.1</version>
    </dependency>

4. Invoke the parser and handle the results. Check file OrangeCompiler.java for an example.

	//
	// Example using Orange parser to process an input file 'filename'.
	//

	import com.bitgenre.orange.ParserDriver;
	import com.bitgenre.orange.ParsingResults;
	
	... your code....

       try {
		// This is how to invoke the parser with 'debug' mode and an input file.
        	ParserDriver parser = new ParserDriver(isDebugMode);
        	
        	// Parsing results contains all the info Orange parsed from the file. Use it according
        	// to your system's requirements.
            	ParsingResults results = parser.parseFile(filename);

            	if (isDebugMode) {
            		System.out.println("\n===============\n" + results);        	
            	}
            
            	System.out.println("Compilation completed of file: '" + filename + "'.");
            	System.exit(0);
            
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(-1);
        }
   

==============
Syntax Notes
==============
Properties for attributes and relationships are always
"false" by default. (e.g. if 'required' not specified, it is presumed false).

Relationship defaults:
----------------------
  Cascading=CascadeNone
  Fetch=Lazy
  // reln properties
  Required=False 
  Unique=false
  PrimaryKey=false
  
  
