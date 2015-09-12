// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype.driver;

import java.io.Reader;
import java.net.URL;

import com.github.tasogare.json.ds.JsonDsException;
import com.github.tasogare.json.ds.parser.ParserException;

/**
 * JSON-DS API仕様にはプロセッサが含まれないためテスト・ドライバが必要
 * @author tasogare
 *
 */
public interface DatatypeSchemaProcessorTestDriver {

    public static enum Mode {
        Extend,
        Standard,
        Strict;
    }

    /**
     * 
     * @param jsds
     * @param sourceName
     * @throws ParserException
     *             XXX: {@linkplain ParserException}は{@linkplain JsonDsException}
     *             の原因としてラップされて扱われるように変更されるかもしれない
     * @throws JsonDsException
     */
    void process(Reader jsds, URL sourceName) throws ParserException, JsonDsException;

    /**
     * 
     * @param jsds
     * @param sourceName
     * @throws ParserException
     *             XXX: {@linkplain ParserException}は{@linkplain JsonDsException}
     *             の原因としてラップされて扱われるように変更されるかもしれない
     * @throws JsonDsException
     */
    void process(String jsds, URL sourceName) throws ParserException, JsonDsException;

}
