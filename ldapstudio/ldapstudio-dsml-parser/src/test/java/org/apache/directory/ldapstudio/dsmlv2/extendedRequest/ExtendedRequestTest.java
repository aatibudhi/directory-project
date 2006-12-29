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

package org.apache.directory.ldapstudio.dsmlv2.extendedRequest;


import org.apache.directory.ldapstudio.dsmlv2.AbstractTest;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2Parser;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.extended.ExtendedRequest;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * Tests for the Extended Request parsing
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ExtendedRequestTest extends AbstractTest
{
    /**
     * Test parsing of a request with the (optional) requestID attribute
     */
    public void testRequestWithRequestId()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_requestID_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 456, extendedRequest.getMessageId() );
    }
    
    /**
     * Test parsing of a request with the (optional) requestID attribute equals to 0
     */
    public void testRequestWithRequestIdEquals0()
    {
        testParsingFail( ExtendedRequestTest.class, "request_with_requestID_equals_0.xml" );
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

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_1_control.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = extendedRequest.getCurrentControl();

        assertEquals( 1, extendedRequest.getControls().size() );
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

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_1_control_base64_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = extendedRequest.getCurrentControl();
        
        assertEquals( 1, extendedRequest.getControls().size() );
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

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_1_control_empty_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = extendedRequest.getCurrentControl();
        
        assertEquals( 1, extendedRequest.getControls().size() );
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

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_2_controls.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = extendedRequest.getCurrentControl();

        assertEquals( 2, extendedRequest.getControls().size() );
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

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_3_controls_without_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = extendedRequest.getCurrentControl();
        
        assertEquals( 3, extendedRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a request with a RequestValue element
     */
    public void testRequestWithRequestValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_requestValue.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "foobar", new String ( extendedRequest.getRequestValue() ) );
    }
    
    /**
     * Test parsing of a request with a RequestValue element with Base64 value
     */
    public void testRequestWithBase64RequestValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_base64_requestValue.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "DSMLv2.0 rocks!!", new String ( extendedRequest.getRequestValue() ) );
    }


    /**
     * Test parsing of a request with 2 requestValue Elements
     */
    public void testRequestWith2RequestValue()
    {
        testParsingFail( ExtendedRequestTest.class, "request_with_2_requestValue.xml" );
    }


    /**
     * Test parsing of a request with 2 requestName Elements
     */
    public void testRequestWith2RequestName()
    {
        testParsingFail( ExtendedRequestTest.class, "request_with_2_requestName.xml" );
    }


    /**
     * Test parsing of a request with an empty requestName
     */
    public void testRequestWithEmptyRequestName()
    {
        testParsingFail( ExtendedRequestTest.class, "request_with_empty_requestName.xml" );
    }


    /**
     * Test parsing of a request with an empty RequestValue
     */
    public void testRequestWithEmptyRequestValue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ExtendedRequestTest.class.getResource( "request_with_empty_requestValue.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ExtendedRequest extendedRequest = ( ExtendedRequest ) parser.getBatchRequest().getCurrentRequest();
        assertNull( extendedRequest.getRequestValue() );
    }
}
