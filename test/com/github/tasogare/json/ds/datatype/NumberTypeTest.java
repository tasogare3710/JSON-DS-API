// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.*;
import static com.github.tasogare.json.ds.datatype.Intrinsics.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NumberTypeTest {

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
    public void testNumberTypeCheckSuccess() {
        final NumberType n1 = numberType;
        final NumberType n2 = numberType;
        assertTrue(n1.isTypeOf(n2));
        assertTrue(n2.isTypeOf(n1));
    }

    @Test
    public void testNumberTypeCheckFail() {
        assertFalse(numberType.isTypeOf(booleanType));
        assertFalse(numberType.isTypeOf(stringType));
    }

    @Test
    public void testNumberTypeisAnyType() {
        assertTrue(numberType.isTypeOf(anyType));
    }

    @Test
    public void testNumberTypeisNonNullable() {
        assertFalse(numberType.isTypeOf(nullType));
    }
}
