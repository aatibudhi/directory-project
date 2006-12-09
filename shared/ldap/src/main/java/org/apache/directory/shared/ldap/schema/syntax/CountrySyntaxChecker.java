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


import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingException;


import org.apache.directory.shared.ldap.exception.LdapInvalidAttributeValueException;
import org.apache.directory.shared.ldap.message.ResultCodeEnum;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * A SyntaxChecker which verifies that a value is a country according to RFC 4517.
 * 
 * From RFC 4517 :
 * 
 * A value of the Country String syntax is one of the two-character
 * codes from ISO 3166 [ISO3166] for representing a country.
 * 
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class CountrySyntaxChecker implements SyntaxChecker
{
    /** The Syntax OID, according to RFC 4517, par. 3.3.4 */
    public static final String DEFAULT_OID = "1.3.6.1.4.1.1466.115.121.1.11";
    
    /** The Syntax OID */
    private final String oid;
    
    /** The ISO 3166 list of countries, as of 2006 */
    private static final String[] COUNTRY_ISO_3166 =
        {
        "AD", "AE", "AF", "AG", "AI", "AL", "AM", "AN", "AO", "AQ", "AR", "AS", "AT", "AU", "AW", "AX", "AZ",
        "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BM", "BN", "BO", "BR", "BS", "BT", "BV", "BW", "BY", "BZ",
        "CA", "CC", "CD", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CX", "CY", "CZ",
        "DE", "DJ", "DK", "DM", "DO", "DZ",
        "EC", "EE", "EG", "EH", "ER", "ES", "ET",
        "FI", "FJ", "FK", "FM", "FO", "FR",
        "GA", "GB", "GD", "GE", "GG", "GF", "GH", "GI", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY",
        "HK", "HM", "HN", "HR", "HT", "HU",
        "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT",
        "JE", "JM", "JO", "JP",
        "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ",
        "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY",
        "MA", "MC", "MD", "ME", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ",
        "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ",
        "OM",
        "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY",
        "QA",
        "RE", "RO", "RS", "RU", "RW", 
        "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SV", "SY", "SZ",
        "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TL", "TM", "TN", "TO", "TR", "TT", "TV", "TW", "TZ",
        "UA", "UG", "UM", "US", "UY", "UZ",
        "VA", "VC", "VE", "VG", "VI", "VN", "VU",
        "WF", "WS",
        "YE", "YT",
        "ZA", "ZM", "ZW"
        };
    
    /** The Set which contains the countries */
    private final static Set<String> COUNTRIES = new HashSet<String>();
    
    /** Initialization of the country set */
    static
    {
        for ( String country:COUNTRY_ISO_3166 )
        {
            COUNTRIES.add( country );
        }
    }
    
    public CountrySyntaxChecker( String oid )
    {
        this.oid = oid;
    }
    
    /**
     * 
     * Creates a new instance of CountrySyntaxChecker.
     *
     */
    public CountrySyntaxChecker()
    {
        this.oid = DEFAULT_OID;
    }
    
    
    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.schema.SyntaxChecker#assertSyntax(java.lang.Object)
     */
    public void assertSyntax( Object value ) throws NamingException
    {
        if ( ! isValidSyntax( value ) )
        {
            throw new LdapInvalidAttributeValueException( ResultCodeEnum.INVALID_ATTRIBUTE_SYNTAX );
        }
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.schema.SyntaxChecker#getSyntaxOid()
     */
    public String getSyntaxOid()
    {
        return oid;
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
        
        return COUNTRIES.contains( StringTools.toUpperCase( strValue ) );
    }
}
