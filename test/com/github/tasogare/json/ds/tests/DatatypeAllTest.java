// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.tasogare.json.ds.datatype.AnyTypeTest;
import com.github.tasogare.json.ds.datatype.ArrayTypeTest;
import com.github.tasogare.json.ds.datatype.BooleanTypeTest;
import com.github.tasogare.json.ds.datatype.NullTypeTest;
import com.github.tasogare.json.ds.datatype.NumberTypeTest;
import com.github.tasogare.json.ds.datatype.RecordTypeTest;
import com.github.tasogare.json.ds.datatype.StringTypeTest;
import com.github.tasogare.json.ds.datatype.UnionTypeTest;

@RunWith(Suite.class)
@SuiteClasses({AnyTypeTest.class, ArrayTypeTest.class, BooleanTypeTest.class, NullTypeTest.class, NumberTypeTest.class,
    RecordTypeTest.class, StringTypeTest.class, UnionTypeTest.class })
public class DatatypeAllTest {
}
