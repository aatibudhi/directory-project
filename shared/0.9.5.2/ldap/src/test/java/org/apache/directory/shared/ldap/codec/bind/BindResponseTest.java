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
package org.apache.directory.shared.ldap.codec.bind;


import java.nio.ByteBuffer;
import java.util.List;

import javax.naming.NamingException;

import org.apache.directory.shared.asn1.ber.Asn1Decoder;
import org.apache.directory.shared.asn1.ber.IAsn1Container;
import org.apache.directory.shared.asn1.codec.DecoderException;
import org.apache.directory.shared.asn1.codec.EncoderException;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.LdapDecoder;
import org.apache.directory.shared.ldap.codec.LdapMessage;
import org.apache.directory.shared.ldap.codec.LdapMessageContainer;
import org.apache.directory.shared.ldap.codec.bind.BindResponse;
import org.apache.directory.shared.ldap.util.StringTools;

import junit.framework.TestCase;


/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class BindResponseTest extends TestCase
{
    /**
     * Test the decoding of a BindResponse
     */
    public void testDecodeBindResponseSuccess()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer stream = ByteBuffer.allocate( 0x0E );

        stream.put( new byte[]
            { 0x30, 0x0C, // LDAPMessage ::=SEQUENCE {
                0x02, 0x01, 0x01, // messageID MessageID
                0x61, 0x07, // CHOICE { ..., bindResponse BindResponse, ...
                // BindResponse ::= APPLICATION[1] SEQUENCE {
                // COMPONENTS OF LDAPResult,
                0x0A, 0x01, 0x00, // LDAPResult ::= SEQUENCE {
                // resultCode ENUMERATED {
                // success (0), ...
                // },
                0x04, 0x00, // matchedDN LDAPDN,
                0x04, 0x00 // errorMessage LDAPString,
            // referral [3] Referral OPTIONAL }
            // serverSaslCreds [7] OCTET STRING OPTIONAL }
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindResponse PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }
        catch ( NamingException ne )
        {
            ne.printStackTrace();
            fail( ne.getMessage() );
        }

        // Check the decoded BindResponse
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindResponse br = message.getBindResponse();

        assertEquals( 1, message.getMessageId() );
        assertEquals( 0, br.getLdapResult().getResultCode() );
        assertEquals( "", br.getLdapResult().getMatchedDN() );
        assertEquals( "", br.getLdapResult().getErrorMessage() );

        // Check the length
        assertEquals( 0x0E, message.computeLength() );

        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );

            String encodedPdu = StringTools.dumpBytes( bb.array() );

            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }


    /**
     * Test the decoding of a BindResponse with a control
     */
    public void testDecodeBindResponseWithControlSuccess()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer stream = ByteBuffer.allocate( 0x3C );

        stream.put( new byte[]
            { 0x30, 0x3A, // LDAPMessage ::=SEQUENCE {
                0x02, 0x01, 0x01, // messageID MessageID
                0x61, 0x07, // CHOICE { ..., bindResponse BindResponse, ...
                // BindResponse ::= APPLICATION[1] SEQUENCE {
                // COMPONENTS OF LDAPResult,
                0x0A, 0x01, 0x00, // LDAPResult ::= SEQUENCE {
                // resultCode ENUMERATED {
                // success (0), ...
                // },
                0x04, 0x00, // matchedDN LDAPDN,
                0x04, 0x00, // errorMessage LDAPString,
                // referral [3] Referral OPTIONAL }
                // serverSaslCreds [7] OCTET STRING OPTIONAL }
                ( byte ) 0xa0, 0x2C, // controls
                0x30, 0x2A, 0x04, 0x16, 0x31, 0x2e, 0x32, 0x2e, 0x38, 0x34, 0x30, 0x2e, 0x31, 0x31, 0x33, 0x35, 0x35,
                0x36, 0x2e, 0x31, 0x2e, 0x34, 0x2e, 0x33, 0x31, 0x39, // control
                                                                        // oid:
                                                                        // 1.2.840.113556.1.4.319
                0x01, 0x01, ( byte ) 0xff, // criticality: false
                0x04, 0x0D, 0x30, 0x0B, 0x0A, 0x01, 0x08, 0x04, 0x03, 'a', '=', 'b', 0x02, 0x01, 0x10 } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindResponse PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }
        catch ( NamingException ne )
        {
            ne.printStackTrace();
            fail( ne.getMessage() );
        }

        // Check the decoded BindResponse
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindResponse br = message.getBindResponse();

        assertEquals( 1, message.getMessageId() );
        assertEquals( 0, br.getLdapResult().getResultCode() );
        assertEquals( "", br.getLdapResult().getMatchedDN() );
        assertEquals( "", br.getLdapResult().getErrorMessage() );

        // Check the length
        assertEquals( 0x3C, message.computeLength() );

        // Check the Control
        List controls = message.getControls();

        assertEquals( 1, controls.size() );

        Control control = message.getControls( 0 );
        assertEquals( "1.2.840.113556.1.4.319", control.getControlType() );
        assertEquals( "0x30 0x0B 0x0A 0x01 0x08 0x04 0x03 0x61 0x3D 0x62 0x02 0x01 0x10 ", StringTools
            .dumpBytes( ( byte[] ) control.getControlValue() ) );

        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );

            String encodedPdu = StringTools.dumpBytes( bb.array() );

            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }


    /**
     * Test the decoding of a BindResponse with an empty credentials
     */
    public void testDecodeBindResponseServerSASLEmptyCredentials()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer stream = ByteBuffer.allocate( 0x10 );

        stream.put( new byte[]
            { 0x30, 0x0E, // LDAPMessage ::=SEQUENCE {
                0x02, 0x01, 0x01, // messageID MessageID
                0x61, 0x09, // CHOICE { ..., bindResponse BindResponse, ...
                // BindResponse ::= APPLICATION[1] SEQUENCE {
                // COMPONENTS OF LDAPResult,
                0x0A, 0x01, 0x00, // LDAPResult ::= SEQUENCE {
                // resultCode ENUMERATED {
                // success (0), ...
                // },
                0x04, 0x00, // matchedDN LDAPDN,
                0x04, 0x00, // errorMessage LDAPString,
                // referral [3] Referral OPTIONAL }
                ( byte ) 0x87, 0x00 // serverSaslCreds [7] OCTET STRING OPTIONAL
                                    // }
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindResponse PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }
        catch ( NamingException ne )
        {
            ne.printStackTrace();
            fail( ne.getMessage() );
        }

        // Check the decoded BindResponse
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindResponse br = message.getBindResponse();

        assertEquals( 1, message.getMessageId() );
        assertEquals( 0, br.getLdapResult().getResultCode() );
        assertEquals( "", br.getLdapResult().getMatchedDN() );
        assertEquals( "", br.getLdapResult().getErrorMessage() );
        assertEquals( "", StringTools.utf8ToString( br.getServerSaslCreds() ) );

        // Check the length
        assertEquals( 0x10, message.computeLength() );

        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );

            String encodedPdu = StringTools.dumpBytes( bb.array() );

            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }


    /**
     * Test the decoding of a BindResponse with an empty credentials with
     * controls
     */
    public void testDecodeBindResponseServerSASLEmptyCredentialsWithControls()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer stream = ByteBuffer.allocate( 0x2D );

        stream.put( new byte[]
            { 0x30, 0x2B, // LDAPMessage ::=SEQUENCE {
                0x02, 0x01, 0x01, // messageID MessageID
                0x61, 0x09, // CHOICE { ..., bindResponse BindResponse, ...
                // BindResponse ::= APPLICATION[1] SEQUENCE {
                // COMPONENTS OF LDAPResult,
                0x0A, 0x01, 0x00, // LDAPResult ::= SEQUENCE {
                // resultCode ENUMERATED {
                // success (0), ...
                // },
                0x04, 0x00, // matchedDN LDAPDN,
                0x04, 0x00, // errorMessage LDAPString,
                // referral [3] Referral OPTIONAL }
                ( byte ) 0x87, 0x00, // serverSaslCreds [7] OCTET STRING
                                        // OPTIONAL }
                ( byte ) 0xA0, 0x1B, // A control
                0x30, 0x19, 0x04, 0x17, 0x32, 0x2E, 0x31, 0x36, 0x2E, 0x38, 0x34, 0x30, 0x2E, 0x31, 0x2E, 0x31, 0x31,
                0x33, 0x37, 0x33, 0x30, 0x2E, 0x33, 0x2E, 0x34, 0x2E, 0x32 } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindResponse PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }
        catch ( NamingException ne )
        {
            ne.printStackTrace();
            fail( ne.getMessage() );
        }

        // Check the decoded BindResponse
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindResponse br = message.getBindResponse();

        assertEquals( 1, message.getMessageId() );
        assertEquals( 0, br.getLdapResult().getResultCode() );
        assertEquals( "", br.getLdapResult().getMatchedDN() );
        assertEquals( "", br.getLdapResult().getErrorMessage() );
        assertEquals( "", StringTools.utf8ToString( br.getServerSaslCreds() ) );

        // Check the Control
        List controls = message.getControls();

        assertEquals( 1, controls.size() );

        Control control = message.getControls( 0 );
        assertEquals( "2.16.840.1.113730.3.4.2", control.getControlType() );
        assertEquals( "", StringTools.dumpBytes( ( byte[] ) control.getControlValue() ) );

        // Check the length
        assertEquals( 0x2D, message.computeLength() );

        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );

            String encodedPdu = StringTools.dumpBytes( bb.array() );

            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }


    /**
     * Test the decoding of a BindResponse with a credentials
     */
    public void testDecodeBindResponseServerSASL()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer stream = ByteBuffer.allocate( 0x12 );

        stream.put( new byte[]
            { 0x30, 0x10, // LDAPMessage ::=SEQUENCE {
                0x02, 0x01, 0x01, // messageID MessageID
                0x61, 0x0B, // CHOICE { ..., bindResponse BindResponse, ...
                // BindResponse ::= APPLICATION[1] SEQUENCE {
                // COMPONENTS OF LDAPResult,
                0x0A, 0x01, 0x00, // LDAPResult ::= SEQUENCE {
                // resultCode ENUMERATED {
                // success (0), ...
                // },
                0x04, 0x00, // matchedDN LDAPDN,
                0x04, 0x00, // errorMessage LDAPString,
                // referral [3] Referral OPTIONAL }
                ( byte ) 0x87, 0x02, 'A', 'B' // serverSaslCreds [7] OCTET
                                                // STRING OPTIONAL }
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindResponse PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            fail( de.getMessage() );
        }
        catch ( NamingException ne )
        {
            ne.printStackTrace();
            fail( ne.getMessage() );
        }

        // Check the decoded BindResponse
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindResponse br = message.getBindResponse();

        assertEquals( 1, message.getMessageId() );
        assertEquals( 0, br.getLdapResult().getResultCode() );
        assertEquals( "", br.getLdapResult().getMatchedDN() );
        assertEquals( "", br.getLdapResult().getErrorMessage() );
        assertEquals( "AB", StringTools.utf8ToString( br.getServerSaslCreds() ) );

        // Check the length
        assertEquals( 0x12, message.computeLength() );

        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );

            String encodedPdu = StringTools.dumpBytes( bb.array() );

            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }


    /**
     * Test the decoding of a BindResponse with no LdapResult
     */
    public void testDecodeAddResponseEmptyResult()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer stream = ByteBuffer.allocate( 0x07 );

        stream.put( new byte[]
            { 0x30, 0x05, // LDAPMessage ::=SEQUENCE {
                0x02, 0x01, 0x01, // messageID MessageID
                0x61, 0x00, // CHOICE { ..., bindResponse BindResponse, ...
            } );

        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode a BindResponse message
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            assertTrue( true );
            return;
        }
        catch ( NamingException ne )
        {
            ne.printStackTrace();
            fail( ne.getMessage() );
        }

        fail( "We should not reach this point" );
    }
}
