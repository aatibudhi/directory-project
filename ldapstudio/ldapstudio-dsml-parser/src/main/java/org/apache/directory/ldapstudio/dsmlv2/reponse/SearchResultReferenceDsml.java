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


import java.util.ArrayList;

import org.apache.directory.shared.ldap.codec.LdapMessage;
import org.apache.directory.shared.ldap.codec.search.SearchResultReference;
import org.dom4j.Element;


/**
 * DSML Decorator for SearchResultReference
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class SearchResultReferenceDsml extends LdapResponseDecorator implements DsmlDecorator
{
    /**
     * Creates a new instance of SearchResultReferenceDsml.
     *
     * @param ldapMessage
     *      the message to decorate
     */
    public SearchResultReferenceDsml( LdapMessage ldapMessage )
    {
        super( ldapMessage );
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.dsmlv2.reponse.LdapMessageDecorator#getMessageType()
     */
    public int getMessageType()
    {
        return instance.getSearchResultReference().getMessageType();
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.dsmlv2.reponse.DsmlDecorator#toDsml(org.dom4j.Element)
     */
    public Element toDsml( Element root )
    {
        Element element = root.addElement( "searchResultReference" );
        SearchResultReference searchResultReference = instance.getSearchResultReference();

        // Adding References
        ArrayList refsList = searchResultReference.getSearchResultReferences();
        for ( int i = 0; i < refsList.size(); i++ )
        {
            element.addElement( "ref" ).addText( refsList.get( i ).toString() );
        }

        return element;
    }
}
