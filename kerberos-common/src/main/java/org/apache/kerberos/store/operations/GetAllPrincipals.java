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

package org.apache.kerberos.store.operations;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.kerberos.KerberosPrincipal;

import org.apache.kerberos.messages.value.SamType;
import org.apache.kerberos.store.KerberosAttribute;
import org.apache.kerberos.store.PrincipalStoreEntry;
import org.apache.kerberos.store.PrincipalStoreEntryModifier;
import org.apache.protocol.common.store.ContextOperation;

/**
 * Command for getting all principals in a JNDI context.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class GetAllPrincipals implements ContextOperation
{
    private static final long serialVersionUID = -1214321426487445132L;

    public Object execute( DirContext ctx, Name searchBaseDn )
    {
        SearchControls controls = new SearchControls();

        String filter = "(objectClass=krb5Principal)";

        List answers = new ArrayList();

        try
        {
            Attributes attrs = null;

            NamingEnumeration answer = ctx.search( searchBaseDn, filter, controls );

            while ( answer.hasMore() )
            {
                SearchResult result = (SearchResult) answer.next();
                attrs = result.getAttributes();
                PrincipalStoreEntry entry = getEntry( attrs );
                System.out.println( "Result name is " + result.getName() );
                answers.add( entry );
            }

            answer.close();

            PrincipalStoreEntry[] entries = new PrincipalStoreEntry[ answers.size() ];

            return (PrincipalStoreEntry[]) answers.toArray( entries );
        }
        catch ( NamingException e )
        {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Marshals an a PrincipalStoreEntry from an Attributes object.
     *
     * @param attrs the attributes of the Kerberos principal
     * @return the entry for the principal
     * @throws NamingException if there are any access problems
     */
    private PrincipalStoreEntry getEntry( Attributes attrs ) throws NamingException
    {
        PrincipalStoreEntryModifier modifier = new PrincipalStoreEntryModifier();

        String principal = (String) attrs.get( KerberosAttribute.PRINCIPAL ).get();
        String encryptionType = (String) attrs.get( KerberosAttribute.TYPE ).get();
        String keyVersionNumber = (String) attrs.get( KerberosAttribute.VERSION ).get();

        String commonName = (String) attrs.get( "cn" ).get();

        if ( attrs.get( "apacheSamType" ) != null )
        {
            String samType = (String) attrs.get( "apacheSamType" ).get();

            modifier.setSamType( SamType.getTypeByOrdinal( Integer.parseInt( samType ) ) );
        }

        byte[] keyBytes = (byte[]) attrs.get( KerberosAttribute.KEY ).get();

        modifier.setCommonName( commonName );
        modifier.setPrincipal( new KerberosPrincipal( principal ) );
        modifier.setEncryptionType( Integer.parseInt( encryptionType ) );
        modifier.setKeyVersionNumber( Integer.parseInt( keyVersionNumber ) );
        modifier.setKey( keyBytes );

        return modifier.getEntry();
    }
}
