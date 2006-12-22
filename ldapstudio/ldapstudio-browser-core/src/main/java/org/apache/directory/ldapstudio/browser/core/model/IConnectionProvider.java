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

package org.apache.directory.ldapstudio.browser.core.model;


import org.apache.directory.ldapstudio.browser.core.internal.model.ConnectionException;
import org.apache.directory.ldapstudio.browser.core.jobs.ExtendedProgressMonitor;
import org.apache.directory.ldapstudio.browser.core.model.ldif.LdifEnumeration;
import org.apache.directory.ldapstudio.browser.core.model.ldif.container.LdifRecord;


public interface IConnectionProvider
{

    public void connect( ConnectionParameter parameter, ExtendedProgressMonitor monitor ) throws ConnectionException;


    public void bind( ConnectionParameter parameter, ICredentials credentials, ExtendedProgressMonitor monitor )
        throws ConnectionException;


    public void close() throws ConnectionException;


    public LdifEnumeration search( SearchParameter parameter, ExtendedProgressMonitor monitor )
        throws ConnectionException;


    public void applyModification( LdifRecord record, int handleReferralsMethod, ExtendedProgressMonitor monitor )
        throws ConnectionException;

}
