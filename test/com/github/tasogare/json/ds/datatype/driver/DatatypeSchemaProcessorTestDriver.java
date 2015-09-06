// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype.driver;

import java.io.Reader;
import java.net.URL;

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
     * TODO throws宣言必要
     * @param jsds
     * @param sourceName
     */
    void process(Reader jsds, URL sourceName);

    /**
     * TODO throws宣言必要
     * @param jsds
     * @param sourceName
     */
    void process(String jsds, URL sourceName);

}
