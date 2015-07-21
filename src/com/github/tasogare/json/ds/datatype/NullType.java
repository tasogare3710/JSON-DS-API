// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

public final class NullType implements Type {

    protected NullType() {
    }

    @Override
    public boolean isTypeOf(Type t) {
        if(t instanceof ReferenceType){
            t = ((ReferenceType)t).getValue();
        }

        return t instanceof NullType;
    }
}
