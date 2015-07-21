// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayType implements StructuralType {

    final List<Type> fixedElements;
    final Type variableElements;

    public ArrayType(final List<Type> fixedElements, final Type variableElements) {
        this.fixedElements = Collections.unmodifiableList(new ArrayList<>(fixedElements));
        this.variableElements = variableElements;
    }

    public ArrayType(final List<Type> fixedElements) {
        this(Collections.unmodifiableList(new ArrayList<>(fixedElements)), null);
    }

    public ArrayType(final Type variableElements){
        this(Collections.emptyList(), variableElements);
    }

    public ArrayType() {
        this(Collections.emptyList());
    }

    /**
     * VariableArrayの...TはZeroOrMoreなので[T, ...T]ならばTが一つ以上、[...T]ならTが0以上にならなければならない
     */
    @Override
    public boolean isTypeOf(Type t) {
        if(t instanceof ReferenceType){
            t = ((ReferenceType)t).getValue();
        }

        if(t instanceof AnyType){
            return true;
        }

        if( !(t instanceof ArrayType) ){
            return false;
        }
        final ArrayType other = (ArrayType) t;
        int i = 0;
        final int MAX = Math.min(this.fixedElements.size(), other.fixedElements.size());
        boolean difference = false;
        while(i < MAX){
            Type selfE = this.fixedElements.get(i);
            Type otherE = other.fixedElements.get(i);
            if(!selfE.isTypeOf(otherE)){
                difference = true;
                break;
            }
            i++;
        }
        // fixedElementsのsizeが同じ
        if(this.fixedElements.size() == other.fixedElements.size()){
            if(difference){
                // 残ったthis.fixedElementsの全てとother.variableElementsが同じであるか？
                for(int last = i; last<this.fixedElements.size(); last++){
                    if(!this.fixedElements.get(last).isTypeOf(other.variableElements)){
                        return false;
                    }
                }
                return true;
            } else {
                boolean s = this.variableElements == null;
                boolean o = other.variableElements == null;
                if(s && o){
                    return !difference;
                }else if(!s && !o){
                    return this.variableElements.isTypeOf(other.variableElements);
                }else if(!s && o){
                    return !difference;
                }else if(s && !o){
                    return !difference;
                }
                throw new AssertionError();
            }
        } else if(this.fixedElements.size() < other.fixedElements.size()){
            return eq(i, this, other);
        }else if(this.fixedElements.size() > other.fixedElements.size()){
            return eq(i, other, this);
        }
        throw new AssertionError();
        //return false;
    }

    private boolean eq(int i, ArrayType a, ArrayType b) {
        if (a.variableElements == null) {
            return false;
        } else {
            if (b.variableElements == null) {
                for (int last = i; last < b.fixedElements.size(); last++) {
                    if (!b.fixedElements.get(last).isTypeOf(a.variableElements)) {
                        return false;
                    }
                }
                return true;
            } else {
                for (int last = i; last < b.fixedElements.size(); last++) {
                    if (!b.fixedElements.get(last).isTypeOf(a.variableElements)) {
                        return false;
                    }
                }
                return a.variableElements.isTypeOf(b.variableElements);
            }
        }
    }

    public List<Type> getFixedElements() {
        return fixedElements;
    }

    public Type getVariableElements() {
        return variableElements;
    }

}
