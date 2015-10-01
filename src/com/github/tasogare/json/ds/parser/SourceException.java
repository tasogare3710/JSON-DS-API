// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

/**
 * @deprecated not used
 * @author tasogare
 *
 */
@Deprecated
public class SourceException extends RuntimeException {

    private static final long serialVersionUID = -2378572089292572246L;

    public SourceException() {
        super();
    }

    public SourceException(String message) {
        super(message);
    }

    public SourceException(Throwable cause) {
        super(cause);
    }

    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
