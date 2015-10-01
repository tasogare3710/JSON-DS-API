// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static com.github.tasogare.json.ds.internal.ast.AstContext.newTypeName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        // "type A = string;"
        final IdentifierNode A = new IdentifierNode(0, 6, "A");

        final TypeExpressionNode<TypeNameNode> init = new TypeExpressionNode<>(9, 15, newTypeName(9, 15, "string"));
        final TypeDefinitionNode<TypeNameNode> def = new TypeDefinitionNode<>(0, 15, A, init);

        assertThat(def.getIdentifier(), is(A));
        assertThat(def.getTypeInitialization(), is(init));
        assertThat(def.getIdentifier().getString(), equalTo("A"));
        assertThat(def.getTypeInitialization().getBasicTypeExpression().getString(), equalTo("string"));
    }
}
