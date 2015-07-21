// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;

public class TypeDefinitionNodeTest {

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
        final IdentifierNode A = new IdentifierNode(0, 6, "A");
        final TypeExpressionNode<TypeNameNode> initializer = new TypeExpressionNode<>(9, 15, new TypeNameNode(9, 15, new NameExpressionNode(9, 15, new IdentifierNode(9, 15, "string"))));
        final TypeDefinitionNode<TypeNameNode> definition = new TypeDefinitionNode<>(0, 15, A, initializer);

        assertThat(definition.getIdentifier(), is(A));
        assertThat(definition.getTypeInitialization(), is(initializer));
        assertThat(definition.getIdentifier().getString(), equalTo("A"));
        assertThat(definition.getTypeInitialization().getBasicTypeExpression().getString(), equalTo("string"));
    }
}
