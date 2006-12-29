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

package org.apache.directory.ldapstudio.dsmlv2.delRequest;


import org.apache.directory.ldapstudio.dsmlv2.AbstractTest;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2Parser;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.del.DelRequest;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * Tests for the Del Request parsing
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class DelRequestTest extends AbstractTest
{
    /**
     * Test parsing of a request without the dn attribute
     */
    public void testRequestWithoutDn()
    {
        testParsingFail( DelRequestTest.class, "request_without_dn_attribute.xml" );
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

            parser.setInputFile( DelRequestTest.class.getResource( "request_with_dn_attribute.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "cn=Bob Rush,ou=Dev,dc=Example,dc=COM", delRequest.getEntry().toString() );
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

            parser.setInputFile( DelRequestTest.class.getResource( "request_with_requestID_attribute.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 456, delRequest.getMessageId() );
    }
    
    /**
     * Test parsing of a request with the (optional) requestID attribute equals to 0
     */
    public void testRequestWithRequestIdEquals0()
    {
        testParsingFail( DelRequestTest.class, "request_with_requestID_equals_0.xml" );
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

            parser.setInputFile( DelRequestTest.class.getResource( "request_with_1_control.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = delRequest.getCurrentControl();
        
        assertEquals( 1, delRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }
    
    /**
     * Test parsing of a request with a (optional) Control element
     */
    public void testRequestWith1ControlBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( DelRequestTest.class.getResource( "request_with_1_control_base64_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = delRequest.getCurrentControl();
        
        assertEquals( 1, delRequest.getControls().size() );
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

            parser
                .setInputFile( DelRequestTest.class.getResource( "request_with_1_control_empty_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = delRequest.getCurrentControl();
        
        assertEquals( 1, delRequest.getControls().size() );
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

            parser.setInputFile( DelRequestTest.class.getResource( "request_with_2_controls.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = delRequest.getCurrentControl();
        
        assertEquals( 2, delRequest.getControls().size() );
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

            parser.setInputFile( DelRequestTest.class.getResource( "request_with_3_controls_without_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        DelRequest delRequest = ( DelRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = delRequest.getCurrentControl();
        
        assertEquals( 3, delRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }
}
