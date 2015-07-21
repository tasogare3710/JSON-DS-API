// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

public class ReferenceType implements Type {

    private final MetaObject<?> metaObjects;
    private final String name;
    public ReferenceType(MetaObject<?> metaObjects, String name) {
        this.metaObjects = metaObjects;
        this.name = name;
    }

    @Override
    public boolean isTypeOf(Type t) {
        return getValue().isTypeOf(t);
    }

    public final Type getValue(){
        return metaObjects.getMetaObject(name);
    }
}
