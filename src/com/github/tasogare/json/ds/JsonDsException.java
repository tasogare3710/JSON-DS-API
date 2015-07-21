// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds;

/**
 * @author tasogare
 *
 */
public class JsonDsException extends RuntimeException {

    public static enum StandardErrors {
        /**
         * 文法の間違いや再宣言のエラー
         */
        SyntaxError,
        /**
         * 型に関する間違いのエラー
         */
        TypeError,
        /**
         * 解決できない参照のエラー
         */
        ReferenceError;
    }

    private static final long serialVersionUID = 5778929708992048465L;
    private final StandardErrors errorType;

    public JsonDsException(StandardErrors error) {
        this.errorType = error;
    }

    public JsonDsException(String message, StandardErrors error) {
        super(message);
        this.errorType = error;
    }

    public JsonDsException(Throwable cause, StandardErrors error) {
        super(cause);
        this.errorType = error;
    }

    public JsonDsException(String message, Throwable cause, StandardErrors error) {
        super(message, cause);
        this.errorType = error;
    }

    public JsonDsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, StandardErrors error){
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorType = error;
    }

    public final StandardErrors getErrorType() {
        return errorType;
    }
}
