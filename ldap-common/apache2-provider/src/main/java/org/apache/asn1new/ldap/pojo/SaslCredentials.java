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
package org.apache.asn1new.ldap.pojo;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import org.apache.asn1.codec.EncoderException;
import org.apache.asn1new.ber.tlv.Length;
import org.apache.asn1new.ber.tlv.Value;
import org.apache.asn1new.ldap.codec.LdapConstants;
import org.apache.asn1new.ldap.codec.primitives.LdapString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A ldapObject which stores the SASL authentication of a BindRequest.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class SaslCredentials extends LdapAuthentication
{
	/** The logger */
    private static Logger log = LoggerFactory.getLogger( SimpleAuthentication.class );

    //~ Instance fields ----------------------------------------------------------------------------

    /** Any mechanism defined in RFC 2222 :
     * KERBEROS_V4,
     * GSSAPI,
     * SKEY,
     * EXTERNAL
     **/
    private LdapString mechanism;

    /** optional credentials of the user */
    private byte[] credentials;

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Get the credentials
     *
     * @return The credentials
     */
    public byte[] getCredentials()
    {
        return credentials;
    }

    /**
     * Set the credentials
     *
     * @param credentials The credentials
     */
    public void setCredentials( byte[] credentials )
    {
        this.credentials = credentials;
    }

    /**
     * Get the mechanism
     *
     * @return The mechanism
     */
    public String getMechanism()
    {

        return ( ( mechanism == null ) ? null : mechanism.getString() );
    }

    /**
     * Set the mechanism
     *
     * @param mechanism The mechanism
     */
    public void setMechanism( LdapString mechanism )
    {
        this.mechanism = mechanism;
    }

    /**
     * Compute the Sasl authentication length
     * 
     * Sasl authentication :
     * 
     * 0x83 L1 mechanism
     * [0x04 L2 credentials]
     * 
     * L1 = Length(mechanism)
     * L2 = Length(credentials)
     * 
     * Length(Sasl authentication) = Length(0x83) + Length(L1) + Length(mechanism)
     *                               [+ Length(0x04) + Length(L2) + Length(credentials)]
     */
    public int computeLength()
    {
        int saslLength = 1 + Length.getNbBytes( mechanism.getNbBytes() ) + mechanism.getNbBytes();
        
        if (credentials != null)
        {
            saslLength += 1 + Length.getNbBytes( credentials.length ) + credentials.length;
        }
        
    	if ( log.isDebugEnabled() )
    	{
    		log.debug( "SASL Authentication length : " + saslLength );
    	}

    	return saslLength;
    }
    
    /**
     * Encode the sasl authentication to a PDU.
     * 
     * SimpleAuthentication :
     * 
     * 0x83 LL mechanism
     * [0x04 LL credentials]
     * 
     * @param buffer The buffer where to put the PDU
     * @return The PDU.
     */
    public ByteBuffer encode( ByteBuffer buffer ) throws EncoderException
    {
        if ( buffer == null )
        {
        	log.error( "Cannot put a PDU in a null buffer !" );
            throw new EncoderException( "Cannot put a PDU in a null buffer !" );
        }

        try 
        {
            // The saslAuthentication Tag
            buffer.put( (byte)LdapConstants.BIND_REQUEST_SASL_TAG );
            buffer.put( Length.getBytes( mechanism.getNbBytes() ) ) ;
            buffer.put( mechanism.getBytes() ) ;
            
            if ( credentials != null )
            {
                Value.encode( buffer, credentials );
            }
        }
        catch ( BufferOverflowException boe )
        {
        	log.error( "The PDU buffer size is too small !" );
            throw new EncoderException("The PDU buffer size is too small !"); 
        }

        return buffer;
    }

    /**
     * Get a String representation of a SaslCredential
     *
     * @return A SaslCredential String 
     */
    public String toString()
    {

        StringBuffer sb = new StringBuffer();

        sb.append( "        Sasl credentials\n" );
        sb.append( "            Mechanism :'" ).append( mechanism.toString() ).append( "'\n" );

        if ( credentials != null )
        {
            sb.append( "            Credentials :'" ).append( credentials.toString() ).append(
                "'\n" );
        }

        return sb.toString();
    }
}
