// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.internal.ast.ContextuallyReservedIdentifierNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.PragmaNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.parser.Parser;
import com.github.tasogare.json.ds.parser.ParserException;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.TokenStream;

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
    public void testPragma1() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testPragma1.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testPragma2() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testPragma2.jsds";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testPragmaIsUseOfStrict() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testPragmaIsUseOfStrict.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        final IdentifierNode in = p.getPragmas().get(0).<IdentifierNode>getPragmaItems().get(0);
        assertThat(in, instanceOf(ContextuallyReservedIdentifierNode.class));
        assertTrue(((ContextuallyReservedIdentifierNode)in).isStrict());
    }

    @Test
    public void testPragmaIsUseOfStandard() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testPragmaIsUseOfStandard.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        final IdentifierNode in = p.getPragmas().get(0).<IdentifierNode>getPragmaItems().get(0);
        assertThat(in, instanceOf(ContextuallyReservedIdentifierNode.class));
        assertTrue(((ContextuallyReservedIdentifierNode)in).isStandard());
    }

    @Test(expected=ParserException.class)
    public void testBadUsePragma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadUsePragma.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test
    public void testPragmaIsInclude() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testPragmaIsInclude.jsds";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        final PragmaNode includePragma = p.getPragmas().get(1);
        assertThat(includePragma.getName(), equalTo("include"));
        final StringLiteralNode strLite = includePragma.<StringLiteralNode>getPragmaItems().get(0);
        assertThat(strLite.getString(), equalTo("./Comments.js"));
    }

    @Test(expected=ParserException.class)
    public void testBadIncludePragma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadIncludePragma.jsds";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test(expected = ParserException.class)
    public void testBadIncludePragmaInMultiItem() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadIncludePragmaInMultiItem.jsds";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test
    public void testEmptyPragma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyPragma.jsds";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test
    public void test() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/test.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void tes2() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/inlineDef.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testSourceLocation() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testSourceLocation.jsds";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println("row: " + info.getRow() + " col: " + info.getColumn());
            System.err.println(info.getSource().renge(info.getLineStart(), info.getPosition()));
            final StringBuilder sb = new StringBuilder();
            for(int i=0; i< info.getColumn() - 1; i++){
                sb.append(" ");
            }
            System.err.println(sb.append("^").toString());
            e.printStackTrace();
        }
    }

    @Test
    public void testAsteriskAnyType() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/asteriskAnyType.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test(expected=ParserException.class)
    public void testRecordTypeWithoutTypeAnnotation() throws IOException {
        ProgramNode<?> p = null;
        // FieldType :=   FieldName    の形式を正しく処理してなかった
        final TokenStream ts = new TokenStream(new Source("type T={\"name\"\n}"));
        final Parser parser = new Parser(ts, "raw string");
        p = parser.parse();
        System.out.println(p);
    }

    @Test
    public void test3() throws IOException {
        ProgramNode<?> p = null;
        // 末尾カンマとラインターミネータを正しく処理してなかった。
        try {
            final TokenStream ts = new TokenStream(new Source("type T=[number,\r\n];"));
            final Parser parser = new Parser(ts, "raw string");
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testVariableArrayType() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/variableArrayType.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testArrayType() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/ArrayType.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testUnionType() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/UnionType.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final Source input = new Source(r);
            final TokenStream ts = new TokenStream(input);
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println("row: " + info.getRow() + " col: " + info.getColumn());
            System.err.println(info.getSource().renge(info.getLineStart(), info.getPosition()));
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testComments() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/Comments.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final Source input = new Source(r);
            final TokenStream ts = new TokenStream(input);
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow() + ", " +info.getColumn());
            System.err.println(info.getSource().renge(info.getLineStart(), info.getPosition()));
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testRecordType() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/RecordType.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final Source input = new Source(r);
            final TokenStream ts = new TokenStream(input);
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println("row: " + info.getRow() + " col: " + info.getColumn());
            System.err.println(info.getSource().renge(info.getLineStart(), info.getPosition()));
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testWithComment() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testWithComment.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test(expected=ParserException.class)
    public void testEmptyUnionTypeWithComma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/emptyUnionTypeWithComma.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test(expected=ParserException.class)
    public void testEmptyRecordWithComma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/emptyRecordWithComma.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test
    public void testEmptyArray() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/emptyArray.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    @Test
    public void testEscapedReservedIdentifier() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEscapedReservedIdentifier.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
    }

    /**
     * @throws IOException
     */
    @Test(expected=ParserException.class)
    public void testBadIdentifierByEscapedReservedIdentifier() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadIdentifierByEscapedReservedIdentifier.js";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test(expected=ParserException.class)
    public void testEmptyArrayWithComma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/emptyArrayWithComma.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }

    @Test(expected=ParserException.class)
    public void testEmptyBadFixedArray() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testEmptyBadFixedArray.js";
        InputStream is = getClass().getClassLoader().getResourceAsStream(name);
        ProgramNode<?> p = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            final TokenStream ts = new TokenStream(new Source(r));
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        }
        System.out.println(p);
    }
}
