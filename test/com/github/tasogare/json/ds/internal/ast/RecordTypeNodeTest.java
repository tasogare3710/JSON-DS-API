// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

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

import com.github.tasogare.json.ds.internal.ast.FieldTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;

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
        //"type R = { "a": number, "b": string };"
        final List<FieldTypeNode<TypeNameNode>> list= new ArrayList<>();

        final FieldNameNode.StringLiteral a = new FieldNameNode.StringLiteral(11, 14, new StringLiteralNode(11, 14, "a"));
        final TypeExpressionNode<TypeNameNode> av = new TypeExpressionNode<>(16, 22, new TypeNameNode(16, 22, new NameExpressionNode(16, 22, new IdentifierNode(16, 22, "number"))));

        final FieldNameNode.StringLiteral b = new FieldNameNode.StringLiteral(24, 27, new StringLiteralNode(24, 27, "b"));
        final TypeExpressionNode<TypeNameNode> bv = new TypeExpressionNode<>(29, 35, new TypeNameNode(29, 35, new NameExpressionNode(29, 35, new IdentifierNode(29, 35, "string"))));

        list.add(new FieldTypeNode<TypeNameNode>(11, 22, a, av));
        list.add(new FieldTypeNode<TypeNameNode>(24, 35, b, bv));
        final RecordTypeNode<TypeNameNode> r = new RecordTypeNode<>(9, 37, list);

        final List<FieldTypeNode<TypeNameNode>> fieldTypeList = r.getFieldTypeList();
        for(final FieldTypeNode<TypeNameNode> ft : fieldTypeList){
            assertThat(ft.getName().getString(), anyOf(equalTo("a"), equalTo("b")));
            assertThat(ft.getType().getBasicTypeExpression().getString(), anyOf(equalTo("number"), equalTo("string")));
        }
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(0))).getName().getString().equals(list.get(0).getName().getString()));
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(1))).getName().getString().equals(list.get(1).getName().getString()));

        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(0))).getType().getBasicTypeExpression().getString().equals(list.get(0).getType().getBasicTypeExpression().getString()));
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(1))).getType().getBasicTypeExpression().getString().equals(list.get(1).getType().getBasicTypeExpression().getString()));
    }
}
