// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import com.github.tasogare.json.ds.RuntimeSemanticsException;

/**
 * @deprecated {@link RuntimeSemanticsException}へ移行
 * @author tasogare
 *
 */
@Deprecated
public final class ParserException extends RuntimeException {
    private static final long serialVersionUID = 6185281711673419231L;

    private final SourceInfo sourceInfo;

    public ParserException(SourceInfo sourceInfo) {
        super();
        this.sourceInfo = sourceInfo;
    }

    public ParserException(String message, SourceInfo sourceInfo) {
        super(message);
        this.sourceInfo = sourceInfo;
    }

    public ParserException(Throwable cause, SourceInfo sourceInfo) {
        super(cause);
        this.sourceInfo = sourceInfo;
    }

    public ParserException(String message, Throwable cause, SourceInfo sourceInfo) {
        super(message, cause);
        this.sourceInfo = sourceInfo;
    }

    public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
        SourceInfo sourceInfo)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.sourceInfo = sourceInfo;
    }

    /**
     * @return the sourceInfo
     */
    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
