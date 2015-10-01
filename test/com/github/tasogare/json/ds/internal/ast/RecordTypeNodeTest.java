// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static com.github.tasogare.json.ds.internal.ast.AstContext.newFieldType;
import static com.github.tasogare.json.ds.internal.ast.AstContext.newTypeName;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author tasogare
 *
 */
public class RecordTypeNodeTest {

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
        // "type R = { "a": number, "b": string };"
        final List<FieldTypeNode<TypeNameNode>> list = new ArrayList<>();

        list.add(newFieldType(11, 22, 11, 14, "a", new TypeExpressionNode<>(16, 22, newTypeName(16, 22, "number"))));
        list.add(newFieldType(24, 35, 24, 27, "b", new TypeExpressionNode<>(29, 35, newTypeName(29, 35, "string"))));

        final RecordTypeNode<TypeNameNode> r = new RecordTypeNode<>(9, 37, list);

        final List<FieldTypeNode<TypeNameNode>> fieldTypeList = r.getFieldTypeList();
        for (final FieldTypeNode<TypeNameNode> ft : fieldTypeList) {
            assertThat(ft.getName().getString(), anyOf(equalTo("a"), equalTo("b")));
            assertThat(ft.getType().getBasicTypeExpression().getString(), anyOf(equalTo("number"), equalTo("string")));
        }
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(0))).getName().getString()
            .equals(list.get(0).getName().getString()));
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(1))).getName().getString()
            .equals(list.get(1).getName().getString()));

        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(0))).getType().getBasicTypeExpression().getString()
            .equals(list.get(0).getType().getBasicTypeExpression().getString()));
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(1))).getType().getBasicTypeExpression().getString()
            .equals(list.get(1).getType().getBasicTypeExpression().getString()));
    }
}
