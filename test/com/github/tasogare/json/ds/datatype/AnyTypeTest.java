// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.datatype.Intrinsics;
import com.github.tasogare.json.ds.datatype.RecordType;
import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.UnionType;

/**
 * 
 * @author tasogare
 *
 */
public class AnyTypeTest {

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
    public void testAnyIsAny() {
        assertTrue(Intrinsics.anyType.isTypeOf(Intrinsics.anyType));
    }

    @Test
    public void testAnyIsStringType() {
        assertTrue(Intrinsics.anyType.isTypeOf(Intrinsics.stringType));
    }

    @Test
    public void testAnyIsNumberType() {
        assertTrue(Intrinsics.anyType.isTypeOf(Intrinsics.numberType));
    }

    @Test
    public void testAnyIsBooleanType() {
        assertTrue(Intrinsics.anyType.isTypeOf(Intrinsics.booleanType));
    }

    @Test
    public void testStringTypeIsAnyType() {
        assertTrue(Intrinsics.stringType.isTypeOf(Intrinsics.anyType));
    }

    @Test
    public void testNumberTypeIsAnyType() {
        assertTrue(Intrinsics.numberType.isTypeOf(Intrinsics.anyType));
    }

    @Test
    public void testBooleanTypeIsAnyType() {
        assertTrue(Intrinsics.booleanType.isTypeOf(Intrinsics.anyType));
    }

    @Test
    public void testRecordTypeIsAnyType() {
        final HashSet<RecordType.Field> fields = new HashSet<>();
        fields.add(new RecordType.Field("a", Intrinsics.booleanType));
        fields.add(new RecordType.Field("b", Intrinsics.booleanType));
        final RecordType r = new RecordType(fields);
        assertTrue(r.isTypeOf(Intrinsics.anyType));
    }

    @Test
    public void testUnionIsAnyType() {
        final HashSet<Type> members = new HashSet<>();
        members.addAll(Arrays.asList(Intrinsics.stringType, Intrinsics.numberType));
        final UnionType u = new UnionType(members);
        assertTrue(u.isTypeOf(Intrinsics.anyType));
    }

    @Test
    public void testNonNullableAnyTypeSuccess(){
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.anyType));
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.booleanType));
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.nonNullableAnyType));
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.numberType));
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.stringType));
    }

    /**
     * var a: string? = ...;
     * var b: *! = a;
     * 
     * はaがstringのときは通るがnullの時はTypeErrorとなる。
     * しかし、JSON-DSではJSON valueの値が外部化されたJSONによって一意に決まるのでこのようなシナリオは起こらない。
     * だが、このテストがtrueとなるのは今のところ仕様とする。
     */
    @Test
    public void testNonNullableAnyTypeSuccess2(){
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.newNullable(Intrinsics.booleanType)));
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.newNullable(Intrinsics.numberType)));
        assertTrue(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.newNullable(Intrinsics.stringType)));
    }

    @Test
    public void testNonNullableAnyTypeFail(){
        assertFalse(Intrinsics.nonNullableAnyType.isTypeOf(Intrinsics.nullType));
    }
}
