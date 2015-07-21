// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.internal.ast.synthetic.NameValue;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * 
 * @author tasogare
 *
 */
public class IdentifierNode extends AstNode implements NameValue,  Cloneable {

    private final String identifier;
    public IdentifierNode(final long startPosition, final long endPosition, final String identifier) {
        super(startPosition, endPosition);
        this.identifier = identifier;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getString(){
        return identifier;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
