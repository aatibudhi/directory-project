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
package org.apache.ldap.server.protocol;


import java.util.Properties;

import junit.framework.TestCase;

import org.apache.ldap.common.NotImplementedException;
import org.apache.ldap.common.exception.LdapNamingException;
import org.apache.ldap.common.message.AbandonRequest;
import org.apache.ldap.common.message.AbandonRequestImpl;
import org.apache.ldap.common.message.AddRequest;
import org.apache.ldap.common.message.AddRequestImpl;
import org.apache.ldap.common.message.BindRequest;
import org.apache.ldap.common.message.BindRequestImpl;
import org.apache.ldap.common.message.CompareRequest;
import org.apache.ldap.common.message.CompareRequestImpl;
import org.apache.ldap.common.message.DeleteRequest;
import org.apache.ldap.common.message.DeleteRequestImpl;
import org.apache.ldap.common.message.ExtendedRequest;
import org.apache.ldap.common.message.ExtendedRequestImpl;
import org.apache.ldap.common.message.ModifyDnRequest;
import org.apache.ldap.common.message.ModifyDnRequestImpl;
import org.apache.ldap.common.message.ModifyRequest;
import org.apache.ldap.common.message.ModifyRequestImpl;
import org.apache.ldap.common.message.SearchRequest;
import org.apache.ldap.common.message.SearchRequestImpl;
import org.apache.ldap.common.message.UnbindRequest;
import org.apache.ldap.common.message.UnbindRequestImpl;
import org.apache.mina.protocol.ProtocolSession;
import org.apache.mina.protocol.handler.MessageHandler;


/**
 * Tests the LdapProtocolProvider.
 * FIXME: This test case doesn't test enough now.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class LdapProtocolProviderTest extends TestCase
{
    /**
     * Tests to make sure all the default handlers are kicking in properly with
     * the right request type.
     *
     * @throws LdapNamingException if there are problems initializing the
     * provider
     */
    public void testDefaultOperation() throws LdapNamingException
    {
        LdapProtocolProvider provider = new LdapProtocolProvider();
        assertNotNull( provider.getCodecFactory() );
        assertTrue( provider.getName() == LdapProtocolProvider.SERVICE_NAME );
    }

    /**
     * Tests to make sure handlers for alternative configurations are kicking
     * in properly with the right request type.
     *
     * @throws LdapNamingException if there are problems initializing the
     * provider
     */
    public void testAlternativeConfiguration() throws LdapNamingException
    {
        Properties props = new Properties();

        props.setProperty( AbandonRequest.class.getName(), BogusAbandonHandler.class.getName() );
        props.setProperty( AbandonRequestImpl.class.getName(), BogusAbandonHandler.class.getName() );

        props.setProperty( AddRequest.class.getName(), BogusAddHandler.class.getName() );
        props.setProperty( AddRequestImpl.class.getName(), BogusAddHandler.class.getName() );

        props.setProperty( BindRequest.class.getName(), BogusBindHandler.class.getName() );
        props.setProperty( BindRequestImpl.class.getName(), BogusBindHandler.class.getName() );

        props.setProperty( CompareRequest.class.getName(), BogusCompareHandler.class.getName() );
        props.setProperty( CompareRequestImpl.class.getName(), BogusCompareHandler.class.getName() );

        props.setProperty( DeleteRequest.class.getName(), BogusDeleteHandler.class.getName() );
        props.setProperty( DeleteRequestImpl.class.getName(), BogusDeleteHandler.class.getName() );

        props.setProperty( ExtendedRequest.class.getName(), BogusExtendedHandler.class.getName() );
        props.setProperty( ExtendedRequestImpl.class.getName(), BogusExtendedHandler.class.getName() );

        props.setProperty( ModifyRequest.class.getName(), BogusModifyHandler.class.getName() );
        props.setProperty( ModifyRequestImpl.class.getName(), BogusModifyHandler.class.getName() );

        props.setProperty( ModifyDnRequest.class.getName(), BogusModifyDnHandler.class.getName() );
        props.setProperty( ModifyDnRequestImpl.class.getName(), BogusModifyDnHandler.class.getName() );

        props.setProperty( SearchRequest.class.getName(), BogusSearchHandler.class.getName() );
        props.setProperty( SearchRequestImpl.class.getName(), BogusSearchHandler.class.getName() );

        props.setProperty( UnbindRequest.class.getName(), BogusUnbindHandler.class.getName() );
        props.setProperty( UnbindRequestImpl.class.getName(), BogusUnbindHandler.class.getName() );

        LdapProtocolProvider provider = new LdapProtocolProvider( props );
        assertNotNull( provider.getCodecFactory() );
        assertTrue( provider.getName() == LdapProtocolProvider.SERVICE_NAME );
    }


    public static class BogusAbandonHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }

    public static class BogusUnbindHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }

    public static class BogusAddHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }

    public static class BogusBindHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }

    public static class BogusCompareHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }

    public static class BogusDeleteHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }


    public static class BogusExtendedHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }


    public static class BogusModifyDnHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }


    public static class BogusModifyHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }


    public static class BogusSearchHandler implements MessageHandler
    {
        public void messageReceived( ProtocolSession session, Object request )
        {
            throw new NotImplementedException( "handler not implemented!" );
        }
    }
}
