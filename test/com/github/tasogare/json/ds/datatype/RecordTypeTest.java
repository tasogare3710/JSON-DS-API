// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.*;
import static com.github.tasogare.json.ds.datatype.Intrinsics.*;

import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.datatype.RecordType;

public class RecordTypeTest {

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
    public void testNotSubType() {
        final HashSet<RecordType.Field> fields1 = new HashSet<>();
        fields1.add(new RecordType.Field("a", booleanType));
        fields1.add(new RecordType.Field("b", booleanType));
        final RecordType r1 = new RecordType(fields1);

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        fields2.add(new RecordType.Field("a", booleanType));
        fields2.add(new RecordType.Field("b", booleanType));
        fields2.add(new RecordType.Field("c", booleanType));
        final RecordType r2 = new RecordType(fields2);

        assertFalse(r1.isTypeOf(r2));
        assertFalse(r2.isTypeOf(r1));
    }

    @Test
    public void testSubType() {
        final HashSet<RecordType.Field> fields1 = new HashSet<>();
        fields1.add(new RecordType.Field("a", booleanType));
        fields1.add(new RecordType.Field("b", booleanType));
        final RecordType r1 = new RecordType(fields1);

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        fields2.add(new RecordType.Field("a", booleanType));
        fields2.add(new RecordType.Field("b", booleanType));
        fields2.add(new RecordType.Field("c", booleanType));
        final RecordType r2 = new RecordType(fields2);

        assertFalse(r2.isTypeOf(r1));
        assertFalse(r1.isTypeOf(r2));
    }

    @Test
    public void testEquals() {
        final HashSet<RecordType.Field> fields1 = new HashSet<>();
        fields1.add(new RecordType.Field("a", booleanType));
        fields1.add(new RecordType.Field("b", booleanType));
        fields1.add(new RecordType.Field("c", booleanType));
        final RecordType r1 = new RecordType(fields1);

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        fields2.add(new RecordType.Field("a", booleanType));
        fields2.add(new RecordType.Field("b", booleanType));
        fields2.add(new RecordType.Field("c", booleanType));
        final RecordType r2 = new RecordType(fields2);

        assertTrue(r1.isTypeOf(r2));
        assertTrue(r2.isTypeOf(r1));
    }

    @Test
    public void testNotEquals() {
        final HashSet<RecordType.Field> fields1 = new HashSet<>();
        fields1.add(new RecordType.Field("a", stringType));
        fields1.add(new RecordType.Field("b", stringType));
        fields1.add(new RecordType.Field("c", stringType));
        final RecordType r1 = new RecordType(fields1);

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        fields2.add(new RecordType.Field("a", booleanType));
        fields2.add(new RecordType.Field("b", booleanType));
        fields2.add(new RecordType.Field("c", booleanType));
        final RecordType r2 = new RecordType(fields2);

        assertFalse(r1.isTypeOf(r2));
        assertFalse(r2.isTypeOf(r1));
    }

    /**
     * {a: boolean, b: boolean} is {a: *, b: *}
     * {a: *, b: *} is {a: boolean, b: boolean}
     */
    @Test
    public void testSubTypeAny() {
        final HashSet<RecordType.Field> fields1 = new HashSet<>();
        fields1.add(new RecordType.Field("a", booleanType));
        fields1.add(new RecordType.Field("b", booleanType));
        final RecordType r1 = new RecordType(fields1);

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        fields2.add(new RecordType.Field("a", anyType));
        fields2.add(new RecordType.Field("b", anyType));
        final RecordType r2 = new RecordType(fields2);

        //型が同じ場合対称性があるので
        assertTrue(r1.isTypeOf(r2));
        assertTrue(r2.isTypeOf(r1));
    }

    @Test
    public void testEmpty() {
        final HashSet<RecordType.Field> fields1 = new HashSet<>();
        final RecordType r1 = new RecordType(fields1);

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        final RecordType r2 = new RecordType(fields2);

        assertTrue(r1.isTypeOf(r2));
        assertTrue(r2.isTypeOf(r1));
    }

    @Test
    public void testEmptyElem() {
        final RecordType r1 = new RecordType(new HashSet<>());

        final HashSet<RecordType.Field> fields2 = new HashSet<>();
        fields2.add(new RecordType.Field("a", r1));
        final RecordType r2 = new RecordType(fields2);

        assertTrue(r2.isTypeOf(r2));
    }
}
