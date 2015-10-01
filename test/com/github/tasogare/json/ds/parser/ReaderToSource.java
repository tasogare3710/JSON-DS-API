// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static com.github.tasogare.json.ds.tests.AllTest.newReader;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.parser.Source;

public class ReaderToSource {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/ReaderTest.txt";
        try (final BufferedReader r = newReader(name, getClass())) {
            final Source source = new Source(r);
            final CharArrayWriter caw = new CharArrayWriter();
            int codePoint;
            while ((codePoint = source.next()) != Source.EOF) {
                caw.write(Character.toChars(codePoint));
            }
            System.out.println(caw.toString());
        }
    }

}
