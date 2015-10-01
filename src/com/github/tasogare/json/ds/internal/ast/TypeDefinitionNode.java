// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * @author tasogare
 *
 * @param <T>
 */
public class TypeDefinitionNode<T extends AstNode & BasicTypeExpressionNode<T>>
        extends AttributedDirectiveNode<TypeDefinitionNode<T>>implements Cloneable {

    private final IdentifierNode identifier;
    private final TypeExpressionNode<T> typeInitialization;

    public TypeDefinitionNode(long startPosition, long endPosition, final IdentifierNode identifier,
        final TypeExpressionNode<T> typeInitialization)
    {
        super(startPosition, endPosition);
        this.identifier = identifier;
        this.typeInitialization = typeInitialization;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws RuntimeSemanticsException, StaticSemanticsException {
        return visitor.visit(this);
    }

    public final IdentifierNode getIdentifier() {
        return identifier;
    }

    public final TypeExpressionNode<T> getTypeInitialization() {
        return typeInitialization;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
