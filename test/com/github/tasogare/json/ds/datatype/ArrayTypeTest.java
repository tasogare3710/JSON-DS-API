// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author tasogare
 *
 */
public class ArrayTypeTest{

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
    public void test1(){
        // type a1 = [string, string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.stringType));
        // type a2 = [...string];
        ArrayType a2 = new ArrayType(Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test2(){
        // type a1 = [string, string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.stringType));
        // type a2 = [string, ...string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType), Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test3(){
        // type a1 = [string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType));
        // type a2 = [string, ...string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType), Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test4(){
        // type a1 = [string, ...string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType), Intrinsics.stringType);
        // type a2 = [string, string, string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.stringType, Intrinsics.stringType));
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test5(){
        // type a1 = [string, ...string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType), Intrinsics.stringType);
        // type a2 = [string, string, string, ...string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.stringType, Intrinsics.stringType), Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    /**
     * 空同士
     */
    @Test
    public void test6(){
        // type a1 = [];
        ArrayType a1 = new ArrayType();
        // type a2 = [];
        ArrayType a2 = new ArrayType();
        assertTrue(a1.isTypeOf(a2));
    }

    /**
     * @see NominalType#isTypeOf(Type)
     */
    @Test
    public void test7(){
        // type a1 = [string];
        ArrayType a1 = new ArrayType(Intrinsics.stringType);
        // type a2 = [*];
        ArrayType a2 = new ArrayType(Intrinsics.anyType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test8(){
        // type a1 = [*];
        ArrayType a1 = new ArrayType(Intrinsics.anyType);
        // type a2 = [string];
        ArrayType a2 = new ArrayType(Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test9(){
        // type a1 = [string, number, boolean];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType));
        // type a2 = [string, number, boolean];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType));
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test10(){
        // type a1 = [string, number, boolean];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType));
        // type a2 = [string, number, boolean, ...string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test11(){
        // type a1 = [string, number, boolean, ...string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.stringType);
        // type a2 = [string, number, boolean];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType));
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test12(){
        // type a1 = [string, number, boolean, ...string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.stringType);
        // type a2 = [string, number, boolean, string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType, Intrinsics.stringType));
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test13(){
        // type a1 = [string, number, boolean, string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType, Intrinsics.stringType));
        // type a2 = [string, number, boolean, ...string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test14(){
        // type a1 = [string, number, boolean];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType));
        // type a2 = [string, number, boolean, ...*];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.anyType);
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test15(){
        // type a1 = [string, number, boolean, ...*];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.anyType);
        // type a2 = [string, number, boolean];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType));
        assertTrue(a1.isTypeOf(a2));
    }

    @Test
    public void test16(){
        // type a1 = [string, number, boolean, string, ...string];
        ArrayType a1 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType, Intrinsics.stringType), Intrinsics.stringType);
        // type a2 = [string, number, boolean, ...string];
        ArrayType a2 = new ArrayType(asList(Intrinsics.stringType, Intrinsics.numberType, Intrinsics.booleanType), Intrinsics.stringType);
        assertTrue(a1.isTypeOf(a2));
    }
}
