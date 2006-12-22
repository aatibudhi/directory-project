/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.shared.ldap.schema.syntax;


import org.apache.directory.shared.ldap.util.StringTools;


/**
 * A SyntaxChecker which verifies that a value is a Printable String according to RFC 4517.
 * 
 * From RFC 4517 :
 * 
 * PrintableString    = 1*PrintableCharacter
 * PrintableCharacter = ALPHA | DIGIT | SQUOTE | LPAREN | RPAREN |
 *                          PLUS | COMMA | HYPHEN | DOT | EQUALS |
 *                          SLASH | COLON | QUESTION | SPACE
 *                          
 * SLASH   = %x2F                ; forward slash ("/")
 * COLON   = %x3A                ; colon (":")
 * QUESTION= %x3F                ; question mark ("?")
 * 
 * From RFC 4512 :
 * ALPHA   = %x41-5A | %x61-7A   ; "A"-"Z" / "a"-"z"
 * DIGIT   = %x30 | LDIGIT       ; "0"-"9"
 * LDIGIT  = %x31-39             ; "1"-"9"
 * SQUOTE  = %x27                ; single quote ("'")
 * LPAREN  = %x28                ; left paren ("(")
 * RPAREN  = %x29                ; right paren (")")
 * PLUS    = %x2B                ; plus sign ("+")
 * COMMA   = %x2C                ; comma (",")
 * HYPHEN  = %x2D                ; hyphen ("-")
 * DOT     = %x2E                ; period (".")
 * EQUALS  = %x3D                ; equals sign ("=")
 * SPACE   = %x20                ; space (" ")
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class PrintableStringSyntaxChecker extends AbstractSyntaxChecker
{
    /** The Syntax OID, according to RFC 4517, par. 3.3.29 */
    private static final String SC_OID = "1.3.6.1.4.1.1466.115.121.1.44";
    
    /**
     * 
     * Creates a new instance of PrintableStringSyntaxChecker.
     *
     */
    public PrintableStringSyntaxChecker()
    {
        super( SC_OID );
    }
    
    
    /**
     * 
     * Creates a new instance of PrintableStringSyntaxChecker.
     * 
     * @param the oid to associate with this new SyntaxChecker
     *
     */
    protected PrintableStringSyntaxChecker( String oid )
    {
        super( oid );
    }
    
    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.schema.SyntaxChecker#isValidSyntax(java.lang.Object)
     */
    public boolean isValidSyntax( Object value )
    {
        String strValue;

        if ( value == null )
        {
            return false;
        }
        
        if ( value instanceof String )
        {
            strValue = ( String ) value;
        }
        else if ( value instanceof byte[] )
        {
            strValue = StringTools.utf8ToString( ( byte[] ) value ); 
        }
        else
        {
            strValue = value.toString();
        }

        if ( strValue.length() == 0 )
        {
            return false;
        }
        
        // We must have at least one char
        if ( strValue.length() == 0 )
        {
            return false;
        }

        return StringTools.isPrintableString( strValue );
    }
}
