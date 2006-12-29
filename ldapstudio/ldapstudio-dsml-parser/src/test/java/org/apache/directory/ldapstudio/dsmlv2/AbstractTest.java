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

package org.apache.directory.ldapstudio.dsmlv2;


import junit.framework.TestCase;

import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2Parser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * This class had to be used to create a Request TestCase
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class AbstractTest extends TestCase
{
    /**
     * Asserts that parsing throws a correct XmlPullParserException due to an incorrect file
     *
     * @param testClass
     *      the Class of the TestCase
     * @param filename
     *      the path of the xml file to parse 
     */
    public void testParsingFail( Class testClass, String filename )
    {
        try
        {
            Dsmlv2Parser parser = new Dsmlv2Parser();

            parser.setInputFile( testClass.getResource( filename ).getFile() );

            parser.parse();
        }
        catch ( XmlPullParserException e )
        {
            assertTrue( e.getMessage(), true );
            return;
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
        fail();
    }
}
