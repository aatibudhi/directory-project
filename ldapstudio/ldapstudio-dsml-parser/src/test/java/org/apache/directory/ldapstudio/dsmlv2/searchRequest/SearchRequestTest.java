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

package org.apache.directory.ldapstudio.dsmlv2.searchRequest;


import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.directory.ldapstudio.dsmlv2.AbstractTest;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2Parser;
import org.apache.directory.shared.ldap.codec.AttributeValueAssertion;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.LdapConstants;
import org.apache.directory.shared.ldap.codec.search.AndFilter;
import org.apache.directory.shared.ldap.codec.search.AttributeValueAssertionFilter;
import org.apache.directory.shared.ldap.codec.search.ExtensibleMatchFilter;
import org.apache.directory.shared.ldap.codec.search.Filter;
import org.apache.directory.shared.ldap.codec.search.NotFilter;
import org.apache.directory.shared.ldap.codec.search.OrFilter;
import org.apache.directory.shared.ldap.codec.search.PresentFilter;
import org.apache.directory.shared.ldap.codec.search.SearchRequest;
import org.apache.directory.shared.ldap.codec.search.SubstringFilter;
import org.apache.directory.shared.ldap.message.ScopeEnum;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * Tests for the Del Request parsing
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class SearchRequestTest extends AbstractTest
{
    /**
     * Test parsing of a request without the dn attribute
     */
    public void testRequestWithoutDn()
    {
        testParsingFail( SearchRequestTest.class, "request_without_dn_attribute.xml" );
    }


    /**
     * Test parsing of a request with the dn attribute
     */
    public void testRequestWithDn()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_dn_attribute.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "ou=marketing,dc=microsoft,dc=com", searchRequest.getBaseObject().toString() );
    }


    /**
     * Test parsing of a request with the (optional) requestID attribute
     */
    public void testRequestWithRequestId()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_requestID_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 456, searchRequest.getMessageId() );
    }
    
    
    /**
     * Test parsing of a request with the (optional) requestID attribute equals to 0
     */
    public void testRequestWithRequestIdEquals0()
    {
        testParsingFail( SearchRequestTest.class, "request_with_requestID_equals_0.xml" );
    }


    /**
     * Test parsing of a request with a (optional) Control element
     */
    public void testRequestWith1Control()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_1_control.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = searchRequest.getCurrentControl();
        
        assertEquals( 1, searchRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }
    
    /**
     * Test parsing of a request with a (optional) Control element with Base64 value
     */
    public void testRequestWith1ControlBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_1_control_base64_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = searchRequest.getCurrentControl();
        
        assertEquals( 1, searchRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( "DSMLv2.0 rocks!!", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }


    /**
     * Test parsing of a request with a (optional) Control element with empty value
     */
    public void testRequestWith1ControlEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_1_control_empty_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = searchRequest.getCurrentControl();
        
        assertEquals( 1, searchRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a request with 2 (optional) Control elements
     */
    public void testRequestWith2Controls()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_2_controls.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = searchRequest.getCurrentControl();
        
        assertEquals( 2, searchRequest.getControls().size() );
        assertFalse( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.789", control.getControlType() );
        assertEquals( "Some other text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }


    /**
     * Test parsing of a request with 3 (optional) Control elements without value
     */
    public void testRequestWith3ControlsWithoutValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_3_controls_without_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = searchRequest.getCurrentControl();
        
        assertEquals( 3, searchRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a request without the Filter element
     */
    public void testRequestWithoutFilter()
    {
        testParsingFail( SearchRequestTest.class, "request_without_filter.xml" );
    }


    /**
     * Test parsing of a request without scope attribute
     */
    public void testRequestWithoutScopeAttribute()
    {
        testParsingFail( SearchRequestTest.class, "request_without_scope_attribute.xml" );
    }


    /**
     * Test parsing of a request with scope attribute to BaseObject value
     * @throws NamingException 
     */
    public void testRequestWithScopeBaseObject()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_scope_baseObject.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( ScopeEnum.BASE_OBJECT, searchRequest.getScope() );
    }


    /**
     * Test parsing of a request with scope attribute to SingleLevel value
     * @throws NamingException 
     */
    public void testRequestWithScopeSingleLevel()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_scope_singleLevel.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( ScopeEnum.SINGLE_LEVEL, searchRequest.getScope() );
    }


    /**
     * Test parsing of a request with scope attribute to WholeSubtree value
     * @throws NamingException 
     */
    public void testRequestWithScopeWholeSubtree()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser
                .setInputFile( SearchRequestTest.class.getResource( "request_with_scope_wholeSubtree.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( ScopeEnum.WHOLE_SUBTREE, searchRequest.getScope() );
    }


    /**
     * Test parsing of a request with scope attribute to Error value
     */
    public void testRequestWithScopeError()
    {
        testParsingFail( SearchRequestTest.class, "request_with_scope_error.xml" );
    }


    /**
     * Test parsing of a request without derefAliases attribute
     */
    public void testRequestWithoutDerefAliasesAttribute()
    {
        testParsingFail( SearchRequestTest.class, "request_without_derefAliases_attribute.xml" );
    }


    /**
     * Test parsing of a request with derefAliases attribute to derefAlways value
     * @throws NamingException 
     */
    public void testRequestWithDerefAliasesDerefAlways()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_derefAliases_derefAlways.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( LdapConstants.DEREF_ALWAYS, searchRequest.getDerefAliases() );
    }


    /**
     * Test parsing of a request with derefAliases attribute to derefFindingBaseObj value
     * @throws NamingException 
     */
    public void testRequestWithDerefAliasesDerefFindingBaseObj()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "request_with_derefAliases_derefFindingBaseObj.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( LdapConstants.DEREF_FINDING_BASE_OBJ, searchRequest.getDerefAliases() );
    }


    /**
     * Test parsing of a request with derefAliases attribute to derefinSearching value
     * @throws NamingException 
     */
    public void testRequestWithDerefAliasesDerefinSearching()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_derefAliases_derefInSearching.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( LdapConstants.DEREF_IN_SEARCHING, searchRequest.getDerefAliases() );
    }


    /**
     * Test parsing of a request with derefAliases attribute to neverDerefAliases value
     * @throws NamingException 
     */
    public void testRequestWithDerefAliasesNeverDerefAliases()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class
                .getResource( "request_with_derefAliases_neverDerefAliases.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( LdapConstants.NEVER_DEREF_ALIASES, searchRequest.getDerefAliases() );
    }


    /**
     * Test parsing of a request with derefAliases attribute to Error value
     * @throws NamingException 
     */
    public void testRequestWithDerefAliasesError()
    {
        testParsingFail( SearchRequestTest.class, "request_with_derefAliases_error.xml" );
    }


    /**
     * Test parsing of a request with the sizeLimit (optional) attribute
     * @throws NamingException 
     */
    public void testRequestWithSizeLimitAttribute()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_sizeLimit_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 1000, searchRequest.getSizeLimit() );
    }


    /**
     * Test parsing of a request with sizeLimit attribute to Error value
     * @throws NamingException 
     */
    public void testRequestWithSizeLimitError()
    {
        testParsingFail( SearchRequestTest.class, "request_with_sizeLimit_error.xml" );
    }


    /**
     * Test parsing of a request with the timeLimit (optional) attribute
     * @throws NamingException 
     */
    public void testRequestWithTimeLimitAttribute()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_timeLimit_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 60, searchRequest.getTimeLimit() );
    }


    /**
     * Test parsing of a request with timeLimit attribute to Error value
     * @throws NamingException 
     */
    public void testRequestWithTimeLimitError()
    {
        testParsingFail( SearchRequestTest.class, "request_with_timeLimit_error.xml" );
    }


    /**
     * Test parsing of a request with typesOnly to true
     */
    public void testRequestWithTypesOnlyTrue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_typesOnly_true.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertTrue( searchRequest.isTypesOnly() );
    }


    /**
     * Test parsing of a request with typesOnly to 1
     */
    public void testRequestWithTypesOnly1()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_typesOnly_1.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertTrue( searchRequest.isTypesOnly() );
    }


    /**
     * Test parsing of a request with typesOnly to false
     */
    public void testRequestWithTypesOnlyFalse()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_typesOnly_false.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertFalse( searchRequest.isTypesOnly() );
    }


    /**
     * Test parsing of a request with typesOnly to 0
     */
    public void testRequestWithTypesOnlyRdn0()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_typesOnly_0.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertFalse( searchRequest.isTypesOnly() );
    }


    /**
     * Test parsing of a request with typesOnly to an error value
     */
    public void testRequestWithTypesOnlyError()
    {
        testParsingFail( SearchRequestTest.class, "request_with_typesOnly_error.xml" );
    }


    /**
     * Test parsing of a request with 2 Filter elements
     */
    public void testRequestWith2Filters()
    {
        testParsingFail( SearchRequestTest.class, "request_with_2_filters.xml" );
    }


    /**
     * Test parsing of a request with Attibutes Element but not any Attribute element
     */
    public void testRequestWithAttributesButNoAttribute()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_attributes_but_no_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        assertTrue( true );
    }


    /**
     * Test parsing of a request with 2 Attributes elements
     */
    public void testRequestWith2AttributesElements()
    {
        testParsingFail( SearchRequestTest.class, "request_with_2_attributes_elements.xml" );
    }


    /**
     * Test parsing of a request with an Attributes element with 1 Attribute element
     * @throws NamingException 
     */
    public void testRequestWithAttributes1Attribute() throws NamingException
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_attributes_1_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Attributes attributes = searchRequest.getAttributes();

        assertEquals( 1, attributes.size() );

        NamingEnumeration ne = attributes.getAll();

        Attribute attribute = ( Attribute ) ne.next();

        assertEquals( "sn", attribute.getID() );
    }


    /**
     * Test parsing of a request with an Attributes element with 2 Attribute elements
     * @throws NamingException 
     */
    public void testRequestWithAttributes2Attribute() throws NamingException
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "request_with_attributes_2_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Attributes attributes = searchRequest.getAttributes();

        assertEquals( 2, attributes.size() );

        NamingEnumeration ne = attributes.getAll();

        Attribute attribute = ( Attribute ) ne.next();

        assertEquals( "sn", attribute.getID() );

        attribute = ( Attribute ) ne.next();

        assertEquals( "givenName", attribute.getID() );
    }


    /**
     * Test parsing of a request with 1 Attribute without name attribute
     */
    public void testRequestWithAttributeWithoutNameAttribute()
    {
        testParsingFail( SearchRequestTest.class, "request_with_attribute_without_name_attribute.xml" );
    }


    /**
     * Test parsing of a request with empty Filter element
     */
    public void testRequestWithEmptyFilter()
    {
        testParsingFail( SearchRequestTest.class, "request_with_empty_filter.xml" );
    }


    /**
     * Test parsing of a request with an And Filter
     */
    public void testRequestWithAndFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_and.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AndFilter );
    }


    /**
     * Test parsing of a request with an Or Filter
     */
    public void testRequestWithOrFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_or.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof OrFilter );
    }


    /**
     * Test parsing of a request with an Or Filter
     */
    public void testRequestWithNotFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_not.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof NotFilter );
    }


    /**
     * Test parsing of a request with empty Filter element
     */
    public void testRequestWithNotFilterWith2Children()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_not_with_2_children.xml" );
    }


    /**
     * Test parsing of a request with an approxMatch Filter
     */
    public void testRequestWithApproxMatchFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_approxMatch.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.APPROX_MATCH_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "foobar", assertion.getAssertionValue() );
    }
    
    /**
     * Test parsing of a request with an approxMatch Filter with base64 value
     */
    public void testRequestWithApproxMatchFilterBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_approxMatch_base64_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.APPROX_MATCH_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "DSMLv2.0 rocks!!", new String( (byte[]) assertion.getAssertionValue() ) );
    }


    /**
     * Test parsing of a request with an approxMatch Filter with empty value
     */
    public void testRequestWithApproxMatchFilterEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_approxMatch_with_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.APPROX_MATCH_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertNull( assertion.getAssertionValue() );
    }


    /**
     * Test parsing of a request with approxMatch Filter but no name attribute
     */
    public void testRequestWithApproxMatchFilterWithoutName()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_approxMatch_without_name.xml" );
    }


    /**
     * Test parsing of a request with approxMatch Filter but no value element
     */
    public void testRequestWithApproxMatchFilterWithoutValue()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_approxMatch_without_value.xml" );
    }


    /**
     * Test parsing of a request with approxMatch Filter with 2 Value elements
     */
    public void testRequestWithApproxMatchFilterWith2Values()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_approxMatch_with_2_values.xml" );
    }


    /**
     * Test parsing of a request with an greaterOrEqual Filter
     */
    public void testRequestWithGreaterOrEqualFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_greaterOrEqual.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.GREATER_OR_EQUAL_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "foobar", assertion.getAssertionValue() );
    }
    
    /**
     * Test parsing of a request with an greaterOrEqual Filter with base64 value
     */
    public void testRequestWithGreaterOrEqualFilterBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_greaterOrEqual_base64_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.GREATER_OR_EQUAL_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "DSMLv2.0 rocks!!", new String( (byte[]) assertion.getAssertionValue() ) );
    }


    /**
     * Test parsing of a request with an greaterOrEqual Filter with an empty value
     */
    public void testRequestWithGreaterOrEqualFilterEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_greaterOrEqual_with_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.GREATER_OR_EQUAL_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertNull( assertion.getAssertionValue() );
    }


    /**
     * Test parsing of a request with greaterOrEqual Filter but no name attribute
     */
    public void testRequestWithGreaterOrEqualFilterWithoutName()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_greaterOrEqual_without_name.xml" );
    }


    /**
     * Test parsing of a request with greaterOrEqual Filter but no value element
     */
    public void testRequestWithGreaterOrEqualFilterWithoutValue()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_greaterOrEqual_without_value.xml" );
    }


    /**
     * Test parsing of a request with greaterOrEqual Filter with 2 Value elements
     */
    public void testRequestWithGreaterOrEqualFilterWith2Values()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_greaterOrEqual_with_2_values.xml" );
    }


    /**
     * Test parsing of a request with an lessOrEqual Filter
     */
    public void testRequestWithLessOrEqualFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_lessOrEqual.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.LESS_OR_EQUAL_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "foobar", assertion.getAssertionValue() );
    }
    
    /**
     * Test parsing of a request with an lessOrEqual Filter with Base64 value
     */
    public void testRequestWithLessOrEqualFilterBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_lessOrEqual_base64_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.LESS_OR_EQUAL_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "DSMLv2.0 rocks!!", new String( (byte[]) assertion.getAssertionValue() ) );
    }


    /**
     * Test parsing of a request with an lessOrEqual Filter
     */
    public void testRequestWithLessOrEqualFilterEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_lessOrEqual_with_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.LESS_OR_EQUAL_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertNull( assertion.getAssertionValue() );
    }


    /**
     * Test parsing of a request with lessOrEqual Filter but no name attribute
     */
    public void testRequestWithLessOrEqualFilterWithoutName()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_lessOrEqual_without_name.xml" );
    }


    /**
     * Test parsing of a request with lessOrEqual Filter but no value element
     */
    public void testRequestWithLessOrEqualFilterWithoutValue()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_lessOrEqual_without_value.xml" );
    }


    /**
     * Test parsing of a request with lessOrEqual Filter with 2 Value elements
     */
    public void testRequestWithLessOrEqualFilterWith2Values()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_lessOrEqual_with_2_values.xml" );
    }


    /**
     * Test parsing of a request with an Equality Filter
     */
    public void testRequestWithEqualityMatchFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_equalityMatch.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.EQUALITY_MATCH_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "foobar", assertion.getAssertionValue() );
    }
    
    /**
     * Test parsing of a request with an Equality Filter with base64 value
     */
    public void testRequestWithEqualityMatchFilterBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_equalityMatch_base64_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.EQUALITY_MATCH_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertEquals( "DSMLv2.0 rocks!!", new String( (byte[]) assertion.getAssertionValue() ) );
    }


    /**
     * Test parsing of a request with an Equality Filter with an empty value
     */
    public void testRequestWithEqualityMatchFilterWithEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_equalityMatch_with_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof AttributeValueAssertionFilter );

        AttributeValueAssertionFilter approxMatchFilter = ( AttributeValueAssertionFilter ) filter;

        assertEquals( LdapConstants.EQUALITY_MATCH_FILTER, approxMatchFilter.getFilterType() );

        AttributeValueAssertion assertion = approxMatchFilter.getAssertion();

        assertEquals( "sn", assertion.getAttributeDesc() );

        assertNull( assertion.getAssertionValue() );
    }


    /**
     * Test parsing of a request with EqualityMatch Filter but no name attribute
     */
    public void testRequestWithEqualityMatchFilterWithoutName()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_equalityMatch_without_name.xml" );
    }


    /**
     * Test parsing of a request with EqualityMatch Filter but no value element
     */
    public void testRequestWithEqualityMatchFilterWithoutValue()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_equalityMatch_without_value.xml" );
    }


    /**
     * Test parsing of a request with EqualityMatch Filter with 2 Value elements
     */
    public void testRequestWithEqualityMatchFilterWith2Values()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_equalityMatch_with_2_values.xml" );
    }


    /**
     * Test parsing of a request with an Present Filter
     */
    public void testRequestWithPresentFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_present.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof PresentFilter );

        PresentFilter presentFilter = ( PresentFilter ) filter;

        assertEquals( "givenName", presentFilter.getAttributeDescription().toString() );
    }


    /**
     * Test parsing of a request with Present Filter without name attribute
     */
    public void testRequestWithPresentWithoutName()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_present_without_name.xml" );
    }


    /**
     * Test parsing of a request with an ExtensibleMatch Filter
     */
    public void testRequestWithExtensibleMatchFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_extensibleMatch.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertEquals( "A Value", extensibleMatchFilter.getMatchValue() );

        assertEquals( false, extensibleMatchFilter.isDnAttributes() );
    }
    
    /**
     * Test parsing of a request with an ExtensibleMatch Filter
     */
    public void testRequestWithExtensibleMatchFilterBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_extensibleMatch_base64_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertEquals( "DSMLv2.0 rocks!!", new String( (byte[]) extensibleMatchFilter.getMatchValue() ) );

        assertEquals( false, extensibleMatchFilter.isDnAttributes() );
    }


    /**
     * Test parsing of a request with an ExtensibleMatch Filter with empty value
     */
    public void testRequestWithExtensibleMatchWithEmptyValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertNull( extensibleMatchFilter.getMatchValue() );

        assertEquals( false, extensibleMatchFilter.isDnAttributes() );
    }


    /**
     * Test parsing of a request with ExtensibleMatch Filter without Value element
     */
    public void testRequestWithExtensibleMatchWithoutValue()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_extensibleMatch_without_value.xml" );
    }


    /**
     * Test parsing of a request with ExtensibleMatch Filter with 2 Value elements
     */
    public void testRequestWithExtensibleMatchWith2Values()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_extensibleMatch_with_2_values.xml" );
    }


    /**
     * Test parsing of a request with typesOnly to true
     */
    public void testRequestWithExtensibleMatchWithDnAttributesTrue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_dnAttributes_true.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertTrue( extensibleMatchFilter.isDnAttributes() );
    }


    /**
     * Test parsing of a request with typesOnly to 1
     */
    public void testRequestWithExtensibleMatchWithDnAttributes1()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_dnAttributes_1.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertTrue( extensibleMatchFilter.isDnAttributes() );
    }


    /**
     * Test parsing of a request with typesOnly to false
     */
    public void testRequestWithExtensibleMatchWithDnAttributesFalse()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_dnAttributes_false.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertFalse( extensibleMatchFilter.isDnAttributes() );
    }


    /**
     * Test parsing of a request with typesOnly to 0
     */
    public void testRequestWithExtensibleMatchWithDnAttributes0()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_dnAttributes_0.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertFalse( extensibleMatchFilter.isDnAttributes() );
    }


    /**
     * Test parsing of a request with typesOnly to an error value
     */
    public void testRequestWithExtensibleMatchWithDnAttributesError()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_extensibleMatch_with_dnAttributes_error.xml" );
    }


    /**
     * Test parsing of a request with a matchingRule attribute
     */
    public void testRequestWithExtensibleMatchWithMatchingRule()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_matchingRule.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertEquals( "AMatchingRuleName", extensibleMatchFilter.getMatchingRule().toString() );
    }


    /**
     * Test parsing of a request with a name attribute
     */
    public void testRequestWithExtensibleMatchWithName()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_extensibleMatch_with_name.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof ExtensibleMatchFilter );

        ExtensibleMatchFilter extensibleMatchFilter = ( ExtensibleMatchFilter ) filter;

        assertEquals( "givenName", extensibleMatchFilter.getType().toString() );
    }


    /**
     * Test parsing of a request with an Substrings Filter
     */
    public void testRequestWithSubstringsFilter()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser
                .setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "sn", substringFilter.getType().toString() );
    }


    /**
     * Test parsing of a request with Substrings Filter without name
     */
    public void testRequestWithSubstringsWithoutName()
    {
        testParsingFail( SearchRequestTest.class, "filters/request_with_substrings_without_name.xml" );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Initial element
     */
    public void testRequestWithSubstrings1Initial()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_1_initial.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "jack", substringFilter.getInitialSubstrings().toString() );
    }
    
    /**
     * Test parsing of a request with a Substrings Filter with 1 Initial element with Base64 value
     */
    public void testRequestWithSubstrings1Base64Initial()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_1_base64_initial.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "DSMLv2.0 rocks!!", substringFilter.getInitialSubstrings().toString() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 emptyInitial element
     */
    public void testRequestWithSubstrings1EmptyInitial()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_substrings_1_empty_initial.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertNull( substringFilter.getInitialSubstrings() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Initial and 1 Any elements
     */
    public void testRequestWithSubstrings1Initial1Any()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_substrings_1_initial_1_any.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "jack", substringFilter.getInitialSubstrings() );

        List<String> initials = substringFilter.getAnySubstrings();

        assertEquals( 1, initials.size() );

        assertEquals( "kate", initials.get( 0 ).toString() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Initial and 1 Final elements
     */
    public void testRequestWithSubstrings1Initial1Final()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_substrings_1_initial_1_final.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "jack", substringFilter.getInitialSubstrings() );

        assertEquals( "john", substringFilter.getFinalSubstrings() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Any element
     */
    public void testRequestWithSubstrings1Any()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_1_any.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        List<String>initials = substringFilter.getAnySubstrings();

        assertEquals( 1, initials.size() );
        assertEquals( "kate", initials.get( 0 ) );
    }
    
    /**
     * Test parsing of a request with a Substrings Filter with 1 Any element
     */
    public void testRequestWithSubstrings1Base64Any()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_1_base64_any.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        List<String> initials = substringFilter.getAnySubstrings();

        assertEquals( 1, initials.size() );
        assertEquals( "DSMLv2.0 rocks!!", initials.get( 0 ) );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 empty Any element
     */
    public void testRequestWithSubstrings1EmptyAny()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class
                .getResource( "filters/request_with_substrings_1_empty_any.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        List<String> initials = substringFilter.getAnySubstrings();

        assertEquals( 0, initials.size() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Any element
     */
    public void testRequestWithSubstrings2Any()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_2_any.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        List<String> initials = substringFilter.getAnySubstrings();

        assertEquals( 2, initials.size() );

        assertEquals( "kate", initials.get( 0 ) );

        assertEquals( "sawyer", initials.get( 1 ) );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Any and 1 Final elements
     */
    public void testRequestWithSubstrings1Any1Final()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_substrings_1_any_1_final.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        List<String> initials = substringFilter.getAnySubstrings();

        assertEquals( 1, initials.size() );

        assertEquals( "kate", initials.get( 0 ) );

        assertEquals( "john", substringFilter.getFinalSubstrings() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 Final element
     */
    public void testRequestWithSubstrings1Final()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_1_final.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "john", substringFilter.getFinalSubstrings().toString() );
    }
    
    /**
     * Test parsing of a request with a Substrings Filter with 1 Final element
     */
    public void testRequestWithSubstrings1Base64Final()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource( "filters/request_with_substrings_1_base64_final.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertEquals( "DSMLv2.0 rocks!!", substringFilter.getFinalSubstrings().toString() );
    }


    /**
     * Test parsing of a request with a Substrings Filter with 1 empty Final element
     */
    public void testRequestWithSubstrings1EmptyFinal()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( SearchRequestTest.class.getResource(
                "filters/request_with_substrings_1_empty_final.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        Filter filter = searchRequest.getFilter();

        assertTrue( filter instanceof SubstringFilter );

        SubstringFilter substringFilter = ( SubstringFilter ) filter;

        assertNull( substringFilter.getFinalSubstrings() );
    }


    /**
     * Test parsing of a request with a SubEntries Control
     */
    public void testRequestWithSubEntriesControl()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser
                .setInputFile( SearchRequestTest.class.getResource( "request_with_subentries_control.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchRequest searchRequest = ( SearchRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 1, searchRequest.getControls().size() );

        Control control = searchRequest.getCurrentControl();

        assertTrue( control.getCriticality() );

        assertEquals( "1.3.6.1.4.1.4203.1.10.1", control.getControlType() );

        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );

    }
}
