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

package org.apache.directory.shared.ldap.message;


import java.util.Iterator;
import javax.naming.NamingEnumeration;


/**
 * A NamingEnumeration over an Iterator.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Revision$
 */
public class IteratorNamingEnumeration implements NamingEnumeration
{
    /** the iterator to wrap as in the enumeration */
    private final Iterator m_iterator;


    /**
     * Creates a NamingEnumeration over an Iterator.
     * 
     * @param a_iterator
     *            the Iterator the NamingEnumeration is based on.
     */
    public IteratorNamingEnumeration(final Iterator a_iterator)
    {
        m_iterator = a_iterator;
    }


    // --------------------------------------------------------------------
    // Enumeration Interface Method Implementations
    // --------------------------------------------------------------------

    /**
     * @see java.util.Enumeration#hasMoreElements()
     */
    public boolean hasMoreElements()
    {
        return m_iterator.hasNext();
    }


    /**
     * @see java.util.Enumeration#nextElement()
     */
    public Object nextElement()
    {
        return m_iterator.next();
    }


    // --------------------------------------------------------------------
    // NamingEnumeration Interface Method Implementations
    // --------------------------------------------------------------------

    /**
     * @see javax.naming.NamingEnumeration#close()
     */
    public void close()
    {
        // Does nothing!
    }


    /**
     * @see javax.naming.NamingEnumeration#hasMore()
     */
    public boolean hasMore()
    {
        return m_iterator.hasNext();
    }


    /**
     * @see javax.naming.NamingEnumeration#next()
     */
    public Object next()
    {
        return m_iterator.next();
    }
}
