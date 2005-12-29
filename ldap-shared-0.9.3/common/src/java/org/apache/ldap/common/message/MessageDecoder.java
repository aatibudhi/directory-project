/*
 *   Copyright 2004 The Apache Software Foundation
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
package org.apache.ldap.common.message;


import java.io.InputStream;
import java.util.Hashtable;

import org.apache.ldap.common.message.spi.Provider;
import org.apache.ldap.common.message.spi.TransformerSpi;
import org.apache.ldap.common.message.spi.ProviderDecoder;

import org.apache.asn1.codec.DecoderException;
import org.apache.asn1.codec.stateful.DecoderMonitor;
import org.apache.asn1.codec.stateful.DecoderCallback;
import org.apache.asn1.codec.stateful.StatefulDecoder;


/**
 * Decodes a BER encoded LDAPv3 message envelope from an input stream
 * demarshaling it into a Message instance using a BER library provider.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public final class MessageDecoder implements ProviderDecoder
{
    /** Environment parameters stored here */
    private final Hashtable env;
    /** the ASN.1 provider */
    private final Provider provider;
    /** the ASN.1 provider's decoder */
    private final ProviderDecoder decoder;
    /** the ASN.1 provider's transformer */
    private final TransformerSpi transformer;
    /** the Message decode operation callback */
    private DecoderCallback cb;


    /**
     * Creates a MessageDecoder using default properties for enabling a BER
     * library provider.
     * 
     * @throws MessageException if there is a problem creating this decoder.
     */
    public MessageDecoder() throws MessageException
    {
        this( Provider.getEnvironment() );
    }


    /**
     * Creates a MessageDecoder using default properties for enabling a BER
     * library provider.
     *
     * @param env The Map of environment parameters.
     * @throws MessageException if there is a problem creating this decoder.
     */
    public MessageDecoder( final Hashtable env ) throws MessageException
    {
        this.env = ( Hashtable ) env.clone();
        this.provider = Provider.getProvider( this.env );
        this.decoder = this.provider.getDecoder();
        this.transformer = this.provider.getTransformer();
        this.decoder.setCallback( new DecoderCallback()
        {
            public void decodeOccurred( StatefulDecoder decoder, Object decoded )
            {
                cb.decodeOccurred( decoder, transformer.transform( decoded ) );
            }
        });
    }


    /**
     * Reads and decodes a BER encoded LDAPv3 ASN.1 message envelope structure
     * from an input stream to build a fully populated Message object instance.
     *
     * @param lock lock object used to exclusively read from the input stream
     * @param in the input stream to read PDU data from.
     * @return the populated Message representing the PDU envelope.
     * @throws MessageException if there is a problem decoding.
     */
    public Object decode( final Object lock, final InputStream in ) throws MessageException
    {
        Object providerEnvelope = null;

        if ( lock == null )
        {
            // Complain here somehow first then do the following w/o synch!

            // Call provider decoder to demarshall PDU into berlib specific form
            providerEnvelope = decoder.decode( lock, in );
        }
        else
        {
            synchronized ( lock )
            {
                // Same as above but a synchronized read using valid lock object
                providerEnvelope = decoder.decode( lock, in );
                lock.notifyAll();
            }
        }


        // Call on transformer to convert stub based PDU into Message based PDU
        Message message = transformer.transform( providerEnvelope );

        // Lock down the PDU's parameters to protect against alteration
        message.setLocked( true );

        return message;
    }


    /**
     * Decodes a chunk of stream data returning any resultant decoded PDU via
     * a callback.
     *
     * @param chunk the chunk to decode
     * @throws MessageException if there are failures while decoding the chunk
     */
    public void decode( Object chunk ) throws MessageException
    {
        try
        {
            decoder.decode( chunk );
        }
        catch ( DecoderException e )
        {
            throw new MessageException( "decoder failture: " + e.getMessage() );
        }
    }


    /**
     * Sets the callback used to deliver completly decoded PDU's.
     *
     * @param cb the callback to use for decoded PDU delivery
     */
    public void setCallback( DecoderCallback cb )
    {
        this.cb = cb;
    }


    /**
     * Sets the monitor for this MessageDecoder which receives callbacks for
     * noteworthy events during decoding.
     *
     * @param monitor the monitor to receive notifications via callback events
     */
    public void setDecoderMonitor( DecoderMonitor monitor )
    {
        decoder.setDecoderMonitor( monitor );
    }


    public Provider getProvider()
    {
        return this.provider;
    }
}
