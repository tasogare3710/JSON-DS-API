// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.synthetic.NameValue;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class NameExpressionNode extends AstNode implements NameValue, Cloneable {

    private final IdentifierNode identifier;
    public NameExpressionNode(final long startPosition, final long endPosition, final IdentifierNode identifier) {
        super(startPosition, endPosition);
        this.identifier = identifier;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public IdentifierNode getIdentifier(){
        return identifier;
    }

    @Override
    public String getString() {
        return getIdentifier().getString();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final NameExpressionNode c = (NameExpressionNode) super.clone();
        return c;
    }
}
