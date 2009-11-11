package com.bitgenre.orange;
import com.bitgenre.orange.domain.Entity;
import com.bitgenre.orange.domain.EnumType;
import com.bitgenre.orange.domain.Namespace;
import com.bitgenre.orange.domain.Relationship;
import com.bitgenre.orange.domain.Decorator;
import com.bitgenre.orange.domain.Import;

import java.util.List;
import java.util.ArrayList;

/**
 * Data pulled out of parsing activity
 */
public class ParsingResults {
    protected Namespace namespace;
    protected List<Entity> entities = new ArrayList<Entity>();
    protected List<Relationship> relationships = new ArrayList<Relationship>();
    protected List<Decorator> decorators = new ArrayList<Decorator>();
    protected List<Import> imports = new ArrayList<Import>();
    protected List<EnumType> enumTypes = new ArrayList<EnumType>();

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    
    public List<EnumType> getEnumTypes() {
		return enumTypes;
	}
    
    public void
    addEnumType(EnumType enumType)
    {
    	enumTypes.add(enumType);
    }

	public void
    addEntity(Entity entity)
    {
        entities.add(entity);
    }

    public void
    addRelationship(Relationship relationship)
    {
        relationships.add(relationship);
    }

    public void
    addImport(Import theImport)
    {
        imports.add(theImport);
    }
    
    public void
    addDecorator(Decorator decorator)
    {
    	decorators.add(decorator);
    }
    
    public List<Relationship> getRelationships() {
		return relationships;
	}

	public List<Decorator> getDecorators() {
		return decorators;
	}

	public List<Import> getImports() {
		return imports;
	}

	public String
    toString()
    {
        StringBuilder bldr = new StringBuilder();

        bldr.append("Parsing Results: ");
        bldr.append(entities.size() + " Entities, ");
        bldr.append(relationships.size() + " Relationships, ");
        bldr.append(enumTypes.size() + " EnumTypes, ");
        bldr.append(decorators.size() + " Decorators.\n\n");
       
        bldr.append("\n=== Imports ===");
        for (Import theImport : imports) {
            bldr.append("\n");
            bldr.append(theImport.toString());
        }

        bldr.append("\n\n=== Entities ===");
        for (Entity entity : entities) {
            bldr.append("\n");
            bldr.append(entity.toString());
            bldr.append("\n");
        }

        bldr.append("\n=== Relationships ===");
        for (Relationship rel : relationships) {
            bldr.append("\n");
            bldr.append(rel.toString());
            bldr.append("\n");
        }
        
        bldr.append("\n=== Enums ===");
        for (EnumType e : enumTypes) {
            bldr.append("\n");
            bldr.append(e.toString());
            bldr.append("\n");
        }
        
        bldr.append("\n=== Decorators ===");
        for (Decorator d : decorators) {
            bldr.append("\n");
            bldr.append(d.toString());
            bldr.append("\n");
        }
        

        return bldr.toString();
    }
}
