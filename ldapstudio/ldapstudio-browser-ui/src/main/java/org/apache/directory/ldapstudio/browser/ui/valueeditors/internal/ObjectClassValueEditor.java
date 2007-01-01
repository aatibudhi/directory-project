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

package org.apache.directory.ldapstudio.browser.ui.valueeditors.internal;


import org.apache.directory.ldapstudio.browser.core.model.AttributeHierarchy;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.IValue;
import org.apache.directory.ldapstudio.browser.core.model.schema.ObjectClassDescription;
import org.apache.directory.ldapstudio.browser.core.model.schema.Schema;
import org.apache.directory.ldapstudio.browser.ui.dialogs.ObjectClassDialog;
import org.apache.directory.ldapstudio.browser.ui.dialogs.TextDialog;
import org.apache.directory.ldapstudio.browser.ui.valueeditors.AbstractDialogStringValueEditor;
import org.eclipse.swt.widgets.Shell;


/**
 * Implementation of IValueEditor for attribute objectClass.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ObjectClassValueEditor extends AbstractDialogStringValueEditor
{

    /**
     * This implementation opens the ObjectClassDialog.
     */
    public boolean openDialog( Shell shell )
    {
        Object value = getValue();
        if ( value != null && value instanceof ObjectClassValueEditorRawValueWrapper )
        {
            ObjectClassValueEditorRawValueWrapper wrapper = ( ObjectClassValueEditorRawValueWrapper ) value;
            ObjectClassDialog dialog = new ObjectClassDialog( shell, wrapper.schema, wrapper.objectClass );
            if ( dialog.open() == TextDialog.OK && !"".equals( dialog.getObjectClass() ) )
            {
                setValue( dialog.getObjectClass() );
                return true;
            }
        }
        return false;
    }


    /**
     * This implementation appends the kind of object class,
     * on of structural, abstract, auxiliary or obsolete. 
     * 
     * Reimplementation, because getRawValue() returns a 
     * ObjectClassValueEditorRawValueWrapper.
     */
    public String getDisplayValue( IValue value )
    {
        if ( getRawValue( value ) == null )
        {
            return "NULL";
        }

        String displayValue = value.getStringValue();

        if ( !showRawValues() )
        {
            Schema schema = value.getAttribute().getEntry().getConnection().getSchema();
            ObjectClassDescription ocd = schema.getObjectClassDescription( displayValue );
            if ( ocd.isStructural() )
            {
                displayValue = displayValue + " (structural)";
            }
            else if ( ocd.isAbstract() )
            {
                displayValue = displayValue + " (abstract)";
            }
            else if ( ocd.isAuxiliary() )
            {
                displayValue = displayValue + " (auxiliary)";
            }
            else if ( ocd.isObsolete() )
            {
                displayValue = displayValue + " (obsolete)";
            }
        }

        return displayValue;
    }


    /**
     * Returns null.
     * Modification in search result editor not supported.
     */
    public Object getRawValue( AttributeHierarchy attributeHierarchy )
    {
        return null;
    }


    /**
     * Returns a ObjectClassValueEditorRawValueWrapper.
     */
    public Object getRawValue( IValue value )
    {
        if ( value == null || !value.isString() || !value.getAttribute().isObjectClassAttribute() )
        {
            return null;
        }
        else
        {
            return getRawValue( value.getAttribute().getEntry().getConnection(), value.getStringValue() );
        }
    }


    /**
     * Returns a ObjectClassValueEditorRawValueWrapper.
     */
    public Object getRawValue( IConnection connection, Object value )
    {
        Schema schema = null;
        if ( connection != null )
        {
            schema = connection.getSchema();
        }
        if ( schema == null || value == null || !( value instanceof String ) )
        {
            return null;
        }

        String ocValue = ( String ) value;
        ObjectClassValueEditorRawValueWrapper wrapper = new ObjectClassValueEditorRawValueWrapper( schema, ocValue );
        return wrapper;
    }

    /**
     * The ObjectClassValueEditorRawValueWrapper is used to pass contextual 
     * information to the opened ObjectClassDialog.
     *
     * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
     * @version $Rev$, $Date$
     */
    private class ObjectClassValueEditorRawValueWrapper
    {
        /** 
         * The schema, used in ObjectClassDialog to build the list
         * with possible object classes.
         */
        private Schema schema;

        /** The object class, used as initial value in ObjectClassDialog. */
        private String objectClass;


        /**
         * Creates a new instance of ObjectClassValueEditorRawValueWrapper.
         *
         * @param schema the schema
         * @param objectClass the object class
         */
        private ObjectClassValueEditorRawValueWrapper( Schema schema, String objectClass )
        {
            super();
            this.schema = schema;
            this.objectClass = objectClass;
        }
    }

}
