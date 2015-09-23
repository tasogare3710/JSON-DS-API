// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.tasogare.json.ds.parser.ParserException;
import com.github.tasogare.json.ds.parser.SourceInfo;

@RunWith(Suite.class)
@SuiteClasses({
    ParserAllTest.class,
    AstAllTest.class,
    DatatypeAllTest.class,
    MetaObjectAllTest.class
})
public class AllTest {

    public static BufferedReader newReader(URL url) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
    }

    public static BufferedReader newReader(String name, Class<?> clazz) throws IOException {
        final InputStream is = clazz.getClassLoader().getResourceAsStream(name);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    public static void reportError(ParserException e) {
        final SourceInfo info = e.getSourceInfo();
        System.err.println(info.getSourceName());
        System.err.println("row: " + info.getRow() + " col: " + info.getColumn());
        System.err.println(info.getSource().renge(info.getLineStart(), info.getPosition()));
        final StringBuilder sb = new StringBuilder();
        for(int i=0; i< info.getColumn() - 1; i++){
            sb.append(" ");
        }
        System.err.println(sb.append("^").toString());
        e.printStackTrace();
    }
}
