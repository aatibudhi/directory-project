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
package org.apache.kerberos.crypto.checksum;

import org.apache.kerberos.crypto.encryption.CipherType;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;

public class Sha1Checksum extends ChecksumEngine
{
    public Digest getDigest()
    {
        return new SHA1Digest();
    }

    public ChecksumType checksumType()
    {
        return ChecksumType.SHA1;
    }

    public CipherType keyType()
    {
        return CipherType.NULL;
    }

    public int checksumSize()
    {
        return 20;
    }

    public int keySize()
    {
        return 0;
    }

    public int confounderSize()
    {
        return 0;
    }

    public boolean isSafe()
    {
        return false;
    }

    public byte[] calculateKeyedChecksum( byte[] data, byte[] key )
    {
        return null;
    }

    public boolean verifyKeyedChecksum( byte[] data, byte[] key, byte[] checksum )
    {
        return false;
    }
}
