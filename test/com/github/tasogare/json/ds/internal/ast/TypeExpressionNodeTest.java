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

public class TypeExpressionNodeTest {

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
        final TypeNameNode name = newTypeName(15, 21, "number");
        final TypeExpressionNode<TypeNameNode> type = new TypeExpressionNode<>(15, 21, name, false);

        assertThat(type.getNullability(), is(TypeExpressionNode.Nullability.NonNullable));
        assertThat(type.getBasicTypeExpression().getString(), equalTo("number"));
    }

}
