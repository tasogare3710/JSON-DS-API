// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype;

import java.util.Set;

public final class StringType extends NominalType {

    protected StringType() {
        super();
    }

    @Override
    protected void computeSupers(final Set<Type> supers) {
        supers.add(this);
    }
}
