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

package org.apache.directory.ldapstudio.browser.core.internal.model;


import org.apache.directory.ldapstudio.browser.core.model.DN;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.ModelModificationException;


public class DirectoryMetadataEntry extends BaseDNEntry
{

    private static final long serialVersionUID = 1340597532850853276L;

    private boolean schemaEntry;


    protected DirectoryMetadataEntry()
    {
    }


    public DirectoryMetadataEntry( DN dn, IConnection connection )
        throws ModelModificationException
    {
        super();
        this.baseDn = dn;
        // this.connectionName = connection.getName();
        this.connection = connection;
        this.schemaEntry = false;
    }


    public boolean hasChildren()
    {
        if ( getDn().equals( getConnection().getSchema().getDn() ) )
        {
            return false;
        }
        else
        {
            return super.hasChildren();
        }
    }


    public boolean isSchemaEntry()
    {
        return schemaEntry;
    }


    public void setSchemaEntry( boolean schemaEntry )
    {
        this.schemaEntry = schemaEntry;
    }

}
