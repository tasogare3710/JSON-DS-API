// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds;

import com.github.tasogare.json.ds.parser.SourceInfo;

/**
 * XXX: このクラスのフィールドsourceInfoは現在のところ使用しません。
 * そのため関連するメソッド、コンストラクタは非推奨ですが将来利用可能になる可能性があります。
 * @author tasogare
 *
 */
public class RuntimeSemanticsException extends RuntimeException {

    /**
     * @author tasogare
     *
     */
    public static enum StandardErrors {
        /**
         * 内部エラー
         */
        InternalError,
        /**
         * 解決できない参照のエラー
         */
        ReferenceError,
        /**
         * 型に関する間違いのエラー
         */
        TypeError,
        /**
         * 解決できないURIのエラー
         */
        URIError;
    }

    private static final long serialVersionUID = 5778929708992048465L;
    private final StandardErrors errorType;
    @Deprecated
    private final SourceInfo sourceInfo;

    public RuntimeSemanticsException(StandardErrors error) {
        this(error, (SourceInfo) null);
    }

    @Deprecated
    public RuntimeSemanticsException(StandardErrors error, SourceInfo sourceInfo) {
        super();
        this.errorType = error;
        this.sourceInfo = sourceInfo;
    }

    public RuntimeSemanticsException(String message, StandardErrors error) {
        this(message, error, (SourceInfo) null);
    }

    @Deprecated
    public RuntimeSemanticsException(String message, StandardErrors error, SourceInfo sourceInfo) {
        super(message);
        this.errorType = error;
        this.sourceInfo = sourceInfo;
    }

    @Deprecated
    public RuntimeSemanticsException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, StandardErrors error, SourceInfo sourceInfo)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorType = error;
        this.sourceInfo = sourceInfo;
    }

    public RuntimeSemanticsException(String message, Throwable cause, StandardErrors error) {
        this(message, cause, error, (SourceInfo) null);
    }

    public RuntimeSemanticsException(String message, Throwable cause, StandardErrors error, SourceInfo sourceInfo) {
        super(message, cause);
        this.errorType = error;
        this.sourceInfo = sourceInfo;
    }

    public RuntimeSemanticsException(Throwable cause, StandardErrors error, SourceInfo sourceInfo) {
        super(cause);
        this.errorType = error;
        this.sourceInfo = sourceInfo;
    }

    public final StandardErrors getErrorType() {
        return errorType;
    }

    /**
     * @deprecated
     * @return the source info
     */
    @Deprecated
    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
