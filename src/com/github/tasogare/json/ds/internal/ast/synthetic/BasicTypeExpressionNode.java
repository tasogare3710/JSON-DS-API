// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast.synthetic;

import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.NullLiteralNode;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.UnionTypeNode;

/**
 * BasicTypeExpressionNodeは各型ノードを表す{@literal structural enums(aka disjoint unions)}
 * でjavaのパラメタ型が型消去されるという特性上、自分自身の型への変換メソッドを持ち合わせています。
 * これらのメソッドは特に説明がない限り静的キャストによって実現されます。
 * 
 * @author tasogare
 *
 * @param <B> このインターフェイスの実装型自分自身を表す型パラメタ。
 */
public interface BasicTypeExpressionNode<B extends BasicTypeExpressionNode<B>> {

    /**
     * <p>
     * {@linkplain BasicTypeExpressionNode} raw型から{@link B Bパラメタ化共変型}
     * 、つまり型消去される前のこのインターフェイスの実装クラスの型へと変換します。
     * <p>
     * このメソッドはキャストする必要が無いため{@linkplain ClassCastException}が発生しませんが、利用される宣言時の型が
     * {@code BasicTypeExpressionNode<?>}である場合には使えません。
     * 
     * @return 自分自身
     */
    B getBasicTypeExpression();

    default NullLiteralNode asNullLiteral() {
        return (NullLiteralNode) this;
    }

    default TypeNameNode asTypeName(){
        return (TypeNameNode) this;
    }

    @SuppressWarnings("unchecked")
    default <E extends AstNode & BasicTypeExpressionNode<E>> UnionTypeNode<E> asUnionType(){
        return (UnionTypeNode<E>) this;
    }

    @SuppressWarnings("unchecked")
    default <T extends AstNode & BasicTypeExpressionNode<T>> RecordTypeNode<T> asRecordType(){
        return (RecordTypeNode<T>) this;
    }

    @SuppressWarnings("unchecked")
    default <T extends AstNode & BasicTypeExpressionNode<T>> ArrayTypeNode<T> asArrayType(){
        return (ArrayTypeNode<T>) this;
    }

    /**
     * このメソッドは動的キャストであるため動的キャストが必要ない場合は静的キャストを使う
     * @param dynamicType 変換先{@code Class<T>}型
     * @param <T> {@code dynamicType}の型情報でもある変換後の自分自身の型
     * @return
     */
    default <T extends BasicTypeExpressionNode<T>> T as(final Class<T> dynamicType){
        if(dynamicType.isInstance(this)) {
            return (T) dynamicType.cast(this);
        }
        throw new AssertionError();
    }
}
