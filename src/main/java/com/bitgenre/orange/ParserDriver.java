package com.bitgenre.orange;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import com.bitgenre.orange.domain.Decorator;
import com.bitgenre.orange.domain.Entity;
import com.bitgenre.orange.domain.EnumType;
import com.bitgenre.orange.domain.Import;
import com.bitgenre.orange.domain.Namespace;
import com.bitgenre.orange.domain.Relationship;
import com.bitgenre.orange.domain.StatementType;

/**
 * The main interface to the Orange Parser.
 * 
 * @author brianmcginnis
 *
 */
public class ParserDriver {
	
    private boolean isDebugMode;

	private ParsingResults
    parseInputStream(InputStream stream)
            throws IOException, RecognitionException
    {
        ANTLRInputStream is = new ANTLRInputStream(stream);

        // Initialize Antlr-generated Lexer class with System.in
        OrangeLexer lexer = new OrangeLexer(is);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Initialize Antlr-generated Parser with our tokens.
        OrangeParser parser = new OrangeParser(tokens);

        // walk the TREE starting at the 'prog' rule.
        ParsingResults results = walkAST(parser.prog(), parser.getTokenNames(), tokens);

        return results;
       
    }

    /**
     * Walks the AST creating Entity objects as it goes along.
     * 
     * @param pr
     * @param tokenNames
     * @param tokens
     * @return
     */
    private ParsingResults
    walkAST(OrangeParser.prog_return pr, String[] tokenNames, CommonTokenStream tokens)
    {
    
    	if (isDebugMode) {
    		System.out.println("The parsed AST=\n"+((Tree)pr.getTree()).toStringTree());
    	}

        ParsingResults results = new ParsingResults();
        
        CommonTree statNodes = (CommonTree)pr.getTree();

        // Get list of entities to create
        List<CommonTree> topStatements = getTopStatements(statNodes);

        // The most recent namespace declaration encountered during our processing.
        Namespace currentNamespace = null;

        // Handle each top-level statement: ENTITY, DECORATOR, etc.
        for (CommonTree topNode : topStatements) {

            // Parse out the statement type from the token string.
            StatementType stmtType = StatementType.fromString(topNode.getText());
            
            switch (stmtType) {
            case Entity:
                Entity entity = Entity.createEntityFromAST(topNode, tokenNames, currentNamespace);
                results.addEntity(entity);
                break;
            
            case Decorator:
            	Decorator decorator = Decorator.createDecoratorFromAST(topNode);
            	results.addDecorator(decorator);
                break;

            case Namespace:
                currentNamespace = Namespace.createNamespaceFromAST(topNode);
                results.setNamespace(currentNamespace);
                break;

            case Import:
                Import theImport = Import.createImportFromAST(topNode);
                results.addImport(theImport);
                break;
                
            case Enum:
            	EnumType enumType = EnumType.createEnumFromAST(topNode, currentNamespace);
            	results.addEnumType(enumType);
            	break;

            case Relationship:
                Relationship relationship = Relationship.createRelationshipFromAST(topNode);
                results.addRelationship(relationship);
                break;
            
            default:
                throw(new RuntimeException("Unknown top level statement: " + stmtType));

            }
           
        }
        return results;
    }

    

    @SuppressWarnings("unchecked")
	private List<CommonTree>
    getTopStatements(CommonTree root)
    {
      return(root.getChildren());
    }


    /**
     *  Parse an Orange input file.
     * @param filename
     * @return A value ParsingResults object if successful.
     * 
     * @throws IOException
     * @throws RecognitionException
     */
    public ParsingResults
    parseFile(String filename)
            throws IOException, RecognitionException
    {
        return(parseInputStream(new FileInputStream(filename)));
    }
    
    
    public ParserDriver(boolean isDebugMode) {
    	this.isDebugMode = isDebugMode;
    }
}
