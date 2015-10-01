// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class SemicolonNode extends AstNode implements Cloneable {

    private final boolean virtual;

    public SemicolonNode(final long startPosition, final long endPosition, final boolean virtual) {
        super(startPosition, endPosition);
        this.virtual = virtual;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException {
        return visitor.visit(this);
    }

    /**
     * 
     * @return if {@code true} is virtual-semicolon
     */
    public final boolean isVirtual() {
        return virtual;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
