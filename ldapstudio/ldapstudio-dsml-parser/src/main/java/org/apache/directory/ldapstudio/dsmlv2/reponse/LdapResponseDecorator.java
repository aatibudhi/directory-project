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

package org.apache.directory.ldapstudio.dsmlv2.reponse;


import org.apache.directory.shared.ldap.codec.LdapMessage;
import org.apache.directory.shared.ldap.codec.LdapResponse;
import org.apache.directory.shared.ldap.codec.LdapResult;


/**
 * Decorator abstract class for LdapResponse
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public abstract class LdapResponseDecorator extends LdapMessageDecorator
{
    /**
     * Creates a new instance of LdapResponseDecorator.
     *
     * @param ldapMessage
     *      the message to decorate
     */
    public LdapResponseDecorator( LdapMessage ldapMessage )
    {
        super( ldapMessage );
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.codec.LdapResponse#getLdapResponseLength()
     */
    public int getLdapResponseLength()
    {
        return ( ( LdapResponse ) instance ).getLdapResponseLength();
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.codec.LdapResponse#getLdapResult()
     */
    public LdapResult getLdapResult()
    {
        return ( ( LdapResponse ) instance ).getLdapResult();
    }


    /* (non-Javadoc)
     * @see org.apache.directory.shared.ldap.codec.LdapResponse#setLdapResult(org.apache.directory.shared.ldap.codec.LdapResult)
     */
    public void setLdapResult( LdapResult ldapResult )
    {
        ( ( LdapResponse ) instance ).setLdapResult( ldapResult );
    }
}
