// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class FieldTypeNode<T extends AstNode & BasicTypeExpressionNode<T>> extends AstNode implements Cloneable {

    private final FieldNameNode name;
    private final TypeExpressionNode<T> type;

    public FieldTypeNode(final long startPosition, final long endPosition, final FieldNameNode name, final TypeExpressionNode<T> type) {
        super(startPosition, endPosition);
        this.name = name;
        this.type = type;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public final FieldNameNode getName() {
        return name;
    }

    public final TypeExpressionNode<T> getType() {
        return type;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
