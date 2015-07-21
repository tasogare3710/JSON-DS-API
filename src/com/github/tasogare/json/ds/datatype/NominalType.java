// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class NominalType implements Type {

    protected final Set<Type> supers;

    public NominalType(){
        final HashSet<Type> tmp = new HashSet<>();
        computeSupers(tmp);
        this.supers = Collections.unmodifiableSet(tmp);
    }

    @Override
    public boolean isTypeOf(Type t){
        if(t instanceof ReferenceType){
            t = ((ReferenceType)t).getValue();
        }

        if(t instanceof StructuralType){
            return false;
        }
        if(t instanceof AnyType){
            return true;
        }
        for(Type superT : supers){
            if(superT.getClass().isInstance(t)){
                return true;
            }
        }
        return false;
    }

    protected abstract void computeSupers(Set<Type> supers);

}
