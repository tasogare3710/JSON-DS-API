// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.NameValue;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class TypeNameNode extends AstNode implements BasicTypeExpressionNode<TypeNameNode>, NameValue, Cloneable {

    private final NameExpressionNode nameExpression;

    public TypeNameNode(final long startPosition, final long endPosition, final NameExpressionNode nameExpression) {
        super(startPosition, endPosition);
        this.nameExpression = nameExpression;
    }

    /**
     * {@inheritDoc}
     * 
     * @return TypeNameNode型である自分自身
     */
    @Override
    public TypeNameNode getBasicTypeExpression() {
        return this;
    }

    public final NameExpressionNode getNameExpression() {
        return nameExpression;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException {
        return visitor.visit(this);
    }

    @Override
    public String getString() {
        return getNameExpression().getString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
