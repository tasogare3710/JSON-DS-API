// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.github.tasogare.json.ds.RuntimeSemanticsException.StandardErrors;
import com.github.tasogare.json.ds.datatype.Intrinsics;
import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.TypeException;
import com.github.tasogare.json.ds.datatype.UndefindType;

/**
 * TODO 仕様を煮詰める
 * メタオブジェクトを管理します。
 * 仕様によって定義されている型はオプションを除いてすべて登録済みです。
 * anyとnon-nullable anyはそれぞれ{@code *}と{@code *!}というキーで登録されます。
 * type valueが{@link UndefindType}の型は識別子のバインドがすでに作られていますが値が存在しないことを表します。
 * これはその型が定義される前に識別子が前方参照されていることを表します。
 * 
 * @author tasogare
 *
 * @param <T>
 */
public abstract class MetaObject<T> {

    private final HashMap<String, Type> types;
    public MetaObject() {
        types = new HashMap<String, Type>();
        // standard builtin meta-objects
        registerMetaObject("*", Intrinsics.anyType);
        registerMetaObject("*!", Intrinsics.nonNullableAnyType);
        registerMetaObject("null", Intrinsics.nullType);
        registerMetaObject("boolean", Intrinsics.booleanType);
        registerMetaObject("number", Intrinsics.numberType);
        registerMetaObject("string", Intrinsics.stringType);
    }

    /**
     * 
     * @param name
     * @param type
     * @throws RuntimeSemanticsException
     */
    public final void registerMetaObject(final String name, Type type) throws RuntimeSemanticsException {
        if(types.containsKey(name) && types.get(name) != Intrinsics.undefindType){
            throw new RuntimeSemanticsException("cannot change immutable binding:" + type, StandardErrors.TypeError);
        }
        types.put(name, type);
    }

    /**
     * 
     * @param name
     * @return
     */
    public final Type getMetaObject(final String name){
        return types.get(name);
    }

    public final Set<String> registerKeys(){
        return types.keySet();
    }

    public final Collection<Type> registerValues(){
        return types.values();
        
    }

    public final Set<Map.Entry<String, Type>> registerEntrySet(){
        return types.entrySet();
        
    }

    /**
     * lhs := {@code value}
     * rhs := {@code type}
     * 
     * lhs is rhs;
     * @param value
     * @param type
     * @return
     * @throws TypeException
     */
    public final boolean is(final T value, final Type type) throws TypeException {
        final Type valueOfType = typeOf(value);
        // 型が違う場合対称性がないのでこっちが正しい
        return type.isTypeOf(valueOfType);
//        return valueOfType.isTypeOf(type);
    }

    /**
     * rhs := {@code value}
     * 
     * typeof rhs;
     * @param value
     * @return
     * @throws TypeException
     */
    public abstract Type typeOf(T value) throws TypeException;

}
