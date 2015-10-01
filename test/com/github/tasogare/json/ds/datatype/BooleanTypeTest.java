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

import com.github.tasogare.json.ds.datatype.BooleanType;

public class BooleanTypeTest {

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
    public void testBooleanTypeCheckSuccess() {
        final BooleanType b1 = booleanType;
        final BooleanType b2 = booleanType;
        assertTrue(b1.isTypeOf(b2));
        assertTrue(b2.isTypeOf(b1));
    }

    @Test
    public void testBooleanTypeCheckFail() {
        assertFalse(booleanType.isTypeOf(numberType));
        assertFalse(booleanType.isTypeOf(stringType));
    }

    @Test
    public void testBooleanTypeisAnyType() {
        assertTrue(booleanType.isTypeOf(anyType));
    }

    @Test
    public void testBooleanTypeIsNonNullable() {
        assertFalse(booleanType.isTypeOf(nullType));
    }
}
