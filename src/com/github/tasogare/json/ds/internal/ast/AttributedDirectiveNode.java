// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.DirectiveNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public abstract class AttributedDirectiveNode<A extends AttributedDirectiveNode<A>> extends AstNode
        implements DirectiveNode<A> {

    public AttributedDirectiveNode(long startPosition, long endPosition) {
        super(startPosition, endPosition);
    }

    @Override
    public abstract <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException;

}
