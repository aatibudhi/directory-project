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
import org.apache.kerberos.crypto.checksum.RsaMd5Checksum;

public class DesCbcMd5Encryption extends DesCbcEncryption
{
    public ChecksumEngine getChecksumEngine()
    {
        return new RsaMd5Checksum();
    }

    public EncryptionType encryptionType()
    {
        return EncryptionType.DES_CBC_MD5;
    }

    public ChecksumType checksumType()
    {
        return ChecksumType.RSA_MD5;
    }

    public int confounderSize()
    {
        return 8;
    }

    public int checksumSize()
    {
        return 16;
    }

    public int minimumPadSize()
    {
        return 0;
    }
}
