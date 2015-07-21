// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.LiteralNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class NullLiteralNode extends LiteralNode implements BasicTypeExpressionNode<NullLiteralNode>, Cloneable {

    public NullLiteralNode(long startPosition, long endPosition) {
        super(startPosition, endPosition);
    }

    @Override
    public String getString() {
        return "null";
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @return NullLiteralNode型である自分自身
     */
    @Override
    public NullLiteralNode getBasicTypeExpression() {
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
