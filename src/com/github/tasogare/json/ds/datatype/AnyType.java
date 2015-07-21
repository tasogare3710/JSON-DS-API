// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

/**
 * 
 * @author tasogare
 *
 */
public class AnyType implements Type {

    /**
     * non-nullable any type.
     * @author tasogare
     *
     */
    public static final class NonNullable extends AnyType {
        protected NonNullable(){
        }

        @Override
        public boolean isTypeOf(Type t) {
            if(t instanceof ReferenceType){
                t = ((ReferenceType)t).getValue();
            }

            return t != Intrinsics.nullType;
        }
    }

    protected AnyType() {
    }

    @Override
    public boolean isTypeOf(Type t) {
        return true;
    }
}
