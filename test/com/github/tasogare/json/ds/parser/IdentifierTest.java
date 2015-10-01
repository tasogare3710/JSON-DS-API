// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IdentifierTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIsIdentifierStartOtherSupported() {
        assertTrue(Source.isIdentifierStart('\u2118'));
        assertTrue(Source.isIdentifierStart('\u212e'));
        assertTrue(Source.isIdentifierStart('\u309b'));
        assertTrue(Source.isIdentifierStart('\u309c'));
    }

    @Test
    public void testIsIdentifierPartOtherSupported() {
        assertTrue(Source.isIdentifierPart('\u00B7'));
        assertTrue(Source.isIdentifierPart('\u0387'));
        assertTrue(Source.isIdentifierPart('\u1369'));
        assertTrue(Source.isIdentifierPart('\u136A'));
        assertTrue(Source.isIdentifierPart('\u136B'));
        assertTrue(Source.isIdentifierPart('\u136C'));
        assertTrue(Source.isIdentifierPart('\u136D'));
        assertTrue(Source.isIdentifierPart('\u136E'));
        assertTrue(Source.isIdentifierPart('\u136F'));
        assertTrue(Source.isIdentifierPart('\u1370'));
        assertTrue(Source.isIdentifierPart('\u1371'));
        assertTrue(Source.isIdentifierPart('\u19DA'));
        assertTrue(Source.isIdentifierPart('\u2118'));
        assertTrue(Source.isIdentifierPart('\u212E'));
        assertTrue(Source.isIdentifierPart('\u309B'));
        assertTrue(Source.isIdentifierPart('\u309C'));
    }
}
