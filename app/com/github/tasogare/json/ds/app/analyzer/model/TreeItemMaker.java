// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.app.analyzer.model;

import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.RuntimeSemanticsException.StandardErrors;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.internal.ast.AnyTypeNode;
import com.github.tasogare.json.ds.internal.ast.ArrayTypeNode;
import com.github.tasogare.json.ds.internal.ast.AstNode;
import com.github.tasogare.json.ds.internal.ast.ContextuallyReservedIdentifierNode;
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
import com.github.tasogare.json.ds.internal.ast.synthetic.FieldNameNode.StringLiteral;
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

/**
 * 
 * @author tasogare
 *
 */
public class TreeItemMaker implements NodeVisitor<TreeItem<String>> {

    private final String sourceName;
    private final String pragmaImage;
    private final String arrayImage;
    private final String recordImage;
    private final String unionImage;

    public TreeItemMaker(String sourceName) {
        this.sourceName = sourceName;
        pragmaImage = getClass().getResource("../resources/pragma.png").toString();
        arrayImage = getClass().getResource("../resources/array.png").toString();
        recordImage = getClass().getResource("../resources/record.png").toString();
        unionImage = getClass().getResource("../resources/union.png").toString();
    }

    @Override
    public TreeItem<String> visit(AnyTypeNode node) {
        return new TreeItem<>("*");
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> TreeItem<String> visit(ArrayTypeNode<T> node)
        throws StaticSemanticsException
    {
        final TreeItem<String> array = new TreeItem<>("", new ImageView(arrayImage));
        for (final TypeExpressionNode<T> e : node.getElementTypeList()) {
            array.getChildren().add(e.accept(this));
        }
        final TypeExpressionNode<T> vaTypes = node.getVariableArrayType();
        final TreeItem<String> variableLengthElements = vaTypes == null ? null : vaTypes.accept(this);
        variableLengthElements.setValue("..." + variableLengthElements.getValue());
        array.getChildren().add(variableLengthElements);
        return array;
    }

    @Override
    public TreeItem<String> visit(StringLiteral node) {
        return new TreeItem<>(node.getString());
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> TreeItem<String> visit(FieldTypeNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        final TreeItem<String> name = ((FieldNameNode.StringLiteral) node.getName()).accept(this);
        final TreeItem<String> type = node.getType().accept(this);
        final TreeItem<String> field = new TreeItem<>(name.getValue());
        field.getChildren().add(type);
        return field;
    }

    @Override
    public TreeItem<String> visit(IdentifierNode node) {
        return new TreeItem<>(node.getString());
    }

    @Override
    public TreeItem<String> visit(NameExpressionNode node) {
        return new TreeItem<>(node.getString());
    }

    @Override
    public TreeItem<String> visit(NullLiteralNode node) {
        return new TreeItem<>("Null");
    }

    @Override
    public TreeItem<String> visit(PragmaNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        switch (node.getName()) {
        case "use":
            for (final IdentifierNode iden : node. <IdentifierNode> getPragmaItems()) {
                if (iden instanceof ContextuallyReservedIdentifierNode) {
                    if (((ContextuallyReservedIdentifierNode) iden).isStandard()) {
                        TreeItem<String> use = new TreeItem<>("use", new ImageView(pragmaImage));
                        use.getChildren().add(iden.accept(this));
                        return use;
                    }
                }
            }
            throw new RuntimeSemanticsException("standard variantのみ利用できます", StandardErrors.InternalError);
        case "include":
            // 今のところincludeされるパスを表示するだけ
            TreeItem<String> include = new TreeItem<>("include", new ImageView(pragmaImage));
            final StringLiteralNode strLit = node. <StringLiteralNode> getPragmaItems().get(0);
            include.getChildren().add(strLit.accept(this));
            return include;
        default:
            throw new AssertionError();
        }
    }

    @Override
    public <D extends AstNode & DirectiveNode<D>> TreeItem<String> visit(ProgramNode<D> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        if (node.getPragmas().isEmpty()) {
            throw new RuntimeSemanticsException("Useプラグマがありません", StandardErrors.InternalError);
        }

        final TreeItem<String> schema = new TreeItem<>(sourceName);
        for (final PragmaNode p : node.getPragmas()) {
            schema.getChildren().add(p.accept(this));
        }
        for (final D d : node.getDirectives()) {
            schema.getChildren().add(d.accept(this));
        }
        return schema;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> TreeItem<String> visit(RecordTypeNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        final TreeItem<String> record = new TreeItem<>("", new ImageView(recordImage));
        for (final FieldTypeNode<T> f : node.getFieldTypeList()) {
            record.getChildren().add(f.accept(this));
        }
        return record;
    }

    @Override
    public TreeItem<String> visit(SemicolonNode node) {
        throw new AssertionError("unused");
    }

    @Override
    public TreeItem<String> visit(StringLiteralNode node) {
        return new TreeItem<>(node.getString());
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> TreeItem<String> visit(TypeDefinitionNode<T> node)
        throws StaticSemanticsException
    {
        final TreeItem<String> type = node.getIdentifier().accept(this);
        final TreeItem<String> init = node.getTypeInitialization().accept(this);
        type.getChildren().add(init);
        return type;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> TreeItem<String> visit(TypeExpressionNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        final T bte = node.getBasicTypeExpression();
        final TreeItem<String> b = bte.accept(this);

        switch (node.getNullability()) {
        case Nullable:
            b.setValue(b.getValue() + "?");
            return b;
        case NonNullable:
            b.setValue("*".equals(b.getValue()) ? "*!" : b.getValue());
            return b;
        case Absent:
            return b;
        default:
            throw new AssertionError();
        }
    }

    @Override
    public TreeItem<String> visit(TypeNameNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        final TreeItem<String> ne = node.getNameExpression().accept(this);
        return ne;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> TreeItem<String> visit(UnionTypeNode<T> node)
        throws StaticSemanticsException
    {
        final TreeItem<String> union = new TreeItem<>("", new ImageView(unionImage));
        for (final TypeExpressionNode<T> te : node.getTypeUnionList()) {
            union.getChildren().add(te.accept(this));
        }
        return union;
    }

}
