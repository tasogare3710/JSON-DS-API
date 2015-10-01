// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static com.github.tasogare.json.ds.tests.AllTest.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.ContextuallyReservedIdentifierNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.PragmaNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.parser.Parser;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.TokenStream;

/**
 * @author tasogare
 *
 */
public class ParserTest {

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
    public void test() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/test.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testArrayType() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testArrayType.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testAsteriskAnyType() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testAsteriskAnyType.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    /**
     * @throws IOException
     */
    @Test(expected = StaticSemanticsException.class)
    public void testBadIdentifierByEscapedReservedIdentifier() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadIdentifierByEscapedReservedIdentifier.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadIncludePragma() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadIncludePragma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadIncludePragmaInMultiItem() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadIncludePragmaInMultiItem.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadRecordTypeByWithoutTypeAnnotation() throws IOException, StaticSemanticsException {
        // FieldType := FieldName の形式を正しく処理してなかった
        final TokenStream ts = new TokenStream(new Source("type T={\"name\"\n}"), "heep");
        final Parser parser = new Parser(ts);
        parser.parse();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadUsePragma() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadUsePragma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadUseStandard() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadUseStandard.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testBadUseStrict() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadUseStrict.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test
    public void testComments() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testComments.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testEmptyArray() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyArray.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test(expected = StaticSemanticsException.class)
    public void testEmptyArrayWithComma() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyArrayWithComma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testEmptyBadFixedArray() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyBadFixedArray.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test
    public void testEmptyPragma() throws IOException, StaticSemanticsException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyPragma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test(expected = StaticSemanticsException.class)
    public void testEmptyRecordWithComma() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyRecordWithComma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test(expected = StaticSemanticsException.class)
    public void testEmptyUnionTypeWithComma() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyUnionTypeWithComma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        }
        fail();
    }

    @Test
    public void testEscapedReservedIdentifier() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testEscapedReservedIdentifier.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testIncludePragma() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testIncludePragma.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        final PragmaNode includePragma = p.getPragmas().get(1);
        assertThat(includePragma.getName(), equalTo("include"));
        final StringLiteralNode strLite = includePragma. <StringLiteralNode> getPragmaItems().get(0);
        assertThat(strLite.getString(), equalTo("./testComments.jsds"));
    }

    @Test
    public void testInlineDef() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testInlineDef.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testRecordType() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testRecordType.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testSourceLocation() throws IOException, StaticSemanticsException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testSourceLocation.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            final SourceInfo info = e.getSourceInfo();
            assertThat(info.getRow(), is(4));
            assertThat(info.getColumn(), is(21));
            final String line = info.getSource().renge(info.getLineStart(), info.getPosition());
            assertThat(line, equalTo("/* aaa */type T =   ;"));
        }
    }

    /**
     * 末尾カンマとラインターミネータを正しく処理してなかった。
     * 
     * @throws IOException
     */
    @Test
    public void testTrailingCommaAndLineTerminator() throws IOException {
        ProgramNode<?> p = null;
        try {
            final TokenStream ts = new TokenStream(new Source("type T=[number,\r\n];"), "heep");
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testUnionType() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testUnionType.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testUsePragmaWithMultiItem() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testUsePragmaWithMultiItem.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        final List<IdentifierNode> pragmaItems = p.getPragmas().get(0).getPragmaItems();
        assertThat(pragmaItems.get(0).getString(), equalTo("aaa"));
        assertThat(pragmaItems.get(1).getString(), equalTo("bbb"));
        assertThat(pragmaItems.get(2).getString(), equalTo("ccc"));
    }

    @Test
    public void testUseStandard() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testUseStandard.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        final IdentifierNode in = p.getPragmas().get(0). <IdentifierNode> getPragmaItems().get(0);
        assertThat(in, instanceOf(ContextuallyReservedIdentifierNode.class));
        assertTrue(((ContextuallyReservedIdentifierNode) in).isStandard());
    }

    @Test
    public void testUseStrict() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testUseStrict.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        final IdentifierNode in = p.getPragmas().get(0). <IdentifierNode> getPragmaItems().get(0);
        assertThat(in, instanceOf(ContextuallyReservedIdentifierNode.class));
        assertTrue(((ContextuallyReservedIdentifierNode) in).isStrict());
    }

    @Test
    public void testVariableArrayType() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testVariableArrayType.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testWithComment() throws IOException {
        ProgramNode<?> p = null;
        final String name = "com/github/tasogare/json/ds/parser/resources/testWithComment.jsds";
        try (BufferedReader r = newReader(name, getClass())) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            p = parser.parse();
        } catch (StaticSemanticsException e) {
            reportError(e);
            fail();
        }
        System.out.println(p);
    }
}
