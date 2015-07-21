// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.tasogare.json.ds.parser.ParserTest;
import com.github.tasogare.json.ds.parser.ReaderToSource;
import com.github.tasogare.json.ds.parser.SourceOfZeroTest;
import com.github.tasogare.json.ds.parser.TokenStreamTest;

@RunWith(Suite.class)
@SuiteClasses({
    ParserTest.class,
    ReaderToSource.class,
    SourceOfZeroTest.class,
    TokenStreamTest.class
})
public class ParserAllTest {
}
