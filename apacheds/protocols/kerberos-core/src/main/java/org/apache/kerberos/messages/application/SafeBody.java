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
package org.apache.kerberos.messages.application;

import org.apache.kerberos.messages.value.HostAddress;
import org.apache.kerberos.messages.value.KerberosTime;

public class SafeBody
{
    private byte[]       userData;
    private KerberosTime timestamp; //optional
    private Integer      usec; //optional
    private Integer      seqNumber; //optional
    private HostAddress  sAddress; //optional
    private HostAddress  rAddress; //optional

    /**
     * Class constructor
     */
    public SafeBody( byte[] userData, KerberosTime timestamp, Integer usec, Integer seqNumber,
            HostAddress sAddress, HostAddress rAddress )
    {
        this.userData  = userData;
        this.timestamp = timestamp;
        this.usec      = usec;
        this.seqNumber = seqNumber;
        this.sAddress  = sAddress;
        this.rAddress  = rAddress;
    }

    public HostAddress getRAddress()
    {
        return rAddress;
    }

    public HostAddress getSAddress()
    {
        return sAddress;
    }

    public Integer getSeqNumber()
    {
        return seqNumber;
    }

    public KerberosTime getTimestamp()
    {
        return timestamp;
    }

    public Integer getUsec()
    {
        return usec;
    }

    public byte[] getUserData()
    {
        return userData;
    }
}
