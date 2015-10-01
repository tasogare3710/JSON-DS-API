// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.internal.ast.visitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.StaticSemanticsException;
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
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;
import com.github.tasogare.json.ds.parser.Parser;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.SourceInfo;
import com.github.tasogare.json.ds.parser.TokenStream;

public final class PrintNodeVisitor implements NodeVisitor<String> {

    public static void main(String... args) {
        final String name = "com/github/tasogare/json/ds/datatype/resources/layer/include/layerSplit.jsds";
        final InputStream is = PrintNodeVisitor.class.getClassLoader().getResourceAsStream(name);
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            final TokenStream ts = new TokenStream(new Source(r), name);
            final Parser parser = new Parser(ts);
            final ProgramNode<?> p = parser.parse();
            PrintNodeVisitor.print(p);
            System.exit(0);
        } catch (StaticSemanticsException e) {
            final SourceInfo info = e.getSourceInfo();
            System.err.println(info.getSourceName());
            System.err.println(info.getRow());
            System.err.println(info.getColumn());
            e.printStackTrace();
            System.exit(1);
        } catch (RuntimeSemanticsException e) {
            System.err.println(name);
            System.err.println(e.getErrorType());
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e1) {
            e1.printStackTrace();
            System.exit(1);
        }
    }

    public static <D extends AstNode & DirectiveNode<D>> void print(ProgramNode<D> program)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        final PrintNodeVisitor printer = new PrintNodeVisitor();
        System.out.println(printer.visit(program));
    }

    public PrintNodeVisitor() {
    }

    @Override
    public String visit(AnyTypeNode node) {
        return node.getString();
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> String visit(ArrayTypeNode<T> node)
        throws StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder("ARRAY OF [");
        if (node.isVariable()) {
            sb.append("...").append(node.getVariableArrayType().accept(this));
        } else {
            for (TypeExpressionNode<T> element : node.getElementTypeList()) {
                sb.append(element.accept(this));
            }
        }
        sb.append("]\n");
        return sb.toString();
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> String visit(TypeExpressionNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(node.getBasicTypeExpression().accept(this)).append("NULLABILITY is ")
            .append(node.getNullability().name());
        return sb.toString();
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> String visit(FieldTypeNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder();
        sb.append(((FieldNameNode.StringLiteral) node.getName()).accept(this)).append(" : ");
        sb.append(node.getType().accept(this));
        return sb.toString();
    }

    @Override
    public String visit(IdentifierNode node) {
        StringBuilder sb = new StringBuilder(node.getString());
        return sb.toString();
    }

    @Override
    public String visit(NameExpressionNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        StringBuilder sb = new StringBuilder(node.getIdentifier().accept(this));
        return sb.toString();
    }

    @Override
    public String visit(NullLiteralNode node) {
        StringBuilder sb = new StringBuilder(node.getString());
        return sb.toString();
    }

    @Override
    public <D extends AstNode & DirectiveNode<D>> String visit(ProgramNode<D> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder();
        for (final PragmaNode pragma : node.getPragmas()) {
            sb.append(pragma.accept(this)).append("\n");
        }
        for (final D directive : node.getDirectives()) {
            sb.append(directive.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> String visit(RecordTypeNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder();
        for (FieldTypeNode<T> f : node.getFieldTypeList()) {
            sb.append(f.accept(this)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visit(SemicolonNode node) {
        StringBuilder sb = new StringBuilder("[semicolon is ").append(node.isVirtual()).append("]");
        return sb.toString();
    }

    @Override
    public String visit(StringLiteralNode node) {
        StringBuilder sb = new StringBuilder(node.getString());
        return sb.toString();
    }

    @Override
    public String visit(FieldNameNode.StringLiteral node) throws RuntimeSemanticsException, StaticSemanticsException {
        StringBuilder sb = new StringBuilder("FIELD is ").append(node.getStringLiteral().accept(this));
        return sb.toString();
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> String visit(TypeDefinitionNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder("TYPE of ").append(node.getIdentifier().accept(this)).append("\n")
            .append(node.getTypeInitialization().accept(this));
        return sb.append("\n").toString();
    }

    @Override
    public String visit(TypeNameNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        StringBuilder sb = new StringBuilder(node.getNameExpression().accept(this)).append(" ");
        return sb.toString();
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> String visit(UnionTypeNode<T> node)
        throws StaticSemanticsException
    {
        StringBuilder sb = new StringBuilder("UNION OF (");
        for (TypeExpressionNode<T> u : node.getTypeUnionList()) {
            sb.append(u.accept(this)).append(" | ");
        }
        sb.delete(sb.length() - 3, sb.length());
        sb.append(")\n");
        return sb.toString();
    }

    @Override
    public String visit(PragmaNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        assert"use".equals(node.getName()) || "include".equals(node.getName());

        StringBuilder sb = new StringBuilder();
        final String name = node.getName();
        if ("use".equals(name)) {
            sb.append("USE(");
            for (IdentifierNode item : node. <IdentifierNode> getPragmaItems()) {
                sb.append(item.accept(this)).append(", ");
            }
        } else if ("include".equals(name)) {
            sb.append("INCLUDE(");
            for (StringLiteralNode item : node. <StringLiteralNode> getPragmaItems()) {
                sb.append(item.accept(this)).append(", ");
            }
        }
        sb.delete(sb.length() - ", ".length(), sb.length());
        sb.append(")");
        return sb.toString();
    }
}
