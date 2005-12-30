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
package org.apache.ldap.server.tools.schema;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.ldap.server.schema.bootstrap.AbstractBootstrapSchema;
import org.apache.ldap.server.schema.bootstrap.BootstrapSchema;
import org.apache.ldap.server.schema.bootstrap.ProducerTypeEnum;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;


/**
 * Maven 2 plugin mojo wrapper for directory plugin.
 * 
 * @goal generate
 * @description Generates ApacheDS schema classes from OpenLDAP schema files
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$
 */
public class DirectorySchemaToolMojo extends AbstractMojo
{
    /**
     * The directory containing the OpenLDAP schema files.
     * @parameter expression="src/main/schema"
     */
    private File sourceDirectory;
    
    /**
     * The target directory into which the plugin generates schema java sources.
     * @parameter expression="target/generated-sources"
     */
    private File outputDirectory;
    
    /**
     * The default package to use for generated schema classes.
     * @parameter expression="org.apache.ldap.server.schema.bootstrap"
     */
    private String defaultPackage;

    /**
     * The distinguished name of the default schema owner.
     * @parameter expression="uid=admin,ou=system"
     */
    private String defaultOwner;
    
    /**
     * The set of schemas to generate classes for.
     * @parameter 
     */
    private Schema[] schemas;

    /**
     * Toggles verbose output.
     * @parameter expression="true"
     */
    private boolean verboseOutput;
    
    // Need to figure this out from the pom - maybe make it a parameter for mojoj and set expression for pom element
    private String javaSrcDir = "src/main/java";


    public DirectorySchemaToolMojo() throws Exception
    {
        Velocity.init();
    }


    private void generate( Schema schema ) throws Exception
    {
        BootstrapSchema bootstrapSchema = new AbstractBootstrapSchema( schema.getOwner(), 
            schema.getName(), schema.getPkg(), schema.getDependencies() ){};

        if ( schema == null )
        {
            throw new NullPointerException( "the schema property must be set" );
        }

        String filePath = sourceDirectory + File.separator + schema.getName() + ".schema";
        InputStream in = new FileInputStream( filePath );
        OpenLdapSchemaParser parser = new OpenLdapSchemaParser();
        parser.parse( in );
        generateSchema( bootstrapSchema );
        generateAttributeTypes( parser, bootstrapSchema );
        generateObjectClasses( parser, bootstrapSchema );
        generateRest( bootstrapSchema );
    }


    protected void generateSchema( BootstrapSchema schema ) throws Exception
    {
        StringBuffer schemaCapped = new StringBuffer();
        schemaCapped.append( Character.toUpperCase( schema.getSchemaName().charAt( 0 ) ) );
        schemaCapped.append( schema.getSchemaName().substring( 1, schema.getSchemaName().length() ) );

        VelocityContext context = new VelocityContext();
        context.put( "package", schema.getPackageName() );
        context.put( "classname", schemaCapped.toString() + "Schema" );
        context.put( "schema", schema.getSchemaName() );
        context.put( "owner", schema.getOwner() ) ;
        context.put( "deps", schema.getDependencies()  ) ;

        Reader fileIn = getResourceReader( "Schema.template" );
        Writer writer = getResourceWriter( schema.getPackageName(), schema.getUnqualifiedClassName() );
        Velocity.evaluate( context, writer, "LOG", fileIn );

        writer.flush();
        writer.close();
    }


    protected void generateRest( BootstrapSchema schema ) throws Exception
    {
        List types = new ArrayList();
        types.addAll( ProducerTypeEnum.list() );
        types.remove( ProducerTypeEnum.ATTRIBUTE_TYPE_PRODUCER );
        types.remove( ProducerTypeEnum.OBJECT_CLASS_PRODUCER );

        ProducerTypeEnum type = null;
        for ( int ii = 0; ii < types.size(); ii++ )
        {
            type = ( ProducerTypeEnum ) types.get( ii );

            if ( exists( schema.getFullDefaultBaseClassName( type ), type ) )
            {
                continue;
            }

            VelocityContext context = new VelocityContext();
            context.put( "package", schema.getPackageName() );
            context.put( "classname", schema.getUnqualifiedClassName( type ) );
            context.put( "schema", schema.getSchemaName() );
            context.put( "owner", schema.getOwner() ) ;
            context.put( "type", type.getName().substring( 0, type.getName().length() - 8 ) ) ;

            String typeName = null;
            switch( type.getValue() )
            {
                case( ProducerTypeEnum.COMPARATOR_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.COMPARATOR_PRODUCER";
                    break;
                case( ProducerTypeEnum.DIT_CONTENT_RULE_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.DIT_CONTENT_RULE_PRODUCER";
                    break;
                case( ProducerTypeEnum.DIT_STRUCTURE_RULE_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.DIT_STRUCTURE_RULE_PRODUCER";
                    break;
                case( ProducerTypeEnum.MATCHING_RULE_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.MATCHING_RULE_PRODUCER";
                    break;
                case( ProducerTypeEnum.MATCHING_RULE_USE_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.MATCHING_RULE_USE_PRODUCER";
                    break;
                case( ProducerTypeEnum.NAME_FORM_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.NAME_FORM_PRODUCER";
                    break;
                case( ProducerTypeEnum.NORMALIZER_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.NORMALIZER_PRODUCER";
                    break;
                case( ProducerTypeEnum.SYNTAX_CHECKER_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.SYNTAX_CHECKER_PRODUCER";
                    break;
                case( ProducerTypeEnum.SYNTAX_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.SYNTAX_PRODUCER";
                    break;
                case( ProducerTypeEnum.STATE_FACTORY_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.STATE_FACTORY_PRODUCER";
                    break;
                case( ProducerTypeEnum.OBJECT_FACTORY_PRODUCER_VAL ):
                    typeName = "ProducerTypeEnum.OBJECT_FACTORY_PRODUCER";
                    break;
                default:
                    throw new IllegalStateException( "Unexpected producer: " + type.getName() );
            }

            context.put( "typeName", typeName ) ;
            runVelocity( schema.getPackageName(), schema.getUnqualifiedClassName( type ), 
                context, "typeless.template", type );
        }
    }


    protected void generateAttributeTypes( OpenLdapSchemaParser parser, BootstrapSchema schema ) throws Exception
    {
        final ProducerTypeEnum type = ProducerTypeEnum.ATTRIBUTE_TYPE_PRODUCER;

        // check to see if the producer exists for this type
        if ( exists( schema.getFullDefaultBaseClassName( type ), type ) )
        {
            return;
        }

        int size = parser.getAttributeTypes().size();
        AttributeTypeLiteral[] attributeTypes = new AttributeTypeLiteral[size];
        attributeTypes = ( AttributeTypeLiteral[] ) parser.getAttributeTypes().values().toArray( attributeTypes );

        VelocityContext context = new VelocityContext();
        context.put( "package", schema.getPackageName() );
        context.put( "classname", schema.getUnqualifiedClassName( type ) );
        context.put( "schema", schema.getSchemaName() );
        context.put( "owner", schema.getOwner() ) ;
        context.put( "schemaDepCount", new Integer( schema.getDependencies().length ) );
        context.put( "schemaDeps", new String[] { "dep1", "dep2" }  ) ;
        context.put( "attrTypes", attributeTypes );
        runVelocity( schema.getPackageName(), schema.getUnqualifiedClassName( type ), 
            context, "AttributeTypes.template", type );
    }


    protected void generateObjectClasses( OpenLdapSchemaParser parser, BootstrapSchema schema ) throws Exception
    {
        final ProducerTypeEnum type = ProducerTypeEnum.OBJECT_CLASS_PRODUCER;

        // check to see if the producer exists for this type
        if ( exists( schema.getFullDefaultBaseClassName( type ), type ) )
        {
            return;
        }

        int size = parser.getObjectClassTypes().size();
        ObjectClassLiteral[] objectClasses = new ObjectClassLiteral[size];
        objectClasses = ( ObjectClassLiteral[] ) parser.getObjectClassTypes().values().toArray( objectClasses );

        VelocityContext context = new VelocityContext();
        context.put( "package", schema.getPackageName() );
        context.put( "classname", schema.getUnqualifiedClassName( type ) );
        context.put( "schema", schema.getSchemaName() );
        context.put( "owner", schema.getOwner() ) ;
        context.put( "schemaDepCount", new Integer( schema.getDependencies().length ) );
        context.put( "schemaDeps", new String[] { "dep1", "dep2" }  ) ;
        context.put( "objectClasses", objectClasses );
        runVelocity( schema.getPackageName(), schema.getUnqualifiedClassName( type ), 
            context, "ObjectClasses.template", type );
    }



    protected void runVelocity( String pkg, String uqcn, VelocityContext context, String template, ProducerTypeEnum type )
            throws Exception
    {
        Reader fileIn = getResourceReader( template );
        Writer writer = getResourceWriter( pkg, uqcn );
        Velocity.evaluate( context, writer, "LOG", fileIn );
        writer.flush();
        writer.close();
    }


    protected Reader getResourceReader( String res ) throws IOException
    {
        return new InputStreamReader( getClass().getResourceAsStream( res ) );
    }


    protected boolean mkdirs( String base, String path )
    {
        String[] comps = path.split( "/" );
        File file = new File( base );

        if ( ! file.exists() )
        {
            file.mkdirs();
        }

        for ( int ii = 0; ii < comps.length; ii++ )
        {
            file = new File( file, comps[ii] );

            if ( ! file.exists() )
            {
                file.mkdirs();
            }
        }

        return file.exists();
    }


    protected FileWriter getResourceWriter( String pkg, String classname ) throws IOException
    {
        mkdirs( outputDirectory.getPath(), pkg.replace( '.', File.separatorChar ) );
        File base = outputDirectory;
        String relativePath = pkg.replace( '.', File.separatorChar );
        File dir = new File( base, relativePath );
        return new FileWriter( new File( dir, classname + ".java" ) );
    }


    protected boolean exists( String defaultClass, ProducerTypeEnum type )
    {
        // check to see if any of the classes are available in the java 
        // source directory, if so we return true
        File defaultFile = new File( javaSrcDir + File.separator + getFilePath( defaultClass ) );
        return defaultFile.exists();
    }


    private String getFilePath( String fqcn )
    {
        String path = fqcn.replace( '.', File.separatorChar );
        path += ".java";
        return path;
    }
    
    
    public void execute() throws MojoExecutionException 
    {
        // Bypass if no schemas have yet been defined 
        if ( schemas == null || schemas.length == 0 )
        {
            getLog().warn( "No schemas defined for directory plugin!" );
            return;
        }
        
        // Make sure schema configurations have a name field and set defaults
        // for any other missing properties of the bean: pkg and owner.
        for ( int ii = 0; ii < schemas.length; ii++ )
        {
            Schema schema = schemas[ii];
            
            if ( schema.getName() == null )
            {
                String msg = ii + "th schema configuration element must specify a name."; 
                getLog().error( msg );
                throw new MojoExecutionException( msg );
            }

            if ( schema.getPkg() == null )
            {
                schema.setPkg( defaultPackage );
            }
            
            if ( schema.getOwner() == null )
            {
                schema.setOwner( defaultOwner );
            }
        }        
       
        // Report configuration if verbose output is enabled
        if ( verboseOutput )
        {
            report();
        }

        // Create output directory if it does not exist
        if ( ! outputDirectory.exists() )
        {
            outputDirectory.mkdirs();
        }
        
        // Generate for each schema 
        for ( int ii = 0; ii < schemas.length; ii++ )
        {
            try
            {
                generate( schemas[ii] );
            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Failed while generating sources for " + schemas[ii].getName(), e );
            }
        }        
    }

    private void report()
    {
        getLog().info( "===================================================================" );
        getLog().info( "[directory:generate]" );
        getLog().info( "sourceDirectory = " + sourceDirectory );
        getLog().info( "outputDirectory = " + outputDirectory );
        getLog().info( "defaultPackage  = " + defaultPackage );
        getLog().info( "defaultOwner    = " + defaultOwner );
        getLog().info( "----------------------------- schemas -----------------------------" );
        
        if ( schemas != null )
        {
            for ( int ii = 0; ii < schemas.length; ii++ )
            {
                getLog().info( "SCHEMA: " + schemas[ii].getName() );
                
                if ( schemas[ii].getDependencies() != null )
                {
                    StringBuffer buf = new StringBuffer();
                    for ( int jj = 0; jj < schemas[ii].getDependencies().length; jj++ )
                    {
                        buf.append( schemas[ii].getDependencies()[jj] );
                        buf.append( " " );
                    }
                    getLog().info( "DEPENDENCIES: " + buf.toString() );
                }
                
                getLog().info( "PACKAGE: " + schemas[ii].getPkg() );
                getLog().info( "OWNER: " + schemas[ii].getOwner() );
                
                if ( ii + 1 < schemas.length )
                {
                    getLog().info( "" );
                }
            }
        }
        
        getLog().info( "===================================================================" );
    }
}
