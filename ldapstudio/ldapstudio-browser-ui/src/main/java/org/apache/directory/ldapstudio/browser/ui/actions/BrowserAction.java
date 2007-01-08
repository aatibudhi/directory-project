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


import org.apache.directory.ldapstudio.browser.core.events.BookmarkUpdateEvent;
import org.apache.directory.ldapstudio.browser.core.events.ConnectionUpdateEvent;
import org.apache.directory.ldapstudio.browser.core.events.EntryModificationEvent;
import org.apache.directory.ldapstudio.browser.core.events.SearchUpdateEvent;
import org.apache.directory.ldapstudio.browser.core.model.AttributeHierarchy;
import org.apache.directory.ldapstudio.browser.core.model.IAttribute;
import org.apache.directory.ldapstudio.browser.core.model.IBookmark;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.IEntry;
import org.apache.directory.ldapstudio.browser.core.model.ISearch;
import org.apache.directory.ldapstudio.browser.core.model.ISearchResult;
import org.apache.directory.ldapstudio.browser.core.model.IValue;
import org.apache.directory.ldapstudio.browser.core.model.ldif.LdifFile;
import org.apache.directory.ldapstudio.browser.core.model.ldif.LdifPart;
import org.apache.directory.ldapstudio.browser.core.model.ldif.container.LdifContainer;
import org.apache.directory.ldapstudio.browser.ui.widgets.browser.BrowserCategory;
import org.apache.directory.ldapstudio.browser.ui.widgets.browser.BrowserEntryPage;
import org.apache.directory.ldapstudio.browser.ui.widgets.browser.BrowserSearchResultPage;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;


/**
 * This abstract class must be extended by each Action related to the Browser.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public abstract class BrowserAction implements IWorkbenchWindowActionDelegate
{
    /** The selected Connections */
    private IConnection[] selectedConnections;

    /** The selected Browser View Categories */
    private BrowserCategory[] selectedBrowserViewCategories;

    /** The selected Entries */
    private IEntry[] selectedEntries;

    /** The selected Browser Entry Pages */
    private BrowserEntryPage[] selectedBrowserEntryPages;

    /** The selected Searches */
    private ISearch[] selectedSearches;

    /** The selected Search Results */
    private ISearchResult[] selectedSearchResults;

    /** The selected Browser Search Result Pages */
    private BrowserSearchResultPage[] selectedBrowserSearchResultPages;

    /** The selected Bookmarks */
    private IBookmark[] selectedBookmarks;

    /** The selected Attributes */
    private IAttribute[] selectedAttributes;

    /** The selected Attribute Hierarchies */
    private AttributeHierarchy[] selectedAttributeHierarchies;

    /** The selected Values */
    private IValue[] selectedValues;

    /** The selectec LDIF Model */
    private LdifFile selectedLdifModel;

    /** The selected LDIF Containers */
    private LdifContainer[] selectedLdifContainers;

    /** The selected LDIF Parts */
    private LdifPart[] selectedLdifParts;

    /** The input */
    private Object input;


    /**
     * Creates a new instance of BrowserAction.
     */
    protected BrowserAction()
    {
        this.init();
    }


    /**
     * {@inheritDoc}
     */
    public void init( IWorkbenchWindow window )
    {
        this.init();
    }


    /**
     * {@inheritDoc}
     */
    public void run( IAction action )
    {
        this.run();
    }


    /**
     * {@inheritDoc}
     */
    public void selectionChanged( IAction action, ISelection selection )
    {
        setSelectedConnections( SelectionUtils.getConnections( selection ) );

        setSelectedBrowserViewCategories( SelectionUtils.getBrowserViewCategories( selection ) );
        setSelectedEntries( SelectionUtils.getEntries( selection ) );
        setSelectedBrowserEntryPages( SelectionUtils.getBrowserEntryPages( selection ) );
        setSelectedSearchResults( SelectionUtils.getSearchResults( selection ) );
        setSelectedBrowserSearchResultPages( SelectionUtils.getBrowserSearchResultPages( selection ) );
        setSelectedBookmarks( SelectionUtils.getBookmarks( selection ) );

        setSelectedSearches( SelectionUtils.getSearches( selection ) );

        setSelectedAttributes( SelectionUtils.getAttributes( selection ) );
        setSelectedAttributeHierarchies( SelectionUtils.getAttributeHierarchie( selection ) );
        setSelectedValues( SelectionUtils.getValues( selection ) );

        action.setEnabled( this.isEnabled() );
        action.setText( this.getText() );
        action.setToolTipText( this.getText() );
    }


    /**
     * Returns the text for this action.
     * <p>
     * This method is associated with the <code>TEXT</code> property;
     * property change events are reported when its value changes.
     * </p>
     *
     * @return the text, or <code>null</code> if none
     * @see #TEXT
     */
    public abstract String getText();


    /**
     * Returns the image for this action as an image descriptor.
     * <p>
     * This method is associated with the <code>IMAGE</code> property;
     * property change events are reported when its value changes.
     * </p>
     *
     * @return the image, or <code>null</code> if this action has no image
     * @see #IMAGE
     */
    public abstract ImageDescriptor getImageDescriptor();


    /**
     * Returns the command identifier.
     *
     * @return
     *      the command identifier
     */
    public abstract String getCommandId();


    /**
     * Returns whether this action is enabled.
     * <p>
     * This method is associated with the <code>ENABLED</code> property;
     * property change events are reported when its value changes.
     * </p>
     *
     * @return <code>true</code> if enabled, and
     *   <code>false</code> if disabled
     * @see #ENABLED
     */
    public abstract boolean isEnabled();


    /**
     * Runs this action.
     * Each action implementation must define the steps needed to carry out this action.
     * The default implementation of this method in <code>Action</code>
     * does nothing.
     */
    public abstract void run();


    /**
     * Initializes this action
     */
    private void init()
    {
        this.selectedConnections = new IConnection[0];
        this.selectedBrowserViewCategories = new BrowserCategory[0];
        this.selectedEntries = new IEntry[0];
        this.selectedBrowserEntryPages = new BrowserEntryPage[0];
        this.selectedSearches = new ISearch[0];
        this.selectedSearchResults = new ISearchResult[0];
        this.selectedBrowserSearchResultPages = new BrowserSearchResultPage[0];
        this.selectedBookmarks = new IBookmark[0];
        this.selectedAttributes = new IAttribute[0];
        this.selectedAttributeHierarchies = new AttributeHierarchy[0];
        this.selectedValues = new IValue[0];

        this.selectedLdifModel = null;
        this.selectedLdifContainers = new LdifContainer[0];
        this.selectedLdifParts = new LdifPart[0];

        this.input = null;
    }


    /**
     * {@inheritDoc}
     */
    public void dispose()
    {
        this.selectedConnections = new IConnection[0];
        this.selectedBrowserViewCategories = new BrowserCategory[0];
        this.selectedEntries = new IEntry[0];
        this.selectedBrowserEntryPages = new BrowserEntryPage[0];
        this.selectedSearches = new ISearch[0];
        this.selectedSearchResults = new ISearchResult[0];
        this.selectedBrowserSearchResultPages = new BrowserSearchResultPage[0];
        this.selectedBookmarks = new IBookmark[0];
        this.selectedAttributes = new IAttribute[0];
        this.selectedAttributeHierarchies = new AttributeHierarchy[0];
        this.selectedValues = new IValue[0];

        this.selectedLdifModel = null;
        this.selectedLdifContainers = new LdifContainer[0];
        this.selectedLdifParts = new LdifPart[0];

        this.input = null;
    }


    /**
     * Returns the current active shell
     *
     * @return
     *      the current active shell
     */
    protected Shell getShell()
    {
        return PlatformUI.getWorkbench().getDisplay().getActiveShell();
    }


    /**
     * This method is fired when an Entry is updated.
     *
     * @param event
     *      the associated event
     */
    public final void entryUpdated( EntryModificationEvent event )
    {
    }


    /**
     * This method is fired when a Search is updated.
     *
     * @param searchUpdateEvent
     *      the associated event
     */
    public void searchUpdated( SearchUpdateEvent searchUpdateEvent )
    {
    }


    /**
     * This method is fired when a Bookmark is updated.
     *
     * @param bookmarkUpdateEvent
     *      the associated event
     */
    public void bookmarkUpdated( BookmarkUpdateEvent bookmarkUpdateEvent )
    {
    }


    /**
     * This method is fired when a Connection is updated.
     *
     * @param connectionUpdateEvent
     *      the associated event
     */
    public final void connectionUpdated( ConnectionUpdateEvent connectionUpdateEvent )
    {
    }


    /**
     * Gets the selected Attributes.
     *
     * @return
     *      the selected attributes
     */
    public IAttribute[] getSelectedAttributes()
    {
        return selectedAttributes;
    }


    /**
     * Sets the selected Attributes.
     *
     * @param selectedAttributes
     *      the selected attributes to set
     */
    public void setSelectedAttributes( IAttribute[] selectedAttributes )
    {
        this.selectedAttributes = selectedAttributes;
    }


    /**
     * Gets the selected Bookmarks.
     *
     * @return
     *      the selected Bookmarks
     */
    public IBookmark[] getSelectedBookmarks()
    {
        return selectedBookmarks;
    }


    /**
     * Sets the selected Bookmarks.
     *
     * @param selectedBookmarks
     *      the selected Bookmarks to set
     */
    public void setSelectedBookmarks( IBookmark[] selectedBookmarks )
    {
        this.selectedBookmarks = selectedBookmarks;
    }


    /**
     * Gets the selected Browser View categories.
     *
     * @return
     *      the selected Browser View categories
     */
    public BrowserCategory[] getSelectedBrowserViewCategories()
    {
        return selectedBrowserViewCategories;
    }


    /**
     * Sets the selected Browser View categories.
     *
     * @param selectedBrowserViewCategories
     *      the selected Browser View categories to set
     */
    public void setSelectedBrowserViewCategories( BrowserCategory[] selectedBrowserViewCategories )
    {
        this.selectedBrowserViewCategories = selectedBrowserViewCategories;
    }


    /**
     * Gets the selected Connections.
     *
     * @return
     *      the selected Connections
     */
    public IConnection[] getSelectedConnections()
    {
        return selectedConnections;
    }


    /**
     * Sets the selected Connections.
     *
     * @param selectedConnections
     *      the selected Connections to set
     */
    public void setSelectedConnections( IConnection[] selectedConnections )
    {
        this.selectedConnections = selectedConnections;
    }


    /**
     * Get the selected Entries.
     *
     * @return
     */
    public IEntry[] getSelectedEntries()
    {
        return selectedEntries;
    }


    /**
     * Sets the selected Entries.
     *
     * @param selectedEntries
     *      the selected Entries to set
     */
    public void setSelectedEntries( IEntry[] selectedEntries )
    {
        this.selectedEntries = selectedEntries;
    }


    /**
     * Gets the selected Searches.
     *
     * @return
     *      the selected Searches
     */
    public ISearch[] getSelectedSearches()
    {
        return selectedSearches;
    }


    /**
     * Sets the selected Searches.
     *
     * @param selectedSearches
     *      the selected Searches to set
     */
    public void setSelectedSearches( ISearch[] selectedSearches )
    {
        this.selectedSearches = selectedSearches;
    }


    /**
     * Gets the selected Search Results.
     *
     * @return
     *      the selected Search Results
     */
    public ISearchResult[] getSelectedSearchResults()
    {
        return selectedSearchResults;
    }


    /**
     * Sets the selected Search Results.
     *
     * @param selectedSearchResults
     *      the selected Search Results to set
     */
    public void setSelectedSearchResults( ISearchResult[] selectedSearchResults )
    {
        this.selectedSearchResults = selectedSearchResults;
    }


    /**
     * Gets the selected Values.
     *
     * @return
     *      the selected Values
     */
    public IValue[] getSelectedValues()
    {
        return selectedValues;
    }


    /**
     * Sets the selected Values.
     *
     * @param selectedValues
     *      the selected values to set
     */
    public void setSelectedValues( IValue[] selectedValues )
    {
        this.selectedValues = selectedValues;
    }


    /**
     * Gets the input.
     *
     * @return
     *      the input
     */
    public Object getInput()
    {
        return input;
    }


    /**
     * Sets the input.
     *
     * @param input
     *      the input to set
     */
    public void setInput( Object input )
    {
        this.input = input;
    }


    /**
     * Gets the selected LDIF Containers.
     *
     * @return
     *      the selected LDIF Containers
     */
    public LdifContainer[] getSelectedLdifContainers()
    {
        return selectedLdifContainers;
    }


    /**
     * Sets the selected LDIF Containers.
     *
     * @param selectedLdifContainers
     *      the selected LDIF Containers to set
     */
    public void setSelectedLdifContainers( LdifContainer[] selectedLdifContainers )
    {
        this.selectedLdifContainers = selectedLdifContainers;
    }


    /**
     * Gets the selected LDIF Model.
     * 
     * @return
     *      the selected LDIF Model
     */
    public LdifFile getSelectedLdifModel()
    {
        return selectedLdifModel;
    }


    /**
     * Sets the selected LDIF Model.
     *
     * @param selectedLdifModel
     *      the selected LDIF Model to set
     */
    public void setSelectedLdifModel( LdifFile selectedLdifModel )
    {
        this.selectedLdifModel = selectedLdifModel;
    }


    /**
     * Gets the selected LDIF Parts.
     *
     * @return
     *      the selected LDIF Parts
     */
    public LdifPart[] getSelectedLdifParts()
    {
        return selectedLdifParts;
    }


    /**
     * Sets the selected LDIF Parts.
     *
     * @param selectedLdifParts
     *      the selected LDIF Parts to set
     */
    public void setSelectedLdifParts( LdifPart[] selectedLdifParts )
    {
        this.selectedLdifParts = selectedLdifParts;
    }


    /**
     * Gets the selected Browser Entry Pages.
     *
     * @return
     *      the selected Browser Entru Pages
     */
    public BrowserEntryPage[] getSelectedBrowserEntryPages()
    {
        return selectedBrowserEntryPages;
    }


    /**
     * Sets the selected Browser Entry Pages.
     *
     * @param selectedBrowserEntryPages
     *      the selected Browser Entry Pages to set
     */
    public void setSelectedBrowserEntryPages( BrowserEntryPage[] selectedBrowserEntryPages )
    {
        this.selectedBrowserEntryPages = selectedBrowserEntryPages;
    }


    /**
     * Gets the selected Browser Search Result Pages.
     *
     * @return
     *      the selected Browser Search Result Pages
     */
    public BrowserSearchResultPage[] getSelectedBrowserSearchResultPages()
    {
        return selectedBrowserSearchResultPages;
    }


    /**
     * Sets the selected Browser Search Result Pages.
     *
     * @param selectedBrowserSearchResultPages
     *      the selected Browser Search result Pages to set
     */
    public void setSelectedBrowserSearchResultPages( BrowserSearchResultPage[] selectedBrowserSearchResultPages )
    {
        this.selectedBrowserSearchResultPages = selectedBrowserSearchResultPages;
    }


    /**
     * Gets the selected Attribute Hierarchies.
     *
     * @return
     *      the selected Attribute Hierarchies
     */
    public AttributeHierarchy[] getSelectedAttributeHierarchies()
    {
        return selectedAttributeHierarchies;
    }


    /**
     * Sets the selected Attribute Hierarchies.
     *
     * @param ahs
     *      the selected Attribute Hierarchies to set
     */
    public void setSelectedAttributeHierarchies( AttributeHierarchy[] ahs )
    {
        this.selectedAttributeHierarchies = ahs;
    }
}
