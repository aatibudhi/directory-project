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

package org.apache.directory.ldapstudio.dsmlv2.modDNRequest;


import org.apache.directory.ldapstudio.dsmlv2.AbstractTest;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2Parser;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.modifyDn.ModifyDNRequest;
import org.apache.directory.shared.ldap.util.StringTools;


/**
 * Tests for the Modify DN Request parsing
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ModifyDNRequestTest extends AbstractTest
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

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_requestID_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( 456, modifyDNRequest.getMessageId() );
    }
    
    /**
     * Test parsing of a request with the (optional) requestID attribute equals to 0
     */
    public void testRequestWithRequestIdEquals0()
    {
        testParsingFail( ModifyDNRequestTest.class, "request_with_requestID_equals_0.xml" );
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

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_1_control.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = modifyDNRequest.getCurrentControl();
        
        assertEquals( 1, modifyDNRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }
    
    /**
     * Test parsing of a request with a (optional) Control element with Base64 Value
     */
    public void testRequestWith1ControlBase64Value()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_1_control_base64_value.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = modifyDNRequest.getCurrentControl();
        
        assertEquals( 1, modifyDNRequest.getControls().size() );
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

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_1_control_empty_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = modifyDNRequest.getCurrentControl();
        
        assertEquals( 1, modifyDNRequest.getControls().size() );
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

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_2_controls.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = modifyDNRequest.getCurrentControl();
        
        assertEquals( 2, modifyDNRequest.getControls().size() );
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

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_3_controls_without_value.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();
        Control control = modifyDNRequest.getCurrentControl();
        
        assertEquals( 3, modifyDNRequest.getControls().size() );
        assertTrue( control.getCriticality() );
        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }


    /**
     * Test parsing of a request without dn attribute
     */
    public void testRequestWithoutDnAttribute()
    {
        testParsingFail( ModifyDNRequestTest.class, "request_without_dn_attribute.xml" );
    }


    /**
     * Test parsing of a request without newrdn attribute
     */
    public void testRequestWithoutNewRdnAttribute()
    {
        testParsingFail( ModifyDNRequestTest.class, "request_without_newrdn_attribute.xml" );
    }


    /**
     * Test parsing of a request without dn and newrdn attributes
     */
    public void testRequestWithDnAndNewRdnAttributes()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_dn_and_newrdn_attributes.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "cn=Bob Rush,ou=Dev,dc=Example,dc=COM", modifyDNRequest.getEntry().toString() );

        assertEquals( "cn=Steve Jobs", modifyDNRequest.getNewRDN().toString() );
    }


    /**
     * Test parsing of a request with deleteoldrdn to true
     */
    public void testRequestWithDeleteOldRdnTrue()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_deleteoldrdn_true.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertTrue( modifyDNRequest.isDeleteOldRDN() );
    }


    /**
     * Test parsing of a request with deleteoldrdn to 1
     */
    public void testRequestWithDeleteOldRdn1()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_deleteoldrdn_1.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertTrue( modifyDNRequest.isDeleteOldRDN() );
    }


    /**
     * Test parsing of a request with deleteoldrdn to false
     */
    public void testRequestWithDeleteOldRdnFalse()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_deleteoldrdn_false.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertFalse( modifyDNRequest.isDeleteOldRDN() );
    }


    /**
     * Test parsing of a request with deleteoldrdn to 0
     */
    public void testRequestWithDeleteOldRdn0()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_deleteoldrdn_0.xml" ).getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertFalse( modifyDNRequest.isDeleteOldRDN() );
    }


    /**
     * Test parsing of a request with deleteoldrdn to an error value
     */
    public void testRequestWithDeleteOldRdnError()
    {
        testParsingFail( ModifyDNRequestTest.class, "request_with_deleteoldrdn_error.xml" );
    }


    /**
     * Test parsing of a request with newSuperior attribute
     */
    public void testRequestWithNewSuperior()
    {
        Dsmlv2Parser parser = null;
        try
        {
            parser = new Dsmlv2Parser();

            parser.setInputFile( ModifyDNRequestTest.class.getResource( "request_with_newSuperior_attribute.xml" )
                .getFile() );

            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNRequest modifyDNRequest = ( ModifyDNRequest ) parser.getBatchRequest().getCurrentRequest();

        assertEquals( "cn=Steve Jobs,ou=Dev,dc=apple,dc=com", modifyDNRequest.getNewSuperior().toString() );
    }

}
