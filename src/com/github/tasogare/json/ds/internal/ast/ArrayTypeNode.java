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
 * 
 * @author tasogare
 *
 * @param <T> 要素の型
 */
public class ArrayTypeNode<T extends AstNode & BasicTypeExpressionNode<T>> extends AstNode implements BasicTypeExpressionNode<ArrayTypeNode<T>>, Cloneable {

    // empty ArrayTypeはvariableArrayTypeフィールドがnullで
    // elementTypeListフィールドがCollections.<TypeExpressionNode>emptyList()になる。
    private final List<TypeExpressionNode<T>> elementTypeList;
    private final TypeExpressionNode<T> variableArrayType;

    /**
     * empty ArrayTypeNode
     * @param startPosition
     * @param endPosition
     */
    public ArrayTypeNode(final long startPosition, final long endPosition) {
        this(startPosition, endPosition, Collections.<TypeExpressionNode<T>>emptyList(), null);
    }

    /**
     * [string, string]
     * @param startPosition
     * @param endPosition
     * @param elementTypeList
     */
    public  ArrayTypeNode(final long startPosition, final long endPosition, final List<TypeExpressionNode<T>> elementTypeList) {
        this(startPosition, endPosition, elementTypeList, null);
    }

    /**
     * [...string]
     * @param startPosition
     * @param endPosition
     * @param variableArrayType
     */
    public  ArrayTypeNode(final long startPosition, final long endPosition, final TypeExpressionNode<T> variableArrayType) {
        this(startPosition, endPosition,  null, variableArrayType);
    }

    /**
     * [string, ...string] or [string, string, ...string] etc.
     * 
     * @param startPosition
     * @param endPosition
     * @param elementTypeList
     * @param variableArrayType
     */
    public ArrayTypeNode(final long startPosition, final long endPosition, final List<TypeExpressionNode<T>> elementTypeList, final TypeExpressionNode<T> variableArrayType) {
        super(startPosition, endPosition);
        // 不変としてコピーする
        this.elementTypeList = elementTypeList == null ? null : Collections.unmodifiableList(new ArrayList<TypeExpressionNode<T>>(elementTypeList));
        this.variableArrayType = variableArrayType;
    }

    /**
     * {@inheritDoc}
     * 
     * @return ArrayTypeNode<T>型である自分自身
     */
    @Override
    public ArrayTypeNode<T> getBasicTypeExpression() {
        return this;
    }

    public final List<TypeExpressionNode<T>> getElementTypeList() {
        return elementTypeList;
    }

    public final TypeExpressionNode<T> getVariableArrayType() {
        return variableArrayType;
    }

    /**
     *  
     * @return if {@code true} is empty array type.
     */
    public boolean isEmpty(){
        return variableArrayType == null && elementTypeList.isEmpty();
    }

    /**
     * 
     * @return if {@code true} is contains element type and  not variable-array type.
     */
    public boolean hasElementTypeList(){
        return elementTypeList != null;
    }

    /**
     * 
     * @return if {@code true} is this array type as variable-array type, otherwise {@code false}.
     */
    public boolean isVariable(){
        return variableArrayType != null;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
