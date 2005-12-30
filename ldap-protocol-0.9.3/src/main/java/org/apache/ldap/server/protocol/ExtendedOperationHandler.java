/*
 *   @(#) $Id$
 *   
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

import org.apache.ldap.common.message.ExtendedRequest;
import org.apache.ldap.common.message.ExtendedResponse;
import org.apache.mina.protocol.ProtocolSession;

/**
 * An extension (hook) point that enables an implementor to provide his or her
 * own LDAP 'Extended' operation.  
 *
 * @author The Apache Directory Project
 * @version $Rev$, $Date$
 *
 */
public interface ExtendedOperationHandler
{
    /**
     * Returns the OID of the extended operation this handler can handle.
     */
    String getOid();
    
    /**
     * Handles the specified extended operation and fills its response to
     * <tt>res</tt>.  Don't lock the response; messageID and resultCode might be
     * be filled by caller automatically.
     * 
     * @param session the MINA session object related with current connection
     * @param req the LDAP Extended operation request
     * @param res the LDAP Extended operation response you have to fill its payload
     * 
     * @throws Exception if failed to handle the operation
     */
    void handleExtendedOperation( ProtocolSession session, SessionRegistry registry, ExtendedRequest req, ExtendedResponse res ) throws Exception;
}
