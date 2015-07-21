// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import java.io.CharArrayWriter;
import java.util.Arrays;
import java.util.List;

/**
 * @author tasogare
 *
 */
public class TokenStream {

    private final Source source;
    private final CharArrayWriter buffer;
    private Token currentToken;
    private Token pendding;

    public TokenStream(final Source source) {
        this.source = source;
        this.buffer = new CharArrayWriter();
    }

    public String asComment() {
        return asContentOf(Token.Comment);
    }

    public String asEscapedIdentifier() {
        return asContentOf(Token.EscapedIdentifier);
    }

    public String asIdentifier() {
        return asContentOf(Token.Identifier);
    }

    public String asStringLiteral() {
        return asContentOf(Token.StringLiteral);
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public int length() {
        return source.length();
    }

    public int postion() {
        return source.postion();
    }

    public void pushPendding(Token t){
        if(pendding != null){
            throw new IllegalStateException();
        }
        pendding = t;
    }

    public Token scanToken(Token... ignore) {
        final List<Token> skips = Arrays.asList(ignore);
        final boolean commentSkip = skips.contains(Token.Comment);
        final boolean identifierSkip = skips.contains(Token.Identifier);
        final boolean escapedIdentifierSkip = skips.contains(Token.EscapedIdentifier);
        final boolean stringLiteralSkip = skips.contains(Token.StringLiteral);

        Token t = null;
        do {
            t = (pendding != null) ? popPendding() : scanToken();
            // スキップしたトークンが文字列contentを持つ場合TokenStreamのバッファをクリアしなければならない
            //@formatter:off
            if (commentSkip             && t == Token.Comment || 
                identifierSkip              && t == Token.Identifier ||
                escapedIdentifierSkip && t == Token.EscapedIdentifier ||
                stringLiteralSkip         && t == Token.StringLiteral)
            {
                buffer.reset();
            }
            //@formatter:on
        } while (skips.contains(t));
        return t;
    }

    public Source getSource() {
        return source;
    }

    private String asContentOf(Token t) {
        if (currentToken == t) {
            final String content = buffer.toString();
            buffer.reset();
            return content;
        }
        throw new SourceException();
    }

    private Token currentToken(Token t) {
        return currentToken = t;
    }

    private int numValue(int c) {
        return c >= '0' && c <= '9' ? c - '0' : c >= 'A' && c <= 'F' ? c - 'A' + 10 : c >= 'a' && c <= 'f' ? c - 'a' + 10 : -1;
    }

    private Token popPendding() {
        Token tmp = pendding;
        pendding = null;
        return tmp;
    }

    private Token readIdentifier(int c, boolean hasEscape) {
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
                    throw new SourceException();
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

    private Token readMultiLineComment() {
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
        int c;
        // /* MultiLineCommentChars opt */
        do {
            c = source.next();
            if (c == Source.EOF) {
                throw new SourceException("EOF");
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
            // SourceCharacter but not LineTerminator
            // ~~~~~~~~~~~~~~~~~~~~~~
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
     * @throws SourceException
     */
    private Token readStringLiteral() throws SourceException {
        assert source.match(-1, '"');
        /*
         * Optimization: if the source contains no escaped characters, create
         * the string directly from the source text.
         */
        int stringStart = source.postion();
        while (source.postion() < source.length()) {
            int c = source.next();
            if (c <= '\u001F') {
                throw new SourceException("String contains control character");
            } else if (c == '\\') {
                break;
            } else if (c == '"') {
                buffer.append(source.renge(stringStart, source.postion() - 1));
                return Token.StringLiteral;
            }
        }

        /*
         * Slow case: string contains escaped characters. Copy a maximal
         * sequence of unescaped characters into a temporary buffer, then an
         * escaped character, and repeat until the entire string is consumed.
         */
        while (source.postion() < source.length()) {
            assert source.match(-1, '\\');
            buffer.append(source.renge(stringStart, source.postion() - 1));
            if (source.postion() >= source.length()) {
                throw new SourceException("Unterminated string");
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
                throw new SourceException("Unexpected character in string: '\\" + c + "'");
            }
            stringStart = source.postion();
            while (source.postion() < source.length()) {
                c = source.next();
                if (c <= '\u001F') {
                    throw new SourceException("String contains control character");
                } else if (c == '\\') {
                    break;
                } else if (c == '"') {
                    buffer.append(source.renge(stringStart, source.postion() - 1));
                    return Token.StringLiteral;
                }
            }
        }
        throw new SourceException("Unterminated string literal");
    }

    /**
     * @return
     */
    private int readUnicodeEscape() {
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
//            UnicodeEscapeSequence :: 
//                u Hex4Digits  
//                u{ HexDigits } 
//            Hex4Digits :: 
//                HexDigit HexDigit HexDigit HexDigit
            c = (numValue(c) << 12) | (numValue(source.next()) << 8) | (numValue(source.next()) << 4) | numValue(source.next());
        }
        if (!Character.isValidCodePoint(c)) {
            throw new SourceException();
        }
        return c;
    }

    /**
     * この段階ではVirtual-Semicolonを挿入しない
     * 
     * @return
     */
    private Token scanToken() {
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
                if (c == 0x000D && source.matchWithAdvance(0x000A)) {
                    return currentToken(Token.LineTerminator);
                }
                return currentToken(Token.LineTerminator);
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
            throw new SourceException();
        case '/':
            // Comment ::
            // MultiLineComment
            // SingleLineComment
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
        throw new SourceException();
    }
}
