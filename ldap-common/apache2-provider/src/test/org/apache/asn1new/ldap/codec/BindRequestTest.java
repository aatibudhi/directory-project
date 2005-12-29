/*
 *   Copyright 2005 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.asn1new.ldap.codec;

import java.nio.ByteBuffer;

import org.apache.asn1.codec.DecoderException;
import org.apache.asn1.codec.EncoderException;
import org.apache.asn1new.ber.Asn1Decoder;
import org.apache.asn1new.ber.containers.IAsn1Container;
import org.apache.asn1new.ldap.pojo.BindRequest;
import org.apache.asn1new.ldap.pojo.LdapMessage;
import org.apache.asn1new.ldap.pojo.SaslCredentials;
import org.apache.asn1new.ldap.pojo.SimpleAuthentication;
import org.apache.ldap.common.util.StringTools;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class BindRequestTest extends TestCase {
    /**
     * Test the decoding of a BindRequest with Simple authentication
     * and no controls
     */
    public void testDecodeBindRequestSimpleNoControls()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer  stream      = ByteBuffer.allocate( 0x35 );
        stream.put(
            new byte[]
            {
                0x30, 0x33, 		// LDAPMessage ::=SEQUENCE {
				0x02, 0x01, 0x01, 	//         messageID MessageID
				0x60, 0x2E, 		//        CHOICE { ..., bindRequest BindRequest, ...
                        			// BindRequest ::= APPLICATION[0] SEQUENCE {
				0x02, 0x01, 0x03, 	//        version INTEGER (1..127),
				0x04, 0x1F, 		//        name LDAPDN,
				'u', 'i', 'd', '=', 'a', 'k', 'a', 'r', 'a', 's', 'u', 'l', 'u', ',', 'd', 'c', '=',
                'e', 'x', 'a', 'm', 'p', 'l', 'e', ',', 'd', 'c', '=', 'c', 'o', 'm',
				( byte ) 0x80, 0x08, //        authentication AuthenticationChoice
                                     // AuthenticationChoice ::= CHOICE { simple [0] OCTET STRING, ...
				'p', 'a', 's', 's', 'w', 'o', 'r', 'd'
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindRequest PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
    	
        // Check the decoded BindRequest 
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindRequest br      = message.getBindRequest();

        Assert.assertEquals( 1, message.getMessageId() );
        Assert.assertEquals( 3, br.getVersion() );
        Assert.assertEquals( "uid=akarasulu,dc=example,dc=com", br.getName() );
        Assert.assertEquals( true, ( br.getAuthentication() instanceof SimpleAuthentication ) );
        Assert.assertEquals( "password", StringTools.utf8ToString( ( ( SimpleAuthentication ) br.getAuthentication() ).getSimple() ) );

        // Check the length
        Assert.assertEquals(0x35, message.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb.array() ); 
            
            Assert.assertEquals(encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            Assert.fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a BindRequest with Simple authentication,
     * no name
     * and no controls
     */
    public void testDecodeBindRequestSimpleNoName()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer  stream      = ByteBuffer.allocate( 0x15 );
        stream.put(
            new byte[]
            {
                0x30, 0x13, 		// LDAPMessage ::=SEQUENCE {
				0x02, 0x01, 0x01, 	//         messageID MessageID
				0x60, 0x0D, 		//        CHOICE { ..., bindRequest BindRequest, ...
                        			// BindRequest ::= APPLICATION[0] SEQUENCE {
				0x02, 0x01, 0x03, 	//        version INTEGER (1..127),
				( byte ) 0x80, 0x08, //        authentication AuthenticationChoice
                                     // AuthenticationChoice ::= CHOICE { simple [0] OCTET STRING, ...
				'p', 'a', 's', 's', 'w', 'o', 'r', 'd'
            } );

        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer =  new LdapMessageContainer();

        // Decode the BindRequest PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            Assert.assertEquals( "Bad transition !", de.getMessage() );
            return;
        }
    	
        Assert.fail("Should never reach this point.");
    }

    /**
     * Test the decoding of a BindRequest with Simple authentication,
     * empty name (an anonymous bind)
     * and no controls
     */
    public void testDecodeBindRequestSimpleEmptyName()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer  stream      = ByteBuffer.allocate( 0x16 );
        stream.put(
            new byte[]
            {
                0x30, 0x14, 		 // LDAPMessage ::=SEQUENCE {
				0x02, 0x01, 0x01, 	 //         messageID MessageID
				0x60, 0x0F, 		 //        CHOICE { ..., bindRequest BindRequest, ...
                        			 // BindRequest ::= APPLICATION[0] SEQUENCE {
				0x02, 0x01, 0x03, 	 //        version INTEGER (1..127),
				0x04, 0x00,          //        name LDAPDN,
				( byte ) 0x80, 0x08, //        authentication AuthenticationChoice
                                     // AuthenticationChoice ::= CHOICE { simple [0] OCTET STRING, ...
				'p', 'a', 's', 's', 'w', 'o', 'r', 'd'
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );

        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer =  new LdapMessageContainer();

        // Decode the BindRequest PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }

        // Check the decoded BindRequest 
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindRequest br      = message.getBindRequest();

        Assert.assertEquals( 1, message.getMessageId() );
        Assert.assertEquals( 3, br.getVersion() );
        Assert.assertEquals( "", br.getName() );
        Assert.assertEquals( true, ( br.getAuthentication() instanceof SimpleAuthentication ) );
        Assert.assertEquals( "password", StringTools.utf8ToString( ( (SimpleAuthentication)br.getAuthentication() ).getSimple() ) );

        // Check the length
        Assert.assertEquals(0x16, message.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb.array() ); 
            
            Assert.assertEquals(encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            Assert.fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a BindRequest with Sasl authentication,
     * no credentials and no controls
     */
    public void testDecodeBindRequestSaslNoCredsNoControls()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer  stream      = ByteBuffer.allocate( 0x38 );
        stream.put(
            new byte[]
            {
                0x30, 0x36, 		// LDAPMessage ::=SEQUENCE {
				0x02, 0x01, 0x01, 	//         messageID MessageID
				0x60, 0x31, 		//        CHOICE { ..., bindRequest BindRequest, ...
                        			// BindRequest ::= APPLICATION[0] SEQUENCE {
				0x02, 0x01, 0x03, 	//        version INTEGER (1..127),
				0x04, 0x1F, 		//        name LDAPDN,
				'u', 'i', 'd', '=', 'a', 'k', 'a', 'r', 'a', 's', 'u', 'l', 'u', ',', 'd', 'c', '=',
                'e', 'x', 'a', 'm', 'p', 'l', 'e', ',', 'd', 'c', '=', 'c', 'o', 'm',
				( byte ) 0x83, 0x0B, //        authentication AuthenticationChoice
                                     // AuthenticationChoice ::= CHOICE { ... sasl [3] SaslCredentials }
				 					 // SaslCredentials ::= SEQUENCE {
									 //      mechanism   LDAPSTRING,
				 					 //      ...
				'K', 'E', 'R', 'B', 'E', 'R', 'O', 'S', '_', 'V', '4'
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindRequest PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
    	
        // Check the decoded BindRequest 
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindRequest br      = message.getBindRequest();

        Assert.assertEquals( 1, message.getMessageId() );
        Assert.assertEquals( 3, br.getVersion() );
        Assert.assertEquals( "uid=akarasulu,dc=example,dc=com", br.getName() );
        Assert.assertEquals( true, ( br.getAuthentication() instanceof SaslCredentials ) );
        Assert.assertEquals( "KERBEROS_V4", ( ( SaslCredentials ) br.getAuthentication() ).getMechanism() );

        // Check the length
        Assert.assertEquals(0x38, message.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb.array() ); 
            
            Assert.assertEquals(encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            Assert.fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a BindRequest with Sasl authentication,
     * a credentials and no controls
     */
    public void testDecodeBindRequestSaslCredsNoControls()
    {
        Asn1Decoder ldapDecoder = new LdapDecoder();

        ByteBuffer  stream      = ByteBuffer.allocate( 0x40 );
        stream.put(
            new byte[]
            {
                0x30, 0x3E, 		// LDAPMessage ::=SEQUENCE {
				0x02, 0x01, 0x01, 	//         messageID MessageID
				0x60, 0x39, 		//        CHOICE { ..., bindRequest BindRequest, ...
                        			// BindRequest ::= APPLICATION[0] SEQUENCE {
				0x02, 0x01, 0x03, 	//        version INTEGER (1..127),
				0x04, 0x1F, 		//        name LDAPDN,
				'u', 'i', 'd', '=', 'a', 'k', 'a', 'r', 'a', 's', 'u', 'l', 'u', ',', 'd', 'c', '=',
                'e', 'x', 'a', 'm', 'p', 'l', 'e', ',', 'd', 'c', '=', 'c', 'o', 'm',
				( byte ) 0x83, 0x0B, //        authentication AuthenticationChoice }
                                     // AuthenticationChoice ::= CHOICE { ... sasl [3] SaslCredentials }
									 // SaslCredentials ::= SEQUENCE {
									 //      mechanism   LDAPSTRING,
									 //      ...
				'K', 'E', 'R', 'B', 'E', 'R', 'O', 'S', '_', 'V', '4',
				( byte ) 0x04, 0x06, // SaslCredentials ::= SEQUENCE {        
				 					 //      ...
				 					 //      credentials   OCTET STRING OPTIONAL }
                					 // 
				'a', 'b', 'c', 'd', 'e', 'f'
            } );

        String decodedPdu = StringTools.dumpBytes( stream.array() );
        stream.flip();

        // Allocate a LdapMessage Container
        IAsn1Container ldapMessageContainer = new LdapMessageContainer();

        // Decode the BindRequest PDU
        try
        {
            ldapDecoder.decode( stream, ldapMessageContainer );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
    	
        // Check the decoded BindRequest 
        LdapMessage message = ( ( LdapMessageContainer ) ldapMessageContainer ).getLdapMessage();
        BindRequest br      = message.getBindRequest();

        Assert.assertEquals( 1, message.getMessageId() );
        Assert.assertEquals( 3, br.getVersion() );
        Assert.assertEquals( "uid=akarasulu,dc=example,dc=com", br.getName() );
        Assert.assertEquals( true, ( br.getAuthentication() instanceof SaslCredentials ) );
        Assert.assertEquals( "KERBEROS_V4", ( ( SaslCredentials ) br.getAuthentication() ).getMechanism() );
        Assert.assertEquals( "abcdef", StringTools.utf8ToString( ( ( SaslCredentials ) br.getAuthentication() ).getCredentials() ) );

        // Check the length
        Assert.assertEquals(0x40, message.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb = message.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb.array() ); 
            
            Assert.assertEquals(encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            Assert.fail( ee.getMessage() );
        }
    }
}
