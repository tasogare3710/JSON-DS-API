// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static com.github.tasogare.json.ds.internal.ast.AstContext.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.FieldTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.NullLiteralNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNode;
import com.github.tasogare.json.ds.internal.ast.SemicolonNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.UnionTypeNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;

public class AstNodeCloneableTest {

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
    public void testAstNode() throws CloneNotSupportedException {
        final AstNode node = new SemicolonNode(0, 0, false);
        final AstNode c = (AstNode) ((SemicolonNode) node).clone();

        assertThat(c, not(node));
        assertThat(c.getStartPosition(), is(node.getStartPosition()));
        assertThat(c.getEndPosition(), is(node.getEndPosition()));
        assertThat(c, instanceOf(AstNode.class));
    }

    @Test
    public void testNullLiteral() throws CloneNotSupportedException {
        final NullLiteralNode node = new NullLiteralNode(0, 4);
        final NullLiteralNode c = (NullLiteralNode) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(NullLiteralNode.class));
    }

    @Test
    public void testStringLiteral() throws CloneNotSupportedException {
        final StringLiteralNode node = new StringLiteralNode(0, 5, "aaa");
        final StringLiteralNode c = (StringLiteralNode) node.clone();

        assertThat(c, not(node));
        assertThat(c.getString(), equalTo(node.getString()));
        assertThat(c, instanceOf(StringLiteralNode.class));
    }

    @Test
    public void testIdentifier() throws CloneNotSupportedException {
        final IdentifierNode node = new IdentifierNode(0, 5, "aaa");
        IdentifierNode c = (IdentifierNode) node.clone();

        assertThat(c, not(node));
        assertThat(c.getString(), equalTo(node.getString()));
        assertThat(c, instanceOf(IdentifierNode.class));
    }

    @Test
    public void testNameExpression() throws CloneNotSupportedException {
        final NameExpressionNode node = new NameExpressionNode(0, 5, new IdentifierNode(0, 5, "aaa"));
        final NameExpressionNode c = (NameExpressionNode) node.clone();

        assertThat(c, not(node));
        assertThat(c.getString(), equalTo(node.getString()));
        assertThat(c, instanceOf(NameExpressionNode.class));
    }

    @Test
    public void testTypeName() throws CloneNotSupportedException {
        final TypeNameNode node = newTypeName(0, 5, "aaa");
        final TypeNameNode c = (TypeNameNode) node.clone();

        assertThat(c, not(node));
        assertThat(c.getString(), equalTo(node.getString()));
        assertThat(c, instanceOf(TypeNameNode.class));
    }

    @Test
    public void testEmptyArrayType() throws CloneNotSupportedException {
        // "type A = [];"
        final ArrayTypeNode<?> node = new ArrayTypeNode<>(9, 11);
        final ArrayTypeNode<?> c = (ArrayTypeNode<?>) node.clone();

        assertThat(c, not(node));
        assertTrue(c.hasElementTypeList());
        assertTrue(c.isEmpty());
        assertFalse(c.isVariable());
        assertThat(c, instanceOf(ArrayTypeNode.class));
    }

    @Test
    public void testVariableArrayType() throws CloneNotSupportedException {
        // "type A = [...number];"
        final ArrayTypeNode<TypeNameNode> node = new ArrayTypeNode<>(9, 20,
            new TypeExpressionNode<TypeNameNode>(10, 19, newTypeName(13, 19, "number")));
        @SuppressWarnings("unchecked")
        final ArrayTypeNode<TypeNameNode> c = (ArrayTypeNode<TypeNameNode>) node.clone();

        assertThat(c, not(node));
        assertFalse(c.hasElementTypeList());
        assertFalse(c.isEmpty());
        assertTrue(c.isVariable());
        assertThat(c, instanceOf(ArrayTypeNode.class));

        final TypeExpressionNode<TypeNameNode> name = c.getVariableArrayType();
        assertThat(name.getBasicTypeExpression().getString(), equalTo("number"));
    }

    @Test
    public void testArrayType() throws CloneNotSupportedException {
        // "type A = [number, string];"
        final List<TypeExpressionNode<TypeNameNode>> list = new ArrayList<>();
        list.add(new TypeExpressionNode<>(10, 16, newTypeName(10, 16, "number")));
        list.add(new TypeExpressionNode<>(18, 24, newTypeName(18, 24, "string")));
        final ArrayTypeNode<TypeNameNode> node = new ArrayTypeNode<>(9, 25, list);
        @SuppressWarnings("unchecked")
        final ArrayTypeNode<TypeNameNode> c = (ArrayTypeNode<TypeNameNode>) node.clone();

        assertThat(c, not(node));
        assertTrue(c.hasElementTypeList());
        assertFalse(c.isEmpty());
        assertFalse(c.isVariable());
        assertThat(c, instanceOf(ArrayTypeNode.class));

        final List<TypeExpressionNode<TypeNameNode>> elements = c.getElementTypeList();
        assertThat(elements.get(0).getBasicTypeExpression().getString(), equalTo("number"));
        assertThat(elements.get(1).getBasicTypeExpression().getString(), equalTo("string"));
    }

    @Test
    public void testUnionType() throws CloneNotSupportedException {
        // "type U = (A | B);"
        final List<TypeExpressionNode<TypeNameNode>> list = new ArrayList<>();
        list.add(new TypeExpressionNode<TypeNameNode>(10, 11, newTypeName(10, 11, "A")));
        list.add(new TypeExpressionNode<TypeNameNode>(14, 15, newTypeName(14, 15, "B")));
        final UnionTypeNode<TypeNameNode> node = new UnionTypeNode<>(9, 16, list);
        @SuppressWarnings("unchecked")
        final UnionTypeNode<TypeNameNode> c = (UnionTypeNode<TypeNameNode>) node.clone();

        assertThat(c, not(node));
        assertFalse(c.isEmpty());
        assertThat(c.getTypeUnionList().size(), is(2));
        assertThat(c.getTypeUnionList().get(0).getBasicTypeExpression().getString(), equalTo("A"));
        assertThat(c.getTypeUnionList().get(1).getBasicTypeExpression().getString(), equalTo("B"));
        assertThat(c, instanceOf(UnionTypeNode.class));

        for (final TypeExpressionNode<TypeNameNode> e : c.getTypeUnionList()) {
            // UionTypeの型パラメタが解決できるならその型とマッチできる
            assertThat(e.getBasicTypeExpression().getString(), anyOf(equalTo("A"), equalTo("B")));
        }
    }

    @Test
    public void testEmptyUnionType() throws CloneNotSupportedException {
        // "type U = ();"
        final UnionTypeNode<?> node = new UnionTypeNode<>(9, 11);
        final UnionTypeNode<?> c = (UnionTypeNode<?>) node.clone();

        assertThat(c, not(node));
        assertTrue(c.isEmpty());
        assertThat(c, instanceOf(UnionTypeNode.class));

        for (final TypeExpressionNode<?> e : c.getTypeUnionList()) {
            fail(e.toString());
        }
        ;
    }

    @Test
    public void testRecordType() throws CloneNotSupportedException {
        // "type R = { "a": number, "b": string };"
        final List<FieldTypeNode<TypeNameNode>> list = new ArrayList<>();

        list.add(newFieldType(11, 22, 11, 14, "a", new TypeExpressionNode<>(16, 22, newTypeName(16, 22, "number"))));

        list.add(newFieldType(24, 35, 24, 27, "b", new TypeExpressionNode<>(29, 35, newTypeName(29, 35, "string"))));

        final RecordTypeNode<TypeNameNode> node = new RecordTypeNode<>(9, 37, list);
        @SuppressWarnings("unchecked")
        final RecordTypeNode<TypeNameNode> c = (RecordTypeNode<TypeNameNode>) node.clone();

        final List<FieldTypeNode<TypeNameNode>> fieldTypeList = c.getFieldTypeList();
        for (final FieldTypeNode<TypeNameNode> ft : fieldTypeList) {
            assertThat(ft.getName().getString(), anyOf(equalTo("a"), equalTo("b")));
            assertThat(ft.getType().getBasicTypeExpression().getString(), anyOf(equalTo("number"), equalTo("string")));
        }

        assertThat(c, not(node));
        assertThat(c, instanceOf(RecordTypeNode.class));

        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(0))).getName().getString()
            .equals(list.get(0).getName().getString()));
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(1))).getName().getString()
            .equals(list.get(1).getName().getString()));

        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(0))).getType().getBasicTypeExpression().getString()
            .equals(list.get(0).getType().getBasicTypeExpression().getString()));
        assertTrue(fieldTypeList.get(fieldTypeList.indexOf(list.get(1))).getType().getBasicTypeExpression().getString()
            .equals(list.get(1).getType().getBasicTypeExpression().getString()));
    }

    @Test
    public void testTypeExpression() throws CloneNotSupportedException {
        final TypeExpressionNode<TypeNameNode> node = new TypeExpressionNode<>(10, 19, newTypeName(13, 19, "number"));
        @SuppressWarnings("unchecked")
        final TypeExpressionNode<TypeNameNode> c = (TypeExpressionNode<TypeNameNode>) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(TypeExpressionNode.class));

        assertThat(c.getNullability(), is(node.getNullability()));
        assertThat(c.getBasicTypeExpression(), is(node.getBasicTypeExpression()));
        assertThat(c.getBasicTypeExpression().getNameExpression(),
                   is(node.getBasicTypeExpression().getNameExpression()));
        assertThat(c.getBasicTypeExpression().getNameExpression().getIdentifier(),
                   is(node.getBasicTypeExpression().getNameExpression().getIdentifier()));
    }

    @Test
    public void testStringLiteralFieldNameNode() throws CloneNotSupportedException {
        final FieldNameNode.StringLiteral node = newStringLiteralFieldName(0, 3, "a");
        final FieldNameNode.StringLiteral c = (FieldNameNode.StringLiteral) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(FieldNameNode.StringLiteral.class));

        assertThat(c.getStringLiteral(), is(node.getStringLiteral()));
        assertThat(c.getString(), equalTo(node.getString()));
    }

    @Test
    public void testFieldType() throws CloneNotSupportedException {
        // "type T = {"a": number}"
        final TypeExpressionNode<TypeNameNode> te = new TypeExpressionNode<TypeNameNode>(15, 21,
            newTypeName(15, 21, "number"), false);
        final FieldTypeNode<TypeNameNode> node = newFieldType(10, 21, 10, 13, "a", te);
        @SuppressWarnings("unchecked")
        final FieldTypeNode<TypeNameNode> c = (FieldTypeNode<TypeNameNode>) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(FieldTypeNode.class));

        assertThat(c.getName().getString(), equalTo(node.getName().getString()));
        assertThat(c.getType().getBasicTypeExpression(), is(node.getType().getBasicTypeExpression()));
        assertThat(c.getType().getNullability(), is(node.getType().getNullability()));
    }

    @Test
    public void testTypeDefinition() throws CloneNotSupportedException {
        // "type T = number;"
        final TypeDefinitionNode<TypeNameNode> node = new TypeDefinitionNode<>(0, 15, new IdentifierNode(5, 6, "T"),
            new TypeExpressionNode<>(9, 15, newTypeName(9, 15, "number")));
        @SuppressWarnings("unchecked")
        final TypeDefinitionNode<TypeNameNode> c = (TypeDefinitionNode<TypeNameNode>) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(TypeDefinitionNode.class));

        assertThat(c.getIdentifier(), is(c.getIdentifier()));
        assertThat(c.getTypeInitialization(), is(c.getTypeInitialization()));
    }

    @Test
    public void testEmptyProgram() throws CloneNotSupportedException {
        final ProgramNode<?> node = new ProgramNode<>(0, 0, null, null);
        final ProgramNode<?> c = (ProgramNode<?>) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(ProgramNode.class));

        assertThat(c.getDirectives(), is(node.getDirectives()));
    }

    @Test
    public void testSemicolon() throws CloneNotSupportedException {
        final SemicolonNode node = new SemicolonNode(0, 1, false);
        final SemicolonNode c = (SemicolonNode) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(SemicolonNode.class));

        assertThat(c.isVirtual(), is(node.isVirtual()));
    }

    @Test
    public void testVirtualSemicolon() throws CloneNotSupportedException {
        final SemicolonNode node = new SemicolonNode(0, 1, true);
        final SemicolonNode c = (SemicolonNode) node.clone();

        assertThat(c, not(node));
        assertThat(c, instanceOf(SemicolonNode.class));

        assertThat(c.isVirtual(), is(node.isVirtual()));
    }

    @Test
    public void testContextuallyReservedIdentifierNode() throws CloneNotSupportedException {
        final String s = "strict";
        final ContextuallyReservedIdentifierNode node = new ContextuallyReservedIdentifierNode(0, s.length(), s);
        final ContextuallyReservedIdentifierNode c = (ContextuallyReservedIdentifierNode) node.clone();
        assertThat(c, not(node));
        assertThat(c, instanceOf(ContextuallyReservedIdentifierNode.class));

        assertThat(c.isStrict(), is(true));
    }
}
