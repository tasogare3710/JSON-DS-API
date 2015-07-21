// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast.synthetic;

import com.github.tasogare.json.ds.internal.ast.AstNode;

/**
 * @author tasogare
 *
 */
public abstract class LiteralNode extends AstNode {

    protected LiteralNode(final long startPosition, final long endPosition) {
        super(startPosition, endPosition);
    }

    public abstract String getString();
}
