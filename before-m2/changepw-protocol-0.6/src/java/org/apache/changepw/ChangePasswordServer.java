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

package org.apache.changepw;

import java.io.IOException;
import java.util.Dictionary;

import org.apache.changepw.protocol.ChangePasswordProtocolProvider;
import org.apache.kerberos.store.PrincipalStore;
import org.apache.mina.common.TransportType;
import org.apache.mina.protocol.ProtocolProvider;
import org.apache.mina.registry.Service;
import org.apache.mina.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper encapsulating configuration, a MINA registry, and a PrincipalStore
 * to implement a complete Change Password server. 
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ChangePasswordServer
{
    /** the log for this class */
    private static final Logger log = LoggerFactory.getLogger( ChangePasswordServer.class );

    private ChangePasswordConfiguration config;
    private ServiceRegistry registry;
    private PrincipalStore store;

    private ProtocolProvider provider;
    private Service tcpService;
    private Service udpService;

    public ChangePasswordServer( ChangePasswordConfiguration config, ServiceRegistry registry, PrincipalStore store )
    {
        this.config = config;
        this.registry = registry;
        this.store = store;

        String name = config.getName();
        int port = config.getPort();

        try
        {
            provider = new ChangePasswordProtocolProvider( config, this.store );

            udpService = new Service( name, TransportType.DATAGRAM, port );
            tcpService = new Service( name, TransportType.SOCKET, port );

            registry.bind( udpService, provider );
            registry.bind( tcpService, provider );

            log.debug( name + " listening on port " + port );
        }
        catch ( IOException ioe )
        {
            log.error( ioe.getMessage(), ioe );
        }
    }

    public boolean isDifferent( Dictionary newConfig )
    {
        return config.isDifferent( newConfig );
    }

    public void destroy()
    {
        registry.unbind( udpService );
        registry.unbind( tcpService );

        registry = null;
        provider = null;
        udpService = null;
        tcpService = null;

        log.debug( config.getName() + " has stopped listening on port " + config.getPort() );
    }
}
