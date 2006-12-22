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

package org.apache.directory.ldapstudio.browser.ui.actions;


import org.apache.directory.ldapstudio.browser.core.BrowserCorePlugin;
import org.apache.directory.ldapstudio.browser.core.jobs.CopyEntriesJob;
import org.apache.directory.ldapstudio.browser.core.jobs.CreateValuesJob;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.IEntry;
import org.apache.directory.ldapstudio.browser.core.model.ISearch;
import org.apache.directory.ldapstudio.browser.core.model.IValue;
import org.apache.directory.ldapstudio.browser.core.model.ldif.container.LdifContentRecord;
import org.apache.directory.ldapstudio.browser.ui.dialogs.ScopeDialog;
import org.apache.directory.ldapstudio.browser.ui.dnd.ConnectionTransfer;
import org.apache.directory.ldapstudio.browser.ui.dnd.EntryTransfer;
import org.apache.directory.ldapstudio.browser.ui.dnd.LdifContentRecordTransfer;
import org.apache.directory.ldapstudio.browser.ui.dnd.ValuesTransfer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;


public class PasteAction extends BrowserAction
{

    public PasteAction()
    {
        super();
    }


    public String getText()
    {

        // connection
        IConnection[] connections = getConnectionsToPaste();
        if ( connections != null )
        {
            return connections.length > 1 ? "Paste Connections" : "Paste Connection";
        }

        // entry
        IEntry[] entries = getEntriesToPaste();
        if ( entries != null )
        {
            return entries.length > 1 ? "Paste Entries" : "Paste Entry";
        }
        LdifContentRecord[] records = getLdifContentRecordToPaste();
        if ( records != null )
        {
            return records.length > 1 ? "Paste Entries" : "Paste Entry";
        }

        // value
        IValue[] values = getValuesToPaste();
        if ( values != null )
        {
            return values.length > 1 ? "Paste Values" : "Paste Value";
        }

        return "Paste";
    }


    public ImageDescriptor getImageDescriptor()
    {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor( ISharedImages.IMG_TOOL_PASTE );
    }


    public String getCommandId()
    {
        return IWorkbenchActionDefinitionIds.PASTE;
    }


    public boolean isEnabled()
    {

        // connection
        if ( getConnectionsToPaste() != null )
        {
            return true;
        }

        // entry
        else if ( getEntriesToPaste() != null )
        {
            return true;
        }
        else if ( getLdifContentRecordToPaste() != null )
        {
            return true;
        }

        // value
        else if ( getValuesToPaste() != null )
        {
            return true;
        }

        return false;
    }


    public void run()
    {

        // connection
        IConnection[] connections = getConnectionsToPaste();
        if ( connections != null )
        {
            for ( int i = 0; i < connections.length; i++ )
            {
                IConnection newConnection = ( IConnection ) connections[i].clone();
                BrowserCorePlugin.getDefault().getConnectionManager().addConnection( newConnection );
            }
            return;
        }

        // entry
        IEntry[] entries = getEntriesToPaste();
        if ( entries != null )
        {
            this.pasteEntries( getSelectedEntries()[0], entries );
            return;
        }
        LdifContentRecord[] records = getLdifContentRecordToPaste();
        if ( records != null )
        {
            this.pasteLdifContentRecord( getSelectedEntries()[0], records );
            return;
        }

        // value
        IValue[] values = getValuesToPaste();
        if ( values != null )
        {
            this.pasteValues( values );
            return;
        }

    }


    private void pasteEntries( final IEntry parent, final IEntry[] entriesToPaste )
    {

        int scope = ISearch.SCOPE_OBJECT;
        boolean askForScope = false;
        for ( int i = 0; i < entriesToPaste.length; i++ )
        {
            if ( entriesToPaste[i].hasChildren() )
            {
                askForScope = true;
                break;
            }
        }
        if ( askForScope )
        {
            ScopeDialog scopeDialog = new ScopeDialog( Display.getDefault().getActiveShell(), "Select Copy Depth",
                entriesToPaste.length > 1 );
            scopeDialog.open();
            scope = scopeDialog.getScope();
        }

        new CopyEntriesJob( parent, entriesToPaste, scope ).execute();
    }


    private void pasteLdifContentRecord( final IEntry parent, final LdifContentRecord[] recordsToPaste )
    {

    }


    private void pasteValues( IValue[] values )
    {
        IEntry entry = null;
        if ( getSelectedAttributes().length > 0 )
        {
            entry = getSelectedAttributes()[0].getEntry();
        }
        else if ( getSelectedValues().length > 0 )
        {
            entry = getSelectedValues()[0].getAttribute().getEntry();
        }
        else if ( getSelectedEntries().length == 1 )
        {
            entry = getSelectedEntries()[0];
        }
        else if ( getSelectedSearchResults().length == 1 )
        {
            entry = getSelectedSearchResults()[0].getEntry();
        }
        else if ( getSelectedBookmarks().length == 1 )
        {
            entry = getSelectedBookmarks()[0].getEntry();
        }

        if ( entry != null )
        {
            String[] attributeNames = new String[values.length];
            Object[] rawValues = new Object[values.length];
            for ( int v = 0; v < values.length; v++ )
            {
                attributeNames[v] = values[v].getAttribute().getDescription();
                rawValues[v] = values[v].getRawValue();
            }
            new CreateValuesJob( entry, attributeNames, rawValues ).execute();
        }
    }


    /**
     * Conditions: - a connection is selected - there are connections in
     * clipboard
     * 
     * @return
     */
    private IConnection[] getConnectionsToPaste()
    {
        if ( getSelectedBookmarks().length + getSelectedEntries().length + getSelectedSearchResults().length
            + getSelectedSearches().length + getSelectedAttributes().length + getSelectedValues().length == 0
            && getSelectedConnections().length > 0 )
        {

            Object content = this.getFromClipboard( ConnectionTransfer.getInstance() );
            if ( content != null && content instanceof IConnection[] )
            {
                IConnection[] connections = ( IConnection[] ) content;
                return connections;
            }
        }

        return null;
    }


    /**
     * Conditions: - an entry is selected - there are entries in clipboard
     * 
     * @return
     */
    private IEntry[] getEntriesToPaste()
    {
        if ( getSelectedBookmarks().length + getSelectedSearchResults().length + getSelectedSearches().length
            + getSelectedConnections().length + getSelectedAttributes().length + getSelectedValues().length == 0
            && getSelectedEntries().length == 1 )
        {

            Object content = this.getFromClipboard( EntryTransfer.getInstance() );
            if ( content != null && content instanceof IEntry[] )
            {
                IEntry[] entries = ( IEntry[] ) content;
                return entries;
            }
        }

        return null;
    }


    /**
     * Conditions: - an entry is selected - there are LdifContentRecords in
     * clipboard
     * 
     * @return
     */
    private LdifContentRecord[] getLdifContentRecordToPaste()
    {
        if ( getSelectedBookmarks().length + getSelectedSearchResults().length + getSelectedSearches().length
            + getSelectedConnections().length + getSelectedAttributes().length + getSelectedValues().length == 0
            && getSelectedEntries().length == 1 )
        {

            Object content = this.getFromClipboard( LdifContentRecordTransfer.getInstance() );
            if ( content != null && content instanceof LdifContentRecord[] )
            {
                LdifContentRecord[] records = ( LdifContentRecord[] ) content;
                return records;
            }
        }

        return null;
    }


    /**
     * Conditions: - an attribute or value is selected - there are values in
     * clipboard
     * 
     * @return
     */
    private IValue[] getValuesToPaste()
    {
        if ( ( getSelectedEntries().length + getSelectedSearchResults().length + getSelectedBookmarks().length
            + getSelectedSearches().length + getSelectedConnections().length == 0 && ( getSelectedAttributes().length
            + getSelectedValues().length > 0 ) )
            || ( getSelectedAttributes().length + getSelectedValues().length + getSelectedSearchResults().length
                + getSelectedBookmarks().length + getSelectedSearches().length + getSelectedConnections().length == 0 && ( getSelectedEntries().length == 1 ) )
            || ( getSelectedAttributes().length + getSelectedValues().length + getSelectedEntries().length
                + getSelectedSearchResults().length + getSelectedSearches().length + getSelectedConnections().length == 0 && ( getSelectedBookmarks().length == 1 ) )
            || ( getSelectedAttributes().length + getSelectedValues().length + getSelectedEntries().length
                + getSelectedBookmarks().length + getSelectedSearches().length + getSelectedConnections().length == 0 && ( getSelectedSearchResults().length == 1 ) )

        )
        {

            Object content = this.getFromClipboard( ValuesTransfer.getInstance() );
            if ( content != null && content instanceof IValue[] )
            {
                IValue[] values = ( IValue[] ) content;
                return values;
            }
        }

        return null;
    }


    protected Object getFromClipboard( Transfer dataType )
    {
        Clipboard clipboard = null;
        try
        {
            clipboard = new Clipboard( Display.getCurrent() );
            return clipboard.getContents( dataType );
        }
        finally
        {
            if ( clipboard != null )
                clipboard.dispose();
        }
    }

}
