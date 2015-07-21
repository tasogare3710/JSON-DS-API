// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.parser.Parser;

/**
 * このノードは{@literal ContextuallyReservedIdentifier}を表し{@link Parser}から生成される。
 * @author tasogare
 *
 */
public class ContextuallyReservedIdentifierNode extends IdentifierNode {

    public ContextuallyReservedIdentifierNode(long startPosition, long endPosition, String identifier) {
        super(startPosition, endPosition, identifier);
    }

    public boolean isStandard(){
        return "standard".equals(getString());
    }

    public boolean isStrict(){
        return "strict".equals(getString());
    }
}
