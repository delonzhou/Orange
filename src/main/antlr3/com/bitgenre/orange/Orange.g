grammar Orange;




options {
  language=Java;
  output=AST;
  ASTLabelType=CommonTree;
}

// IMaginary tokens for AST nodes
tokens {
  START;
  ENTITY_NAME;
  LENGTH;
  PRECISION_SCALE; // DECIMAL_SPEC (precision, scale)
  ENTITY;
  IMPORT;
  ATTRIBUTES;
  PROPERTIES;
  CASCADE;
  FETCH;
  INT_PROPERTIES;
  RELATIONSHIP;
  CONSTRAINTS;
  NAMESPACE;
  QUALIFIED_NAME;
  RELATION_OWNER;
  PRIMARY_KEY;  // Primary key constraint (entity level)
  UNIQUE;	// Unique constraint (entity level)
  CHECK;	// Check constraint (entity level)
  ENUM;		// Enum 
  ENUM_VALUES;
  DECORATOR;	// Companion object
  ENTITY_PROPERTIES;	// com.bitgenre.orange.domain.Entity-level property
  RELATIONSHIP_PROPERTIES;
  FIELD_PROPERTIES;	// com.bitgenre.orange.domain.Attribute-level property
 
  
}

@parser::header { package com.bitgenre.orange; }
@lexer::header { package com.bitgenre.orange; }

prog 	:	namespaceDecl (importDecl)* stat+ -> ^(START namespaceDecl importDecl* stat+)
	; 

stat	: createEntity 						-> ^(ENTITY createEntity)
	| relationship							-> ^(RELATIONSHIP relationship)
	| decorator								-> ^(DECORATOR decorator)
	| enumerator							-> ^(ENUM enumerator)
	; 
	
namespaceDecl 
	: 'namespace' qualifiedName SEMICOLON			-> ^(NAMESPACE qualifiedName)
	;

importDecl
	: 'import' qualifiedName SEMICOLON			-> ^(IMPORT qualifiedName)
	;
		
//------------- Begin Companion Object (user-defined key/value metadata) ------------------
decorator 
	: 'decorator' entityOrRelationshipName=ID 'with' decoratorType=ID '{' typePropDecl? fieldPropDecl* '}' 
		-> ^($entityOrRelationshipName $decoratorType  typePropDecl? fieldPropDecl*)	
	;

// Top-level decoration applied to entire entity or relationship. Example: entity => {table="foo"]
typePropDecl 
	: 'entity' '=>' '['  keyValuePair (',' keyValuePair)* ']' SEMICOLON	-> ^(ENTITY_PROPERTIES keyValuePair+)
	| 'relationship' '=>' '['  keyValuePair (',' keyValuePair)* ']' SEMICOLON -> ^(RELATIONSHIP_PROPERTIES keyValuePair+)
	;
	
// For giving decorator values to Attribute or relationship side.
fieldPropDecl
	: attrName=ID '=>' '[' keyValuePair (',' keyValuePair)* ']' SEMICOLON	-> ^(FIELD_PROPERTIES $attrName keyValuePair+)
	;
	
keyValuePair 
	: ID '=' STRINGLITERAL							-> ^(ID STRINGLITERAL)
	;

//------------ ENUM data type declaration -----------------
enumerator 
	: 'enum' enumName=ID '{' enumBody '}' 				-> ^($enumName enumBody)
	;
			
enumBody 
	:	ID (',' ID)* 						-> ^( ID )+
	;
					
//------------- End com.bitgenre.orange.domain.Entity Properties ------------------
	
relationship 
	:  'relationship' ID '{' entityRefDecl SEMICOLON entityRefDecl SEMICOLON '}'	
		-> ^(ID entityRefDecl entityRefDecl)	
	;
	
		
createEntity 
	:  'entity' ename=ID '{' (attrDecl)* (consDecl)* '}' 	
		-> ^(ENTITY_NAME $ename) ^(ATTRIBUTES attrDecl*) ^(CONSTRAINTS consDecl*)
	;

	
attrDecl 
	: simpleAttrDecl SEMICOLON!
	| intDecl SEMICOLON!	
	| strDecl SEMICOLON!	 
	| decimalDecl SEMICOLON!  
	| byteDecl SEMICOLON!	
	| entityRefDecl SEMICOLON! 
	| enumRefDecl SEMICOLON!
	;
	
simpleAttrDecl 
	: TYPE_BOOLEAN ID attrOptions 				-> ^(TYPE_BOOLEAN ID attrOptions)
	| TYPE_ENTITY_ID ID attrOptions				-> ^(TYPE_ENTITY_ID ID attrOptions)
	| TYPE_GUID ID attrOptions				-> ^(TYPE_GUID ID attrOptions)
	| TYPE_AUTOINC ID attrOptions				-> ^(TYPE_AUTOINC ID  attrOptions)
	| TYPE_AUTOINC_LONG ID attrOptions			-> ^(TYPE_AUTOINC_LONG ID attrOptions)
	| TYPE_DATETIME ID attrOptions				-> ^(TYPE_DATETIME ID attrOptions)
	| TYPE_DATE ID attrOptions				-> ^(TYPE_DATE ID attrOptions)
	| TYPE_TIMESTAMP ID attrOptions				-> ^(TYPE_TIMESTAMP ID attrOptions)
	| TYPE_PICKLIST_OPTION ID attrOptions			-> ^(TYPE_PICKLIST_OPTION ID attrOptions)
	| TYPE_I18N_TEXT ID attrOptions				-> ^(TYPE_I18N_TEXT ID attrOptions)
	| TYPE_I18N_MONEY ID attrOptions			-> ^(TYPE_I18N_MONEY ID attrOptions)
	| TYPE_I18N_URL	ID attrOptions				-> ^(TYPE_I18N_URL ID attrOptions)
	| TYPE_FLOAT ID attrOptions				-> ^(TYPE_FLOAT ID attrOptions)
	| TYPE_DOUBLE ID attrOptions				-> ^(TYPE_DOUBLE ID attrOptions)
	;	

entityRefDecl
	: (TYPE_ENTITY_REF '<' refTo=ID '>' attrName=ID attrOptions fetchOptions cascadeOptions) -> ^(TYPE_ENTITY_REF $attrName $refTo attrOptions fetchOptions cascadeOptions)
	| (TYPE_ENTITY_SET '<' refTo=ID '>' attrName=ID attrOptions fetchOptions cascadeOptions) -> ^(TYPE_ENTITY_SET $attrName $refTo attrOptions fetchOptions cascadeOptions)
	| (TYPE_ENTITY_BAG '<' refTo=ID '>' attrName=ID attrOptions fetchOptions cascadeOptions) -> ^(TYPE_ENTITY_BAG $attrName $refTo attrOptions fetchOptions cascadeOptions)
	;
	
enumRefDecl
	: (TYPE_ENUM_REF '<' enumType=ID '>' attrName=ID attrOptions) -> ^(TYPE_ENUM_REF $enumType $attrName attrOptions)
	;
	
decimalDecl
	: (TYPE_DECIMAL ID decimalSize attrOptions ) 		-> ^(TYPE_DECIMAL ID decimalSize attrOptions)
	| (TYPE_MONEY ID decimalSize attrOptions )		-> ^(TYPE_MONEY ID decimalSize attrOptions)
	;
	
byteDecl 
	: (TYPE_BIT ID lengthDecl attrOptions ) 		-> ^(TYPE_BIT ID lengthDecl attrOptions)
	| (TYPE_BLOB ID lengthDecl attrOptions )		-> ^(TYPE_BLOB ID lengthDecl attrOptions)
	;
		
		
intDecl : (TYPE_INT ID intOptions attrOptions)			-> ^(TYPE_INT ID intOptions attrOptions)
	| (TYPE_LONG ID intOptions attrOptions)			-> ^(TYPE_LONG ID intOptions attrOptions)
	| (TYPE_TINYINT ID intOptions attrOptions)		-> ^(TYPE_TINYINT ID intOptions attrOptions)
	;
	
intOptions
	: ('unsigned')? 					-> ^(INT_PROPERTIES 'unsigned'? )
	;

strDecl :	(TYPE_STRING ID lengthDecl attrOptions ) 	-> ^(TYPE_STRING ID lengthDecl attrOptions)
	| 	(TYPE_CHAR ID lengthDecl attrOptions ) 		-> ^(TYPE_CHAR ID lengthDecl attrOptions)
	|	(TYPE_CLOB ID lengthDecl attrOptions ) 		-> ^(TYPE_CLOB ID lengthDecl attrOptions)
	;	

lengthDecl
	:	'[' len=INT ']' 				-> ^(LENGTH $len)
	;

decimalSize
	:	'[' precision=INT ',' scale=INT ']'		-> ^(PRECISION_SCALE $precision $scale)
	;
			
attrOptions
	: ('required' | 'deprecated' | 'unique' | 'primarykey' )*
		-> ^(PROPERTIES 'required'? 'deprecated'? 'unique'? 'primarykey'? )
	;


fetchOptions 
	: ( 'eager' | 'lazy')?					-> ^(FETCH 'eager'? 'lazy'?)
	;
		
cascadeOptions
	: ('cascadeSave' | 'cascadeDelete' | 'cascadeAll' )? 	-> ^(CASCADE 'cascadeSave'? 'cascadeDelete'? 'cascadeAll'?)
	;
	
// com.bitgenre.orange.domain.Entity-level Constraint declarations
consDecl: 'constraint' 'primary' 'key' '(' ID (',' ID)* ')' SEMICOLON	-> ^(PRIMARY_KEY ID+)
	| 'constraint' 'unique' '(' ID (',' ID)* ')' SEMICOLON		-> ^(UNIQUE ID+)
	| 'constraint' 'check' '(' STRINGLITERAL ')' SEMICOLON		-> ^(CHECK STRINGLITERAL)	
	;
	
	
qualifiedName 
    :   ID ('.' ID )*							-> ^(QUALIFIED_NAME ID+)
    ;		
// --- Datatype TOkens ----------------

TYPE_TINYINT		: 'Tinyint' ;

TYPE_INT		: 'Int' ;

TYPE_LONG		: 'Long' ;

TYPE_STRING		: 'String' ;

TYPE_AUTOINC 		: 'Autoinc' ;

TYPE_AUTOINC_LONG 	: 'Autoinclong';

TYPE_BIT		: 'Bit' ;

TYPE_BLOB		: 'Blob' ;

TYPE_BOOLEAN		: 'Boolean' ;

TYPE_CHAR		: 'Char' ;

TYPE_CLOB		: 'Clob' ;

TYPE_DATE		: 'Date' ;

TYPE_DATETIME		: 'Datetime' ;

TYPE_FLOAT		: 'Float' ;

TYPE_DOUBLE		: 'Double' ;

TYPE_DECIMAL		: 'Decimal' ;

TYPE_MONEY		: 'Money' ;

TYPE_GUID		: 'Guid' ;

TYPE_TIMESTAMP		: 'Timestamp' ;

TYPE_PICKLIST_OPTION	: 'Picklist' ;

TYPE_ENTITY_ID		: 'EntityId' ;

TYPE_ENTITY_REF		: 'Ref' ;

TYPE_ENUM_REF 		: 'Enum' ;

TYPE_ENTITY_SET 	: 'Set' ;

TYPE_ENTITY_BAG 	: 'Bag' ;

TYPE_I18N_TEXT		: 'I18text' ;

TYPE_I18N_MONEY		: 'I18money' ;

TYPE_I18N_URL		: 'I18url' ;
		


// ------------- BASIC TOKENS --------------

ID 	:	('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'_'|'0'..'9')* 
	;
	
	
INT 	:	('0'..'9')+ 
	;
	
FLOAT 	:   ('0'..'9')+ '.' ('0'..'9')* 
    	|   '.' ('0'..'9')+ 
    	|   ('0'..'9')+
    	;
    	

WS  	:   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    	;
    	
COMMENT	:   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    	|   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    	;


STRINGLITERAL :   '"' 
        (   EscapeSequence
        |   ~( '\\' | '"' | '\r' | '\n' )        
        )* 
        '"' 
    ;
    
    
fragment
EscapeSequence  :   '\\' (
                 'b' 
             |   't' 
             |   'n' 
             |   'f' 
             |   'r' 
             |   '\"' 
             |   '\'' 
             |   '\\' 
             |       
                 ('0'..'3') ('0'..'7') ('0'..'7')
             |       
                 ('0'..'7') ('0'..'7') 
             |       
                 ('0'..'7')
             )          
;     

	
// -------------- Simple Tokens --------------    	
SEMICOLON :	';' ;
	
EQUALS 	:	'=' ;

LPAREN 	:	'(' ;
RPAREN 	:	')' ;	


//--------- TREE NODE NAMES
	

