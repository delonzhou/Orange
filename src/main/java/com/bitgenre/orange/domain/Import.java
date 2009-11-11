package com.bitgenre.orange.domain;

import org.antlr.runtime.tree.CommonTree;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents an Import declaration.
 */
public class Import {

    /** The namespace separator character. */
    public static final String SEPARATOR_SYMBOL = ".";

    protected List<String> nameParts = new ArrayList<String>();

    public void addPart(String part) { nameParts.add(part); }

    public String toString() { return(compose()); }
    
    public String 
    toUri() 
    {
    	String qualifiedName = toString();
    	String uri = "//" + qualifiedName.replace('.', '/');
    	return(uri);
    }

    /**
     * Assembles the parts of the namespace into a single String.
     *
     * @return
     */
    protected String
    compose()
    {
        StringBuilder bldr = new StringBuilder();

        int count = 0;
        for (String part : nameParts) {
            if (count > 0) {
                bldr.append(SEPARATOR_SYMBOL);
            }

            bldr.append(part);

            count++;
        }

        return bldr.toString();
    }

    public Import() { }

    /**
     * Creates a Namespace instance from a starting AST node.
     *
     * @param namespaceNode
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Import
    createImportFromAST(CommonTree namespaceNode)
    {
        Import theImport = new Import();

        // Child Node 0 = Token "QUALIFIED_NAME" required
        CommonTree qualifiedName = (CommonTree)namespaceNode.getChildren().get(0);


        // Child Nodes == Each part of import without the separator char. Example: com.fubar is 'com' 'fubar'
        List<CommonTree> namespaceParts = qualifiedName.getChildren();
        if (namespaceParts != null) {
            for (CommonTree part : namespaceParts) {
                    theImport.addPart(part.getText());
            }
        }

        return theImport;
    }
}
