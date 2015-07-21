// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.synthetic.LiteralNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.NameValue;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class StringLiteralNode extends LiteralNode implements NameValue, Cloneable {

    private final String value;

    public StringLiteralNode(final long startPosition, final long endPosition, final String value) {
        super(startPosition, endPosition);
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
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
