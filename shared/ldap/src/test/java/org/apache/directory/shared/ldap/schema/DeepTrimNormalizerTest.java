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
package org.apache.directory.shared.ldap.schema;

import javax.naming.NamingException;

import junit.framework.TestCase;

/**
 *
 * Test the normalizer class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DeepTrimNormalizerTest extends TestCase
{
   public void testDeepTrimNormalizerNull() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( "  ", normalizer.normalize( (String)null ) );
   }

   public void testDeepTrimNormalizerEmpty() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( "  ", normalizer.normalize( "" ) );
   }

   public void testDeepTrimNormalizerOneSpace() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( "  ", normalizer.normalize( " " ) );
   }

   public void testDeepTrimNormalizerTwoSpaces() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( "  ", normalizer.normalize( "  " ) );
   }

   public void testDeepTrimNormalizerNSpaces() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( "  ", normalizer.normalize( "      " ) );
   }

   public void testInsignifiantSpacesStringOneChar() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( " a ", normalizer.normalize( "a" ) );
   }

   public void testInsignifiantSpacesStringTwoChars() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( " aa ", normalizer.normalize( "aa" ) );
   }

   public void testInsignifiantSpacesStringNChars() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( " aaaaa ", normalizer.normalize( "aaaaa" ) );
   }

   public void testInsignifiantSpacesStringOneCombining() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       char[] chars = new char[]{ ' ', 0x0310 };
       char[] expected = new char[]{ ' ', ' ', 0x0310, ' ' };
       assertEquals( new String( expected ), normalizer.normalize( new String( chars ) ) );
   }

   public void testInsignifiantSpacesStringNCombining() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       char[] chars = new char[]{ ' ', 0x0310, ' ', 0x0311, ' ', 0x0312 };
       char[] expected = new char[]{ ' ', ' ', 0x0310, ' ', 0x0311, ' ', 0x0312, ' ' };
       assertEquals( new String( expected ), normalizer.normalize( new String( chars ) ) );
   }

   public void testInsignifiantSpacesStringCharsSpaces() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( " a ", normalizer.normalize( " a" ) );
       assertEquals( " a ", normalizer.normalize( "a " ) );
       assertEquals( " a ", normalizer.normalize( " a " ) );
       assertEquals( " a a ", normalizer.normalize( "a a" ) );
       assertEquals( " a a ", normalizer.normalize( " a a" ) );
       assertEquals( " a a ", normalizer.normalize( "a a " ) );
       assertEquals( " a a ", normalizer.normalize( "a  a" ) );
       assertEquals( " a a ", normalizer.normalize( " a   a " ) );
       assertEquals( " aaa aaa aaa ", normalizer.normalize( "  aaa   aaa   aaa  " ) );
   }

   public void testNormalizeCharsCombiningSpaces() throws NamingException
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       char[] chars = new char[]{ ' ', 0x0310, 'a', 'a', ' ', ' ',  0x0311, ' ', ' ', 'a', 0x0311, 0x0312 };
       char[] expected = new char[]{ ' ', ' ', 0x0310, 'a', 'a', ' ', ' ',  0x0311, ' ', 'a', 0x0311, 0x0312, ' ' };
       assertEquals( new String( expected ), normalizer.normalize( new String( chars ) ) );
   }

   public void testNormalizeString() throws Exception
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       assertEquals( " abcd ", normalizer.normalize( "abcd" ) );
   }

   public void testMapToSpace() throws Exception
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       char[] chars = new char[]{ 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0085, 0x00A0, 0x1680,
           0x2000, 0x2001, 0x2002, 0x2003, 0x2004, 0x2005, 0x2006, 0x2007, 0x2008, 0x2009, 0x200A,
           0x2028, 0x2029, 0x202F, 0x205F };
       assertEquals( "  ", normalizer.normalize( new String( chars ) ) );
   }

   public void testNormalizeIgnore() throws Exception
   {
       Normalizer normalizer = new DeepTrimNormalizer();
       char[] chars = new char[58];

       int pos = 0;

       for ( char c = 0x0000; c < 0x0008; c++ )
       {
           chars[pos++] = c;
       }

       for ( char c = 0x000E; c < 0x001F; c++ )
       {
           chars[pos++] = c;
       }

       for ( char c = 0x007F; c < 0x0084; c++ )
       {
           chars[pos++] = c;
       }

       for ( char c = 0x0086; c < 0x009F; c++ )
       {
           chars[pos++] = c;
       }

       chars[pos++] = 0x00AD;

       assertEquals( "  ", normalizer.normalize( new String( chars ) ) );
   }
}