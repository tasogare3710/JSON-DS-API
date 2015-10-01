// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype.driver;

import java.io.Reader;
import java.net.URL;

import com.github.tasogare.json.ds.MetaObject;
import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;

/**
 * JSON-DS API仕様にはプロセッサが含まれないためテスト・ドライバが必要
 * 
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
     * @throws RuntimeSemanticsException
     * @throws StaticSemanticsException
     */
    void process(Reader jsds, URL sourceName) throws RuntimeSemanticsException, StaticSemanticsException;

    /**
     * 
     * @param jsds
     * @param sourceName
     * @throws RuntimeSemanticsException
     * @throws StaticSemanticsException
     */
    void process(String jsds, URL sourceName) throws RuntimeSemanticsException, StaticSemanticsException;

    MetaObject<?> getMetaObjects();
}
