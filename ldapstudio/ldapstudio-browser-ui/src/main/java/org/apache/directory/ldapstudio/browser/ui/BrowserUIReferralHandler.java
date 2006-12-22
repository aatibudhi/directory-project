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

package org.apache.directory.ldapstudio.browser.ui;


import java.util.HashMap;
import java.util.Map;

import org.apache.directory.ldapstudio.browser.core.BrowserCorePlugin;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.IReferralHandler;
import org.apache.directory.ldapstudio.browser.core.model.URL;
import org.apache.directory.ldapstudio.browser.ui.dialogs.SelectReferralConnectionDialog;

import org.eclipse.ui.PlatformUI;


public class BrowserUIReferralHandler implements IReferralHandler
{

    private Map referralUrlToReferralConnectionCache = new HashMap();


    public IConnection getReferralConnection( final URL referralUrl )
    {

        // check cache
        if ( referralUrlToReferralConnectionCache.containsKey( referralUrl ) )
        {
            IConnection referralConnection = ( IConnection ) referralUrlToReferralConnectionCache.get( referralUrl );
            if ( referralConnection != null )
            {
                IConnection[] connections = BrowserCorePlugin.getDefault().getConnectionManager().getConnections();
                for ( int i = 0; i < connections.length; i++ )
                {
                    IConnection connection = connections[i];
                    if ( referralConnection == connection )
                    {
                        return referralConnection;
                    }
                }
            }
        }

        referralUrlToReferralConnectionCache.remove( referralUrl );

        // open dialog
        final IConnection[] referralConnection = new IConnection[1];
        PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable()
        {
            public void run()
            {
                SelectReferralConnectionDialog dialog = new SelectReferralConnectionDialog( PlatformUI.getWorkbench()
                    .getDisplay().getActiveShell(), referralUrl );
                if ( dialog.open() == SelectReferralConnectionDialog.OK )
                {
                    IConnection connection = dialog.getReferralConnection();
                    referralUrlToReferralConnectionCache.put( referralUrl, connection );
                    referralConnection[0] = connection;
                }
            }
        } );

        return referralConnection[0];
    }

}
