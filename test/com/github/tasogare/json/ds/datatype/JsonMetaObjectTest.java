// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static org.junit.Assert.*;
import static com.github.tasogare.json.ds.datatype.Intrinsics.*;
import static com.github.tasogare.json.ds.tests.AllTest.newReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.datatype.driver.JsonMetaObjectTestDriver;

public class JsonMetaObjectTest {

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

    /**
     * type JSON = [...string];
     * var a: JSON = [ "1", "2", "3" ];
     * 
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {
        final JsonMetaObjectTestDriver metaObject = new JsonMetaObjectTestDriver();
        final String name = "com/github/tasogare/json/ds/datatype/resources/test1.json";
        try (final JsonReader json = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure value = json.read();
            final ArrayType test = new ArrayType(stringType);
            assertTrue(metaObject.is(value, test));
            assertTrue(metaObject.is(value, anyType));
            assertTrue(metaObject.is(value, new ArrayType(anyType)));
        }
    }

    /**
     * type JSON = [string, string, string];
     * var a: JSON = [ "1", "2", "3" ];
     * 
     * @throws IOException
     */
    @Test
    public void test2() throws IOException {
        final JsonMetaObjectTestDriver metaObject = new JsonMetaObjectTestDriver();
        final String name = "com/github/tasogare/json/ds/datatype/resources/test1.json";
        try (final JsonReader json = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure value = json.read();
            final ArrayType test = new ArrayType(Arrays.asList(stringType, stringType, stringType));
            assertTrue(metaObject.is(value, test));
            assertTrue(metaObject.is(value, anyType));
            assertTrue(metaObject.is(value, new ArrayType(anyType)));
        }
    }

    @Test
    public void test3() throws IOException {
        final JsonMetaObjectTestDriver metaObject = new JsonMetaObjectTestDriver();
        final String name = "com/github/tasogare/json/ds/datatype/resources/mixed.json";
        try (final JsonReader json = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure value = json.read();
            final ArrayType test = new ArrayType(Arrays.asList(numberType, stringType, booleanType), numberType);
            assertTrue(metaObject.is(value, test));
            assertTrue(metaObject.is(value, anyType));
            assertTrue(metaObject.is(value, new ArrayType(anyType)));
        }
    }

    /**
     * @see NominalType#isTypeOf(Type)
     * @throws IOException
     */
    @Test
    public void test4() throws IOException {
        final JsonMetaObjectTestDriver metaObject = new JsonMetaObjectTestDriver();
        final String name = "com/github/tasogare/json/ds/datatype/resources/mixed.json";
        try (final JsonReader json = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure value = json.read();
            final ArrayType test = new ArrayType(anyType);
            assertTrue(metaObject.is(value, test));
            assertTrue(metaObject.is(value, anyType));
        }
    }

    @Test
    public void testUnionTypedJsonInArray() throws IOException {
        // type r = {a: boolean, b: string}
        final HashSet<RecordType.Field> fields = new HashSet<>();
        fields.add(new RecordType.Field("a", booleanType));
        fields.add(new RecordType.Field("b", stringType));
        final RecordType R = new RecordType(fields);

        // type a = [string]
        // type u1 = (a | r);
        final HashSet<Type> members = new HashSet<>();
        final ArrayType A = new ArrayType(stringType);
        members.addAll(Arrays.asList(A, R));
        final UnionType test = new UnionType(members);

        final JsonMetaObjectTestDriver metaObject = new JsonMetaObjectTestDriver();
        final String name = "com/github/tasogare/json/ds/datatype/resources/UnionTypedJsonInArray.json";
        try (final JsonReader json = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure value = json.read();
            assertTrue(metaObject.is(value, test));
            assertTrue(metaObject.is(value, anyType));
            assertTrue(metaObject.is(value, new ArrayType(anyType)));
        }
    }

    @Test
    public void testUnionTypedJsonInObject() throws IOException {
        // type r = {a: boolean, b: string}
        final HashSet<RecordType.Field> fields = new HashSet<>();
        fields.add(new RecordType.Field("a", booleanType));
        fields.add(new RecordType.Field("b", stringType));
        final RecordType R = new RecordType(fields);

        // type a = [string]
        // type u1 = (a | r);
        final HashSet<Type> members = new HashSet<>();
        final ArrayType A = new ArrayType(stringType);
        members.addAll(Arrays.asList(A, R));
        final UnionType test = new UnionType(members);

        final JsonMetaObjectTestDriver metaObject = new JsonMetaObjectTestDriver();
        final String name = "com/github/tasogare/json/ds/datatype/resources/UnionTypedJsonInObject.json";
        try (final JsonReader json = Json.createReader(newReader(name, getClass()))) {
            final JsonStructure value = json.read();
            assertTrue(metaObject.is(value, test));
            assertTrue(metaObject.is(value, anyType));

            final HashSet<RecordType.Field> fields2 = new HashSet<>();
            fields2.add(new RecordType.Field("a", anyType));
            fields2.add(new RecordType.Field("b", anyType));
            final RecordType R2 = new RecordType(fields2);
            assertTrue(metaObject.is(value, R2));
        }
    }
}
