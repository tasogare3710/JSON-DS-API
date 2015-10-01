// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.github.tasogare.json.ds.datatype.driver;

import static com.github.tasogare.json.ds.datatype.Intrinsics.anyType;
import static com.github.tasogare.json.ds.datatype.Intrinsics.newNullable;
import static com.github.tasogare.json.ds.datatype.Intrinsics.nonNullableAnyType;
import static com.github.tasogare.json.ds.datatype.Intrinsics.nullType;
import static com.github.tasogare.json.ds.datatype.Intrinsics.undefindType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import javax.json.JsonValue;

import com.github.tasogare.json.ds.MetaObject;
import com.github.tasogare.json.ds.RuntimeSemanticsException;
import com.github.tasogare.json.ds.RuntimeSemanticsException.StandardErrors;
import com.github.tasogare.json.ds.StaticSemanticsException;
import com.github.tasogare.json.ds.datatype.AnyType;
import com.github.tasogare.json.ds.datatype.ArrayType;
import com.github.tasogare.json.ds.datatype.RecordType;
import com.github.tasogare.json.ds.datatype.Type;
import com.github.tasogare.json.ds.datatype.UnionType;
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
import com.github.tasogare.json.ds.internal.ast.visitor.NodeVisitor;
import com.github.tasogare.json.ds.parser.Parser;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.TokenStream;

/**
 * non-hositable版のプロセッサ テスト・ドライバの実装
 * 
 * とくに説明がない限りこのクラスのメソッドは戻り値が必要ないメソッドは{@code null}を返します。
 * 
 * @author tasogare
 *
 */
public class JsonDsProcessorTestDriver implements NodeVisitor<Type>, DatatypeSchemaProcessorTestDriver {

    protected Mode complianceMode;
    protected URL sourceName;
    private final MetaObject<JsonValue> metaObjects;

    public JsonDsProcessorTestDriver(MetaObject<JsonValue> metaObjects) {
        this.metaObjects = metaObjects;
    }

    public JsonDsProcessorTestDriver() {
        this(new JsonMetaObjectTestDriver());
    }

    @Override
    public Type visit(AnyTypeNode node) {
        return anyType;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(ArrayTypeNode<T> node)
        throws StaticSemanticsException
    {
        final ArrayList<Type> fixedLengthElements = new ArrayList<>();
        for (final TypeExpressionNode<T> e : node.getElementTypeList()) {
            fixedLengthElements.add(e.accept(this));
        }
        final TypeExpressionNode<T> vaTypes = node.getVariableArrayType();
        final Type variableLengthElements = vaTypes == null ? null : vaTypes.accept(this);
        ArrayType array = new ArrayType(fixedLengthElements, variableLengthElements);
        return array;
    }

    /**
     * 使ってない
     */
    @Override
    public Type visit(FieldNameNode.StringLiteral node) {
        throw new AssertionError("unused");
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(FieldTypeNode<T> node)
        throws StaticSemanticsException
    {
        final String name = ((FieldNameNode.StringLiteral) node.getName()).getString();
        final Type type = node.getType().accept(this);
        final RecordType.Field field = new RecordType.Field(name, type);
        return field;
    }

    @Override
    public Type visit(IdentifierNode node) {
        metaObjects.registerMetaObject(node.getString(), undefindType);
        return null;
    }

    @Override
    public Type visit(NameExpressionNode node) {
        final String name = node.getString();
        return metaObjects.getMetaObject(name);
    }

    @Override
    public Type visit(NullLiteralNode node) {
        return nullType;
    }

    @Override
    public Type visit(PragmaNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        switch (node.getName()) {
        case "use":
            for (final IdentifierNode iden : node. <IdentifierNode> getPragmaItems()) {
                if (iden instanceof ContextuallyReservedIdentifierNode) {
                    if (((ContextuallyReservedIdentifierNode) iden).isStandard()) {
                        complianceMode = Mode.Standard;
                        return null;
                    }
                }
            }
            // XXX: モードの指定がないとエラーになる挙動は今はまだ仕様ではない
            throw new RuntimeSemanticsException("standard variantのみ利用できます", StandardErrors.InternalError);
        case "include":
            if (sourceName != null) {
                final URI baseUri;
                try {
                    baseUri = new URI(sourceName.toString());
                } catch (URISyntaxException e) {
                    throw new RuntimeSemanticsException(sourceName.toString(), e, StandardErrors.URIError);
                }
                // include pragmaのitemは一つのみ許されるのでPragmaNode#getPragmaItems()は一つの要素を持ったListになる
                final URI includeUri = baseUri.resolve(node. <StringLiteralNode> getPragmaItems().get(0).getString());
                final JsonDsProcessorTestDriver nested = new JsonDsProcessorTestDriver(getMetaObjects());
                try (final InputStreamReader r = new InputStreamReader(includeUri.toURL().openStream())) {
                    nested.process(r, includeUri.toURL());
                } catch (MalformedURLException e) {
                    throw new RuntimeSemanticsException(includeUri.toString(), e, StandardErrors.URIError);
                } catch (IOException | IllegalArgumentException e) {
                    throw new RuntimeSemanticsException(includeUri.toString(), e, StandardErrors.InternalError);
                }
                return null;
            } else {
                throw new RuntimeSemanticsException("base URI is null", StandardErrors.URIError);
            }
        default:
            throw new AssertionError();
        }
    }

    @Override
    public <D extends AstNode & DirectiveNode<D>> Type visit(ProgramNode<D> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        if (node.getPragmas().isEmpty()) {
            // XXX: モードの指定がないとエラーになる挙動は今はまだ仕様ではない
            throw new RuntimeSemanticsException("Useプラグマがありません", StandardErrors.InternalError);
        }
        for (final PragmaNode p : node.getPragmas()) {
            p.accept(this);
        }
        for (final D d : node.getDirectives()) {
            d.accept(this);
        }
        return null;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(RecordTypeNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        HashSet<RecordType.Field> fields = new HashSet<>();
        for (final FieldTypeNode<T> f : node.getFieldTypeList()) {
            fields.add((RecordType.Field) f.accept(this));
        }
        return new RecordType(fields);
    }

    /**
     * 使ってない
     */
    @Override
    public Type visit(SemicolonNode node) {
        throw new AssertionError("unused");
    }

    /**
     * 使ってない
     */
    @Override
    public Type visit(StringLiteralNode node) {
        throw new AssertionError("unused");
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeDefinitionNode<T> node)
        throws StaticSemanticsException
    {
        node.getIdentifier().accept(this);
        Type init = node.getTypeInitialization().accept(this);
        metaObjects.registerMetaObject(node.getIdentifier().getString(), init);
        return null;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeExpressionNode<T> node)
        throws RuntimeSemanticsException, StaticSemanticsException
    {
        final T bte = node.getBasicTypeExpression();
        final Type b = bte.accept(this);

        switch (node.getNullability()) {
        case Nullable:
            return newNullable(b);
        case NonNullable:
            return b instanceof AnyType ? nonNullableAnyType : b;
        case Absent:
            return b;
        default:
            throw new AssertionError();
        }
    }

    @Override
    public Type visit(TypeNameNode node) throws RuntimeSemanticsException, StaticSemanticsException {
        final Type metaObject = node.getNameExpression().accept(this);
        if (metaObject == null) {
            throw new RuntimeSemanticsException(node.getString() + "が未定義です", StandardErrors.ReferenceError);
        }
        return metaObject;
    }

    @Override
    public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(UnionTypeNode<T> node)
        throws StaticSemanticsException
    {
        HashSet<Type> members = new HashSet<>();
        for (final TypeExpressionNode<T> te : node.getTypeUnionList()) {
            members.add(te.accept(this));
        }
        return new UnionType(members);
    }

    @Override
    public void process(String jsds, URL sourceName) throws RuntimeSemanticsException, StaticSemanticsException {
        final Source source = new Source(jsds);
        processImpl(source, sourceName);
    }

    @Override
    public void process(Reader jsds, URL sourceName) throws RuntimeSemanticsException, StaticSemanticsException {
        final Source source = new Source(jsds);
        processImpl(source, sourceName);
    }

    protected void processImpl(final Source source, URL sourceName) throws RuntimeSemanticsException, StaticSemanticsException {
        this.sourceName = sourceName;
        final TokenStream ts = new TokenStream(source, sourceName != null ? sourceName.toString() : "");
        final Parser parser = new Parser(ts);
        final ProgramNode<?> p = parser.parse();
        visit(p);
    }

    /**
     * 
     * @return JSON-DSファイルが準拠しているJSON-DS Variantを返す
     */
    public final Mode getComplianceMode() {
        return complianceMode;
    }

    @Override
    public final MetaObject<JsonValue> getMetaObjects() {
        return metaObjects;
    }

}
