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
import org.apache.asn1.der.DERInteger;
import org.apache.asn1.der.DEROctetString;
import org.apache.asn1.der.DERSequence;
import org.apache.asn1.der.DERTaggedObject;
import org.apache.kerberos.crypto.encryption.EncryptionType;
import org.apache.kerberos.messages.value.EncryptionKey;

public class EncryptionKeyDecoder
{
	/**
     * EncryptionKey ::=   SEQUENCE {
     *     keytype[0]    INTEGER,
     *     keyvalue[1]   OCTET STRING
     * }
     */
	protected static EncryptionKey decode( DERSequence sequence )
    {
        EncryptionType type = EncryptionType.NULL;
        byte[] data = null;

        for ( Enumeration e = sequence.getObjects(); e.hasMoreElements(); )
        {
            DERTaggedObject object = (DERTaggedObject) e.nextElement();
            int tag = object.getTagNo();
            DEREncodable derObject = object.getObject();

            switch ( tag )
            {
                case 0:
                    DERInteger tag0 = (DERInteger) derObject;
                    type = EncryptionType.getTypeByOrdinal( tag0.intValue() );
                    break;
                case 1:
                    DEROctetString tag1 = (DEROctetString) derObject;
                    data = tag1.getOctets();
                    break;
            }
        }

        return new EncryptionKey( type, data );
    }
}
