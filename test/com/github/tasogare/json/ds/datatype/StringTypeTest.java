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

public class StringTypeTest {

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
    public void testStringTypeCheckSuccess() {
        final StringType n1 = stringType;
        final StringType n2 = stringType;
        assertTrue(n1.isTypeOf(n2));
        assertTrue(n2.isTypeOf(n1));
    }

    @Test
    public void testStringTypeCheckFail() {
        assertFalse(stringType.isTypeOf(booleanType));
        assertFalse(stringType.isTypeOf(numberType));
    }

    @Test
    public void testStringTypeisAnyType() {
        assertTrue(stringType.isTypeOf(anyType));
    }

    @Test
    public void testStringTypeisNonNullable() {
        assertFalse(stringType.isTypeOf(nullType));
    }
}
