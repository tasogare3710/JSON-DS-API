// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.PragmaNode;

public class PragmaNodeTest {

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
    public void test() {
        //use strict;
        final ArrayList<IdentifierNode> pragmaItems = new ArrayList<>();
        pragmaItems.add(new IdentifierNode(4, 10, "strict"));
        final PragmaNode pragma = new PragmaNode(0, 10, pragmaItems);

        for(final IdentifierNode idn : pragma.getPragmaItems()){
            assertThat(idn.getString(), equalTo("strict"));
        }
    }

}
