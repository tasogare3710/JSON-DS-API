// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * raw入力sourceのラッパー。
 * 
 * @author tasogare
 *
 */
public final class Source {

    public static final int BOM = '\uFEFF';
    public static final int EOF = -1;

    public static boolean isIdentifierPart(int c) {
        return Character.isJavaIdentifierPart(c);
    }

    public static boolean isIdentifierStart(int c) {
        return Character.isJavaIdentifierStart(c);
    }

    /**
     * <pre>
     * Code Point | Unicode Name        | Abbreviation 
     * U+000A     | LINE FEED (LF)      | &lt;LF&gt;
     * U+000D     | CARRIAGE RETURN     | (CR)  &lt;CR&gt;
     * U+2028     | LINE SEPARATOR      | &lt;LS&gt;
     * U+2029     | PARAGRAPH SEPARATOR | &lt;PS&gt;
     * </pre>
     * 
     * @param c
     * @return
     * @see ES6 spec section 11.3 Line Terminators Table 33 — Line Terminator
     *      Code Points
     */
    public static boolean isLineTerminator(int c) {
        return c == 0x0A || c == 0x0D || c == 0x2028 || c == 0x2029;
    }

    /**
     * Unicode category Zs
     * 
     * @param c
     * @return
     */
    public static boolean isSpaceSeparator(final int c) {
        return c == 0x20 || c == 0xA0 || c == 0x1680 || c == 0x180E || (c >= 0x2000 && c <= 0x200A) || c == 0x202F || c == 0x205F || c == 0x3000;
    }

    /**
     * <pre>
     * Code Point     | Name                                               | Abbreviation
     * U+0009         | CHARACTER TABULATION                               | &lt;TAB&gt;
     * U+000B         | LINE TABULATION                                    | &lt;VT&gt;
     * U+000C         | FORM FEED (FF)                                     | &lt;FF&gt;
     * U+0020         | SPACE                                              | &lt;SP&gt;
     * U+00A0         | NO-BREAK SPACE                                     | &lt;NBSP&gt;
     * U+FEFF         | ZERO WIDTH NO-BREAK SPACE                          | &lt;ZWNBSP&gt;
     * Other category |“Zs”Any other Unicode “Separator, space” code point | &lt;USP&gt;
     * </pre>
     * 
     * @param c
     * @return
     * @see ES6 spec section 11.2 White Space - Table 32 - White Space Code
     *      Points
     */
    public static boolean isWhitespace(final int c) {
        return (c == 0x09 || c == 0x0B || c == 0x0C || c == 0x20) || c == 0xA0 || c == 0xFEFF || isSpaceSeparator(c);
    }

    /**
     * {@code reader}はこのメソッド内ですべて消費され、そののち閉じられます。
     * 
     * @param reader
     * @return
     * @throws UncheckedIOException
     */
    private static String fromReader(final BufferedReader reader) throws UncheckedIOException {
        final char[] cbuff = new char[5120];
        final StringBuilder sb = new StringBuilder(5120);
        try {
            int len;
            while ((len = reader.read(cbuff)) != -1) {
                sb.append(cbuff, 0, len);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final String source;

    private final int length;

    private int postion;

    /**
     * 
     * @param raw
     * @throws UncheckedIOException
     */
    public Source(final BufferedReader raw) throws UncheckedIOException {
        this(fromReader(raw));
    }

    public Source(final String raw) {
        this.source = raw;
        length = raw.length();
    }

    public int length() {
        return length;
    }

    public int postion() {
        return postion;
    }

    boolean match(final int c) throws IndexOutOfBoundsException {
        return match(0, c);
    }

    boolean match(final int pos, final int c) throws IndexOutOfBoundsException {
        return c == peek(pos);
    }

    /**
     * @param c
     *            unicode character
     * @return match if {@code true}, otherwise {@code false}.
     * @throws IndexOutOfBoundsException
     */
    boolean matchWithAdvance(final int c) throws IndexOutOfBoundsException {
        return matchWithAdvance(0, c);
    }

    boolean matchWithAdvance(final int pos, final int c) throws IndexOutOfBoundsException {
        if (c != peek(pos)) {
            return false;
        }
        postion += Character.charCount(c);
        return true;
    }

    /**
     * 
     * @param c
     *            unicode character
     * @throws SourceException
     * @throws IndexOutOfBoundsException
     */
    void mustMatch(final int c) throws SourceException, IndexOutOfBoundsException {
        if (c != peek()) {
            throw new SourceException(String.valueOf(c));
        }
    }

    /**
     * 
     * @param c
     *            unicode character
     * @throws SourceException
     * @throws IndexOutOfBoundsException
     */
    void mustMatchWithAdvance(final int c) throws SourceException, IndexOutOfBoundsException {
        mustMatch(c);
        postion += Character.charCount(c);
    }

    /**
     * 
     * @return
     * @throws IndexOutOfBoundsException
     */
    int next() throws IndexOutOfBoundsException {
        final int cp = peek();
        postion += Character.charCount(cp);
        return cp;
    }

    int peek() throws IndexOutOfBoundsException {
        return peek(0);
    }

    /**
     * 一度EOFに達した場合、その後何度呼び出しても{@link #EOF} を返します。
     * 
     * @param offset
     * @return
     * @throws IndexOutOfBoundsException {@code postion + offset < 0}のとき
     */
    int peek(int offset) throws IndexOutOfBoundsException {
        if (postion + offset >= length) {
            return EOF;
        }
        return source.codePointAt(postion + offset);
    }

    void pushback(final int i) {
        assert 0 < i;
        final int newPos = postion - i;
        if (0 > newPos) {
            throw new IndexOutOfBoundsException(String.valueOf(newPos));
        }
        postion = newPos;
    }

    String renge(final int start, final int end) {
        return source.substring(start, end);
    }
}
