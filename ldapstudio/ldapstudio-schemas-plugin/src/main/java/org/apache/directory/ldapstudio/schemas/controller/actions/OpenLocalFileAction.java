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

package org.apache.directory.ldapstudio.schemas.controller.actions;


import org.apache.directory.ldapstudio.schemas.controller.Application;
import org.apache.directory.ldapstudio.schemas.controller.ICommandIds;
import org.apache.directory.ldapstudio.schemas.model.SchemaPool;
import org.apache.directory.ldapstudio.schemas.model.Schema.SchemaType;
import org.apache.directory.ldapstudio.schemas.view.IImageKeys;
import org.apache.directory.ldapstudio.schemas.view.preferences.SchemaPreferencePage;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * This class implements the Action for opening a local file schema
 */
public class OpenLocalFileAction extends Action implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
    private static Logger logger = Logger.getLogger( OpenLocalFileAction.class );


    public OpenLocalFileAction()
    {
        setText( Messages.getString( "OpenLocalFileAction.Open_a_schema_file" ) ); //$NON-NLS-1$
        // The id is used to refer to the action in a menu or toolbar
        setId( ICommandIds.CMD_OPEN_LOCAL );
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId( ICommandIds.CMD_OPEN_LOCAL );
        setImageDescriptor( AbstractUIPlugin.imageDescriptorFromPlugin( Application.PLUGIN_ID, IImageKeys.OPEN_LOCAL ) );
    }


    public void run( IAction action )
    {
        this.run();
    }


    public void run()
    {
        FileDialog fd = new FileDialog( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN );
        fd.setText( Messages.getString( "OpenLocalFileAction.Open_a_schema_file" ) ); //$NON-NLS-1$

        IEclipsePreferences prefs = new ConfigurationScope().getNode( Application.PLUGIN_ID );
        String defaultPath = prefs.get( SchemaPreferencePage.DEFAULT_DIRECTORY, System.getProperty( "user.home" ) ); //$NON-NLS-1$
        fd.setFilterPath( defaultPath );
        String[] filterExt =
            { "*.schema", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$
        fd.setFilterExtensions( filterExt );
        String selected = fd.open();
        // selected == null if 'cancel' has been pushed
        if ( selected != null )
        {
            SchemaPool pool = SchemaPool.getInstance();
            try
            {
                pool.addAlreadyExistingSchema( selected, SchemaType.userSchema );
            }
            catch ( Exception e )
            {
                logger.debug( "Error when opening a schema file" ); //$NON-NLS-1$
            }
        }

    }


    public void dispose()
    {
    }


    public void init( IWorkbenchWindow window )
    {
    }


    public void selectionChanged( IAction action, ISelection selection )
    {
    }


    public void init( IViewPart view )
    {
    }
}
