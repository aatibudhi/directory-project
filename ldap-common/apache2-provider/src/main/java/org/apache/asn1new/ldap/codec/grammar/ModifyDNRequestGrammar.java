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
package org.apache.asn1new.ldap.codec.grammar;

import javax.naming.InvalidNameException;

import org.apache.asn1.codec.DecoderException;
import org.apache.asn1new.ber.containers.IAsn1Container;
import org.apache.asn1new.ber.grammar.AbstractGrammar;
import org.apache.asn1new.ber.grammar.GrammarAction;
import org.apache.asn1new.ber.grammar.GrammarTransition;
import org.apache.asn1new.ber.grammar.IGrammar;
import org.apache.asn1new.ber.tlv.TLV;
import org.apache.asn1new.ber.tlv.UniversalTag;
import org.apache.asn1new.ber.tlv.Value;
import org.apache.asn1new.util.BooleanDecoder;
import org.apache.asn1new.util.BooleanDecoderException;
import org.apache.asn1new.ldap.codec.LdapConstants;
import org.apache.asn1new.ldap.codec.LdapMessageContainer;
import org.apache.asn1new.ldap.codec.primitives.LdapDN;
import org.apache.asn1new.ldap.codec.primitives.LdapRDN;
import org.apache.asn1new.ldap.pojo.LdapMessage;
import org.apache.asn1new.ldap.pojo.ModifyDNRequest;
import org.apache.ldap.common.util.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class implements the ModifyDNRequest LDAP message. All the actions are declared in this
 * class. As it is a singleton, these declaration are only done once.
 * 
 * If an action is to be added or modified, this is where the work is to be done !
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class ModifyDNRequestGrammar extends AbstractGrammar implements IGrammar
{
    //~ Static fields/initializers -----------------------------------------------------------------

    /** The logger */
    private static final Logger log = LoggerFactory.getLogger( ModifyDNRequestGrammar.class );

    /** The instance of grammar. ModifyDNRequestGrammar is a singleton */
    private static IGrammar instance = new ModifyDNRequestGrammar();

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Creates a new ModifyDNRequestGrammar object.
     */
    private ModifyDNRequestGrammar()
    {
        name              = ModifyDNRequestGrammar.class.getName();
        statesEnum        = LdapStatesEnum.getInstance();

        // Create the transitions table
        super.transitions = new GrammarTransition[LdapStatesEnum.LAST_MODIFY_DN_REQUEST_STATE][256];

        //============================================================================================
        // ModifyDNRequest
        //============================================================================================
        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE { (Tag)
        // Nothing to do
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_TAG][LdapConstants.MODIFY_DN_REQUEST_TAG] = new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_TAG, LdapStatesEnum.MODIFY_DN_REQUEST_VALUE,
                null );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE { (Value)
        // Initialise the modify DN request pojo
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_VALUE][LdapConstants.MODIFY_DN_REQUEST_TAG] = new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_VALUE, LdapStatesEnum.MODIFY_DN_REQUEST_ENTRY_TAG,
                new GrammarAction( "Init Modify DN Request" )
                {
                    public void action( IAsn1Container container ) throws DecoderException
                    {

                        LdapMessageContainer ldapMessageContainer = ( LdapMessageContainer )
                            container;
                        LdapMessage          ldapMessage          =
                            ldapMessageContainer.getLdapMessage();

                        // Now, we can allocate the ModifyDNRequest Object
                        // And we associate it to the ldapMessage Object
                        ldapMessage.setProtocolOP( new ModifyDNRequest() );

                    }
                } );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    entry           LDAPDN, (Tag)
        //    ...
        // The tag is 0x04. Nothing to do
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_ENTRY_TAG][UniversalTag.OCTET_STRING_TAG] =
            new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_ENTRY_TAG,
                LdapStatesEnum.MODIFY_DN_REQUEST_ENTRY_VALUE, null );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    entry           LDAPDN, (Value)
        //    ...
        // We store the entry. It must not be null.
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_ENTRY_VALUE][UniversalTag.OCTET_STRING_TAG] =
            new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_ENTRY_VALUE,
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_RDN_TAG,
                new GrammarAction( "Store entry" )
                {
                    public void action( IAsn1Container container ) throws DecoderException
                    {

                        LdapMessageContainer ldapMessageContainer = ( LdapMessageContainer )
                            container;
                        LdapMessage          ldapMessage          =
                            ldapMessageContainer.getLdapMessage();

                        ModifyDNRequest      modifyDNRequest = ldapMessage.getModifyDNRequest();

                        // Get the Value and store it in the modifyDNRequest
                        TLV tlv = ldapMessageContainer.getCurrentTLV();

                        // We have to handle the special case of a 0 length matched DN
                        LdapDN entry = null;

                        if ( tlv.getLength().getLength() == 0 )
                        {
                            throw new DecoderException( "The entry must nut be null" );
                        }
                        else
                        {
                            try
                            {
                                entry = new LdapDN( tlv.getValue().getData() );
                                modifyDNRequest.setEntry( entry );
                            }
                            catch ( InvalidNameException ine )
                            {
                                log.error( "The DN to modify  (" + StringTools.dumpBytes( tlv.getValue().getData() ) + ") is invalid" );
                                throw new DecoderException( "Invalid DN " + StringTools.dumpBytes( tlv.getValue().getData() ) + ", : " + ine.getMessage() );
                            }
                        }

                        if ( log.isDebugEnabled() )
                        {
                            log.debug( "Modifying DN " + entry );
                        }

                        return;
                    }
                } );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    ...
        //    newrdn          RelativeLDAPDN, (Tag)
        //    ...
        // The tag is 0x04. A RelativeLDAPDN is a component of a
        // LDAPDN. Nothing to do
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_NEW_RDN_TAG][UniversalTag.OCTET_STRING_TAG] =
            new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_RDN_TAG,
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_RDN_VALUE, null );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    ...
        //    newrdn          RelativeLDAPDN, (Value)
        //    ...
        // A RelativeLDAPDN is a component of a LDAPDN. We have to check that it is
        // valid.
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_NEW_RDN_VALUE][UniversalTag.OCTET_STRING_TAG] =
            new GrammarTransition( LdapStatesEnum.MODIFY_DN_REQUEST_NEW_RDN_VALUE,
                LdapStatesEnum.MODIFY_DN_REQUEST_DELETE_OLD_RDN_TAG,
                new GrammarAction( "Store new RDN" )
                {
                    public void action( IAsn1Container container ) throws DecoderException
                    {

                        LdapMessageContainer ldapMessageContainer = ( LdapMessageContainer )
                            container;
                        LdapMessage          ldapMessage          =
                            ldapMessageContainer.getLdapMessage();

                        ModifyDNRequest      modifyDNRequest = ldapMessage.getModifyDNRequest();

                        // Get the Value and store it in the modifyDNRequest
                        TLV tlv = ldapMessageContainer.getCurrentTLV();

                        // We have to handle the special case of a 0 length matched newDN
                        LdapRDN newRdn = null;
                        
                        if ( tlv.getLength().getLength() == 0 )
                        {
                            throw new DecoderException( "The newrdn must nut be null" );
                        }
                        else
                        {
                            try
                            {
                                newRdn = new LdapRDN( tlv.getValue().getData() );
                                modifyDNRequest.setNewRDN( newRdn );
                            }
                            catch ( InvalidNameException ine )
                            {
                                log.error( "The new RDN (" + StringTools.dumpBytes( tlv.getValue().getData() ) + ") is invalid" );
                                throw new DecoderException( "Invalid RDN " + StringTools.dumpBytes( tlv.getValue().getData() ) + ", : " + ine.getMessage() );
                            }
                        }

                        if ( log.isDebugEnabled() )
                        {
                            log.debug( "Modifying with new RDN " + newRdn );
                        }
                    }
                } );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    ...
        //    deleteoldrdn    BOOLEAN, (Tag)
        //    ...
        // Nothing to do.
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_DELETE_OLD_RDN_TAG][UniversalTag.BOOLEAN_TAG] =
            new GrammarTransition( LdapStatesEnum.MODIFY_DN_REQUEST_DELETE_OLD_RDN_TAG,
                LdapStatesEnum.MODIFY_DN_REQUEST_DELETE_OLD_RDN_VALUE, null );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    ...
        //    deleteoldrdn    BOOLEAN, (Value)
        //    ...
        // Store the flag
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_DELETE_OLD_RDN_VALUE][UniversalTag.BOOLEAN_TAG] =
            new GrammarTransition( LdapStatesEnum.MODIFY_DN_REQUEST_DELETE_OLD_RDN_VALUE,
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_SUPERIOR_TAG,
                new GrammarAction( "Store matching dnAttributes Value" )
                {
                    public void action( IAsn1Container container ) throws DecoderException
                    {

                        LdapMessageContainer ldapMessageContainer = ( LdapMessageContainer )
                            container;
                        LdapMessage          ldapMessage          =
                            ldapMessageContainer.getLdapMessage();

                        TLV                  tlv = ldapMessageContainer.getCurrentTLV();

                        // Store the value.
                        ModifyDNRequest modifyDNRequest = ldapMessage.getModifyDNRequest();

                        // We get the value. If it's a 0, it's a FALSE. If it's
                        // a FF, it's a TRUE. Any other value should be an error,
                        // but we could relax this constraint. So if we have something
                        // which is not 0, it will be interpreted as TRUE, but we
                        // will generate a warning.
                        Value value        = tlv.getValue();

                        try
                        {
                            modifyDNRequest.setDeleteOldRDN( BooleanDecoder.parse( value ) );
                        }
                        catch ( BooleanDecoderException bde )
                        {
                            log.error("The oldRDN flag " + StringTools.dumpBytes( value.getData() ) + 
                                    " is invalid : " + bde.getMessage() + ". It should be 0 or 255" );
                        
                            throw new DecoderException( bde.getMessage() );
                        }

                        if ( log.isDebugEnabled() )
                        {
                            if ( modifyDNRequest.isDeleteOldRDN() )
                            {
                                log.debug( " Old RDN attributes will be deleted" );
                            }
                            else
                            {
                                log.debug( " Old RDN attributes will be retained" );
                            }
                        }
                    }
                } );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    ...
        //    newSuperior     [0] LDAPDN OPTIONAL } (Tag)
        // This is an optionnal element. Nothing to do
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_NEW_SUPERIOR_TAG][LdapConstants.MODIFY_DN_REQUEST_NEW_SUPERIOR_TAG] =
            new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_SUPERIOR_TAG,
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_SUPERIOR_VALUE, null );

        // ModifyDNRequest ::= [APPLICATION 12] SEQUENCE {
        //    ...
        //    newSuperior     [0] LDAPDN OPTIONAL } (Tag)
        // We store the new superior. It may be null, but in this case,
        // we will consider that it's just a change of the entry, except
        // if the deleteOldRDN flag is set to true, in which case this is
        // an error.
        super.transitions[LdapStatesEnum.MODIFY_DN_REQUEST_NEW_SUPERIOR_VALUE][LdapConstants.MODIFY_DN_REQUEST_NEW_SUPERIOR_TAG] =
            new GrammarTransition(
                LdapStatesEnum.MODIFY_DN_REQUEST_NEW_SUPERIOR_VALUE, LdapStatesEnum.GRAMMAR_END,
                new GrammarAction( "Store new superior" )
                {
                    public void action( IAsn1Container container ) throws DecoderException
                    {

                        LdapMessageContainer ldapMessageContainer = ( LdapMessageContainer )
                            container;
                        LdapMessage          ldapMessage          =
                            ldapMessageContainer.getLdapMessage();

                        ModifyDNRequest      modifyDNRequest = ldapMessage.getModifyDNRequest();

                        // Get the Value and store it in the modifyDNRequest
                        TLV tlv = ldapMessageContainer.getCurrentTLV();

                        // We have to handle the special case of a 0 length matched DN
                        LdapDN newSuperior = LdapDN.EMPTY_LDAPDN;

                        if ( tlv.getLength().getLength() == 0 )
                        {

                            if ( modifyDNRequest.isDeleteOldRDN() )
                            {
                                throw new DecoderException(
                                    "The new superior must nut be null if the flag 'delete old DN' is set to true" );
                            }
                            else
                            {
                                log.warn(
                                    "The new superior is null, so we will change the entry" );
                            }

                            modifyDNRequest.setNewSuperior( newSuperior );
                        }
                        else
                        {
                            try
                            {
                                newSuperior = new LdapDN( tlv.getValue().getData() );
                                modifyDNRequest.setNewSuperior( newSuperior );
                            }
                            catch ( InvalidNameException ine )
                            {
                                log.error( "The new superior DN (" + StringTools.dumpBytes( tlv.getValue().getData() ) + ") is invalid" );
                                throw new DecoderException( "Invalid DN " + StringTools.dumpBytes( tlv.getValue().getData() ) + ", : " + ine.getMessage() );
                            }
                        }

                        if ( log.isDebugEnabled() )
                        {
                            log.debug( "New superior DN " + newSuperior );
                        }
                        
                        return;
                    }
                } );

    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * This class is a singleton.
     *
     * @return An instance on this grammar
     */
    public static IGrammar getInstance()
    {
        return instance;
    }
}
