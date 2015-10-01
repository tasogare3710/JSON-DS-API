// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class RecordTypeNode<T extends AstNode & BasicTypeExpressionNode<T>> extends AstNode
        implements BasicTypeExpressionNode<RecordTypeNode<T>>, Cloneable {

    private final List<FieldTypeNode<T>> fieldTypeList;

    /**
     * contains fieldTypeList RecordTypeNode
     * 
     * @param startPosition
     * @param endPosition
     * @param fieldTypeList
     */
    public RecordTypeNode(final long startPosition, final long endPosition,
        final List<FieldTypeNode<T>> fieldTypeList)
    {
        super(startPosition, endPosition);
        this.fieldTypeList = fieldTypeList == null ? null
            : Collections.unmodifiableList(new ArrayList<FieldTypeNode<T>>(fieldTypeList));
    }

    /**
     * empty RecordTypeNode
     * 
     * @param startPosition
     * @param endPosition
     */
    public RecordTypeNode(final long startPosition, final long endPosition) {
        this(startPosition, endPosition, Collections. <FieldTypeNode<T>> emptyList());
    }

    /**
     * {@inheritDoc}
     * 
     * @return RecordTypeNode<T>型である自分自身
     */
    @Override
    public RecordTypeNode<T> getBasicTypeExpression() {
        return this;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException {
        return visitor.visit(this);
    }

    public final List<FieldTypeNode<T>> getFieldTypeList() {
        return fieldTypeList;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
