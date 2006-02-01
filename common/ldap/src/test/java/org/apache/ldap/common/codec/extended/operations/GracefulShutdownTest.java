/*
 *   Copyright 2006 The Apache Software Foundation
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
package org.apache.ldap.common.codec.extended.operations;

import java.nio.ByteBuffer;

import javax.naming.NamingException;

import org.apache.asn1.codec.DecoderException;
import org.apache.asn1.codec.EncoderException;
import org.apache.asn1.ber.Asn1Decoder;
import org.apache.ldap.common.codec.LdapDecoder;
import org.apache.ldap.common.util.StringTools;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test the GracefulShutdownTest codec
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class GracefulShutdownTest extends TestCase {
    /**
     * Test the decoding of a GracefulShutdown
     */
    public void testDecodeGracefulShutdownSuccess() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x08 );
        bb.put( new byte[]
            {
                0x30, 0x06, 		        // GracefulShutdown ::= SEQUENCE {
				  0x02, 0x01, 0x01,         //     timeOffline INTEGER (0..720) DEFAULT 0,
				  (byte)0x80, 0x01, 0x01	//     delay INTEGER (0..86400) DEFAULT 0
                                            // }
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 1, gracefulShutdown.getTimeOffline() );
        assertEquals( 1, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x08, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a GracefulShutdown with a timeOffline only
     */
    public void testDecodeGracefulShutdownTimeOffline() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x05 );
        bb.put( new byte[]
            {
                0x30, 0x03,                 // GracefulShutdown ::= SEQUENCE {
                  0x02, 0x01, 0x01          //     timeOffline INTEGER (0..720) DEFAULT 0,
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 1, gracefulShutdown.getTimeOffline() );
        assertEquals( 0, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x05, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a GracefulShutdown with a delay only
     */
    public void testDecodeGracefulShutdownDelay() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x05 );
        bb.put( new byte[]
            {
                0x30, 0x03,                 // GracefulShutdown ::= SEQUENCE {
                  (byte)0x80, 0x01, 0x01          //     delay INTEGER (0..86400) DEFAULT 0
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 0, gracefulShutdown.getTimeOffline() );
        assertEquals( 1, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x05, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a empty GracefulShutdown
     */
    public void testDecodeGracefulShutdownEmpty() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x02 );
        bb.put( new byte[]
            {
                0x30, 0x00                 // GracefulShutdown ::= SEQUENCE {
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 0, gracefulShutdown.getTimeOffline() );
        assertEquals( 0, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x02, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }
    
    /**
     * Test the decoding of a GracefulShutdown with a delay above 128
     */
    public void testDecodeGracefulShutdownDelayHigh() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x06 );
        bb.put( new byte[]
            {
                0x30, 0x04,                     // GracefulShutdown ::= SEQUENCE {
                  (byte)0x80, 0x02, 0x01, (byte)0xF4 //     delay INTEGER (0..86400) DEFAULT 0
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 0, gracefulShutdown.getTimeOffline() );
        assertEquals( 500, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x06, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a GracefulShutdown with a delay equals 32767
     */
    public void testDecodeGracefulShutdownDelay32767() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x06 );
        bb.put( new byte[]
            {
                0x30, 0x04,                     // GracefulShutdown ::= SEQUENCE {
                  (byte)0x80, 0x02, 0x7F, (byte)0xFF //     delay INTEGER (0..86400) DEFAULT 0
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 0, gracefulShutdown.getTimeOffline() );
        assertEquals( 32767, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x06, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }

    /**
     * Test the decoding of a GracefulShutdown with a delay above 32768
     */
    public void testDecodeGracefulShutdownDelay32768() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x07 );
        bb.put( new byte[]
            {
                0x30, 0x05,                     // GracefulShutdown ::= SEQUENCE {
                  (byte)0x80, 0x03, 0x00, (byte)0x80, (byte)0x00 //     delay INTEGER (0..86400) DEFAULT 0
            } );

        String decodedPdu = StringTools.dumpBytes( bb.array() );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            de.printStackTrace();
            Assert.fail( de.getMessage() );
        }
        
        GracefulShutdown gracefulShutdown = container.getGracefulShutdown();
        assertEquals( 0, gracefulShutdown.getTimeOffline() );
        assertEquals( 32768, gracefulShutdown.getDelay() );

        // Check the length
        assertEquals( 0x07, gracefulShutdown.computeLength());
        
        // Check the encoding
        try
        {
            ByteBuffer bb1 = gracefulShutdown.encode( null );
            
            String encodedPdu = StringTools.dumpBytes( bb1.array() ); 
            
            assertEquals( encodedPdu, decodedPdu );
        }
        catch ( EncoderException ee )
        {
            ee.printStackTrace();
            fail( ee.getMessage() );
        }
    }

    // Defensive tests

    /**
     * Test the decoding of a GracefulShutdown with a timeOffline off limit
     */
    public void testDecodeGracefulShutdownTimeOfflineOffLimit() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x06 );
        bb.put( new byte[]
            {
                0x30, 0x04,                     // GracefulShutdown ::= SEQUENCE {
                  0x02, 0x02, 0x03, (byte)0xE8  //     timeOffline INTEGER (0..720) DEFAULT 0,
            } );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            assertTrue( true );
            return;
        }
        
        fail( "We should not reach this point" );
    }

    /**
     * Test the decoding of a GracefulShutdown with a delay off limit
     */
    public void testDecodeGracefulShutdownDelayOffLimit() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x0b );
        bb.put( new byte[]
            {
                0x30, 0x05,                     // GracefulShutdown ::= SEQUENCE {
                  (byte)0x80, 0x03, 0x01, (byte)0x86, (byte)0xA0  //     delay INTEGER (0..86400) DEFAULT 0
            } );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            assertTrue( true );
            return;
        }
        
        fail( "We should not reach this point" );
    }

    /**
     * Test the decoding of a GracefulShutdown with an empty TimeOffline
     */
    public void testDecodeGracefulShutdownTimeOfflineEmpty() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x0b );
        bb.put( new byte[]
            {
                0x30, 0x02,   // GracefulShutdown ::= SEQUENCE {
                  0x02, 0x00  //     timeOffline INTEGER (0..720) DEFAULT 0,
            } );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            assertTrue( true );
            return;
        }
        
        fail( "We should not reach this point" );
    }

    /**
     * Test the decoding of a GracefulShutdown with an empty delay
     */
    public void testDecodeGracefulShutdownDelayEmpty() throws NamingException
    {
        Asn1Decoder decoder = new LdapDecoder();
        ByteBuffer bb = ByteBuffer.allocate( 0x0b );
        bb.put( new byte[]
            {
                0x30, 0x02,         // GracefulShutdown ::= SEQUENCE {
                  (byte)0x80, 0x00  //     delay INTEGER (0..86400) DEFAULT 0
            } );
        bb.flip();

        GracefulShutdownContainer container = new GracefulShutdownContainer();
        
        try
        {
            decoder.decode( bb, container );
        }
        catch ( DecoderException de )
        {
            assertTrue( true );
            return;
        }
        
        fail( "We should not reach this point" );
    }
}
