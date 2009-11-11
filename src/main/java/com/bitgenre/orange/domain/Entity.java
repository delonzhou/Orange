package com.bitgenre.orange.domain;

import com.bitgenre.orange.domain.Attribute;

import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.CommonTree;

/**
 *  An Entity declaration parsed from input file.
 */
public class Entity {

    protected String name;
    protected List<Attribute> attributes = new ArrayList<Attribute>();
    protected Namespace namespace;

    public Entity(Namespace namespace, String name) {
        this.name = name;
        this.namespace = namespace;
    }

    public Entity(Namespace namespace, String name, List<Attribute> attributes) {
        this(namespace, name);
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attr) {
        attributes.add(attr);
    }


    /**
     * Parses out an Entity from the top-level AST node.
     *
     * @param entityCreateNode
     * @param tokenNames
     * @param namespace
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Entity
    createEntityFromAST(CommonTree entityCreateNode, String[] tokenNames, Namespace namespace)
    {
        //
        // First get the entity's name. AST = Node:ENTITY_NAME -> Node:"the entity name"
        //
        CommonTree entityNameParent = AntlrUtil.findNodeByText(entityCreateNode.getChildren(), "ENTITY_NAME");

        CommonTree entityNameNode = (CommonTree)entityNameParent.getChildren().get(0);


        String entityName = entityNameNode.getText();

        Entity entity = new Entity(namespace, entityName);


        //
        // Process Entity's attributes
        //
        CommonTree entityAttParent = AntlrUtil.findNodeByText(entityCreateNode.getChildren(), "ATTRIBUTES");
        List<CommonTree> attributeNodes = entityAttParent.getChildren();

        if (attributeNodes != null) {

            for (CommonTree attributeNode : attributeNodes) {
                Attribute attr = AttributeBuilder.parseAttributeFromAST(attributeNode, tokenNames);
                entity.addAttribute(attr);
            }
        }

       return entity;
    }


    public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append("Entity:  namespace=");
        bldr.append(this.namespace);
        bldr.append(", name=");
        bldr.append(this.name);


        for (Attribute attribute : this.attributes) {
            bldr.append("\n\t");
            bldr.append(attribute.toString());
        }

        return bldr.toString();
    }

}
