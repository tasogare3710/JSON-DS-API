// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static com.github.tasogare.json.ds.internal.ast.AstContext.newTypeName;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnionTypeNodeTest {

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
    public void testUnionType() {
        // "type U = (A | B);"
        final List<TypeExpressionNode<TypeNameNode>> list = new ArrayList<>();
        list.add(new TypeExpressionNode<TypeNameNode>(10, 11, newTypeName(10, 11, "A")));
        list.add(new TypeExpressionNode<TypeNameNode>(14, 15, newTypeName(14, 15, "B")));
        final UnionTypeNode<TypeNameNode> unionType = new UnionTypeNode<>(9, 16, list);

        assertFalse(unionType.isEmpty());
        assertThat(unionType.getTypeUnionList().size(), is(2));
        assertThat(unionType.getTypeUnionList().get(0).getBasicTypeExpression().getString(), equalTo("A"));
        assertThat(unionType.getTypeUnionList().get(1).getBasicTypeExpression().getString(), equalTo("B"));

        for (final TypeExpressionNode<TypeNameNode> e : unionType.getTypeUnionList()) {
            // UionTypeの型パラメタが解決できるならその型とマッチできる
            assertThat(e.getBasicTypeExpression().getString(), anyOf(equalTo("A"), equalTo("B")));
        }
    }

    @Test
    public void testEmptyUnionType() {
        // "type U = ();"
        final UnionTypeNode<?> unionType = new UnionTypeNode<>(9, 11);
        assertTrue(unionType.isEmpty());
        // javaのジェネリックスの実装の都合で?が解決できないので代入できないが
        // List<TypeExpressionNode<?>> a= unionType.getTypeUnionList();
        // これなら出来る
        for (final TypeExpressionNode<?> e : unionType.getTypeUnionList()) {
            // ただし、basicTypeExpressionは?(この場合BasicTypeExpression型)になるのでダウンキャストしなければならない
            // e.getBasicTypeExpression();
            fail(e.toString());
        }
    }
}
