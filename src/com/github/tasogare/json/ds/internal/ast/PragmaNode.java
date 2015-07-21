// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class PragmaNode extends AstNode implements Cloneable {

    //ContextuallyReservedIdentifierNode is IdentifierNodeとなる
    private final List<IdentifierNode> pragmaItems;
    public PragmaNode(final long startPosition, final long endPosition, final List<IdentifierNode> pragmaItems) {
        super(startPosition, endPosition);
        this.pragmaItems = pragmaItems == null ? null : Collections.unmodifiableList(new ArrayList<>(pragmaItems));
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<IdentifierNode> getPragmaItems() {
        return pragmaItems;
    }
}
