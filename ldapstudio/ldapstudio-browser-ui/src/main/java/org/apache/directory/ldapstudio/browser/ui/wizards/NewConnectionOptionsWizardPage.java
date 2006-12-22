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

package org.apache.directory.ldapstudio.browser.ui.wizards;


import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.ui.BrowserUIConstants;
import org.apache.directory.ldapstudio.browser.ui.BrowserUIPlugin;
import org.apache.directory.ldapstudio.browser.ui.widgets.connection.ConnectionPageModifyListener;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;


public class NewConnectionOptionsWizardPage extends WizardPage implements ConnectionPageModifyListener
{

    private NewConnectionWizard wizard;


    public NewConnectionOptionsWizardPage( String pageName, NewConnectionWizard wizard )
    {
        super( pageName );
        super.setTitle( "Connection Parameter" );
        super.setDescription( "You can specify additional connection parameters." );
        super.setImageDescriptor( BrowserUIPlugin.getDefault().getImageDescriptor(
            BrowserUIConstants.IMG_CONNECTION_WIZARD ) );
        super.setPageComplete( true );

        this.wizard = wizard;
        wizard.getCpw().addConnectionPageModifyListener( this );
    }


    public void connectionPageModified()
    {
        if ( isCurrentPage() )
        {
            validate();
        }
    }


    public void setMessage( String message )
    {
        if ( isCurrentPage() )
        {
            super.setMessage( message );
            validate();
        }
    }


    public void setErrorMessage( String errorMessage )
    {
        if ( isCurrentPage() )
        {
            super.setErrorMessage( errorMessage );
            validate();
        }
    }


    public IConnection getRealConnection()
    {
        return null;
    }


    public void setVisible( boolean visible )
    {
        super.setVisible( visible );
        // this.validate();
    }


    private void validate()
    {
        setPageComplete( getMessage() == null );
        getContainer().updateButtons();
    }


    public void createControl( Composite parent )
    {
        Composite composite = new Composite( parent, SWT.NONE );
        GridLayout gl = new GridLayout( 1, false );
        composite.setLayout( gl );

        wizard.getCpw().addBaseDNInput( true, "", composite );
        wizard.getCpw().addLimitInput(
            BrowserUIPlugin.getDefault().getPreferenceStore().getInt( BrowserUIConstants.PREFERENCE_COUNT_LIMIT ),
            BrowserUIPlugin.getDefault().getPreferenceStore().getInt( BrowserUIConstants.PREFERENCE_TIME_LIMIT ),
            IConnection.DEREFERENCE_ALIASES_ALWAYS, IConnection.HANDLE_REFERRALS_FOLLOW, composite );

        setControl( composite );
        // baseDNText.setFocus();
    }

}