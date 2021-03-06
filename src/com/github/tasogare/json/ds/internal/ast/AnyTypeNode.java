// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.LiteralNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class AnyTypeNode extends LiteralNode implements BasicTypeExpressionNode<AnyTypeNode>, Cloneable {

    public AnyTypeNode(long startPosition, long endPosition) {
        super(startPosition, endPosition);
    }

    /**
     * {@inheritDoc}
     * 
     * @return AnyTypeNode型である自分自身
     * 
     */
    @Override
    public AnyTypeNode getBasicTypeExpression() {
        return this;
    }

    @Override
    public String getString() {
        return "*";
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException {
        return visitor.visit(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
