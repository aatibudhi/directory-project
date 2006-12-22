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
import org.apache.directory.ldapstudio.schemas.view.IImageKeys;
import org.apache.directory.ldapstudio.schemas.view.wizards.CreateANewSchemaWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * This class implements the Action for creating a new schema
 */

public class CreateANewSchemaAction extends Action implements IWorkbenchWindowActionDelegate, IViewActionDelegate
{
    /**
     * Default constructor
     * @param window
     * @param label
     */
    public CreateANewSchemaAction()
    {
        setText( Messages.getString( "CreateANewSchemaAction.Create_a_new_schema" ) ); //$NON-NLS-1$
        // The id is used to refer to the action in a menu or toolbar
        setId( ICommandIds.CMD_CREATE_A_NEW_SCHEMA );
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId( ICommandIds.CMD_CREATE_A_NEW_SCHEMA );
        setImageDescriptor( AbstractUIPlugin.imageDescriptorFromPlugin( Application.PLUGIN_ID,
            IImageKeys.CREATE_A_NEW_SCHEMA ) );
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run( IAction action )
    {
        this.run();
    }


    public void run()
    {
        // Instantiates and initializes the wizard
        CreateANewSchemaWizard wizard = new CreateANewSchemaWizard();
        wizard.init( PlatformUI.getWorkbench(), StructuredSelection.EMPTY );
        // Instantiates the wizard container with the wizard and opens it
        WizardDialog dialog = new WizardDialog( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), wizard );
        dialog.create();
        dialog.open();
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
