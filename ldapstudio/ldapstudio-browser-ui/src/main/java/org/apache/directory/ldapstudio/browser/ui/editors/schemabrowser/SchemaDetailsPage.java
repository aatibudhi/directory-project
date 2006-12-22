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

package org.apache.directory.ldapstudio.browser.ui.editors.schemabrowser;


import org.apache.directory.ldapstudio.browser.core.events.EventRegistry;
import org.apache.directory.ldapstudio.browser.core.events.SchemaElementSelectionListener;
import org.apache.directory.ldapstudio.browser.core.model.schema.AttributeTypeDescription;
import org.apache.directory.ldapstudio.browser.core.model.schema.LdapSyntaxDescription;
import org.apache.directory.ldapstudio.browser.core.model.schema.MatchingRuleDescription;
import org.apache.directory.ldapstudio.browser.core.model.schema.MatchingRuleUseDescription;
import org.apache.directory.ldapstudio.browser.core.model.schema.ObjectClassDescription;
import org.apache.directory.ldapstudio.browser.core.model.schema.SchemaPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;


public abstract class SchemaDetailsPage implements SchemaElementSelectionListener
{

    protected Section rawSection;

    protected Text rawText;

    protected FormToolkit toolkit;

    protected SchemaBrowser schemaBrowser;

    protected ScrolledForm detailForm;


    protected SchemaDetailsPage( SchemaBrowser schemaBrowser, FormToolkit toolkit )
    {
        this.schemaBrowser = schemaBrowser;
        this.toolkit = toolkit;
        EventRegistry.addSchemaElementSelectionListener( this );
    }


    public void dispose()
    {
        EventRegistry.removeSchemaElementSelectionListener( this );
    }


    public void attributeTypeDescriptionSelected( AttributeTypeDescription atd )
    {
    }


    public void objectClassDescriptionSelected( ObjectClassDescription ocd )
    {
    }


    public void matchingRuleDescriptionSelected( MatchingRuleDescription mrd )
    {
    }


    public void ldapSyntacDescriptionSelected( LdapSyntaxDescription lsd )
    {
    }


    public void matchingRuleUseDescriptionSelected( MatchingRuleUseDescription mrud )
    {
    }


    public abstract void createContents( final ScrolledForm detailForm );


    public void createRawSection()
    {
        rawSection = toolkit.createSection( detailForm.getBody(), Section.TWISTIE );
        rawSection.setText( "Raw Schema Definition" );
        rawSection.marginWidth = 0;
        rawSection.marginHeight = 0;
        rawSection.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
        toolkit.createCompositeSeparator( rawSection );
        rawSection.addExpansionListener( new ExpansionAdapter()
        {
            public void expansionStateChanged( ExpansionEvent e )
            {
                detailForm.reflow( true );
            }
        } );
    }


    public void createRawContents( SchemaPart schemaPart )
    {

        if ( rawSection.getClient() != null && !rawSection.getClient().isDisposed() )
        {
            rawSection.getClient().dispose();
        }

        Composite client = toolkit.createComposite( rawSection, SWT.WRAP );
        client.setLayout( new GridLayout() );
        rawSection.setClient( client );

        if ( schemaPart != null )
        {
            rawText = toolkit.createText( client, getNonNullString( schemaPart.getLine().getValueAsString() ), SWT.WRAP
                | SWT.MULTI );
            GridData gd2 = new GridData( GridData.FILL_HORIZONTAL );
            gd2.widthHint = detailForm.getForm().getSize().x - 100 - 60;
            // detailForm.getForm().getVerticalBar().getSize().x
            // gd2.widthHint = 10;
            rawText.setLayoutData( gd2 );
            rawText.setEditable( false );
        }

        rawSection.layout();

    }


    protected String getNonNullString( String s )
    {
        return s == null ? "-" : s;
    }

}
