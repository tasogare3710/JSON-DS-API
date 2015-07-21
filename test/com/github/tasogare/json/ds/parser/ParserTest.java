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
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
            e.printStackTrace();
            fail();
        }
        final IdentifierNode in = p.getPragmas().get(0).getPragmaItems().get(0);
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
            e.printStackTrace();
            fail();
        }
        final IdentifierNode in = p.getPragmas().get(0).getPragmaItems().get(0);
        assertThat(in, instanceOf(ContextuallyReservedIdentifierNode.class));
        assertTrue(((ContextuallyReservedIdentifierNode)in).isStandard());
    }

    @Test(expected=ParserException.class)
    public void testBadPragma() throws IOException {
        final String name = "com/github/tasogare/json/ds/parser/resources/testBadPragma.js";
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
            e.printStackTrace();
            fail();
        }
        System.out.println(p);
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
        Source input = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            input = new Source(r);
            final TokenStream ts = new TokenStream(input);
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            System.err.println(e.getSourceName());
            System.err.println(input.renge(0, e.getEndPosition()));
            System.err.println(e.getEndPosition());
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
            Source input = new Source(r);
            final TokenStream ts = new TokenStream(input);
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            System.err.println(e.getSourceName());
            System.err.println(e.getSource().renge(0, e.getEndPosition()));
            System.err.println(e.getStartPosition() + ", " +e.getEndPosition());
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
        Source input = null;
        try(BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            input = new Source(r);
            final TokenStream ts = new TokenStream(input);
            final Parser parser = new Parser(ts, name);
            p = parser.parse();
        } catch(ParserException e){
            System.err.println(e.getSourceName());
            System.err.println(input.renge(0, e.getEndPosition()));
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
            System.err.println(e.getSourceName());
            System.err.println(e.getStartPosition());
            System.err.println(e.getEndPosition());
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
