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

package org.apache.directory.ldapstudio.browser.ui.dialogs;


import org.apache.directory.ldapstudio.browser.core.internal.model.DummyConnection;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.IEntry;
import org.apache.directory.ldapstudio.browser.core.model.ModelModificationException;
import org.apache.directory.ldapstudio.browser.core.model.NameException;
import org.apache.directory.ldapstudio.browser.core.model.ldif.container.LdifChangeAddRecord;
import org.apache.directory.ldapstudio.browser.core.model.ldif.container.LdifContentRecord;
import org.apache.directory.ldapstudio.browser.core.model.ldif.container.LdifRecord;
import org.apache.directory.ldapstudio.browser.core.model.schema.Schema;
import org.apache.directory.ldapstudio.browser.core.utils.ModelConverter;
import org.apache.directory.ldapstudio.browser.ui.BrowserUIConstants;
import org.apache.directory.ldapstudio.browser.ui.BrowserUIPlugin;
import org.apache.directory.ldapstudio.browser.ui.widgets.entryeditor.EntryEditorWidget;
import org.apache.directory.ldapstudio.browser.ui.widgets.entryeditor.EntryEditorWidgetActionGroup;
import org.apache.directory.ldapstudio.browser.ui.widgets.entryeditor.EntryEditorWidgetConfiguration;
import org.apache.directory.ldapstudio.browser.ui.widgets.entryeditor.EntryEditorWidgetUniversalListener;
import org.apache.directory.ldapstudio.browser.ui.wizards.NewEntryAttributesWizardPageActionGroup;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;


public class LdifEntryEditorDialog extends Dialog
{

    public static final String DIALOG_TITLE = "LDIF Entry Editor";

    public static final int MAX_WIDTH = 450;

    public static final int MAX_HEIGHT = 250;

    private IConnection connection;

    private LdifRecord ldifRecord;

    private IEntry entry;

    private EntryEditorWidgetConfiguration configuration;

    private EntryEditorWidgetActionGroup actionGroup;

    private EntryEditorWidget mainWidget;

    private EntryEditorWidgetUniversalListener universalListener;


    public LdifEntryEditorDialog( Shell parentShell, IConnection connection, LdifContentRecord ldifRecord )
    {
        this( parentShell, connection, ldifRecord, null );
    }


    public LdifEntryEditorDialog( Shell parentShell, IConnection connection, LdifChangeAddRecord ldifRecord )
    {
        this( parentShell, connection, ldifRecord, null );
    }


    private LdifEntryEditorDialog( Shell parentShell, IConnection connection, LdifRecord ldifRecord, String s )
    {
        super( parentShell );
        setShellStyle( getShellStyle() | SWT.RESIZE );
        this.ldifRecord = ldifRecord;

        this.connection = new DummyConnection( ( connection != null && connection.getSchema() != null ) ? connection
            .getSchema() : Schema.DEFAULT_SCHEMA );
    }


    protected void configureShell( Shell shell )
    {
        super.configureShell( shell );
        shell.setText( DIALOG_TITLE );
        shell.setImage( BrowserUIPlugin.getDefault().getImage( BrowserUIConstants.IMG_BROWSER_LDIFEDITOR ) );
    }


    protected void createButtonsForButtonBar( Composite parent )
    {
        createButton( parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false );
        createButton( parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false );

        getShell().update();
        getShell().layout( true, true );
    }


    protected void buttonPressed( int buttonId )
    {

        if ( IDialogConstants.OK_ID == buttonId )
        {
            if ( this.ldifRecord instanceof LdifContentRecord )
            {
                this.ldifRecord = ModelConverter.entryToLdifContentRecord( entry );
            }
            else if ( this.ldifRecord instanceof LdifChangeAddRecord )
            {
                this.ldifRecord = ModelConverter.entryToLdifChangeAddRecord( entry );
            }
        }

        super.buttonPressed( buttonId );
    }


    public void create()
    {
        super.create();
        // this.actionGroup.activateGlobalActionHandlers();
    }


    public boolean close()
    {
        boolean returnValue = super.close();
        if ( returnValue )
        {
            this.dispose();
        }
        return returnValue;
    }


    public void dispose()
    {
        if ( this.configuration != null )
        {
            this.universalListener.dispose();
            this.universalListener = null;
            this.mainWidget.dispose();
            this.mainWidget = null;
            this.actionGroup.deactivateGlobalActionHandlers();
            this.actionGroup.dispose();
            this.actionGroup = null;
            this.configuration.dispose();
            this.configuration = null;
        }
    }


    protected Control createDialogArea( Composite parent )
    {

        Composite composite = ( Composite ) super.createDialogArea( parent );

        // create configuration
        this.configuration = new EntryEditorWidgetConfiguration();

        // create main widget
        this.mainWidget = new EntryEditorWidget( this.configuration );
        this.mainWidget.createWidget( composite );
        this.mainWidget.getViewer().getTree().setFocus();

        // create actions
        this.actionGroup = new NewEntryAttributesWizardPageActionGroup( this.mainWidget, this.configuration );
        this.actionGroup.fillToolBar( this.mainWidget.getToolBarManager() );
        this.actionGroup.fillMenu( this.mainWidget.getMenuManager() );
        this.actionGroup.fillContextMenu( this.mainWidget.getContextMenuManager() );
        this.actionGroup.activateGlobalActionHandlers();

        // create the listener
        this.universalListener = new EntryEditorWidgetUniversalListener( this.mainWidget.getViewer(), this.actionGroup
            .getOpenDefaultEditorAction() );

        try
        {

            if ( ldifRecord instanceof LdifContentRecord )
            {
                entry = ModelConverter.ldifContentRecordToEntry( ( LdifContentRecord ) ldifRecord, connection );
            }
            else if ( ldifRecord instanceof LdifChangeAddRecord )
            {
                entry = ModelConverter.ldifChangeAddRecordToEntry( ( LdifChangeAddRecord ) ldifRecord, connection );
            }

            if ( entry != null )
            {
                this.mainWidget.getViewer().setInput( entry );
            }

        }
        catch ( ModelModificationException e )
        {
            e.printStackTrace();
        }
        catch ( NameException e )
        {
            e.printStackTrace();
        }

        applyDialogFont( composite );
        return composite;
    }


    public LdifRecord getLdifRecord()
    {
        return ldifRecord;
    }

}
