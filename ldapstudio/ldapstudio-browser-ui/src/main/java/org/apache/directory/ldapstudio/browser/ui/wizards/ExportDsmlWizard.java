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

import org.apache.directory.ldapstudio.browser.core.jobs.ExportDsmlJob;


/**
 * This class implements the Wizard for Exporting to DSML
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ExportDsmlWizard extends ExportBaseWizard
{
    public static final String WIZARD_TITLE = "DSML Export";

    private ExportDsmlFromWizardPage fromPage;

    private ExportDsmlToWizardPage toPage;


    /**
     * Creates a new instance of ExportDsmlWizard.
     */
    public ExportDsmlWizard()
    {
        super( WIZARD_TITLE );
    }


    /**
     * Gets the ID of the Export DSML Wizard
     * @return The ID of the Export DSML Wizard
     */
    public static String getId()
    {
        return ExportDsmlWizard.class.getName();
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages()
    {
        fromPage = new ExportDsmlFromWizardPage( ExportDsmlFromWizardPage.class.getName(), this );
        addPage( fromPage );
        toPage = new ExportDsmlToWizardPage( ExportDsmlToWizardPage.class.getName(), this );
        addPage( toPage );
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        this.fromPage.saveDialogSettings();
        this.toPage.saveDialogSettings();

        ExportDsmlJob edj = new ExportDsmlJob( this.exportFilename, this.search.getConnection(), this.search
            .getSearchParameter() );
        edj.execute();
        
        return true;
    }
}
