// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * <p>
 * 仕様上の注意: javaには要素の型がそれぞれ違うタプル型が存在しないため、
 * 本来ならば要素のそれぞれの型が違うであろうUnion型であってもの要素の型はすべてTとなります。
 * 
 * @author tasogare
 *
 * @param <T> 要素の型
 */
public class UnionTypeNode<T extends AstNode & BasicTypeExpressionNode<T>> extends AstNode implements BasicTypeExpressionNode<UnionTypeNode<T>>, Cloneable {

    private final List<TypeExpressionNode<T>> typeUnionList;

    /**
     * contains typeUnionList UnionTypeNode
     * @param startPosition
     * @param endPosition
     * @param typeUnionList
     */
    public UnionTypeNode(final long startPosition, final long endPosition, final List<TypeExpressionNode<T>> typeUnionList) {
        super(startPosition, endPosition);
        this.typeUnionList = typeUnionList == null ? null : Collections.unmodifiableList(new ArrayList<TypeExpressionNode<T>>(typeUnionList));
    }

    /**
     * empty UnionTypeNode
     * @param startPosition
     * @param endPosition
     */
    public UnionTypeNode(final long startPosition, final long endPosition){
        this(startPosition, endPosition, Collections.<TypeExpressionNode<T>>emptyList());
    }

    /**
     * {@inheritDoc}
     * 
     * @return UnionTypeNode<T>型である自分自身
     */
    @Override
    public UnionTypeNode<T> getBasicTypeExpression() {
        return this;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.<T>visit(this);
    }

    public final List<TypeExpressionNode<T>> getTypeUnionList() {
        return typeUnionList;
    }

    public boolean isEmpty(){
        return typeUnionList.isEmpty();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
