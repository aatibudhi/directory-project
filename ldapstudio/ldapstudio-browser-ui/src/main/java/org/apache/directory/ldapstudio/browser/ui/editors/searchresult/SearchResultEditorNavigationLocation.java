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

package org.apache.directory.ldapstudio.browser.ui.editors.searchresult;


import org.apache.directory.ldapstudio.browser.core.BrowserCorePlugin;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.ISearch;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.NavigationLocation;


public class SearchResultEditorNavigationLocation extends NavigationLocation
{

    protected SearchResultEditorNavigationLocation( SearchResultEditor editor )
    {
        super( editor );
    }


    public String getText()
    {
        ISearch search = getSearch();
        if ( search != null )
        {
            return search.getName();
        }
        else
        {
            return super.getText();
        }
    }


    public void saveState( IMemento memento )
    {
        ISearch search = getSearch();
        memento.putString( "SEARCH", search.getName() );
        memento.putString( "CONNECTION", search.getConnection().getName() );
    }


    public void restoreState( IMemento memento )
    {
        IConnection connection = BrowserCorePlugin.getDefault().getConnectionManager().getConnection(
            memento.getString( "CONNECTION" ) );
        ISearch search = connection.getSearchManager().getSearch( memento.getString( "SEARCH" ) );
        super.setInput( new SearchResultEditorInput( search ) );
    }


    public void restoreLocation()
    {
        IEditorPart editorPart = getEditorPart();
        if ( editorPart != null && editorPart instanceof SearchResultEditor )
        {
            SearchResultEditor searchResultEditor = ( SearchResultEditor ) editorPart;
            searchResultEditor.setInput( ( SearchResultEditorInput ) getInput() );
        }
    }


    public boolean mergeInto( INavigationLocation currentLocation )
    {
        return false;
    }


    public void update()
    {
    }


    private ISearch getSearch()
    {

        Object editorInput = getInput();
        if ( editorInput != null && editorInput instanceof SearchResultEditorInput )
        {
            SearchResultEditorInput searchResultEditorInput = ( SearchResultEditorInput ) editorInput;
            ISearch search = searchResultEditorInput.getSearch();
            if ( search != null )
            {
                return search;
            }
        }

        return null;
    }

}
