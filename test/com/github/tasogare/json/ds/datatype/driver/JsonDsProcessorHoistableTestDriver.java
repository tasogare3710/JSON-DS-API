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
import java.util.Map;

import com.github.tasogare.json.ds.JsonDsException;
import com.github.tasogare.json.ds.JsonDsException.StandardErrors;
import com.github.tasogare.json.ds.datatype.AnyType;
import com.github.tasogare.json.ds.datatype.ArrayType;
import com.github.tasogare.json.ds.datatype.RecordType;
import com.github.tasogare.json.ds.datatype.ReferenceType;
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
import com.github.tasogare.json.ds.parser.ParserException;
import com.github.tasogare.json.ds.parser.Source;
import com.github.tasogare.json.ds.parser.TokenStream;

/**
 * hositable版のプロセッサ　テスト・ドライバの実装
 * 
 * とくに説明がない限り{@link PassOne}と{@link PassTwo}のメソッドは戻り値が必要ないメソッドは{@code null}
 * を返します。
 * 
 * @author tasogare
 *
 */
public class JsonDsProcessorHoistableTestDriver implements DatatypeSchemaProcessorTestDriver {

    public class PassOne implements NodeVisitor<Type> {
        @Override
        public Type visit(AnyTypeNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(ArrayTypeNode<T> node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(FieldNameNode.StringLiteral node) {
            throw new AssertionError("unused");
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(FieldTypeNode<T> node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(IdentifierNode node) {
            getMetaObjects().registerMetaObject(node.getString(), undefindType);
            return null;
        }

        @Override
        public Type visit(NameExpressionNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(NullLiteralNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(PragmaNode node) {
            switch (node.getName()) {
            case "use":
                for (final IdentifierNode iden : node.<IdentifierNode> getPragmaItems()) {
                    if (iden instanceof ContextuallyReservedIdentifierNode) {
                        if (((ContextuallyReservedIdentifierNode) iden).isStandard()) {
                            complianceMode = Mode.Standard;
                            return null;
                        }
                    }
                }
                throw new JsonDsException("standard variantのみ利用できます", StandardErrors.SyntaxError);
            case "include":
                System.err.println("warning: include pragma TBD");
                if (JsonDsProcessorHoistableTestDriver.this.sourceName != null) {
                    final URI baseUri;
                    try {
                        baseUri = new URI(JsonDsProcessorHoistableTestDriver.this.sourceName.toString());
                    } catch (URISyntaxException e) {
                        throw new JsonDsException(JsonDsProcessorHoistableTestDriver.this.sourceName.toString(), e, StandardErrors.URIError);
                    }
                    // XXX: とりあえず一つのインクルード・パスを受け取る
                    final URI includeUri = baseUri.resolve(node.<StringLiteralNode> getPragmaItems().get(0).getString());
                    final JsonDsProcessorHoistableTestDriver nested = new JsonDsProcessorHoistableTestDriver(JsonDsProcessorHoistableTestDriver.this.getMetaObjects());
                    try (final InputStreamReader r = new InputStreamReader(includeUri.toURL().openStream())) {
                        nested.process(r, includeUri.toURL());
                    } catch (MalformedURLException e) {
                        throw new JsonDsException(includeUri.toString(), e, StandardErrors.URIError);
                    } catch (IOException | IllegalArgumentException e) {
                        throw new JsonDsException(includeUri.toString(), e, StandardErrors.InternalError);
                    }
                    return null;
                } else {
                    throw new JsonDsException("base URI is null", StandardErrors.URIError);
                }
            default:
                throw new AssertionError();
            }
        }

        @Override
        public <D extends AstNode & DirectiveNode<D>> Type visit(ProgramNode<D> node) {
            if(node.getPragmas().isEmpty()){
                throw new JsonDsException("Useプラグマがありません", StandardErrors.SyntaxError);
            }
            for(final PragmaNode p : node.getPragmas()){
                p.accept(this);
            }
            for(D d : node.getDirectives()){
                d.accept(this);
            }
            return null;
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(RecordTypeNode<T> node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(SemicolonNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(StringLiteralNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeDefinitionNode<T> node) {
            node.getIdentifier().accept(this);
            return null;
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeExpressionNode<T> node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(TypeNameNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(UnionTypeNode<T> node) {
            throw new AssertionError("unused");
        }
    }

    public class PassTwo implements NodeVisitor<Type> {
        @Override
        public Type visit(AnyTypeNode node) {
            return anyType;
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(ArrayTypeNode<T> node) {
            final ArrayList<Type> fixedLengthElements = new ArrayList<>();
            for(final TypeExpressionNode<T> e : node.getElementTypeList()){
                fixedLengthElements.add(e.accept(this));
            }
            final TypeExpressionNode<T> vaTypes = node.getVariableArrayType();
            final Type variableLengthElements = vaTypes == null ? null : vaTypes.accept(this);
            ArrayType array = new ArrayType(fixedLengthElements, variableLengthElements);
            return array;
        }

        @Override
        public Type visit(FieldNameNode.StringLiteral node) {
            throw new AssertionError("unused");
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(FieldTypeNode<T> node) {
            final String name = ((FieldNameNode.StringLiteral)node.getName()).getString();
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
        public Type visit(PragmaNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public <D extends AstNode & DirectiveNode<D>> Type visit(ProgramNode<D> node) {
            for(D d : node.getDirectives()){
                d.accept(this);
            }
            //TODO JSON-DSのパースが完了した後にUndefindTypeな識別子が存在した場合の仕様が決まっていない
            for(final Map.Entry<String, Type> e : JsonDsProcessorHoistableTestDriver.this.metaObjects.registerEntrySet()){
                if(e.getValue() == undefindType){
                    throw new JsonDsException("undefined type" + e.getKey(), StandardErrors.TypeError);
                }
            }
            return null;
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(RecordTypeNode<T> node) {
            HashSet<RecordType.Field> fields = new HashSet<>();
            for(final FieldTypeNode<T> f : node.getFieldTypeList()){
                fields.add((RecordType.Field) f.accept(this));
            }
            return new RecordType(fields);
        }

        @Override
        public Type visit(SemicolonNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public Type visit(StringLiteralNode node) {
            throw new AssertionError("unused");
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeDefinitionNode<T> node) {
            node.getIdentifier().accept(this);
            Type init = node.getTypeInitialization().accept(this);
            metaObjects.registerMetaObject(node.getIdentifier().getString(), init);
            return null;
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(TypeExpressionNode<T> node) {
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
        public Type visit(TypeNameNode node) {
            final Type metaObject = node.getNameExpression().accept(this);
            if (metaObject == null) {
                throw new JsonDsException(node.getString() + "が未定義です", StandardErrors.ReferenceError);
            }
            return (metaObject != undefindType) ? metaObject : new ReferenceType(metaObjects, node.getString());
        }

        @Override
        public <T extends AstNode & BasicTypeExpressionNode<T>> Type visit(UnionTypeNode<T> node) {
            HashSet<Type> members = new HashSet<>();
            for(final TypeExpressionNode<T> te : node.getTypeUnionList()){
                members.add(te.accept(this));
            }
            return new UnionType(members);
        }
    }

    protected Mode complianceMode;
    protected URL sourceName;
    private final JsonMetaObjectTestDriver metaObjects;

    public JsonDsProcessorHoistableTestDriver(JsonMetaObjectTestDriver metaObjects) {
        this.metaObjects = metaObjects;
    }

    public JsonDsProcessorHoistableTestDriver() {
        this(new JsonMetaObjectTestDriver());
    }

    @Override
    public void process(String jsds, URL sourceName) throws ParserException, JsonDsException {
        processImpl(new Source(jsds), sourceName);
    }

    @Override
    public void process(Reader jsds, URL sourceName) throws ParserException, JsonDsException {
        processImpl(new Source(jsds), sourceName);
    }

    protected void processImpl(Source source, URL sourceName) throws ParserException, JsonDsException {
        this.sourceName = sourceName;
        final TokenStream ts = new TokenStream(source);
        final Parser parser = new Parser(ts, sourceName != null ? sourceName.toString() : "");
        final ProgramNode<?> p = parser.parse();
        PassOne one = new PassOne();
        PassTwo two = new PassTwo();
        one.visit(p);
        two.visit(p);
    }

    public final Mode getComplianceMode() {
        return complianceMode;
    }

    public final JsonMetaObjectTestDriver getMetaObjects() {
        return metaObjects;
    }
}
