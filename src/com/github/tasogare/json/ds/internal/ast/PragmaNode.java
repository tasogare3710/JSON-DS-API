// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.github.tasogare.json.ds.internal.ast.synthetic.NameValue;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * 
 * @author tasogare
 *
 */
public class PragmaNode extends AstNode implements Cloneable {

    private final String name;
    private final List<NameValue> pragmaItems;

    /**
     * 
     * @param startPosition
     * @param endPosition
     * @param name
     * @param pragmaItems
     */
    public <ITEM extends AstNode & NameValue> PragmaNode(final long startPosition, final long endPosition, final String name,
        final List<ITEM> pragmaItems)
    {
        super(startPosition, endPosition);
        this.name = Objects.requireNonNull(name, "name is null");
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

    public final String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public <ITEM extends AstNode & NameValue> List<ITEM> getPragmaItems() {
        return (List<ITEM>) pragmaItems;
    }
}
