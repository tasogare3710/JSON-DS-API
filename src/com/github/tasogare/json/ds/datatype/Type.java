// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import com.github.tasogare.json.ds.MetaObject;

public interface Type {

    /**
     * 型が一致するかを調べます。
     * <p>
     * このメソッドは{@linkplain Object#equals(Object)}を洗練するものではありません。 型の一致する条件はnominal
     * typeかstructural typeかで異なります。
     * <p>
     * もし比較される型と自身の型が同じ場合対称性があります。 つまり、T1とT2が同じ型の場合、
     * {@code T1.isTypeOf(T2) == T2.isTypeOf(T1)}となります。 {@link AnyType}は全ての型と
     * {@link AnyType.NonNullable}は{@link NullType}以外の全ての型と一致します。
     * <p>
     * このメソッドは内部APIであり直接使われることはありません。 ある値がある型と一致するかどうかを調べたい場合は
     * {@link MetaObject#is(Object, Type)}を利用します。
     * 
     * @param t
     *            比較される型
     * @return 型が一致するなら{@code true}、それ以外なら{@code false}を返します。
     */
    boolean isTypeOf(Type t);
}
