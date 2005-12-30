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
package org.apache.ldap.server.protocol.support;


import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.spi.InitialContextFactory;

import org.apache.ldap.common.exception.LdapException;
import org.apache.ldap.common.message.BindRequest;
import org.apache.ldap.common.message.BindResponse;
import org.apache.ldap.common.message.BindResponseImpl;
import org.apache.ldap.common.message.Control;
import org.apache.ldap.common.message.LdapResult;
import org.apache.ldap.common.message.LdapResultImpl;
import org.apache.ldap.common.message.ResultCodeEnum;
import org.apache.ldap.common.util.ExceptionUtils;
import org.apache.ldap.common.util.StringTools;
import org.apache.ldap.server.protocol.SessionRegistry;
import org.apache.mina.common.IoSession;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single reply handler for {@link org.apache.ldap.common.message.BindRequest}s.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class BindHandler implements MessageHandler
{
    private static final Logger log = LoggerFactory.getLogger( BindHandler.class );
    private static final Control[] EMPTY = new Control[0];


    public void messageReceived( IoSession session, Object request )
    {
        LdapContext ctx;
        BindRequest req = ( BindRequest ) request;
        BindResponse resp = new BindResponseImpl( req.getMessageId() );
        LdapResult result = new LdapResultImpl( resp );
        resp.setLdapResult( result );

        Hashtable env = SessionRegistry.getSingleton().getEnvironment();
//        StartupConfiguration cfg = ( StartupConfiguration ) Configuration.toConfiguration( env );
        
        // if the bind request is not simple then we freak: no strong auth yet
        if ( ! req.isSimple() )
        {
            result.setResultCode( ResultCodeEnum.AUTHMETHODNOTSUPPORTED );
            result.setErrorMessage( "Only simple binds currently supported" );
            session.write( resp );
            return;
        }

//        boolean allowAnonymousBinds = cfg.isAllowAnonymousAccess();
//        boolean emptyCredentials = req.getCredentials() == null || req.getCredentials().length == 0;
        boolean emptyDn = StringTools.isEmpty( req.getName() );
//
//        if ( emptyCredentials && emptyDn && ! allowAnonymousBinds )
//        {
//            result.setResultCode( ResultCodeEnum.INSUFFICIENTACCESSRIGHTS );
//            String msg = "Bind failure: Anonymous binds have been disabled!";
//            result.setErrorMessage( msg );
//            session.write( resp );
//            return;
//        }

        // clone the environment first then add the required security settings
        String dn = ( emptyDn ? "" : req.getName() );

        byte[] creds = req.getCredentials();

        Hashtable cloned = ( Hashtable ) env.clone();
        cloned.put( Context.SECURITY_PRINCIPAL, dn );
        cloned.put( Context.SECURITY_CREDENTIALS, creds );
        cloned.put( Context.SECURITY_AUTHENTICATION, "simple" );

        Control[] connCtls = ( Control[] ) req.getControls().toArray( EMPTY );

        try
        {
            if ( cloned.containsKey( "server.use.factory.instance" ) )
            {
                InitialContextFactory factory = ( InitialContextFactory ) cloned.get( "server.use.factory.instance" );

                if ( factory == null )
                {
                    throw new NullPointerException( "server.use.factory.instance was set in env but was null" );
                }

                ctx = ( LdapContext ) factory.getInitialContext( cloned );
            }
            else
            {
                ctx = new InitialLdapContext( cloned, connCtls );
            }
        }
        catch( NamingException e )
        {
            ResultCodeEnum code;
            
            if ( e instanceof LdapException )
            {
                code = ( ( LdapException ) e ).getResultCode();
                result.setResultCode( code );
            }
            else
            {
                code = ResultCodeEnum.getBestEstimate( e, req.getType() );
                result.setResultCode( code );
            }

            String msg = "Bind failed";

            if ( log.isDebugEnabled() )
            {
                msg += ":\n" + ExceptionUtils.getStackTrace( e );
                msg += "\n\nBindRequest = \n" + req.toString();
            }

            if ( ( e.getResolvedName() != null ) &&
                    ( ( code == ResultCodeEnum.NOSUCHOBJECT ) ||
                      ( code == ResultCodeEnum.ALIASPROBLEM ) ||
                      ( code == ResultCodeEnum.INVALIDDNSYNTAX ) ||
                      ( code == ResultCodeEnum.ALIASDEREFERENCINGPROBLEM ) ) )
            {
                resp.getLdapResult().setMatchedDn( e.getResolvedName().toString() );
            }
            
            result.setErrorMessage( msg );
            session.write( resp );
            return;
        }

        SessionRegistry.getSingleton().setLdapContext( session, ctx );
        result.setResultCode( ResultCodeEnum.SUCCESS );
        session.write( resp );
    }
}
