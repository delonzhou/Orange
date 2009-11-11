

import com.bitgenre.orange.ParserDriver;
import com.bitgenre.orange.ParsingResults;

public class OrangeCompiler {

   static final String VERSION = "0.2";
   
   static boolean isDebugMode = false;
   static boolean isCreateMode = false;
   static boolean isReplaceMode = false;   

    protected static void
    printUsage()
    {
    	System.out.println("Usage: OrangeCompiler [(-c |-r) -v]" + " filename ");
		System.out.println("Create mode is selected by default.");
		System.out.println("-c : Create mode.  Creates newly parsed metadata only if it doesn't already exist.");
		System.out.println("-r : Replace mode. Replaces existing metadata with newly parsed metadata.");
		System.out.println("-v : Debug / Verbose. Prints extra information.");  	
    }
    
    private static boolean 
    checkModeFromArgv(String mode, String[] args)
    {
    	for (String arg : args) {
    		if (mode.equals(arg))
    			return true;
    	}   	
    	return false;
    }
    
    protected static String
    getFileName(String[] args)
    {
    	// last argument is filename
    	return args[(args.length-1)];
    }
    
    public static void main(String[] args)
    {
    	if (args.length < 1) {
    		printUsage();
    		System.exit(-1);
    	}
    	
    	// Argv flags
    	final String[] modes = {"-d", "-c", "-r"};
    	
    	isDebugMode = checkModeFromArgv(modes[0], args);
    	isCreateMode = checkModeFromArgv(modes[1], args);
    	isReplaceMode = checkModeFromArgv(modes[2], args);
    	
    	String filename = getFileName(args);
    	
    	if (isCreateMode && isReplaceMode) {
    		System.err.println("ERROR! You must select either create or replace mode, not both.");
    		printUsage();
    		System.exit(-1);
    	} else if (!(isCreateMode|| isReplaceMode)) {
    		// default to Create mode if -c or -r is not given.
    		isCreateMode = true;
    	}
    	
    	if (isDebugMode) {
    		System.out.println("Orange compiler version " + VERSION + " parsing file: '" + filename + "' ...\n");
    	}

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
    }
}
