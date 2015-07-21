// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.parser.ParserException;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.SourceException;
import com.github.tasogare.json.ds.parser.Token;
import com.github.tasogare.json.ds.parser.TokenStream;

public class TokenStreamTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStringLiteral() {
        final TokenStream ts = new TokenStream(new Source("\"test\""));
        final Token stringLiteral = ts.scanToken();
        assertThat(stringLiteral, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("test"));
    }

    @Test
    public void testStringLiteralWithNonLATIN_1() {
        final TokenStream ts = new TokenStream(new Source("\"あ\", \"〠\", \"〒\", \"〄\""));
        final Token hiraganaA = ts.scanToken();
        assertThat(hiraganaA, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("あ"));
        ts.scanToken();

        final Token postalMarkFace = ts.scanToken();
        assertThat(postalMarkFace, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("〠"));
        ts.scanToken();

        final Token postalMark = ts.scanToken();
        assertThat(postalMark, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("〒"));
        ts.scanToken();

        final Token JISSymbol = ts.scanToken();
        assertThat(JISSymbol, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("〄"));

        final Token eof = ts.scanToken();
        assertThat(eof, is(Token.Eof));
    }

    @Test
    public void testStringLiteralWithSurrogatePear() {
        final TokenStream ts = new TokenStream(new Source("\"🂠\", \"🂡\", \"🂢\", \"🂣\", \"🂤\", \"🂥\", \"🂦\", \"🂧\", \"🂨\", \"🂩\", \"🂪\", \"🂫\", \"🂬\", \"🂭\", \"🂮\""));

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂠"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂡"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂢"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂣"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂤"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂥"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂦"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂧"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂨"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂩"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂪"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂫"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂬"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂭"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("🂮"));
        ts.scanToken();

        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testStringLiteralWithEscapedBMP() {
        final TokenStream ts = new TokenStream(new Source("\"\u0030\", \"\u3042\""));
        Token stringLiteral = ts.scanToken();
        assertThat(stringLiteral, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("0"));

        assertThat(ts.scanToken(), is(Token.Comma));

        stringLiteral = ts.scanToken();
        assertThat(stringLiteral, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("あ"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePearOnBrace() {
        final CharArrayWriter caw = new CharArrayWriter();
        try {
            // "你"
            final String str = "\"" + "\\" + "u{2F804}\"";
            System.out.println(str);
            caw.write(str);
            caw.write(", ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        final TokenStream ts = new TokenStream(new Source(caw.toString()));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("你"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePearOnBrace2() {
        final CharArrayWriter caw = new CharArrayWriter();
        try {
            // type
            final String str = "\"" + "\\u{0074}\\u{0079}\\u{0070}\\u{0065}" + "\"";
            System.out.println(str);
            caw.write(str);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        final TokenStream ts = new TokenStream(new Source(caw.toString()));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("type"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePear() {
        final CharArrayWriter caw = new CharArrayWriter();
        try {
            // "a"
            final String str = "\"" + "\\" + "u0061" + "\"";
            System.out.println(str);
            caw.write(str);
            caw.write(", ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        final TokenStream ts = new TokenStream(new Source(caw.toString()));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("a"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePear2() {
        final CharArrayWriter caw = new CharArrayWriter();
        try {
            // "type"
            final String str = "\"" + "\\u0074\\u0079\\u0070\\u0065" + "\"";
            System.out.println(str);
            caw.write(str);
            caw.write(", ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        final TokenStream ts = new TokenStream(new Source(caw.toString()));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("type"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePearChop() {
        final CharArrayWriter caw = new CharArrayWriter();
        try {
            // "你"
            final String str = "\"" + "\\" + "u2F804" + "\"";
            System.out.println(str);
            caw.write(str);
            caw.write(", ");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        final TokenStream ts = new TokenStream(new Source(caw.toString()));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("⾀4"));
    }

    @Test
    public void testEof() {
        final TokenStream ts = new TokenStream(new Source(""));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testWhitespace() {
        final TokenStream ts = new TokenStream(new Source("\u0009\u000B\u000C\u0020\u00A0\uFEFF\u0020\u00A0\u1680\u180E\u202F\u205F\u3000\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A"));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testLineterminator() {
        CharArrayWriter caw = new CharArrayWriter();
        // <LF>
        caw.write(0x0A);
        // <CR> [lookahead ≠ <LF> ]
        caw.write(0x0D);
        // <LS>
        caw.write(0x2028);
        // <PS>
        caw.write(0x2029);
        // <CR> <LF>
        caw.write(0x0D);
        caw.write(0x0A);

        // TokenStream lexer do not process the Automatic Semicolon Insertions
        final TokenStream ts = new TokenStream(new Source(caw.toString()));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testSingleLineCommentWithLFCR() {
        final TokenStream ts = new TokenStream(new Source("//aa\n\r//bb"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("aa"));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("bb"));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testSingleLineCommentWithCRLF() {
        final TokenStream ts = new TokenStream(new Source("//aa\r\n//bb"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("aa"));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("bb"));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testEmptySingleLineCommentWithLineTerminator() {
        final TokenStream ts = new TokenStream(new Source("//\r\n//\n\r"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo(""));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        // auto inserted virtual-semicolon *here*; but TokenStream lexer do not
        // automatic insertion.
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo(""));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testMultiLineComment() {
        final TokenStream ts = new TokenStream(new Source("/* * /\r\n */"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo(" * /\r\n "));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testMultiLineComment2() {
        final TokenStream ts = new TokenStream(new Source("/*   */type N = number;"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("   "));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("N"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test(expected = SourceException.class)
    public void testBadMultiLineCommentWithInsideEndComment() {
        final TokenStream ts = new TokenStream(new Source("/* */ */"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.scanToken(), is(Token.Any));
        ts.scanToken();
    }

    @Test(expected = SourceException.class)
    public void testBadMultiLineCommentWithFinished() {
        final TokenStream ts = new TokenStream(new Source("/* aaa"));
        assertThat(ts.scanToken(), is(Token.Comment));
    }

    @Test
    public void testBrace() {
        final TokenStream ts = new TokenStream(new Source("{}"));
        assertThat(ts.scanToken(), is(Token.LBrace));
        assertThat(ts.scanToken(), is(Token.RBrace));
        assertThat(ts.scanToken(), is(Token.Eof));

        final TokenStream ts2 = new TokenStream(new Source("{     }"));
        assertThat(ts2.scanToken(), is(Token.LBrace));
        assertThat(ts2.scanToken(), is(Token.RBrace));
        assertThat(ts2.scanToken(), is(Token.Eof));

        final TokenStream ts3 = new TokenStream(new Source("{\n\r\"p\"}"));
        assertThat(ts3.scanToken(), is(Token.LBrace));
        assertThat(ts3.scanToken(), is(Token.LineTerminator));
        assertThat(ts3.scanToken(), is(Token.LineTerminator));
        assertThat(ts3.scanToken(), is(Token.StringLiteral));
        assertThat(ts3.asStringLiteral(), equalTo("p"));
        assertThat(ts3.scanToken(), is(Token.RBrace));
        assertThat(ts3.scanToken(), is(Token.Eof));
    }

    @Test
    public void testIdentifier() {
        final TokenStream ts = new TokenStream(new Source("type typo = number;"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("typo"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testUsePragma() {
        final TokenStream ts = new TokenStream(new Source("use standard, aaa;"));
        assertThat(ts.scanToken(), is(Token.UsePragma));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("standard"));
        assertThat(ts.scanToken(), is(Token.Comma));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("aaa"));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testNullable() {
        final TokenStream ts = new TokenStream(new Source("type N = number?;"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("N"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.Nullable));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testNotNullable() {
        final TokenStream ts = new TokenStream(new Source("type N = number!;"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("N"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.NotNullable));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testAnyType() {
        final TokenStream ts = new TokenStream(new Source("type Any = *;"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("Any"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Any));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testRecordType() {
        final TokenStream ts = new TokenStream(new Source("type R = {\"a\": number, \"b\": string};"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("R"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LBrace));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("a"));
        assertThat(ts.scanToken(), is(Token.Colon));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.Comma));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("b"));
        assertThat(ts.scanToken(), is(Token.Colon));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("string"));
        assertThat(ts.scanToken(), is(Token.RBrace));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testEmptyArrayType() {
        final TokenStream ts = new TokenStream(new Source("type A = [];"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("A"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LBracket));
        assertThat(ts.scanToken(), is(Token.RBracket));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testArrayType() {
        final TokenStream ts = new TokenStream(new Source("type A = [number, string];"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("A"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LBracket));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.Comma));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("string"));
        assertThat(ts.scanToken(), is(Token.RBracket));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testVariableArrayType() {
        final TokenStream ts = new TokenStream(new Source("type A = [...number];"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("A"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LBracket));
        assertThat(ts.scanToken(), is(Token.TripleDot));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.RBracket));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testEscapedIdentifier() {
        final TokenStream ts = new TokenStream(new Source("type \\u0061 = number;"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
        assertThat(ts.asEscapedIdentifier(), equalTo("a"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testEmptyUnionType() {
        final TokenStream ts = new TokenStream(new Source("type E = ();"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("E"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LParen));
        assertThat(ts.scanToken(), is(Token.RParen));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testUnionType() {
        final TokenStream ts = new TokenStream(new Source("type E = (number);"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("E"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LParen));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.RParen));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testUnionTypeAsEnum() {
        final TokenStream ts = new TokenStream(new Source("type E = (number | string);"));
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("E"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.LParen));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("number"));
        assertThat(ts.scanToken(), is(Token.Or));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("string"));
        assertThat(ts.scanToken(), is(Token.RParen));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test(expected = SourceException.class)
    public void testBadAsComment() {
        final TokenStream ts = new TokenStream(new Source("\"aaa\""));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        ts.asComment();
    }

    public void testAsComment() {
        final TokenStream ts = new TokenStream(new Source("// aaaa"));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo(" aaaa"));
    }

    @Test(expected = SourceException.class)
    public void testBadAsStringLiteral() {
        final TokenStream ts = new TokenStream(new Source("// aaa"));
        assertThat(ts.scanToken(), is(Token.Comment));
        ts.asStringLiteral();
    }

    public void testAsStringLiteral() {
        final TokenStream ts = new TokenStream(new Source("\"bbb\""));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("bbb"));
    }

    @Test(expected = SourceException.class)
    public void testBadAsIdentifier() {
        final TokenStream ts = new TokenStream(new Source("\\u0041"));
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
        ts.asIdentifier();
    }

    public void testAsIdentifier() {
        final TokenStream ts = new TokenStream(new Source("C"));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("C"));
    }

    @Test(expected = SourceException.class)
    public void testBadAsEscapedIdentifier() {
        final TokenStream ts = new TokenStream(new Source("aaa"));
        assertThat(ts.scanToken(), is(Token.Identifier));
        ts.asEscapedIdentifier();
    }

    @Test(expected=SourceException.class)
    public void testBadAsHex4DigitsEscapedIdentifier() {
        final TokenStream ts = new TokenStream(new Source("\\u2f804"));
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
    }

    @Test
    public void testAsEscapedIdentifier() {
        final TokenStream ts = new TokenStream(new Source( "\\u" + "0061"));
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
        assertThat(ts.asEscapedIdentifier(), equalTo("a"));
    }

    @Test
    public void testAsEscapedReservedIdentifier() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/escapedReservedIdentifierInTS.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            assertThat(ts.scanToken(), is(Token.EscapedTypeOperator));
        } catch(ParserException e){
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAsEscapedReservedIdentifier2() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/escapedReservedIdentifierInTS2.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            assertThat(ts.scanToken(), is(Token.EscapedTypeOperator));
        } catch(ParserException e){
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
            e.printStackTrace();
            fail();
        }
    }
}
