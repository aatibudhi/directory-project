/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */

package org.apache.directory.ldapstudio.browser.core.jobs;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.directory.ldapstudio.browser.core.BrowserCoreMessages;
import org.apache.directory.ldapstudio.browser.core.model.IConnection;
import org.apache.directory.ldapstudio.browser.core.model.ISearch;
import org.apache.directory.ldapstudio.browser.core.model.SearchParameter;
import org.apache.directory.ldapstudio.dsmlv2.engine.Dsmlv2Engine;
import org.apache.directory.ldapstudio.dsmlv2.request.SearchRequestDsml;
import org.apache.directory.shared.asn1.codec.DecoderException;
import org.apache.directory.shared.ldap.codec.AttributeValueAssertion;
import org.apache.directory.shared.ldap.codec.LdapConstants;
import org.apache.directory.shared.ldap.codec.search.AndFilter;
import org.apache.directory.shared.ldap.codec.search.AttributeValueAssertionFilter;
import org.apache.directory.shared.ldap.codec.search.ExtensibleMatchFilter;
import org.apache.directory.shared.ldap.codec.search.Filter;
import org.apache.directory.shared.ldap.codec.search.NotFilter;
import org.apache.directory.shared.ldap.codec.search.OrFilter;
import org.apache.directory.shared.ldap.codec.search.PresentFilter;
import org.apache.directory.shared.ldap.codec.search.SearchRequest;
import org.apache.directory.shared.ldap.codec.search.SubstringFilter;
import org.apache.directory.shared.ldap.filter.BranchNode;
import org.apache.directory.shared.ldap.filter.ExprNode;
import org.apache.directory.shared.ldap.filter.ExtensibleNode;
import org.apache.directory.shared.ldap.filter.FilterParser;
import org.apache.directory.shared.ldap.filter.FilterParserImpl;
import org.apache.directory.shared.ldap.filter.PresenceNode;
import org.apache.directory.shared.ldap.filter.SimpleNode;
import org.apache.directory.shared.ldap.filter.SubstringNode;
import org.apache.directory.shared.ldap.message.ScopeEnum;
import org.apache.directory.shared.ldap.name.LdapDN;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * This class implements a Job for Exporting a part of a LDAP Server into a DSML File.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ExportDsmlJob extends AbstractEclipseJob
{
    /** The name of the DSML file to export to */
    private String exportDsmlFilename;

    /** The connection to use */
    private IConnection connection;

    /** The Search Parameter of the export*/
    private SearchParameter searchParameter;


    /**
     * Creates a new instance of ExportDsmlJob.
     *
     * @param exportDsmlFilename
     *          the name of the DSML file to export to
     * @param connection
     *          the connection to use
     * @param searchParameter
     *          the Search Parameter of the export
     */
    public ExportDsmlJob( String exportDsmlFilename, IConnection connection, SearchParameter searchParameter )
    {
        this.exportDsmlFilename = exportDsmlFilename;
        this.connection = connection;
        this.searchParameter = searchParameter;

        setName( BrowserCoreMessages.jobs__export_dsml_name );
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.browser.core.jobs.AbstractEclipseJob#getConnections()
     */
    protected IConnection[] getConnections()
    {
        return new IConnection[]
            { connection };
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.browser.core.jobs.AbstractEclipseJob#getLockedObjects()
     */
    protected Object[] getLockedObjects()
    {
        List<String> l = new ArrayList<String>();
        l.add( connection.getUrl() + "_" + DigestUtils.shaHex( exportDsmlFilename ) );
        return l.toArray();
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.browser.core.jobs.AbstractEclipseJob#executeAsyncJob(org.apache.directory.ldapstudio.browser.core.jobs.ExtendedProgressMonitor)
     */
    protected void executeAsyncJob( ExtendedProgressMonitor monitor )
    {
        monitor.beginTask( BrowserCoreMessages.jobs__export_dsml_task, 2 );
        monitor.reportProgress( " " ); //$NON-NLS-1$
        monitor.worked( 1 );

        try
        {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setProtocolOP( searchRequest );

            // DN
            searchRequest.setBaseObject( new LdapDN( searchParameter.getSearchBase().toString() ) );

            // Scope
            int scope = searchParameter.getScope();
            if ( scope == ISearch.SCOPE_OBJECT )
            {
                searchRequest.setScope( ScopeEnum.BASE_OBJECT );
            }
            else if ( scope == ISearch.SCOPE_ONELEVEL )
            {
                searchRequest.setScope( ScopeEnum.SINGLE_LEVEL );
            }
            else if ( scope == ISearch.SCOPE_SUBTREE )
            {
                searchRequest.setScope( ScopeEnum.WHOLE_SUBTREE );
            }

            // DerefAliases
            int derefAliases = searchParameter.getAliasesDereferencingMethod();
            if ( derefAliases == IConnection.DEREFERENCE_ALIASES_ALWAYS )
            {
                searchRequest.setDerefAliases( LdapConstants.DEREF_ALWAYS );
            }
            else if ( derefAliases == IConnection.DEREFERENCE_ALIASES_FINDING )
            {
                searchRequest.setDerefAliases( LdapConstants.DEREF_FINDING_BASE_OBJ );
            }
            else if ( derefAliases == IConnection.DEREFERENCE_ALIASES_NEVER )
            {
                searchRequest.setDerefAliases( LdapConstants.NEVER_DEREF_ALIASES );
            }
            else if ( derefAliases == IConnection.DEREFERENCE_ALIASES_SEARCH )
            {
                searchRequest.setDerefAliases( LdapConstants.DEREF_IN_SEARCHING );
            }

            // Time Limit
            int timeLimit = searchParameter.getTimeLimit();
            if ( timeLimit != 0 )
            {
                searchRequest.setTimeLimit( timeLimit );
            }

            // Size Limit
            int countLimit = searchParameter.getCountLimit();
            if ( countLimit != 0 )
            {
                searchRequest.setSizeLimit( countLimit );
            }

            // Filter
            //            PresentFilter presentFilter = new PresentFilter();
            //            presentFilter.setAttributeDescription( "objectclass" );
            searchRequest.setFilter( convertToSharedLdapFilter( searchParameter.getFilter() ) );

            // Attributes
            String[] returningAttributes = searchParameter.getReturningAttributes();
            for ( int i = 0; i < returningAttributes.length; i++ )
            {
                searchRequest.addAttribute( returningAttributes[i] );
            }

            // Executing the request
            Document xmlRequest = DocumentHelper.createDocument();
            Element rootElement = xmlRequest.addElement( "batchRequest" );
            SearchRequestDsml searchRequestDsml = new SearchRequestDsml( searchRequest );
            searchRequestDsml.toDsml( rootElement );
            Dsmlv2Engine engine = new Dsmlv2Engine( connection.getHost(), connection.getPort(), connection
                .getBindPrincipal(), connection.getBindPassword() );
            String response = engine.processDSML( xmlRequest.asXML() );

            // Saving the response
            FileOutputStream fout = new FileOutputStream( exportDsmlFilename );
            new PrintStream( fout ).println( response );
            fout.close();
        }
        catch ( Exception e )
        {
            monitor.reportError( e );
        }
    }


    /* (non-Javadoc)
     * @see org.apache.directory.ldapstudio.browser.core.jobs.AbstractEclipseJob#getErrorMessage()
     */
    protected String getErrorMessage()
    {
        return BrowserCoreMessages.jobs__export_dsml_error;
    }


    /**
     * Converts a String filter into a Shared LDAP Filter.
     *
     * @param filter
     *      the filter String to convert
     * @return
     *      the corresponding Shared LDAP Filter
     * @throws ParseException 
     * @throws IOException 
     * @throws DecoderException 
     */
    public static Filter convertToSharedLdapFilter( String filter ) throws IOException, ParseException,
        DecoderException
    {
        FilterParser filterParser = new FilterParserImpl();

        ExprNode exprNode = filterParser.parse( filter );

        return convertToSharedLdapFilter( exprNode );
    }


    /**
     * Converts a ExprNode Filter Model into a Shared LDAP Model.
     *
     * @param exprNode
     *      the filter
     * @return
     *      the corresponding filter in the Shared LDAP Model
     * @throws DecoderException 
     */
    public static Filter convertToSharedLdapFilter( ExprNode exprNode ) throws DecoderException
    {
        Filter sharedLdapFilter = null;

        if ( exprNode instanceof BranchNode )
        {
            BranchNode branchNode = ( BranchNode ) exprNode;

            switch ( branchNode.getOperator() )
            {
                case AND:
                    AndFilter andFilter = new AndFilter();
                    sharedLdapFilter = andFilter;

                    List<Filter> andFilters = iterateOnFilters( branchNode.getChildren() );
                    for ( int i = 0; i < andFilters.size(); i++ )
                    {
                        andFilter.addFilter( andFilters.get( i ) );
                    }
                    break;

                case OR:
                    OrFilter orFilter = new OrFilter();
                    sharedLdapFilter = orFilter;

                    List<Filter> orFilters = iterateOnFilters( branchNode.getChildren() );
                    for ( int i = 0; i < orFilters.size(); i++ )
                    {
                        orFilter.addFilter( orFilters.get( i ) );
                    }
                    break;
                case NOT:
                    NotFilter notFilter = new NotFilter();
                    sharedLdapFilter = notFilter;

                    List<Filter> notFilters = iterateOnFilters( branchNode.getChildren() );
                    notFilter.setNotFilter( notFilters.get( 0 ) );
                    break;
            }
        }
        else if ( exprNode instanceof PresenceNode )
        {
            PresenceNode presenceNode = ( PresenceNode ) exprNode;

            PresentFilter presentFilter = new PresentFilter();
            sharedLdapFilter = presentFilter;

            presentFilter.setAttributeDescription( presenceNode.getAttribute() );
        }
        else if ( exprNode instanceof SimpleNode )
        {
            SimpleNode simpleNode = ( SimpleNode ) exprNode;

            switch ( simpleNode.getAssertionType() )
            {
                case APPROXIMATE:
                    AttributeValueAssertionFilter approxMatchFilter = createAttributeValueAssertionFilter( simpleNode,
                        LdapConstants.APPROX_MATCH_FILTER );
                    sharedLdapFilter = approxMatchFilter;
                    break;

                case EQUALITY:
                    AttributeValueAssertionFilter equalityMatchFilter = createAttributeValueAssertionFilter(
                        simpleNode, LdapConstants.EQUALITY_MATCH_FILTER );
                    sharedLdapFilter = equalityMatchFilter;
                    break;

                case GREATEREQ:
                    AttributeValueAssertionFilter greaterOrEqualFilter = createAttributeValueAssertionFilter(
                        simpleNode, LdapConstants.GREATER_OR_EQUAL_FILTER );
                    sharedLdapFilter = greaterOrEqualFilter;
                    break;

                case LESSEQ:
                    AttributeValueAssertionFilter lessOrEqualFilter = createAttributeValueAssertionFilter( simpleNode,
                        LdapConstants.LESS_OR_EQUAL_FILTER );
                    sharedLdapFilter = lessOrEqualFilter;
                    break;
            }
        }
        else if ( exprNode instanceof ExtensibleNode )
        {
            ExtensibleNode extensibleNode = ( ExtensibleNode ) exprNode;

            ExtensibleMatchFilter extensibleMatchFilter = new ExtensibleMatchFilter();
            sharedLdapFilter = extensibleMatchFilter;

            extensibleMatchFilter.setDnAttributes( extensibleNode.dnAttributes() );
            extensibleMatchFilter.setMatchingRule( extensibleNode.getMatchingRuleId() );
            extensibleMatchFilter.setMatchValue( extensibleNode.getValue() );
            extensibleMatchFilter.setType( extensibleNode.getAttribute() );
        }
        else if ( exprNode instanceof SubstringNode )
        {
            SubstringNode substringNode = ( SubstringNode ) exprNode;

            SubstringFilter substringFilter = new SubstringFilter();
            sharedLdapFilter = substringFilter;

            substringFilter.setType( substringNode.getAttribute() );
            substringFilter.setInitialSubstrings( substringNode.getInitial() );
            substringFilter.setFinalSubstrings( substringNode.getFinal() );
            List anys = substringNode.getAny();
            for ( int i = 0; i < anys.size(); i++ )
            {
                substringFilter.addAnySubstrings( ( String ) anys.get( i ) );
            }
        }

        return sharedLdapFilter;
    }


    /**
     * Iterates the conversion on the given List of notdes.
     *
     * @param filters
     *      the List of nodes to convert
     * @return
     *      an array containing the conversion for each Ldap Filter into its Shared LDAP Model
     * @throws DecoderException 
     */
    private static List<Filter> iterateOnFilters( List<ExprNode> filters ) throws DecoderException
    {
        List<Filter> filtersList = new ArrayList<Filter>();

        for ( int c = 0; c < filters.size(); c++ )
        {
            filtersList.add( convertToSharedLdapFilter( filters.get( c ) ) );
        }

        return filtersList;
    }


    /**
     * Create and returns an Attribute Value Assertion Filter from the given SimpleNode ant the given type.
     *
     * @param node
     *      the filter to convert
     * @param type
     *      the type of the Attribute Value Assertion Filter
     * @return
     *      the corresponding Attribute Value Assertion Filter
     */
    private static AttributeValueAssertionFilter createAttributeValueAssertionFilter( SimpleNode node, int type )
    {
        AttributeValueAssertionFilter avaFilter = new AttributeValueAssertionFilter( type );

        AttributeValueAssertion assertion = new AttributeValueAssertion();
        avaFilter.setAssertion( assertion );
        assertion.setAttributeDesc( node.getAttribute() );
        assertion.setAssertionValue( node.getValue() );

        return avaFilter;
    }
}
