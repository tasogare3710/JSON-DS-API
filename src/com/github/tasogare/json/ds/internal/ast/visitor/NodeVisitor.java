// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast.visitor;

import com.github.tasogare.json.ds.internal.ast.AnyTypeNode;
import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.FieldTypeNode;
import com.github.tasogare.json.ds.internal.ast.IdentifierNode;
import com.github.tasogare.json.ds.internal.ast.NameExpressionNode;
import com.github.tasogare.json.ds.internal.ast.NullLiteralNode;
import com.github.tasogare.json.ds.internal.ast.PragmaNode;
import com.github.tasogare.json.ds.internal.ast.ProgramNode;
import com.github.tasogare.json.ds.internal.ast.RecordTypeNode;
import com.github.tasogare.json.ds.internal.ast.SemicolonNode;
import com.github.tasogare.json.ds.internal.ast.StringLiteralNode;
import com.github.tasogare.json.ds.internal.ast.TypeDefinitionNode;
import com.github.tasogare.json.ds.internal.ast.TypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.TypeNameNode;
import com.github.tasogare.json.ds.internal.ast.UnionTypeNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.BasicTypeExpressionNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.DirectiveNode;
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode;

/**
 * TODO Visitorの仕様が決まっていない
 * @author tasogare
 *
 */
public interface NodeVisitor<R>{

    R visit(AnyTypeNode node);

    <T extends AstNode & BasicTypeExpressionNode<T>> R visit(ArrayTypeNode<T> node);

    R visit(FieldNameNode.StringLiteral node);

    <T extends AstNode & BasicTypeExpressionNode<T>> R visit(FieldTypeNode<T> node);

    R visit(IdentifierNode node);

    R visit(NameExpressionNode node);

    R visit(NullLiteralNode node);

    R visit(PragmaNode node);

    <D extends AstNode & DirectiveNode<D>> R visit(ProgramNode<D> node);

    <T extends AstNode & BasicTypeExpressionNode<T>> R visit(RecordTypeNode<T> node);

    R visit(SemicolonNode node);

    R visit(StringLiteralNode node);

    <T extends AstNode & BasicTypeExpressionNode<T>> R visit(TypeDefinitionNode<T> node);

    <T extends AstNode & BasicTypeExpressionNode<T>> R visit(TypeExpressionNode<T> node);

    R visit(TypeNameNode node);

    <T extends AstNode & BasicTypeExpressionNode<T>> R visit(UnionTypeNode<T> node);
}
