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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.directory.ldapstudio.browser.core.ConnectionManager;
import org.apache.directory.ldapstudio.browser.core.model.IAttribute;
import org.apache.directory.ldapstudio.browser.core.model.IEntry;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;


public class SelectAllAction extends BrowserAction
{

    private Viewer viewer;


    public SelectAllAction( Viewer viewer )
    {
        this.viewer = viewer;
    }


    public String getText()
    {
        return "Select All";
    }


    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }


    public String getCommandId()
    {
        return IWorkbenchActionDefinitionIds.SELECT_ALL;
    }


    public boolean isEnabled()
    {
        return true;
    }


    public void run()
    {
        if ( getInput() != null && getInput() instanceof IEntry )
        {
            List selectionList = new ArrayList();
            IAttribute[] attributes = ( ( IEntry ) getInput() ).getAttributes();
            if ( attributes != null )
            {
                selectionList.addAll( Arrays.asList( attributes ) );
                for ( int i = 0; i < attributes.length; i++ )
                {
                    selectionList.addAll( Arrays.asList( attributes[i].getValues() ) );
                }
            }
            StructuredSelection selection = new StructuredSelection( selectionList );
            this.viewer.setSelection( selection );
        }
        else if ( getInput() != null && getInput() instanceof ConnectionManager )
        {
            StructuredSelection selection = new StructuredSelection( ( ( ConnectionManager ) getInput() )
                .getConnections() );
            this.viewer.setSelection( selection );
        }
        else if ( getSelectedConnections().length > 0 && viewer.getInput() instanceof ConnectionManager )
        {
            StructuredSelection selection = new StructuredSelection( ( ( ConnectionManager ) viewer.getInput() )
                .getConnections() );
            this.viewer.setSelection( selection );
        }
    }

}
