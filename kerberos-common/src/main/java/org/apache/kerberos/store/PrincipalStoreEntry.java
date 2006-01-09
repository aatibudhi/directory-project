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
package org.apache.kerberos.store;

import javax.security.auth.kerberos.KerberosPrincipal;

import org.apache.kerberos.crypto.encryption.EncryptionType;
import org.apache.kerberos.messages.value.EncryptionKey;
import org.apache.kerberos.messages.value.KerberosTime;
import org.apache.kerberos.messages.value.SamType;

public class PrincipalStoreEntry
{
    // principal
    private String commonName;
    private KerberosPrincipal principal;
    private String realmName;

    // uidObject
    private String userId;

    // KDCEntry
    private KerberosTime validStart;
    private KerberosTime validEnd;
    private KerberosTime passwordEnd;
    private int maxLife;
    private int maxRenew;
    private int kdcFlags;
    private SamType samType;
    private EncryptionKey key;

    PrincipalStoreEntry( String commonName, String userId, KerberosPrincipal principal, int keyVersionNumber,
            KerberosTime validStart, KerberosTime validEnd, KerberosTime passwordEnd, int maxLife, int maxRenew,
            int kdcFlags, int keyType, byte[] key, String realmName, SamType samType )
    {
        this.commonName = commonName;
        this.userId = userId;
        this.principal = principal;
        this.validStart = validStart;
        this.validEnd = validEnd;
        this.passwordEnd = passwordEnd;
        this.maxLife = maxLife;
        this.maxRenew = maxRenew;
        this.kdcFlags = kdcFlags;
        this.realmName = realmName;

        this.samType = samType;

        this.key = new EncryptionKey( EncryptionType.getTypeByOrdinal( keyType ), key, keyVersionNumber );
    }

    public String getCommonName()
    {
        return commonName;
    }

    public String getUserId()
    {
        return userId;
    }

    public EncryptionKey getEncryptionKey()
    {
        return key;
    }

    public int getKDCFlags()
    {
        return kdcFlags;
    }

    public int getMaxLife()
    {
        return maxLife;
    }

    public int getMaxRenew()
    {
        return maxRenew;
    }

    public KerberosTime getPasswordEnd()
    {
        return passwordEnd;
    }

    public KerberosPrincipal getPrincipal()
    {
        return principal;
    }

    public String getRealmName()
    {
        return realmName;
    }

    public KerberosTime getValidEnd()
    {
        return validEnd;
    }

    public KerberosTime getValidStart()
    {
        return validStart;
    }

    public SamType getSamType()
    {
        return samType;
    }
}
