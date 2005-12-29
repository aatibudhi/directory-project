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
package org.apache.ldap.common.berlib.asn1.decoder.add ;


import org.apache.asn1.ber.TypeClass;
import org.apache.asn1.ber.digester.AbstractRule;
import org.apache.ldap.common.berlib.asn1.LdapTag;
import org.apache.ldap.common.message.AddRequestImpl;


/**
 * A rule that creates an AddRequest and populates it.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory
 * Project</a>
 * @version $Rev$
 */
public class AddRequestRule extends AbstractRule
{
    /* (non-Javadoc)
     * @see org.apache.asn1.ber.digester.Rule#tag(int, boolean,
     * org.apache.asn1.ber.TypeClass)
     */
    public void tag( int id, boolean isPrimitive, TypeClass typeClass )
    {
        super.tag( id, isPrimitive, typeClass ) ;

        LdapTag tag = LdapTag.getLdapTagById( id ) ;

        if ( LdapTag.ADD_REQUEST != tag )
        {
            throw new IllegalArgumentException( "Expected a ADD_REQUEST tag "
                + "id but got a " + tag ) ;
        }

        AddRequestImpl req = new AddRequestImpl( getDigester().popInt() ) ;
        getDigester().push( req ) ;
    }


    /* (non-Javadoc)
     * @see org.apache.asn1.ber.digester.Rule#finish()
     */
    public void finish()
    {
        super.finish() ;
        getDigester().pop() ;
    }
}
