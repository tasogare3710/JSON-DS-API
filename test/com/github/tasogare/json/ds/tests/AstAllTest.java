// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.github.tasogare.json.ds.internal.ast.ArrayTypeNodeTest;
import com.github.tasogare.json.ds.internal.ast.AstNodeCloneableTest;
import com.github.tasogare.json.ds.internal.ast.NullLiteralNodeTest;
import com.github.tasogare.json.ds.internal.ast.PragmaNodeTest;
import com.github.tasogare.json.ds.internal.ast.ProgramNodeTest;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNodeTest;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNodeTest;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNodeTest;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNodeTest;
import com.github.tasogare.json.ds.internal.ast.UnionTypeNodeTest;

@RunWith(Suite.class)
@SuiteClasses({ArrayTypeNodeTest.class, AstNodeCloneableTest.class, NullLiteralNodeTest.class, PragmaNodeTest.class,
    ProgramNodeTest.class, RecordTypeNodeTest.class, StringLiteralNodeTest.class, TypeDefinitionNodeTest.class,
    TypeExpressionNodeTest.class, UnionTypeNodeTest.class })
public class AstAllTest {
}
