// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UnionType implements StructuralType {

    private final Set<Type> members;

    public UnionType(final Set<Type> members) {
        this.members = Collections.unmodifiableSet(new HashSet<Type>(members));
    }

    public Set<Type> getMembers() {
        return members;
    }

    @Override
    public boolean isTypeOf(Type t) {
        if(t instanceof ReferenceType){
            t = ((ReferenceType)t).getValue();
        }

        if(t instanceof AnyType){
            return true;
        }

        if (t instanceof UnionType) {
            final UnionType other = (UnionType) t;
            // 自分と対象がともに空集合
            if (this.members.isEmpty() && other.members.isEmpty()) {
                return true;
            }else if(this.members.size() == other.members.size()){
                if(this == other){
                    return true;
                }
                for(final Type m : this.members){
                    final HashSet<Boolean> detected = new HashSet<>();
                    for(final Type om : other.members){
                        detected.add(m.isTypeOf(om));
                    }
                    if(!detected.contains(Boolean.TRUE)){
                        return false;
                    }
                }
                return true;
            }else if(this.members.isEmpty()){
                assert !other.members.isEmpty();
                return false;
            }else if(other.members.isEmpty()){
                assert !this.members.isEmpty();
                for(final Type m : this.members){
                    if(m instanceof UnionType && ((UnionType)m).members.isEmpty()){
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        for(final Type m : members){
            if(m.isTypeOf(t)){
                return true;
            }
        }
        return false;
    }
}
