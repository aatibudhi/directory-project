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
import org.apache.directory.ldapstudio.schemas.model.Schema;
import org.apache.directory.ldapstudio.schemas.view.IImageKeys;
import org.apache.directory.ldapstudio.schemas.view.viewers.PoolManager;
import org.apache.directory.ldapstudio.schemas.view.viewers.wrappers.AttributeTypeWrapper;
import org.apache.directory.ldapstudio.schemas.view.viewers.wrappers.IntermediateNode;
import org.apache.directory.ldapstudio.schemas.view.viewers.wrappers.ObjectClassWrapper;
import org.apache.directory.ldapstudio.schemas.view.viewers.wrappers.SchemaWrapper;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * This class implements the Action for saving a schema
 */
public class SaveAction extends Action implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
    private static Logger logger = Logger.getLogger( SaveAction.class );


    /**
     * Default constructor
     * @param window
     * @param label
     */
    public SaveAction()
    {
        setText( Messages.getString( "SaveAction.Save_schema" ) ); //$NON-NLS-1$
        // The id is used to refer to the action in a menu or toolbar
        setId( ICommandIds.CMD_SAVE );
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId( ICommandIds.CMD_SAVE );
        setImageDescriptor( AbstractUIPlugin.imageDescriptorFromPlugin( Application.PLUGIN_ID, IImageKeys.SAVE ) );
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run()
    {
        PoolManager view = ( PoolManager ) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .findView( Application.PLUGIN_ID + ".view.PoolManager" ); //$NON-NLS-1$
        Object selection = ( ( TreeSelection ) view.getViewer().getSelection() ).getFirstElement();

        // We have to check on which node we are to get the schema name
        if ( selection != null )
        {

            Schema schema = null;

            if ( selection instanceof SchemaWrapper )
            {
                schema = ( ( SchemaWrapper ) selection ).getMySchema();
            }
            else if ( selection instanceof AttributeTypeWrapper )
            {
                // We have to get the parent of the parent ( AttributeTypeWrapper => IntermediateNode => SchemaWrapper )
                schema = ( ( SchemaWrapper ) ( ( AttributeTypeWrapper ) selection ).getParent().getParent() )
                    .getMySchema();
            }
            else if ( selection instanceof ObjectClassWrapper )
            {
                // We have to get the parent of the parent ( ObjectClassWrapper => IntermediateNode => SchemaWrapper )
                schema = ( ( SchemaWrapper ) ( ( ObjectClassWrapper ) selection ).getParent().getParent() )
                    .getMySchema();
            }
            else if ( selection instanceof IntermediateNode )
            {
                schema = ( ( SchemaWrapper ) ( ( IntermediateNode ) selection ).getParent() ).getMySchema();
            }

            if ( schema != null )
            {
                try
                {
                    schema.save();
                }
                catch ( Exception e )
                {
                    ErrorDialog
                        .openError(
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                            Messages.getString( "SaveAction.Error" ), Messages.getString( "SaveAction.An_error_occurred_when_saving_schema" ) + schema.getName(), new Status( IStatus.ERROR, Application.PLUGIN_ID, 0, "Status Error Message", null ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    logger.debug( "An error occured when saving schema " + schema.getName() ); //$NON-NLS-1$
                }
            }
        }
    }


    public void dispose()
    {
    }


    public void init( IWorkbenchWindow window )
    {
    }


    public void run( IAction action )
    {
        this.run();
    }


    public void selectionChanged( IAction action, ISelection selection )
    {
    }


    public void init( IViewPart view )
    {
    }
}
