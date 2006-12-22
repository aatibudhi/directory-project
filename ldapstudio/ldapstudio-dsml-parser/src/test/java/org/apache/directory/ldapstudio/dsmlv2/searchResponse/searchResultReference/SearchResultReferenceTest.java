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

package org.apache.directory.ldapstudio.dsmlv2.searchResponse.searchResultReference;


import java.util.List;

import org.apache.directory.ldapstudio.dsmlv2.AbstractResponseTest;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2ResponseParser;
import org.apache.directory.ldapstudio.dsmlv2.batchResponse.BatchResponseTest;
import org.apache.directory.ldapstudio.dsmlv2.reponse.SearchResponse;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.search.SearchResultReference;
import org.apache.directory.shared.ldap.codec.util.LdapURL;
import org.apache.directory.shared.ldap.codec.util.LdapURLEncodingException;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * Tests for the Search Result Reference Response parsing
 */
public class SearchResultReferenceTest extends AbstractResponseTest
{
    /**
     * Test parsing of a response with a (optional) Control element
     */
    public void testResponseWith1Control()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser
                .setInputFile( SearchResultReferenceTest.class.getResource( "response_with_1_control.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        assertEquals( 1, searchResultReference.getControls().size() );

        Control control = searchResultReference.getCurrentControl();

        assertTrue( control.getCriticality() );

        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );

        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }
    
    /**
     * Test parsing of a response with a (optional) Control element with empty value
     */
    public void testResponseWith1ControlEmptyValue()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser
                .setInputFile( SearchResultReferenceTest.class.getResource( "response_with_1_control_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        assertEquals( 1, searchResultReference.getControls().size() );

        Control control = searchResultReference.getCurrentControl();

        assertTrue( control.getCriticality() );

        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );

        assertEquals( StringTools.EMPTY_BYTES, ( byte[] ) control.getControlValue() );
    }


    /**
     * Test parsing of a response with 2 (optional) Control elements
     */
    public void testResponseWith2Controls()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( SearchResultReferenceTest.class.getResource( "response_with_2_controls.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        assertEquals( 2, searchResultReference.getControls().size() );

        Control control = searchResultReference.getCurrentControl();

        assertFalse( control.getCriticality() );

        assertEquals( "1.2.840.113556.1.4.789", control.getControlType() );

        assertEquals( "Some other text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }


    /**
     * Test parsing of a response with 3 (optional) Control elements without value
     */
    public void testResponseWith3ControlsWithoutValue()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( SearchResultReferenceTest.class.getResource(
                "response_with_3_controls_without_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        assertEquals( 3, searchResultReference.getControls().size() );

        Control control = searchResultReference.getCurrentControl();

        assertTrue( control.getCriticality() );

        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );

        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a Response with the (optional) requestID attribute
     */
    public void testResponseWithRequestId()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( SearchResultReferenceTest.class.getResource( "response_with_requestID_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        assertEquals( 456, searchResultReference.getMessageId() );
    }
    
    /**
     * Test parsing of a Response with the (optional) requestID attribute equals 0
     */
    public void testResponseWithRequestIdEquals0()
    {
        testParsingFail( SearchResultReferenceTest.class, "response_with_requestID_equals_0.xml" );
    }


    /**
     * Test parsing of a response with 0 Ref
     */
    public void testResponseWith0Ref()
    {
        testParsingFail( SearchResultReferenceTest.class, "response_with_0_ref.xml" );
    }


    /**
     * Test parsing of a Response with 1 Ref
     */
    public void testResponseWith1Ref()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( SearchResultReferenceTest.class.getResource( "response_with_1_ref.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        List references = searchResultReference.getSearchResultReferences();

        assertEquals( 1, references.size() );

        try
        {
            assertEquals( new LdapURL( "ldap://localhost" ).toString(), references.get( 0 ).toString() );
        }
        catch ( LdapURLEncodingException e )
        {
            fail();
        }
    }
    
    /**
     * Test parsing of a Response with 1 Ref
     */
    public void testResponseWith1EmptyRef()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( SearchResultReferenceTest.class.getResource( "response_with_1_empty_ref.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        List references = searchResultReference.getSearchResultReferences();

        assertEquals( 0, references.size() );
    }



    /**
     * Test parsing of a Response with 2 Ref
     */
    public void testResponseWith2Ref()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( SearchResultReferenceTest.class.getResource( "response_with_2_ref.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        SearchResultReference searchResultReference = ( ( SearchResponse ) parser.getBatchResponse()
            .getCurrentResponse() ).getCurrentSearchResultReference();

        List references = searchResultReference.getSearchResultReferences();

        assertEquals( 2, references.size() );

        try
        {
            assertEquals( new LdapURL( "ldap://localhost" ).toString(), references.get( 0 ).toString() );
        }
        catch ( LdapURLEncodingException e )
        {
            fail();
        }

        try
        {
            assertEquals( new LdapURL( "ldap://www.apache.org" ).toString(), references.get( 1 ).toString() );
        }
        catch ( LdapURLEncodingException e )
        {
            fail();
        }
    }


    /**
     * Test parsing of a response with 1 wrong Ref
     */
    public void testResponseWith1WrongRef()
    {
        testParsingFail( SearchResultReferenceTest.class, "response_with_1_wrong_ref.xml" );
    }
}
