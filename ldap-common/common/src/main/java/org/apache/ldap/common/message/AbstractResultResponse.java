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
package org.apache.ldap.common.message;


/**
 * Abstract base for a Lockable ResultResponse message.
 * 
 * @author <a href="mailto:aok123@bellsouth.net">Alex Karasulu</a>
 * @author $Author: akarasulu $
 * @version $Revision$
 */
public abstract class AbstractResultResponse
    extends AbstractResponse implements ResultResponse
{
    /** Response result components */
    private LdapResult result;


    // ------------------------------------------------------------------------
    // Response Interface Method Implementations
    // ------------------------------------------------------------------------


    /**
     * Allows subclasses based on the abstract type to create a response to a
     * request.
     *
     * @param id the response eliciting this Request
     * @param type the message type of the response
     */
    protected AbstractResultResponse( final int id,
        final MessageTypeEnum type )
    {
        super( id, type );
    }


    // ------------------------------------------------------------------------
    // Response Interface Method Implementations
    // ------------------------------------------------------------------------


    /**
     * Gets the LdapResult components of this Response.
     *
     * @return the LdapResult for this Response.
     */
    public LdapResult getLdapResult()
    {
        return result;
    }


    /**
     * Sets the LdapResult components of this Response.
     *
     * @param ldapResult the LdapResult for this Response.
     */
    public void setLdapResult( LdapResult ldapResult )
    {
        lockCheck( "Attempt to alter the LdapResult for a locked Response!" );
        result = ldapResult;
    }


    /**
     * Checks to see if an object is equal to this AbstractResultResponse.
     * First the object is checked to see if it is this AbstractResultResponse
     * instance if so it returns true.  Next it checks if the super method
     * returns false and if it does false is returned.  It then checks if the
     * LDAPResult's are equal.  If not false is returned and if they match
     * true is returned.
     *
     * @param obj the object to compare to this LdapResult containing response
     * @return true if they objects are equivalent false otherwise
     */
    public boolean equals( Object obj )
    {
        if ( obj == this )
        {
            return true;
        }

        if ( ! super.equals( obj ) )
        {
            return false;
        }

        if ( ! ( obj instanceof ResultResponse ) )
        {
            return false;
        }

        ResultResponse resp = ( ResultResponse ) obj;

        if ( getLdapResult() != null && resp.getLdapResult() == null )
        {
            return false;
        }

        if ( getLdapResult() == null && resp.getLdapResult() != null )
        {
            return false;
        }

        if ( getLdapResult() != null && resp.getLdapResult() != null )
        {
            if ( ! getLdapResult().equals( resp.getLdapResult() ) )
            {
                return false;
            }
        }

        return true;
    }
    
    /**
     * Get a String representation of an Response
     *
     * @return An Response String 
     */
    public String toString()
    {
    	if ( result != null )
    	{
    		return result.toString();
    	}
    	else
    	{
    		return "No result";
    	}
    }
}
