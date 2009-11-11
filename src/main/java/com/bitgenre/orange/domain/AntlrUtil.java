package com.bitgenre.orange.domain;

import org.antlr.runtime.tree.CommonTree;

import java.util.List;

/**
 * Generic util class for handling Antlr AST node processing.
 */
public class AntlrUtil {

    
    // A tree utility funciton.
    // Looks in list of child nodes for a node with matching text name.
    public static CommonTree
    findNodeByText(List<CommonTree> childNodes, String nodeText)
    {
        for (CommonTree node : childNodes) {
            if (nodeText.equals(node.getText())) {
                return node;
            }
        }
        return null; // not found
    }
}
