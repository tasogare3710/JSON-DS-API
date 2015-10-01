// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds;

import com.github.tasogare.json.ds.parser.SourceInfo;

/**
 * JSON-DSのStaticSemanticsに関するErrorを扱う例外クラスです。
 * 
 * @author tasogare
 *
 */
public class StaticSemanticsException extends Exception {

    /**
     * 
     * @author tasogare
     *
     */
    public static enum StandardErrors {
        /**
         * 文法の間違いや再宣言のエラー
         */
        SyntaxError;
    }

    private static final long serialVersionUID = -1458573795574817381L;

    public static StaticSemanticsException newSyntaxError(SourceInfo sourceInfo) {
        return new StaticSemanticsException(StandardErrors.SyntaxError, sourceInfo);
    }

    public static StaticSemanticsException newSyntaxError(String message, SourceInfo sourceInfo) {
        return new StaticSemanticsException(message, StandardErrors.SyntaxError, sourceInfo);
    }

    public static StaticSemanticsException newSyntaxError(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, SourceInfo sourceInfo)
    {
        return new StaticSemanticsException(message, cause, enableSuppression, writableStackTrace,
            StandardErrors.SyntaxError, sourceInfo);
    }

    public static StaticSemanticsException newSyntaxError(String message, Throwable cause, SourceInfo sourceInfo) {
        return new StaticSemanticsException(message, cause, StandardErrors.SyntaxError, sourceInfo);
    }

    public static StaticSemanticsException newSyntaxError(Throwable cause, SourceInfo sourceInfo) {
        return new StaticSemanticsException(cause, StandardErrors.SyntaxError, sourceInfo);
    }

    private final StandardErrors errorType;

    private final SourceInfo sourceInfo;

    /**
     * 
     * @param errorType
     *            エラーの種類
     * @param sourceInfo
     *            JSON-DSソースに関する情報
     */
    public StaticSemanticsException(StandardErrors errorType, SourceInfo sourceInfo) {
        super();
        this.errorType = errorType;
        this.sourceInfo = sourceInfo;
    }

    public StaticSemanticsException(String message, StandardErrors errorType, SourceInfo sourceInfo) {
        super(message);
        this.errorType = errorType;
        this.sourceInfo = sourceInfo;
    }

    public StaticSemanticsException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace, StandardErrors errorType, SourceInfo sourceInfo)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorType = errorType;
        this.sourceInfo = sourceInfo;
    }

    public StaticSemanticsException(String message, Throwable cause, StandardErrors errorType, SourceInfo sourceInfo) {
        super(message, cause);
        this.errorType = errorType;
        this.sourceInfo = sourceInfo;
    }

    public StaticSemanticsException(Throwable cause, StandardErrors errorType, SourceInfo sourceInfo) {
        super(cause);
        this.errorType = errorType;
        this.sourceInfo = sourceInfo;
    }

    /**
     * @return the errorType
     */
    public StandardErrors getErrorType() {
        return errorType;
    }

    /**
     * @return the sourceInfo
     */
    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }
}
