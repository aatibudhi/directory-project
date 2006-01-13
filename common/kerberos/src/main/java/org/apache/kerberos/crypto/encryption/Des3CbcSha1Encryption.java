/*
 *   Copyright 2004 The Apache Software Foundation
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
package org.apache.kerberos.crypto.encryption;

import org.apache.kerberos.crypto.checksum.ChecksumEngine;
import org.apache.kerberos.crypto.checksum.ChecksumType;
import org.apache.kerberos.crypto.checksum.Sha1Checksum;

public class Des3CbcSha1Encryption extends Des3CbcEncryption
{
    public ChecksumEngine getChecksumEngine()
    {
        return new Sha1Checksum();
    }

    public EncryptionType encryptionType()
    {
        return EncryptionType.DES3_CBC_SHA1;
    }

    public ChecksumType checksumType()
    {
        return ChecksumType.SHA1;
    }

    public int confounderSize()
    {
        return 8;
    }

    public int checksumSize()
    {
        return 20;
    }

    public int minimumPadSize()
    {
        return 0;
    }
}
