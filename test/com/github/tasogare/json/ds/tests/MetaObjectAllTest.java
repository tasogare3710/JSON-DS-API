// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.tasogare.json.ds.datatype.JsonDsProcessorHoistableTest;
import com.github.tasogare.json.ds.datatype.JsonDsProcessorTest;
import com.github.tasogare.json.ds.datatype.JsonMetaObjectTest;

@RunWith(Suite.class)
@SuiteClasses({JsonMetaObjectTest.class, JsonDsProcessorTest.class, JsonDsProcessorHoistableTest.class })
public class MetaObjectAllTest {
}
