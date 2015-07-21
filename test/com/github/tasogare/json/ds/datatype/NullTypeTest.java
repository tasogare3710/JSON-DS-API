// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.datatype.Intrinsics;

public class NullTypeTest {

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
    public void testNotNullType() {
        assertFalse(Intrinsics.booleanType.isTypeOf(Intrinsics.nullType));
        assertFalse(Intrinsics.numberType.isTypeOf(Intrinsics.nullType));
        assertFalse(Intrinsics.stringType.isTypeOf(Intrinsics.nullType));
    }

    @Test
    public void testNullableNominalTypeIsNullType() {
        assertTrue(Intrinsics.newNullable(Intrinsics.booleanType).isTypeOf(Intrinsics.nullType));
        assertTrue(Intrinsics.newNullable(Intrinsics.numberType).isTypeOf(Intrinsics.nullType));
        assertTrue(Intrinsics.newNullable(Intrinsics.stringType).isTypeOf(Intrinsics.nullType));
    }

    @Test
    public void testNullTypeIsntNominalType() {
        assertFalse(Intrinsics.nullType.isTypeOf(Intrinsics.booleanType));
        assertFalse(Intrinsics.nullType.isTypeOf(Intrinsics.numberType));
        assertFalse(Intrinsics.nullType.isTypeOf(Intrinsics.stringType));
    }

    /**
     * nullable type is union type
     */
    @Test
    public void testNullTypeIsNullableNominalType() {
        assertFalse(Intrinsics.nullType.isTypeOf(Intrinsics.newNullable(Intrinsics.booleanType)));
        assertFalse(Intrinsics.nullType.isTypeOf(Intrinsics.newNullable(Intrinsics.numberType)));
        assertFalse(Intrinsics.nullType.isTypeOf(Intrinsics.newNullable(Intrinsics.stringType)));
    }

    @Test
    public void testNullTypeIsNullType() {
        assertTrue(Intrinsics.nullType.isTypeOf(Intrinsics.nullType));
    }
}
