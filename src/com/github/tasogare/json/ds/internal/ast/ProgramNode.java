// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.DirectiveNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

public class ProgramNode<D extends AstNode & DirectiveNode<D>> extends AstNode implements Cloneable {

    private final List<D> directives;
    private final List<PragmaNode> pragmas;

    public ProgramNode(final long startPosition, final long endPosition, final List<D> directives,
        final List<PragmaNode> pragmas)
    {
        super(startPosition, endPosition);
        this.directives = directives == null ? null : Collections.unmodifiableList(new ArrayList<D>(directives));
        this.pragmas = pragmas == null ? null : Collections.unmodifiableList(new ArrayList<PragmaNode>(pragmas));
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException {
        return visitor.visit(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public final List<D> getDirectives() {
        return directives;
    }

    public List<PragmaNode> getPragmas() {
        return pragmas;
    }
}
