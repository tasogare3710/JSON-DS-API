// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;

public class ArrayTypeNodeTest {

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
    public void testEmptyArrayType() {
        //"type A = [];"
        final ArrayTypeNode<?> arrayType = new ArrayTypeNode<>(9, 11);
        assertTrue(arrayType.hasElementTypeList());
        assertTrue(arrayType.isEmpty());
        assertFalse(arrayType.isVariable());
    }

    @Test
    public void testVariableArrayType(){
        //"type A = [...number];"
        final ArrayTypeNode<TypeNameNode> arrayType = new ArrayTypeNode<>(9, 20, new TypeExpressionNode<TypeNameNode>(10, 19, new TypeNameNode(13, 19, new NameExpressionNode(13, 19, new IdentifierNode(13,  19, "number")))));
        assertFalse(arrayType.hasElementTypeList());
        assertFalse(arrayType.isEmpty());
        assertTrue(arrayType.isVariable());

        final TypeExpressionNode<TypeNameNode> name = arrayType.getVariableArrayType();
        assertThat(name.getBasicTypeExpression().getString(), equalTo("number"));
    }

    @Test
    public void testArrayType(){
        //"type A = [number, string];"
        final List<TypeExpressionNode<TypeNameNode>> list = new ArrayList<>();
        list.add(new TypeExpressionNode<>(10, 16, AstContext.newTypeName(10, 16, "number")));
        list.add(new TypeExpressionNode<>(18, 24, AstContext.newTypeName(18, 24, "string")));
        final ArrayTypeNode<TypeNameNode> arrayType = new ArrayTypeNode<>(9, 25, list);
        assertTrue(arrayType.hasElementTypeList());
        assertFalse(arrayType.isEmpty());
        assertFalse(arrayType.isVariable());

        // 以下の問題はjavaのジェネリックスの実装に由来する問題なのでAPI側の問題ではない。
        // ListのTypeExpressionのbasicTypeExpressionの型が全て同じでない場合、
        // List<TypeExpressionNode<? extends BasicTypeExpressionNode<?>>>と宣言するしかなくTypeExpressionのbasicTypeExpressionの型が消去されてしまう
        // したがって、elements.get(0).getBasicTypeExpression()の戻り値の型は自分自身の型ではなくBasicTypeExpressionになる。
        final List<TypeExpressionNode<TypeNameNode>> elements = arrayType.getElementTypeList();
//        BasicTypeExpressionNode<?> a= elements.get(0).getBasicTypeExpression();
        // というわけで、a.getClass()の戻り値の型がClass<? extends BasicTypeExpressionNode>になるのでどうしてもuncheckedになる。
        // そうすると以下のようにすると? extends BasicTypeは実際の型を適応できないので失敗する
//         TypeNameNode name = a.<TypeNameNode>as(a.getClass());
//        TypeNameNode name = a.getBasicTypeExpression();
        // 以下のようにするしか無い
//        TypeNameNode name = a.as(TypeNameNode.class);
        // 静的キャストでいいなら
//        TypeNameNode name = a.asTypeName();
        assertThat(elements.get(0).getBasicTypeExpression().getString(), equalTo("number"));
        assertThat(elements.get(1).getBasicTypeExpression().getString(), equalTo("string"));
    }
}
