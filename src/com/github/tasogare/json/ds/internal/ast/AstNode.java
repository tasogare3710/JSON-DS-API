// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * @author tasogare
 *
 */
public abstract class AstNode implements Cloneable {

    private final long startPosition;
    private final long endPosition;

    public AstNode(final long startPosition, final long endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public final long getStartPosition() {
        return startPosition;
    }

    public final long getEndPosition() {
        return endPosition;
    }

    public abstract <R> R accept(NodeVisitor<R> visitor);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
