// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import java.util.HashSet;

public class Intrinsics {

    /**
     * any language type
     */
    public static final AnyType anyType = new AnyType();

    /**
     * non-nullable any specification type
     */
    public static final AnyType.NonNullable nonNullableAnyType = new AnyType.NonNullable();

    /**
     * null language type
     */
    public static final NullType nullType = new NullType();

    /**
     * Undefined specification type
     */
    public static final UndefindType undefindType = new UndefindType();

    /**
     * boolean language type
     */
    public static final BooleanType booleanType = new BooleanType();

    /**
     * number language type
     */
    public static final NumberType numberType = new NumberType();

    /**
     * string language type
     */
    public static final StringType stringType = new StringType();

    private Intrinsics() {
    }

    /**
     * 
     * @param t
     * @return
     * @throws IllegalArgumentException {@code t}が{@link AnyType}のとき
     */
    public static UnionType newNullable(Type t) throws IllegalArgumentException {
        if(t instanceof AnyType){
            throw new IllegalArgumentException();
        }
        final HashSet<Type> m = new HashSet<>();
        m.add(t);
        m.add(nullType);
        return new UnionType(m);
    }
}
