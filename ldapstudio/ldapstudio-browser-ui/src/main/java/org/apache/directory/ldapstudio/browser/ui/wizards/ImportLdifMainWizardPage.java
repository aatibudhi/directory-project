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


import java.io.File;

import org.apache.directory.ldapstudio.browser.ui.BrowserUIConstants;
import org.apache.directory.ldapstudio.browser.ui.BrowserUIPlugin;
import org.apache.directory.ldapstudio.browser.ui.widgets.BaseWidgetUtils;
import org.apache.directory.ldapstudio.browser.ui.widgets.FileBrowserWidget;
import org.apache.directory.ldapstudio.browser.ui.widgets.WidgetModifyEvent;
import org.apache.directory.ldapstudio.browser.ui.widgets.WidgetModifyListener;
import org.apache.directory.ldapstudio.browser.ui.widgets.search.ConnectionWidget;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


public class ImportLdifMainWizardPage extends WizardPage
{

    public static final String CONTINUE_ON_ERROR_DIALOGSETTING_KEY = ImportLdifMainWizardPage.class.getName()
        + ".continueOnError";

    private static final String[] EXTENSIONS = new String[]
        { "*.ldif", "*.*" };

    private ImportLdifWizard wizard;

    // private Text ldifFilenameText;
    private FileBrowserWidget ldifFileBrowserWidget;

    private ConnectionWidget connectionWidget;

    private Button enableLoggingButton;

    private Button useDefaultLogfileButton;

    private Button useCustomLogfileButton;

    private String customLogfileName;

    private FileBrowserWidget logFileBrowserWidget;

    // private Text logfileText;
    private Button overwriteLogfileButton;

    private Button continueOnErrorButton;


    public ImportLdifMainWizardPage( String pageName, ImportLdifWizard wizard )
    {
        super( pageName );
        super.setTitle( "LDIF Import" );
        super.setDescription( "Please select a connection and the LDIF to import" );
        super.setImageDescriptor( BrowserUIPlugin.getDefault().getImageDescriptor(
            BrowserUIConstants.IMG_IMPORT_LDIF_WIZARD ) );
        super.setPageComplete( false );

        this.wizard = wizard;
    }


    public void dispose()
    {
        super.dispose();
    }


    public void setVisible( boolean visible )
    {
        super.setVisible( visible );
    }


    private void validate()
    {

        boolean ok = true;

        File ldifFile = new File( ldifFileBrowserWidget.getFilename() );
        if ( "".equals( ldifFileBrowserWidget.getFilename() ) )
        {
            setErrorMessage( null );
            ok = false;
        }
        else if ( !ldifFile.isFile() || !ldifFile.exists() )
        {
            setErrorMessage( "Selected LDIF file doesn't exist." );
            ok = false;
        }
        else if ( !ldifFile.canRead() )
        {
            setErrorMessage( "Selected LDIF file is not readable." );
            ok = false;
        }
        else if ( this.enableLoggingButton.getSelection() )
        {
            File logFile = new File( logFileBrowserWidget.getFilename() );
            File logFileDirectory = logFile.getParentFile();

            if ( logFile.equals( ldifFile ) )
            {
                setErrorMessage( "LDIF file and Logfile must not be equal." );
                ok = false;
            }
            else if ( logFile.isDirectory() )
            {
                setErrorMessage( "Selected logfile is no file." );
                ok = false;
            }
            else if ( logFile.exists() && !this.overwriteLogfileButton.getSelection() )
            {
                setErrorMessage( "Selected logfile already exists. Select option 'Overwrite existing logfile' if you want to overwrite the logfile." );
                ok = false;
            }
            else if ( logFile.exists() && !logFile.canWrite() )
            {
                setErrorMessage( "Selected logfile is not writeable." );
                ok = false;
            }
            else if ( logFile.getParentFile() == null )
            {
                setErrorMessage( "Selected logfile directory is not writeable." );
                ok = false;
            }
            else if ( !logFile.exists() && ( logFileDirectory == null || !logFileDirectory.canWrite() ) )
            {
                setErrorMessage( "Selected logfile directory is not writeable." );
                ok = false;
            }
        }

        if ( wizard.getImportConnection() == null )
        {
            ok = false;
        }

        if ( ok )
        {
            setErrorMessage( null );
        }
        setPageComplete( ok );
        getContainer().updateButtons();
    }


    public void createControl( Composite parent )
    {

        Composite composite = BaseWidgetUtils.createColumnContainer( parent, 3, 1 );

        // LDIF file
        BaseWidgetUtils.createLabel( composite, "LDIF file:", 1 );
        ldifFileBrowserWidget = new FileBrowserWidget( "Select LDIF File", EXTENSIONS, FileBrowserWidget.TYPE_OPEN );
        ldifFileBrowserWidget.createWidget( composite );
        ldifFileBrowserWidget.addWidgetModifyListener( new WidgetModifyListener()
        {
            public void widgetModified( WidgetModifyEvent event )
            {
                wizard.setLdifFilename( ldifFileBrowserWidget.getFilename() );
                if ( useDefaultLogfileButton.getSelection() )
                {
                    logFileBrowserWidget.setFilename( ldifFileBrowserWidget.getFilename() + ".log" );
                }
                validate();
            }
        } );

        // Connection
        BaseWidgetUtils.createLabel( composite, "Import into:", 1 );
        connectionWidget = new ConnectionWidget( wizard.getImportConnection() );
        connectionWidget.createWidget( composite );
        connectionWidget.addWidgetModifyListener( new WidgetModifyListener()
        {
            public void widgetModified( WidgetModifyEvent event )
            {
                wizard.setImportConnection( connectionWidget.getConnection() );
                validate();
            }
        } );

        // Logging
        Composite loggingOuterComposite = BaseWidgetUtils.createColumnContainer( composite, 1, 3 );
        Group loggingGroup = BaseWidgetUtils.createGroup( loggingOuterComposite, "Logging", 1 );
        Composite loggingContainer = BaseWidgetUtils.createColumnContainer( loggingGroup, 3, 1 );

        enableLoggingButton = BaseWidgetUtils.createCheckbox( loggingContainer, "Enable logging", 3 );
        enableLoggingButton.setSelection( true );
        wizard.setEnableLogging( enableLoggingButton.getSelection() );
        enableLoggingButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent event )
            {
                wizard.setEnableLogging( enableLoggingButton.getSelection() );
                useDefaultLogfileButton.setEnabled( enableLoggingButton.getSelection() );
                useCustomLogfileButton.setEnabled( enableLoggingButton.getSelection() );
                logFileBrowserWidget.setEnabled( enableLoggingButton.getSelection()
                    && useCustomLogfileButton.getSelection() );
                overwriteLogfileButton.setEnabled( enableLoggingButton.getSelection() );
                validate();
            }
        } );

        BaseWidgetUtils.createRadioIndent( loggingContainer, 1 );
        useDefaultLogfileButton = BaseWidgetUtils.createRadiobutton( loggingContainer, "Use default logfile", 2 );
        useDefaultLogfileButton.setSelection( true );
        useDefaultLogfileButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent event )
            {
                String temp = customLogfileName;
                logFileBrowserWidget.setFilename( ldifFileBrowserWidget.getFilename() + ".log" );
                logFileBrowserWidget.setEnabled( false );
                customLogfileName = temp;
                validate();
            }
        } );

        BaseWidgetUtils.createRadioIndent( loggingContainer, 1 );
        useCustomLogfileButton = BaseWidgetUtils.createRadiobutton( loggingContainer, "Use custom logfile", 2 );
        useCustomLogfileButton.setSelection( false );
        useCustomLogfileButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent event )
            {
                logFileBrowserWidget.setFilename( customLogfileName != null ? customLogfileName : "" );
                logFileBrowserWidget.setEnabled( true );
                validate();
            }
        } );

        BaseWidgetUtils.createRadioIndent( loggingContainer, 1 );
        logFileBrowserWidget = new FileBrowserWidget( "Select Logfile", null, FileBrowserWidget.TYPE_SAVE );
        logFileBrowserWidget.createWidget( loggingContainer );
        logFileBrowserWidget.addWidgetModifyListener( new WidgetModifyListener()
        {
            public void widgetModified( WidgetModifyEvent event )
            {
                customLogfileName = logFileBrowserWidget.getFilename();
                wizard.setLogFilename( customLogfileName );
                validate();
            }
        } );
        logFileBrowserWidget.setEnabled( false );

        BaseWidgetUtils.createRadioIndent( loggingContainer, 1 );
        overwriteLogfileButton = BaseWidgetUtils.createCheckbox( loggingContainer, "Overwrite existing logfile", 2 );
        overwriteLogfileButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent event )
            {
                validate();
            }
        } );

        // Continue
        continueOnErrorButton = BaseWidgetUtils.createCheckbox( composite, "Continue on error", 3 );
        if ( BrowserUIPlugin.getDefault().getDialogSettings().get( CONTINUE_ON_ERROR_DIALOGSETTING_KEY ) == null )
        {
            BrowserUIPlugin.getDefault().getDialogSettings().put( CONTINUE_ON_ERROR_DIALOGSETTING_KEY, false );
        }
        continueOnErrorButton.setSelection( BrowserUIPlugin.getDefault().getDialogSettings().getBoolean(
            CONTINUE_ON_ERROR_DIALOGSETTING_KEY ) );
        wizard.setContinueOnError( continueOnErrorButton.getSelection() );
        continueOnErrorButton.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent event )
            {
                wizard.setContinueOnError( continueOnErrorButton.getSelection() );
                validate();
            }
        } );

        setControl( composite );
        // nameText.setFocus();
    }


    public void saveDialogSettings()
    {
        this.ldifFileBrowserWidget.saveDialogSettings();
        BrowserUIPlugin.getDefault().getDialogSettings().put( CONTINUE_ON_ERROR_DIALOGSETTING_KEY,
            continueOnErrorButton.getSelection() );
    }

}