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

package org.apache.directory.ldapstudio.view;


import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


/**
 * This class defines LDAP Studio Main perspective.
 */
public class Perspective implements IPerspectiveFactory
{

    /**
     * Creates the initial layout for a page.
     */
    public void createInitialLayout( IPageLayout layout )
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible( false );

        layout.addStandaloneView( WelcomeView.ID, false, IPageLayout.LEFT, 1.0f, editorArea );
        layout.getViewLayout( WelcomeView.ID ).setCloseable( false );
        
//        // Outline folder
//        IFolderLayout outlineFolder = layout.createFolder( "outlineFolder", IPageLayout.RIGHT, ( float ) 0.75,
//            editorArea );
//        outlineFolder.addView( IPageLayout.ID_OUTLINE );
//        
//        // Progress folder
//        IFolderLayout progessFolder = layout.createFolder( "progressFolder", IPageLayout.BOTTOM, ( float ) 0.75,
//            IPageLayout.ID_OUTLINE );
//        progessFolder.addView( IPageLayout.ID_PROGRESS_VIEW );
    }

}
