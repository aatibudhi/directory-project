package org.apache.ldap.common.asn1.pojo.psearch;

import org.apache.asn1.ber.tlv.Length;
import org.apache.asn1.ber.tlv.Value;


public class PSearchControl
{
    /** 
     * If changesOnly is TRUE, the server MUST NOT return any existing
     * entries that match the search criteria.  Entries are only
     * returned when they are changed (added, modified, deleted, or 
     * subject to a modifyDN operation).
     */
    private boolean changesOnly;

    /**
     * If returnECs is TRUE, the server MUST return an Entry Change 
     * Notification control with each entry returned as the result of
     * changes.
     */
    private boolean returnECs;
    
    /**
     * As changes are made to the server, the effected entries MUST be
     * returned to the client if they match the standard search cri-
     * teria and if the operation that caused the change is included in
     * the changeTypes field.  The changeTypes field is the logical OR
     * of one or more of these values: add (1), delete (2), modify (4),
     * modDN (8).
     */
    private int changeTypes;


    /**
     * Compute the PSearchControl length
     */
    public int computeLength()
    {
        int changeTypesLength = 2 + Value.getNbBytes( changeTypes );
        return  1 + Length.getNbBytes( changeTypesLength + 6 ) +
                changeTypesLength + // for changeTypes 
                6;                  // for two boolean values 
    }

    
    public void setChangesOnly( boolean changesOnly )
    {
        this.changesOnly = changesOnly;
    }

    
    public boolean isChangesOnly()
    {
        return changesOnly;
    }


    public void setReturnECs( boolean returnECs )
    {
        this.returnECs = returnECs;
    }


    public boolean isReturnECs()
    {
        return returnECs;
    }


    public void setChangeTypes( int changeTypes )
    {
        this.changeTypes = changeTypes;
    }


    public int getChangeTypes()
    {
        return changeTypes;
    }
}
