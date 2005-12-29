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
package org.apache.ldap.common.exception;


import javax.naming.directory.NoSuchAttributeException;

import org.apache.ldap.common.message.ResultCodeEnum;


/**
 * A subclass of NoSuchAttributeException which holds the LDAP resultCode
 * associated with the exception.
 *
 * @see <a href="http://java.sun.com/j2se/1.4.2/docs/guide/jndi/jndi-ldap-gl.html#EXCEPT">
 * LDAP ResultCode to JNDI Exception Mappings</a>
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class LdapNoSuchAttributeException extends NoSuchAttributeException implements LdapException
{
    static final long serialVersionUID = 886120483680893537L;


    /**
     * @see javax.naming.directory.NoSuchAttributeException#NoSuchAttributeException()
     */
    public LdapNoSuchAttributeException()
    {
        super();
    }


    /**
     * @see javax.naming.directory.NoSuchAttributeException#NoSuchAttributeException(String)
     */
    public LdapNoSuchAttributeException( String explanation )
    {
        super( explanation );
    }


    /**
     * @see LdapException#getResultCode()
     */
    public ResultCodeEnum getResultCode()
    {
        return ResultCodeEnum.NOSUCHATTRIBUTE;
    }
}
