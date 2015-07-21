// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast.synthetic;

import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * @author tasogare
 *
 */
public interface FieldNameNode extends NameValue {

    /**
     * 
     * @author tasogare
     *
     */
    public static final class StringLiteral extends AstNode implements FieldNameNode, Cloneable {

        private final StringLiteralNode stringLiteral;
        public StringLiteral(final long startPosition, final long endPosition, final StringLiteralNode stringLiteral) {
            super(startPosition, endPosition);
            this.stringLiteral = stringLiteral;
        }

        public StringLiteralNode getStringLiteral(){
            return stringLiteral;
        }

        @Override
        public String getString(){
            return stringLiteral.getString();
        }

        @Override
        public <R> R accept(NodeVisitor<R> visitor) {
            return visitor.visit(this);
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

}
