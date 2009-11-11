package com.bitgenre.orange.domain;

import org.antlr.runtime.tree.CommonTree;

/**
 *  Represents a Relationship between 2 entities.
 */
public class Relationship {
    protected final String name;
    protected final RelationshipSide left;
    protected final RelationshipSide right;

    public Relationship(String name, RelationshipSide left, RelationshipSide right)
    {
        this.name = name;
        this.left = left;
        this.right = right;
    }


    public static Relationship
    createRelationshipFromAST(CommonTree relationshipTopNode)
    {
        // First, get relationship name from 1st child node.
        CommonTree relationshipNode = (CommonTree)relationshipTopNode.getChildren().get(0);
        String relationshipName = relationshipNode.getText();

        //
        // Parse left and right relationships.
        // Child 0 == Left side
        // Child 1 == Right side
        CommonTree leftNode = (CommonTree)relationshipNode.getChildren().get(0);
        CommonTree rightNode = (CommonTree)relationshipNode.getChildren().get(1);

        RelationshipSide leftSide = RelationshipSide.createFromAST(leftNode);
        RelationshipSide rightSide = RelationshipSide.createFromAST(rightNode);


        Relationship rel = new Relationship(relationshipName, leftSide, rightSide);
        
        return rel;
    }

    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append("Relationship name=");
        bldr.append(name);
        bldr.append(", ");
        bldr.append(left);
        bldr.append(", ");
        bldr.append(right);

        return bldr.toString();

    }


	public String getName() {
		return name;
	}


	public RelationshipSide getLeft() {
		return left;
	}


	public RelationshipSide getRight() {
		return right;
	}
    
    
}
