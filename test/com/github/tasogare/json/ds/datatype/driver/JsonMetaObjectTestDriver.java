// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype.driver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import com.github.tasogare.json.ds.datatype.ArrayType;
import com.github.tasogare.json.ds.datatype.Intrinsics;
import com.github.tasogare.json.ds.datatype.MetaObject;
import com.github.tasogare.json.ds.datatype.RecordType;
import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.TypeException;
import com.github.tasogare.json.ds.datatype.UnionType;

public class JsonMetaObjectTestDriver extends MetaObject<JsonValue> {

    public JsonMetaObjectTestDriver() {
        super();
    }

    @Override
    public Type typeOf(final JsonValue value) throws TypeException {
        switch (value.getValueType()) {
        case NULL:
            return getTypeOfNull();
        case TRUE:
            return getTypeOfBoolean();
        case FALSE:
            return getTypeOfBoolean();
        case NUMBER:
            return getTypeOfNumber();
        case STRING:
            return getTypeOfString();
        case OBJECT:
            return getTypeOfObject((JsonObject)value);
        case ARRAY:
            return getTypeOfArray((JsonArray)value);
        default:
            throw new AssertionError();
        }
    }

    private Type getTypeOfString() {
        return Intrinsics.stringType;
    }

    private Type getTypeOfNumber() {
        return Intrinsics.numberType;
    }

    private Type getTypeOfBoolean() {
        return Intrinsics.booleanType;
    }

    private Type getTypeOfNull() {
        return Intrinsics.nullType;
    }

    private RecordType getTypeOfObject(JsonObject object){
        final HashSet<RecordType.Field> fields = new HashSet<>();
        for(final Map.Entry<String,JsonValue> e : object.entrySet()){
            final String name = e.getKey();
            final Type type = typeOf(e.getValue());
            fields.add(new RecordType.Field(name, type));
        }
        return new RecordType(fields);
    }

    private ArrayType getTypeOfArray(JsonArray array) {
        final ArrayList<Type> typeList = new ArrayList<>();
        for(final JsonValue e : array) {
            final Type current = typeOf(e);
            typeList.add(current);
        }
        return new ArrayType(typeList);
    }

    @SuppressWarnings(value="unused")
    private Type getTypeOfArrayOldSpecComplianced(JsonArray array) {
        final ArrayList<Type> typeList = new ArrayList<>();
        Type prev = null;
        // 一回目は必ずtrue
        boolean allMaths = true;
        for(final JsonValue e : array) {
            final Type current = typeOf(e);
            typeList.add(current);
            // 二回目以降で今までのすべての型が同じ場合
            if(prev != null && allMaths){
                allMaths = current.isTypeOf(prev);
            }
            prev = current;
        }
        if(allMaths){
            // 全ての要素の型が同じ場合、二通りの解釈ができる。
            // var a = [1,2,3];
            // の場合...
            // type FA = [number, number, number];
            //      or
            // type VA = [...number];
            final ArrayType a = new ArrayType(Collections.<Type>emptyList(), typeList.get(0));
            final ArrayType b = new ArrayType(typeList);
            final HashSet<Type> members = new HashSet<>();
            members.add(a);
            members.add(b);
            return new UnionType(members);
        }

        final int beginVariableArrayIndex = computeBeginVariableArrayIndex(typeList);
        if(beginVariableArrayIndex == -1){
            return new ArrayType(typeList);
        }
        final List<Type> fixedArray = typeList.subList(0, beginVariableArrayIndex + 1);
        final Type variableArray = typeList.get(beginVariableArrayIndex + 1);
        return new ArrayType(fixedArray, variableArray);
    }

    /**
     * variable array-typeの要素が始まるこれを含まないindexを返す。
     * ただし、{@literal -1}はvariable array-typeの要素が存在しないことを表す。
     * 
     * @param typeList
     * @return variable array-typeの要素が始まる数。
     */
    private int computeBeginVariableArrayIndex(ArrayList<Type> typeList){
        final ListIterator<Type> ite = typeList.listIterator(typeList.size());
        Type prev = null;
        while(ite.hasPrevious()){
            final int lastIndex = ite.previousIndex();
            final Type current = ite.previous();
            if(prev != null && !current.isTypeOf(prev)){
                return lastIndex;
            }
            prev = current;
        }
        return -1;
    }
}
