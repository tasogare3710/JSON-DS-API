// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

/**
 * @author tasogare
 *
 * @param <T>
 */
public class TypeExpressionNode<T extends AstNode & BasicTypeExpressionNode<T>> extends AstNode implements Cloneable {

    public static enum Nullability {
        Absent,
        Nullable,
        NonNullable;
    }

    private final Nullability nullability;
    private final T basicTypeExpression;

    /**
     * 
     * @param startPosition
     * @param endPosition
     * @param basicType
     * @param nullable
     *            if {@code true} is type as nullable. otherwise, not nullable.
     * @throws IllegalArgumentException
     *             {@code nullable}が{@code false}で{@code basicType}が
     *             {@link NullLiteralNode}のとき。
     */
    public TypeExpressionNode(final long startPosition, final long endPosition, final T basicType,
        final boolean nullable) throws IllegalArgumentException
    {
        this(startPosition, endPosition, basicType, nullable ? Nullability.Nullable : Nullability.NonNullable);
    }

    /**
     * 
     * @param startPosition
     * @param endPosition
     * @param basicType
     */
    public TypeExpressionNode(final long startPosition, final long endPosition, final T basicType) {
        this(startPosition, endPosition, basicType, Nullability.Absent);
    }

    /**
     * 
     * @param startPosition
     * @param endPosition
     * @param basicType
     * @param nullability
     * @throws IllegalArgumentException
     *             {@code nullability}が{@link Nullability#NonNullable}で
     *             {@code basicType}が{@link NullLiteralNode}のとき。
     */
    public TypeExpressionNode(final long startPosition, final long endPosition, final T basicType,
        final Nullability nullability) throws IllegalArgumentException
    {
        super(startPosition, endPosition);
        if (nullability == Nullability.NonNullable && basicType instanceof NullLiteralNode) {
            throw new IllegalArgumentException();
        }
        this.basicTypeExpression = basicType;
        this.nullability = nullability;
    }

    @Override
    public <R> R accept(NodeVisitor<R> visitor) throws StaticSemanticsException {
        // 明示的に指定するとBasicTypeExpressionクラスのTになって
        // 型推論するとvisitメソッドのTになるがどちらも定義が同じなので
        // どちらでも構わないが、将来特殊化が実装された時も
        // 型推論できるか分からないので指定しておく。
        return visitor. <T> visit(this);
    }

    public final T getBasicTypeExpression() {
        return basicTypeExpression;
    }

    public final Nullability getNullability() {
        return nullability;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
