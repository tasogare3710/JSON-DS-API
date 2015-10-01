// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import java.io.CharArrayWriter;
import java.util.Arrays;
import java.util.List;

import com.github.tasogare.json.ds.StaticSemanticsException;

/**
 * @author tasogare
 *
 */
public class TokenStream {

    private final Source source;
    private final String sourceName;
    private final CharArrayWriter buffer;
    private Token currentToken;
    private Token pendding;
    private int line;
    private int lineStart;

    public TokenStream(final Source source, final String sourceName) {
        this.source = source;
        this.sourceName = sourceName;
        this.buffer = new CharArrayWriter();
    }

    public SourceInfo createCurrentSourceInfo() {
        return new SourceInfo(row(), column(), lineStart(), position(), sourceName(), source());
    }

    /**
     * 
     * @return
     * @throws IllegalStateException {@code t != currentToken}のとき
     */
    public String asComment() throws IllegalStateException {
        return asContentOf(Token.Comment);
    }

    /**
     * 
     * @return
     * @throws IllegalStateException {@code t != currentToken}のとき
     */
    public String asEscapedIdentifier() throws IllegalStateException {
        return asContentOf(Token.EscapedIdentifier);
    }

    /**
     * 
     * @return
     * @throws IllegalStateException {@code t != currentToken}のとき
     */
    public String asIdentifier() throws IllegalStateException {
        return asContentOf(Token.Identifier);
    }

    /**
     * 
     * @return
     * @throws IllegalStateException {@code t != currentToken}のとき
     */
    public String asStringLiteral() throws IllegalStateException {
        return asContentOf(Token.StringLiteral);
    }

    public int column(){
        return position() - lineStart;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public int length() {
        return source.length();
    }

    /**
     * @return the lineStart
     */
    public int lineStart() {
        return lineStart;
    }

    public int position() {
        return source.position();
    }

    public void pushPendding(Token t){
        if(pendding != null){
            throw new IllegalStateException();
        }
        pendding = t;
    }

    /**
     * XXX: lineNumberの方が分かりやすいのではないか？
     * ゼロ起算の行番号を返します。
     * @return
     */
    public int row(){
        return line;
    }

    /**
     * この段階ではVirtual-Semicolonを挿入しない
     * 
     * @return
     * @throws StaticSemanticsException 
     */
    public Token scanToken() throws StaticSemanticsException {
        if(pendding != null){
            return popPendding();
        }

        int c;
        while (true) {
            c = source.next();
            if (c == Source.EOF) {
                return currentToken(Token.Eof);
            }
            if (Source.isWhitespace(c)) {
                continue;
            }
            // LineTerminatorSequence ::
            // <LF>
            // <CR> [lookahead ≠ <LF> ]
            // <LS>
            // <PS>
            // <CR> <LF>
            if (Source.isLineTerminator(c)) {
                if (c == 0x000D && source.match(0x000A)) {
                    source.next();
                    incrementLine(true);
                    return currentToken(Token.LineTerminator);
                } else {
                    incrementLine(false);
                    return currentToken(Token.LineTerminator);
                }
            }
            break;
        }

        switch (c) {
        case '"':
            return currentToken(readStringLiteral());
        case '\\':
            source.mustMatchWithAdvance('u');
            c = readUnicodeEscape();
            if (Source.isIdentifierStart(c)) {
                return currentToken(readIdentifier(c, true));
            }
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        case '/':
            // Comment ::
            //      MultiLineComment
            //      SingleLineComment
            if (source.matchWithAdvance('/')) {
                return currentToken(readSingleLineComment());
            } else if (source.matchWithAdvance('*')) {
                return currentToken(readMultiLineComment());
            }
            break;
        case '{':
            return currentToken(Token.LBrace);
        case '}':
            return currentToken(Token.RBrace);
        case '(':
            return currentToken(Token.LParen);
        case ')':
            return currentToken(Token.RParen);
        case '[':
            return currentToken(Token.LBracket);
        case ']':
            return currentToken(Token.RBracket);
        case '.':
            source.mustMatchWithAdvance('.');
            source.mustMatchWithAdvance('.');
            return currentToken(Token.TripleDot);
        case ',':
            return currentToken(Token.Comma);
        case ':':
            return currentToken(Token.Colon);
        case ';':
            return currentToken(Token.SemiColon);
        case '?':
            return currentToken(Token.Nullable);
        case '!':
            return currentToken(Token.NotNullable);
        case '=':
            return currentToken(Token.Assign);
        case '*':
            return currentToken(Token.Any);
        case '|':
            return currentToken(Token.Or);
        case 't':
            if (source.matchWithAdvance('y')) {
                if (source.matchWithAdvance('p')) {
                    if (source.matchWithAdvance('e')) {
                        return currentToken(Token.TypeOperator);
                    } else {
                        source.pushback(2);
                        break;
                    }
                } else {
                    source.pushback(1);
                    break;
                }
            } else {
                break;
            }
        case 'u':
            if (source.matchWithAdvance('s')) {
                if (source.matchWithAdvance('e')) {
                    return currentToken(Token.UsePragma);
                } else {
                    source.pushback(1);
                    break;
                }
            } else {
                break;
            }
        case 'i':
            if(source.matchWithAdvance('n')){
                if(source.matchWithAdvance('c')){
                    if(source.matchWithAdvance('l')){
                        if(source.matchWithAdvance('u')){
                            if(source.matchWithAdvance('d')){
                                if(source.matchWithAdvance('e')){
                                    return currentToken(Token.IncludePragma);
                                }else{
                                    source.pushback(5);
                                    break;
                                }
                            }else{
                                source.pushback(4);
                                break;
                            }
                        }else{
                            source.pushback(3);
                            break;
                        }
                    }else{
                        source.pushback(2);
                        break;
                    }
                }else{
                    source.pushback(1);
                    break;
                }
            }else {
                break;
            }
        case 'n':
            if (source.matchWithAdvance('u')) {
                if (source.matchWithAdvance('l')) {
                    if (source.matchWithAdvance('l')) {
                        return currentToken(Token.Null);
                    } else {
                        source.pushback(2);
                        break;
                    }
                } else {
                    source.pushback(1);
                    break;
                }
            } else {
                break;
            }
        }
        if (Source.isIdentifierStart(c)) {
            return currentToken(readIdentifier(c, false));
        }
        throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
    }

    /**
     * 
     * @param ignore 無視するトークン
     * @return
     * @throws StaticSemanticsException 
     */
    public Token scanTokenWithoutOf(Token... ignore) throws StaticSemanticsException {
        final List<Token> skips = Arrays.asList(ignore);
        final boolean commentSkip = skips.contains(Token.Comment);
        final boolean identifierSkip = skips.contains(Token.Identifier);
        final boolean escapedIdentifierSkip = skips.contains(Token.EscapedIdentifier);
        final boolean stringLiteralSkip = skips.contains(Token.StringLiteral);

        Token t = null;
        do {
            t = scanToken();
            // スキップしたトークンが文字列contentを持つ場合TokenStreamのバッファをクリアしなければならない
            //@formatter:off
            if (commentSkip           && t == Token.Comment            ||
                identifierSkip        && t == Token.Identifier         ||
                escapedIdentifierSkip && t == Token.EscapedIdentifier ||
                stringLiteralSkip     && t == Token.StringLiteral      )
            {
                buffer.reset();
            }
            //@formatter:on
        } while (skips.contains(t));
        return t;
    }

    public Source source() {
        return source;
    }

    public String sourceName(){
        return sourceName;
    }

    /**
     * 
     * @param t
     * @return
     * @throws IllegalStateException {@code t != currentToken}のとき
     */
    private String asContentOf(Token t) throws IllegalStateException {
        if (currentToken == t) {
            final String content = buffer.toString();
            buffer.reset();
            return content;
        }
        //XXX: IllegalStateExceptionでいいか？
        throw new IllegalStateException("currentToken !=" + t.toString());
    }

    private Token currentToken(Token t) {
        return currentToken = t;
    }

    private void incrementLine(boolean isCRLF){
        line += !isCRLF ? 1 : 2;
        lineStart = position();
    }

    private int numValue(int c) {
        return c >= '0' && c <= '9' ? c - '0' : c >= 'A' && c <= 'F' ? c - 'A' + 10 : c >= 'a' && c <= 'f' ? c - 'a' + 10 : -1;
    }

    private Token popPendding() {
        Token tmp = pendding;
        pendding = null;
        return tmp;
    }

    private Token readIdentifier(int c, boolean hasEscape) throws StaticSemanticsException {
        buffer.write(c);
        while (true) {
            c = source.next();
            if (Source.isIdentifierPart(c)) {
                buffer.write(c);
            } else if (c == '\\') {
                hasEscape = true;
                source.mustMatchWithAdvance('u');
                c = readUnicodeEscape();
                if (!Source.isIdentifierPart(c)) {
                    throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
                }
                buffer.write(c);
            } else {
                source.pushback(Character.charCount(c));
                break;
            }
        }

        final String word = buffer.toString();
        Token t = readReservedIdentifier(word);
        if (hasEscape) {
            return t.toEscaped();
        }
        return t;
    }

    private Token readMultiLineComment() throws StaticSemanticsException {
        assert source.match(-1, '*');
        //@formatter:off
        // MultiLineComment ::
        //      /* MultiLineCommentChars opt */
        //
        // MultiLineCommentChars ::
        //      MultiLineNotAsteriskChar MultiLineCommentChars opt
        //      * PostAsteriskCommentChars opt
        //
        // PostAsteriskCommentChars ::
        //      MultiLineNotForwardSlashOrAsteriskChar MultiLineCommentChars opt
        //      * PostAsteriskCommentChars opt
        //
        // MultiLineNotAsteriskChar ::
        //      SourceCharacter but not *
        //
        // MultiLineNotForwardSlashOrAsteriskChar ::
        //      SourceCharacter but not one of / or *
        //@formatter:on
        // /* MultiLineCommentChars opt */
        do {
            final int c = source.next();
            if (c == Source.EOF) {
                throw StaticSemanticsException.newSyntaxError("EOF", createCurrentSourceInfo());
            }
            // LineTerminatorSequence ::
            // <LF>
            // <CR> [lookahead ≠ <LF> ]
            // <LS>
            // <PS>
            // <CR> <LF>
            if (Source.isLineTerminator(c)) {
                if (c == 0x000D && source.match(0x000A)) {
                    incrementLine(false);
                } else {
                    incrementLine(false);
                }
            }
            if (c == '*' && source.matchWithAdvance('/')) {
                return Token.Comment;
            }
            buffer.write(c);
        } while (true);
    }

    private Token readReservedIdentifier(final String word) {
        switch (word) {
        case "type":
            return Token.TypeOperator;
        case "use":
            return Token.UsePragma;
        case "null":
            return Token.Null;
        default:
            return Token.Identifier;
        }
    }

    private Token readSingleLineComment() {
        assert source.match(-1, '/');
        //@formatter:off
        // SingleLineComment ::
        //      // SingleLineCommentChars opt
        //
        // SingleLineCommentChars ::
        //      SingleLineCommentChar SingleLineCommentChars opt
        //
        // SingleLineCommentChar ::
        //      SourceCharacter but not LineTerminator
        //@formatter:on
        while (true) {
            int c = source.next();
            if (c == Source.EOF) {
                break;
            }
            // *SourceCharacter but not* LineTerminator
            if (Source.isLineTerminator(c)) {
                if (c == 0x000D && source.matchWithAdvance(0x000A)) {
                    // chars count of <CR> <LF> to two
                    source.pushback(2);
                    break;
                }
                // chars count of other LineTerminators to one
                source.pushback(1);
                break;
            }
            buffer.write(c);
        }
        return Token.Comment;
    }

    /**
     * XXX rewrite rhino code
     * <p>
     * This Source Code Form is subject to the terms of the Mozilla Public
     * License, v. 2.0. If a copy of the MPL was not distributed with this file,
     * You can obtain one at http://mozilla.org/MPL/2.0/.
     * 
     * @return
     * @throws StaticSemanticsException 
     */
    private Token readStringLiteral() throws StaticSemanticsException {
        assert source.match(-1, '"');
        /*
         * Optimization: if the source contains no escaped characters, create
         * the string directly from the source text.
         */
        int stringStart = source.position();
        while (source.position() < source.length()) {
            int c = source.next();
            if (c <= '\u001F') {
                throw StaticSemanticsException.newSyntaxError("String contains control character",
                                                              createCurrentSourceInfo());
            } else if (c == '\\') {
                break;
            } else if (c == '"') {
                buffer.append(source.renge(stringStart, source.position() - 1));
                return Token.StringLiteral;
            }
        }

        /*
         * Slow case: string contains escaped characters. Copy a maximal
         * sequence of unescaped characters into a temporary buffer, then an
         * escaped character, and repeat until the entire string is consumed.
         */
        while (source.position() < source.length()) {
            assert source.match(-1, '\\');
            buffer.append(source.renge(stringStart, source.position() - 1));
            if (source.position() >= source.length()) {
                throw StaticSemanticsException.newSyntaxError("Unterminated string", createCurrentSourceInfo());
            }
            int c = source.next();
            switch (c) {
            case '"':
                buffer.write('"');
                break;
            case '\\':
                buffer.write('\\');
                break;
            case '/':
                buffer.write('/');
                break;
            case 'b':
                buffer.write('\b');
                break;
            case 'f':
                buffer.write('\f');
                break;
            case 'n':
                buffer.write('\n');
                break;
            case 'r':
                buffer.write('\r');
                break;
            case 't':
                buffer.write('\t');
                break;
            case 'u':
                char[] code = Character.toChars(readUnicodeEscape());
                buffer.write(code, 0, code.length);
                break;
            default:
                throw StaticSemanticsException.newSyntaxError("Unexpected character in string: '\\" + c + "'",
                                                              createCurrentSourceInfo());
            }
            stringStart = source.position();
            while (source.position() < source.length()) {
                c = source.next();
                if (c <= '\u001F') {
                    throw StaticSemanticsException.newSyntaxError("String contains control character", createCurrentSourceInfo());
                } else if (c == '\\') {
                    break;
                } else if (c == '"') {
                    buffer.append(source.renge(stringStart, source.position() - 1));
                    return Token.StringLiteral;
                }
            }
        }
        throw StaticSemanticsException.newSyntaxError("Unterminated string literal", createCurrentSourceInfo());
    }

    /**
     * 
     * @return
     * @throws StaticSemanticsException 
     */
    private int readUnicodeEscape() throws StaticSemanticsException {
        int c = source.next();
        if (c == '{') {
            int acc = 0;
            c = source.next();
            do {
                acc = (acc << 4) | numValue(c);
            } while (Character.isValidCodePoint(acc) && (c = source.next()) != '}');
            if (c == '}') {
                c = acc;
            } else {
                c = -1;
            }
        } else {
            // UnicodeEscapeSequence ::
            //      u Hex4Digits
            //      u{ HexDigits }
            // Hex4Digits ::
            //      HexDigit HexDigit HexDigit HexDigit
            c = (numValue(c) << 12) | (numValue(source.next()) << 8) | (numValue(source.next()) << 4) | numValue(source.next());
        }
        if (!Character.isValidCodePoint(c)) {
            throw StaticSemanticsException.newSyntaxError(createCurrentSourceInfo());
        }
        return c;
    }
}
