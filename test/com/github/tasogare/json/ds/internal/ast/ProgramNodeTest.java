// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static com.github.tasogare.json.ds.internal.ast.AstContext.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;

/**
 * @author tasogare
 *
 */
public class ProgramNodeTest {

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
        //"type A = string;"
        //"type B = number;"
        final IdentifierNode A = new IdentifierNode(5, 6, "A");
        final TypeExpressionNode<TypeNameNode> aInitializer = new TypeExpressionNode<>(9, 15, newTypeName(9, 15, "string"));
        final TypeDefinitionNode<TypeNameNode> aDefinition = new TypeDefinitionNode<>(0, 15, A, aInitializer);

        final IdentifierNode B = new IdentifierNode(21, 22, "B");
        final TypeExpressionNode<TypeNameNode> bInitializer = new TypeExpressionNode<>(25, 32, newTypeName(25, 32, "number"));
        final TypeDefinitionNode<TypeNameNode> bDefinition = new TypeDefinitionNode<>(17, 33, B, bInitializer);

        final ArrayList<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> list = new ArrayList<>();
        list.add(aDefinition);
        list.add(bDefinition);
        final ProgramNode<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> p = new ProgramNode<>(0, 33, list, null);

        final List<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> ds = p.getDirectives();
        for(final TypeDefinitionNode<? extends BasicTypeExpressionNode<?>> d : ds){
            assertThat(d.getIdentifier().getString(), anyOf(equalTo("A"), equalTo("B")));
            assertThat(d.getTypeInitialization().getBasicTypeExpression(), instanceOf(TypeNameNode.class));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test2() {
        //"type A = string;"
        //"type B = [number, string];"
        final IdentifierNode A = new IdentifierNode(5, 6, "A");
        final TypeExpressionNode<TypeNameNode> aInitializer = new TypeExpressionNode<>(9, 15, newTypeName(9, 15, "string"));
        final TypeDefinitionNode<TypeNameNode> aDefinition = new TypeDefinitionNode<>(0, 15, A, aInitializer);

        final IdentifierNode B = new IdentifierNode(21, 22, "B");
        final List<TypeExpressionNode<TypeNameNode>> list = new ArrayList<>();
        list.add(new TypeExpressionNode<>(10, 16, newTypeName(10, 16, "number")));
        list.add(new TypeExpressionNode<>(18, 24, newTypeName(18,  24, "string")));
        final ArrayTypeNode<TypeNameNode> arrayType = new ArrayTypeNode<>(9, 25, list);
        final TypeExpressionNode<ArrayTypeNode<TypeNameNode>> bInitializer = new TypeExpressionNode<>(25, 32, arrayType);
        final TypeDefinitionNode<ArrayTypeNode<TypeNameNode>> bDefinition = new TypeDefinitionNode<>(17, 33, B, bInitializer);

        final ArrayList<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> list2 = new ArrayList<>();
        list2.add(aDefinition);
        list2.add(bDefinition);

        final ProgramNode<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> p = new ProgramNode<>(0, 33, list2, null);

        final List<TypeDefinitionNode<? extends BasicTypeExpressionNode<?>>> ds = p.getDirectives();
        for(final TypeDefinitionNode<? extends BasicTypeExpressionNode<?>> d : ds){
            assertThat(d.getIdentifier().getString(), anyOf(equalTo("A"), equalTo("B")));
            assertThat(d.getTypeInitialization().getBasicTypeExpression(), anyOf(instanceOf(TypeNameNode.class), instanceOf(ArrayTypeNode.class)));
        }
        assertThat(ds.get(1).getTypeInitialization().getBasicTypeExpression(), instanceOf(ArrayTypeNode.class));
        ArrayTypeNode<? extends BasicTypeExpressionNode<?>> at =(ArrayTypeNode<? extends BasicTypeExpressionNode<?>>) ds.get(1).getTypeInitialization().getBasicTypeExpression();

        //ここでもjavaのジェネリックスの問題が起こる
        final List<?> elements = at.getElementTypeList();
        assertThat(((TypeExpressionNode<TypeNameNode>)elements.get(0)).getBasicTypeExpression().getString(), equalTo("number"));
        assertThat(((TypeExpressionNode<TypeNameNode>)elements.get(1)).getBasicTypeExpression().getString(), equalTo("string"));

        for(final TypeExpressionNode<? extends BasicTypeExpressionNode<?>> e : at.getElementTypeList()){
            e.getBasicTypeExpression().as(TypeNameNode.class);
        }
    }
}
