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


import org.apache.directory.ldapstudio.browser.core.model.IValue;
import org.apache.directory.ldapstudio.browser.ui.dialogs.ImageDialog;
import org.apache.directory.ldapstudio.browser.ui.dialogs.TextDialog;
import org.apache.directory.ldapstudio.browser.ui.valueeditors.AbstractDialogBinaryValueEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;


/**
 * Implementation of IValueEditor for syntax 1.3.6.1.4.1.1466.115.121.1.28 
 * (JPEG). 
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ImageValueEditor extends AbstractDialogBinaryValueEditor
{

    /**
     * This implementation opens the ImageDialog.
     */
    protected boolean openDialog( Shell shell )
    {
        Object value = getValue();

        if ( value != null && value instanceof byte[] )
        {
            byte[] currentImageData = ( byte[] ) value;

            ImageDialog dialog = new ImageDialog( shell, currentImageData, SWT.IMAGE_JPEG );
            if ( dialog.open() == TextDialog.OK && dialog.getNewImageRawData() != null )
            {
                setValue( dialog.getNewImageRawData() );
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the image info text created by 
     * ImageDialog.getImageInfo().
     */
    public String getDisplayValue( IValue value )
    {
        if ( showRawValues() )
        {
            return getPrintableString( value );
        }
        else
        {
            if ( value == null )
            {
                return "NULL";
            }
            else if ( value.isBinary() )
            {
                byte[] data = value.getBinaryValue();
                String text = ImageDialog.getImageInfo( data );
                return text;
            }
            else
            {
                return "Invalid Image Data";
            }
        }
    }

}
