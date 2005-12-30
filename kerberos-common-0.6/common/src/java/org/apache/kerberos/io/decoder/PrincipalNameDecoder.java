/*
 *   Copyright 2005 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.kerberos.io.decoder;

import java.util.Enumeration;

import org.apache.asn1.der.DEREncodable;
import org.apache.asn1.der.DERGeneralString;
import org.apache.asn1.der.DERInteger;
import org.apache.asn1.der.DERSequence;
import org.apache.asn1.der.DERTaggedObject;
import org.apache.kerberos.messages.value.PrincipalName;
import org.apache.kerberos.messages.value.PrincipalNameModifier;

public class PrincipalNameDecoder
{
    /**
     * PrincipalName ::=   SEQUENCE {
     *               name-type[0]     INTEGER,
     *               name-string[1]   SEQUENCE OF GeneralString
     * }
     */
    public static PrincipalName decode( DERSequence sequence )
    {
        PrincipalNameModifier modifier = new PrincipalNameModifier();

        for ( Enumeration e = sequence.getObjects(); e.hasMoreElements(); )
        {
            DERTaggedObject object = (DERTaggedObject) e.nextElement();
            int tag = object.getTagNo();
            DEREncodable derObject = object.getObject();

            switch ( tag )
            {
                case 0:
                    DERInteger nameType = (DERInteger) derObject;
                    modifier.setType( nameType.intValue() );
                    break;
                case 1:
                    DERSequence nameString = (DERSequence) derObject;
                    decodeNameString( nameString, modifier );
                    break;
            }
        }

        return modifier.getPrincipalName();
    }

    private static void decodeNameString( DERSequence sequence, PrincipalNameModifier modifier )
    {
        for ( Enumeration e = sequence.getObjects(); e.hasMoreElements(); )
        {
            DERGeneralString object = (DERGeneralString) e.nextElement();
            modifier.addName( object.getString() );
        }
    }
}
