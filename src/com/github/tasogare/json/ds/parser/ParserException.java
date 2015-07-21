// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

/**
 * 
 * @author tasogare
 *
 */
public class ParserException extends RuntimeException {
    private static final long serialVersionUID = 6185281711673419231L;

    private final int startPosition;
    private final int endPosition;
    private final String sourceName;
    private final Source source;

    public ParserException(final int startPosition, final int endPosition, final String sourceName, final Source source) {
        super();
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.sourceName = sourceName;
        this.source = source;
    }

    public ParserException(final String message, final int startPosition, final int endPosition, final String sourceName, final Source source) {
        super(message);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.sourceName = sourceName;
        this.source = source;
    }

    public ParserException(final Throwable cause, final int startPosition, final int endPosition, final String sourceName, final Source source) {
        super(cause);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.sourceName = sourceName;
        this.source = source;
    }

    public ParserException(final String message, final Throwable cause, final int startPosition, final int endPosition, final String sourceName, final Source source) {
        super(message, cause);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.sourceName = sourceName;
        this.source = source;
    }

    public ParserException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace, final int startPosition, final int endPosition, final String sourceName, final Source source) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.sourceName = sourceName;
        this.source = source;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Source getSource() {
        return source;
    }
}
