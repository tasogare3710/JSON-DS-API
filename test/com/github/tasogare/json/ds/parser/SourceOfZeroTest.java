// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.parser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.SourceException;

public class SourceOfZeroTest {

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
    public void testZeroLength() {
        final Source source = new Source("");
        assertThat(source.length(), is(0));
    }

    @Test
    public void testZeroPosition() {
        final Source source = new Source("");
        assertThat(source.position(), is(0));
    }

    @Test
    public void testZeroPeek_Int_Inside() {
        final Source source = new Source("");
        assertThat(source.peek(0), is(Source.EOF));
    }

    /**
     * EOFより後ろに超えてもEOFを返す。
     */
    @Test
    public void testZeroPeek_Int_Overrun() {
        final Source source = new Source("");
        assertThat(source.peek(1), is(Source.EOF));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testZeroPeek_Int_Underrun() {
        final Source source = new Source("");
        assertThat(source.peek(-1), is(Source.EOF));
    }

    @Test
    public void testZeroPeek_Void() {
        final Source source = new Source("");
        assertThat(source.peek(), is(Source.EOF));
    }

    @Test
    public void testZeroNext() {
        final Source source = new Source("");
        assertThat(source.next(), is(Source.EOF));
    }

    /**
     * EOFが返された以降は何度でもEOFを返す。
     */
    @Test
    public void testZeroNext_Overrun() {
        final Source source = new Source("");
        assertThat(source.next(), is(Source.EOF));
        assertThat(source.next(), is(Source.EOF));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testZeroPushback() {
        final Source source = new Source("");
        source.pushback(1);
    }

    /**
     * sourceが空のとき
     */
    @Test
    public void testZeroMatch_Int_Int() {
        final Source source = new Source("");
        assertThat(source.match(0, Source.EOF), is(true));
        assertThat(source.match(0, '\u3042'), is(false));
    }

    /**
     * sourceのpositionがマイナス方向にOutOfIndexのときIndexOutOfBoundsException
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void testZeroMatch_Int_Int2() {
        final Source source = new Source("");
        assertThat(source.match(-1, Source.EOF), is(true));
        assertThat(source.match(-1, '\u3042'), is(false));
    }

    @Test
    public void testZeroMatch_Void() {
        final Source source = new Source("");
        assertThat(source.match(Source.EOF), is(true));
    }

    /**
     * overrunしても{@link Source#length()}を超えていれば常に{@link Source#EOF}を返す。
     * @see Source#peek(int)
     */
    @Test
    public void testZeroMatchWithAdvance_Int_IntOverrun(){
        final Source source = new Source("");
        final int pos = source.position();
        assertThat(source.matchWithAdvance(0, Source.EOF), is(true));
        assertThat(pos + 1, is(source.position()));
    }

    /**
     * overrunしても{@link Source#length()}を超えていれば常に{@link Source#EOF}を返す。
     * @see Source#peek(int)
     */
    @Test
    public void testZeroMatchWithAdvance_IntOverrun(){
        final Source source = new Source("");
        final int pos = source.position();
        assertThat(source.matchWithAdvance(Source.EOF), is(true));
        assertThat(pos + 1, is(source.position()));
    }

    @Test
    public void testZeroMustMatch_Int(){
        final Source source = new Source("");
        final int pos = source.position();
        source.mustMatch(Source.EOF);
        assertThat(pos, is(source.position()));
    }

    @Test(expected=SourceException.class)
    public void testZeroMustMatch_Int_WithException(){
        final Source source = new Source("");
        source.mustMatch('\u3042');
    }

    @Test
    public void testZeroMustMatchWithAdvance_Int(){
        final Source source = new Source("");
        final int pos = source.position();
        source.mustMatchWithAdvance(Source.EOF);
        assertThat(pos + 1, is(source.position()));
    }

    @Test(expected=SourceException.class)
    public void testZeroMustMatchWithAdvance_Int_WithException(){
        final Source source = new Source("");
        source.mustMatchWithAdvance('\u3042');
    }

    @Test
    public void testZeroRenge() {
        final Source source = new Source("");
        final String substr = source.renge(0, 0);

        final String Empty = "";
        assertThat(substr, equalTo(Empty));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testZeroRengeWithOutOfRenge() {
        final Source source = new Source("");
        source.renge(0, 1);
    }
}
