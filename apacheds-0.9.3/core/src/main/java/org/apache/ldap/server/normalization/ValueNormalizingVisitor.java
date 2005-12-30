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
package org.apache.ldap.server.normalization;


import org.apache.ldap.common.filter.*;
import org.apache.ldap.common.name.NameComponentNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.util.ArrayList;


/**
 * A filter visitor which normalizes leaf node values as it visits them.  It also removes
 * leaf nodes from branches whose attributeType is undefined.  It obviously cannot remove
 * a leaf node from a filter which is only a leaf node.  Checks to see if a filter is a
 * leaf node with undefined attributeTypes should be done outside this visitor.
 *
 * Since this visitor may remove filter nodes it may produce negative results on filters,
 * like NOT branch nodes without a child or AND and OR nodes with one or less children.  This
 * might make some partition implementations choke.  To avoid this problem we clean up branch
 * nodes that don't make sense.  For example all BranchNodes without children are just
 * removed.  An AND and OR BranchNode with a single child is replaced with it's child for
 * all but the topmost branchnode which we cannot replace.  So again the top most branch
 * node must be inspected by code outside of this visitor.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class ValueNormalizingVisitor implements FilterVisitor
{
    /** logger used by this class */
    private final static Logger log = LoggerFactory.getLogger( ValueNormalizingVisitor.class );
    /** the name component normalizer used by this visitor */
    private final NameComponentNormalizer ncn;


    public ValueNormalizingVisitor( NameComponentNormalizer ncn )
    {
        this.ncn = ncn;
    }


    public void visit( ExprNode node )
    {
        if ( node instanceof SimpleNode )
        {
            SimpleNode snode = ( SimpleNode ) node;
            String normalized;

            try
            {
                // still need this check here in case the top level is a leaf node
                // with an undefined attributeType for its attribute
                if ( ! ncn.isDefined( snode.getAttribute() ) )
                {
                    normalized = snode.getValue();
                }
                else if ( Character.isDigit( snode.getAttribute().charAt( 0 ) ) )
                {
                    normalized = ncn.normalizeByOid( snode.getAttribute(), snode.getValue() );
                }
                else
                {
                    normalized = ncn.normalizeByName( snode.getAttribute(), snode.getValue() );
                }
            }
            catch ( NamingException e )
            {
                log.error( "Failed to normalize filter value: " + e.getMessage(), e );
                throw new RuntimeException( e.getMessage() );
            }

            snode.setValue( normalized );
            return;
        }

        if ( node instanceof BranchNode )
        {
            BranchNode bnode = ( BranchNode ) node;
            StringBuffer buf = null;
            for ( int ii = 0; ii < bnode.getChildren().size() ; ii++ )
            {
                // before visiting each node let's check to make sure non-branch children use
                // attributes that are defined in the system, if undefined nodes are removed
                ExprNode child = ( ExprNode ) bnode.getChildren().get( ii );
                if ( child.isLeaf() )
                {
                    LeafNode ln = ( LeafNode ) child;
                    if ( ! ncn.isDefined( ln.getAttribute() ) )
                    {
                        if ( buf == null )
                        {
                            buf = new StringBuffer();
                        }
                        else
                        {
                            buf.setLength( 0 );
                        }
                        buf.append( "Removing leaf node based on undefined attribute '" );
                        buf.append( ln.getAttribute() );
                        buf.append( "' from filter." );
                        log.warn( buf.toString() );

                        // remove the child at ii
                        bnode.getChildren().remove( child );
                        ii--; // decrement so we can evaluate next child which has shifted to ii
                        continue;
                    }
                }

                visit( child );
            }

            // now see if any branch child nodes are damaged (NOT without children,
            // AND/OR with one or less children) and repair them by removing branch
            // nodes without children and replacing branch nodes like AND/OR with
            // their single child if other branch nodes do not remain.
            for ( int ii = 0; ii < bnode.getChildren().size() ; ii++ )
            {
                ExprNode unknown = ( ExprNode ) bnode.getChildren().get( ii );
                if ( !unknown.isLeaf() )
                {
                    BranchNode child = ( BranchNode ) unknown;

                    // remove child branch node that has no children left
                    if ( child.getChildren().size() == 0 )
                    {
                        // remove the child at ii
                        bnode.getChildren().remove( child );
                        ii--; // decrement so we can evaluate next child which has shifted to ii
                        continue;
                    }

                    // now for AND & OR nodes with a single child left replace them
                    // with their child at the same index they AND/OR node was in
                    if ( child.getChildren().size() == 1 && child.getOperator() != BranchNode.NOT )
                    {
                        bnode.getChildren().remove( child );
                        if ( ii >= bnode.getChildren().size() )
                        {
                            bnode.getChildren().add( child.getChild() );
                        }
                        else
                        {
                            bnode.getChildren().add( ii, child.getChild() );
                        }
                    }
                }
            }
        }
    }


    public boolean canVisit( ExprNode node )
    {
        return node instanceof BranchNode || node instanceof SimpleNode;
    }


    public boolean isPrefix()
    {
        return false;
    }


    public ArrayList getOrder( BranchNode node, ArrayList children )
    {
        return children;
    }
}
