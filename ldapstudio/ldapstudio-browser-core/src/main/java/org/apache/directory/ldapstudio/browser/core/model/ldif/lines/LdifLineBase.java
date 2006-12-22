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

package org.apache.directory.ldapstudio.browser.core.model.ldif.lines;


import org.apache.directory.ldapstudio.browser.core.BrowserCoreConstants;
import org.apache.directory.ldapstudio.browser.core.BrowserCorePlugin;
import org.apache.directory.ldapstudio.browser.core.model.ldif.LdifPart;
import org.eclipse.core.runtime.Preferences;


/**
 * Base class for all lines in a LDIF file.
 * 
 * 
 */
public abstract class LdifLineBase implements LdifPart
{

    private String rawNewLine;

    private int offset;


    protected LdifLineBase()
    {
    }


    protected LdifLineBase( int offset, String rawNewLine )
    {
        super();
        this.rawNewLine = rawNewLine;
        this.offset = offset;
    }


    public final String getRawNewLine()
    {
        return getNonNull( this.rawNewLine );
    }


    public String getUnfoldedNewLine()
    {
        return unfold( this.getRawNewLine() );
    }


    public final void adjustOffset( int adjust )
    {
        this.offset += adjust;
    }


    public final int getOffset()
    {
        return this.offset;
    }


    public final int getLength()
    {
        return this.toRawString().length();
    }


    public boolean isValid()
    {
        return this.rawNewLine != null;
    }


    public String getInvalidString()
    {
        if ( this.rawNewLine == null )
        {
            return "Missing new line";
        }
        else
        {
            return null;
        }
    }


    public String toRawString()
    {
        return this.getRawNewLine();
    }


    public final String toFormattedString()
    {

        Preferences store = BrowserCorePlugin.getDefault().getPluginPreferences();
        boolean spaceAfterColon = store.getBoolean( BrowserCoreConstants.PREFERENCE_LDIF_SPACE_AFTER_COLON );
        String lineSeparator = store.getString( BrowserCoreConstants.PREFERENCE_LDIF_LINE_SEPARATOR );

        String raw = toRawString();
        String unfolded = unfold( raw );

        if ( this instanceof LdifValueLineBase )
        {
            if ( unfolded.indexOf( "::" ) > -1 )
            {
                unfolded = unfolded.replaceFirst( "::[ ]*", spaceAfterColon ? ":: " : "::" );
            }
            else if ( unfolded.indexOf( ":<" ) > -1 )
            {
                unfolded = unfolded.replaceFirst( ":<[ ]*", spaceAfterColon ? ":< " : ":<" );
            }
            else if ( unfolded.indexOf( ":" ) > -1 )
            {
                unfolded = unfolded.replaceFirst( ":[ ]*", spaceAfterColon ? ": " : ":" );
            }
        }

        if ( rawNewLine != null )
        {
            int index = unfolded.lastIndexOf( rawNewLine );
            if ( index > -1 )
            {
                unfolded = unfolded.substring( 0, unfolded.length() - rawNewLine.length() );
                unfolded = unfolded + lineSeparator;
            }
        }

        if ( this instanceof LdifValueLineBase )
        {
            return fold( unfolded, 0 );
        }
        else
        {
            return unfolded;
        }
    }


    public final String toString()
    {
        String text = toRawString();
        text = text.replaceAll( "\n", "\\\\n" );
        text = text.replaceAll( "\r", "\\\\r" );
        return getClass().getName() + " (" + getOffset() + "," + getLength() + "): '" + text + "'";
    }


    protected static String getNonNull( String s )
    {
        return s != null ? s : "";
    }


    protected static String unfold( String s )
    {
        s = s.replaceAll( "\n\r ", "" );
        s = s.replaceAll( "\r\n ", "" );
        s = s.replaceAll( "\n ", "" );
        s = s.replaceAll( "\r ", "" );
        return s;
    }


    protected static String fold( String value, int indent )
    {
        Preferences store = BrowserCorePlugin.getDefault().getPluginPreferences();
        int lineWidth = store.getInt( BrowserCoreConstants.PREFERENCE_LDIF_LINE_WIDTH );
        String lineSeparator = store.getString( BrowserCoreConstants.PREFERENCE_LDIF_LINE_SEPARATOR );

        StringBuffer formattedLdif = new StringBuffer();
        int offset = lineWidth - indent;
        int endIndex = 0 + offset;
        while ( endIndex + lineSeparator.length() < value.length() )
        {
            formattedLdif.append( value.substring( endIndex - offset, endIndex ) );
            formattedLdif.append( lineSeparator );
            formattedLdif.append( ' ' );
            offset = lineWidth - 1;
            endIndex += offset;
        }
        String rest = value.substring( endIndex - offset, value.length() );
        formattedLdif.append( rest );

        // return
        return formattedLdif.toString();
    }

}
