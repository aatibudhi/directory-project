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

package org.apache.directory.ldapstudio.dsmlv2.batchResponse;


import org.apache.directory.ldapstudio.dsmlv2.AbstractResponseTest;
import org.apache.directory.ldapstudio.dsmlv2.BatchResponse;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2ResponseParser;
import org.apache.directory.ldapstudio.dsmlv2.reponse.ErrorResponse;
import org.apache.directory.ldapstudio.dsmlv2.reponse.SearchResponse;
import org.apache.directory.shared.ldap.codec.LdapResponse;
import org.apache.directory.shared.ldap.codec.add.AddResponse;
import org.apache.directory.shared.ldap.codec.bind.BindResponse;
import org.apache.directory.shared.ldap.codec.compare.CompareResponse;
import org.apache.directory.shared.ldap.codec.del.DelResponse;
import org.apache.directory.shared.ldap.codec.extended.ExtendedResponse;
import org.apache.directory.shared.ldap.codec.modify.ModifyResponse;
import org.apache.directory.shared.ldap.codec.modifyDn.ModifyDNResponse;


/**
 * Tests for the Compare Response parsing
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class BatchResponseTest extends AbstractResponseTest
{
    /**
     * Test parsing of a Response with the (optional) requestID attribute
     */
    public void testResponseWithRequestId()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_requestID_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1234567890, batchResponse.getRequestID() );
    }
    
    
    /**
     * Test parsing of a Response with the (optional) requestID attribute equals 0
     */
    public void testResponseWithRequestIdEquals0()
    {
        testParsingFail( BatchResponseTest.class, "response_with_requestID_equals_0.xml" );
    }


    /**
     * Test parsing of a Response with 0 Response
     */
    public void testResponseWith0Reponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_0_response.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 0, batchResponse.getResponses().size() );
    }


    /**
     * Test parsing of a Response with the 1 AddResponse
     */
    public void testResponseWith1AddResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_AddResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof AddResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 AuthResponse
     */
    public void testResponseWith1AuthResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_AuthResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof BindResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 CompareResponse
     */
    public void testResponseWith1CompareResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser
                .setInputFile( BatchResponseTest.class.getResource( "response_with_1_CompareResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof CompareResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 DelResponse
     */
    public void testResponseWith1DelResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_DelResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof DelResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 ErrorResponse
     */
    public void testResponseWith1ErrorResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_ErrorResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ErrorResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 ExtendedResponse
     */
    public void testResponseWith1ExtendedResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_ExtendedResponse.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ExtendedResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 ModDNResponse
     */
    public void testResponseWith1ModDNResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_ModDNResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ModifyDNResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 ModifyResponse
     */
    public void testResponseWith1ModifyResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_ModifyResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ModifyResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 1 SearchResponse
     */
    public void testResponseWith1SearchResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_1_SearchResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 1, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof SearchResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 AddResponse
     */
    public void testResponseWith2AddResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_AddResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof AddResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 AuthResponse
     */
    public void testResponseWith2AuthResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_AuthResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof BindResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 CompareResponse
     */
    public void testResponseWith2CompareResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser
                .setInputFile( BatchResponseTest.class.getResource( "response_with_2_CompareResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof CompareResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 DelResponse
     */
    public void testResponseWith2DelResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_DelResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof DelResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 ErrorResponse
     */
    public void testResponseWith2ErrorResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_ErrorResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ErrorResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 ExtendedResponse
     */
    public void testResponseWith2ExtendedResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_ExtendedResponse.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ExtendedResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 ModDNResponse
     */
    public void testResponseWith2ModDNResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_ModDNResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ModifyDNResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 ModifyResponse
     */
    public void testResponseWith2ModifyResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_ModifyResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof ModifyResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }


    /**
     * Test parsing of a Response with the 2 SearchResponse
     */
    public void testResponseWith2SearchResponse()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();

            parser.setInputFile( BatchResponseTest.class.getResource( "response_with_2_SearchResponse.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        BatchResponse batchResponse = parser.getBatchResponse();

        assertEquals( 2, batchResponse.getResponses().size() );

        LdapResponse response = batchResponse.getCurrentResponse();

        if ( response instanceof SearchResponse )
        {
            assertTrue( true );
        }
        else
        {
            fail();
        }
    }
}
