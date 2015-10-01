// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static com.github.tasogare.json.ds.tests.AllTest.*;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.parser.Source;
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
    public void testAnyType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type Any = *;"), "heep");
        assertThat(ts.scanToken(), is(Token.TypeOperator));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("Any"));
        assertThat(ts.scanToken(), is(Token.Assign));
        assertThat(ts.scanToken(), is(Token.Any));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testArrayType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type A = [number, string];"), "heep");
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

    public void testAsComment() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("// aaaa"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo(" aaaa"));
    }

    @Test
    public void testAsEscapedIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\\u" + "0061"), "heep");
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
        assertThat(ts.asEscapedIdentifier(), equalTo("a"));
    }

    public void testAsIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("C"), "heep");
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("C"));
    }

    public void testAsStringLiteral() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\"bbb\""), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("bbb"));
    }

    @Test(expected = IllegalStateException.class)
    public void testBadAsComment() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\"aaa\""), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        ts.asComment();
    }

    @Test(expected = IllegalStateException.class)
    public void testBadAsEscapedIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("aaa"), "heep");
        assertThat(ts.scanToken(), is(Token.Identifier));
        ts.asEscapedIdentifier();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadAsHex4DigitsEscapedIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\\u2f804"), "heep");
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
    }

    @Test(expected = IllegalStateException.class)
    public void testBadAsIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\\u0041"), "heep");
        assertThat(ts.scanToken(), is(Token.EscapedIdentifier));
        ts.asIdentifier();
    }

    @Test(expected = IllegalStateException.class)
    public void testBadAsStringLiteral() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("// aaa"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
        ts.asStringLiteral();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadMultiLineCommentWithFinished() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("/* aaa"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadMultiLineCommentWithInsideEndComment() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("/* */ */"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.scanToken(), is(Token.Any));
        ts.scanToken();
    }

    @Test
    public void testBrace() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("{}"), "heep");
        assertThat(ts.scanToken(), is(Token.LBrace));
        assertThat(ts.scanToken(), is(Token.RBrace));
        assertThat(ts.scanToken(), is(Token.Eof));

        final TokenStream ts2 = new TokenStream(new Source("{     }"), "heep");
        assertThat(ts2.scanToken(), is(Token.LBrace));
        assertThat(ts2.scanToken(), is(Token.RBrace));
        assertThat(ts2.scanToken(), is(Token.Eof));

        final TokenStream ts3 = new TokenStream(new Source("{\n\r\"p\"}"), "heep");
        assertThat(ts3.scanToken(), is(Token.LBrace));
        assertThat(ts3.scanToken(), is(Token.LineTerminator));
        assertThat(ts3.scanToken(), is(Token.LineTerminator));
        assertThat(ts3.scanToken(), is(Token.StringLiteral));
        assertThat(ts3.asStringLiteral(), equalTo("p"));
        assertThat(ts3.scanToken(), is(Token.RBrace));
        assertThat(ts3.scanToken(), is(Token.Eof));
    }

    @Test
    public void testEmptyArrayType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type A = [];"), "heep");
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
    public void testEmptySingleLineCommentWithLineTerminator() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("//\r\n//\n\r"), "heep");
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
    public void testEmptyUnionType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type E = ();"), "heep");
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
    public void testEof() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source(""), "heep");
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testEscapedIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type \\u0061 = number;"), "heep");
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
    public void testEscapedReservedIdentifier() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/ts/testEscapedReservedIdentifier.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            assertThat(ts.scanToken(), is(Token.EscapedTypeOperator));
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
    }

    @Test
    public void testEscapedReservedIdentifier2() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/ts/testEscapedReservedIdentifier2.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            assertThat(ts.scanToken(), is(Token.EscapedTypeOperator));
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
    }

    @Test
    public void testIdentifier() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type typo = number;"), "heep");
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
    public void testIncludePragma() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("include \"./part2.jsds\";"), "heep");
        assertThat(ts.scanToken(), is(Token.IncludePragma));
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("./part2.jsds"));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testLineterminator() throws StaticSemanticsException {
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
        final TokenStream ts = new TokenStream(new Source(caw.toString()), "heep");
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testMultiLineComment() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("/* * /\r\n */"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo(" * /\r\n "));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testMultiLineComment2() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("/*   */type N = number;"), "heep");
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

    @Test
    public void testNotNullable() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type N = number!;"), "heep");
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
    public void testNullable() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type N = number?;"), "heep");
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
    public void testRecordType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type R = {\"a\": number, \"b\": string};"), "heep");
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
    public void testSingleLineCommentWithCRLF() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("//aa\r\n//bb"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("aa"));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("bb"));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testSingleLineCommentWithLFCR() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("//aa\n\r//bb"), "heep");
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("aa"));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.LineTerminator));
        assertThat(ts.scanToken(), is(Token.Comment));
        assertThat(ts.asComment(), equalTo("bb"));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testStringLiteral() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\"test\""), "heep");
        final Token stringLiteral = ts.scanToken();
        assertThat(stringLiteral, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("test"));
    }

    @Test
    public void testStringLiteralWithEscapedBMP() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\"\u0030\", \"\u3042\""), "heep");
        Token stringLiteral = ts.scanToken();
        assertThat(stringLiteral, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("0"));

        assertThat(ts.scanToken(), is(Token.Comma));

        stringLiteral = ts.scanToken();
        assertThat(stringLiteral, is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("あ"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePear() throws IOException, StaticSemanticsException {
        final CharArrayWriter caw = new CharArrayWriter();
        // "a"
        final String str = "\"" + "\\" + "u0061" + "\"";
        System.out.println(str);
        caw.write(str);
        caw.write(", ");

        final TokenStream ts = new TokenStream(new Source(caw.toString()), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("a"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePear2() throws IOException, StaticSemanticsException {
        final CharArrayWriter caw = new CharArrayWriter();
        // "type"
        final String str = "\"" + "\\u0074\\u0079\\u0070\\u0065" + "\"";
        System.out.println(str);
        caw.write(str);
        caw.write(", ");

        final TokenStream ts = new TokenStream(new Source(caw.toString()), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("type"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePearChop() throws IOException, StaticSemanticsException {
        final CharArrayWriter caw = new CharArrayWriter();
        // "你"
        final String str = "\"" + "\\" + "u2F804" + "\"";
        System.out.println(str);
        caw.write(str);
        caw.write(", ");

        final TokenStream ts = new TokenStream(new Source(caw.toString()), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("⾀4"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePearOnBrace() throws IOException, StaticSemanticsException {
        final CharArrayWriter caw = new CharArrayWriter();
        // "你"
        final String str = "\"" + "\\" + "u{2F804}\"";
        System.out.println(str);
        caw.write(str);
        caw.write(", ");

        final TokenStream ts = new TokenStream(new Source(caw.toString()), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        final String stringLiteral = ts.asStringLiteral();
        assertThat(stringLiteral, equalTo("你"));
    }

    @Test
    public void testStringLiteralWithEscapedSurrogatePearOnBrace2() throws IOException, StaticSemanticsException {
        final CharArrayWriter caw = new CharArrayWriter();
        // type
        final String str = "\"" + "\\u{0074}\\u{0079}\\u{0070}\\u{0065}" + "\"";
        System.out.println(str);
        caw.write(str);

        final TokenStream ts = new TokenStream(new Source(caw.toString()), "heep");
        assertThat(ts.scanToken(), is(Token.StringLiteral));
        assertThat(ts.asStringLiteral(), equalTo("type"));
    }

    @Test
    public void testStringLiteralWithNonLATIN_1() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("\"あ\", \"〠\", \"〒\", \"〄\""), "heep");
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
    public void testStringLiteralWithSurrogatePear() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(
            new Source(
                "\"🂠\", \"🂡\", \"🂢\", \"🂣\", \"🂤\", \"🂥\", \"🂦\", \"🂧\", \"🂨\", \"🂩\", \"🂪\", \"🂫\", \"🂬\", \"🂭\", \"🂮\""),
            "heep");

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
    public void testUnionType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type E = (number);"), "heep");
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
    public void testUnionTypeAsEnum() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type E = (number | string);"), "heep");
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

    @Test
    public void testUsePragma() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("use standard;"), "heep");
        assertThat(ts.scanToken(), is(Token.UsePragma));
        assertThat(ts.scanToken(), is(Token.Identifier));
        assertThat(ts.asIdentifier(), equalTo("standard"));
        assertThat(ts.scanToken(), is(Token.SemiColon));
        assertThat(ts.scanToken(), is(Token.Eof));
    }

    @Test
    public void testVariableArrayType() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(new Source("type A = [...number];"), "heep");
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
    public void testWhitespace() throws StaticSemanticsException {
        final TokenStream ts = new TokenStream(
            new Source(
                "\u0009\u000B\u000C\u0020\u00A0\uFEFF\u0020\u00A0\u1680\u180E\u202F\u205F\u3000\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200A"),
            "heep");
        assertThat(ts.scanToken(), is(Token.Eof));
    }
}
