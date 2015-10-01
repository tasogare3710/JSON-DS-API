// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.*;
import static com.github.tasogare.json.ds.datatype.Intrinsics.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.UnionType;

/**
 * @author tasogare
 *
 */
public class UnionTypeTest {

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
    public void test() {
        final HashSet<Type> members = new HashSet<>();
        members.addAll(Arrays.asList(stringType, numberType));
        final UnionType u1 = new UnionType(members);
        assertTrue(u1.isTypeOf(stringType));
        assertTrue(u1.isTypeOf(numberType));
        assertFalse(u1.isTypeOf(booleanType));
    }

    @Test
    public void testNullable() {
        final UnionType u1 = newNullable(stringType);
        assertTrue(u1.isTypeOf(stringType));
        assertTrue(u1.isTypeOf(nullType));
        assertFalse(u1.isTypeOf(numberType));
        assertFalse(u1.isTypeOf(booleanType));
    }

    @Test
    public void testEmpty() {
        final HashSet<Type> members = new HashSet<>();
        final UnionType u1 = new UnionType(members);
        assertTrue(u1.isTypeOf(u1));
    }

    @Test
    public void testEmptyElem() {
        // type u1 = ();
        final HashSet<Type> members = new HashSet<>();
        final UnionType u1 = new UnionType(members);

        // type u2 = (u1 | string);
        final HashSet<Type> members2 = new HashSet<>();
        members2.add(u1);
        members2.add(stringType);
        final UnionType u2 = new UnionType(members2);

        assertTrue(u2.isTypeOf(u1));
    }

    @Test
    public void testNominals() {
        final HashSet<Type> members = new HashSet<>();
        members.addAll(Arrays.asList(stringType, numberType, booleanType));
        final UnionType u1 = new UnionType(members);

        assertTrue(u1.isTypeOf(stringType));
        assertTrue(u1.isTypeOf(numberType));
        assertTrue(u1.isTypeOf(booleanType));

        assertFalse(stringType.isTypeOf(u1));
        assertFalse(numberType.isTypeOf(u1));
        assertFalse(booleanType.isTypeOf(u1));
    }

    @Test
    public void testNominals2() {
        final HashSet<Type> members = new HashSet<>();
        members.addAll(Arrays.asList(stringType, numberType, booleanType));
        final UnionType u1 = new UnionType(members);

        final HashSet<Type> members2 = new HashSet<>();
        members2.addAll(Arrays.asList(stringType, numberType, booleanType));
        final UnionType u2 = new UnionType(members2);

        assertTrue(u1.isTypeOf(u1));
        assertTrue(u2.isTypeOf(u2));

        assertTrue(u1.isTypeOf(u2));
        assertTrue(u2.isTypeOf(u1));
    }

    @Test
    public void testObjects() {
        // type r = {a: boolean, b: string}
        final HashSet<RecordType.Field> fields = new HashSet<>();
        fields.add(new RecordType.Field("a", booleanType));
        fields.add(new RecordType.Field("b", stringType));
        final RecordType r = new RecordType(fields);

        // type a = [string]
        // type u1 = (a | r);
        final HashSet<Type> members = new HashSet<>();
        final ArrayType a = new ArrayType(stringType);
        members.addAll(Arrays.asList(a, r));
        final UnionType u1 = new UnionType(members);

        assertTrue(u1.isTypeOf(a));
        assertTrue(u1.isTypeOf(r));
        // 型が違うので対称性はない
        assertFalse(a.isTypeOf(u1));
        assertFalse(r.isTypeOf(u1));
    }
}
