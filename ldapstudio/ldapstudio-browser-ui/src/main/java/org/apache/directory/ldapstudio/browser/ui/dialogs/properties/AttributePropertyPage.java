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

package org.apache.directory.ldapstudio.browser.ui.dialogs.properties;


import java.util.Arrays;

import org.apache.directory.ldapstudio.browser.core.model.IAttribute;
import org.apache.directory.ldapstudio.browser.core.model.IValue;
import org.apache.directory.ldapstudio.browser.core.model.schema.AttributeTypeDescription;
import org.apache.directory.ldapstudio.browser.core.utils.Utils;
import org.apache.directory.ldapstudio.browser.ui.widgets.BaseWidgetUtils;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;


public class AttributePropertyPage extends PropertyPage implements IWorkbenchPropertyPage
{

    private Text attributeNameText;

    private Text attributeTypeText;

    private Text attributeValuesText;

    private Text attributeSizeText;

    private Text atdOidText;

    private Text atdNamesText;

    private Text atdDescText;

    private Text atdUsageText;

    private Button singleValuedFlag;

    private Button collectiveFlag;

    private Button obsoleteFlag;

    private Button noUserModificationFlag;

    private Text equalityMatchingRuleText;

    private Text substringMatchingRuleText;

    private Text orderingMatchingRuleText;

    private Text syntaxOidText;

    private Text syntaxDescText;

    private Text syntaxLengthText;


    public AttributePropertyPage()
    {
        super();
        super.noDefaultAndApplyButton();
    }


    protected Control createContents( Composite parent )
    {

        Composite composite = BaseWidgetUtils.createColumnContainer( parent, 1, 1 );

        Composite mainGroup = BaseWidgetUtils.createColumnContainer( composite, 2, 1 );

        BaseWidgetUtils.createLabel( mainGroup, "Description:", 1 );
        attributeNameText = BaseWidgetUtils.createLabeledText( mainGroup, "", 1 );

        BaseWidgetUtils.createLabel( mainGroup, "Type:", 1 );
        attributeTypeText = BaseWidgetUtils.createLabeledText( mainGroup, "", 1 );

        BaseWidgetUtils.createLabel( mainGroup, "Number of Values:", 1 );
        attributeValuesText = BaseWidgetUtils.createLabeledText( mainGroup, "", 1 );

        BaseWidgetUtils.createLabel( mainGroup, "Attribute Size:", 1 );
        attributeSizeText = BaseWidgetUtils.createLabeledText( mainGroup, "", 1 );

        Group atdGroup = BaseWidgetUtils.createGroup( composite, "Attribute Type", 1 );
        Composite atdComposite = BaseWidgetUtils.createColumnContainer( atdGroup, 2, 1 );

        BaseWidgetUtils.createLabel( atdComposite, "Numeric OID:", 1 );
        atdOidText = BaseWidgetUtils.createLabeledText( atdComposite, "", 1 );

        BaseWidgetUtils.createLabel( atdComposite, "Alternative Names:", 1 );
        atdNamesText = BaseWidgetUtils.createLabeledText( atdComposite, "", 1 );

        BaseWidgetUtils.createLabel( atdComposite, "Description:", 1 );
        atdDescText = BaseWidgetUtils.createWrappedLabeledText( atdComposite, "", 1 );

        BaseWidgetUtils.createLabel( atdComposite, "Usage:", 1 );
        atdUsageText = BaseWidgetUtils.createLabeledText( atdComposite, "", 1 );

        Group flagsGroup = BaseWidgetUtils.createGroup( composite, "Flags", 1 );
        Composite flagsComposite = BaseWidgetUtils.createColumnContainer( flagsGroup, 4, 1 );

        singleValuedFlag = BaseWidgetUtils.createCheckbox( flagsComposite, "Single valued", 1 );
        singleValuedFlag.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                singleValuedFlag.setSelection( !singleValuedFlag.getSelection() );
            }
        } );

        noUserModificationFlag = BaseWidgetUtils.createCheckbox( flagsComposite, "Read only", 1 );
        noUserModificationFlag.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                noUserModificationFlag.setSelection( !noUserModificationFlag.getSelection() );
            }
        } );

        collectiveFlag = BaseWidgetUtils.createCheckbox( flagsComposite, "Collective", 1 );
        collectiveFlag.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                collectiveFlag.setSelection( !collectiveFlag.getSelection() );
            }
        } );

        obsoleteFlag = BaseWidgetUtils.createCheckbox( flagsComposite, "Obsolete", 1 );
        obsoleteFlag.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                obsoleteFlag.setSelection( !obsoleteFlag.getSelection() );
            }
        } );

        Group syntaxGroup = BaseWidgetUtils.createGroup( composite, "Syntax", 1 );
        Composite syntaxComposite = BaseWidgetUtils.createColumnContainer( syntaxGroup, 2, 1 );

        BaseWidgetUtils.createLabel( syntaxComposite, "Syntax OID:", 1 );
        syntaxOidText = BaseWidgetUtils.createLabeledText( syntaxComposite, "", 1 );

        BaseWidgetUtils.createLabel( syntaxComposite, "Syntax Description:", 1 );
        syntaxDescText = BaseWidgetUtils.createLabeledText( syntaxComposite, "", 1 );

        BaseWidgetUtils.createLabel( syntaxComposite, "Syntax Length:", 1 );
        syntaxLengthText = BaseWidgetUtils.createLabeledText( syntaxComposite, "", 1 );

        Group matchingGroup = BaseWidgetUtils.createGroup( composite, "Matching Rules", 1 );
        Composite matchingComposite = BaseWidgetUtils.createColumnContainer( matchingGroup, 2, 1 );

        BaseWidgetUtils.createLabel( matchingComposite, "Equality Match:", 1 );
        equalityMatchingRuleText = BaseWidgetUtils.createLabeledText( matchingComposite, "", 1 );

        BaseWidgetUtils.createLabel( matchingComposite, "Substring Match:", 1 );
        substringMatchingRuleText = BaseWidgetUtils.createLabeledText( matchingComposite, "", 1 );

        BaseWidgetUtils.createLabel( matchingComposite, "Ordering Match:", 1 );
        orderingMatchingRuleText = BaseWidgetUtils.createLabeledText( matchingComposite, "", 1 );

        IAttribute attribute = getAttribute( getElement() );
        if ( attribute != null )
        {

            int bytes = 0;
            int valCount = 0;
            IValue[] allValues = attribute.getValues();
            for ( int valIndex = 0; valIndex < allValues.length; valIndex++ )
            {
                if ( !allValues[valIndex].isEmpty() )
                {
                    valCount++;
                    bytes += allValues[valIndex].getBinaryValue().length;
                }
            }

            this.setMessage( "Attribute " + attribute.getDescription() );
            attributeNameText.setText( attribute.getDescription() );
            attributeTypeText.setText( attribute.isString() ? "String" : "Binary" );
            attributeValuesText.setText( "" + valCount );
            attributeSizeText.setText( Utils.formatBytes( bytes ) );

            if ( attribute.getEntry().getConnection().getSchema().hasAttributeTypeDescription(
                attribute.getDescription() ) )
            {
                AttributeTypeDescription atd = attribute.getEntry().getConnection().getSchema()
                    .getAttributeTypeDescription( attribute.getDescription() );

                atdOidText.setText( atd.getNumericOID() );
                String atdNames = Arrays.asList( atd.getNames() ).toString();
                atdNamesText.setText( atdNames.substring( 1, atdNames.length() - 1 ) );
                atdDescText.setText( Utils.getNonNullString( atd.getDesc() ) );
                atdUsageText.setText( Utils.getNonNullString( atd.getUsage() ) );

                singleValuedFlag.setSelection( atd.isSingleValued() );
                noUserModificationFlag.setSelection( atd.isNoUserModification() );
                collectiveFlag.setSelection( atd.isCollective() );
                obsoleteFlag.setSelection( atd.isObsolete() );

                syntaxOidText.setText( Utils.getNonNullString( atd.getSyntaxDescriptionNumericOIDTransitive() ) );
                syntaxDescText.setText( Utils.getNonNullString( atd.getSyntaxDescription().getDesc() ) );
                syntaxLengthText.setText( Utils.getNonNullString( atd.getSyntaxDescriptionLengthTransitive() ) );

                equalityMatchingRuleText.setText( Utils.getNonNullString( atd
                    .getEqualityMatchingRuleDescriptionOIDTransitive() ) );
                substringMatchingRuleText.setText( Utils.getNonNullString( atd
                    .getSubstringMatchingRuleDescriptionOIDTransitive() ) );
                orderingMatchingRuleText.setText( Utils.getNonNullString( atd
                    .getOrderingMatchingRuleDescriptionOIDTransitive() ) );
            }
        }

        return parent;
    }


    private static IAttribute getAttribute( Object element )
    {
        IAttribute attribute = null;
        if ( element instanceof IAdaptable )
        {
            attribute = ( IAttribute ) ( ( IAdaptable ) element ).getAdapter( IAttribute.class );
        }
        return attribute;
    }

}
